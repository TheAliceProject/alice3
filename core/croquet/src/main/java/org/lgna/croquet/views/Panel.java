/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class Panel extends CompositeView<javax.swing.JPanel, org.lgna.croquet.Composite<?>> {
	protected class DefaultJPanel extends javax.swing.JPanel {
		public DefaultJPanel() {
			this.setOpaque( false );
			this.setBackground( null );
			this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
			this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		}

		@Override
		public void invalidate() {
			super.invalidate();
			Panel.this.refreshIfNecessary();
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
		}

		@Override
		public java.awt.Dimension getMaximumSize() {
			java.awt.Dimension rv = super.getMaximumSize();
			if( Panel.this.isMaximumSizeClampedToPreferredSize() ) {
				rv.setSize( this.getPreferredSize() );
			}
			return rv;
		}
	}

	public Panel() {
		this( null );
	}

	public Panel( org.lgna.croquet.Composite<?> composite ) {
		super( composite );
	}

	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel();
	}

	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );

	@Override
	protected final javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = this.createJPanel();
		java.awt.LayoutManager prevLayoutManager = rv.getLayout();
		java.awt.LayoutManager nextLayoutManager = this.createLayoutManager( rv );
		if( prevLayoutManager instanceof java.awt.FlowLayout ) {
			//pass
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append( "\n********************************************************" );
			sb.append( "\n********************************************************" );
			sb.append( "\n********************************************************" );
			sb.append( "\n\tIt appears that a layout manager was set on:\n\t\t" );
			sb.append( this );
			sb.append( "\n\tduring the creation of its JPanel.\n\n\t" );
			sb.append( prevLayoutManager );
			sb.append( ";hashCode=" );
			sb.append( Integer.toHexString( prevLayoutManager.hashCode() ) );
			sb.append( " will be replaced by " );
			sb.append( nextLayoutManager );
			sb.append( ";hashCode=" );
			sb.append( Integer.toHexString( nextLayoutManager.hashCode() ) );
			sb.append( "." );
			if( rv.getComponentCount() > 0 ) {
				sb.append( "\n\n\tOf great concern is the fact that you have already added " );
				sb.append( rv.getComponentCount() );
				sb.append( " components which may not get laid out." );
			}
			sb.append( "\n********************************************************" );
			sb.append( "\n********************************************************" );
			sb.append( "\n********************************************************" );
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( sb.toString() );
		}
		rv.setLayout( nextLayoutManager );
		return rv;
	}

	public void removeComponent( AwtComponentView<?> component ) {
		this.internalRemoveComponent( component );
	}

	public void forgetAndRemoveComponent( AwtComponentView<?> component ) {
		this.internalForgetAndRemoveComponent( component );
	}

	public void removeAllComponents() {
		this.internalRemoveAllComponents();
	}

	public void forgetAndRemoveAllComponents() {
		this.internalForgetAndRemoveAllComponents();
	}

	private boolean isInTheMidstOfRefreshing = false;
	private boolean isRefreshNecessary = true;

	protected void internalRefresh() {
	}

	protected void refreshIfNecessary() {
		if( this.isRefreshNecessary ) {
			if( this.isInTheMidstOfRefreshing ) {
				//pass
			} else {
				this.isInTheMidstOfRefreshing = true;
				try {
					//this.forgetAndRemoveAllComponents();
					synchronized( this.getTreeLock() ) {
						this.internalRefresh();
					}
					this.isRefreshNecessary = false;
				} finally {
					this.isInTheMidstOfRefreshing = false;
				}
			}
		}
	}

	public final void refreshLater() {
		this.isRefreshNecessary = true;
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleDisplayable() {
		this.refreshIfNecessary();
		super.handleDisplayable();
	}

	protected boolean isRefreshOnAddedToDesired() {
		return false;
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		if( this.isRefreshOnAddedToDesired() ) {
			this.refreshIfNecessary();
		}
		super.handleAddedTo( parent );
	}
}

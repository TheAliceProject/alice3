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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeItem<F, B> extends MenuItemPrepModel implements CascadeBlankChild<F> {
	private transient boolean isDirty = true;
	private transient javax.swing.JComponent menuProxy = null;
	private transient javax.swing.Icon icon = null;

	public CascadeItem( java.util.UUID id ) {
		super( id );
	}

	@Override
	public int getItemCount() {
		return 1;
	}

	@Override
	public org.lgna.croquet.CascadeItem<F, B> getItemAt( int index ) {
		assert index == 0;
		return this;
	}

	public abstract F getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node );

	public abstract F createValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node, org.lgna.croquet.history.TransactionHistory transactionHistory );

	protected abstract javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node );

	@Override
	protected void localize() {
		this.isDirty = true;
	}

	protected boolean isDirty() {
		return this.isDirty;
	}

	protected void markClean() {
		this.isDirty = false;
	}

	public void markDirty() {
		this.isDirty = true;
	}

	protected javax.swing.JComponent getMenuProxy( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		if( this.menuProxy != null ) {
			//pass
		} else {
			this.menuProxy = this.createMenuItemIconProxy( node );
		}
		return this.menuProxy;
	}

	//todo:
	private static void setBoxLayoutComponentOrientationTree( java.awt.Component c, java.awt.ComponentOrientation componentOrientation ) {
		if( c instanceof javax.swing.JPanel ) {
			javax.swing.JPanel jPanel = (javax.swing.JPanel)c;
			java.awt.LayoutManager layoutManager = jPanel.getLayout();
			if( layoutManager instanceof javax.swing.BoxLayout ) {
				//javax.swing.BoxLayout boxLayout = (javax.swing.BoxLayout)layoutManager;
				c.setComponentOrientation( componentOrientation );
			}
		}
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				setBoxLayoutComponentOrientationTree( component, componentOrientation );
			}
		}
	}

	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		if( this.isDirty() ) {
			this.icon = null;
			this.menuProxy = null;
		}
		if( this.icon != null ) {
			//pass
		} else {
			javax.swing.JComponent component = this.getMenuProxy( node );
			if( component != null ) {
				final boolean IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK = true;
				java.awt.ComponentOrientation componentOrientation = component.getComponentOrientation();
				if( componentOrientation.isLeftToRight() ) {
					//pass
				} else {
					if( IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK ) {
						setBoxLayoutComponentOrientationTree( component, java.awt.ComponentOrientation.LEFT_TO_RIGHT );
					}
				}

				edu.cmu.cs.dennisc.java.awt.ComponentUtilities.invalidateTree( component );
				edu.cmu.cs.dennisc.java.awt.ComponentUtilities.doLayoutTree( component );
				edu.cmu.cs.dennisc.java.awt.ComponentUtilities.setSizeToPreferredSizeTree( component );

				if( componentOrientation.isLeftToRight() ) {
					//pass
				} else {
					if( IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK ) {
						setBoxLayoutComponentOrientationTree( component, componentOrientation );
					}
				}

				java.awt.Dimension size = component.getPreferredSize();
				if( ( size.width > 0 ) && ( size.height > 0 ) ) {
					this.icon = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createIcon( component );
				} else {
					this.icon = null;
				}
			} else {
				this.icon = null;
			}
			this.markClean();
		}
		return this.icon;
	}

	public String getMenuItemText( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
		return null;
	}
}

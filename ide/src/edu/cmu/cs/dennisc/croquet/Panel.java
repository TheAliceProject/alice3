/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Panel extends JComponent< javax.swing.JPanel > {
	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );
	protected class DefaultJPanel extends javax.swing.JPanel {
		public DefaultJPanel() {
			this.setOpaque( false );
			this.setBackground( null );
			this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
			this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			java.awt.Dimension rv = super.getPreferredSize();
			if( minimumPreferredWidth != null ) {
				rv.width = Math.max( rv.width, minimumPreferredWidth );
			}
			if( maximumPreferredWidth != null ) {
				rv.width = Math.min( rv.width, maximumPreferredWidth );
			}
			if( minimumPreferredHeight != null ) {
				rv.height = Math.max( rv.height, minimumPreferredHeight );
			}
			if( maximumPreferredHeight != null ) {
				rv.height = Math.min( rv.height, maximumPreferredHeight );
			}
			return rv;
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			if( Panel.this.isMaximumSizeClampedToPreferredSize() ) {
				return this.getPreferredSize();
			} else {
				return super.getMaximumSize();
			}
		}
	}
	protected boolean isMaximumSizeClampedToPreferredSize() {
		return false;
	}
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel();
	}
	
	
	//todo: move to Component<?>
	private Integer minimumPreferredWidth = null;
	private Integer maximumPreferredWidth = null;
	private Integer minimumPreferredHeight = null;
	private Integer maximumPreferredHeight = null;
	
	public final Integer getMaximumPreferredWidth() {
		return this.maximumPreferredWidth;
	}
	public final void setMaximumPreferredWidth( Integer maximumPreferredWidth ) {
		this.maximumPreferredWidth = maximumPreferredWidth;
	}
	public final Integer getMinimumPreferredWidth() {
		return this.minimumPreferredWidth;
	}
	public final void setMinimumPreferredWidth( Integer minimumPreferredWidth ) {
		this.minimumPreferredWidth = minimumPreferredWidth;
	}
	public final Integer getMaximumPreferredHeight() {
		return this.maximumPreferredHeight;
	}
	public final void setMaximumPreferredHeight( Integer maximumPreferredHeight ) {
		this.maximumPreferredHeight = maximumPreferredHeight;
	}
	public final Integer getMinimumPreferredHeight() {
		return this.minimumPreferredHeight;
	}
	public final void setMinimumPreferredHeight( Integer minimumPreferredHeight ) {
		this.minimumPreferredHeight = minimumPreferredHeight;
	}
	
	@Override
	protected final javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = this.createJPanel();
		java.awt.LayoutManager layoutManager = this.createLayoutManager( rv );
		rv.setLayout( layoutManager );
		return rv;
	}
	@Override
	public void setBackgroundColor( java.awt.Color color ) {
		super.setBackgroundColor( color );
		this.getAwtComponent().setOpaque( color != null );
	}
	public void removeComponent( Component< ? > component ) {
		this.internalRemoveComponent( component );
	}
	public void forgetAndRemoveComponent( Component< ? > component ) {
		this.internalForgetAndRemoveComponent( component );
	}
	public void removeAllComponents() {
		this.internalRemoveAllComponents();
	}
	public void forgetAndRemoveAllComponents() {
		this.internalForgetAndRemoveAllComponents();
	}
}

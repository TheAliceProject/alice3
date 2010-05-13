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
public abstract class SpringPanel extends Panel {
	public enum Vertical {
		NORTH(javax.swing.SpringLayout.NORTH), 
		CENTER(null), 
		SOUTH(javax.swing.SpringLayout.SOUTH);
		private String internal;
		private Vertical( String internal ) {
			this.internal = internal;
		}
		public String getInternal() {
			return internal;
		}
	}
	public enum Horizontal {
		WEST(javax.swing.SpringLayout.WEST), 
		CENTER(null), 
		EAST(javax.swing.SpringLayout.EAST);
		private String internal;
		private Horizontal( String internal ) {
			this.internal = internal;
		}
		public String getInternal() {
			return internal;
		}
	}
	private javax.swing.SpringLayout springLayout = new javax.swing.SpringLayout();
	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return this.springLayout;
	}
//	protected javax.swing.SpringLayout getSpringLayout() {
//		return this.springLayout;
//	}
	abstract class CenterSpring extends javax.swing.Spring {
		private Component< ? > component;
		private int offset;
		public CenterSpring( Component< ? > component, int offset ) {
			this.component = component;
			this.offset = offset;
		}
		@Override
		public int getValue() {
			return this.getPreferredValue();
		}
		@Override
		public void setValue( int value ) {
		}
		protected abstract int getValue( java.awt.Dimension dimension );
		@Override
		public int getPreferredValue() {
			int macro = getValue( SpringPanel.this.getAwtComponent().getSize() );
			java.awt.Dimension size;
			if( this.component.getAwtComponent().isValid() ) {
				size = this.component.getAwtComponent().getSize();
			} else {
				size = this.component.getAwtComponent().getPreferredSize();
			}
			int micro = getValue( size );
			return this.offset + (macro - micro) / 2;
		}
		@Override
		public int getMinimumValue() {
			return this.getPreferredValue();
		}
		@Override
		public int getMaximumValue() {
			return this.getPreferredValue();
		}
	}
	class HorizontalCenterSpring extends CenterSpring {
		public HorizontalCenterSpring( Component< ? > component, int offset ) {
			super( component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.width;
		}
	}
	class VerticalCenterSpring extends CenterSpring {
		public VerticalCenterSpring( Component< ? > component, int offset ) {
			super( component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.height;
		}
	}
	private void putConstraint( Component< ? > component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		String horizontalConstraint = horizontal.getInternal();
		String verticalConstraint = vertical.getInternal();
		if( horizontalConstraint != null ) {
			this.springLayout.putConstraint( horizontalConstraint, component.getAwtComponent(), x, horizontalConstraint, this.getAwtComponent() );
		} else {
			this.springLayout.putConstraint( javax.swing.SpringLayout.WEST, component.getAwtComponent(), new HorizontalCenterSpring( component, x ), javax.swing.SpringLayout.WEST, this.getAwtComponent() );
		}
		if( verticalConstraint != null ) {
			this.springLayout.putConstraint( verticalConstraint, component.getAwtComponent(), y, verticalConstraint, this.getAwtComponent() );
		} else {
			this.springLayout.putConstraint( javax.swing.SpringLayout.NORTH, component.getAwtComponent(), new VerticalCenterSpring( component, y ), javax.swing.SpringLayout.NORTH, this.getAwtComponent() );
		}
	}
	
	public void addComponent( Component< ? > component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		this.putConstraint( component, horizontal, x, vertical, y );
		this.internalAddComponent( component );
	}
	public void removeComponent( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		this.internalRemoveComponent( component );
		this.springLayout.removeLayoutComponent( component.getAwtComponent() );
	}
}

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
public class BorderPanel extends Panel {
	public static class Builder {
		private org.lgna.croquet.Composite<?> composite;
		private AwtComponentView<?> pageStart;
		private AwtComponentView<?> pageEnd;
		private AwtComponentView<?> center;
		private AwtComponentView<?> lineStart;
		private AwtComponentView<?> lineEnd;
		private int hgap;
		private int vgap;

		public Builder composite( org.lgna.croquet.Composite<?> composite ) {
			assert this.composite == null : this.composite + " " + composite;
			this.composite = composite;
			return this;
		}

		public Builder center( AwtComponentView<?> center ) {
			assert center != null;
			assert this.center == null : this.center + " " + center;
			this.center = center;
			return this;
		}

		public Builder pageStart( AwtComponentView<?> pageStart ) {
			assert pageStart != null;
			assert this.pageStart == null : this.pageStart + " " + pageStart;
			this.pageStart = pageStart;
			return this;
		}

		public Builder pageEnd( AwtComponentView<?> pageEnd ) {
			assert pageEnd != null;
			assert this.pageEnd == null : this.pageEnd + " " + pageEnd;
			this.pageEnd = pageEnd;
			return this;
		}

		public Builder lineStart( AwtComponentView<?> lineStart ) {
			assert lineStart != null;
			assert this.lineStart == null : this.lineStart + " " + lineStart;
			this.lineStart = lineStart;
			return this;
		}

		public Builder lineEnd( AwtComponentView<?> lineEnd ) {
			assert lineEnd != null;
			assert this.lineEnd == null : this.lineEnd + " " + lineEnd;
			this.lineEnd = lineEnd;
			return this;
		}

		public Builder hgap( int hgap ) {
			assert this.hgap == 0 : this.hgap + " " + hgap;
			this.hgap = hgap;
			return this;
		}

		public Builder vgap( int vgap ) {
			assert this.vgap == 0 : this.vgap + " " + vgap;
			this.vgap = vgap;
			return this;
		}

		public BorderPanel build() {
			BorderPanel rv = new BorderPanel( this.composite, this.hgap, this.vgap );
			if( this.center != null ) {
				rv.addComponent( this.center, Constraint.CENTER );
			}
			if( this.pageStart != null ) {
				rv.addComponent( this.pageStart, Constraint.PAGE_START );
			}
			if( this.pageEnd != null ) {
				rv.addComponent( this.pageEnd, Constraint.PAGE_END );
			}
			if( this.lineStart != null ) {
				rv.addComponent( this.lineStart, Constraint.LINE_START );
			}
			if( this.lineEnd != null ) {
				rv.addComponent( this.lineEnd, Constraint.LINE_END );
			}
			return rv;
		}
	}

	public enum Constraint {
		CENTER( java.awt.BorderLayout.CENTER ),
		PAGE_START( java.awt.BorderLayout.PAGE_START ),
		PAGE_END( java.awt.BorderLayout.PAGE_END ),
		LINE_START( java.awt.BorderLayout.LINE_START ),
		LINE_END( java.awt.BorderLayout.LINE_END );

		private String internal;

		private Constraint( String internal ) {
			this.internal = internal;
		}

		//todo: reduce visibility? /*package-private*/
		public String getInternal() {
			return this.internal;
		}
	}

	private final int hgap;
	private final int vgap;

	public BorderPanel() {
		this( null );
	}

	public BorderPanel( int hgap, int vgap ) {
		this( null, hgap, vgap );
	}

	public BorderPanel( org.lgna.croquet.Composite<?> composite ) {
		this( composite, 0, 0 );
	}

	public BorderPanel( org.lgna.croquet.Composite<?> composite, int hgap, int vgap ) {
		super( composite );
		this.hgap = hgap;
		this.vgap = vgap;
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout( this.hgap, this.vgap );
	}

	public void addComponent( AwtComponentView<?> child, Constraint constraint ) {
		this.internalAddComponent( child, constraint.internal );
	}

	public void addCenterComponent( AwtComponentView<?> child ) {
		this.addComponent( child, Constraint.CENTER );
	}

	public void addPageStartComponent( AwtComponentView<?> child ) {
		this.addComponent( child, Constraint.PAGE_START );
	}

	public void addPageEndComponent( AwtComponentView<?> child ) {
		this.addComponent( child, Constraint.PAGE_END );
	}

	public void addLineStartComponent( AwtComponentView<?> child ) {
		this.addComponent( child, Constraint.LINE_START );
	}

	public void addLineEndComponent( AwtComponentView<?> child ) {
		this.addComponent( child, Constraint.LINE_END );
	}

	public AwtComponentView<?> getComponent( Constraint constraint ) {
		javax.swing.JPanel jPanel = this.getAwtComponent();
		java.awt.BorderLayout borderLayout = (java.awt.BorderLayout)jPanel.getLayout();
		for( AwtComponentView<?> component : this.getComponents() ) {
			java.awt.Component awtComponent = component.getAwtComponent();
			if( constraint.internal.equals( borderLayout.getConstraints( awtComponent ) ) ) {
				return component;
			}
		}
		return null;
	}

	public AwtComponentView<?> getCenterComponent() {
		return this.getComponent( Constraint.CENTER );
	}

	public AwtComponentView<?> getPageStartComponent() {
		return this.getComponent( Constraint.PAGE_START );
	}

	public AwtComponentView<?> getPageEndComponent() {
		return this.getComponent( Constraint.PAGE_END );
	}

	public AwtComponentView<?> getLineStartComponent() {
		return this.getComponent( Constraint.LINE_START );
	}

	public AwtComponentView<?> getLineEndComponent() {
		return this.getComponent( Constraint.LINE_END );
	}
}

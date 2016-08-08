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
public class FlowPanel extends Panel {
	public enum Alignment {
		CENTER( java.awt.FlowLayout.CENTER ),

		LEFT( java.awt.FlowLayout.LEFT ),
		LEADING( java.awt.FlowLayout.LEADING ),

		RIGHT( java.awt.FlowLayout.RIGHT ),
		TRAILING( java.awt.FlowLayout.TRAILING );

		private int internal;

		private Alignment( int internal ) {
			this.internal = internal;
		}
	}

	private final Alignment alignment;
	private final int hgap;
	private final int vgap;

	public FlowPanel( AwtComponentView<?>... components ) {
		this( (org.lgna.croquet.Composite)null, components );
	}

	public FlowPanel( Alignment alignment, AwtComponentView<?>... components ) {
		this( null, alignment, components );
	}

	public FlowPanel( Alignment alignment, int hgap, int vgap, AwtComponentView<?>... components ) {
		this( null, alignment, hgap, vgap, components );
	}

	public FlowPanel( org.lgna.croquet.Composite composite, AwtComponentView<?>... components ) {
		this( composite, Alignment.CENTER, components );
	}

	public FlowPanel( org.lgna.croquet.Composite composite, Alignment alignment, AwtComponentView<?>... components ) {
		this( composite, alignment, 0, 0, components );
	}

	public FlowPanel( org.lgna.croquet.Composite composite, Alignment alignment, int hgap, int vgap, AwtComponentView<?>... components ) {
		super( composite );
		this.alignment = alignment;
		this.hgap = hgap;
		this.vgap = vgap;
		for( AwtComponentView<?> component : components ) {
			this.addComponent( component );
		}
	}

	//	@Override
	//	protected javax.swing.JPanel createJPanel() {
	//		class FlowJPanel extends DefaultJPanel {
	////			@Override
	////			public java.awt.Dimension getPreferredSize() {
	////				java.awt.Dimension rv = super.getPreferredSize();
	////				rv.width = this.getWidth();
	////				return rv;
	////			}
	//			@Override
	//			public java.awt.Dimension getMaximumSize() {
	//				return this.getPreferredSize();
	//			}
	//		};
	//		FlowJPanel rv = new FlowJPanel();
	////		rv.setPreferredSize( new java.awt.Dimension( 256, 1 ) );
	//		return rv;
	//	}
	protected java.awt.FlowLayout createFlowLayout( int alignment, int hgap, int vgap ) {
		return new java.awt.FlowLayout( alignment, hgap, vgap );
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return this.createFlowLayout( this.alignment.internal, this.hgap, this.vgap );
	}

	public void addComponent( AwtComponentView<?> component ) {
		this.internalAddComponent( component );
	}
}

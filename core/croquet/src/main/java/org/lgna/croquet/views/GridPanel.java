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
public class GridPanel extends Panel {
	private final int rowCount;
	private final int columnCount;
	private final int hgap;
	private final int vgap;

	public static class Details {
		private org.lgna.croquet.Composite<?> composite;
		private int rowCount;
		private int columnCount;
		private int hgap;
		private int vgap;

		public Details() {
		}

		public Details composite( org.lgna.croquet.Composite<?> composite ) {
			this.composite = composite;
			return this;
		}

		public Details rowCount( int rowCount ) {
			this.rowCount = rowCount;
			return this;
		}

		public Details columnCount( int columnCount ) {
			this.columnCount = columnCount;
			return this;
		}

		public Details hgap( int hgap ) {
			this.hgap = hgap;
			return this;
		}

		public Details vgap( int vgap ) {
			this.vgap = vgap;
			return this;
		}
	}

	public GridPanel( Details details, AwtComponentView<?>... components ) {
		super( details.composite );
		this.rowCount = details.rowCount;
		this.columnCount = details.columnCount;
		this.hgap = details.hgap;
		this.vgap = details.vgap;
		for( AwtComponentView<?> component : components ) {
			this.addComponent( component );
		}
	}

	public static GridPanel createSingleRowGridPane( org.lgna.croquet.Composite<?> composite, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).rowCount( 1 ), components );
	}

	public static GridPanel createSingleRowGridPane( org.lgna.croquet.Composite<?> composite, int hgap, int vgap, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).rowCount( 1 ).hgap( hgap ).vgap( vgap ), components );
	}

	public static GridPanel createSingleColumnGridPane( org.lgna.croquet.Composite<?> composite, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).columnCount( 1 ), components );
	}

	public static GridPanel createSingleColumnGridPane( org.lgna.croquet.Composite<?> composite, int hgap, int vgap, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).columnCount( 1 ).hgap( hgap ).vgap( vgap ), components );
	}

	public static GridPanel createGridPane( org.lgna.croquet.Composite<?> composite, int rowCount, int columnCount, int hgap, int vgap, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).rowCount( rowCount ).columnCount( columnCount ).hgap( hgap ).vgap( vgap ), components );
	}

	public static GridPanel createGridPane( org.lgna.croquet.Composite<?> composite, int rowCount, int columnCount, AwtComponentView<?>... components ) {
		return new GridPanel( new Details().composite( composite ).rowCount( rowCount ).columnCount( columnCount ), components );
	}

	public static GridPanel createSingleRowGridPane( AwtComponentView<?>... components ) {
		return createSingleRowGridPane( null, components );
	}

	public static GridPanel createSingleRowGridPane( int hgap, int vgap, AwtComponentView<?>... components ) {
		return createSingleRowGridPane( null, hgap, vgap, components );
	}

	public static GridPanel createSingleColumnGridPane( AwtComponentView<?>... components ) {
		return createSingleColumnGridPane( null, components );
	}

	public static GridPanel createSingleColumnGridPane( int hgap, int vgap, AwtComponentView<?>... components ) {
		return createSingleColumnGridPane( null, hgap, vgap, components );
	}

	public static GridPanel createGridPane( int rowCount, int columnCount, int hgap, int vgap, AwtComponentView<?>... components ) {
		return createGridPane( null, rowCount, columnCount, hgap, vgap, components );
	}

	public static GridPanel createGridPane( int rowCount, int columnCount, AwtComponentView<?>... components ) {
		return createGridPane( null, rowCount, columnCount, components );
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.GridLayout( this.rowCount, this.columnCount, this.hgap, this.vgap );
	}

	public void addComponent( AwtComponentView<?> component ) {
		this.internalAddComponent( component );
	}
}

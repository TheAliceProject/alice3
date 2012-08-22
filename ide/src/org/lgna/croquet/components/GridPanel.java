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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class GridPanel extends Panel {
	private static final int DEFAULT_HGAP = 0;
	private static final int DEFAULT_VGAP = 0;
	private int rowCount;
	private int columnCount;
	private int hgap;
	private int vgap;

	private GridPanel( org.lgna.croquet.Composite composite, int rowCount, int columnCount, int hgap, int vgap, Component<?>[] components ) {
		super( composite );
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.hgap = hgap;
		this.vgap = vgap;
		for( Component<?> component : components ) {
			this.addComponent( component );
		}
	}

	public static GridPanel createSingleRowGridPane( org.lgna.croquet.Composite composite, Component<?>... components ) {
		return new GridPanel( composite, 1, 0, DEFAULT_HGAP, DEFAULT_VGAP, components );
	}

	public static GridPanel createSingleRowGridPane( org.lgna.croquet.Composite composite, int hgap, int vgap, Component<?>... components ) {
		return new GridPanel( composite, 1, 0, hgap, vgap, components );
	}

	public static GridPanel createSingleColumnGridPane( org.lgna.croquet.Composite composite, Component<?>... components ) {
		return new GridPanel( composite, 0, 1, DEFAULT_HGAP, DEFAULT_VGAP, components );
	}

	public static GridPanel createSingleColumnGridPane( org.lgna.croquet.Composite composite, int hgap, int vgap, Component<?>... components ) {
		return new GridPanel( composite, 0, 1, hgap, vgap, components );
	}

	public static GridPanel createGridPane( org.lgna.croquet.Composite composite, int rowCount, int columnCount, int hgap, int vgap, Component<?>... components ) {
		return new GridPanel( composite, rowCount, columnCount, hgap, vgap, components );
	}

	public static GridPanel createGridPane( org.lgna.croquet.Composite composite, int rowCount, int columnCount, Component<?>... components ) {
		return new GridPanel( composite, rowCount, columnCount, DEFAULT_HGAP, DEFAULT_VGAP, components );
	}

	public static GridPanel createSingleRowGridPane( Component<?>... components ) {
		return createSingleRowGridPane( null, components );
	}

	public static GridPanel createSingleRowGridPane( int hgap, int vgap, Component<?>... components ) {
		return createSingleRowGridPane( null, hgap, vgap, components );
	}

	public static GridPanel createSingleColumnGridPane( Component<?>... components ) {
		return createSingleColumnGridPane( null, components );
	}

	public static GridPanel createSingleColumnGridPane( int hgap, int vgap, Component<?>... components ) {
		return createSingleColumnGridPane( null, hgap, vgap, components );
	}

	public static GridPanel createGridPane( int rowCount, int columnCount, int hgap, int vgap, Component<?>... components ) {
		return createGridPane( null, rowCount, columnCount, hgap, vgap, components );
	}

	public static GridPanel createGridPane( int rowCount, int columnCount, Component<?>... components ) {
		return createGridPane( null, rowCount, columnCount, DEFAULT_HGAP, DEFAULT_VGAP, components );
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.GridLayout( this.rowCount, this.columnCount, this.hgap, this.vgap );
	}

	public void addComponent( Component<?> component ) {
		this.internalAddComponent( component );
	}
}

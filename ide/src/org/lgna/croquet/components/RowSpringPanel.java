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
public abstract class RowSpringPanel extends SpringPanel {
	private java.util.List<SpringRow> rows;
	private final int xPad;
	private final int yPad;

	protected abstract void appendRows( java.util.List<SpringRow> rows );

	public RowSpringPanel( org.lgna.croquet.Composite<?> composite ) {
		this( composite, 12, 12 );
	}

	public RowSpringPanel( org.lgna.croquet.Composite<?> composite, int xPad, int yPad ) {
		super( composite );
		this.xPad = xPad;
		this.yPad = yPad;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
	}

	public RowSpringPanel() {
		this( null );
	}

	public RowSpringPanel( int xPad, int yPad ) {
		this( null, xPad, yPad );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		if( this.rows != null ) {
			//pass
		} else {
			this.rows = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.appendRows( this.rows );
			java.util.List<Component<?>[]> components = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( rows.size() );
			for( SpringRow row : rows ) {
				components.add( row.createComponentArray() );
			}
			SpringUtilities.springItUpANotch( this, components, this.xPad, this.yPad );
		}
	}

	@Override
	protected void handleUndisplayable() {
		//todo?
		super.handleUndisplayable();
	};
}

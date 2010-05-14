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
public class List<E> extends ItemSelectable< E, javax.swing.JList > {
	public enum LayoutOrientation {
		VERTICAL ( javax.swing.JList.VERTICAL ),
		VERTICAL_WRAP( javax.swing.JList.VERTICAL_WRAP ),
		HORIZONTAL_WRAP( javax.swing.JList.HORIZONTAL_WRAP );
		private int internal;
		private LayoutOrientation( int internal ) {
			this.internal = internal;
		}
//		/*package-private*/ int getInternal() {
//			return this.internal;
//		}
	}
	
	/*package-private*/ public List() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of List constructor" );
	}

	@Override
	protected javax.swing.JList createAwtComponent() {
		return new javax.swing.JList();
	}
	
	@Override
	public javax.swing.ListCellRenderer getRenderer() {
		return this.getAwtComponent().getCellRenderer();
	}
	@Override
	public void setRenderer( javax.swing.ListCellRenderer listCellRenderer ) {
		this.getAwtComponent().setCellRenderer( listCellRenderer );
	}


	public int getVisibleRowCount() {
		return this.getAwtComponent().getVisibleRowCount();
	}
	public void setVisibleRowCount( int visibleRowCount ) {
		this.getAwtComponent().setVisibleRowCount( visibleRowCount );
	}
	public void setLayoutOrientation( LayoutOrientation layoutOrientation ) {
		this.getAwtComponent().setLayoutOrientation( layoutOrientation.internal );
	}

	@Override
	/*package-private*/ void setModel( javax.swing.ComboBoxModel model ) {
		this.getAwtComponent().setModel( model );
	}
	/*package-private*/ void addListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getAwtComponent().addListSelectionListener( listSelectionListener );
	}
	/*package-private*/ void removeListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getAwtComponent().removeListSelectionListener( listSelectionListener );
	}

	@Deprecated
	public void setListData( E... values ) {
		this.getAwtComponent().setListData( values );
	}
	@Deprecated
	public void setListData( java.util.Vector<E> values ) {
		this.getAwtComponent().setListData( values );
	}
	@Deprecated
	public E getSelectedValue() {
		return (E)this.getAwtComponent().getSelectedValue();
	}
	@Deprecated
	public void setSelectedIndex( int index ) {
		this.getAwtComponent().setSelectedIndex( index );
	}
	@Deprecated
	public void setSelectedValue( E value, boolean shouldScroll ) {
		this.getAwtComponent().setSelectedValue( value, shouldScroll );
	}
	@Deprecated
	public void clearSelection() {
		this.getAwtComponent().clearSelection();
	}
	
//	@Deprecated
//	public void setItemSelectionOperation( ItemSelectionOperation< E > operation ) {
//		throw new RuntimeException( "todo" );
//	}
}

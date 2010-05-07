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
public class KList<E> extends KComponent< javax.swing.JList > {
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
	@Override
	protected javax.swing.JList createJComponent() {
		return new javax.swing.JList();
	}
	
	@Deprecated
	public javax.swing.ListModel getModel() {
		return this.getJComponent().getModel();
	}
	@Deprecated
	public void setModel( javax.swing.ListModel model ) {
		this.getJComponent().setModel( model );
	}
	@Deprecated
	public void setListData( E... values ) {
		this.getJComponent().setListData( values );
	}

	public javax.swing.ListCellRenderer getCellRenderer() {
		return this.getJComponent().getCellRenderer();
	}
	public void setCellRenderer( javax.swing.ListCellRenderer listCellRenderer ) {
		this.getJComponent().setCellRenderer( listCellRenderer );
	}

	@Deprecated
	public E getSelectedValue() {
		return (E)this.getJComponent().getSelectedValue();
	}
	@Deprecated
	public void setSelectedIndex( int index ) {
		this.getJComponent().setSelectedIndex( index );
	}
	@Deprecated
	public void setSelectedValue( E value, boolean shouldScroll ) {
		this.getJComponent().setSelectedValue( value, shouldScroll );
	}
	
	public int getVisibleRowCount() {
		return this.getJComponent().getVisibleRowCount();
	}
	public void setVisibleRowCount( int visibleRowCount ) {
		this.getJComponent().setVisibleRowCount( visibleRowCount );
	}
	
	public void setLayoutOrientation( LayoutOrientation layoutOrientation ) {
		this.getJComponent().setLayoutOrientation( layoutOrientation.internal );
	}

	@Deprecated
	public void addListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getJComponent().addListSelectionListener( listSelectionListener );
	}
	@Deprecated
	public void removeListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getJComponent().removeListSelectionListener( listSelectionListener );
	}
	
	@Deprecated
	public void setItemSelectionOperation( ItemSelectionOperation< E > operation ) {
		throw new RuntimeException( "todo" );
	}
}

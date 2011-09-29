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
public class ItemDetails<E,D extends ItemDetails<E,D,J>, J extends ItemSelectablePanel<E,D>>  {
	private final J panel;
	private final E item;
	private final BooleanStateButton< ? extends javax.swing.AbstractButton > button;
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if( ItemDetails.this.panel.getSwingComboBoxModel().getSelectedItem() != ItemDetails.this.item ) {
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
					int index = ItemDetails.this.panel.getModel().indexOf( item );
					ItemDetails.this.panel.getSwingListSelectionModel().setSelectionInterval( index, index );
				}
			}
		}
	};
	public ItemDetails( J panel, E item, BooleanStateButton<? extends javax.swing.AbstractButton> button ) {
		this.panel = panel;
		this.item = item;
		this.button = button;
	}
	public J getPanel() {
		return this.panel;
	}
	public E getItem() {
		return this.item;
	}
	public TrackableShape getTrackableShape() {
		return this.getButton();
	}
	public BooleanStateButton< ? extends javax.swing.AbstractButton > getButton() {
		return this.button;
	}
	public void add( javax.swing.ButtonGroup buttonGroup ) {
		this.button.getAwtComponent().addItemListener( this.itemListener );
		buttonGroup.add( this.button.getAwtComponent() );
	}

	// note: does not seem to be called
	public void remove( javax.swing.ButtonGroup buttonGroup ) {
		//note: should already be removed by removeAllComponents()
		assert this.button.getParent() == null;
		this.button.getAwtComponent().removeItemListener( this.itemListener );
		buttonGroup.remove( this.button.getAwtComponent() );
	}
	public void setSelected( boolean isSelected ) {
		if( this.button.getAwtComponent().isSelected() != isSelected ) {
			this.button.getAwtComponent().setSelected( isSelected );
		}
	}
}

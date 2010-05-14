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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractItemSelectionOperation<E> extends edu.cmu.cs.dennisc.croquet.ItemSelectionOperation< E > {
//	private class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation<E> {
//		public ItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
//			super( java.util.UUID.fromString( "a10c07e8-bd0a-45e2-87aa-a3715fefb847" ), comboBoxModel );
//		}
//		@Override
//		protected void handleSelectionChange(E value) {
//			AbstractList.this.handlePerformSelectionChange( value );
//		}
//	}

	public AbstractItemSelectionOperation( java.util.UUID individualId, javax.swing.ComboBoxModel comboBoxModel ) {
		super( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, individualId, comboBoxModel );
//		this.setItemSelectionOperation( new ItemSelectionOperation( comboBoxModel ) );
		this.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					E item = (E)AbstractItemSelectionOperation.this.getValue();
					if( item != null ) {
						AbstractItemSelectionOperation.this.handlePerformSelectionChange( item );
					}
				}
			}
		} );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.ItemSelectionEdit<E> createItemSelectionEdit(edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, E previousSelection, E nextSelection) {
		throw new RuntimeException( "todo" );
	}
	protected abstract void handlePerformSelectionChange( E value );
	protected int getVisibleRowCount() {
		return 1;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.List<E> createList() {
		edu.cmu.cs.dennisc.croquet.List<E> rv = super.createList();
		rv.setLayoutOrientation( edu.cmu.cs.dennisc.croquet.List.LayoutOrientation.HORIZONTAL_WRAP );
		rv.setVisibleRowCount( this.getVisibleRowCount() );
		rv.setOpaque( false );
		return rv;
	}	
}

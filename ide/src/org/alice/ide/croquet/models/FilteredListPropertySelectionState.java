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

package org.alice.ide.croquet.models;

/**
 * @author Dennis Cosgrove
 */
public abstract class FilteredListPropertySelectionState< E > extends org.lgna.croquet.ListSelectionState< E > {
	private final edu.cmu.cs.dennisc.property.ListProperty< E > listProperty;

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > listPropertyListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< E >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
			FilteredListPropertySelectionState.this.updateData();
		}
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
			FilteredListPropertySelectionState.this.updateData();
		}
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
			FilteredListPropertySelectionState.this.updateData();
		}
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
			FilteredListPropertySelectionState.this.updateData();
		}
	};
	private edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			FilteredListPropertySelectionState.this.updateData();
		}
	};

	private final java.util.List< E > data = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	
	public FilteredListPropertySelectionState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< E > codec, int selectionIndex, edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
		super( group, id, codec, selectionIndex );
		this.listProperty = listProperty;
		this.listProperty.addPropertyListener( this.propertyListener );
		this.listProperty.addListPropertyListener( this.listPropertyListener );
		this.updateData();
	}
	protected abstract boolean isAcceptableItem( E item );
	private void updateData() {
		java.util.List< E > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

		for( E item : this.listProperty ) {
			if( this.isAcceptableItem( item ) ) {
				list.add( item );
			}
		}
		
		boolean isDataChanged = false;
		if( list.size() == this.data.size() ) {
			final int N = list.size();
			for( int i=0; i<N; i++ ) {
				if( list.get( i ) != this.data.get( i ) ) {
					isDataChanged = true;
					break;
				}
			}
		} else {
			isDataChanged = true;
		}

		if( isDataChanged ) {
			E selectedItem = this.getSelectedItem();
			this.setListData( list.indexOf( selectedItem ), list );
		}
	}
	@Override
	public E getItemAt( int index ) {
		return this.data.get( index );
	}
	@Override
	public int getItemCount() {
		return this.data.size();
	}
	@Override
	public int indexOf(E item) {
		return this.data.indexOf( item );
	}
	public java.util.Iterator< E > iterator() {
		return this.data.iterator();
	}
	@Override
	public E[] toArray( Class< E > componentType ) {
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.data, componentType );
	}
	@Override
	protected void internalAddItem( E item ) {
		this.data.add( item );
	}
	@Override
	protected void internalRemoveItem( E item ) {
		int index = this.data.indexOf( item );
		this.data.remove( index );
	}
	@Override
	protected void internalSetItems( java.util.Collection< E > items ) {
		this.data.clear();
		this.data.addAll( 0, items );
	}
}

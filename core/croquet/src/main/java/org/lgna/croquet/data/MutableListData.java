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
package org.lgna.croquet.data;

/**
 * @author Dennis Cosgrove
 */
public final class MutableListData<T> extends AbstractMutableListData<T> {
	private final java.util.concurrent.CopyOnWriteArrayList<T> values;

	public MutableListData( org.lgna.croquet.ItemCodec<T> itemCodec ) {
		super( itemCodec );
		this.values = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	}

	public MutableListData( org.lgna.croquet.ItemCodec<T> itemCodec, T[] values ) {
		super( itemCodec );
		this.values = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList( values );
	}

	public MutableListData( org.lgna.croquet.ItemCodec<T> itemCodec, java.util.Collection<T> values ) {
		super( itemCodec );
		this.values = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList( values );
	}

	@Override
	public boolean contains( T item ) {
		return this.values.contains( item );
	}

	@Override
	public T getItemAt( int index ) {
		if( index >= 0 ) {
			if( index < this.getItemCount() ) {
				return this.values.get( index );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( index, this.getItemCount() );
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public int getItemCount() {
		return this.values.size();
	}

	@Override
	public java.util.Iterator<T> iterator() {
		return this.values.iterator();
	}

	@Override
	public int indexOf( T item ) {
		return this.values.indexOf( item );
	}

	@Override
	protected final T[] toArray( Class<T> componentType ) {
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.values, componentType );
	}

	@Override
	public void internalAddItem( int index, T item ) {
		this.values.add( index, item );
		this.fireContentsChanged();
	}

	@Override
	public void internalRemoveItem( T item ) {
		this.values.remove( item );
		this.fireContentsChanged();
	}

	@Override
	public void internalSetAllItems( java.util.Collection<T> items ) {
		this.values.clear();
		this.values.addAll( items );
		this.fireContentsChanged();
	}

	@Override
	public void internalSetItemAt( int index, T item ) {
		this.values.set( index, item );
		this.fireContentsChanged();
	}
}

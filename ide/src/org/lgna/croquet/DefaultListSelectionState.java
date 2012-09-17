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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public class DefaultListSelectionState<E> extends ListSelectionState<E> {
	private final java.util.concurrent.CopyOnWriteArrayList<E> data = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public DefaultListSelectionState( Group group, java.util.UUID id, ItemCodec<E> codec, int selectionIndex ) {
		super( group, id, codec, selectionIndex );
	}

	public DefaultListSelectionState( Group group, java.util.UUID id, ItemCodec<E> codec ) {
		this( group, id, codec, -1 );
	}

	public DefaultListSelectionState( Group group, java.util.UUID id, ItemCodec<E> codec, int selectionIndex, java.util.Collection<E> data ) {
		this( group, id, codec, selectionIndex );
		this.data.addAll( data );
	}

	public DefaultListSelectionState( Group group, java.util.UUID id, ItemCodec<E> codec, int selectionIndex, E... data ) {
		this( group, id, codec, selectionIndex, java.util.Arrays.asList( data ) );
	}

	public java.util.Iterator<E> iterator() {
		return this.data.iterator();
	}

	@Override
	public int indexOf( E item ) {
		return this.data.indexOf( item );
	}

	@Override
	public E getItemAt( int index ) {
		if( index >= 0 ) {
			return this.data.get( index );
		} else {
			return null;
		}
	}

	@Override
	public int getItemCount() {
		return this.data.size();
	}

	@Override
	public E[] toArray( Class<E> componentType ) {
		E[] rv = (E[])java.lang.reflect.Array.newInstance( componentType, this.getItemCount() );
		this.data.toArray( rv );
		//		for( int i = 0; i < rv.length; i++ ) {
		//			rv[ i ] = this.getItemAt( i );
		//		}
		return rv;
	}

	@Override
	protected void internalAddItem( E item ) {
		this.data.add( item );
	}

	@Override
	protected void internalRemoveItem( E item ) {
		this.data.remove( item );
	}

	@Override
	protected void internalSetItems( java.util.Collection<E> items ) {
		this.data.clear();
		this.data.addAll( items );
	}
}

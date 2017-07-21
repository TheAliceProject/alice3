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
package edu.cmu.cs.dennisc.javax.swing.models;

/**
 * @author Dennis Cosgrove
 */
public class ListPropertyMutableListModel<E> extends AbstractReorderableListModel<E> implements MutableListModel<E> {
	private edu.cmu.cs.dennisc.property.ListProperty<E> property;

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener<E> listPropertyListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener<E>() {
		@Override
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<E> e ) {
		}

		@Override
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<E> e ) {
			//todo
		}

		@Override
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<E> e ) {
		}

		@Override
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<E> e ) {
			//todo
		}

		@Override
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<E> e ) {
		}

		@Override
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<E> e ) {
			//todo
		}

		@Override
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<E> e ) {
		}

		@Override
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<E> e ) {
			//todo
		}
	};

	public ListPropertyMutableListModel( edu.cmu.cs.dennisc.property.ListProperty<E> property ) {
		this.property = property;
	}

	public void startListening() {
		this.property.addListPropertyListener( this.listPropertyListener );
	}

	public void stopListening() {
		this.property.removeListPropertyListener( this.listPropertyListener );
	}

	@Override
	public Object getElementAt( int index ) {
		return this.property.get( index );
	}

	@Override
	public int getSize() {
		return this.property.size();
	}

	@Override
	public void add( E element ) {
		this.property.add( element );
	}

	@Override
	public void add( E... elements ) {
		this.property.add( elements );
	}

	@Override
	public void insert( int i, E element ) {
		this.property.add( i, element );
	}

	@Override
	public void insert( int i, E... elements ) {
		this.property.add( i, elements );
	}

	@Override
	public void clear() {
		this.property.clear();
	}

	@Override
	public void remove( int i ) {
		this.property.remove( i );
	}

	@Override
	public void removeExclusive( int fromIndex, int upToButExcludingIndex ) {
		this.property.removeExclusive( fromIndex, upToButExcludingIndex );
	}

	@Override
	public void removeInclusive( int fromIndex, int upToAndIncludingIndex ) {
		this.property.removeInclusive( fromIndex, upToAndIncludingIndex );
	}

	@Override
	public void set( E... elements ) {
		//todo
		this.property.clear();
		this.property.set( 0, elements );
	}

	@Override
	public void swap( int indexA, int indexB ) {
		this.property.swap( indexA, indexB );
	}

	@Override
	public void slide( int prevIndex, int nextIndex ) {
		this.property.slide( prevIndex, nextIndex );
	}
}

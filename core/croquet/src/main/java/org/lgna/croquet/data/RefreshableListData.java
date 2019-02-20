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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.ItemCodec;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class RefreshableListData<T> extends AbstractMutableListData<T> {
	private boolean isRefreshNecessary = true;
	private List<T> values;

	public RefreshableListData( ItemCodec<T> itemCodec ) {
		super( itemCodec );
	}

	private boolean refreshIfNecessary() {
		if( this.isRefreshNecessary ) {
			List<T> nextValues = this.createValues();

			assert nextValues != null : this;

			boolean isDataChanged = false;
			if( this.values != null ) {
				if( nextValues.size() == this.values.size() ) {
					final int N = nextValues.size();
					for( int i = 0; i < N; i++ ) {
						if( nextValues.get( i ) != this.values.get( i ) ) {
							isDataChanged = true;
							break;
						}
					}
				} else {
					isDataChanged = true;
				}
			} else {
				isDataChanged = true;
			}

			if( isDataChanged ) {
				this.values = nextValues;
			}
			this.isRefreshNecessary = false;
			return isDataChanged;
		} else {
			return false;
		}
	}

	protected abstract List<T> createValues();

	public final void refresh() {
		this.isRefreshNecessary = true;
		if( this.refreshIfNecessary() ) {
			this.fireContentsChanged();
		}
	}

	@Override
	public boolean contains( T item ) {
		return this.values.contains( item );
	}

	@Override
	public final T getItemAt( int index ) {
		return this.values.get( index );
	}

	@Override
	public final int getItemCount() {
		this.refreshIfNecessary();
		return this.values.size();
	}

	@Override
	public final int indexOf( T item ) {
		if( this.values != null ) {
			return this.values.indexOf( item );
		} else {
			return -1;
		}
	}

	@Override
	public void internalAddItem( int index, T item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalRemoveItem( T item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void internalSetAllItems( Collection<T> items ) {
		Logger.severe( items );
	}

	@Override
	public void internalSetItemAt( int index, T item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Iterator<T> iterator() {
		this.refreshIfNecessary();
		return this.values.iterator();
	}

	@Override
	protected final T[] toArray( Class<T> componentType ) {
		this.refreshIfNecessary();
		return ArrayUtilities.createArray( this.values, componentType );
	}
}

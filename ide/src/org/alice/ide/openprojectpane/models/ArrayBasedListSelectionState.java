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

package org.alice.ide.openprojectpane.models;

/**
 * @author Dennis Cosgrove
 */
public abstract class ArrayBasedListSelectionState<E> extends org.lgna.croquet.ListSelectionState< E > {
	private boolean isRefreshNecessary = true;
	private E[] array;
	public ArrayBasedListSelectionState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< E > itemCodec, int selectionIndex ) {
		super( group, id, itemCodec, selectionIndex );
	}
	protected abstract E[] createArray();
	private void refreshIfNecessary() {
		if( this.isRefreshNecessary ) {
			this.array = this.createArray();
			this.isRefreshNecessary = false;
		}
	}
	public final void refresh() {
		this.isRefreshNecessary = true;
		this.refreshIfNecessary();
		edu.cmu.cs.dennisc.java.util.logging.GlobalLogger.todo( "this.fireListDataChange();" );
	}
	@Override
	public final E getItemAt( int index ) {
		return this.array[ index ];
	}
	@Override
	public final int getItemCount() {
		this.refreshIfNecessary();
		return this.array.length;
	}
	@Override
	public final int indexOf( E item ) {
		return java.util.Arrays.asList( this.array ).indexOf( item );
	}
	@Override
	protected final void internalAddItem( E item ) {
		throw new AssertionError();
	}
	@Override
	protected final void internalRemoveItem( E item ) {
		throw new AssertionError();
	}
	@Override
	protected final void internalSetItems( java.util.Collection< E > items ) {
	}
	public final java.util.Iterator< E > iterator() {
		this.refreshIfNecessary();
		return java.util.Arrays.asList( this.array ).iterator();
	}
	@Override
	public final E[] toArray( Class< E > componentType ) {
		this.refreshIfNecessary();
		return this.array;
	}
}

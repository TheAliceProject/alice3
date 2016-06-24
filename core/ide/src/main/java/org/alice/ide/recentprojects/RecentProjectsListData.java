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
package org.alice.ide.recentprojects;

/**
 * @author Dennis Cosgrove
 */
public class RecentProjectsListData extends org.lgna.croquet.data.AbstractMutableListData<java.net.URI> {
	private static class SingletonHolder {
		private static RecentProjectsListData instance = new RecentProjectsListData();
	}

	public static RecentProjectsListData getInstance() {
		return SingletonHolder.instance;
	}

	private final java.util.List<java.net.URI> values;

	private RecentProjectsListData() {
		super( org.alice.ide.croquet.codecs.UriCodec.SINGLETON );
		java.net.URI[] array = org.lgna.croquet.preferences.PreferenceManager.decodeListData( this.getPreferenceKey(), this.getItemCodec(), new java.net.URI[] {} );

		java.util.List<java.net.URI> existingFileUris = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( java.net.URI uri : array ) {
			try {
				java.io.File file = new java.io.File( uri );
				if( file.exists() ) {
					existingFileUris.add( uri );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "file does not exist for:", uri );
				}
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, uri );
				//note: do not throw
			}
		}

		this.values = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList( existingFileUris );
		org.lgna.croquet.preferences.PreferenceManager.registerListData( this );
	}

	@Override
	public boolean contains( java.net.URI item ) {
		return this.values.contains( item );
	}

	@Override
	public int getItemCount() {
		return this.values.size();
	}

	@Override
	public java.net.URI getItemAt( int index ) {
		return this.values.get( index );
	}

	@Override
	public java.util.Iterator<java.net.URI> iterator() {
		return this.values.iterator();
	}

	@Override
	public void internalAddItem( int index, java.net.URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalRemoveItem( java.net.URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalSetAllItems( java.util.Collection<java.net.URI> items ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalSetItemAt( int index, java.net.URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf( java.net.URI item ) {
		return this.values.indexOf( item );
	}

	@Override
	public java.net.URI[] toArray( Class<java.net.URI> componentType ) {
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.values, componentType );
	}

	private void addFile( java.io.File file ) {
		if( file != null ) {
			final int N = org.alice.ide.projecturi.RecentProjectCountState.getInstance().getValue();
			if( N > 0 ) {
				java.net.URI uri = file.toURI();
				if( this.values.contains( uri ) ) {
					this.values.remove( uri );
				}
				this.values.add( 0, uri );
				while( this.values.size() > N ) {
					this.values.remove( this.values.size() - 1 );
				}
			} else {
				this.values.clear();
			}
			this.fireContentsChanged();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( file );
		}
	}

	public void handleOpen( java.io.File file ) {
		this.addFile( file );
	}

	public void handleSave( java.io.File file ) {
		this.addFile( file );
	}
}

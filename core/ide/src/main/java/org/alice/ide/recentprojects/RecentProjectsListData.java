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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.croquet.codecs.UriCodec;
import org.alice.ide.projecturi.RecentProjectCountState;
import org.lgna.croquet.data.AbstractMutableListData;
import org.lgna.croquet.preferences.PreferenceManager;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class RecentProjectsListData extends AbstractMutableListData<URI> {
	private static class SingletonHolder {
		private static RecentProjectsListData instance = new RecentProjectsListData();
	}

	public static RecentProjectsListData getInstance() {
		return SingletonHolder.instance;
	}

	private final List<URI> values;

	private RecentProjectsListData() {
		super( UriCodec.SINGLETON );
		URI[] array = PreferenceManager.decodeListData( this.getPreferenceKey(), this.getItemCodec(), new URI[] {} );

		List<URI> existingFileUris = Lists.newLinkedList();
		for( URI uri : array ) {
			try {
				File file = new File( uri );
				if( file.exists() ) {
					existingFileUris.add( uri );
				} else {
					Logger.errln( "file does not exist for:", uri );
				}
			} catch( Throwable t ) {
				Logger.throwable( t, uri );
				//note: do not throw
			}
		}

		this.values = Lists.newCopyOnWriteArrayList( existingFileUris );
		PreferenceManager.registerListData( this );
	}

	@Override
	public boolean contains( URI item ) {
		return this.values.contains( item );
	}

	@Override
	public int getItemCount() {
		return this.values.size();
	}

	@Override
	public URI getItemAt( int index ) {
		return this.values.get( index );
	}

	@Override
	public Iterator<URI> iterator() {
		return this.values.iterator();
	}

	@Override
	public void internalAddItem( int index, URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalRemoveItem( URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalSetAllItems( Collection<URI> items ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void internalSetItemAt( int index, URI item ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf( URI item ) {
		return this.values.indexOf( item );
	}

	@Override
	public URI[] toArray( Class<URI> componentType ) {
		return ArrayUtilities.createArray( this.values, componentType );
	}

	private void addFile( File file ) {
		if( file != null ) {
			final int N = RecentProjectCountState.getInstance().getValue();
			if( N > 0 ) {
				URI uri = file.toURI();
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
			Logger.severe( file );
		}
	}

	public void handleOpen( File file ) {
		this.addFile( file );
	}

	public void handleSave( File file ) {
		this.addFile( file );
	}
}

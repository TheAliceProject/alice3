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
package org.alice.ide.recentprojects;

/**
 * @author Dennis Cosgrove
 */
public class RecentProjectsListData extends org.lgna.croquet.AbstractListData< java.net.URI > {
	private static class SingletonHolder {
		private static RecentProjectsListData instance = new RecentProjectsListData();
	}
	public static RecentProjectsListData getInstance() {
		return SingletonHolder.instance;
	}
	private final java.util.List< java.net.URI > list = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private RecentProjectsListData() {
	}
	public org.lgna.croquet.ItemCodec< java.net.URI > getItemCodec() {
		return org.alice.ide.croquet.codecs.UriCodec.SINGLETON;
	}
	public int getSize() {
		return this.list.size();
	}
	public java.net.URI get( int index ) {
		return this.list.get( index );
	}
	
	public java.net.URI[] createArray() {
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.list, java.net.URI.class );
	}

	private void addFile( java.io.File file ) {
		if( file != null ) {
			final int N = org.alice.ide.croquet.models.openproject.RecentProjectCountState.getInstance().getValue();
			if( N > 0 ) {
				java.net.URI uri = file.toURI();
				if( this.list.contains( uri ) ) {
					this.list.remove( uri );
				}
				this.list.add( 0, uri );
				while( this.list.size() > N ) {
					this.list.remove( this.list.size()-1 );
				}
			} else {
				this.list.clear();
			}
			this.fireChanged();
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

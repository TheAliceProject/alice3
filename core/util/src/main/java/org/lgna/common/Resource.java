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
package org.lgna.common;

import edu.cmu.cs.dennisc.java.io.InputStreamUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.pattern.NameChangeListenable;
import edu.cmu.cs.dennisc.pattern.Nameable;
import edu.cmu.cs.dennisc.pattern.event.NameEvent;
import edu.cmu.cs.dennisc.pattern.event.NameListener;
import org.lgna.common.event.ResourceContentEvent;
import org.lgna.common.event.ResourceContentListener;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Resource implements Nameable, NameChangeListenable {
	protected Resource( UUID uuid ) {
		this.uuid = uuid;
	}

	protected Resource( Class<?> cls, String resourceName, String contentType ) {
		this( UUID.randomUUID() );
		try {
			byte[] data = InputStreamUtilities.getBytes( cls, resourceName );
			this.setOriginalFileName( resourceName );
			this.setName( resourceName );
			this.setContent( contentType, data );
		} catch( IOException ioe ) {
			throw new RuntimeException( resourceName, ioe );
		}
	}

	protected Resource( File file, String contentType ) throws IOException {
		this( UUID.randomUUID() );
		String resourceName = file.getName();
		byte[] data = InputStreamUtilities.getBytes( file );
		this.setOriginalFileName( resourceName );
		this.setName( resourceName );
		this.setContent( contentType, data );
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName( String name ) {
		if( Objects.notEquals( this.name, name ) ) {
			NameEvent nameEvent = new NameEvent( this, this.name, name );
			for( NameListener nameListener : this.nameListeners ) {
				nameListener.nameChanging( nameEvent );
			}
			this.name = name;
			for( NameListener nameListener : this.nameListeners ) {
				nameListener.nameChanged( nameEvent );
			}
		}
	}

	@Override
	public void addNameListener( NameListener nameListener ) {
		assert nameListener != null;
		this.nameListeners.add( nameListener );
	}

	@Override
	public void removeNameListener( NameListener nameListener ) {
		assert nameListener != null;
		this.nameListeners.remove( nameListener );
	}

	@Override
	public Collection<NameListener> getNameListeners() {
		return Collections.unmodifiableCollection( this.nameListeners );
	}

	public void addContentListener( ResourceContentListener contentListener ) {
		assert contentListener != null;
		this.contentListeners.add( contentListener );
	}

	public void removeContentListener( ResourceContentListener contentListener ) {
		assert contentListener != null;
		this.contentListeners.remove( contentListener );
	}

	public Collection<ResourceContentListener> getContentListeners() {
		return Collections.unmodifiableCollection( this.contentListeners );
	}

	public UUID getId() {
		return this.uuid;
	}

	public String getContentType() {
		return this.contentType;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setContent( String contentType, byte[] data ) {
		ResourceContentEvent e = new ResourceContentEvent( this, contentType, data );
		for( ResourceContentListener contentListener : this.contentListeners ) {
			contentListener.contentChanging( e );
		}
		this.contentType = contentType;
		this.data = data;
		for( ResourceContentListener contentListener : this.contentListeners ) {
			contentListener.contentChanged( e );
		}
	}

	public String getOriginalFileName() {
		return this.originalFileName;
	}

	public void setOriginalFileName( String originalFileName ) {
		this.originalFileName = originalFileName;
	}

	private static String XML_NAME_ATTRIBUTE = "name";
	private static String XML_ORIGINAL_FILE_NAME_ATTRIBUTE = "originalFileName";
	private static String XML_CONTENT_TYPE_ATTRIBUTE = "contentType";

	public void decodeAttributes( Element xmlElement, byte[] data ) {
		this.setName( xmlElement.getAttribute( XML_NAME_ATTRIBUTE ) );
		this.setOriginalFileName( xmlElement.getAttribute( XML_ORIGINAL_FILE_NAME_ATTRIBUTE ) );
		this.setContent( xmlElement.getAttribute( XML_CONTENT_TYPE_ATTRIBUTE ), data );
	}

	public void encodeAttributes( Element xmlElement ) {
		xmlElement.setAttribute( XML_NAME_ATTRIBUTE, this.getName() );
		xmlElement.setAttribute( XML_ORIGINAL_FILE_NAME_ATTRIBUTE, this.getOriginalFileName() );
		xmlElement.setAttribute( XML_CONTENT_TYPE_ATTRIBUTE, this.getContentType() );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( Resource.class.getName() );
		sb.append( "[name=" );
		sb.append( this.getName() );
		sb.append( ";contentType=" );
		sb.append( this.getContentType() );
		sb.append( ";uuid=" );
		sb.append( this.getId() );
		sb.append( "]" );
		return sb.toString();
	}

	private final UUID uuid;
	private String name;
	private String originalFileName;
	private String contentType;
	private byte[] data;
	private List<NameListener> nameListeners = Lists.newCopyOnWriteArrayList();
	private List<ResourceContentListener> contentListeners = Lists.newCopyOnWriteArrayList();
}

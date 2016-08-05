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

public abstract class Resource implements edu.cmu.cs.dennisc.pattern.Nameable, edu.cmu.cs.dennisc.pattern.NameChangeListenable {
	protected Resource( java.util.UUID uuid ) {
		this.uuid = uuid;
	}

	protected Resource( Class<?> cls, String resourceName, String contentType ) {
		this( java.util.UUID.randomUUID() );
		try {
			byte[] data = edu.cmu.cs.dennisc.java.io.InputStreamUtilities.getBytes( cls, resourceName );
			this.setOriginalFileName( resourceName );
			this.setName( resourceName );
			this.setContent( contentType, data );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( resourceName, ioe );
		}
	}

	protected Resource( java.io.File file, String contentType ) throws java.io.IOException {
		this( java.util.UUID.randomUUID() );
		String resourceName = file.getName();
		byte[] data = edu.cmu.cs.dennisc.java.io.InputStreamUtilities.getBytes( file );
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
		if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( this.name, name ) ) {
			edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent = new edu.cmu.cs.dennisc.pattern.event.NameEvent( this, this.name, name );
			for( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener : this.nameListeners ) {
				nameListener.nameChanging( nameEvent );
			}
			this.name = name;
			for( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener : this.nameListeners ) {
				nameListener.nameChanged( nameEvent );
			}
		}
	}

	@Override
	public void addNameListener( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener ) {
		assert nameListener != null;
		this.nameListeners.add( nameListener );
	}

	@Override
	public void removeNameListener( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener ) {
		assert nameListener != null;
		this.nameListeners.remove( nameListener );
	}

	@Override
	public java.util.Collection<edu.cmu.cs.dennisc.pattern.event.NameListener> getNameListeners() {
		return java.util.Collections.unmodifiableCollection( this.nameListeners );
	}

	public void addContentListener( org.lgna.common.event.ResourceContentListener contentListener ) {
		assert contentListener != null;
		this.contentListeners.add( contentListener );
	}

	public void removeContentListener( org.lgna.common.event.ResourceContentListener contentListener ) {
		assert contentListener != null;
		this.contentListeners.remove( contentListener );
	}

	public java.util.Collection<org.lgna.common.event.ResourceContentListener> getContentListeners() {
		return java.util.Collections.unmodifiableCollection( this.contentListeners );
	}

	public java.util.UUID getId() {
		return this.uuid;
	}

	public String getContentType() {
		return this.contentType;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setContent( String contentType, byte[] data ) {
		org.lgna.common.event.ResourceContentEvent e = new org.lgna.common.event.ResourceContentEvent( this, contentType, data );
		for( org.lgna.common.event.ResourceContentListener contentListener : this.contentListeners ) {
			contentListener.contentChanging( e );
		}
		this.contentType = contentType;
		this.data = data;
		for( org.lgna.common.event.ResourceContentListener contentListener : this.contentListeners ) {
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

	public void decodeAttributes( org.w3c.dom.Element xmlElement, byte[] data ) {
		this.setName( xmlElement.getAttribute( XML_NAME_ATTRIBUTE ) );
		this.setOriginalFileName( xmlElement.getAttribute( XML_ORIGINAL_FILE_NAME_ATTRIBUTE ) );
		this.setContent( xmlElement.getAttribute( XML_CONTENT_TYPE_ATTRIBUTE ), data );
	}

	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
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

	private final java.util.UUID uuid;
	private String name;
	private String originalFileName;
	private String contentType;
	private byte[] data;
	private java.util.List<edu.cmu.cs.dennisc.pattern.event.NameListener> nameListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private java.util.List<org.lgna.common.event.ResourceContentListener> contentListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
}

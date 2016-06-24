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
package org.lgna.common.resources;

public class AudioResource extends org.lgna.common.Resource {
	private static java.util.Map<String, String> extensionToContentTypeMap;
	static {
		AudioResource.extensionToContentTypeMap = new java.util.HashMap<String, String>();
		AudioResource.extensionToContentTypeMap.put( "au", "audio.basic" );
		AudioResource.extensionToContentTypeMap.put( "wav", "audio.x_wav" );
		AudioResource.extensionToContentTypeMap.put( "mp3", "audio.mpeg" );
	}

	public static String getContentType( String path ) {
		String extension = edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( path );
		String contentType = AudioResource.extensionToContentTypeMap.get( extension.toLowerCase( java.util.Locale.ENGLISH ) );
		return contentType;
	}

	public static String getContentType( java.io.File file ) {
		return getContentType( file.getName() );
	}

	public static boolean isAcceptableContentType( String contentType ) {
		return AudioResource.extensionToContentTypeMap.containsValue( contentType );
	}

	public static java.io.FilenameFilter createFilenameFilter( final boolean areDirectoriesAccepted ) {
		return new java.io.FilenameFilter() {
			@Override
			public boolean accept( java.io.File dir, String name ) {
				java.io.File file = new java.io.File( dir, name );
				if( file.isDirectory() ) {
					return areDirectoriesAccepted;
				} else {
					return getContentType( name ) != null;
				}
			}
		};
	}

	private static java.util.Map<java.util.UUID, AudioResource> uuidToResourceMap = new java.util.HashMap<java.util.UUID, AudioResource>();

	private static AudioResource get( java.util.UUID uuid ) {
		AudioResource rv = uuidToResourceMap.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new AudioResource( uuid );
			uuidToResourceMap.put( uuid, rv );
		}
		return rv;
	}

	public static AudioResource valueOf( String s ) {
		return get( java.util.UUID.fromString( s ) );
	}

	private double duration = Double.NaN;

	protected AudioResource( java.util.UUID uuid ) {
		super( uuid );
	}

	public AudioResource( Class<?> cls, String resourceName, String contentType ) {
		super( cls, resourceName, contentType );
		uuidToResourceMap.put( this.getId(), this );
	}

	public AudioResource( Class<?> cls, String resourceName ) {
		this( cls, resourceName, getContentType( resourceName ) );
	}

	public AudioResource( java.io.File file, String contentType ) throws java.io.IOException {
		super( file, contentType );
		uuidToResourceMap.put( this.getId(), this );
	}

	public AudioResource( java.io.File file ) throws java.io.IOException {
		this( file, getContentType( file ) );
	}

	public double getDuration() {
		return this.duration;
	}

	public void setDuration( double duration ) {
		this.duration = duration;
	}

	private static String XML_DURATION_ATTRIBUTE = "duration";

	@Override
	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
		super.encodeAttributes( xmlElement );
		xmlElement.setAttribute( XML_DURATION_ATTRIBUTE, Double.toString( this.duration ) );
	}

	@Override
	public void decodeAttributes( org.w3c.dom.Element xmlElement, byte[] data ) {
		super.decodeAttributes( xmlElement, data );
		this.duration = Double.parseDouble( xmlElement.getAttribute( XML_DURATION_ATTRIBUTE ) );
	}
}

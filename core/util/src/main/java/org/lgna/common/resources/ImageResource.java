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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.lgna.common.Resource;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ImageResource extends Resource {
	private static Map<String, String> extensionToContentTypeMap;

	private static final String PNG_MIME_TYPE = "image/png";
	private static final String JPEG_MIME_TYPE = "image/jpeg";
	private static final String BMP_MIME_TYPE = "image/bmp";
	private static final String GIF_MIME_TYPE = "image/gif";

	static {
		ImageResource.extensionToContentTypeMap = new HashMap<String, String>();
		ImageResource.extensionToContentTypeMap.put( "png", PNG_MIME_TYPE );
		ImageResource.extensionToContentTypeMap.put( "jpg", JPEG_MIME_TYPE );
		ImageResource.extensionToContentTypeMap.put( "jpeg", JPEG_MIME_TYPE );
		ImageResource.extensionToContentTypeMap.put( "bmp", BMP_MIME_TYPE );
		ImageResource.extensionToContentTypeMap.put( "gif", GIF_MIME_TYPE );
	}

	public static String getContentType( String path ) {
		String extension = FileUtilities.getExtension( path );
		return extension != null ? ImageResource.extensionToContentTypeMap.get( extension.toLowerCase( Locale.ENGLISH ) ) : null;
	}

	public static String getContentType( File file ) {
		return getContentType( file.getName() );
	}

	public static boolean isAcceptableContentType( String contentType ) {
		return ImageResource.extensionToContentTypeMap.containsValue( contentType );
	}

	public static FilenameFilter createFilenameFilter( final boolean areDirectoriesAccepted ) {
		return new FilenameFilter() {
			@Override
			public boolean accept( File dir, String name ) {
				File file = new File( dir, name );
				if( file.isDirectory() ) {
					return areDirectoriesAccepted;
				} else {
					return getContentType( name ) != null;
				}
			}
		};
	}

	private static Map<UUID, ImageResource> uuidToResourceMap = new HashMap<UUID, ImageResource>();

	private static ImageResource get( UUID uuid ) {
		ImageResource rv = uuidToResourceMap.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new ImageResource( uuid );
			uuidToResourceMap.put( uuid, rv );
		}
		return rv;
	}

	public static ImageResource valueOf( String s ) {
		return get( UUID.fromString( s ) );
	}

	private int width = -1;
	private int height = -1;

	public ImageResource( UUID uuid ) {
		super( uuid );
	}

	public ImageResource( Class<?> cls, String resourceName, String contentType ) {
		super( cls, resourceName, contentType );
		uuidToResourceMap.put( this.getId(), this );
	}

	public ImageResource( Class<?> cls, String resourceName ) {
		this( cls, resourceName, getContentType( resourceName ) );
	}

	public ImageResource( File file, String contentType ) throws IOException {
		super( file, contentType );
		uuidToResourceMap.put( this.getId(), this );
	}

	public ImageResource( File file ) throws IOException {
		this( file, getContentType( file ) );
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth( int width ) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight( int height ) {
		this.height = height;
	}

	private static String XML_WIDTH_ATTRIBUTE = "width";
	private static String XML_HEIGHT_ATTRIBUTE = "height";

	@Override
	public void encodeAttributes( Element xmlElement ) {
		super.encodeAttributes( xmlElement );
		xmlElement.setAttribute( XML_WIDTH_ATTRIBUTE, Integer.toString( this.width ) );
		xmlElement.setAttribute( XML_HEIGHT_ATTRIBUTE, Integer.toString( this.height ) );
	}

	@Override
	public void decodeAttributes( Element xmlElement, byte[] data ) {
		super.decodeAttributes( xmlElement, data );
		this.width = Integer.parseInt( xmlElement.getAttribute( XML_WIDTH_ATTRIBUTE ) );
		this.height = Integer.parseInt( xmlElement.getAttribute( XML_HEIGHT_ATTRIBUTE ) );
	}
}

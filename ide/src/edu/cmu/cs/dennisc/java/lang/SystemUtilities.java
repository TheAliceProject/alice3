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
package edu.cmu.cs.dennisc.java.lang;

/**
 * @author Dennis Cosgrove
 */
public class SystemUtilities {
	private SystemUtilities() {
		throw new AssertionError();
	}

	public static boolean isPropertyTrue( String propertyName ) {
		return "true".equals( System.getProperty( propertyName ) );
	}

	public static boolean isPropertyFalse( String propertyName ) {
		return "false".equals( System.getProperty( propertyName ) );
	}

	public static boolean getBooleanProperty( String propertyName, boolean defaultValue ) {
		String textValue = System.getProperty( propertyName );
		if( textValue != null ) {
			return Boolean.parseBoolean( textValue );
		} else {
			return defaultValue;
		}
	}

	private static java.io.ByteArrayOutputStream getPropertiesAsXMLByteArrayOutputStream() {
		java.util.List<Property> properties = getSortedPropertyList();
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlRootElement = xmlDocument.createElement( "systemProperties" );
		xmlDocument.appendChild( xmlRootElement );
		for( Property property : properties ) {
			org.w3c.dom.Element xmlProperty = xmlDocument.createElement( "property" );
			xmlProperty.setAttribute( "key", property.getKey() );
			xmlProperty.appendChild( xmlDocument.createTextNode( property.getValue() ) );
			xmlRootElement.appendChild( xmlProperty );
		}
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, baos );
		try {
			baos.flush();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
		return baos;
	}

	public static byte[] getPropertiesAsXMLByteArray() {
		return getPropertiesAsXMLByteArrayOutputStream().toByteArray();
	}

	public static String getPropertiesAsXMLString() {
		return getPropertiesAsXMLByteArrayOutputStream().toString();
	}

	public static java.util.List<Property> getPropertyList() {
		java.util.List<Property> rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		java.util.Properties systemProperties = System.getProperties();
		for( java.util.Map.Entry<Object, Object> entry : systemProperties.entrySet() ) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if( ( key instanceof String ) && ( value instanceof String ) ) {
				rv.add( new edu.cmu.cs.dennisc.java.lang.Property( (String)key, (String)value ) );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( key, value );
			}
		}
		return rv;
	}

	public static java.util.List<Property> getSortedPropertyList() {
		java.util.List<Property> rv = getPropertyList();
		java.util.Collections.sort( rv );
		return rv;
	}

	private enum Platform {
		WINDOWS,
		OSX,
		LINUX
	}

	private static Platform platform;
	static {
		String lowercaseOSName = System.getProperty( "os.name" ).toLowerCase();
		if( lowercaseOSName.contains( "windows" ) ) {
			platform = Platform.WINDOWS;
		} else if( lowercaseOSName.startsWith( "mac os x" ) ) {
			platform = Platform.OSX;
		} else if( lowercaseOSName.startsWith( "linux" ) ) {
			platform = Platform.LINUX;
		} else {
			//todo
			platform = null;
		}
	}

	public static boolean isLinux() {
		return SystemUtilities.platform == Platform.LINUX;
	}

	public static boolean isMac() {
		return SystemUtilities.platform == Platform.OSX;
	}

	public static boolean isWindows() {
		return SystemUtilities.platform == Platform.WINDOWS;
	}

	public static Integer getBitCount() {
		String bitCountText = System.getProperty( "sun.arch.data.model" );
		if( bitCountText != null ) {
			try {
				return Integer.parseInt( bitCountText );
			} catch( NumberFormatException nfe ) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean isPlatformSpecificLibraryLoadingDesired() {
		//return isWindows();
		return false;
	}

	public static String getPlatformSpecificLibraryNameIfAppropriate( String libraryName ) {
		if( isPlatformSpecificLibraryLoadingDesired() ) {
			StringBuilder sb = new StringBuilder();
			if( isMac() ) {
				sb.append( "macosx-universal/" );
			} else {
				if( isWindows() ) {
					sb.append( "windows-" );
				} else if( isLinux() ) {
					sb.append( "linux-" );
				} else {
					throw new RuntimeException( System.getProperty( "os.name" ) );
				}
				Integer bitCount = getBitCount();
				if( bitCount != null ) {
					switch( bitCount ) {
					case 32:
						sb.append( "i586/" );
						break;
					case 64:
						sb.append( "amd64/" );
						break;
					default:
						throw new RuntimeException( System.getProperty( "sun.arch.data.model" ) );
					}
				} else {
					throw new RuntimeException( System.getProperty( "sun.arch.data.model" ) );
				}
			}
			sb.append( libraryName );
			return sb.toString();
		} else {
			return libraryName;
		}
	}

	public static boolean areIconsDisplayedInMenus() {
		//return isWindows();
		return false;
	}

	public static <E> E[] returnArray( Class<E> componentType, E... rv ) {
		return rv;
	}

	public static <E> E[] createArray( Class<E> componentType, E[]... arrays ) {
		int n = 0;
		for( E[] array : arrays ) {
			if( array != null ) {
				n += array.length;
			}
		}
		E[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, n );
		int offset = 0;
		for( E[] array : arrays ) {
			if( array != null ) {
				System.arraycopy( array, 0, rv, offset, array.length );
				offset += array.length;
			}
		}
		return rv;
	}

	private static final String PATH_SEPARATOR = System.getProperty( "path.separator" );;

	private static String[] parsePath( String propertyName ) {
		String value = System.getProperty( propertyName );
		assert value != null;
		return value.split( PATH_SEPARATOR );
	}

	public static String[] getClassPath() {
		return parsePath( "java.class.path" );
	}

	public static String[] getLibraryPath() {
		return parsePath( "java.library.path" );
	}

	public static void main( String[] args ) {
		System.out.println( getPropertiesAsXMLString() );
	}
}

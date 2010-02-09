/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.io;

/**
 * @author Dennis Cosgrove
 */
public class InputStreamUtilities {
	public static byte[] getBytes( java.io.InputStream is ) throws java.io.IOException {
		byte[] buffer = null;
		java.io.ByteArrayOutputStream baos = null;
		while( true ) {
			int n = is.available();
			if( buffer != null ) {
				// handle the previous iteration
				if( baos != null ) {
					//pass
				} else {
					// it is the second iteration
					if( n > 0 ) {
						// more than one buffer so we use a ByteArrayOutputStream
						baos = new java.io.ByteArrayOutputStream();
					} else {
						// there is one and only one buffer, so simply return it
						return buffer;
					}
				}
				// will only get here if there are 2 or more buffers
				baos.write( buffer );
			}
			if( n > 0 ) {
				buffer = new byte[ n ];
				int offset = 0;
				while( offset<n ) {
					offset += is.read( buffer, offset, n-offset ); 
				}
				assert offset == n;
			} else {
				break;
			}
		}
		return baos.toByteArray();
	}
	public static byte[] getBytes( java.io.File file ) throws java.io.IOException{
		java.io.FileInputStream fis = new java.io.FileInputStream( file );
		try {
			return getBytes( fis );
		} finally {
			fis.close();
		}
	}
	public static byte[] getBytes( Class< ? > cls, String resourceName ) throws java.io.IOException{
		java.io.InputStream is = cls.getResourceAsStream( resourceName );
		try {
			return getBytes( is );
		} finally {
			is.close();
		}
	}
}

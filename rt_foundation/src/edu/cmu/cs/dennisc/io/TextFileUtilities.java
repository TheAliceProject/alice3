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
public class TextFileUtilities {
	public static String read( java.io.Reader reader ) {
		final String SEPARATOR = System.getProperty( "line.separator" );
		java.io.BufferedReader bufferedReader = new java.io.BufferedReader( reader );
		try {
			try {
				StringBuffer sb = new StringBuffer();
				String line;
				while( true ) {
					line = bufferedReader.readLine();
					if( line != null ) {
						sb.append( line );
						sb.append( SEPARATOR );
					} else {
						break;
					}
				}
				return sb.toString();
			} finally {
				bufferedReader.close();
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static String read( java.io.InputStream is ) {
		return read( new java.io.InputStreamReader( is ) );
	}
	public static String read( java.io.File file ) {
		try {
			return read( new java.io.FileReader( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static String read( String path ) {
		return read( new java.io.File( path ) );
	}

	public static void write( java.io.Writer writer, String contents ) {
		try {
			java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter( writer );
			try {
				bufferedWriter.write( contents );
				bufferedWriter.flush();
			} finally {
				bufferedWriter.close();
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static void write( java.io.File file, String contents ) {
		file.getParentFile().mkdirs();
		try {
			write( new java.io.FileWriter( file ), contents );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}

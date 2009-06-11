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
package edu.cmu.cs.dennisc.alice.ide.editors.code;


/**
 * @author Dennis Cosgrove
 */
public class Line {
	private static final String PREFIX = "</";
	private static final String POSTFIX = "/>";
	private static final java.util.regex.Pattern TAG_PATTERN = java.util.regex.Pattern.compile( PREFIX + "[A-Za-z_0-9()]*" + POSTFIX );
	
	private int indentCount;
	private java.util.List< Chunk > chunks = new java.util.LinkedList< Chunk >();

	public Line( String s ) {
		this.indentCount = 0;
		for( byte b : s.getBytes() ) {
			if( b == '\t' ) {
				this.indentCount++;
			} else {
				break;
			}
		}
		java.util.regex.Matcher matcher = TAG_PATTERN.matcher( s );
		int iEnd = this.indentCount;
		while( matcher.find() ) {
			int iStart = matcher.start();
			if( iStart != iEnd ) {
				chunks.add( new TextChunk( s.substring( iEnd, iStart ) ) );
			}
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( s );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( iStart );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( iEnd );
			iEnd = matcher.end();
			String sub = s.substring( iStart + PREFIX.length(), iEnd - POSTFIX.length() );
			if( sub.startsWith( "_gets_toward_" ) ) {
				boolean isTowardLeading = sub.equals( "_gets_toward_leading_" );
				chunks.add( new GetsChunk( isTowardLeading ) );
			} else {
				if( sub.endsWith( "()" ) ) {
					chunks.add( new MethodInvocationChunk( sub ) );
				} else {
					chunks.add( new PropertyChunk( sub ) );
				}
			}
		}
		if( iEnd < s.length() ) {
			chunks.add( new TextChunk( s.substring( iEnd ) ) );
		}
	}
	public int getIndentCount() {
		return this.indentCount;
	}
	public Iterable< Chunk > getChunks() {
		return this.chunks;
	}
}

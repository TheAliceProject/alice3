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

package edu.cmu.cs.dennisc.timing;

/**
 * @author Dennis Cosgrove
 */
public class Timer {
	private long tStart;
	private java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2<Object, Long> > marks = new java.util.LinkedList<edu.cmu.cs.dennisc.pattern.Tuple2<Object,Long>>();
	public void start() {
		this.tStart = System.currentTimeMillis();
	}
	public void mark( Object text ) {
		long tMark = System.currentTimeMillis();
		this.marks.add( new edu.cmu.cs.dennisc.pattern.Tuple2<Object, Long>( text, tMark ) );
	}
	public void stopAndPrintResults() {
		long tPrev = this.tStart;
		long tStop = System.currentTimeMillis();
		for( edu.cmu.cs.dennisc.pattern.Tuple2<Object, Long> mark : this.marks ) {
			long tMark = mark.getB();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "...", tMark-tPrev, mark.getA() );
			tPrev = tMark;
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "... ", tStop-tPrev, "end" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "total:", tStop-this.tStart );
	}
}

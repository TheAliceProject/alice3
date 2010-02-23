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
package edu.cmu.cs.dennisc.lang;

/**
 * @author Dennis Cosgrove
 */
public class ThreadWithRevealingToString extends Thread {
	public ThreadWithRevealingToString() {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getName() );
	}
	public ThreadWithRevealingToString( ThreadGroup threadGroup, String name ) {
		super( threadGroup, name );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getName() );
	}
	protected StringBuffer updateRepr( StringBuffer rv ) { 
		rv.append( "id=" );
		rv.append( this.getId() );
//		rv.append( ";group=" );
//		rv.append( this.getThreadGroup() );
//		rv.append( ";priority=" );
//		rv.append( this.getPriority() );
		return rv;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		updateRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
}

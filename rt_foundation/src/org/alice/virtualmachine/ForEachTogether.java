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
package org.alice.virtualmachine;

/**
 * @author Dennis Cosgrove
 */
class ForEachRunnableAdapter<E> implements Runnable {
	private ForEachRunnable< E > forEachRunnable;
	private E value;

	public ForEachRunnableAdapter( ForEachRunnable< E > forEachRunnable, E value ) {
		this.forEachRunnable = forEachRunnable;
		this.value = value;
	}
	public void run() {
		this.forEachRunnable.run( this.value );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ForEachTogether {
	public static < E extends Object> void invokeAndWait( E[] array, ForEachRunnable< E > forEachRunnable ) {
		switch( array.length ) {
		case 0:
			break;
		case 1:
			forEachRunnable.run( array[ 0 ] );
			break;
		default:
			Runnable[] runnables = new Runnable[ array.length ];
			for( int i = 0; i < runnables.length; i++ ) {
				runnables[ i ] = new ForEachRunnableAdapter( forEachRunnable, array[ i ] );
			}
			edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
		}
	}
	public static <E extends Object> void invokeAndWait( Iterable<E> iterable, final ForEachRunnable< E > forEachRunnable ) {
		java.util.Collection< E > collection;
		if( iterable instanceof java.util.Collection< ? > ) {
			collection = (java.util.Collection< E >)iterable;
		} else {
			collection = new java.util.Vector< E >();
			for( E item : iterable ) {
				collection.add( item );
			}
		}
		Runnable[] runnables = new Runnable[ collection.size() ];
		int i = 0;
		for( E value : collection ) {
			runnables[ i ] = new ForEachRunnableAdapter( forEachRunnable, value );
			i++;
		}
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
	}
}

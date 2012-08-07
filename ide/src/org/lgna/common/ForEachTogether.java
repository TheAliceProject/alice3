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
package org.lgna.common;

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
@Deprecated
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
			org.lgna.common.DoTogether.invokeAndWait( runnables );
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
		org.lgna.common.DoTogether.invokeAndWait( runnables );
	}
}

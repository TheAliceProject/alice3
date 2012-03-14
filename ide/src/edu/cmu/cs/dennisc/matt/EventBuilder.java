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
package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.HashMap;

import org.lgna.story.MovableTurnable;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.StartCollisionEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class EventBuilder {

	private static HashMap<Object,ArrayList<ArrayList<? extends Object>>> collectionMap = Collections.newHashMap();
	private static HashMap<Object,Class<?>[]> classMap = Collections.newHashMap();
	static Class<?>[] arr = { CollisionStartListener.class };
	private static ArrayList<Class<?>> handledList = Collections.newArrayList( arr );

	public static void register( Object listener, Class<?>[] clsArr, ArrayList<ArrayList<?>> list ) {
		boolean check = false;
		for( Class<?> cls : handledList ) {
			if( listener.getClass().isAssignableFrom( cls ) ) {
				check = true;
			}
		}
		if( check ) {
			return;
		}
		if( collectionMap.keySet().contains( listener ) ) {
			return;
		}

		collectionMap.put( listener, list );
		classMap.put( listener, clsArr );
	}

	public static <A> A buildEvent( Class<A> event, Object listener, Object[] array ) {
		if( event == StartCollisionEvent.class || event == EndCollisionEvent.class ) {
			if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends MovableTurnable> clsOne = (Class<? extends MovableTurnable>)classMap.get( listener )[ 0 ];
					Class<? extends MovableTurnable> clsTwo = (Class<? extends MovableTurnable>)classMap.get( listener )[ 1 ];
					return (A)buildCollisionEvent( clsOne, clsTwo, listener, array );
				}
			}
		}
		return null;
	}
	private static <A extends MovableTurnable, B extends MovableTurnable> CollisionEvent<A,B> buildCollisionEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		assert array.length == 2;
		A first = null;
		B second = null;
		if( collectionMap.get( listener ).get( 0 ).contains( array[ 0 ] ) ) {
			if( collectionMap.get( listener ).get( 1 ).contains( array[ 1 ] ) ) {
				first = (A)array[ 0 ];
				second = (B)array[ 1 ];
			} else {
				first = (A)array[ 1 ];
				second = (B)array[ 0 ];
			}
		} else {
			first = (A)array[ 1 ];
			second = (B)array[ 0 ];
		}
		//		for( Object o : array ) {
		//			if( collectionMap.get( listener ).get( 0 ).contains( o ) ) {
		//				if(collectionMap.get( listener ).get( 1 ).contains( o ))
		//				first = (A)o;
		//			}
		//			if( collectionMap.get( listener ).get( 1 ).contains( o ) ) {
		//				second = (B)o;
		//			}
		//		}
		if( listener instanceof CollisionStartListener ) {
			return new StartCollisionEvent<A,B>( first, second );
		} else if( listener instanceof CollisionEndListener ) {
			return new EndCollisionEvent<A,B>( first, second );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED COLLISION EVENT" );
			return null;
		}
	}
	public static void ammend( Object key, int group, Object newObject ) {
		ArrayList temp = Collections.newArrayList( newObject );
		collectionMap.get( key ).get( group ).addAll( temp );
	}
}

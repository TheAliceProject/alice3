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

import org.lgna.story.Entity;
import org.lgna.story.Model;
import org.lgna.story.MovableTurnable;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityEvent;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewExitListener;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class EventBuilder {

	private static HashMap<Object,ArrayList<ArrayList<? extends Object>>> collectionMap = Collections.newHashMap();
	private static HashMap<Object,Class<?>[]> classMap = Collections.newHashMap();

	private static class EventPair<A, B> {
		A first;
		B second;

		public EventPair( A first, B second ) {
			this.first = first;
			this.second = second;
		}

		protected A getFirst() {
			return first;
		}
		protected B getSecond() {
			return second;
		}
	}

	public static void register( Object listener, Class<?>[] clsArr, ArrayList<ArrayList<?>> list ) {
		collectionMap.put( listener, list );
		classMap.put( listener, clsArr );
	}

	@SuppressWarnings("unchecked")
	private static <A> A buildEvent( Class<A> event, Object listener, Object[] array ) {
		if( CollisionEvent.class.isAssignableFrom( event ) ) {
			if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends MovableTurnable> clsOne = (Class<? extends MovableTurnable>)classMap.get( listener )[ 0 ];
					Class<? extends MovableTurnable> clsTwo = (Class<? extends MovableTurnable>)classMap.get( listener )[ 1 ];
					return (A)makeCollisionEvent( clsOne, clsTwo, listener, array );
				}
			}
		} else if( ProximityEvent.class.isAssignableFrom( event ) ) {
			if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( MovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends MovableTurnable> clsOne = (Class<? extends MovableTurnable>)classMap.get( listener )[ 0 ];
					Class<? extends MovableTurnable> clsTwo = (Class<? extends MovableTurnable>)classMap.get( listener )[ 1 ];
					return (A)makeProximityEvent( clsOne, clsTwo, listener, array );
				}
			}
		} else if( ViewEvent.class.isAssignableFrom( event ) ) {
			if( Model.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				Class<? extends Model> clsOne = (Class<? extends Model>)classMap.get( listener )[ 0 ];
				return (A)makeViewEvent( clsOne, listener, array );
			}
		} else if( OcclusionEvent.class.isAssignableFrom( event ) ) {
			if( Model.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( Model.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends Model> clsOne = (Class<? extends Model>)classMap.get( listener )[ 0 ];
					Class<? extends Model> clsTwo = (Class<? extends Model>)classMap.get( listener )[ 1 ];
					return (A)makeCollisionEvent( clsOne, clsTwo, listener, array );
				}
			}
		}
		System.out.println( "ATTEMPTED TO MAKE UNHANDLED EVENT" );
		return null;
	}
	@SuppressWarnings("unchecked")
	private static <A extends Model> ViewEvent<A> makeViewEvent( Class<A> clsOne, Object listener, Object[] array ) {
		if( listener instanceof ViewEnterListener ) {
			return new ComesIntoViewEvent<A>( (A)array[ 0 ] );
		} else if( listener instanceof ViewExitListener ) {
			return new LeavesViewEvent<A>( (A)array[ 0 ] );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED COLLISION EVENT" );
			return null;
		}
	}

	private static <A extends MovableTurnable, B extends MovableTurnable> ProximityEvent<A,B> makeProximityEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		EventPair<A,B> pair = pairedEvent( clsOne, clsTwo, listener, array );
		if( listener instanceof ProximityEnterListener ) {
			return new EnterProximityEvent<A,B>( pair.getFirst(), pair.getSecond() );
		} else if( listener instanceof ProximityEnterListener ) {
			return new ExitProximityEvent<A,B>( pair.getFirst(), pair.getSecond() );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED COLLISION EVENT" );
			return null;
		}
	}

	private static <A extends MovableTurnable, B extends MovableTurnable> CollisionEvent<A,B> makeCollisionEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		EventPair<A,B> pair = pairedEvent( clsOne, clsTwo, listener, array );
		if( listener instanceof CollisionStartListener ) {
			return new StartCollisionEvent<A,B>( pair.getFirst(), pair.getSecond() );
		} else if( listener instanceof CollisionEndListener ) {
			return new EndCollisionEvent<A,B>( pair.getFirst(), pair.getSecond() );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED PROXIMITY EVENT" );
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	private static <A, B> EventPair<A,B> pairedEvent( Class<A> a, Class<B> b, Object listener, Object[] array ) {
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
		return new EventPair<A,B>( first, second );
	}
	public static void ammend( Object key, int group, Object newObject ) {
		ArrayList temp = Collections.newArrayList( newObject );
		collectionMap.get( key ).get( group ).addAll( temp );
	}

	public static <A> A buildCollisionEvent( Class<A> eventClass, Object colList, MovableTurnable[] array ) {
		return buildEvent( eventClass, colList, array );
	}

	public static <A> A buildProximityEvent( Class<A> eventClass, Object proxList, MovableTurnable[] array ) {
		return buildEvent( eventClass, proxList, array );
	}

	public static <A> A buildViewEvent( Class<A> eventClass, PointOfViewChangeListener listener, Entity[] array ) {
		return buildEvent( eventClass, listener, array );
	}

	public static <A> A buildOcclusionEvent( Class<A> eventClass, PointOfViewChangeListener listener, Entity[] array ) {
		return buildEvent( eventClass, listener, array );
	}
}

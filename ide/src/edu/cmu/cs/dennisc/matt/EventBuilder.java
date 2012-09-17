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

import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewEvent;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityEvent;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewExitListener;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class EventBuilder {

	private static HashMap<Object, ArrayList<ArrayList<? extends Object>>> collectionMap = Collections.newHashMap();
	private static HashMap<Object, Class<?>[]> classMap = Collections.newHashMap();

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

	@SuppressWarnings( "unchecked" )
	private static <A> A buildEvent( Class<A> event, Object listener, Object[] array ) {
		if( CollisionEvent.class.isAssignableFrom( event ) ) {
			if( SMovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( SMovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends SMovableTurnable> clsOne = (Class<? extends SMovableTurnable>)classMap.get( listener )[ 0 ];
					Class<? extends SMovableTurnable> clsTwo = (Class<? extends SMovableTurnable>)classMap.get( listener )[ 1 ];
					return (A)makeCollisionEvent( clsOne, clsTwo, listener, array );
				}
			}
		} else if( ProximityEvent.class.isAssignableFrom( event ) ) {
			if( SMovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( SMovableTurnable.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends SMovableTurnable> clsOne = (Class<? extends SMovableTurnable>)classMap.get( listener )[ 0 ];
					Class<? extends SMovableTurnable> clsTwo = (Class<? extends SMovableTurnable>)classMap.get( listener )[ 1 ];
					return (A)makeProximityEvent( clsOne, clsTwo, listener, array );
				}
			}
		} else if( ViewEvent.class.isAssignableFrom( event ) ) {
			if( SModel.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				Class<? extends SModel> clsOne = (Class<? extends SModel>)classMap.get( listener )[ 0 ];
				return (A)makeViewEvent( clsOne, listener, array );
			}
		} else if( OcclusionEvent.class.isAssignableFrom( event ) ) {
			if( SModel.class.isAssignableFrom( classMap.get( listener )[ 0 ] ) ) {
				if( SModel.class.isAssignableFrom( classMap.get( listener )[ 1 ] ) ) {
					Class<? extends SModel> clsOne = (Class<? extends SModel>)classMap.get( listener )[ 0 ];
					Class<? extends SModel> clsTwo = (Class<? extends SModel>)classMap.get( listener )[ 1 ];
					return (A)makeOcclusionEvent( clsOne, clsTwo, listener, array );
				}
			}
		} else if( PointOfViewEvent.class.isAssignableFrom( event ) ) {
			if( ( array != null ) && ( array.length > 0 ) ) {
				return (A)new PointOfViewEvent( (SMovableTurnable)array[ 0 ] );
			}
		}
		System.out.println( "ATTEMPTED TO MAKE UNHANDLED EVENT: " + event );
		return null;
	}

	private static <A extends SModel, B extends SModel> OcclusionEvent<A, B> makeOcclusionEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		EventPair<A, B> pair = pairedEvent( clsOne, clsTwo, listener, array );
		if( listener instanceof OcclusionStartListener ) {
			return new StartOcclusionEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else if( listener instanceof OcclusionEndListener ) {
			return new EndOcclusionEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED OCCLUSION EVENT" );
			return null;
		}
	}

	@SuppressWarnings( "unchecked" )
	private static <A extends SModel> ViewEvent<A> makeViewEvent( Class<A> clsOne, Object listener, Object[] array ) {
		if( listener instanceof ViewEnterListener ) {
			return new ComesIntoViewEvent<A>( (A)array[ 0 ] );
		} else if( listener instanceof ViewExitListener ) {
			return new LeavesViewEvent<A>( (A)array[ 0 ] );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED VIEW EVENT" );
			return null;
		}
	}

	private static <A extends SMovableTurnable, B extends SMovableTurnable> ProximityEvent<A, B> makeProximityEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		EventPair<A, B> pair = pairedEvent( clsOne, clsTwo, listener, array );
		if( listener instanceof ProximityEnterListener ) {
			return new EnterProximityEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else if( listener instanceof ProximityExitListener ) {
			return new ExitProximityEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED PROXIMITY EVENT" );
			return null;
		}
	}

	private static <A extends SMovableTurnable, B extends SMovableTurnable> CollisionEvent<A, B> makeCollisionEvent( Class<A> clsOne, Class<B> clsTwo, Object listener, Object[] array ) {
		EventPair<A, B> pair = pairedEvent( clsOne, clsTwo, listener, array );
		if( listener instanceof CollisionStartListener ) {
			return new StartCollisionEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else if( listener instanceof CollisionEndListener ) {
			return new EndCollisionEvent<A, B>( pair.getFirst(), pair.getSecond() );
		} else {
			System.out.println( "ATTEMPTED TO MAKE UNHANDLED COLLISION EVENT" );
			return null;
		}
	}

	@SuppressWarnings( "unchecked" )
	private static <A, B> EventPair<A, B> pairedEvent( Class<A> a, Class<B> b, Object listener, Object[] array ) {
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
		return new EventPair<A, B>( first, second );
	}

	public static void ammend( Object key, int group, Object newObject ) {
		ArrayList temp = Collections.newArrayList( newObject );
		collectionMap.get( key ).get( group ).addAll( temp );
	}

	public static <A> A buildCollisionEvent( Class<A> eventClass, Object colList, SMovableTurnable[] array ) {
		return buildEvent( eventClass, colList, array );
	}

	public static <A> A buildProximityEvent( Class<A> eventClass, Object proxList, SMovableTurnable[] array ) {
		return buildEvent( eventClass, proxList, array );
	}

	public static <A> A buildViewEvent( Class<A> eventClass, Object listener, SThing[] array ) {
		return buildEvent( eventClass, listener, array );
	}

	public static <A> A buildOcclusionEvent( Class<A> eventClass, Object listener, SThing[] array ) {
		return buildEvent( eventClass, listener, array );
	}

	public static <A> A buildPointOfViewEvent( Class<A> eventClass, Object listener, SThing[] arr ) {
		return buildEvent( eventClass, listener, arr );
	}
}

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
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.SThing;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.StartCollisionEvent;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Matt May
 */
public class CollisionHandler extends TransformationChangedHandler<Object,CollisionEvent> implements InstanceCreationListener {

	protected CollisionEventHandler collisionEventHandler = new CollisionEventHandler();

	public <A extends SMovableTurnable, B extends SMovableTurnable> void addCollisionListener( Object collisionListener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, MultipleEventPolicy policy ) {
		ArrayList[] listArr = super.addPairedListener( collisionListener, groupOne, a, groupTwo, b, policy );
		collisionEventHandler.register( collisionListener, new ArrayList<SThing>( (ArrayList<? extends SThing>)listArr[ 0 ] ), new ArrayList<SThing>( (ArrayList<? extends SThing>)listArr[ 1 ] ) );
	}
	@Override
	protected void check( SThing changedThing ) {
		collisionEventHandler.check( changedThing );
	}
	@Override
	protected void nameOfFireCall( Object listener, CollisionEvent event ) {
		if( listener instanceof CollisionStartListener ) {
			CollisionStartListener startCollisionEvent = (CollisionStartListener)listener;
			startCollisionEvent.collisionStarted( (StartCollisionEvent)event );
		} else if( listener instanceof CollisionEndListener ) {
			CollisionEndListener endCollisionEvent = (CollisionEndListener)listener;
			endCollisionEvent.collisionEnded( (EndCollisionEvent)event );
		}
	}
	@Override
	protected void ammend( Object key, int group, SThing newObject ) {
		ImplementationAccessor.getImplementation( newObject ).getSgComposite().addAbsoluteTransformationListener( this );
		collisionEventHandler.ammend( key, group, newObject );
	}

	private class CollisionEventHandler {

		HashMap<SThing,LinkedList<SThing>> checkMap = new HashMap<SThing,LinkedList<SThing>>();
		HashMap<SThing,HashMap<SThing,LinkedList<Object>>> internalEventMap = new HashMap<SThing,HashMap<SThing,LinkedList<Object>>>();
		HashMap<SThing,HashMap<SThing,Boolean>> wereTouchingMap = new HashMap<SThing,HashMap<SThing,Boolean>>();
		HashMap<Object,List<List<SThing>>> listenerToGroupMap = Collections.newHashMap();

		public void check( SThing changedThing ) {
			for( SThing m : checkMap.get( changedThing ) ) {
				LinkedList<Object> listenerList = internalEventMap.get( changedThing ).get( m );
				if( listenerList == null || listenerList.size() == 0 ) {
					return;
				}
				for( Object colList : listenerList ) {
					if( check( colList, m, changedThing ) ) {
						LinkedList<SThing> models = new LinkedList<SThing>();
						models.add( changedThing );
						models.add( m );
						if( colList instanceof CollisionStartListener ) {
							fireEvent( colList, EventBuilder.buildCollisionEvent( StartCollisionEvent.class, colList, models.toArray( new SMovableTurnable[ 0 ] ) ), models );
						} else if( colList instanceof CollisionEndListener ) {
							fireEvent( colList, EventBuilder.buildCollisionEvent( EndCollisionEvent.class, colList, models.toArray( new SMovableTurnable[ 0 ] ) ), models );
						}
					}
				}
				boolean doTheseCollide = AabbCollisionDetector.doTheseCollide( m, changedThing );
				wereTouchingMap.get( m ).put( changedThing, doTheseCollide );
				wereTouchingMap.get( changedThing ).put( m, doTheseCollide );
			}
		}
		public void ammend( Object key, int group, SThing newObject ) {
			EventBuilder.ammend( key, group, newObject );
			listenerToGroupMap.get( key ).get( group ).add( newObject );
			if( checkMap.get( newObject ) == null ) {
				checkMap.put( newObject, new LinkedList<SThing>() );
			}
			if( internalEventMap.get( newObject ) == null ) {
				internalEventMap.put( newObject, new HashMap<SThing,LinkedList<Object>>() );
			}
			if( wereTouchingMap.get( newObject ) == null ) {
				wereTouchingMap.put( newObject, new HashMap<SThing,Boolean>() );
			}
			for( int i = 0; i != listenerToGroupMap.get( key ).size(); ++i ) {
				if( i == group ) {
					//pass
				} else {
					for( SThing e : listenerToGroupMap.get( key ).get( i ) ) {
						//checkMap
						checkMap.get( e ).add( newObject );
						checkMap.get( newObject ).add( e );

						//eventMap
						if( internalEventMap.get( newObject ).get( e ) == null ) {
							internalEventMap.get( newObject ).put( e, new LinkedList<Object>() );
							internalEventMap.get( e ).put( newObject, new LinkedList<Object>() );
						}
						internalEventMap.get( newObject ).get( e ).add( key );
						internalEventMap.get( e ).get( newObject ).add( key );

						//wereTouchingMap
						wereTouchingMap.get( e ).put( newObject, false );
						wereTouchingMap.get( newObject ).put( e, false );
					}
				}
			}
		}
		private boolean check( Object colList, SThing m, SThing changedThing ) {
			if( colList instanceof CollisionStartListener ) {
				return !wereTouchingMap.get( m ).get( changedThing ) && AabbCollisionDetector.doTheseCollide( m, changedThing );
			} else if( colList instanceof CollisionEndListener ) {
				return wereTouchingMap.get( m ).get( changedThing ) && !AabbCollisionDetector.doTheseCollide( m, changedThing );
			}
			Logger.errln( "UNHANDLED CollisionListener TYPE", colList.getClass() );
			return false;
		}

		public void register( Object collisionListener, List<SThing> groupOne, List<SThing> groupTwo ) {
			List<List<SThing>> list = Collections.newLinkedList( groupOne, groupTwo );
			listenerToGroupMap.put( collisionListener, list );
			for( SThing m : groupOne ) {
				if( internalEventMap.get( m ) == null ) {
					internalEventMap.put( m, new HashMap<SThing,LinkedList<Object>>() );
					wereTouchingMap.put( m, new HashMap<SThing,Boolean>() );
					checkMap.put( m, new LinkedList<SThing>() );
				}
				for( SThing t : groupTwo ) {
					if( internalEventMap.get( m ).get( t ) == null ) {
						internalEventMap.get( m ).put( t, new LinkedList<Object>() );
					}
					if( !m.equals( t ) ) {
						internalEventMap.get( m ).get( t ).add( collisionListener );
						wereTouchingMap.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( SThing m : groupTwo ) {
				if( internalEventMap.get( m ) == null ) {
					internalEventMap.put( m, new HashMap<SThing,LinkedList<Object>>() );
					wereTouchingMap.put( m, new HashMap<SThing,Boolean>() );
					checkMap.put( m, new LinkedList<SThing>() );
				}
				for( SThing t : groupOne ) {
					if( internalEventMap.get( m ).get( t ) == null ) {
						internalEventMap.get( m ).put( t, new LinkedList<Object>() );
					}
					if( !m.equals( t ) ) {
						internalEventMap.get( m ).get( t ).add( collisionListener );
						wereTouchingMap.get( m ).put( t, AabbCollisionDetector.doTheseCollide( m, t ) );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
		}
	}
}

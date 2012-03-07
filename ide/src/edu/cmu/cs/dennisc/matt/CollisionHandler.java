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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MovableTurnable;
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
public class CollisionHandler extends TransformationChangedHandler<Object,CollisionEvent> {

	protected CollisionEventHandler collisionEventHandler = new CollisionEventHandler();

	public <A extends MovableTurnable, B extends MovableTurnable> void addCollisionListener( Object collisionListener, ArrayList<A> arrayList, Class<A> a, ArrayList<B> arrayList2, Class<B> b ) {
		registerIsFiringMap( collisionListener );
		registerPolicyMap( collisionListener, MultipleEventPolicy.IGNORE );
		Class<?>[] clsArr = { a, b };
		ArrayList<ArrayList<?>> list = Collections.newArrayList( (ArrayList<?>)arrayList, (ArrayList<?>)arrayList2 );
		EventBuilder.register( collisionListener, clsArr, list );
		List<Entity> allObserving = (List<Entity>)Collections.newArrayList( (Collection<? extends Entity>)arrayList );
		allObserving.addAll( (Collection<? extends Entity>)arrayList2 );
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		collisionEventHandler.register( collisionListener, new ArrayList<Entity>( (ArrayList<? extends Entity>)arrayList ), new ArrayList<Entity>( (ArrayList<? extends Entity>)arrayList2 ) );
	}
	@Override
	protected void check( Entity changedEntity ) {
		collisionEventHandler.check( changedEntity );
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

	private class CollisionEventHandler {

		HashMap<Entity,LinkedList<Entity>> checkMap = new HashMap<Entity,LinkedList<Entity>>();
		HashMap<Entity,HashMap<Entity,LinkedList<Object>>> eventMap = new HashMap<Entity,HashMap<Entity,LinkedList<Object>>>();
		HashMap<Entity,HashMap<Entity,Boolean>> wereTouchingMap = new HashMap<Entity,HashMap<Entity,Boolean>>();

		public void check( Entity changedEntity ) {
			for( Entity m : checkMap.get( changedEntity ) ) {
				LinkedList<Object> listenerList = eventMap.get( changedEntity ).get( m );
				if( listenerList == null || listenerList.size() == 0 ) {
					return;
				}
				for( Object colList : listenerList ) {
					if( check( colList, m, changedEntity ) ) {
						LinkedList<Entity> models = new LinkedList<Entity>();
						models.add( changedEntity );
						models.add( m );
						if( colList instanceof CollisionStartListener ) {
							fireEvent( colList, EventBuilder.buildEvent( StartCollisionEvent.class, colList, models.toArray( new MovableTurnable[ 0 ] ) ) );
						} else if( colList instanceof CollisionEndListener ) {
							fireEvent( colList, EventBuilder.buildEvent( EndCollisionEvent.class, colList, models.toArray( new MovableTurnable[ 0 ] ) ) );
						}
					}
				}
				boolean doTheseCollide = AabbCollisionDetector.doTheseCollide( m, changedEntity );
				wereTouchingMap.get( m ).put( changedEntity, doTheseCollide );
				wereTouchingMap.get( changedEntity ).put( m, doTheseCollide );
			}
		}
		private boolean check( Object colList, Entity m, Entity changedEntity ) {
			if( colList instanceof CollisionStartListener ) {
				return !wereTouchingMap.get( m ).get( changedEntity ) && AabbCollisionDetector.doTheseCollide( m, changedEntity );
			} else if( colList instanceof CollisionEndListener ) {
				return wereTouchingMap.get( m ).get( changedEntity ) && !AabbCollisionDetector.doTheseCollide( m, changedEntity );
			}
			Logger.errln( "UNHANDLED CollisionListener TYPE " + colList.getClass() );
			return false;
		}

		public void register( Object collisionListener, List<Entity> groupOne, List<Entity> groupTwo ) {
			for( Entity m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap<Entity,LinkedList<Object>>() );
					wereTouchingMap.put( m, new HashMap<Entity,Boolean>() );
					checkMap.put( m, new LinkedList<Entity>() );
				}
				for( Entity t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( collisionListener );
						wereTouchingMap.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( Entity m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap<Entity,LinkedList<Object>>() );
					wereTouchingMap.put( m, new HashMap<Entity,Boolean>() );
					checkMap.put( m, new LinkedList<Entity>() );
				}
				for( Entity t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( collisionListener );
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

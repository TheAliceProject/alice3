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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.SThing;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityEvent;
import org.lgna.story.event.ProximityExitListener;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class ProximityEventHandler extends TransformationChangedHandler<Object,ProximityEvent> {

	private ProximityEventManager proximityEventManager = new ProximityEventManager();

	public <A extends SMovableTurnable, B extends SMovableTurnable> void addProximityListener( Object proximityListener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Double dist, MultipleEventPolicy policy ) {
		ArrayList[] listArr = super.addPairedListener( proximityListener, groupOne, a, groupTwo, b, policy );
		proximityEventManager.register( proximityListener, new ArrayList<SThing>( (ArrayList<? extends SThing>)listArr[ 0 ] ), new ArrayList<SThing>( (ArrayList<? extends SThing>)listArr[ 1 ] ), dist );
	}

	@Override
	protected void ammend( Object key, int group, SThing newObject ) {
		ImplementationAccessor.getImplementation( newObject ).getSgComposite().addAbsoluteTransformationListener( this );
		proximityEventManager.ammend( key, group, newObject );
	}

	@Override
	protected void nameOfFireCall( Object listener, ProximityEvent e ) {
		if( listener instanceof ProximityEnterListener ) {
			ProximityEnterListener enter = (ProximityEnterListener)listener;
			enter.proximityEntered( (EnterProximityEvent)e );
		} else if( listener instanceof ProximityExitListener ) {
			ProximityExitListener exit = (ProximityExitListener)listener;
			exit.proximityExited( (ExitProximityEvent)e );
		}
	}

	@Override
	protected void check( SThing changedThing ) {
		proximityEventManager.check( changedThing );
	}

	protected class ProximityEventManager {

		Map<SThing,CopyOnWriteArrayList<SThing>> checkMap = new HashMap<SThing,CopyOnWriteArrayList<SThing>>();
		Map<SThing,HashMap<SThing,CopyOnWriteArrayList<Object>>> internalEventMap = new HashMap<SThing,HashMap<SThing,CopyOnWriteArrayList<Object>>>();
		Map<Object,HashMap<SThing,HashMap<SThing,Boolean>>> wereClose = new HashMap<Object,HashMap<SThing,HashMap<SThing,Boolean>>>();
		Map<Object,Double> distMap = new HashMap<Object,Double>();
		HashMap<Object,List<List<SThing>>> listenerToGroupMap = Collections.newHashMap();

		public void check( SThing changedThing ) {
			for( SThing m : checkMap.get( changedThing ) ) {
				for( Object proxList : internalEventMap.get( changedThing ).get( m ) ) {
					if( check( proxList, m, changedThing, distMap.get( proxList ) ) ) {
						LinkedList<SThing> models = new LinkedList<SThing>();
						models.add( changedThing );
						models.add( m );
						if( proxList instanceof ProximityEnterListener ) {
							fireEvent( proxList, EventBuilder.buildProximityEvent( EnterProximityEvent.class, proxList, models.toArray( new SMovableTurnable[ 0 ] ) ) );
						} else if( proxList instanceof ProximityExitListener ) {
							fireEvent( proxList, EventBuilder.buildProximityEvent( ExitProximityEvent.class, proxList, models.toArray( new SMovableTurnable[ 0 ] ) ) );
						}
					}
					boolean areTheseClose = AabbCollisionDetector.doTheseCollide( m, changedThing, distMap.get( proxList ) );
					wereClose.get( proxList ).get( m ).put( changedThing, areTheseClose );
					wereClose.get( proxList ).get( changedThing ).put( m, areTheseClose );
				}
			}
		}

		public void ammend( Object key, int group, SThing newObject ) {

			EventBuilder.ammend( key, group, newObject );
			listenerToGroupMap.get( key ).get( group ).add( newObject );
			if( checkMap.get( newObject ) == null ) {
				checkMap.put( newObject, new CopyOnWriteArrayList<SThing>() );
			}
			if( internalEventMap.get( newObject ) == null ) {
				internalEventMap.put( newObject, new HashMap<SThing,CopyOnWriteArrayList<Object>>() );
			}
			if( wereClose.get( key ) == null ) {
				wereClose.put( key, new HashMap<SThing,HashMap<SThing,Boolean>>() );
				wereClose.get( key ).put( newObject, new HashMap<SThing,Boolean>() );
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
							internalEventMap.get( newObject ).put( e, new CopyOnWriteArrayList<Object>() );
							internalEventMap.get( e ).put( newObject, new CopyOnWriteArrayList<Object>() );
						}
						internalEventMap.get( newObject ).get( e ).add( key );
						internalEventMap.get( e ).get( newObject ).add( key );

						//wereTouchingMap
						wereClose.get( key ).get( newObject ).put( e, false );
						wereClose.get( key ).get( e ).put( newObject, false );
					}
				}
			}
		}

		private boolean check( Object proxList, SThing m, SThing changedThing, Double dist ) {
			if( proxList instanceof ProximityEnterListener ) {
				return !wereClose.get( proxList ).get( m ).get( changedThing ) && AabbCollisionDetector.doTheseCollide( m, changedThing, dist );
			} else if( proxList instanceof ProximityExitListener ) {
				return wereClose.get( proxList ).get( m ).get( changedThing ) && !AabbCollisionDetector.doTheseCollide( m, changedThing, dist );
			}
			return false;
		}

		public void register( Object proximityEventListener, List<SThing> groupOne, List<SThing> groupTwo, Double dist ) {
			distMap.put( proximityEventListener, dist );
			wereClose.put( proximityEventListener, new HashMap<SThing,HashMap<SThing,Boolean>>() );
			for( SThing m : groupOne ) {
				if( internalEventMap.get( m ) == null ) {
					internalEventMap.put( m, new HashMap<SThing,CopyOnWriteArrayList<Object>>() );
					checkMap.put( m, new CopyOnWriteArrayList<SThing>() );
				}
				wereClose.get( proximityEventListener ).put( m, new HashMap<SThing,Boolean>() );
				for( SThing t : groupTwo ) {
					if( internalEventMap.get( m ).get( t ) == null ) {
						internalEventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						internalEventMap.get( m ).get( t ).add( proximityEventListener );
						wereClose.get( proximityEventListener ).get( m ).put( t, false );
						checkMap.get( m ).add( t );
					}
				}
			}
			for( SThing m : groupTwo ) {
				if( internalEventMap.get( m ) == null ) {
					internalEventMap.put( m, new HashMap<SThing,CopyOnWriteArrayList<Object>>() );
					checkMap.put( m, new CopyOnWriteArrayList<SThing>() );
				}
				wereClose.get( proximityEventListener ).put( m, new HashMap<SThing,Boolean>() );
				for( SThing t : groupOne ) {
					if( internalEventMap.get( m ).get( t ) == null ) {
						internalEventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						internalEventMap.get( m ).get( t ).add( proximityEventListener );
						wereClose.get( proximityEventListener ).get( m ).put( t, false );
						checkMap.get( m ).add( t );
					}
				}
			}
		}
	}
}

/*
 * Copyright (c) 2006-2013, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.implementation.eventhandling;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.concurrent.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.matt.EndOcclusionEvent;

/**
 * @author Matt May
 */
public class OcclusionHandler extends TransformationChangedHandler<Object, OcclusionEvent> {

	private OcclusionEventHandler occlusionEventHandler = new OcclusionEventHandler();
	private CameraImp camera;

	public void addOcclusionEvent( Object occlusionEventListener, List<SModel> groupOne, List<SModel> groupTwo ) {
		registerIsFiringMap( occlusionEventListener );
		registerPolicyMap( occlusionEventListener, MultipleEventPolicy.IGNORE );
		List<SModel> allObserving = Collections.newCopyOnWriteArrayList( groupOne );
		allObserving.addAll( groupTwo );
		if( ( groupOne.size() > 0 ) && ( groupOne.get( 0 ) != null ) && ( camera == null ) ) {
			camera = ImplementationAccessor.getImplementation( groupOne.get( 0 ) ).getScene().findFirstCamera();
			camera.getSgComposite().addAbsoluteTransformationListener( this );
		}
		for( SThing m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		occlusionEventHandler.register( occlusionEventListener, groupOne, groupTwo );
	}

	@Override
	protected void check( SThing changedEntity ) {
		occlusionEventHandler.check( changedEntity );
	}

	@Override
	protected void nameOfFireCall( Object listener, OcclusionEvent event ) {
		if( listener instanceof OcclusionStartListener ) {
			OcclusionStartListener start = (OcclusionStartListener)listener;
			start.occlusionStarted( (StartOcclusionEvent)event );
		} else if( listener instanceof OcclusionEndListener ) {
			OcclusionEndListener start = (OcclusionEndListener)listener;
			start.occlusionEnded( (EndOcclusionEvent)event );
		}
	}

	private class OcclusionEventHandler {

		private Map<SModel, CopyOnWriteArrayList<SModel>> checkMap = new ConcurrentHashMap<SModel, CopyOnWriteArrayList<SModel>>();
		private Map<SModel, Map<SModel, CopyOnWriteArrayList<Object>>> eventMap = new ConcurrentHashMap<SModel, Map<SModel, CopyOnWriteArrayList<Object>>>();
		private Map<SModel, Map<SModel, Boolean>> wereOccluded = new ConcurrentHashMap<SModel, Map<SModel, Boolean>>();

		public void check( SThing changedEntity ) {
			if( camera == null ) {
				camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
				if( camera == null ) {
					return;
				}
			}
			if( changedEntity.equals( camera.getAbstraction() ) ) {
				for( SModel t : checkMap.keySet() ) {
					for( SModel m : checkMap.get( t ) ) {
						for( Object occList : eventMap.get( t ).get( m ) ) {
							if( check( occList, t, m ) ) {
								CopyOnWriteArrayList<SMovableTurnable> models = new CopyOnWriteArrayList<SMovableTurnable>();
								if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( t ) ) ) {
									models.add( (SMovableTurnable)m );
									models.add( (SMovableTurnable)t );
								} else {
									models.add( (SMovableTurnable)t );
									models.add( (SMovableTurnable)m );
								}
								if( occList instanceof OcclusionStartListener ) {
									fireEvent( occList, new StartOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
								} else if( occList instanceof OcclusionEndListener ) {
									fireEvent( occList, new EndOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
								}
							} else {

							}
						}
						boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, t, m );
						wereOccluded.get( t ).put( m, doTheseOcclude );
						wereOccluded.get( m ).put( t, doTheseOcclude );
					}
				}
			} else {
				for( SModel m : checkMap.get( changedEntity ) ) {
					for( Object occList : eventMap.get( changedEntity ).get( m ) ) {
						if( check( occList, (SModel)changedEntity, m ) ) {
							CopyOnWriteArrayList<SMovableTurnable> models = new CopyOnWriteArrayList<SMovableTurnable>();
							if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( changedEntity ) ) ) {
								models.add( (SMovableTurnable)m );
								models.add( (SMovableTurnable)changedEntity );
							} else {
								models.add( (SMovableTurnable)changedEntity );
								models.add( (SMovableTurnable)m );
							}
							if( occList instanceof OcclusionStartListener ) {
								fireEvent( occList, new StartOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
							} else if( occList instanceof OcclusionEndListener ) {
								fireEvent( occList, new EndOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
							}
						}
					}
					boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
					wereOccluded.get( changedEntity ).put( m, AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m ) );
					wereOccluded.get( m ).put( (SModel)changedEntity, AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity ) );
				}
			}
		}

		private boolean check( Object occList, SModel changedEntity, SModel m ) {
			if( occList instanceof OcclusionStartListener ) {
				return !wereOccluded.get( changedEntity ).get( m ) && AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
			} else if( occList instanceof OcclusionEndListener ) {
				return wereOccluded.get( changedEntity ).get( m ) && !AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
			}
			Logger.errln( "UNHANDLED OcclusionListener TYPE", occList.getClass() );
			return false;
		}

		public void register( Object occlusionListener, List<SModel> groupOne, List<SModel> groupTwo ) {
			for( SModel m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new ConcurrentHashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new ConcurrentHashMap<SModel, Boolean>() );
					checkMap.put( m, new CopyOnWriteArrayList<SModel>() );
				}
				for( SModel t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( SModel m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new ConcurrentHashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new ConcurrentHashMap<SModel, Boolean>() );
					checkMap.put( m, new CopyOnWriteArrayList<SModel>() );
				}
				for( SModel t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.story.implementation.eventhandling;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SThing;
import org.lgna.story.event.EnterViewEvent;
import org.lgna.story.event.ExitViewEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class ViewEventHandler extends TransformationChangedHandler<Object, ViewEvent> {

	private CameraImp camera;
	private final Map<SModel, List<Object>> map = Maps.newConcurrentHashMap();
	private final Map<SModel, Boolean> wasInView = Maps.newConcurrentHashMap();

	@Override
	protected void check( SThing changedEntity ) {
		if( camera == null ) { //should not really be hit
			camera = EmployeesOnly.getImplementation( changedEntity ).getScene().findFirstCamera();
			if( camera == null ) {
				return;
			} else {
				camera.getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		if( changedEntity == camera.getAbstraction() ) {
			for( SModel model : map.keySet() ) {
				for( Object listener : map.get( model ) ) {
					if( check( listener, model ) ) {
						ViewEvent event = listener instanceof ViewEnterListener ? new EnterViewEvent( model ) : new ExitViewEvent( model );
						fireEvent( listener, event );
					}
				}
				wasInView.put( model, IsInViewDetector.isThisInView( model, camera ) );
			}
		} else {
			for( Object listener : map.get( changedEntity ) ) {
				if( check( listener, changedEntity ) ) {
					ViewEvent event = listener instanceof ViewEnterListener ? new EnterViewEvent( (SModel)changedEntity ) : new ExitViewEvent( (SModel)changedEntity );
					fireEvent( listener, event );
				}
			}
			wasInView.put( (SModel)changedEntity, IsInViewDetector.isThisInView( changedEntity, camera ) );
		}
	}

	private boolean check( Object listener, SThing changedEntity ) {
		boolean rv = false;
		boolean thisInView = IsInViewDetector.isThisInView( changedEntity, camera );
		if( wasInView.get( changedEntity ) == null ) {
			wasInView.put( (SModel)changedEntity, thisInView );
			return false;
		}
		if( listener instanceof ViewEnterListener ) {
			if( thisInView && !wasInView.get( changedEntity ) ) {
				rv = true;
			}
		} else if( listener instanceof ViewExitListener ) {
			if( !thisInView && wasInView.get( changedEntity ) ) {
				rv = true;
			}
		}
		return rv;
	}

	@Override
	protected void fire( Object listener, ViewEvent event ) {
		if( listener instanceof ViewEnterListener ) {
			ViewEnterListener intoViewEL = (ViewEnterListener)listener;
			intoViewEL.viewEntered( (EnterViewEvent)event );
		} else if( listener instanceof ViewExitListener ) {
			ViewExitListener outOfViewEL = (ViewExitListener)listener;
			outOfViewEL.viewExited( (ExitViewEvent)event );
		}
	}

	public void addViewEventListener( Object listener, SModel[] models, MultipleEventPolicy policy ) {
		registerIsFiringMap( listener );
		registerPolicyMap( listener, policy );
		for( SModel m : models ) {
			if( !getModelList().contains( m ) ) {
				getModelList().add( m );
				EmployeesOnly.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				if( camera == null ) {
					camera = EmployeesOnly.getImplementation( m ).getScene().findFirstCamera();
					camera.getSgComposite().addAbsoluteTransformationListener( this );
				}
			}
		}
		for( SModel model : models ) {
			if( map.get( model ) == null ) {
				map.put( model, new CopyOnWriteArrayList<Object>() );
			}
			map.get( model ).add( listener );
		}
	}
}

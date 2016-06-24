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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class TransformationHandler extends TransformationChangedHandler<PointOfViewChangeListener, PointOfViewEvent> {

	private final Map<SThing, List<PointOfViewChangeListener>> checkMap = Maps.newConcurrentHashMap();

	public void addTransformationListener( PointOfViewChangeListener transformationlistener, SThing[] shouldListenTo ) {
		registerIsFiringMap( transformationlistener );
		registerPolicyMap( transformationlistener, MultipleEventPolicy.IGNORE );
		List<SThing> allObserving = Lists.newCopyOnWriteArrayList( shouldListenTo );
		for( SThing m : allObserving ) {
			if( !getModelList().contains( m ) ) {
				getModelList().add( m );
				EmployeesOnly.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				//				collisionEventHandler.register( collisionListener, groupOne, groupTwo );
			}
		}
		for( SThing e : shouldListenTo ) {
			if( checkMap.get( e ) == null ) {
				checkMap.put( e, new LinkedList<PointOfViewChangeListener>() );
			}
			checkMap.get( e ).add( transformationlistener );
		}
	}

	@Override
	protected void check( SThing changedEntity ) {
		for( PointOfViewChangeListener listener : checkMap.get( changedEntity ) ) {
			fireEvent( listener, new PointOfViewEvent( changedEntity ) );
		}
	}

	@Override
	protected void fire( PointOfViewChangeListener listener, PointOfViewEvent event ) {
		listener.pointOfViewChanged( event );
	}
}

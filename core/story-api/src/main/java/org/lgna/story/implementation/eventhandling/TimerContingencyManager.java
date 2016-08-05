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

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.EnterViewEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ExitViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

/**
 * @author Matt May
 */
public class TimerContingencyManager {

	private TimerEventHandler timer;
	private SScene scene;

	public TimerContingencyManager( TimerEventHandler timer ) {
		this.timer = timer;
	}

	public void register( WhileCollisionListener listener, List<SThing> groupOne, List<SThing> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addCollisionStartListener( newStartCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
		scene.addCollisionEndListener( newEndCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
	}

	public void register( WhileProximityListener listener, List<SThing> groupOne, List<SThing> groupTwo, Double dist, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addProximityEnterListener( newEnterProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
		scene.addProximityExitListener( newExitProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
	}

	public void register( WhileOcclusionListener listener, List<SModel> groupOne, List<SModel> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addOcclusionStartListener( newEnterOcclusionAdapter( listener ), (SModel[])toArray( groupOne ), (SModel[])toArray( groupTwo ) );
		scene.addOcclusionEndListener( newExitOcclusionAdapter( listener ), (SModel[])toArray( groupOne ), (SModel[])toArray( groupTwo ) );
	}

	public void register( WhileInViewListener listener, List<SModel> group, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addViewEnterListener( newEnterViewAdapter( listener ), (SModel[])toArray( group ) );
		scene.addViewExitListener( newExitViewAdapter( listener ), (SModel[])toArray( group ) );
	}

	private ViewExitListener newExitViewAdapter( final WhileInViewListener listener ) {
		return new ViewExitListener() {
			@Override
			public void viewExited( ExitViewEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ViewEnterListener newEnterViewAdapter( final WhileInViewListener listener ) {
		return new ViewEnterListener() {
			@Override
			public void viewEntered( EnterViewEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionStartListener newEnterOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionStartListener() {
			@Override
			public void occlusionStarted( StartOcclusionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionEndListener newExitOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionEndListener() {
			@Override
			public void occlusionEnded( EndOcclusionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ProximityEnterListener newEnterProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityEnterListener() {
			@Override
			public void proximityEntered( EnterProximityEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private ProximityExitListener newExitProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityExitListener() {
			@Override
			public void proximityExited( ExitProximityEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private CollisionEndListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionEndListener() {
			@Override
			public void collisionEnded( EndCollisionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private CollisionStartListener newStartCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionStartListener() {
			@Override
			public void collisionStarted( StartCollisionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private SThing[] toArray( List<? extends SThing> arr ) {
		SThing[] rv = new SThing[ arr.size() ];
		for( int i = 0; i != arr.size(); ++i ) {
			rv[ i ] = arr.get( i );
		}
		return rv;
	}

	public void setScene( SceneImp scene ) {
		this.scene = scene.getAbstraction();
	}
}

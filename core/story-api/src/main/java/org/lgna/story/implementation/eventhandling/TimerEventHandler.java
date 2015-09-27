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

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeEvent;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.WhileContingencyListener;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;

/**
 * @author Matt May
 */
public class TimerEventHandler extends AbstractEventHandler<TimeListener, TimeEvent> implements SceneActivationListener {

	private final Map<TimeListener, Double> freqMap = Maps.newConcurrentHashMap();
	private final List<TimeListener> timerList = Lists.newCopyOnWriteArrayList();
	private final Map<TimeListener, Double> mostRecentFire = Maps.newConcurrentHashMap();
	private final Map<TimeListener, Boolean> activationMap = Maps.newConcurrentHashMap();
	private Double currentTime;
	private boolean isEnabled = false;
	private boolean isActivated = false;

	private final AutomaticDisplayListener automaticDisplayListener = new AutomaticDisplayListener() {
		@Override
		public void automaticDisplayCompleted( AutomaticDisplayEvent e ) {
			currentTime = scene.getProgram().getAnimator().getCurrentTime();
			update();
		}
	};

	public void enable() {
		isEnabled = true;
		edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().addAutomaticDisplayListener( this.automaticDisplayListener );
	}

	public void disable() {
		isEnabled = false;
		edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().removeAutomaticDisplayListener( this.automaticDisplayListener );
	}

	public void addListener( TimeListener timerEventListener, Double frequency, MultipleEventPolicy policy ) {
		activationMap.put( timerEventListener, true );
		if( !isEnabled ) {
			enable();
		}
		registerPolicyMap( timerEventListener, policy );
		registerIsFiringMap( timerEventListener );
		freqMap.put( timerEventListener, frequency );
		mostRecentFire.put( timerEventListener, Double.MIN_VALUE );
		timerList.add( timerEventListener );
	}

	private void update() {
		for( TimeListener listener : timerList ) {
			if( timeToFire( listener ) ) {
				double timeElapsed = currentTime - mostRecentFire.get( listener );
				trigger( listener, new TimeEvent( timeElapsed ) );
				mostRecentFire.put( listener, currentTime );
			}
		}
	}

	private void trigger( TimeListener listener, TimeEvent timerEvent ) {
		if( isActivated ) {
			fireEvent( listener, timerEvent );
		}
	}

	private boolean timeToFire( TimeListener listener ) {
		return ( ( ( currentTime - mostRecentFire.get( listener ) ) > freqMap.get( listener ) ) && activationMap.get( listener ) );
	}

	@Override
	protected void fire( TimeListener listener, TimeEvent event ) {
		listener.timeElapsed( new TimeEvent( event.getTimeSinceLastFire() ) );
	}

	@Override
	public void sceneActivated( SceneActivationEvent e ) {
		this.isActivated = true;
	}

	public void deactivate( WhileContingencyListener listener ) {
		activationMap.put( listener, false );
	}

	public void activate( WhileContingencyListener listener ) {
		activationMap.put( listener, true );
	}
}

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

package org.lgna.story;

import org.lgna.project.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class Scene extends Entity{
	private final org.lgna.story.implementation.SceneImp implementation = new org.lgna.story.implementation.SceneImp( this );

	@Override
	/*package-private*/org.lgna.story.implementation.SceneImp getImplementation() {
		return this.implementation;
	}

	protected abstract void handleActiveChanged( Boolean isActive, Integer activationCount );

	protected void preserveStateAndEventListeners() {
		this.implementation.preserveStateAndEventListeners();
	}
	protected void restoreStateAndEventListeners() {
		this.implementation.restoreStateAndEventListeners();
	}

	@GetterTemplate(isPersistent = true)
	@MethodTemplate()
	public Color getAtmosphereColor() {
		return this.implementation.atmosphereColor.getValue();
	}
	@MethodTemplate()
	public void setAtmosphereColor( Color color, SetAtmosphereColor.Detail... details ) {
		this.implementation.atmosphereColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@GetterTemplate(isPersistent = false)
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	@Deprecated
	public Color getAmbientLightColor() {
		return this.implementation.fromAboveLightColor.getValue();
	}
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	@Deprecated
	public void setAmbientLightColor( Color color, SetAmbientLightColor.Detail... details ) {
		this.implementation.fromAboveLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@GetterTemplate(isPersistent = true)
	@MethodTemplate()
	public Color getFromAboveLightColor() {
		return this.implementation.fromAboveLightColor.getValue();
	}
	@MethodTemplate()
	public void setFromAboveLightColor( Color color, SetFromAboveLightColor.Detail... details ) {
		this.implementation.fromAboveLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@GetterTemplate(isPersistent = true)
	@MethodTemplate()
	public Color getFromBelowLightColor() {
		return this.implementation.fromBelowLightColor.getValue();
	}
	@MethodTemplate()
	public void setFromBelowLightColor( Color color, SetFromBelowLightColor.Detail... details ) {
		this.implementation.fromBelowLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.PortionDetails.class)
	public Double getFogDensity() {
		return (double)this.getImplementation().fogDensity.getValue();
	}
	@MethodTemplate()
	public void setFogDensity( Number density, SetFogDensity.Detail... details ) {
		this.getImplementation().fogDensity.animateValue( density.floatValue(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.implementation.addSceneActivationListener( sceneActivationListener );
	}
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.implementation.removeSceneActivationListener( sceneActivationListener );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addMouseButtonListener( org.lgna.story.event.MouseButtonListener mouseButtonListener, AddMouseButtonListener.Detail... details ) {
		this.getImplementation().getEventManager().addMouseButtonListener( mouseButtonListener, MultipleEventPolicy.getValue( details ), SetOfVisuals.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public void removeMouseButtonListener( org.lgna.story.event.MouseButtonListener mouseButtonListener ) {
		this.getImplementation().getEventManager().removeMouseButtonListener( mouseButtonListener );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addKeyPressedListener( org.lgna.story.event.KeyListener keyListener,  AddKeyPressedListener.Detail... details) {
		this.implementation.getEventManager().addKeyListener( keyListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public void removeKeyListener( org.lgna.story.event.KeyListener keyListener ) {
		this.implementation.getEventManager().removeKeyListener( keyListener );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addCollisionListener( org.lgna.story.event.CollisionListener collisionListener, Entity[] groupOne, Entity[] groupTwo){
		this.getImplementation().getEventManager().addCollisionListener(collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo));
	}
	
	//todo: removeCollisionListener
	
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	public void addProximityEventListener( org.lgna.story.event.ProximityEventListener proximityEventListener, Entity[] groupOne, Entity[] groupTwo, AddProximityEventListener.Detail... details){
		this.getImplementation().getEventManager().addProximityEventListener(proximityEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), AddProximityEventListener.getDist( details ));
	}

	//todo: removeProximityListener

	
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addTimerEventListener(org.lgna.story.event.TimerEventListener timerEventListener, AddTimerEventListener.Detail... details) {
		this.getImplementation().getEventManager().addTimerEventListener(timerEventListener, TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details));
	}

	//todo: removeTimerListener
}

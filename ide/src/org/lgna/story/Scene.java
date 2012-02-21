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

import org.lgna.project.annotations.AddEventListenerTemplate;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.event.EnterViewListener;
import org.lgna.story.event.ExitViewListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.WhileInViewListener;

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

	//Mouse
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addMouseClickOnScreenListener( MouseClickOnScreenListener listener, AddMouseButtonListener.Detail... details ) {
		this.implementation.getEventManager().addMouseClickOnScreenListener( listener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addMouseClickOnObjectListener( MouseClickOnObjectListener listener, AddMouseButtonListener.Detail... details ) {
		this.implementation.getEventManager().addMouseClickOnObjectListener( listener, MultipleEventPolicy.getValue( details ), SetOfVisuals.getValue( details ) );
	}
	
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addDefaultModelManipulation() {
		this.getImplementation().getEventManager().addDragAdapter();
	}
	
	//time/Scene
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addTimerEventListener(TimeListener timerEventListener, AddTimerEventListener.Detail... details) {
		this.getImplementation().getEventManager().addTimerEventListener(timerEventListener, TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener,
			AddSceneActivationListener.Detail... details ) {
		this.implementation.addSceneActivationListener( sceneActivationListener );
	}

	//keyListeners
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addKeyPressListener( org.lgna.story.event.KeyPressListener keyListener,
			AddKeyPressListener.Detail... details) {
		this.implementation.getEventManager().addKeyListener( keyListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addArrowKeyPressListener( org.lgna.story.event.ArrowKeyPressListener keyPressListener,
			AddKeyPressListener.Detail... details) {
		this.getImplementation().getEventManager().addArrowKeyListener( keyPressListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addNumberKeyPressListener( org.lgna.story.event.NumberKeyPressListener keyPressListener,
			AddKeyPressListener.Detail... details) {
		this.getImplementation().getEventManager().addNumberKeyListener( keyPressListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addObjectMoverFor( MovableTurnable entity, AddObjectMoverFor.Detail... details ) {
		this.implementation.getEventManager().moveWithArrows( entity );
	}
	
	//TransformationListeners
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addPositionOrientationChangeListener( org.lgna.story.event.TransformationListener transformationlistener,
			Entity[] shouldListenTo, AddPositionOrientationChangeListener.Detail... details ) {
		this.getImplementation().getEventManager().addTransformationListener( transformationlistener, shouldListenTo );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addStartCollisionListener( org.lgna.story.event.StartCollisionListener collisionListener,
			Entity[] groupOne, Entity[] groupTwo, AddStartCollisionListener.Detail... details ){
		this.getImplementation().getEventManager().addCollisionListener(collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addWhileCollisionListener( org.lgna.story.event.WhileCollisionListener collisionListener,
			Entity[] groupOne, Entity[] groupTwo, AddTimerEventListener.Detail... details ){
		this.getImplementation().getEventManager().addWhileCollisionListener(collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details, MultipleEventPolicy.IGNORE));
	}	
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addEndCollisionListener( org.lgna.story.event.EndCollisionListener collisionListener,
			Entity[] groupOne, Entity[] groupTwo, AddEndCollisionListener.Detail... details ){
		this.getImplementation().getEventManager().addCollisionListener(collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addEnterProximityEventListener( org.lgna.story.event.EnterProximityListener proximityEventListener,
			Entity[] groupOne, Entity[] groupTwo, Double distance, AddEnterProximityEventListener.Detail... details ){
		this.getImplementation().getEventManager().addProximityEventListener(proximityEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), distance );// AddEnterProximityEventListener.getDist( details ));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addWhileProximityListener( org.lgna.story.event.WhileProximityListener proximityListener,
			Entity[] groupOne, Entity[] groupTwo, Double distance, AddTimerEventListener.Detail... details ){
		this.getImplementation().getEventManager().addWhileProximityListener(proximityListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), distance, TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details, MultipleEventPolicy.IGNORE));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addEnterViewListener( EnterViewListener listener, Entity[] entities,
			AddEnterViewListener.Detail... details ) {
		this.implementation.getEventManager().addComesIntoViewEventListener( listener, entities );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addWhileInViewListener( WhileInViewListener listener, Entity[] entities,
			AddTimerEventListener.Detail... details ) {
		this.implementation.getEventManager().addWhileInViewListener( listener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( entities ), TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details, MultipleEventPolicy.IGNORE ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addExitProximityEventListener( org.lgna.story.event.ExitProximityListener proximityEventListener,
			Entity[] groupOne, Entity[] groupTwo, Double distance, AddExitProximityEventListener.Detail... details ){
		this.getImplementation().getEventManager().addProximityEventListener(proximityEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), distance );// AddExitProximityEventListener.getDist( details ));
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addWhileOcclusionListener( org.lgna.story.event.WhileOcclusionListener occlusionListener,
			Entity[] groupOne, Entity[] groupTwo, AddTimerEventListener.Detail... details ){
		this.getImplementation().getEventManager().addWhileOcclusionListener( occlusionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo), TimerFrequency.getValue(details).getFrequency(), MultipleEventPolicy.getValue(details, MultipleEventPolicy.IGNORE ) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addExitViewListener( ExitViewListener listener, Entity[] entities, AddExitViewListener.Detail...details ) {
		this.implementation.getEventManager().addLeavesViewEventListener( listener, entities );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addStartOcclusionListener(  org.lgna.story.event.StartOcclusionListener occlusionEventListener,
			Entity[] groupOne, Entity[] groupTwo, AddStartOcclusionListener.Detail... details ){
		this.getImplementation().getEventManager().addOcclusionEventListener( occlusionEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo) );
	}
	@MethodTemplate(visibility=Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addEndOcclusionListener(  org.lgna.story.event.EndOcclusionListener occlusionEventListener,
			Entity[] groupOne, Entity[] groupTwo, AddEndOcclusionListener.Detail... details ){
		this.getImplementation().getEventManager().addOcclusionEventListener( occlusionEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupOne), edu.cmu.cs.dennisc.java.util.Collections.newArrayList(groupTwo) );
	}

	//remove
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public void removeKeyListener( org.lgna.story.event.KeyPressListener keyListener ) {
		this.implementation.getEventManager().removeKeyListener( keyListener );
	}
	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.implementation.removeSceneActivationListener( sceneActivationListener );
	}
}

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
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileInViewListener;

/**
 * @author Dennis Cosgrove
 */
public abstract class Scene extends Entity {
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
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@Deprecated
	public Color getAmbientLightColor() {
		return this.implementation.fromAboveLightColor.getValue();
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
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
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addMouseClickOnScreenListener( MouseClickOnScreenListener listener, AddMouseButtonListener.Detail... details ) {
		this.implementation.getEventManager().addMouseClickOnScreenListener( listener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	@Deprecated
	public void addMouseClickOnObjectListener( MouseClickOnObjectListener listener, AddMouseButtonListener.Detail... details ) {
		this.implementation.getEventManager().addMouseClickOnObjectListener( listener, Model.class, MultipleEventPolicy.getValue( details ), SetOfVisuals.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public <T extends Model> void addMouseClickOnObjectListener( MouseClickOnObjectListener<T> listener, Class<T> cls, AddMouseButtonListener.Detail... details ) {
		this.implementation.getEventManager().addMouseClickOnObjectListener( listener, cls, MultipleEventPolicy.getValue( details ), SetOfVisuals.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addDefaultModelManipulation() {
		this.getImplementation().getEventManager().addDragAdapter();
	}

	//time/Scene
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addTimeListener( TimeListener timeListener, AddTimeListener.Detail... details ) {
		this.getImplementation().getEventManager().addTimerEventListener( timeListener, TimerFrequency.getValue( details ).getFrequency(), MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener, AddSceneActivationListener.Detail... details ) {
		this.implementation.addSceneActivationListener( sceneActivationListener );
	}

	//keyListeners
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addKeyPressListener( org.lgna.story.event.KeyPressListener keyListener, AddKeyPressListener.Detail... details ) {
		this.implementation.getEventManager().addKeyListener( keyListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addArrowKeyPressListener( org.lgna.story.event.ArrowKeyPressListener keyPressListener, AddKeyPressListener.Detail... details ) {
		this.getImplementation().getEventManager().addArrowKeyListener( keyPressListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addNumberKeyPressListener( org.lgna.story.event.NumberKeyPressListener keyPressListener, AddKeyPressListener.Detail... details ) {
		this.getImplementation().getEventManager().addNumberKeyListener( keyPressListener, MultipleEventPolicy.getValue( details ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addObjectMoverFor( MovableTurnable entity, AddObjectMoverFor.Detail... details ) {
		this.implementation.getEventManager().moveWithArrows( entity );
	}

	//TransformationListeners
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addPointOfViewChangeListener( org.lgna.story.event.PointOfViewChangeListener transformationlistener, Entity[] shouldListenTo, AddPositionOrientationChangeListener.Detail... details ) {
		this.getImplementation().getEventManager().addTransformationListener( transformationlistener, shouldListenTo );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	@Deprecated
	public void addCollisionStartListener( org.lgna.story.event.CollisionStartListener collisionListener, Entity[] groupOne, Entity[] groupTwo, AddStartCollisionListener.Detail... details ) {
		//		this.getImplementation().getEventManager().addCollisionListener( collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ) );
	}
	public <A extends MovableTurnable, B extends MovableTurnable> void addCollisionStartListener( CollisionStartListener<A,B> collisionStartListener, Class<A> a, Class<B> b, AddStartCollisionListener.Detail... details ) {
		this.getImplementation().getEventManager().addCollisionListener( collisionStartListener, a, b, AddStartCollisionListener.getGroupOne( details, a ), AddStartCollisionListener.getGroupOne( details, b ) );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@AddEventListenerTemplate()
	public void addWhileCollisionListener( org.lgna.story.event.WhileCollisionListener collisionListener, Entity[] groupOne, Entity[] groupTwo, AddTimeListener.Detail... details ) {
		this.getImplementation()
				.getEventManager()
				.addWhileCollisionListener( collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ), TimerFrequency.getValue( details ).getFrequency(),
						MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}

	public <A extends MovableTurnable, B extends MovableTurnable> void addCollisionEndListener( CollisionEndListener<A,B> collisionEndListener, Class<A> a, Class<B> b, AddEndCollisionListener.Detail... details ) {
		this.getImplementation().getEventManager().addCollisionListener( collisionEndListener, a, b, AddEndCollisionListener.getGroupOne( details, a ), AddEndCollisionListener.getGroupTwo( details, b ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	@Deprecated
	public void addCollisionEndListener( org.lgna.story.event.CollisionEndListener collisionListener, Entity[] groupOne, Entity[] groupTwo, AddEndCollisionListener.Detail... details ) {
		//		this.getImplementation().getEventManager().addCollisionListener( collisionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addProximityEnterListener( org.lgna.story.event.ProximityEnterListener proximityEventListener, Entity[] groupOne, Entity[] groupTwo, Double distance, AddEnterProximityEventListener.Detail... details ) {
		this.getImplementation().getEventManager().addProximityEventListener( proximityEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ), distance );// AddEnterProximityEventListener.getDist( details ));
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@AddEventListenerTemplate()
	public void addWhileProximityListener( org.lgna.story.event.WhileProximityListener proximityListener, Entity[] groupOne, Entity[] groupTwo, Double distance, AddTimeListener.Detail... details ) {
		this.getImplementation()
				.getEventManager()
				.addWhileProximityListener( proximityListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ), distance,
						TimerFrequency.getValue( details ).getFrequency(), MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addViewEnterListener( ViewEnterListener listener, Model[] models, AddEnterViewListener.Detail... details ) {
		this.implementation.getEventManager().addComesIntoViewEventListener( listener, models );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@AddEventListenerTemplate()
	public void addWhileInViewListener( WhileInViewListener listener, Model[] models, AddTimeListener.Detail... details ) {
		this.implementation.getEventManager().addWhileInViewListener( listener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( models ), TimerFrequency.getValue( details ).getFrequency(),
				MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addProximityExitListener( org.lgna.story.event.ProximityExitListener proximityEventListener, Entity[] groupOne, Entity[] groupTwo, Double distance, AddExitProximityEventListener.Detail... details ) {
		this.getImplementation().getEventManager().addProximityEventListener( proximityEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ), distance );// AddExitProximityEventListener.getDist( details ));
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addViewExitListener( ViewExitListener listener, Model[] entities, AddExitViewListener.Detail... details ) {
		this.implementation.getEventManager().addLeavesViewEventListener( listener, entities );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addOcclusionStartListener( org.lgna.story.event.OcclusionStartListener occlusionEventListener, Model[] groupOne, Model[] groupTwo, AddStartOcclusionListener.Detail... details ) {
		this.getImplementation().getEventManager().addOcclusionEventListener( occlusionEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ) );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@AddEventListenerTemplate()
	public void addWhileOcclusionListener( org.lgna.story.event.WhileOcclusionListener occlusionListener, Model[] groupOne, Model[] groupTwo, AddTimeListener.Detail... details ) {
		this.getImplementation()
				.getEventManager()
				.addWhileOcclusionListener( occlusionListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ), TimerFrequency.getValue( details ).getFrequency(),
						MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	@AddEventListenerTemplate()
	public void addOcclusionEndListener( org.lgna.story.event.OcclusionEndListener occlusionEventListener, Model[] groupOne, Model[] groupTwo, AddEndOcclusionListener.Detail... details ) {
		this.getImplementation().getEventManager().addOcclusionEventListener( occlusionEventListener, edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupOne ), edu.cmu.cs.dennisc.java.util.Collections.newArrayList( groupTwo ) );
	}

	//remove
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public void removeKeyListener( org.lgna.story.event.KeyPressListener keyListener ) {
		this.implementation.getEventManager().removeKeyListener( keyListener );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.implementation.removeSceneActivationListener( sceneActivationListener );
	}
}

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

package org.lgna.story;

import org.lgna.project.annotations.AddEventListenerTemplate;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public abstract class SScene extends SThing {
	private final org.lgna.story.implementation.SceneImp implementation = new org.lgna.story.implementation.SceneImp( this );

	@Override
			/* package-private */org.lgna.story.implementation.SceneImp getImplementation() {
		return this.implementation;
	}

	protected abstract void handleActiveChanged( Boolean isActive, Integer activationCount );

	protected void preserveStateAndEventListeners() {
		this.implementation.preserveStateAndEventListeners();
	}

	protected void restoreStateAndEventListeners() {
		this.implementation.restoreStateAndEventListeners();
	}

	@GetterTemplate( isPersistent = true )
	@MethodTemplate( )
	public Color getAtmosphereColor() {
		return this.implementation.atmosphereColor.getValue();
	}

	@MethodTemplate( )
	public void setAtmosphereColor( Color color, SetAtmosphereColor.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( color, 0 );
		this.implementation.atmosphereColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@GetterTemplate( isPersistent = false )
	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public Color getAmbientLightColor() {
		return this.implementation.fromAboveLightColor.getValue();
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@Deprecated
	public void setAmbientLightColor( Color color, SetAmbientLightColor.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( color, 0 );
		this.implementation.fromAboveLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@GetterTemplate( isPersistent = true )
	@MethodTemplate( )
	public Color getFromAboveLightColor() {
		return this.implementation.fromAboveLightColor.getValue();
	}

	@MethodTemplate( )
	public void setFromAboveLightColor( Color color, SetFromAboveLightColor.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( color, 0 );
		this.implementation.fromAboveLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@GetterTemplate( isPersistent = true )
	@MethodTemplate( )
	public Color getFromBelowLightColor() {
		return this.implementation.fromBelowLightColor.getValue();
	}

	@MethodTemplate( )
	public void setFromBelowLightColor( Color color, SetFromBelowLightColor.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( color, 0 );
		this.implementation.fromBelowLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.PortionDetails.class )
	public Double getFogDensity() {
		return (double)this.getImplementation().fogDensity.getValue();
	}

	@MethodTemplate( )
	public void setFogDensity( Number density, SetFogDensity.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( density, 0 );
		this.getImplementation().fogDensity.animateValue( density.floatValue(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	//Mouse
	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addMouseClickOnScreenListener( org.lgna.story.event.MouseClickOnScreenListener listener, AddMouseClickOnScreenListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.implementation.getEventManager().addMouseClickOnScreenListener( listener, MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addMouseClickOnObjectListener( org.lgna.story.event.MouseClickOnObjectListener listener, AddMouseClickOnObjectListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.implementation.getEventManager().addMouseClickOnObjectListener( listener, MultipleEventPolicy.getValue( details ), SetOfVisuals.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addDefaultModelManipulation() {
		this.getImplementation().getEventManager().addDragAdapter();
	}

	//time/Scene
	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addTimeListener( org.lgna.story.event.TimeListener listener, Number frequency, AddTimeListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( frequency, 1 );
		this.getImplementation().getEventManager().addTimerEventListener( listener, frequency, MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener listener ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.implementation.addSceneActivationListener( listener );
	}

	//keyListeners
	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addKeyPressListener( org.lgna.story.event.KeyPressListener listener, AddKeyPressListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.implementation.getEventManager().addKeyListener( listener, MultipleEventPolicy.getValue( details ), HeldKeyPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addArrowKeyPressListener( org.lgna.story.event.ArrowKeyPressListener listener, AddKeyPressListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.getImplementation().getEventManager().addArrowKeyListener( listener, MultipleEventPolicy.getValue( details ), HeldKeyPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addNumberKeyPressListener( org.lgna.story.event.NumberKeyPressListener listener, AddKeyPressListener.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( listener, 0 );
		this.getImplementation().getEventManager().addNumberKeyListener( listener, MultipleEventPolicy.getValue( details ), HeldKeyPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addObjectMoverFor( SMovableTurnable thing ) {
		this.implementation.getEventManager().moveWithArrows( thing, 2.5 );
	}

	//TransformationListeners
	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addPointOfViewChangeListener( org.lgna.story.event.PointOfViewChangeListener listener, SThing[] set ) {
		this.getImplementation().getEventManager().addTransformationListener( listener, set );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addCollisionStartListener( org.lgna.story.event.CollisionStartListener listener, SThing[] setA, SThing[] setB, AddCollisionStartListener.Detail... details ) {
		this.getImplementation().getEventManager().addCollisionListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@AddEventListenerTemplate( )
	public void addWhileCollisionListener( org.lgna.story.event.WhileCollisionListener listener, SThing[] setA, SThing[] setB, AddTimeListener.Detail... details ) {
		this.getImplementation()
				.getEventManager()
				.addWhileCollisionListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), TimerFrequency.getValue( details ).getFrequency(),
						MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addCollisionEndListener( org.lgna.story.event.CollisionEndListener listener, SThing[] setA, SThing[] setB, AddCollisionEndListener.Detail... details ) {
		this.getImplementation().getEventManager().addCollisionListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addProximityEnterListener( org.lgna.story.event.ProximityEnterListener listener, SThing[] setA, SThing[] setB, Number distance, AddProximityEnterListener.Detail... details ) {
		this.getImplementation().getEventManager().addProximityEventListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), distance, MultipleEventPolicy.getValue( details ) );// AddEnterProximityEventListener.getDist( details ));
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@AddEventListenerTemplate( )
	public void addWhileProximityListener( org.lgna.story.event.WhileProximityListener listener, SThing[] setA, SThing[] setB, Number distance, AddTimeListener.Detail... details ) {
		this.getImplementation().getEventManager().addWhileProximityListener(
				listener,
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ),
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ),
				distance,
				TimerFrequency.getValue( details ).getFrequency(),
				MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addProximityExitListener( org.lgna.story.event.ProximityExitListener listener, SThing[] setA, SThing[] setB, Number distance, AddProximityExitListener.Detail... details ) {
		this.getImplementation().getEventManager().addProximityEventListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), distance, MultipleEventPolicy.getValue( details ) );// AddExitProximityEventListener.getDist( details ));
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addViewEnterListener( org.lgna.story.event.ViewEnterListener listener, SModel[] set, AddViewEnterListener.Detail... details ) {
		this.implementation.getEventManager().addComesIntoViewEventListener( listener, set, MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@AddEventListenerTemplate( )
	public void addWhileInViewListener( org.lgna.story.event.WhileInViewListener listener, SModel[] set, AddTimeListener.Detail... details ) {
		this.implementation.getEventManager().addWhileInViewListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( set ), TimerFrequency.getValue( details ).getFrequency(),
				MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addViewExitListener( org.lgna.story.event.ViewExitListener listener, SModel[] set, AddViewExitListener.Detail... details ) {
		this.implementation.getEventManager().addLeavesViewEventListener( listener, set, MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addOcclusionStartListener( org.lgna.story.event.OcclusionStartListener listener, SModel[] setA, SModel[] setB, AddOcclusionStartListener.Detail... details ) {
		this.getImplementation().getEventManager().addOcclusionEventListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), MultipleEventPolicy.getValue( details ) );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	@AddEventListenerTemplate( )
	public void addWhileOcclusionListener( org.lgna.story.event.WhileOcclusionListener listener, SModel[] setA, SModel[] setB, AddTimeListener.Detail... details ) {
		this.getImplementation().getEventManager().addWhileOcclusionListener(
				listener,
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ),
				edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ),
				TimerFrequency.getValue( details ).getFrequency(),
				MultipleEventPolicy.getValue( details, MultipleEventPolicy.IGNORE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	@AddEventListenerTemplate( )
	public void addOcclusionEndListener( org.lgna.story.event.OcclusionEndListener listener, SModel[] setA, SModel[] setB, AddOcclusionEndListener.Detail... details ) {
		this.getImplementation().getEventManager().addOcclusionEventListener( listener, edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setA ), edu.cmu.cs.dennisc.java.util.Lists.newArrayList( setB ), MultipleEventPolicy.getValue( details ) );
	}

	//remove
	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public void removeKeyListener( org.lgna.story.event.KeyPressListener listener ) {
		this.implementation.getEventManager().removeKeyListener( listener );
	}

	@MethodTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener listener ) {
		this.implementation.removeSceneActivationListener( listener );
	}
}

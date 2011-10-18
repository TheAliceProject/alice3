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
public abstract class Scene extends Entity {
	private final org.lgna.story.implementation.SceneImplementation implementation = new org.lgna.story.implementation.SceneImplementation( this );

	@Override
	/*package-private*/org.lgna.story.implementation.SceneImplementation getImplementation() {
		return this.implementation;
	}

	private void changeActiveStatus( Program program, boolean isActive, int activeCount ) {
		double prevSimulationSpeedFactor = program.getSimulationSpeedFactor();
		program.setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		this.handleActiveChanged( isActive, activeCount );
		if( isActive ) {
			this.implementation.addCamerasTo( program.getImplementation() );
		} else {
			this.implementation.removeCamerasFrom( program.getImplementation() );
		}
		program.setSimulationSpeedFactor( prevSimulationSpeedFactor );
	}

	private int activeCount;
	private int deactiveCount;

	/*package-private*/void activate( Program program ) {
		assert deactiveCount == activeCount;
		activeCount++;
		this.implementation.setProgram( program.getImplementation() );
		this.implementation.setGlobalBrightness( 0.0f );
		this.changeActiveStatus( program, true, activeCount );
		this.implementation.animateGlobalBrightness( 1.0f, 1.0, TraditionalStyle.BEGIN_AND_END_GENTLY );
	}
	/*package-private*/void deactivate( Program program ) {
		deactiveCount++;
		assert deactiveCount == activeCount;
		this.implementation.animateGlobalBrightness( 0.0f, 0.5, TraditionalStyle.BEGIN_AND_END_GENTLY );
		this.changeActiveStatus( program, false, activeCount );
		this.implementation.setProgram( null );
	}

	protected abstract void handleActiveChanged( Boolean isActive, Integer activeCount );

	protected void preserveVehiclesAndVantagePoints() {
		this.implementation.preserveVehiclesAndVantagePoints();
	}
	protected void restoreVehiclesAndVantagePoints() {
		this.implementation.restoreVehiclesAndVantagePoints();
	}

	@GetterTemplate(isPersistent = true)
	@MethodTemplate()
	public Color getAtmosphereColor() {
		return this.implementation.atmosphereColor.getValue();
	}
	@MethodTemplate()
	public void setAtmosphereColor( Color color, SetAtmosphereColor.Detail... details ) {
		this.implementation.atmosphereColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ) );
	}
	@GetterTemplate(isPersistent = true)
	@MethodTemplate()
	public Color getAmbientLightColor() {
		return this.implementation.ambientLightColor.getValue();
	}
	@MethodTemplate()
	public void setAmbientLightColor( Color color, SetAmbientLightColor.Detail... details ) {
		this.implementation.ambientLightColor.animateValue( color, Duration.getValue( details ), AnimationStyle.getValue( details ) );
	}
}

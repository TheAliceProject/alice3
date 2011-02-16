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

package org.lookingglassandalice.storytelling;

/**
 * @author Dennis Cosgrove
 */
public abstract class Scene extends Entity {
	private final org.lookingglassandalice.storytelling.implementation.SceneImplementation implementation = new org.lookingglassandalice.storytelling.implementation.SceneImplementation( this );

	@Override
	/*package-private*/ org.lookingglassandalice.storytelling.implementation.SceneImplementation getImplementation() {
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
	/*package-private*/ void activate( Program program ) {
		assert deactiveCount == activeCount;
		activeCount++;
		this.implementation.setProgram( program.getImplementation() );
		this.changeActiveStatus( program, true, activeCount );
	}
	/*package-private*/ void deactivate( Program program ) {
		deactiveCount++;
		assert deactiveCount == activeCount;
		this.changeActiveStatus( program, false, activeCount );
		this.implementation.setProgram( null );
	}
	
	protected abstract void handleActiveChanged( boolean isActive, int activeCount );
	
	protected void preserveVehiclesAndPointsOfView() {
		this.implementation.preserveVehiclesAndPointsOfView();
	}
	protected void restoreVehiclesAndPointsOfView() {
		this.implementation.restoreVehiclesAndPointsOfView();
	}
	
//	public void addEntity( Entity entity ) {
//		this.implementation.addEntity( entity.getImplementation() );
//	}
//	public void removeEntity( Entity entity ) {
//		this.implementation.removeEntity( entity.getImplementation() );
//	}
}

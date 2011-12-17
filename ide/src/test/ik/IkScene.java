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

package test.ik;

import org.lgna.story.*;

/**
 * @author Dennis Cosgrove
 */
public class IkScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground snow = new Ground();
	private final Camera camera;
	private final Biped ogre;
	private final Sphere target;
	public IkScene( Camera camera, Biped ogre, Sphere target ) {
		this.camera = camera;
		this.ogre = ogre;
		this.target = target;
	}
	
	private void performGeneratedSetup() {
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.ogre.setVehicle( this );
		this.target.setVehicle( this );
		
		this.ogre.place( SpatialRelation.ABOVE, this.snow );
		this.snow.setPaint( Ground.SurfaceAppearance.SNOW );

		this.target.setRadius( 0.2 );
		this.target.setPaint( Color.RED );
		this.target.setOpacity( 0.5 );
		
		//camera vantage point taken care of by camera navigator
		//this.camera.moveAndOrientToAGoodVantagePointOf( this.ogre );
	}
	private void performCustomSetup() {
		//if you want the skeleton visualization to be co-located
		//this.ogre.setOpacity( 0.25 );
		
		org.lgna.story.implementation.JointedModelImp impl = ImplementationAccessor.getImplementation( this.ogre );
		impl.showVisualization();
	}
	
	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreVehiclesAndVantagePoints();
			}
		} else {
			this.preserveVehiclesAndVantagePoints();
		}
	}
}

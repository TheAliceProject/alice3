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
package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;

/**
 * @author David Culyba
 */
public class PerspectiveCameraMarker extends CameraMarker 
{
	@Override
	protected void createVisuals()
	{
		super.createVisuals();
		
		Visual sgVisualCylinder = new Visual();
		sgVisualCylinder.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		double topRadius = .25;
		double bottomRadius = .25;
		double cylinderLength = .5;
		double radiusCone = .4;
		double lengthCone = .45;
		
		BottomToTopAxis bottomToTopAxis = BottomToTopAxis.NEGATIVE_Z;
		Cylinder sgCylinder = new Cylinder();
		sgCylinder.topRadius.setValue( topRadius );
		sgCylinder.bottomRadius.setValue( bottomRadius );
		sgCylinder.length.setValue( cylinderLength );
		sgCylinder.bottomToTopAxis.setValue( bottomToTopAxis );
		sgCylinder.hasTopCap.setValue( false );
		//todo?
		sgCylinder.hasBottomCap.setValue( true );
		
		
		edu.cmu.cs.dennisc.math.Vector3 coneTranslation = edu.cmu.cs.dennisc.math.Vector3.createMultiplication(  
			new edu.cmu.cs.dennisc.math.Vector3( -lengthCone, -lengthCone, -lengthCone ), 
			bottomToTopAxis.accessVector() 
		);
		
		edu.cmu.cs.dennisc.math.Vector3 cylinderTranslation = edu.cmu.cs.dennisc.math.Vector3.createMultiplication(  
				new edu.cmu.cs.dennisc.math.Vector3( -cylinderLength-lengthCone, -cylinderLength-lengthCone, -cylinderLength-lengthCone ), 
				bottomToTopAxis.accessVector() 
			);
		
		Transformable sgTransformableCone = new Transformable();
		sgTransformableCone.applyTranslation( coneTranslation );
		Transformable sgTransformableCylinder = new Transformable();
		sgTransformableCylinder.applyTranslation( cylinderTranslation );
		

		Visual sgVisualCone = new Visual();
		sgVisualCone.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );

		Cylinder sgCone = new Cylinder();
	    sgCone.topRadius.setValue( 0.0 );
	    sgCone.bottomRadius.setValue( radiusCone );
	    sgCone.length.setValue( lengthCone );
	    sgCone.bottomToTopAxis.setValue( bottomToTopAxis );
	    sgCone.hasTopCap.setValue( false ); //redundant
	    sgCone.hasBottomCap.setValue( true );
	    
	    sgVisualCylinder.geometries.setValue( new Geometry[] { sgCylinder } );
	    sgVisualCone.geometries.setValue( new Geometry[] { sgCone } );
	    
		sgVisualCone.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
	    
	    
	    sgTransformableCylinder.setParent( this.sgVisualTransformable );
	    sgTransformableCone.setParent( this.sgVisualTransformable );
	    
	    sgVisualCylinder.setParent( sgTransformableCylinder );
	    sgVisualCone.setParent( sgTransformableCone );
		
	}
}

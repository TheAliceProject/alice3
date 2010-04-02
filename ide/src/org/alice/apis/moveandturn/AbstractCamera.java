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

import edu.cmu.cs.dennisc.alice.annotations.ClassTemplate;
import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */

@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired=true)
public abstract class AbstractCamera extends Transformable {
	private edu.cmu.cs.dennisc.lookingglass.LookingGlass m_lookingGlass = null;

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public abstract edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera();

	private void setLookingGlass( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
		if( m_lookingGlass != lookingGlass ) {
			if( m_lookingGlass != null ) {
				m_lookingGlass.removeCamera( getSGCamera() );
			}
			m_lookingGlass = lookingGlass;
			if( lookingGlass != null ) {
				m_lookingGlass.addCamera( getSGCamera() );
			}
		}
	}
	@Override
	protected void handleOwnerChange( SceneOwner owner ) {
		super.handleOwnerChange( owner );
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass;
		if( owner != null ) {
			lookingGlass = owner.getOnscreenLookingGlass();
		} else {
			lookingGlass = null;
		}
		setLookingGlass( lookingGlass );
	}

	@Override
	protected void handleVehicleChange( Composite vehicle ) {
		super.handleVehicleChange( vehicle );
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass;
		if( vehicle != null ) {
			lookingGlass = vehicle.getLookingGlass();
		} else {
			lookingGlass = null;
		}
		setLookingGlass( lookingGlass );
	}

	/*package protected*/java.awt.Point transformToAWT( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
		return edu.cmu.cs.dennisc.lookingglass.util.TransformationUtilities.transformFromCameraToAWT_New( xyzw, lookingGlass, getSGCamera() );
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix() {
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lg = getLookingGlass();
		assert lg != null;
		return lg.getActualProjectionMatrix( getSGCamera() );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.scenegraph.Layer getPostRenderLayer() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.getSGCamera();
		if( sgCamera.postRenderLayers.getLength() == 0 ) {
			edu.cmu.cs.dennisc.scenegraph.Layer[] layers = {
				new edu.cmu.cs.dennisc.scenegraph.Layer()
			};
			sgCamera.postRenderLayers.setValue( layers );
		}
		return sgCamera.postRenderLayers.getValue()[ 0 ];
	}
}

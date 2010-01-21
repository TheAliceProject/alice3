/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
		setLookingGlass( owner.getOnscreenLookingGlass() );
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

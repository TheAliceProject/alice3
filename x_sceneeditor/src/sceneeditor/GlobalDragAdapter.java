/**
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

package sceneeditor;
import java.awt.Point;

import edu.cmu.cs.dennisc.ui.DragStyle;
import edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter extends OnscreenLookingGlassDragAdapter {

	private edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedObject = null;
	
	private sceneeditor.CameraTranslateManipulator cameraTranslator = new sceneeditor.CameraTranslateManipulator();
	
	
	private int cameraIndex = 0;
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		if( onscreenLookingGlass != null ) {
			if( this.cameraIndex < onscreenLookingGlass.getCameraCount() ) {
				return onscreenLookingGlass.getCameraAt( this.cameraIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraTransformable() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgCamera.getParent();
		} else {
			return null;
		}
	}
	
	@Override
	protected void handleMouseDrag(Point current, int deltaSince0,
			int deltaSince02, int xDeltaSincePrevious, int yDeltaSincePrevious,
			DragStyle dragStyle) {

		this.cameraTranslator.updateManipulator( current, xDeltaSincePrevious, yDeltaSincePrevious );

	}

	@Override
	protected void handleMousePress(Point current, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
		
		this.cameraTranslator.setManipulatedTransformable( this.getSGCameraTransformable() );
		this.cameraTranslator.startManipulator( current );
	}

	@Override
	protected Point handleMouseRelease(Point rvCurrent, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
		this.cameraTranslator.endManipulator( rvCurrent );
		return null;
	}

}

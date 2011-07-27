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

package org.alice.stageide.sceneeditor.viewmanager;

import org.lookingglassandalice.storytelling.Entity;
import org.lookingglassandalice.storytelling.OrthographicCameraMarker;
import org.lookingglassandalice.storytelling.implementation.CameraMarkerImplementation;
import org.lookingglassandalice.storytelling.implementation.OrthographicCameraMarkerImplementation;
import org.alice.ide.IDE;
import org.alice.ide.operations.ActionOperation;
import org.alice.ide.sceneeditor.AbstractSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

public class MoveMarkerToActiveCameraActionOperation extends ActionOperation {

	private static class SingletonHolder {
		private static MoveMarkerToActiveCameraActionOperation instance = new MoveMarkerToActiveCameraActionOperation();
	}
	
	public static MoveMarkerToActiveCameraActionOperation getInstance() {
		return SingletonHolder.instance;
	}
	
	private FieldDeclaredInAlice markerField;
	private CameraMarkerImplementation cameraMarker;
	private MoveToImageIcon imageIcon;

	private MoveMarkerToActiveCameraActionOperation() {
		super(edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a95908d8-0161-4a03-8a38-61eebea0c58c" ));
		this.markerField = null;
		this.cameraMarker = null;
		this.setToolTipText("Move the camera to this marker.");
		this.imageIcon = new MoveToImageIcon();
		this.setSmallIcon(imageIcon);
		this.updateBasedOnSettings();
	}

	private void updateBasedOnSettings()
	{
		if (this.markerField != null && this.cameraMarker != null)
		{
			this.setToolTipText("Move "+this.markerField.getName()+" to the current camera.");
			if (this.cameraMarker instanceof OrthographicCameraMarkerImplementation)
			{
				this.setEnabled(false);
			}
			else
			{
				this.setEnabled(true);
			}
		}
		else
		{
			this.setEnabled(false);
		}
		this.setSmallIcon(null);
		this.setSmallIcon(this.imageIcon);
	}
	
	public void setMarkerField(FieldDeclaredInAlice markerField)
	{
		this.markerField = markerField;
		if (this.markerField != null)
		{
			CameraMarkerImplementation marker = IDE.getActiveInstance().getSceneEditor().getImplementation(this.markerField);
			if (marker != null)
			{
//				this.imageIcon.setLeftImage(marker.getIcon());				
			}
		}
		this.updateBasedOnSettings();
	}
	
	public void setCameraMarker(CameraMarkerImplementation cameraMarker)
	{
		this.cameraMarker = cameraMarker;
//		if (this.cameraMarker != null)
//		{
//			this.imageIcon.setRightImage(this.cameraMarker.getIcon());
//		}
		this.updateBasedOnSettings();
	}

	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		final CameraMarkerImplementation cameraMarker;
		final org.lookingglassandalice.storytelling.Camera camera;
		final org.lookingglassandalice.storytelling.VantagePoint prevPOV;
		final org.lookingglassandalice.storytelling.VantagePoint nextPOV;
		
//		AbstractSceneEditor sceneEditor = IDE.getActiveInstance().getSceneEditor();
//		
//		cameraMarker = sceneEditor.getInstanceInJavaVMForField(this.markerField, org.lookingglassandalice.storytelling.MarkerWithIcon.class);
//		AbstractCamera sgCamera = sceneEditor.getSGPerspectiveCamera();
//		camera = (org.lookingglassandalice.storytelling.Camera)Element.getElement(sgCamera);
//		if( cameraMarker != null ) {
//			prevPOV = cameraMarker.getPointOfView( org.lookingglassandalice.storytelling.resources.sims2.AsSeenBy.SCENE );
//			nextPOV = camera.getPointOfView(org.lookingglassandalice.storytelling.resources.sims2.AsSeenBy.SCENE);
//			if( nextPOV.getInternal().isNaN() ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: MoveActiveCameraToMarkerActionOperation isNaN" );
//				step.cancel();
//			} else {
//				step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
//					@Override
//					protected final void doOrRedoInternal( boolean isDo ) {
//						setAbsolutePOV( cameraMarker, nextPOV );
//					}
//					@Override
//					protected final void undoInternal() {
//						setAbsolutePOV( cameraMarker, prevPOV );
//					}
//					@Override
//					protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
//						rv.append( MoveMarkerToActiveCameraActionOperation.this.getName() );
//						return rv;
//					}
//				} );
//			}
//		} else {
//			step.cancel();
//		}
		
	}
	
	private static void setAbsolutePOV( org.lookingglassandalice.storytelling.Turnable transformable, org.lookingglassandalice.storytelling.VantagePoint pov ) {
//		org.lookingglassandalice.storytelling.Scene scene = transformable.getScene();
//		assert scene != null;
//		transformable.moveAndOrientTo( scene.createOffsetStandIn( pov.getInternal() ) );
	}


}

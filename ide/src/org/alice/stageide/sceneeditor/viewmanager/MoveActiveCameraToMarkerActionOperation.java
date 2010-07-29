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

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.ImageIcon;

import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.Element;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.ide.IDE;
import org.alice.ide.operations.ActionOperation;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.croquet.ActionOperationContext;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

public class MoveActiveCameraToMarkerActionOperation extends ActionOperation {
	
//	private static class SingletonHolder {
//		private static HashMap<FieldDeclaredInAlice, MoveActiveCameraToMarkerActionOperation> fieldToOperationMap = new HashMap<FieldDeclaredInAlice, MoveActiveCameraToMarkerActionOperation>();
//	}
//	
//	public static MoveActiveCameraToMarkerActionOperation getInstanceForField(FieldDeclaredInAlice markerField) 
//	{
//		if ( SingletonHolder.fieldToOperationMap.containsKey(markerField) )
//		{
//			return SingletonHolder.fieldToOperationMap.get(markerField);
//		}
//		else
//		{
//			MoveActiveCameraToMarkerActionOperation operation = new MoveActiveCameraToMarkerActionOperation(markerField);
//			SingletonHolder.fieldToOperationMap.put(markerField, operation);
//			return operation;
//		}
//	}
	
	private static class SingletonHolder {
		private static MoveActiveCameraToMarkerActionOperation instance = new MoveActiveCameraToMarkerActionOperation();
	}
	
	public static MoveActiveCameraToMarkerActionOperation getInstance() {
		return SingletonHolder.instance;
	}
	
	private FieldDeclaredInAlice markerField;
	private CameraMarker cameraMarker;
	private MoveToImageIcon imageIcon;

	private MoveActiveCameraToMarkerActionOperation() {
		super(edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "8d1cbb1e-3f58-4f48-99ed-350f2decb203" ));
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
			this.setToolTipText("Move the current camera to the point of view of "+this.markerField.getName()+".");
			if (this.cameraMarker instanceof OrthographicCameraMarker)
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
			CameraMarker marker = ((MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor())).getCameraMarkerForField(this.markerField);
			if (marker != null)
			{
				this.imageIcon.setRightImage(marker.getIcon());
			}
		}
		this.updateBasedOnSettings();
	}
	
	public void setCameraMarker(CameraMarker cameraMarker)
	{
		this.cameraMarker = cameraMarker;
		if (this.cameraMarker != null)
		{
			this.imageIcon.setLeftImage(this.cameraMarker.getIcon());
		}
		this.updateBasedOnSettings();
	}
	
	@Override
	protected void perform(ActionOperationContext context) 
	{
		
		final CameraMarker cameraMarker;
		final org.alice.apis.moveandturn.AbstractCamera camera;
		final org.alice.apis.moveandturn.PointOfView prevPOV;
		final org.alice.apis.moveandturn.PointOfView nextPOV;
		
		MoveAndTurnSceneEditor sceneEditor = (MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor());
		
		cameraMarker = sceneEditor.getInstanceInJavaForField(this.markerField, org.alice.apis.moveandturn.CameraMarker.class);
		AbstractCamera sgCamera = sceneEditor.getSGPerspectiveCamera();
		camera = (org.alice.apis.moveandturn.AbstractCamera)Element.getElement(sgCamera);
		if( cameraMarker != null ) {
			nextPOV = cameraMarker.getPointOfView( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			prevPOV = camera.getPointOfView(org.alice.apis.moveandturn.AsSeenBy.SCENE);
			if( nextPOV.getInternal().isNaN() ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: MoveActiveCameraToMarkerActionOperation isNaN" );
				context.cancel();
			} else {
				context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						setAbsolutePOV( camera, nextPOV );
					}
					@Override
					public void undo() {
						setAbsolutePOV( camera, prevPOV );
					}
					@Override
					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
						rv.append( MoveActiveCameraToMarkerActionOperation.this.getName() );
						return rv;
					}
				} );
			}
		} else {
			context.cancel();
		}
		
	}
	
	private static void setAbsolutePOV( org.alice.apis.moveandturn.AbstractTransformable transformable, org.alice.apis.moveandturn.PointOfView pov ) {
		org.alice.apis.moveandturn.Scene scene = transformable.getScene();
		assert scene != null;
		transformable.moveAndOrientTo( scene.createOffsetStandIn( pov.getInternal() ) );
	}

}

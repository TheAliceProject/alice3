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

import org.alice.stageide.operations.ast.MoveAndOrientToEdit;
import org.alice.stageide.operations.ast.oneshot.LocalTransformationEdit;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.UserField;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.OrthographicCameraMarkerImp;
import org.lgna.story.implementation.TransformableImp;

public class MoveActiveCameraToMarkerActionOperation extends org.lgna.croquet.ActionOperation {
	
	private static class SingletonHolder {
		private static MoveActiveCameraToMarkerActionOperation instance = new MoveActiveCameraToMarkerActionOperation();
	}
	
	public static MoveActiveCameraToMarkerActionOperation getInstance() {
		return SingletonHolder.instance;
	}
	
	private UserField markerField;
	private CameraMarkerImp cameraMarker;
	
	private TransformableImp toMoveImp;
	private TransformableImp toMoveToImp;
	
	private MoveToImageIcon imageIcon;

	private MoveActiveCameraToMarkerActionOperation() {
		super(org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "8d1cbb1e-3f58-4f48-99ed-350f2decb203" ));
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
			if (this.cameraMarker instanceof OrthographicCameraMarkerImp)
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
	
	public void setMarkerField(UserField markerField)
	{
		this.markerField = markerField;
		if (this.markerField != null)
		{
			this.imageIcon.setRightImage(MarkerUtilities.getIconForCameraMarker(this.markerField));	
		}
		else {
			this.imageIcon.setRightImage(null);
		}
		this.updateBasedOnSettings();
	}
	
	public void setCameraMarker(CameraMarkerImp cameraMarker)
	{
		this.cameraMarker = cameraMarker;
		if (this.cameraMarker != null)
		{
			this.imageIcon.setLeftImage(MarkerUtilities.getIconForCameraImp(cameraMarker));
		}
		else {
			this.imageIcon.setLeftImage(null);
		}
		this.updateBasedOnSettings();
	}
	
	@Override
	protected void perform(org.lgna.croquet.history.ActionOperationStep step) 
	{
		if (this.toMoveImp != null && this.toMoveToImp != null && 
			this.toMoveImp.getAbstraction() instanceof org.lgna.story.MovableTurnable &&
			this.toMoveToImp.getAbstraction() != null) {

			MoveAndOrientToEdit edit = new MoveAndOrientToEdit(step, (org.lgna.story.MovableTurnable)this.toMoveImp.getAbstraction(), this.toMoveToImp.getAbstraction());
			step.commitAndInvokeDo(edit);
		} else {
			step.cancel();
		}

		
	}

}

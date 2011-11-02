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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.alice.ide.IDE;
import org.alice.stageide.sceneeditor.MarkerUtilities;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.UserField;
import org.lgna.story.implementation.CameraMarkerImp;

import edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon;

public class MoveMarkerToSelectedObjectActionOperation extends org.lgna.croquet.ActionOperation {

	private static class SingletonHolder {
		private static MoveMarkerToSelectedObjectActionOperation instance = new MoveMarkerToSelectedObjectActionOperation();
	}
	
	public static MoveMarkerToSelectedObjectActionOperation getInstance() {
		return SingletonHolder.instance;
	}
	
	private UserField markerField;
	private UserField selectedField;
	private MoveToImageIcon imageIcon;

	private MoveMarkerToSelectedObjectActionOperation() {
		super(org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "b09e12cc-22e0-440a-ac37-35e0d9878d86" ));
		this.markerField = null;
		this.selectedField = null;
		this.setToolTipText("Move the camera to this marker.");
		this.imageIcon = new MoveToImageIcon();
		this.setSmallIcon(imageIcon);
		this.updateBasedOnSettings();
	}

	private void updateBasedOnSettings()
	{
		if (this.markerField != null && this.selectedField != null)
		{
			this.setToolTipText("Move "+this.markerField.getName()+" to "+this.selectedField.getName()+".");
			this.setEnabled(true);
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
			this.imageIcon.setLeftImage(MarkerUtilities.getIconForMarker(markerField));				
		}
		this.updateBasedOnSettings();
	}
	
	public void setSelectedField(AbstractField field)
	{
		if (field instanceof UserField)
		{
			this.selectedField = (UserField)field;
		}
		else
		{
			this.selectedField = null;
		}
		if (this.selectedField != null)
		{
			org.lgna.project.ast.AbstractType<?,?,?> valueType = field.getValueType();
			
			Icon icon = org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( valueType );
			if (icon != null)
			{
				if (icon instanceof ImageIcon)
				{
					icon = new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( ((javax.swing.ImageIcon)icon).getImage(), MoveToImageIcon.SUB_ICON_WIDTH,  MoveToImageIcon.SUB_ICON_HEIGHT );
				}
				else if (icon instanceof ScaledImageIcon)
				{
					icon = new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( ((ScaledImageIcon)icon).getImage(), MoveToImageIcon.SUB_ICON_WIDTH,  MoveToImageIcon.SUB_ICON_HEIGHT );
				}
			}
			this.imageIcon.setRightImage(icon);
		}
		else
		{
			this.imageIcon.setRightImage(null);
		}
		this.updateBasedOnSettings();
	}

	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		final CameraMarkerImp objectMarker;
		final org.lgna.story.VantagePoint prevPOV;
		final org.lgna.story.VantagePoint nextPOV;
		
//		MoveAndTurnSceneEditor sceneEditor = (MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor());
//		
//		FieldDeclaredInAlice selectedField = (FieldDeclaredInAlice)org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
//		final Turnable selectedTransformable = sceneEditor.getTransformableForField(selectedField);
//		objectMarker = sceneEditor.getInstanceInJavaVMForField(this.markerField, org.lookingglassandalice.storytelling.ObjectMarker.class);
//		if( objectMarker != null ) {
//			prevPOV = objectMarker.getPointOfView( org.lookingglassandalice.storytelling.resources.sims2.AsSeenBy.SCENE );
//			nextPOV = selectedTransformable.getPointOfView(org.lookingglassandalice.storytelling.resources.sims2.AsSeenBy.SCENE);
//			if( nextPOV.getInternal().isNaN() ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: MoveMarkerToSelectedObjectActionOperation isNaN" );
//				step.cancel();
//			} else {
//				step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
//					@Override
//					public void doOrRedoInternal( boolean isDo ) {
//						setAbsolutePOV( objectMarker, nextPOV );
//					}
//					@Override
//					public void undoInternal() {
//						setAbsolutePOV( objectMarker, prevPOV );
//					}
//					@Override
//					protected StringBuilder updatePresentation(StringBuilder rv, java.util.Locale locale) {
//						rv.append( MoveMarkerToSelectedObjectActionOperation.this.getName() );
//						return rv;
//					}
//				} );
//			}
//		} else {
//			step.cancel();
//		}
		
	}
	
	private static void setAbsolutePOV( org.lgna.story.Turnable transformable, org.lgna.story.VantagePoint pov ) {
//		org.lookingglassandalice.storytelling.Scene scene = transformable.getScene();
//		assert scene != null;
//		transformable.moveAndOrientTo( scene.createOffsetStandIn( pov.getInternal() ) );
	}


}

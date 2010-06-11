package org.alice.stageide.sceneeditor.viewmanager;

import java.util.LinkedList;
import java.util.List;


import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.interact.QuaternionAndTranslation;
import org.alice.interact.QuaternionAndTranslationTargetBasedAnimation;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class LookingGlassViewSelector extends CameraViewSelector implements PropertyListener
{
	private SymmetricPerspectiveCamera perspectiveCamera;
	private OrthographicCamera orthographicCamera;
	private Animator animator;
	private QuaternionAndTranslationTargetBasedAnimation cameraAnimation = null;
	private PointOfViewAnimation pointOfViewAnimation = null;
	private CameraMarker markerToUpdate = null;
	
	public LookingGlassViewSelector(MoveAndTurnSceneEditor sceneEditor, Animator animator, List<CameraFieldAndMarker> extraOptions)
	{
		super(sceneEditor, extraOptions);	
		this.animator = animator;
	}
	
	public LookingGlassViewSelector(MoveAndTurnSceneEditor sceneEditor, Animator animator)
	{
		this(sceneEditor, animator, null);
	}
	
	public void setPerspectiveCamera(SymmetricPerspectiveCamera camera)
	{
		this.perspectiveCamera = camera;
//		if (this.perspectiveCamera != null)
//		{
//			if (doesMarkerMatchCamera(this.activeMarker, this.perspectiveCamera) && this.markerToUpdate == null)
//			{
//				startTrackingCamera(this.perspectiveCamera, this.activeMarker);
//			}
//		}
	}
	
	public void setOrthographicCamera(OrthographicCamera camera)
	{
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.picturePlane.removePropertyListener(this);
		}
		this.orthographicCamera = camera;
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.picturePlane.addPropertyListener(this);
			if (doesMarkerMatchCamera(this.activeMarker, this.orthographicCamera) && this.markerToUpdate == null)
			{
				startTrackingCamera(this.orthographicCamera, this.activeMarker);
			}
		}
	}
	
	
	private synchronized boolean isAnimatingCamera()
	{
		return (this.cameraAnimation != null && !this.cameraAnimation.isDone());
	}
	
	private void printMatrix(AffineMatrix4x4 matrix)
	{
		System.out.println("[["+matrix.orientation.right.x+", "+matrix.orientation.right.y+", "+matrix.orientation.right.z+"]");
		System.out.println("["+matrix.orientation.up.x+", "+matrix.orientation.up.y+", "+matrix.orientation.up.z+"]");
		System.out.println("["+matrix.orientation.backward.x+", "+matrix.orientation.backward.y+", "+matrix.orientation.backward.z+"]]");
		System.out.println("<"+matrix.translation.x+", "+matrix.translation.y+", "+matrix.translation.z+">");
	}
	
	private void printMatrixDistance(AffineMatrix4x4 oldMatrix, AffineMatrix4x4 newMatrix)
	{
		double rightDistance =  Point3.calculateDistanceBetween(new Point3(oldMatrix.orientation.right), new Point3(newMatrix.orientation.right));
		double upDistance =  Point3.calculateDistanceBetween(new Point3(oldMatrix.orientation.up), new Point3(newMatrix.orientation.up));
		double backwardDistance =  Point3.calculateDistanceBetween(new Point3(oldMatrix.orientation.backward), new Point3(newMatrix.orientation.backward));
		double translationDistance = Point3.calculateDistanceBetween(oldMatrix.translation, newMatrix.translation);
		
		double totalDistance = rightDistance + upDistance + backwardDistance + translationDistance;
		
		System.out.println("Distance: ["+rightDistance+", "+upDistance+", "+backwardDistance+"], "+translationDistance+" = "+totalDistance);
		
	}
	
	private boolean doEpilogue = true;
	
	private boolean transformsAreWithinReasonableEpsilonOfEachOther( AffineMatrix4x4 a, AffineMatrix4x4 b)
	{
		if (a.orientation.isWithinReasonableEpsilonOf(b.orientation))
		{
			return a.translation.isWithinReasonableEpsilonOf(b.translation);
		}
		return false;
	}
	
	private void animateToTargetView()
	{
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = this.perspectiveCamera.getAbsoluteTransformation();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransform = this.activeMarker.getTransformation( AsSeenBy.SCENE );
		
		if (this.pointOfViewAnimation != null)
		{
			this.doEpilogue = false;
			this.pointOfViewAnimation.complete(null);
			this.doEpilogue = true;
		}
		if (transformsAreWithinReasonableEpsilonOfEachOther( currentTransform, targetTransform))
		{
			startTrackingCamera(LookingGlassViewSelector.this.perspectiveCamera, LookingGlassViewSelector.this.activeMarker);
		}
		else
		{
			Transformable cameraParent = (Transformable)this.perspectiveCamera.getParent();
			this.pointOfViewAnimation = new PointOfViewAnimation(cameraParent, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE, currentTransform, targetTransform)
										{
											@Override
											protected void epilogue() 
											{
												if (LookingGlassViewSelector.this.doEpilogue)
												{
													startTrackingCamera(LookingGlassViewSelector.this.perspectiveCamera, LookingGlassViewSelector.this.activeMarker);
												}
											}
										};
			this.animator.invokeLater(this.pointOfViewAnimation, null);
		}
	}
	
	private void switchToOrthographicView()
	{
		this.sceneEditor.switchToOthographicCamera();
	}
	
	private void switchToPerspectiveView()
	{
		this.sceneEditor.switchToPerspectiveCamera();
	}
	
	private CameraFieldAndMarker getAddedField(List<CameraFieldAndMarker> previousFields, List<CameraFieldAndMarker> currentFields)
	{
		for (CameraFieldAndMarker cfm : currentFields)
		{
			if (!previousFields.contains(cfm))
			{
				return cfm;
			}
		}
		return null;
	}
	
	private CameraFieldAndMarker getRemovedField(List<CameraFieldAndMarker> previousFields, List<CameraFieldAndMarker> currentFields)
	{
		for (CameraFieldAndMarker cfm : previousFields)
		{
			if (!currentFields.contains(cfm))
			{
				return cfm;
			}
		}
		return null;
	}
	
	@Override
	public void refreshFields() 
	{
		List<CameraFieldAndMarker> previousFields = new LinkedList<CameraFieldAndMarker>(this.fieldBasedOptions);
		super.refreshFields();
		
		CameraFieldAndMarker addedField = getAddedField(previousFields, this.fieldBasedOptions);
		if (addedField != null)
		{
			int addedIndex = this.getIndexForCameraFieldAndMarker(addedField);
			this.setSelectedIndex(addedIndex);
		}
		
	}
	
	@Override
	public void doSetSelectedView(int index)
	{
		CameraMarker previousMarker = this.activeMarker;
		super.doSetSelectedView(index);
		
//		if (previousMarker != null && this.activeMarker != null)
//		{
//			System.out.println("setting selected view from "+previousMarker.getName()+" to "+this.activeMarker.getName());
//		}
		if (this.perspectiveCamera != null && this.activeMarker != null)
		{
			if (previousMarker != this.activeMarker)
			{
				stopTrackingCamera();
				if (this.activeMarker instanceof OrthographicCameraMarker)
				{
					OrthographicCameraMarker orthoMarker = (OrthographicCameraMarker)this.activeMarker;
					switchToOrthographicView();
					Transformable cameraParent = (Transformable)LookingGlassViewSelector.this.orthographicCamera.getParent();
					LookingGlassViewSelector.this.orthographicCamera.picturePlane.setValue(new ClippedZPlane(orthoMarker.getPicturePlane()) );
					cameraParent.setTransformation( LookingGlassViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE ), LookingGlassViewSelector.this.orthographicCamera.getRoot() );
					startTrackingCamera(this.orthographicCamera, orthoMarker);
				}
				else
				{
					switchToPerspectiveView();
					animateToTargetView();
				}
			}
		}
	}

	//Sets the given camera to the absolute orientation of the given marker
	//Parents the given marker to the camera and then zeros out the local transform
	private void startTrackingCamera(AbstractCamera camera, CameraMarker markerToUpdate)
	{
		if (this.markerToUpdate != null)
		{
			stopTrackingCamera();
		}
		
		this.markerToUpdate = markerToUpdate;
		if (this.markerToUpdate != null)
		{
			Transformable cameraParent = (Transformable)camera.getParent();
			cameraParent.setTransformation( this.markerToUpdate.getTransformation( AsSeenBy.SCENE ), camera.getRoot() );
			this.markerToUpdate.setShowing(false);
			this.markerToUpdate.setLocalTransformation(AffineMatrix4x4.accessIdentity());
			this.markerToUpdate.getSGTransformable().setParent(cameraParent);
			this.sceneEditor.setHandleVisibilityForObject(this.markerToUpdate.getSGTransformable(), false);
		}
	}
	
	private void stopTrackingCamera()
	{
		if (this.markerToUpdate != null)
		{
			AffineMatrix4x4 previousMarkerTransform = this.markerToUpdate.getTransformation(AsSeenBy.SCENE);
			this.markerToUpdate.getSGTransformable().setParent(this.markerToUpdate.getScene().getSGComposite());
			this.markerToUpdate.getSGTransformable().setTransformation(previousMarkerTransform, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE);
			this.markerToUpdate.setShowing(true);
			this.sceneEditor.setHandleVisibilityForObject(this.markerToUpdate.getSGTransformable(), true);
		}
		this.markerToUpdate = null;
	}
	
	private boolean doesMarkerMatchCamera(org.alice.apis.moveandturn.CameraMarker marker, AbstractCamera camera)
	{
		if (camera instanceof OrthographicCamera)
		{
			return marker instanceof OrthographicCameraMarker;
		}
		else if (camera instanceof SymmetricPerspectiveCamera)
		{
			return marker instanceof PerspectiveCameraMarker;
		}
		return false;
	}
	

	public void propertyChanged(PropertyEvent e) {
		if (e.getOwner() == this.orthographicCamera && e.getTypedSource() == this.orthographicCamera.picturePlane)
		{		
			if (doesMarkerMatchCamera( this.activeMarker, this.orthographicCamera ))
			{
				((OrthographicCameraMarker)this.activeMarker).setPicturePlane(this.orthographicCamera.picturePlane.getValue());
			}
		}		
	}

	public void propertyChanging(PropertyEvent e) 
	{
		//Do Nothing
	}

}

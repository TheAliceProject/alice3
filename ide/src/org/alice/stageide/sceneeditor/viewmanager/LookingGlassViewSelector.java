package org.alice.stageide.sceneeditor.viewmanager;

import java.util.List;


import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.interact.QuaternionAndTranslation;
import org.alice.interact.QuaternionAndTranslationTargetBasedAnimation;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.animation.Animator;
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

public class LookingGlassViewSelector extends CameraViewSelector implements AbsoluteTransformationListener, PropertyListener
{
	private SymmetricPerspectiveCamera perspectiveCamera;
	private OrthographicCamera orthographicCamera;
	private Animator animator;
	private QuaternionAndTranslationTargetBasedAnimation cameraAnimation = null;
	
	public LookingGlassViewSelector(MoveAndTurnSceneEditor sceneEditor, Animator animator, List<CameraFieldAndMarker> extraOptions)
	{
		super(sceneEditor, extraOptions);	
		this.animator = animator;
	}
	
	public LookingGlassViewSelector(MoveAndTurnSceneEditor sceneEditor, Animator animator)
	{
		this(sceneEditor, animator, null);
	}
	
	public void setPersespectiveCamera(SymmetricPerspectiveCamera camera)
	{
		if (this.perspectiveCamera != null)
		{
			this.perspectiveCamera.removeAbsoluteTransformationListener( this );
		}
		this.perspectiveCamera = camera;
		if (this.perspectiveCamera != null)
		{
			this.perspectiveCamera.addAbsoluteTransformationListener( this );
		}
	}
	
	public void setOrthographicCamera(OrthographicCamera camera)
	{
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.removeAbsoluteTransformationListener( this );
			this.orthographicCamera.picturePlane.removePropertyListener(this);
		}
		this.orthographicCamera = camera;
		if (this.orthographicCamera != null)
		{
			this.orthographicCamera.addAbsoluteTransformationListener( this );
			this.orthographicCamera.picturePlane.addPropertyListener(this);
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
	
	private void animateToTargetView()
	{
		if (this.cameraAnimation == null)
		{
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = LookingGlassViewSelector.this.perspectiveCamera.getAbsoluteTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransform = LookingGlassViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE );
			this.cameraAnimation = new QuaternionAndTranslationTargetBasedAnimation(new QuaternionAndTranslation(currentTransform), new QuaternionAndTranslation(targetTransform))
			{

				@Override
				protected void updateValue( QuaternionAndTranslation value ) {
					Transformable cameraParent = (Transformable)LookingGlassViewSelector.this.perspectiveCamera.getParent();
					cameraParent.setTransformation( value.getAffineMatrix(), LookingGlassViewSelector.this.perspectiveCamera.getRoot() );
				}

			};
			this.animator.addFrameObserver( this.cameraAnimation );
		}
		
		this.cameraAnimation.setCurrentValue( new QuaternionAndTranslation(LookingGlassViewSelector.this.perspectiveCamera.getAbsoluteTransformation()) );
		this.cameraAnimation.setTarget( new QuaternionAndTranslation(LookingGlassViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE ) ));
	}
	
	private void switchToOrthographicView()
	{
		this.sceneEditor.switchToOthographicCamera();
	}
	
	private void switchToPerspectiveView()
	{
		this.sceneEditor.switchToPerspectiveCamera();
	}
	
	@Override
	public void doSetSelectedView(int index)
	{
		super.doSetSelectedView(index);
		if (this.perspectiveCamera != null && this.activeMarker != null)
		{
			if (this.activeMarker instanceof OrthographicCameraMarker)
			{
				OrthographicCameraMarker orthoMarker = (OrthographicCameraMarker)this.activeMarker;
				switchToOrthographicView();
				Transformable cameraParent = (Transformable)LookingGlassViewSelector.this.orthographicCamera.getParent();
				LookingGlassViewSelector.this.orthographicCamera.picturePlane.setValue(new ClippedZPlane(orthoMarker.getPicturePlane()) );
				cameraParent.setTransformation( LookingGlassViewSelector.this.activeMarker.getTransformation( AsSeenBy.SCENE ), LookingGlassViewSelector.this.orthographicCamera.getRoot() );
			}
			else
			{
				switchToPerspectiveView();
				animateToTargetView();
			}
			
		}
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
	
	public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) 
	{
		AbstractCamera cameraSource = (AbstractCamera)absoluteTransformationEvent.getSource();
		if (doesMarkerMatchCamera( this.activeMarker, cameraSource ))
		{
			if (cameraSource instanceof OrthographicCamera)
			{
				AffineMatrix4x4 cameraTransform = this.orthographicCamera.getAbsoluteTransformation();
				Component root = this.orthographicCamera.getRoot();
				this.activeMarker.getSGTransformable().setTransformation( cameraTransform, root );
			}
			else if (cameraSource instanceof SymmetricPerspectiveCamera)
			{
				if (!isAnimatingCamera())
				{
					this.activeMarker.getSGTransformable().setTransformation( this.perspectiveCamera.getAbsoluteTransformation(), this.perspectiveCamera.getRoot() );
				}
				
			}
		}
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

	public void propertyChanging(PropertyEvent e) {
		// TODO Auto-generated method stub
		
	}

}

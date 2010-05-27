package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.AbstractDragAdapter.CameraView;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;


public class CameraMoveDragManipulator  extends CameraManipulator 
{
	
	private double PIXEL_DISTANCE_FACTOR = 200.0d;
	
	static final Plane GROUND_PLANE = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	
	private Point originalMousePoint;
	private AffineMatrix4x4 originalLocalTransformation;
	private Vector3 moveXVector;
	private Vector3 moveYVector;
	private double worldUnitsPerPixelX = .01d;
	private double worldUnitsPerPixelY = .01d;
	private double initialDistanceToGround;
	private double initialCameraDotVertical;
	private double pickDistance;
	
	public CameraMoveDragManipulator()
	{
		super();
	}
	
	
	
	@Override
	public String getUndoRedoDescription() {
		return "Camera Move";
	}
	
	@Override
	public CameraView getDesiredCameraView() 
	{
		return CameraView.PICK_CAMERA;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
		int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;
		xChange *= -1; //invert X
		
		Vector3 translationX = Vector3.createMultiplication(moveXVector, xChange*calculateWorldUnitsPerPixelX());
		Vector3 translationY = Vector3.createMultiplication(moveYVector, yChange*calculateWorldUnitsPerPixelY());
		
		this.manipulatedTransformable.setLocalTransformation(this.originalLocalTransformation);
		this.manipulatedTransformable.applyTranslation(translationX, AsSeenBy.SCENE);
		this.manipulatedTransformable.applyTranslation(translationY, AsSeenBy.SCENE);
		
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		
	}
	
	@Override
	public void doClickManipulator(InputState clickInput, InputState previousInput) {
		//Do nothing
	}


	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (super.doStartManipulator( startInput ))
		{
			this.originalLocalTransformation = new AffineMatrix4x4(manipulatedTransformable.localTransformation.getValue());
			this.originalMousePoint = new Point(startInput.getMouseLocation());
			AffineMatrix4x4 absoluteTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			initialCameraDotVertical = Vector3.calculateDotProduct(absoluteTransform.orientation.backward, Vector3.accessPositiveYAxis());
			initialCameraDotVertical = Math.abs(initialCameraDotVertical);
			if (this.camera instanceof OrthographicCamera)
			{
				moveXVector = new Vector3(absoluteTransform.orientation.right);
				moveYVector = new Vector3(absoluteTransform.orientation.up);
			}
			else
			{
				
				if (initialCameraDotVertical > .99999)
				{
					moveYVector = new Vector3(absoluteTransform.orientation.up);
				}
				else
				{
					moveYVector = new Vector3(absoluteTransform.orientation.backward);
					moveYVector.multiply(-1);
				}
				moveXVector = new Vector3(absoluteTransform.orientation.right);
				moveYVector.y = 0;
				moveYVector.normalize();
				moveXVector.y = 0;
				moveXVector.normalize();
			}
			
			initialDistanceToGround = Math.abs(absoluteTransform.translation.y);
			pickDistance = -1;
			Vector3 cameraForward = new Vector3(absoluteTransform.orientation.backward);
			cameraForward.multiply( -1.0d );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( GROUND_PLANE, new edu.cmu.cs.dennisc.math.Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward));
			if ( pickPoint != null)
			{
				pickDistance = Point3.calculateDistanceBetween(pickPoint, absoluteTransform.translation);
			}
			worldUnitsPerPixelX = calculateWorldUnitsPerPixelX();
			worldUnitsPerPixelY = calculateWorldUnitsPerPixelY();
			return true;
		}
		return false;
		
	}

	private double calculateWorldUnitsPerPixelX()
	{
		worldUnitsPerPixelX = initialDistanceToGround / 200.0d;
		return worldUnitsPerPixelX;
	}
	
	private double calculateWorldUnitsPerPixelY()
	{
		double parallelToGroundFactor = 150d;
		double perpToGroundFactor = 200d;
		double yFactor = parallelToGroundFactor + (perpToGroundFactor - parallelToGroundFactor) * initialCameraDotVertical;
		worldUnitsPerPixelY = initialDistanceToGround / yFactor;
		return worldUnitsPerPixelX;
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}
	
}

package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.debug.DebugSphere;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public class CameraPanDragManipulator extends CameraManipulator {

	protected static final double MOVEMENT_PER_PIXEL = .02d;
	
	protected Point originalMousePoint;
	protected Vector3 xDirection;
	protected Vector3 yDirection;
	
	public CameraPanDragManipulator()
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
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) 
	{
		double yDif = -(currentInput.getMouseLocation().y - previousInput.getMouseLocation().y);
		double xDif = currentInput.getMouseLocation().x - previousInput.getMouseLocation().x;
		
		Vector3 xMovement = Vector3.createMultiplication(this.xDirection, xDif*MOVEMENT_PER_PIXEL);
		Vector3 yMovement = Vector3.createMultiplication(this.yDirection, yDif*MOVEMENT_PER_PIXEL);
		
		this.manipulatedTransformable.applyTranslation(xMovement, AsSeenBy.SCENE);
		this.manipulatedTransformable.applyTranslation(yMovement, AsSeenBy.SCENE);
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
		if (super.doStartManipulator( startInput ) && this.camera instanceof SymmetricPerspectiveCamera)
		{
			boolean success = false;
			AffineMatrix4x4 cameraTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			this.yDirection = new Vector3(Vector3.accessPositiveYAxis());
			this.xDirection = new Vector3(cameraTransform.orientation.right);
			
			double xDoty = Vector3.calculateDotProduct(this.yDirection, this.xDirection);
			if (Math.abs(xDoty) > EpsilonUtilities.REASONABLE_EPSILON)
			{
				System.out.println("Boom!");
				return false;
			}
			return true;
		}
		return false;
		
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}
	

}

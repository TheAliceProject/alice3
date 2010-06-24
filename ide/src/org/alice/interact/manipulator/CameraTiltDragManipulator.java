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

public class CameraTiltDragManipulator extends CameraManipulator implements OnScreenLookingGlassInformedManipulator{

	
	static final Plane GROUND_PLANE = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );

	private Plane cameraFacingPickPlane;
	private Point3 pickPoint = null;
	
	private static final boolean SHOW_PICK_POINT = false;
	private DebugSphere pickPointDebugSphere = new DebugSphere();
	
	
	
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	public CameraTiltDragManipulator()
	{
		super();
	}
	
	public OnscreenLookingGlass getOnscreenLookingGlass()
	{
		return this.onscreenLookingGlass;
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass lookingGlass )
	{
		this.onscreenLookingGlass = lookingGlass;
	}
	
	private void addPickPointSphereToScene()
	{
		if (SHOW_PICK_POINT)
		{
			if (this.camera != null && this.pickPointDebugSphere.getParent() == null)
			{
				this.camera.getRoot().addComponent(this.pickPointDebugSphere);
			}
		}
	}
	
	private void removePickPointSphereFromScene()
	{
		if (SHOW_PICK_POINT)
		{
			if (this.camera != null && this.pickPointDebugSphere.getParent() == this.camera.getRoot())
			{
				this.camera.getRoot().removeComponent(this.pickPointDebugSphere);
			}
		}
	}
	
	private void setPickPoint(Tuple3 position)
	{
		if (SHOW_PICK_POINT)
		{
			this.pickPointDebugSphere.setLocalTranslation(position);
		}
	}
	
	public void setPlaneDiscPoint( Point3 planeDiscPoint )
	{
		this.pickPoint = planeDiscPoint;
		setPickPoint(this.pickPoint);
		
	}
	
	
	@Override
	public String getUndoRedoDescription() {
		return "Camera Rotate";
	}
	
	@Override
	public CameraView getDesiredCameraView() 
	{
		return CameraView.PICK_CAMERA;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) 
	{
		Ray oldPickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), previousInput.getMouseLocation().x, previousInput.getMouseLocation().y );
		Ray newPickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 oldPickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, oldPickRay);
		Point3 newPickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, newPickRay);
		if ( newPickPoint != null && oldPickPoint != null )
		{
			this.setPlaneDiscPoint( pickPoint );
			
			Point3 oldPointInCamera = this.camera.transformFrom_New(oldPickPoint, this.camera.getRoot());
			Point3 newPointInCamera = this.camera.transformFrom_New(newPickPoint, this.camera.getRoot());
			
			Vector3 xDif = new Vector3(newPointInCamera.x, oldPointInCamera.y, oldPointInCamera.z);
			xDif.normalize();
			Vector3 yDif = new Vector3(oldPointInCamera.x, newPointInCamera.y, oldPointInCamera.z);
			yDif.normalize();
			
			Vector3 oldDirection = new Vector3(oldPointInCamera);
			oldDirection.normalize();
			
			Angle xAngle = VectorUtilities.getAngleBetweenVectors(oldDirection, xDif);
			if (currentInput.getMouseLocation().x < previousInput.getMouseLocation().x)
			{
				xAngle.setAsRadians(xAngle.getAsRadians() * -1);
			}
			Angle yAngle = VectorUtilities.getAngleBetweenVectors(oldDirection, yDif);
			if (currentInput.getMouseLocation().y < previousInput.getMouseLocation().y)
			{
				yAngle.setAsRadians(yAngle.getAsRadians() * -1);
			}

			StandIn standIn = new StandIn();
			standIn.setName("CameraOrbitStandIn");
			standIn.vehicle.setValue( this.getCamera().getRoot() );
			standIn.setTransformation(this.manipulatedTransformable.getAbsoluteTransformation(), AsSeenBy.SCENE );
			standIn.setAxesOnlyToStandUp();
			this.manipulatedTransformable.applyRotationAboutXAxis( yAngle, standIn );
			this.manipulatedTransformable.applyRotationAboutYAxis( xAngle, standIn );
			
			standIn.vehicle.setValue(null);
			
			//Make sure the camera's x-axis is still horizontal
			AffineMatrix4x4 cameraTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			Vector3 rightAxis = cameraTransform.orientation.right;
			rightAxis.y = 0;
			rightAxis.normalize();
			Vector3 upAxis = Vector3.createCrossProduct( cameraTransform.orientation.backward, rightAxis );
			upAxis.normalize();
			cameraTransform.orientation.right.set( rightAxis );
			cameraTransform.orientation.up.set( upAxis );
			this.manipulatedTransformable.setTransformation(cameraTransform, AsSeenBy.SCENE);
			
			
			this.cameraFacingPickPlane = new Plane(newPickPoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward);
		}
		

	}
	
	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		removePickPointSphereFromScene();
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
			Vector3 cameraForward = this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward;
			cameraForward.multiply( -10.0d );

			addPickPointSphereToScene();
			
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			
			Point3 planePoint = Point3.createAddition(this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward);
			this.cameraFacingPickPlane = new Plane(planePoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward);
			
			Point3 pickPoint = PlaneUtilities.getPointInPlane( this.cameraFacingPickPlane, pickRay);
			if ( pickPoint != null)
			{
				this.setPlaneDiscPoint( pickPoint );
				success = true;
			}
			return success;
		}
		return false;
		
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}
	

}

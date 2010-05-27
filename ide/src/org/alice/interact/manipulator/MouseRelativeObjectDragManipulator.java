package org.alice.interact.manipulator;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import javax.swing.SwingUtilities;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class MouseRelativeObjectDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnScreenLookingGlassInformedManipulator {

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
	protected Plane orthographicPickPlane = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	protected Point3 orthographicOffsetToOrigin = null;
	protected Point3 originalPosition = null;
	protected Boolean hasMoved = false;
	protected Point3 offsetFromOrigin = null;
	
	protected boolean hidCursor = false;
	
	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	public MouseRelativeObjectDragManipulator()
	{
		super();
	}
	
	public AbstractCamera getCamera()
	{
		return this.camera;
	}
	
	public void setCamera( AbstractCamera camera ) 
	{
		this.camera = camera;
	}
	
	public void setDesiredCameraView( CameraView cameraView )
	{
		//this can only be PICK_CAMERA
	}
	
	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}
	
	public OnscreenLookingGlass getOnscreenLookingGlass()
	{
		return this.onscreenLookingGlass;
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass lookingGlass )
	{
		this.onscreenLookingGlass = lookingGlass;
	}
	
	@Override
	public String getUndoRedoDescription() {
		return "Object Move";
	}
	
	@Override
	protected HandleSet getHandleSetToEnable() {
		return HandleSet.GROUND_TRANSLATION_VISUALIZATION;
	}
	
	private Vector3 getMouseMovementFromVector(InputState currentInput, InputState previousInput)
	{
		int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
		int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;
		yChange *= -1; //invert X
		
		Vector3 translationX = Vector3.createMultiplication(moveXVector, xChange*calculateWorldUnitsPerPixelX());
		Vector3 translationY = Vector3.createMultiplication(moveYVector, yChange*calculateWorldUnitsPerPixelY());
		
		return Vector3.createAddition(translationX, translationY);
	}
	
	private Vector3 getOthographicMovementVector(InputState currentInput, InputState previousInput)
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		Point3 pickPoint = PlaneUtilities.getPointInPlane( this.orthographicPickPlane, pickRay );
		Point3 newPosition = Point3.createAddition(pickPoint, this.orthographicOffsetToOrigin);
		
		return Vector3.createSubtraction(newPosition, this.originalPosition);
	}
	
	private Vector3 getMovementVectorBasedOnCamera(InputState currentInput, InputState previousInput)
	{
		if (this.getCamera() instanceof OrthographicCamera)
		{
			return getOthographicMovementVector(currentInput, previousInput);
		}
		else
		{
			return getMouseMovementFromVector(currentInput, previousInput);
		}
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && this.manipulatedTransformable != null)
		{
			if (!this.hasMoved)
			{
				this.hasMoved = true;
			}
				
			Vector3 movementVector = getMovementVectorBasedOnCamera( currentInput, previousInput );
			Point3 newPosition = Point3.createAddition( this.originalPosition, movementVector );
			
			newPosition = SnapUtilities.doMovementSnapping(this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera());
			
			//Send manipulation events
			Vector3 movementDif = Vector3.createSubtraction( newPosition, this.manipulatedTransformable.getAbsoluteTransformation().translation);
			movementDif.normalize();
			for (ManipulationEvent event : this.manipulationEvents)
			{
				double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), movementDif );
				if (dot > 0.1d)
				{
					this.dragAdapter.triggerManipulationEvent( event, true );
				}
				else if ( dot < -.07d)
				{
					this.dragAdapter.triggerManipulationEvent( event, false );
				}
			}
			if (newPosition != null)
			{
				this.manipulatedTransformable.setTranslationOnly( newPosition, AsSeenBy.SCENE );
			}
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		if (this.hidCursor)
		{
			this.showCursor();
		}
	}
	
	@Override
	public void doClickManipulator(InputState clickInput, InputState previousInput) {
		//Do nothing
	}

	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
	}
	
	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.manipulatedTransformable = startInput.getClickPickTransformable();	
		hidCursor = false;
		if (this.manipulatedTransformable != null)
		{
			this.initializeEventMessages();
			
			this.hasMoved = false;
			
			this.originalLocalTransformation = new AffineMatrix4x4(manipulatedTransformable.localTransformation.getValue());
			this.originalMousePoint = new Point(startInput.getMouseLocation());
			this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
			this.orthographicPickPlane = new Plane( this.originalPosition, this.getCamera().getAxes( AsSeenBy.SCENE ).backward );
			
			Ray orthoPickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			Point3 orthoPickPoint = PlaneUtilities.getPointInPlane( orthographicPickPlane, orthoPickRay );
			this.orthographicOffsetToOrigin = Point3.createSubtraction(this.originalPosition, orthoPickPoint);
			
			Point3 initialClickPoint = new Point3();
			startInput.getClickPickResult().getPositionInSource(initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			if (pickRay != null)
			{
				this.offsetFromOrigin = Point3.createSubtraction( initialClickPoint, this.originalPosition );
			}
			
			
			AffineMatrix4x4 cameraTransform = this.camera.getParent().getAbsoluteTransformation();
			initialCameraDotVertical = Vector3.calculateDotProduct(cameraTransform.orientation.backward, Vector3.accessPositiveYAxis());
			initialCameraDotVertical = Math.abs(initialCameraDotVertical);
			if (this.camera instanceof OrthographicCamera)
			{
				moveXVector = new Vector3(cameraTransform.orientation.right);
				moveYVector = new Vector3(cameraTransform.orientation.up);
			}
			else
			{
				
				if (initialCameraDotVertical > .99999)
				{
					moveYVector = new Vector3(cameraTransform.orientation.up);
				}
				else
				{
					moveYVector = new Vector3(cameraTransform.orientation.backward);
					moveYVector.multiply(-1);
				}
				moveXVector = new Vector3(cameraTransform.orientation.right);
				moveYVector.y = 0;
				moveYVector.normalize();
				moveXVector.y = 0;
				moveXVector.normalize();
			}
			
			initialDistanceToGround = Math.abs(cameraTransform.translation.y);
			pickDistance = -1;
			Vector3 cameraForward = new Vector3(cameraTransform.orientation.backward);
			cameraForward.multiply( -1.0d );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( GROUND_PLANE, new edu.cmu.cs.dennisc.math.Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward));
			if ( pickPoint != null)
			{
				pickDistance = Point3.calculateDistanceBetween(pickPoint, cameraTransform.translation);
			}
			worldUnitsPerPixelX = calculateWorldUnitsPerPixelX();
			worldUnitsPerPixelY = calculateWorldUnitsPerPixelY();
			
			this.hideCursor();
			return true;
		}
		return false;
		
	}

	private double calculateWorldUnitsPerPixelX()
	{
		worldUnitsPerPixelX = initialDistanceToGround / 150.0d;
		return worldUnitsPerPixelX;
	}
	
	private double calculateWorldUnitsPerPixelY()
	{
		double parallelToGroundFactor = 30d;
		double perpToGroundFactor = 200d;
		double yFactor = parallelToGroundFactor + (perpToGroundFactor - parallelToGroundFactor) * initialCameraDotVertical;
		worldUnitsPerPixelY = initialDistanceToGround / yFactor;
		return worldUnitsPerPixelX;
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}

	protected void hideCursor()
	{
		CursorUtilities.pushAndSet( this.getOnscreenLookingGlass().getAWTComponent(), CursorUtilities.NULL_CURSOR );
		this.hidCursor = true;
	}
	
	protected void showCursor()
	{
		try {
			Point3 new3DPoint = Point3.createAddition(this.manipulatedTransformable.getAbsoluteTransformation().translation, this.offsetFromOrigin);
			
			Point3 pointInCamera = this.camera.transformFrom_New(new3DPoint, this.camera.getRoot());
			Point awtPoint = edu.cmu.cs.dennisc.lookingglass.util.TransformationUtilities.transformFromCameraToAWT_New( pointInCamera, this.getOnscreenLookingGlass(), this.getCamera() );
			SwingUtilities.convertPointToScreen( awtPoint, this.getOnscreenLookingGlass().getAWTComponent() );
			Robot mouseMover = new Robot();
			mouseMover.mouseMove(awtPoint.x, awtPoint.y);
		} catch( AWTException e ) {
		}
		finally
		{
			CursorUtilities.popAndSet( this.getOnscreenLookingGlass().getAWTComponent() );
		}
	}
}

package org.alice.interact.manipulator;

import java.awt.Color;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.OrthographicCameraController;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.ImageBasedManipulationHandle2D;

import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

public class OrthographicCameraDragZoomManipulator extends Camera2DDragManipulator
{

	protected static final Color IN = Color.RED;
	protected static final Color OUT = Color.GREEN;
	
	protected static final double INITIAL_ZOOM_FACTOR = .1d;
	protected static final double ZOOMS_PER_SECOND = .1d;
	
	protected double initialZoomValue = 0.0d;
	
	protected OrthographicCameraController cameraController;
	
	public OrthographicCameraDragZoomManipulator( ImageBasedManipulationHandle2D handle)
	{
		super(handle);
	}
	
	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Zoom, new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Zoom, new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL), this.manipulatedTransformable ) );
	}
	
	@Override
	public void setCamera(AbstractCamera camera) 
	{
		super.setCamera(camera);
		assert camera instanceof OrthographicCamera;
		cameraController = new OrthographicCameraController((OrthographicCamera)camera);
		initializeEventMessages();
	}
	
	protected double getZoomValueForColor(Color color) {
		double initialZoom = 0.0d;
		if (color != null)
		{
			if (color.equals( IN ))
			{
				initialZoom = INITIAL_ZOOM_FACTOR;
			}
			else if (color.equals( OUT ))
			{
				initialZoom = -INITIAL_ZOOM_FACTOR;
			}
		}
		return initialZoom;
	}
	
	
	protected double getRelativeZoomAmount(Vector2 mousePos, double time)
	{
		Vector2 relativeMousePos = Vector2.createSubtraction( mousePos, this.initialMousePosition );
		double amountToZoom = relativeMousePos.y * ZOOMS_PER_SECOND * time;
		return amountToZoom;
	}
	
	protected double getTotalZoomAmount(Vector2 mousePos, double time)
	{
		double relativeZoomAmount = this.getRelativeZoomAmount( mousePos, time );
		double amountToZoomInitial = this.initialZoomValue * ZOOMS_PER_SECOND * time;
		double amountToZoom = relativeZoomAmount + amountToZoomInitial;
		return amountToZoom;
	}
	
	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (super.doStartManipulator( startInput ))
		{
			this.initialZoomValue = this.getZoomValueForColor( this.initialHandleColor );
			return true;
		}
		return false;
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		if (time < MIN_TIME)
			time = MIN_TIME;
		else if (time > MAX_TIME)
			time = MAX_TIME;
		
		Vector2 mousePos = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y);
		double zoomAmount = this.getTotalZoomAmount( mousePos, time );
		
		cameraController.zoom(zoomAmount);
		
		for (ManipulationEvent event : this.manipulationEvents)
		{
			if (event.getMovementDescription().direction == MovementDirection.FORWARD)
			{
				this.dragAdapter.triggerManipulationEvent( event, zoomAmount < 0.0d );
			}
			else if (event.getMovementDescription().direction == MovementDirection.BACKWARD)
			{
				this.dragAdapter.triggerManipulationEvent( event, zoomAmount >= 0.0d );
			}
		}
	}
	
	
	@Override
	protected Vector3 getRelativeRotationAmount(Vector2 mousePos, double time)
	{
		return new Vector3(0.0d, 0.0d, 0.0d);
	}
	
	@Override
	protected ReferenceFrame getRotationReferenceFrame()
	{
		return this.getManipulatedTransformable();
	}
	
	@Override
	protected ReferenceFrame getMovementReferenceFrame()
	{
		return this.getManipulatedTransformable();
	}

	@Override
	protected Vector3 getMovementVectorForColor(Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Vector3 getRelativeMovementAmount(Vector2 mousePos, double time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Vector3 getRotationVectorForColor(Color color) {
		// TODO Auto-generated method stub
		return null;
	}

}

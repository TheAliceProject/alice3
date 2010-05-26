package org.alice.interact.manipulator;

import org.alice.apis.moveandturn.Element;
import org.alice.apis.moveandturn.Model;
import org.alice.apis.moveandturn.SymmetricPerspectiveCamera;
import org.alice.interact.InputState;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class GetAGoodLookAtManipulator extends AbstractManipulator implements CameraInformedManipulator{

	protected AbstractCamera camera;
	public AbstractCamera getCamera() {
		return camera;
	}

	public CameraView getDesiredCameraView() {
		return CameraView.PICK_CAMERA;
	}

	public void setCamera(AbstractCamera camera) 
	{
		this.camera = camera;
		if (this.camera != null && this.camera.getParent() instanceof Transformable)
		{
			this.manipulatedTransformable = (Transformable)this.camera.getParent();
		}
	}

	public void setDesiredCameraView(CameraView cameraView) 
	{
		//this can only be PICK_CAMERA
	}
	
	@Override
	public void doClickManipulator(InputState endInput, InputState previousInput) 
	{
		Transformable toLookAt = endInput.getClickPickTransformable();
		if (toLookAt != null && this.camera != null)
		{
			Element cameraElement = Element.getElement(this.camera);
			Element objectElement = Element.getElement(toLookAt);
			if (cameraElement instanceof SymmetricPerspectiveCamera)
			{
				SymmetricPerspectiveCamera moveAndTurnCamera = (SymmetricPerspectiveCamera)cameraElement;
				if (objectElement instanceof Model)
				{
					this.hasDoneUpdate = true;
					Model moveAndTurnModel = (Model)objectElement;
					moveAndTurnCamera.getGoodLookAt(moveAndTurnModel);
				}
			}
		}
	}

	@Override
	public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEndManipulator(InputState endInput, InputState previousInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doStartManipulator(InputState startInput) {
		if (this.manipulatedTransformable != null)
		{
			return true;
		}
		return false;
	}

	@Override
	public void doTimeUpdateManipulator(double dTime, InputState currentInput) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUndoRedoDescription() 
	{
		return "Look At Object";
	}

}

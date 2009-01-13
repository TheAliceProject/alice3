import org.alice.apis.moveandturn.DirectionalLight;
import org.alice.apis.moveandturn.Program;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.SymmetricPerspectiveCamera;
import org.alice.apis.moveandturn.TurnDirection;
import org.alice.apis.moveandturn.gallery.animals.Chicken;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;

import edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter;

public class SceneEditor extends Program {

	Scene scene = new Scene();
	GrassyGround grassyGround = new GrassyGround();
	DirectionalLight sunLight = new DirectionalLight();
	SymmetricPerspectiveCamera camera = new SymmetricPerspectiveCamera();
	Chicken chicken = new Chicken();

	CameraNavigationDragAdapter cameraNavigationDragAdapter = new CameraNavigationDragAdapter();

	@Override
	protected void initialize() {
		scene.addComponent(grassyGround);
		scene.addComponent(chicken);
		scene.addComponent(sunLight);
		scene.addComponent(camera);
		grassyGround.setVehicle(scene);
		sunLight.setVehicle(scene);
		camera.setVehicle(scene);
		chicken.setVehicle(scene);
		this.setScene(this.scene);
		sunLight.turn(TurnDirection.FORWARD, 0.25);
		cameraNavigationDragAdapter.setOnscreenLookingGlass(this
				.getOnscreenLookingGlass());
	}

	@Override
	protected void run() {
	}

	public static void main(String[] args) {
		SceneEditor sceneEditor = new SceneEditor();
		sceneEditor.showInJFrame(args, true);
	}
}

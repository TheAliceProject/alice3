
import java.awt.Point;
import java.util.Set;
import java.util.TreeSet;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.AxisRotation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.wustl.cse.galankus.math.VectorMathUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults.Coach;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic.AgentAjunt;
import edu.wustl.cse.lookingglass.inputdevice.wii.WiiRemote;
import edu.wustl.cse.lookingglass.inputdevice.wii.listener.ButtonListener;
import edu.wustl.cse.lookingglass.inputdevice.wii.listener.OrientationListener;

import org.alice.apis.moveandturn.*;

public class MyAgentAjunt extends AgentAjunt implements OrientationListener, ButtonListener {


	private TreeSet<WiiButton> pressed = new TreeSet<WiiButton>();
	private Thread movement;
	private Thread action;
	public boolean standing = true;
	final public MyBasketball basketball;
	private AxisRotation direction;
	public boolean isAnimating;
	public PointOfView origin;

	public MyAgentAjunt(MyBasketball basketball) {
		this.basketball = basketball;
		this.standing = true;
	}

	public AxisAlignedBox getAxisAlignedBox(){
		return this.getAxisAlignedMinimumBoundingBox(AsSeenBy.SCENE);
	}
	
	public void setOrigin(PointOfView pov){
		this.origin = pov;
	}
	
	public void reset(){
		this.setLocalPointOfView(new PointOfView(new Quaternion(-1.7987036556935211E-19, 0.9483553656895922, 5.0876937649104405E-19, 0.317209868017626), new Position(0.6599924686175442, -0.004437316378993653, 0.12842369889240057)));
	}
	
	public void throwingMotion( ) {
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.touch( MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.05 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				}
		);

		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.touch( MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 1.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.getPart(MyAgentAjunt.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyAgentAjunt.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.05, 0.25 );
					}
				}
		);

		this.delay(0.5);
		this.standUp( false );
	}


	public void orientationReceived(OrthogonalMatrix3x3 orientation,
			WiiRemote senderRemote) {
		//startMovementThread();
		//		startActionThread();
		//		System.out.println("recieved orientation");
		Vector3 newY = orientation.transform(Vector3.createPositiveYAxis());
		double characterAngle = -(VectorMathUtilities.findAngleInRadiansBetween(Vector3.accessPositiveYAxis(),
				newY, Vector3.accessPositiveZAxis()));
		if (characterAngle<Math.PI/4)
			direction = new AxisRotation(Vector3.accessPositiveYAxis(), (new AngleInRadians(characterAngle/100)));
		else
			direction = new AxisRotation(Vector3.accessPositiveYAxis(), (new AngleInRadians(characterAngle/50)));
		setOrientationRightNow(new OrthogonalMatrix3x3(direction), AsSeenBy.SELF);
	}

	public void throwBall(){
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
						MyAgentAjunt.this.throwingMotion();
						//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
						basketball.delay(1.25);
						StandIn standIn = createOffsetStandIn(0,0,0);
						basketball.setVehicle(getScene());
						basketball.setAxesOnlyAsSeenByParent(standIn.getOrientation(AsSeenBy.SCENE));
						basketball.go();
//						System.out.println("setting new location...");
						edu.cmu.cs.dennisc.math.Point3 pos = new edu.cmu.cs.dennisc.math.Point3(MyAgentAjunt.this.getPart(AgentAjunt.Part.RightHand).getPosition(getScene()));
						double x = pos.x;
						double y = pos.y;
						double z = pos.z;
						basketball.setPositionRightNow(x, y, z, getScene());
						//				}
					}
				}
		);

	}

	public void buttonPressed(WiiButton button, WiiRemote senderRemote) {
		System.out.println(button + " pressed!");
		pressed.add(button);
		if(button == WiiButton.UP && this.standing)
			move(MoveDirection.FORWARD,2);
		if(button == WiiButton.DOWN && this.standing)
			move(MoveDirection.BACKWARD,2);
		if(button == WiiButton.LEFT && this.standing)
			move(MoveDirection.LEFT,1);
		if(button == WiiButton.RIGHT && this.standing)
			move(MoveDirection.RIGHT,1);
		if(button == WiiButton.A && this.standing){
			move(MoveDirection.UP, 1);
			move(MoveDirection.DOWN, 1);
		}
		if(button == WiiButton.B && this.standing){
			MyAgentAjunt.this.throwBall();
		}
//		if(button == WiiButton.ONE)	
//			walk(3,50);
	}

	public void buttonReleased(WiiButton button, WiiRemote senderRemote) {
		pressed.remove(button);
		System.out.println(button + " released!");
	}
}
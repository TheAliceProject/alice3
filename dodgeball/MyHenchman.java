
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic.Henchman;
import org.alice.apis.moveandturn.*;

public class MyHenchman extends Henchman {
	public MyBasketball basketball;
	public Person agent;
	public HenchmansBall ammo;
	private Thread thread;
	public boolean shouldBeStanding;
	public boolean isAnimating;
	public boolean hasFallen;
	public boolean isTurning;
	public PointOfView origin;

	public MyHenchman(MyBasketball basketball, Person agent, HenchmansBall ammo, Thread thread) {
		this.basketball = basketball;
		this.agent = agent;
		this.ammo = ammo;
		this.thread = thread;
		this.shouldBeStanding = true;
		this.isAnimating = false;
		this.hasFallen = false;
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
						MyHenchman.this.touch( MyHenchman.this.getPart(MyHenchman.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.05 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				}
		);

		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MyHenchman.this.touch( MyHenchman.this.getPart(MyHenchman.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 1.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.getPart(MyHenchman.Part.RightlowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						MyHenchman.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.05, 0.25 );
					}
				}
		);

		this.delay(0.5);
		this.standUp( false );
	}

	public void throwBall(){
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
						MyHenchman.this.throwingMotion();
						//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
						ammo.delay(1.2);
						StandIn standIn = createOffsetStandIn(0,0,0);
						ammo.setVehicle(getScene());
						ammo.setAxesOnlyAsSeenByParent(standIn.getOrientation(AsSeenBy.SCENE));
						if(MyHenchman.this.shouldBeStanding)
							ammo.hGo();
						//						System.out.println("setting new location...");
//						edu.cmu.cs.dennisc.math.Point3 pos = new edu.cmu.cs.dennisc.math.Point3(MyHenchman.this.getPart(MyHenchman.Part.RightHand).getPosition(getScene()));
//						double x = pos.x;
//						double y = pos.y;
//						double z = pos.z;
						ammo.addToHand(MyHenchman.this);
					}
				}
		);

	}

	//	public void attackMode(){
	//		System.out.println("enemy: attack mode initiated");
	//		isAttackMode = true;
	//		Thread attackModeThread = new Thread(){
	//			public void run(){
	//				MyHenchman.this.turnToFace(agent, 3);
	//				if(MyHenchman.this.stillStanding)
	//					MyHenchman.this.throwBall();
	//			}
	//		};
	//		attackModeThread.start();
	//	}


	//	public void shouldIFallDown(){
	////		System.out.println("throw the ball!");
	//		//		while(!basketball.flying){
	//		//			try {
	//		///				thread.sleep(100);
	//		//			} catch (InterruptedException e) {
	//		//			}
	//		//		}
	//		//		System.out.println("im ready");
	//		//		while(basketball.flying){
	//		if (!basketball.isAtLeastThresholdAwayFrom(this.getWidth()*2, this)){
	//			//				synchronized(this){
	//			System.out.println("garrr! I'm dead");
	//			basketball.flying = false;
	//			stillStanding = false;
	//			//				System.out.println("i've fallen");
	//			//			}
	//		}
	//		//		}
	//		//		if(stillStanding)
	//		//			this.shouldIFallDown();
	//		//	}
	//	}
}
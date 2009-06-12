
import java.util.Iterator;
import java.util.Set;

import org.alice.apis.moveandturn.*;

import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic.AgentAjunt;
import edu.wustl.cse.lookingglass.inputdevice.wii.WiiRemote;
import edu.wustl.cse.lookingglass.inputdevice.wii.listener.ButtonListener;


public class MyScene extends Scene implements ButtonListener {
	public Thread henchmanOneThread;
	public Thread henchmanTwoThread;
	private MyCamera camera = new MyCamera();
	private MyGrassyGround grassyGround = new MyGrassyGround();
	private MySunLight sunLight = new MySunLight();
	private MyBasketball basketballOne = new MyBasketball();
	private MyAgentAjunt playerOne = new MyAgentAjunt(basketballOne);
	private HenchmansBall henchmanTwoBall = new HenchmansBall(playerOne);
	private HenchmansBall henchmanOneBall = new HenchmansBall(playerOne);
	private MyHenchman henchmanOne = new MyHenchman(basketballOne, playerOne, henchmanOneBall, henchmanOneThread);
	private MyHenchman henchmanTwo = new MyHenchman(basketballOne, playerOne, henchmanTwoBall, henchmanTwoThread);
	private Thread henchmanAttackThread;
	private Thread henchmanTwoAttackThread;
	private Thread checkIfStanding;
	private Set<edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson> personList;

	public MyScene() {
		this.performSetUp();
	}

	protected void performCustomPropertySetUp() {
	}

	public MyCamera getCamera(){
		return camera;
	}


	public void setWiiRemotes(WiiRemote[] wiiRemotes) {
		assert wiiRemotes.length == 1;
		assert wiiRemotes[0] != null;

		//wiiRemotes[0].setCamera(camera);

		wiiRemotes[0].addOrientationListener(playerOne);
		wiiRemotes[0].addButtonListener(playerOne);
		wiiRemotes[0].addButtonListener(this);
	}



	public void buttonPressed(WiiButton button, WiiRemote senderRemote) {
//		if(button == WiiButton.TWO){
//			this.systemReset();
//		}
	}

	public void systemReset(){
		

		//		while(true){
		//			if(!playerOne.isAnimating){
		//				playerOne.reset();
		//				camera.setVehicle(playerOne);
		//			}
		//			else break;
		//		}
		//		while(true){
		//			if(!henchmanOne.isAnimating){
		//				henchmanOne.reset();
		//				henchmanOneBall.addToHand(henchmanOne);
		//		}
		//			else break;
		//		}
		//		while(true){
		//			if(!henchmanTwo.isAnimating){
		//				henchmanTwo.reset();
		//				henchmanTwoBall.addToHand(henchmanTwo);
		//			}
		//			else break;
		//	}
		//	while(true){
		//		if(!basketballOne.flying){
		//			basketballOne.reset(playerOne);
		//		}
		//	}

	}

	public void buttonReleased(WiiButton button, WiiRemote senderRemote) {
		// TODO Auto-generated method stub

	}

	public void run() {
		//		playerOne.getAxisAlignedBox().set
		camera.setVehicle(playerOne);
		basketballOne.setVehicle(playerOne.getPart(AgentAjunt.Part.RightHand));
		henchmanTwoBall.addToHand(henchmanTwo);
		henchmanOneBall.addToHand(henchmanOne);
		henchmanOneThread = new Thread(){
			public void run() {
				int b = 0;
				while (b != 1){
					if ((!basketballOne.isAtLeastThresholdAwayFrom(henchmanOne.getWidth()*2, henchmanOne)&& basketballOne.flying) || !henchmanOne.shouldBeStanding){
						//						System.out.println("garrr! I'm dead");
						basketballOne.flying = false;
						henchmanOne.shouldBeStanding = false;
						//						if(!henchmanOne.isThrowing){
						//							henchmanOne.fallDown();
						//							b++;
						//						}
						//						if(!henchmanOne.isTurning){
						//							henchmanOne.fallDown();
						//							b++;
						//						}
					}
				}
			}
		};
		henchmanTwoThread = new Thread(){
			public void run() {
				int c = 0;
				while (c != 1){
					if ((!basketballOne.isAtLeastThresholdAwayFrom(henchmanTwo.getWidth()*2, henchmanTwo) && basketballOne.flying) || !henchmanTwo.shouldBeStanding){
						//					System.out.println("garrr! I'm dead");
						basketballOne.flying = false;
						henchmanTwo.shouldBeStanding = false;
						c++;
					}
				}
			}
		};
		henchmanAttackThread = new Thread(){
			public void run(){
				while(!henchmanOne.hasFallen){
					//henchmanOne.isTurning = true;
					henchmanOne.turnToFace(playerOne, 3);
					//henchmanOne.isTurning = false;
					if(!henchmanOne.shouldBeStanding){
						henchmanOne.isAnimating = true;
						henchmanOne.fallDown();
						henchmanOne.isAnimating = false;
						break;
					}
					else{
						henchmanOne.isAnimating = true;
						henchmanOne.throwBall();
						henchmanOne.isAnimating = false;
					}
					if(!henchmanOne.shouldBeStanding){
						henchmanOne.isAnimating = true;
						henchmanOne.fallDown();
						henchmanOne.isAnimating = false;
						break;
					}
				}
			}
		};
		henchmanTwoAttackThread = new Thread(){
			public void run(){
				while(henchmanTwo.shouldBeStanding){
					//henchmanTwo.isTurning = true;
					henchmanTwo.turnToFace(playerOne, 3);
					//henchmanTwo.isTurning = false;
					if(!henchmanTwo.shouldBeStanding){
						henchmanTwo.isAnimating = true;
						henchmanTwo.fallDown();
						henchmanTwo.isAnimating = false;
						break;
					}
					else {
						henchmanTwo.isAnimating = true;
						henchmanTwo.throwBall();
						henchmanTwo.isAnimating = false;
					}

					if(!henchmanTwo.shouldBeStanding){
						henchmanTwo.isAnimating = true;
						henchmanTwo.fallDown();
						henchmanTwo.isAnimating = false;
						break;
					}
				}
			}
		};
		checkIfStanding = new Thread(){
			public void run() {
				int a = 0;
				while(a == 0){
					if(!playerOne.standing){
						System.out.println("agent: I've been hit!");
						camera.setVehicle(getScene());
						playerOne.fallDown();
						a = 1;
					}
				}
				System.out.println("done checking");
			}
		};
		checkIfStanding.start();
		henchmanTwoAttackThread.start();
		henchmanAttackThread.start();
		henchmanOneThread.start();
		henchmanTwoThread.start();

	}

	protected void performSceneEditorGeneratedSetUp() {
		this.setName("scene");
		this.setAtmosphereColor(new Color(0.5, 0.5, 1.0));
		this.camera.setName("camera");
		this.camera.setLocalPointOfView(new PointOfView(new Quaternion(-0.025400301313926028, 0.9432296329092124, 0.074233782037511, 0.32274142885523993), new Position(4.655598941098651, 2.157575720321664, -4.687392810959366)));
		this.addComponent(this.camera);
		this.grassyGround.setName("grassyGround");
		this.grassyGround.setLocalPointOfView(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(0.0, 0.0, -0.0)));
		this.addComponent(this.grassyGround);
		this.sunLight.setName("sunLight");
		this.sunLight.setLocalPointOfView(new PointOfView(new Quaternion(-0.7071067811865475, 0.0, 0.0, 0.7071067811865476), new Position(0.0, 0.0, 0.0)));
		this.addComponent(this.sunLight);
		this.playerOne.setName("agentAjunt");
		//		personList.add(playerOne);
		this.playerOne.setLocalPointOfView(new PointOfView(new Quaternion(-1.7987036556935211E-19, 0.9483553656895922, 5.0876937649104405E-19, 0.317209868017626), new Position(0.6599924686175442, -0.004437316378993653, 0.12842369889240057)));
		this.addComponent(this.playerOne);
		playerOne.setOrigin(new PointOfView(new Quaternion(-1.7987036556935211E-19, 0.9483553656895922, 5.0876937649104405E-19, 0.317209868017626), new Position(0.6599924686175442, -0.004437316378993653, 0.12842369889240057)));
		this.henchmanOne.setName("henchman");
		//		personList.add(henchmanOne);
		this.henchmanOne.setLocalPointOfView(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(-5.595242100858692, -0.008897937654938515, 13.582435066495332)));
		this.addComponent(this.henchmanOne);
		henchmanOne.setOrigin(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(-5.595242100858692, -0.008897937654938515, 13.582435066495332)));
		this.henchmanTwo.setName("henchman");
		//		personList.add(henchmanTwo);
		this.henchmanTwo.setLocalPointOfView(new PointOfView(new Quaternion(-4.2703851302360884E-19, -0.6635252424331612, -2.726637159167332E-19, 0.7481538963702685), new Position(-9.995370370652763, -0.008897937654938515, 6.1296244020257955)));
		this.addComponent(this.henchmanTwo);
		henchmanTwo.setOrigin(new PointOfView(new Quaternion(-4.2703851302360884E-19, -0.6635252424331612, -2.726637159167332E-19, 0.7481538963702685), new Position(-9.995370370652763, -0.008897937654938515, 6.1296244020257955)));
		this.henchmanOneBall.setName("basketball");
		this.henchmanOneBall.setLocalPointOfView(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(-6.002203812380754, 0.9972925168476782, 13.627007453996432)));
		this.addComponent(this.henchmanOneBall);
		this.henchmanTwoBall.setName("basketball");
		this.henchmanTwoBall.setLocalPointOfView(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(-10.120995864641063, 0.9437938601508173, 5.599327605675515)));
		this.addComponent(this.henchmanTwoBall);
		this.basketballOne.setName("basketball");
		this.basketballOne.setLocalPointOfView(new PointOfView(new Quaternion(0.0, 0.0, 0.0, 1.0), new Position(0.35231681299633, 0.6847611140366644, -0.032300300510736735)));
		this.addComponent(this.basketballOne);
		System.out.println("YOU ARE NOW STARTING THE GAME!!!");
	}

	public void performSetUp() {
		this.performSceneEditorGeneratedSetUp();
		this.performCustomPropertySetUp();
	}
}

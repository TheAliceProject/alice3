import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.MoveDirection;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;


public class HenchmansBall extends MyBasketball{

	public MyAgentAjunt agent;

	public HenchmansBall(MyAgentAjunt agent) {
		this.agent = agent;
	}

	public void addToHand(MyHenchman mine){
		this.setVehicle(mine.getPart(MyHenchman.Part.RightHand));
		this.setPositionRightNow(-.05, 0, 0, AsSeenBy.PARENT);
	}
	
	private boolean isCollision(Point3 ballMin, Point3 ballMax, Point3 agentMin, Point3 agentMax) {
		if((agentMin.x < ballMin.x && ballMin.x < agentMax.x 
			&& agentMin.y < ballMin.y && ballMin.y < agentMax.y
			&& agentMin.z < ballMin.z && ballMin.z < agentMax.z)
			|| (agentMin.x < ballMax.x && ballMax.x < agentMax.x
				&& agentMin.y < ballMax.y && ballMax.y < agentMax.y
				&& agentMin.z < ballMax.z && ballMax.z < agentMax.z))
			return true;
		else return false;
	}

	public AxisAlignedBox getAxisAlignedBox(){
		return this.getAxisAlignedMinimumBoundingBox(AsSeenBy.SCENE);
	}

	public void hGo() {
//		goThread = new Thread(){
//			public void run() {
//				//				HenchmansBall.this.setOpacity(100);
				flying = true;
				System.out.println("enemy ball: flying initiated!");
				double dist = 0;
				while(flying){
					move(MoveDirection.FORWARD,.2,.001);
					move(MoveDirection.DOWN, .03, .001);
					dist = dist + .2;
					Point3 ballMin = HenchmansBall.this.getAxisAlignedBox().getMinimum();
					Point3 ballMax = HenchmansBall.this.getAxisAlignedBox().getMaximum();
					Point3 agentMin = agent.getAxisAlignedBox().getMinimum();
					Point3 agentMax = agent.getAxisAlignedBox().getMaximum();
					if(isCollision(ballMin, ballMax, agentMin, agentMax)) {
						System.out.println("enemy ball: I've hit him!");
						agent.standing = false;
						flying = false;
					}
					if (dist>10){
						flying=false;
						//						HenchmansBall.this.setOpacity(0);
						System.out.println("enemy ball: im done flying");
					}	
				}
//			}
//		};
//		goThread.start();
	}
}

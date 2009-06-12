
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic.AgentAjunt;
import edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.gym.Basketball;
import org.alice.apis.moveandturn.*;

public class MyBasketball extends Basketball {

	Thread goThread;
	public boolean flying = false;
	public Person agent;

	public MyBasketball() {
	}
	
	public void reset(MyAgentAjunt agent){
		this.setVehicle(agent.getPart(AgentAjunt.Part.RightHand));
		this.setPositionRightNow(-.05, 0, 0, AsSeenBy.PARENT);
	}
	
	public void go() {
		goThread = new Thread(){
			public void run() {
				flying = true;
				System.out.println("flying initiated!");
				double dist = 0;
				while(flying){
					move(MoveDirection.FORWARD,.1,.001);
					dist = dist + .1;
					if (dist%1==0)
						System.out.println("flying! " + dist);
					else if (dist>12)
						flying=false;
				}
				System.out.println("I'm done flying");

			}
		};
		goThread.start();
	}
}


import org.alice.apis.moveandturn.*;

import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.inputdevice.wii.WiiRemote;

public class MyProgram extends edu.wustl.cse.lookingglass.inputdevice.wii.program.Program {

    private MyScene scene = new MyScene();

    public MyProgram() {
    }

    protected void initialize() {
        this.scene.performSetUp();
        this.setScene(this.scene);
    }

    protected void run() {
    	WiiRemote[] wiiRemotes = detectAndWaitForWiiRemotes(1);
        PrintUtilities.println("detected");
        this.scene.setWiiRemotes(wiiRemotes);
        this.scene.run();
    }

    public static void main(String[] args) {
        MyProgram myProgram = new MyProgram();
        myProgram.showInJFrame(args, true);
    }
}

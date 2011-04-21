package autotutorial;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ECardTutorial {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ECardMainFrame thisClass = new ECardMainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

}

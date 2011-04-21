package ecard;

public class ECardTutorial {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ECardApplication app = ECardApplication.getSingleton();
				app.initialize( false );
				app.getFrame().setVisible(true);
//				ECardMainFrame thisClass = new ECardMainFrame();
//				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				thisClass.setVisible(true);
			}
		});
	}

}

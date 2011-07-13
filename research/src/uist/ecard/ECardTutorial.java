package uist.ecard;

public class ECardTutorial {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ECardApplication app = new uist.TutorialECard();
				app.initialize( args );
				app.getFrame().setVisible(true);
			}
		});
	}

}

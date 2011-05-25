package uist.ecard;

public class ECardTutorial {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Boolean useRibbon = false;
				if (args.length == 1 && args[0].equalsIgnoreCase("-ribbon")) {
					useRibbon = true;
				}

				ECardApplication app = ECardApplication.getSingleton();
				app.initialize( args );
				app.getFrame().setVisible(true);
			}
		});
	}

}

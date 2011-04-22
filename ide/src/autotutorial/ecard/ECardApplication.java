package autotutorial.ecard;

import java.awt.Image;

import autotutorial.ecard.menu.MenuBarModel;
import edu.cmu.cs.dennisc.croquet.ScrollPane;

public class ECardApplication extends edu.cmu.cs.dennisc.croquet.Application {
	public static final edu.cmu.cs.dennisc.croquet.Group HISTORY_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final edu.cmu.cs.dennisc.croquet.Group URI_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	protected String year = "2003";
	protected ECardPanel cardPanel;
	
	protected ECardApplication() {
		singleton = this;
	}

	private static ECardApplication singleton;
	public static ECardApplication getSingleton() {
		if (singleton == null) {
			singleton = new ECardApplication();
		}
		return ECardApplication.singleton;
	}
	
	@Override
	public void initialize( String args[] ) {
		int x = Integer.parseInt( args[ 0 ] );
		int y = Integer.parseInt( args[ 1 ] );
		int width = Integer.parseInt( args[ 2 ] );
		int height = Integer.parseInt( args[ 3 ] );
		this.year = args[ 4 ];

		try {
			// We like nimbus?
			//javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// We like the current platform?
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image icon = java.awt.Toolkit.getDefaultToolkit().getImage( ECardApplication.class.getResource( "resources/e-card-icon.png" ));
		getFrame().getAwtComponent().setIconImage(icon);
		
		//super.initialize(null);

		// Best size for screen shots
		getFrame().setSize( x, y );
		getFrame().setSize( width, height );

		super.initialize( args );
	}
	
	public ECardPanel getCardPanel() {
		return this.cardPanel;
	}

	public String getYear() {
		return this.year;
	}
	public boolean isRibbonBased() {
		return "2007".equals( this.year );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		
		this.cardPanel = new ECardPanel(ECardPanel.CardState.PHOTO);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setViewportView(new edu.cmu.cs.dennisc.croquet.SwingAdapter(cardPanel));
		rv.addComponent( scrollPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
//		rv.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.cardPanel ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		
		// TODO: Set scroll pane vertical to got to bottom
		// TODO: Set scroll pane horizontal to center.
		
		if (this.isRibbonBased()) {
			rv.addComponent( autotutorial.ecard.ribbon.ECardRibbonModel.getInstance().createDefaultFolderTabbedPane(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		} else {
			getFrame().setMenuBarModel( MenuBarModel.getInstance() );
			
			// TODO add false toolbar
		}

		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor(edu.cmu.cs.dennisc.croquet.DropSite dropSite) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void handleWindowOpened(java.awt.event.WindowEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void handleAbout(java.util.EventObject e) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void handlePreferences(java.util.EventObject e) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void handleQuit(java.util.EventObject e) {
		System.exit(0);
	}

}

package ecard;

import java.awt.Image;

import ecard.menu.MenuBarModel;

public class ECardApplication extends edu.cmu.cs.dennisc.croquet.Application {
	public static final edu.cmu.cs.dennisc.croquet.Group HISTORY_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final edu.cmu.cs.dennisc.croquet.Group URI_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	protected Boolean useRibbon = false;
	
	protected ECardApplication() {
	}

	private static ECardApplication singleton;
	public static ECardApplication getSingleton() {
		if (singleton == null) {
			singleton = new ECardApplication();
		}

		return ECardApplication.singleton;
	}

	public void initialize(Boolean useRibbon) {
		this.useRibbon = useRibbon;

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
		
		super.initialize(null);

		// Best size for screen shots
		getFrame().setSize(800, 600);
	}

	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		//rv.addComponent( this.root, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		
		if (this.useRibbon) {
			rv.addComponent( ecard.ribbon.ECardRibbonModel.getInstance().createDefaultFolderTabbedPane(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
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

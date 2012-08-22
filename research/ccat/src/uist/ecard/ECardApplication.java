package uist.ecard;

public class ECardApplication extends org.lgna.croquet.simple.SimpleApplication {
	public static final org.lgna.croquet.Group HISTORY_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final org.lgna.croquet.Group URI_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	protected String year = "2003";
	protected ECardPanel cardPanel;

	public static ECardApplication getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.Application.getActiveInstance(), ECardApplication.class );
	}
	@Override
	public void initialize( String args[] ) {
		this.setPerspective( MainPerspective.getInstance() );
		int x = Integer.parseInt( args[ 0 ] );
		int y = Integer.parseInt( args[ 1 ] );
		int width = Integer.parseInt( args[ 2 ] );
		int height = Integer.parseInt( args[ 3 ] );
		this.year = args[ 4 ];

		try {
			if (isRibbonBased()) {
				// Office 2007
				javax.swing.UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel());
			} else {
				// We like nimbus?
				//javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				// We like the current platform?
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( e, "setLookAndFeel" );
		}

		java.awt.Image icon = java.awt.Toolkit.getDefaultToolkit().getImage( ECardApplication.class.getResource( "resources/e-card-icon.png" ));
		getFrame().getAwtComponent().setIconImage(icon);

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
}

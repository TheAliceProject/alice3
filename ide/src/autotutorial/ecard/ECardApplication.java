package autotutorial.ecard;

import javax.swing.ImageIcon;
import javax.swing.JButton;

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
			if (isRibbonBased()) {
				// Ofice 2007
				javax.swing.UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel());
			} else {
			// We like nimbus?
			//javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// We like the current platform?
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();

		this.cardPanel = new ECardPanel(ECardPanel.CardState.PHOTO);
		rv.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter(this.cardPanel), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		if (this.isRibbonBased()) {
			edu.cmu.cs.dennisc.croquet.FolderTabbedPane< ? > folderTabbedPane = autotutorial.ecard.ribbon.ECardRibbonModel.getInstance().createDefaultFolderTabbedPane();
			folderTabbedPane.setBackgroundColor( java.awt.SystemColor.controlShadow );
			rv.addComponent( folderTabbedPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		} else {
			getFrame().setMenuBarModel( autotutorial.ecard.menu.MenuBarModel.getInstance() );

			// Make mock toolbar
			javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
			toolbar.setFloatable(false);
			toolbar.setRollover(true);

			JButton newButton = new JButton();
			newButton.setFocusable(false);
			newButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/document-new.png")));
			toolbar.add(newButton);

			JButton openButton = new JButton();
			openButton.setFocusable(false);
			openButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/document-open.png")));
			toolbar.add(openButton);

			JButton saveButton = new JButton();
			saveButton.setFocusable(false);
			saveButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/document-save.png")));
			toolbar.add(saveButton);

			toolbar.addSeparator();

			JButton printButton = new JButton();
			printButton.setFocusable(false);
			//printButton.setText("Print");
			printButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/document-print.png")));
			toolbar.add(printButton);

			toolbar.addSeparator();

			JButton undoButton = new JButton();
			undoButton.setFocusable(false);
			undoButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-undo.png")));
			toolbar.add(undoButton);

			JButton redoButton = new JButton();
			redoButton.setFocusable(false);
			redoButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-redo.png")));
			toolbar.add(redoButton);

			toolbar.addSeparator();

			JButton copyButton = new JButton();
			copyButton.setFocusable(false);
			copyButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-copy.png")));
			toolbar.add(copyButton);	

			JButton cutButton = new JButton();
			cutButton.setFocusable(false);
			cutButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-cut.png")));
			toolbar.add(cutButton);	

			JButton pasteButton = new JButton();
			pasteButton.setFocusable(false);
			pasteButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-paste.png")));
			toolbar.add(pasteButton);	
			
			//toolbar.addSeparator();
			
			//JButton deleteJButton = DeletePictureModel.getInstance().createButton().getAwtComponent();
			//deleteJButton.setFocusable(false);
			//toolbar.add(deleteJButton);	

			rv.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(toolbar) , edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
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

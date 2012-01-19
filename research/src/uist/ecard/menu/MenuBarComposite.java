package uist.ecard.menu;

public class MenuBarComposite extends org.lgna.croquet.MenuBarComposite {
	private static class SingletonHolder {
		private static MenuBarComposite instance = new MenuBarComposite();
	}
	public static MenuBarComposite getInstance() {
		return SingletonHolder.instance;
	}
	private MenuBarComposite() {
		super( java.util.UUID.fromString( "78686290-eac5-47b3-9ec1-625fdb838721" ) );
		this.addItem( FileMenuModel.getInstance() );
		this.addItem( EditMenuModel.getInstance() );
		this.addItem( InsertMenuModel.getInstance() );
		this.addItem( PictureEffectsMenuModel.getInstance() );
		this.addItem( HelpMenuModel.getInstance() );
	}
}
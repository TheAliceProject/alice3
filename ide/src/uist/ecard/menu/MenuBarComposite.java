package uist.ecard.menu;

public class MenuBarComposite extends edu.cmu.cs.dennisc.croquet.MenuBarComposite {
	private static class SingletonHolder {
		private static MenuBarComposite instance = new MenuBarComposite();
	}
	public static MenuBarComposite getInstance() {
		return SingletonHolder.instance;
	}
	private MenuBarComposite() {
		super( java.util.UUID.fromString( "78686290-eac5-47b3-9ec1-625fdb838721" ) );
		this.addMenuModel( FileMenuModel.getInstance() );
		this.addMenuModel( EditMenuModel.getInstance() );
		this.addMenuModel( InsertMenuModel.getInstance() );
		this.addMenuModel( PictureEffectsMenuModel.getInstance() );
		this.addMenuModel( HelpMenuModel.getInstance() );
	}
}
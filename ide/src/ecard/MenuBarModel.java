package ecard;

public class MenuBarModel extends edu.cmu.cs.dennisc.croquet.MenuBarModel {
	private static class SingletonHolder {
		private static MenuBarModel instance = new MenuBarModel();
	}
	public static MenuBarModel getInstance() {
		return SingletonHolder.instance;
	}
	private MenuBarModel() {
		super( java.util.UUID.fromString( "78686290-eac5-47b3-9ec1-625fdb838721" ) );
		this.addMenuModel( FileMenuModel.getInstance() );
		this.addMenuModel( EditMenuModel.getInstance() );
		this.addMenuModel( InsertMenuModel.getInstance() );
		this.addMenuModel( PictureEffectsMenuModel.getInstance() );
		this.addMenuModel( HelpMenuModel.getInstance() );
	}
}
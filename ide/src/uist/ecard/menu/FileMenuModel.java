package uist.ecard.menu;

public class FileMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static FileMenuModel instance = new FileMenuModel();
	}
	public static FileMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private FileMenuModel() {
		super( java.util.UUID.fromString( "19120db4-10f8-4787-9453-4c5a83a7376f" ), 
				org.lgna.croquet.MenuModel.SEPARATOR
		);
	}
}
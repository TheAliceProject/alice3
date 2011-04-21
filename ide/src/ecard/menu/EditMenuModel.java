package ecard.menu;

public class EditMenuModel extends edu.cmu.cs.dennisc.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static EditMenuModel instance = new EditMenuModel();
	}
	public static EditMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private EditMenuModel() {
		super( java.util.UUID.fromString( "78c4b609-3100-4992-b494-852181c73492" ), 
				edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR
		);
	}
}
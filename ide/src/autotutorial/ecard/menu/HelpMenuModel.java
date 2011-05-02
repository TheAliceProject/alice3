package autotutorial.ecard.menu;

public class HelpMenuModel extends edu.cmu.cs.dennisc.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static HelpMenuModel instance = new HelpMenuModel();
	}
	public static HelpMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private HelpMenuModel() {
		super( java.util.UUID.fromString( "a03f6538-fbd2-4268-af38-7d6ad7ff7b99" ), 
				edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR
		);
	}
}
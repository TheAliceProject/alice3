package uist.ecard.menu;

public class HelpMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static HelpMenuModel instance = new HelpMenuModel();
	}
	public static HelpMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private HelpMenuModel() {
		super( java.util.UUID.fromString( "a03f6538-fbd2-4268-af38-7d6ad7ff7b99" ), 
				org.lgna.croquet.MenuModel.SEPARATOR
		);
	}
}
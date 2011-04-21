package ecard;

public class InsertMenuModel extends edu.cmu.cs.dennisc.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static InsertMenuModel instance = new InsertMenuModel();
	}
	public static InsertMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private InsertMenuModel() {
		super( java.util.UUID.fromString( "cedd529a-38e1-498f-b998-416d98e38c3b" ) );
	}
}
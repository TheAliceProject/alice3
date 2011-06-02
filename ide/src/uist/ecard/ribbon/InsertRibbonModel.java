package uist.ecard.ribbon;

public class InsertRibbonModel extends org.lgna.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static InsertRibbonModel instance = new InsertRibbonModel();
	}
	public static InsertRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private InsertRibbonModel() {
		super( java.util.UUID.fromString( "9c428c49-11f7-485a-ba03-dc9f0e3d3ec6" ) );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
		org.lgna.croquet.components.PageAxisPanel rv = new org.lgna.croquet.components.PageAxisPanel();
		rv.setBackgroundColor(java.awt.SystemColor.control);
		return rv;
	}
}
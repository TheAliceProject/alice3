package autotutorial.ecard.ribbon;

public class InsertRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
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
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		return new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
	}
}
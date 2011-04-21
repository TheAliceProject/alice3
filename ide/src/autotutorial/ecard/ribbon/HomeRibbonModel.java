package autotutorial.ecard.ribbon;

public class HomeRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static HomeRibbonModel instance = new HomeRibbonModel();
	}
	public static HomeRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private HomeRibbonModel() {
		super( java.util.UUID.fromString( "808f41b6-c1e1-4c79-8d8f-de43900ae17b" ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		return new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
	}
}
package ecard.ribbon;

public class PictureEffectsRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static PictureEffectsRibbonModel instance = new PictureEffectsRibbonModel();
	}
	public static PictureEffectsRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private PictureEffectsRibbonModel() {
		super( java.util.UUID.fromString( "e0b095cb-1cba-443d-b320-c0fc4f7c3312" ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		return ecard.ThoughtBubbleEffectModel.getInstance().createButton();
	}
}
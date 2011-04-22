package autotutorial.ecard.ribbon;

public class PictureEffectsRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static PictureEffectsRibbonModel instance = new PictureEffectsRibbonModel();
	}
	public static PictureEffectsRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private edu.cmu.cs.dennisc.croquet.Operation< ? >[] operations = new edu.cmu.cs.dennisc.croquet.Operation< ? >[] {
			autotutorial.ecard.menu.DropShadowModel.getInstance(),
			null,
			autotutorial.ecard.menu.RoundedCornersModel.getInstance(),
			autotutorial.ecard.ThoughtBubbleEffectModel.getInstance(),
			autotutorial.ecard.menu.EllipseModel.getInstance(),
			null,
			autotutorial.ecard.menu.RotateForwardModel.getInstance(),
			autotutorial.ecard.menu.RotateBackwardModel.getInstance()
	};
	private PictureEffectsRibbonModel() {
		super( java.util.UUID.fromString( "e0b095cb-1cba-443d-b320-c0fc4f7c3312" ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.LineAxisPanel rv = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();
		for( edu.cmu.cs.dennisc.croquet.Operation< ? > operation : this.operations ) {
			if( operation != null ) {
				rv.addComponent( operation.createButton() );
			} else {
				rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
			}
		}
		return rv;
	}
	@Override
	public boolean contains( edu.cmu.cs.dennisc.croquet.Model model ) {
		for( edu.cmu.cs.dennisc.croquet.Operation< ? > operation : this.operations ) {
			if( operation == model ) {
				return true;
			}
		}
		return false;
//		return edu.cmu.cs.dennisc.java.util.Collections.newHashSet( this.operations ).contains( model );
	}
}
package uist.ecard.ribbon;

public class ECardRibbonModel extends org.lgna.croquet.TabSelectionState {
	private static class SingletonHolder {
		private static ECardRibbonModel instance = new ECardRibbonModel();
	}
	public static ECardRibbonModel getInstance() {
		return SingletonHolder.instance;
	}

	private ECardRibbonModel() {
		super( org.alice.ide.IDE.DOCUMENT_UI_GROUP, 
				java.util.UUID.fromString( "ad55dddc-74f6-418e-ba2b-7627e0cea43c" ), 
				org.alice.ide.croquet.codecs.SingletonCodec.getInstance( org.lgna.croquet.TabComposite.class ), 
				0,
				HomeRibbonModel.getInstance(),
				InsertRibbonModel.getInstance(),
				PictureEffectsRibbonModel.getInstance()
		);
	}
//	@Override
//	protected void localize() {
//		super.localize();
//		HomeRibbonModel.getInstance().setTitleText("Home");
//		InsertRibbonModel.getInstance().setTitleText("Insert");
//		PictureEffectsRibbonModel.getInstance().setTitleText("Picture Effects");
//	}
}
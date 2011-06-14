package uist.ecard.menu;

public class PictureEffectsMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
	private static class SingletonHolder {
		private static PictureEffectsMenuModel instance = new PictureEffectsMenuModel();
	}
	public static PictureEffectsMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	private PictureEffectsMenuModel() {
		super( java.util.UUID.fromString( "c7d44cca-5fa8-48a6-8904-20c0ee16b38b" ), 
				DropShadowModel.getInstance().getMenuItemPrepModel(),
				org.lgna.croquet.MenuModel.SEPARATOR,
				RoundedCornersModel.getInstance().getMenuItemPrepModel(),
				uist.ecard.ThoughtBubbleEffectModel.getInstance().getMenuItemPrepModel(),
				EllipseModel.getInstance().getMenuItemPrepModel(),
				org.lgna.croquet.MenuModel.SEPARATOR,
				RotateForwardModel.getInstance().getMenuItemPrepModel(),
				RotateBackwardModel.getInstance().getMenuItemPrepModel()
		);
	}
}
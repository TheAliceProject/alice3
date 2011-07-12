package uist.ecard;

import javax.swing.ImageIcon;

public class DeletePictureModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static DeletePictureModel instance = new DeletePictureModel();
	}
	public static DeletePictureModel getInstance() {
		return SingletonHolder.instance;
	}

	private DeletePictureModel() {
		super( null, java.util.UUID.fromString( "f7efb45b-d71d-4114-ad1a-dbb31ead5215" ) );
		if (ECardApplication.getActiveInstance().isRibbonBased()) {
			setSmallIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-delete.png") ));
		} else {
			setSmallIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/toolbar/edit-delete.png") ));
		}
	}

	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		ECardApplication.getActiveInstance().getCardPanel().setImage(ECardPanel.CardState.EMPTY);
		step.finish();
	}
}
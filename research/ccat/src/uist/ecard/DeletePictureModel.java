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
			setSmallIcon(new ImageIcon(DeletePictureModel.class.getResource( "resources/ribbon/edit-delete.png") ));
		} else {
			setSmallIcon(new ImageIcon(DeletePictureModel.class.getResource( "resources/toolbar/edit-delete.png") ));
		}
	}

	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
		ECardApplication.getActiveInstance().getCardPanel().setImage(ECardPanel.CardState.EMPTY);
		step.finish();
	}
}
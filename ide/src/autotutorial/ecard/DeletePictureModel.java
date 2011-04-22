package autotutorial.ecard;

public class DeletePictureModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static DeletePictureModel instance = new DeletePictureModel();
	}
	public static DeletePictureModel getInstance() {
		return SingletonHolder.instance;
	}

	private DeletePictureModel() {
		super( null, java.util.UUID.fromString( "f7efb45b-d71d-4114-ad1a-dbb31ead5215" ) );
	}
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		ECardApplication.getSingleton().getCardPanel().setImage(ECardPanel.CardState.EMPTY);
		context.finish();
	}
}
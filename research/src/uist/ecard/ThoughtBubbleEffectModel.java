package uist.ecard;

public class ThoughtBubbleEffectModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static ThoughtBubbleEffectModel instance = new ThoughtBubbleEffectModel();
	}
	public static ThoughtBubbleEffectModel getInstance() {
		return SingletonHolder.instance;
	}
	
	private ThoughtBubbleEffectModel() {
		super( null, java.util.UUID.fromString( "73fa6653-073e-4ab0-91ab-3148678b5226" ) );
		this.setSmallIcon( new javax.swing.ImageIcon(getClass().getResource("resources/menu/thought-bubble.png") ) );
	}
	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		ECardApplication.getActiveInstance().getCardPanel().setImage(ECardPanel.CardState.BUBBLE_PHOTO);
		step.finish();
	}
	@Override
	protected java.lang.StringBuilder updateTutorialStepText( java.lang.StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		rv.append( "Select " + this.getDefaultLocalizedText() );
		return rv;
	}
}
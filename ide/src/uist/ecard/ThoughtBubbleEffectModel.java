package uist.ecard;

public class ThoughtBubbleEffectModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static ThoughtBubbleEffectModel instance = new ThoughtBubbleEffectModel();
	}
	public static ThoughtBubbleEffectModel getInstance() {
		return SingletonHolder.instance;
	}
	
	private ThoughtBubbleEffectModel() {
		super( null, java.util.UUID.fromString( "73fa6653-073e-4ab0-91ab-3148678b5226" ) );
		this.setSmallIcon( new javax.swing.ImageIcon(getClass().getResource("/autotutorial/ecard/resources/menu/thought-bubble.png") ) );
	}
	@Override
	protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
		ECardApplication.getSingleton().getCardPanel().setImage(ECardPanel.CardState.BUBBLE_PHOTO);
		step.finish();
	}
	@Override
	protected java.lang.StringBuilder updateTutorialStepText( java.lang.StringBuilder rv, org.lgna.croquet.steps.Step< ? > step, edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		rv.append( "Select " + this.getDefaultLocalizedText() );
		return rv;
	}
}
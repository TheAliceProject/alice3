package autotutorial.ecard;

import javax.swing.ImageIcon;

public class ThoughtBubbleEffectModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static ThoughtBubbleEffectModel instance = new ThoughtBubbleEffectModel();
	}
	public static ThoughtBubbleEffectModel getInstance() {
		return SingletonHolder.instance;
	}
	
	private ThoughtBubbleEffectModel() {
		super( null, java.util.UUID.fromString( "73fa6653-073e-4ab0-91ab-3148678b5226" ) );
		this.setSmallIcon( new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/menu/thought-bubble.png") ) );
	}
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		ECardApplication.getSingleton().getCardPanel().setImage(ECardPanel.CardState.BUBBLE_PHOTO);
		context.finish();
	}
}
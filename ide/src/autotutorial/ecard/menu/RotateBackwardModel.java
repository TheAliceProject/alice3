package autotutorial.ecard.menu;

import javax.swing.ImageIcon;

public class RotateBackwardModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RotateBackwardModel instance = new RotateBackwardModel();
	}
	public static RotateBackwardModel getInstance() {
		return SingletonHolder.instance;
	}
	private RotateBackwardModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		this.setSmallIcon( new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/menu/rotate-counter-clockwise.png") ) );
	}
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		// TODO Auto-generated method stub
	}
}
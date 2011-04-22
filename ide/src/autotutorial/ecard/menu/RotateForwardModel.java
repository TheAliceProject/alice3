package autotutorial.ecard.menu;

import javax.swing.ImageIcon;

public class RotateForwardModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RotateForwardModel instance = new RotateForwardModel();
	}
	public static RotateForwardModel getInstance() {
		return SingletonHolder.instance;
	}
	private RotateForwardModel() {
		super( null, java.util.UUID.fromString( "510dd64d-ce22-41a2-adeb-73ad8285187b" ) );
		this.setSmallIcon( new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/menu/rotate-clockwise.png") ) );
	}
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		// TODO Auto-generated method stub
	}
}
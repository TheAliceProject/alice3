package uist.ecard.menu;

import javax.swing.ImageIcon;

import uist.ecard.ECardApplication;


public class RotateForwardModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RotateForwardModel instance = new RotateForwardModel();
	}
	public static RotateForwardModel getInstance() {
		return SingletonHolder.instance;
	}
	private RotateForwardModel() {
		super( null, java.util.UUID.fromString( "510dd64d-ce22-41a2-adeb-73ad8285187b" ) );
		if (ECardApplication.getActiveInstance().isRibbonBased()) {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/ribbon/rotate-clockwise.png") ) );
		} else {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/rotate-clockwise.png") ) );
		}
	}
	@Override
	protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
		// TODO Auto-generated method stub
	}
}
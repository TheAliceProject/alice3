package uist.ecard.menu;

import javax.swing.ImageIcon;

import uist.ecard.ECardApplication;


public class RotateBackwardModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RotateBackwardModel instance = new RotateBackwardModel();
	}
	public static RotateBackwardModel getInstance() {
		return SingletonHolder.instance;
	}
	private RotateBackwardModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		if (ECardApplication.getActiveInstance().isRibbonBased()) {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/ribbon/rotate-counter-clockwise.png") ) );
		} else {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/rotate-counter-clockwise.png") ) );
		}
	}
	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		// TODO Auto-generated method stub
	}
}
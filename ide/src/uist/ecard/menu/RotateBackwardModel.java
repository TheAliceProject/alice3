package uist.ecard.menu;

import javax.swing.ImageIcon;

import uist.ecard.ECardApplication;


public class RotateBackwardModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RotateBackwardModel instance = new RotateBackwardModel();
	}
	public static RotateBackwardModel getInstance() {
		return SingletonHolder.instance;
	}
	private RotateBackwardModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		if (ECardApplication.getSingleton().isRibbonBased()) {
			this.setSmallIcon( new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/rotate-counter-clockwise.png") ) );
		} else {
			this.setSmallIcon( new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/menu/rotate-counter-clockwise.png") ) );
		}
	}
	@Override
	protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
		// TODO Auto-generated method stub
	}
}
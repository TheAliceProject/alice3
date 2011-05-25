package uist.ecard.menu;

import javax.swing.ImageIcon;

import uist.ecard.ECardApplication;


public class DropShadowModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static DropShadowModel instance = new DropShadowModel();
	}
	public static DropShadowModel getInstance() {
		return SingletonHolder.instance;
	}
	private DropShadowModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		if (ECardApplication.getSingleton().isRibbonBased()) {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/ribbon/drop-shadow.png") ) );
		} else {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/drop-shadow.png") ) );
		}
	}
	@Override
	protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
		// TODO Auto-generated method stub
	}
}
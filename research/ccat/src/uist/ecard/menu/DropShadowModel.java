package uist.ecard.menu;

import javax.swing.ImageIcon;

import uist.ecard.ECardApplication;


public class DropShadowModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static DropShadowModel instance = new DropShadowModel();
	}
	public static DropShadowModel getInstance() {
		return SingletonHolder.instance;
	}
	private DropShadowModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		if (ECardApplication.getActiveInstance().isRibbonBased()) {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/ribbon/drop-shadow.png") ) );
		} else {
			this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/drop-shadow.png") ) );
		}
	}
	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
		// TODO Auto-generated method stub
	}
}
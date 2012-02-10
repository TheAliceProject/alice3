package uist.ecard.menu;

import javax.swing.ImageIcon;

public class RoundedCornersModel extends org.lgna.croquet.ActionOperation {
	private static class SingletonHolder {
		private static RoundedCornersModel instance = new RoundedCornersModel();
	}
	public static RoundedCornersModel getInstance() {
		return SingletonHolder.instance;
	}
	private RoundedCornersModel() {
		super( null, java.util.UUID.fromString( "3e8dec1d-8fec-41c3-961a-1828fbb17250" ) );
		this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/rounded-corners.png") ) );
	}
	@Override
	protected void perform( org.lgna.croquet.history.OperationStep step ) {
		// TODO Auto-generated method stub
	}
}
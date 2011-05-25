package uist.ecard.menu;

import javax.swing.ImageIcon;

public class EllipseModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static EllipseModel instance = new EllipseModel();
	}
	public static EllipseModel getInstance() {
		return SingletonHolder.instance;
	}
	private EllipseModel() {
		super( null, java.util.UUID.fromString( "2ba1d48e-7474-490a-beba-38c3f0a40352" ) );
		this.setSmallIcon( new ImageIcon(getClass().getResource("../resources/menu/ellipse.png") ) );
	}
	@Override
	protected void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
		// TODO Auto-generated method stub
	}
}
package ecard.menu;
public class DropShadowModel extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static class SingletonHolder {
		private static DropShadowModel instance = new DropShadowModel();
	}
	public static DropShadowModel getInstance() {
		return SingletonHolder.instance;
	}
	private DropShadowModel() {
		super( null, java.util.UUID.fromString( "88d087c4-a144-4418-9727-9ad752df5a6a" ) );
		//this.setSmallIcon( new javax.swing.ImageIcon( RedoOperation.class.getResource( "images/undo.png" ) ) );
	}
	@Override
	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		// TODO Auto-generated method stub
	}
}
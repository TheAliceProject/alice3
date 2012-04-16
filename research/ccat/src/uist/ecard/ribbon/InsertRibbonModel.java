package uist.ecard.ribbon;

public class InsertRibbonModel extends org.lgna.croquet.TabComposite {
	private static class SingletonHolder {
		private static InsertRibbonModel instance = new InsertRibbonModel();
	}
	public static InsertRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private InsertRibbonModel() {
		super( java.util.UUID.fromString( "9c428c49-11f7-485a-ba03-dc9f0e3d3ec6" ) );
	}
	
	@Override
	public boolean isCloseable() {
		return false;
	}
	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		//todo
		return false;
	}
	@Override
	protected org.lgna.croquet.components.View createView() {
		org.lgna.croquet.components.PageAxisPanel rv = new org.lgna.croquet.components.PageAxisPanel( this );
		rv.setBackgroundColor(java.awt.SystemColor.control);
		return rv;
	}
}
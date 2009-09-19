package edu.cmu.cs.dennisc.toolkit.hyperlink;

public class HyperlinkOperation extends zoot.InconsequentialActionOperation {
	private String url;

	public HyperlinkOperation( String url ) {
		this( url, url );
	}
	public HyperlinkOperation( String url, String text ) {
		this.url = url;
		this.putValue( javax.swing.Action.NAME, text );
		this.putValue( javax.swing.Action.SHORT_DESCRIPTION, url );
		this.putValue( javax.swing.Action.LONG_DESCRIPTION, url );
	}
	@Override
	protected void performInternal(zoot.ActionContext actionContext) {
		try {
			edu.cmu.cs.dennisc.browser.BrowserUtilities.browse( this.url );
		} catch( Exception e ) {
			e.printStackTrace();
			edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( url );
			javax.swing.JOptionPane.showMessageDialog( this.getSourceComponent( actionContext ), "Alice was unable to launch your default browser.\n\nThe text\n\n    " + url + "\n\nhas been copied to your clipboard so that you may paste it into the address line of your favorite web browser." );
		}
	}
}

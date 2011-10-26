package org.alice.ide.croquet.models.ui.preferences;

/**
 * @author Kyle Harms, Dennis Cosgrove
 */
public class UserApplicationDirectoryState extends DirectoryState {
	public static final String KEY = "${user_application_documents}";
	private static final java.util.regex.Pattern KEY_PATTERN = java.util.regex.Pattern.compile( java.util.regex.Pattern.quote( KEY ) );
	public String substituteKeyIfNecessary( String value ) {
		java.util.regex.Matcher matcher = KEY_PATTERN.matcher( value );
		return matcher.replaceAll( this.getValue() );
	}
	private static String getInitialValue() {
		java.io.File defaultDirectory = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
		java.io.File directory = new java.io.File( defaultDirectory, org.alice.ide.IDE.getActiveInstance().getApplicationSubPath() );
		java.net.URI uri = directory.toURI();
		return uri.toString();
	}
	private static class SingletonHolder {
		private static UserApplicationDirectoryState instance = new UserApplicationDirectoryState();
	}
	public static UserApplicationDirectoryState getInstance() {
		return SingletonHolder.instance;
	}
	private UserApplicationDirectoryState() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "5f80de2f-5119-4131-96d0-c0b80919a589" ), 
				getInitialValue() 
		);
	}
	@Override
	protected String getPath() {
		return this.getValue();
	}
}

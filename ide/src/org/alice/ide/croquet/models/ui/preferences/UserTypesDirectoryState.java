package org.alice.ide.croquet.models.ui.preferences;

/**
 * @author Kyle Harms, Dennis Cosgrove
 */
public class UserTypesDirectoryState extends DirectoryState {
	private static class SingletonHolder {
		private static UserTypesDirectoryState instance = new UserTypesDirectoryState();
	}
	public static UserTypesDirectoryState getInstance() {
		return SingletonHolder.instance;
	}
	private UserTypesDirectoryState() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "7f431542-fedc-4c21-8719-4f751836addf" ), 
				UserApplicationDirectoryState.KEY + java.io.File.separator + "MyClasses"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );
	}
	@Override
	protected String getPath() {
		return UserApplicationDirectoryState.getInstance().substituteKeyIfNecessary( this.getValue() );
	}
}

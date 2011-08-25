package org.alice.ide.croquet.models.ui.preferences;

public class UserTypesDirectoryState extends org.lgna.croquet.StringState {
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
				UserApplicationDirectoryState.getInstance().getValue() + java.io.File.separator + "MyClasses"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

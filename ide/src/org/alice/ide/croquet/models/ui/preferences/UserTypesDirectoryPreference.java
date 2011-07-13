package org.alice.ide.croquet.models.ui.preferences;

public class UserTypesDirectoryPreference extends org.lgna.croquet.StringState {
	private static class SingletonHolder {
		private static UserTypesDirectoryPreference instance = new UserTypesDirectoryPreference();
	}
	public static UserTypesDirectoryPreference getInstance() {
		return SingletonHolder.instance;
	}
	private UserTypesDirectoryPreference() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "7f431542-fedc-4c21-8719-4f751836addf" ), 
				UserApplicationDirectoryPreference.getInstance().getValue() + "/MyClasses"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

package org.alice.ide.croquet.models.ui.preferences;

public class UserProjectsDirectoryPreference extends org.lgna.croquet.StringState {
	private static class SingletonHolder {
		private static UserProjectsDirectoryPreference instance = new UserProjectsDirectoryPreference();
	}
	public static UserProjectsDirectoryPreference getInstance() {
		return SingletonHolder.instance;
	}
	private UserProjectsDirectoryPreference() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "b6cf8508-35ce-46b5-a208-b53784ebeca6" ), 
				UserApplicationDirectoryPreference.getInstance().getValue() + "/MyProjects"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

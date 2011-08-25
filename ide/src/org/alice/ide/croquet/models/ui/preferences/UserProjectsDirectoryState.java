package org.alice.ide.croquet.models.ui.preferences;

public class UserProjectsDirectoryState extends org.lgna.croquet.StringState {
	private static class SingletonHolder {
		private static UserProjectsDirectoryState instance = new UserProjectsDirectoryState();
	}
	public static UserProjectsDirectoryState getInstance() {
		return SingletonHolder.instance;
	}
	private UserProjectsDirectoryState() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "b6cf8508-35ce-46b5-a208-b53784ebeca6" ), 
				UserApplicationDirectoryState.getInstance().getValue() + java.io.File.separator + "MyProjects"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

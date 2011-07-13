package org.alice.ide.croquet.models.ui.preferences;

public class UserApplicationDirectoryPreference extends org.lgna.croquet.StringState {
	private static class SingletonHolder {
		private static UserApplicationDirectoryPreference instance = new UserApplicationDirectoryPreference();
	}
	public static UserApplicationDirectoryPreference getInstance() {
		return SingletonHolder.instance;
	}
	private UserApplicationDirectoryPreference() {
		super( 
				org.lgna.croquet.Application.UI_STATE_GROUP, 
				java.util.UUID.fromString( "5f80de2f-5119-4131-96d0-c0b80919a589" ), 
				edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + "/" + org.alice.ide.IDE.getActiveInstance().getApplicationSubPath() 
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

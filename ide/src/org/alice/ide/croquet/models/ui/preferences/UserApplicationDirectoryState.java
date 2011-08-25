package org.alice.ide.croquet.models.ui.preferences;

public class UserApplicationDirectoryState extends org.lgna.croquet.StringState {
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
				edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + java.io.File.separator + org.alice.ide.IDE.getActiveInstance().getApplicationSubPath() 
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );

		// Create the directory
		new java.io.File(this.getValue()).mkdir();
	}
	
	public java.io.File getDirectory() {
		return new java.io.File(this.getValue());
	}
}

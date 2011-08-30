package org.alice.ide.croquet.models.ui.preferences;

/**
 * @author Kyle Harms, Dennis Cosgrove
 */
public class UserProjectsDirectoryState extends DirectoryState {
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
				UserApplicationDirectoryState.KEY + DO_NOT_USE_FILE_SEPARATOR_IT_IS_BAD_FOR_YOUR_HEALTH + "MyProjects"
		);
		org.alice.ide.PreferenceManager.registerAndInitializePreference( this );
	}
	@Override
	protected String getPath() {
		return UserApplicationDirectoryState.getInstance().substituteKeyIfNecessary( this.getValue() );
	}
}

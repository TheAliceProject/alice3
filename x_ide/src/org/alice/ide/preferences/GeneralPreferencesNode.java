package org.alice.ide.preferences;

public class GeneralPreferencesNode extends PreferencesNode {
	private static GeneralPreferencesNode singleton;
	public static GeneralPreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new GeneralPreferencesNode();
		}
		return singleton;
	}
	public GeneralPreferencesNode() {
		super(
				new edu.cmu.cs.dennisc.preference.IntPreference( "recentProjectCount", 4 )
		);
	}
}

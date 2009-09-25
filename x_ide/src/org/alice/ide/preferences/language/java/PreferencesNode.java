package org.alice.ide.preferences.language.java;

public class PreferencesNode extends org.alice.ide.preferences.language.PreferencesNode {
	private static PreferencesNode singleton;
	public static PreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new PreferencesNode();
		}
		return singleton;
	}
	private PreferencesNode() {
		super( false );
	}
}

package org.alice.ide.preferences;

public class PerspectivePreferencesNode extends PreferencesNode {
	private static PerspectivePreferencesNode singleton;
	public static PerspectivePreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new PerspectivePreferencesNode();
		}
		return singleton;
	}
	public PerspectivePreferencesNode() {
		super(
				new edu.cmu.cs.dennisc.preference.StringPreference( "selection", "exposure" )
		);
	}
}

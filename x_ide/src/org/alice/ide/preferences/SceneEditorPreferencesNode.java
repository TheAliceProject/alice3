package org.alice.ide.preferences;

public class SceneEditorPreferencesNode extends PreferencesNode {
	private static SceneEditorPreferencesNode singleton;
	public static SceneEditorPreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new SceneEditorPreferencesNode();
		}
		return singleton;
	}
	
	public edu.cmu.cs.dennisc.preference.BooleanPreference isTreeInOverlayWhenContracted = new edu.cmu.cs.dennisc.preference.BooleanPreference( "isTreeInOverlayWhenContracted", false );
	public edu.cmu.cs.dennisc.preference.BooleanPreference isTreeInOverlayWhenExpanded = new edu.cmu.cs.dennisc.preference.BooleanPreference( "isTreeInOverlayWhenExpanded", true );
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[] { isTreeInOverlayWhenContracted, isTreeInOverlayWhenExpanded };
	@Override
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
}

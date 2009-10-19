package org.alice.ide.preferences;

public class SceneEditorPreferences extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	private static SceneEditorPreferences singleton;
	public static SceneEditorPreferences getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new SceneEditorPreferences();
			singleton.initialize();
		}
		return singleton;
	}
	
	public final edu.cmu.cs.dennisc.preference.BooleanPreference isTreeInOverlayWhenContracted = new edu.cmu.cs.dennisc.preference.BooleanPreference( false );
	public final edu.cmu.cs.dennisc.preference.BooleanPreference isTreeInOverlayWhenExpanded = new edu.cmu.cs.dennisc.preference.BooleanPreference( true );
	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 2;
		rv[ 0 ] = this.isTreeInOverlayWhenContracted;
		rv[ 1 ] = this.isTreeInOverlayWhenExpanded;
		return rv;
	}
}

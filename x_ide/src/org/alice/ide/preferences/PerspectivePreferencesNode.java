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
	
	public edu.cmu.cs.dennisc.preference.StringPreference activePerspective = new edu.cmu.cs.dennisc.preference.StringPreference( "activePerspective", "exposure" );
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[] { activePerspective };

	private org.alice.ide.preferences.perspective.PreferencesNode activeProxy = new org.alice.ide.preferences.perspective.PreferencesNode(){
		@Override
		protected boolean isDefaultFieldNameGenerationDesiredByDefault() {
			//todo?
			return false;
		}
		@Override
		protected boolean isSyntaxNoiseDesiredByDefault() {
			//todo?
			return false;
		}
	};
	
	private org.alice.ide.preferences.perspective.PreferencesNode[] availablePreferenceNodes = new org.alice.ide.preferences.perspective.PreferencesNode[] {
			org.alice.ide.preferences.perspective.exposure.PreferencesNode.getSingleton(),
			org.alice.ide.preferences.perspective.preparation.PreferencesNode.getSingleton()	
	};
	public org.alice.ide.preferences.perspective.PreferencesNode getActiveProxy() {
		return this.activeProxy;
	}
	public org.alice.ide.preferences.perspective.PreferencesNode[] getAvailablePreferenceNodes() {
		return this.availablePreferenceNodes;
	}
	@Override
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
}

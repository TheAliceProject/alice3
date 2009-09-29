package org.alice.ide.preferences;

public abstract class PreferencesNode {
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences;
	public PreferencesNode( edu.cmu.cs.dennisc.preference.Preference<?>... preferences ) {
		this.preferences = preferences;
	}
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
}

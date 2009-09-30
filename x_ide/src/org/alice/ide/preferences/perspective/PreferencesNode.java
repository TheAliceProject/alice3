package org.alice.ide.preferences.perspective;

public abstract class PreferencesNode extends org.alice.ide.preferences.PreferencesNode {
	public edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference("isDefaultFieldNameGenerationDesired", this.isDefaultFieldNameGenerationDesiredByDefault());
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[] { isDefaultFieldNameGenerationDesired };
	@Override
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
	protected abstract boolean isDefaultFieldNameGenerationDesiredByDefault();
}

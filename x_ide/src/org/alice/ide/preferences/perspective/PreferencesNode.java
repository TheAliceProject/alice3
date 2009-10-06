package org.alice.ide.preferences.perspective;

public abstract class PreferencesNode extends org.alice.ide.preferences.PreferencesNode {
	public edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference("isDefaultFieldNameGenerationDesired", this.isDefaultFieldNameGenerationDesiredByDefault());
	public edu.cmu.cs.dennisc.preference.BooleanPreference isSyntaxNoiseDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference("isSyntaxNoiseDesired", this.isSyntaxNoiseDesiredByDefault());
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[] { this.isDefaultFieldNameGenerationDesired, this.isSyntaxNoiseDesired };
	@Override
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
	protected abstract boolean isDefaultFieldNameGenerationDesiredByDefault();
	protected abstract boolean isSyntaxNoiseDesiredByDefault();
}

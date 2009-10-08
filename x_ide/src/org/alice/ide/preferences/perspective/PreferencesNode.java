package org.alice.ide.preferences.perspective;

public abstract class PreferencesNode extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	public final edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference(this.isDefaultFieldNameGenerationDesiredByDefault());
	public final edu.cmu.cs.dennisc.preference.BooleanPreference isSyntaxNoiseDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference(this.isSyntaxNoiseDesiredByDefault());
	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 2;
		rv[ 0 ] = this.isDefaultFieldNameGenerationDesired;
		rv[ 1 ] = this.isSyntaxNoiseDesired;
		return rv;
	}
	protected abstract boolean isDefaultFieldNameGenerationDesiredByDefault();
	protected abstract boolean isSyntaxNoiseDesiredByDefault();
}

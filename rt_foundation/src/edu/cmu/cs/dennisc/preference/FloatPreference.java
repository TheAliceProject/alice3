package edu.cmu.cs.dennisc.preference;

public class FloatPreference extends Preference< Float > {
	public FloatPreference( Float defaultValue ) {
		super( defaultValue );
	}
	@Override
	public Float getValue(java.util.prefs.Preferences utilPrefs, String key, Float defaultValue) {
		return utilPrefs.getFloat( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, Float nextValue) {
		utilPrefs.putFloat(key, nextValue);
	}
}

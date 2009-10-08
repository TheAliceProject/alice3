package edu.cmu.cs.dennisc.preference;

public class BooleanPreference extends Preference< Boolean > {
	public BooleanPreference( Boolean defaultValue ) {
		super( defaultValue );
	}
	@Override
	public Boolean getValue(java.util.prefs.Preferences utilPrefs, String key, Boolean defaultValue) {
		return utilPrefs.getBoolean( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, Boolean nextValue) {
		utilPrefs.putBoolean(key, nextValue);
	}
}

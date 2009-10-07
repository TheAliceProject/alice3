package edu.cmu.cs.dennisc.preference;

public class IntPreference extends Preference< Integer > {
	public IntPreference( String key, Integer defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public Integer getValue(java.util.prefs.Preferences preferences, String key, Integer defaultValue) {
		return preferences.getInt( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences preferences, String key, Integer nextValue) {
		preferences.putInt(key, nextValue);
	}
}

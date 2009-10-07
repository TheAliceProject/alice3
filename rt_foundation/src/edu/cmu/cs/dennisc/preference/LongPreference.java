package edu.cmu.cs.dennisc.preference;

public class LongPreference extends Preference< Long > {
	public LongPreference( String key, Long defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public Long getValue(java.util.prefs.Preferences preferences, String key, Long defaultValue) {
		return preferences.getLong( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences preferences, String key, Long nextValue) {
		preferences.putLong(key, nextValue);
	}
}

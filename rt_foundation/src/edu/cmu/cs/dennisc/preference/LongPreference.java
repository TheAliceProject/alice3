package edu.cmu.cs.dennisc.preference;

public class LongPreference extends Preference< Long > {
	public LongPreference( Long defaultValue ) {
		super( defaultValue );
	}
	@Override
	public Long getValue(java.util.prefs.Preferences utilPrefs, String key, Long defaultValue) {
		return utilPrefs.getLong( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, Long nextValue) {
		utilPrefs.putLong(key, nextValue);
	}
}

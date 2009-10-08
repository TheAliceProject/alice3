package edu.cmu.cs.dennisc.preference;

public class IntPreference extends Preference< Integer > {
	public IntPreference( Integer defaultValue ) {
		super( defaultValue );
	}
	@Override
	public Integer getValue(java.util.prefs.Preferences utilPrefs, String key, Integer defaultValue) {
		return utilPrefs.getInt( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, Integer nextValue) {
		utilPrefs.putInt(key, nextValue);
	}
}

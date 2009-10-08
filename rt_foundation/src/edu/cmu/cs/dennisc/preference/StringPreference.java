package edu.cmu.cs.dennisc.preference;

public class StringPreference extends Preference< String > {
	public StringPreference( String defaultValue ) {
		super( defaultValue );
	}
	@Override
	public String getValue(java.util.prefs.Preferences utilPrefs, String key, String defaultValue) {
		return utilPrefs.get(key, defaultValue);
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, String nextValue) {
		utilPrefs.put(key, nextValue);
	}
}

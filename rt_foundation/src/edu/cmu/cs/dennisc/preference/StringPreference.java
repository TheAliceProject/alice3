package edu.cmu.cs.dennisc.preference;

public class StringPreference extends Preference< String > {
	public StringPreference( String key, String defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public String getValue(java.util.prefs.Preferences preferences, String key, String defaultValue) {
		return preferences.get(key, defaultValue);
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences preferences, String key, String nextValue) {
		preferences.put(key, nextValue);
	}
}

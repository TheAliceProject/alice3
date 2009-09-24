package edu.cmu.cs.dennisc.preference;

public class BooleanPreference extends Preference< Boolean > {
	public BooleanPreference( String key, Boolean defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public Boolean getValue(java.util.prefs.Preferences preferences, String key, Boolean defaultValue) {
		return preferences.getBoolean( key, defaultValue );
	}
}

package edu.cmu.cs.dennisc.preference;

public class FloatPreference extends Preference< Float > {
	public FloatPreference( String key, Float defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public Float getValue(java.util.prefs.Preferences preferences, String key, Float defaultValue) {
		return preferences.getFloat( key, defaultValue );
	}
}

package edu.cmu.cs.dennisc.preference;

public class DoublePreference extends Preference< Double > {
	public DoublePreference( String key, Double defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public Double getValue(java.util.prefs.Preferences preferences, String key, Double defaultValue) {
		return preferences.getDouble( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences preferences, String key, Double nextValue) {
		preferences.putDouble(key, nextValue);
	}
}

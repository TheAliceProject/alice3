package edu.cmu.cs.dennisc.preference;

public class DoublePreference extends Preference< Double > {
	public DoublePreference( Double defaultValue ) {
		super( defaultValue );
	}
	@Override
	public Double getValue(java.util.prefs.Preferences utilPrefs, String key, Double defaultValue) {
		return utilPrefs.getDouble( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, Double nextValue) {
		utilPrefs.putDouble(key, nextValue);
	}
}

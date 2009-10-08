package edu.cmu.cs.dennisc.preference;

public class ByteArrayPreference extends Preference< byte[] > {
	public ByteArrayPreference( byte[] defaultValue ) {
		super( defaultValue );
	}
	@Override
	public byte[] getValue(java.util.prefs.Preferences utilPrefs, String key, byte[] defaultValue) {
		return utilPrefs.getByteArray( key, defaultValue );
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, byte[] nextValue) {
		utilPrefs.putByteArray(key, nextValue);
	}
}

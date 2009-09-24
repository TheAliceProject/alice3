package edu.cmu.cs.dennisc.preference;

public class ByteArrayPreference extends Preference< byte[] > {
	public ByteArrayPreference( String key, byte[] defaultValue ) {
		super( key, defaultValue );
	}
	@Override
	public byte[] getValue(java.util.prefs.Preferences preferences, String key, byte[] defaultValue) {
		return preferences.getByteArray( key, defaultValue );
	}
}

package edu.cmu.cs.dennisc.preference;

public abstract class Preference< E > {
	private String key;
	private E defaultValue;
	public Preference( String key, E defaultValue ) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	public String getKey() {
		return this.key;
	}
	public E getDefaultValue() {
		return this.defaultValue;
	}
	public abstract E getValue( java.util.prefs.Preferences preferences, String key, E defaultValue );
	public final E getValue( java.util.prefs.Preferences preferences ) {
		return this.getValue( preferences, this.key, this.defaultValue );
	}
}

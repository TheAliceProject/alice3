package org.alice.ide.preferences.programming.preparation;

public class Configuration implements org.alice.ide.preferences.programming.Configuration {
	private static Configuration singleton;
	public static Configuration getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new Configuration();
		}
		return singleton;
	}
	private Configuration() {
	}
	public java.util.UUID getUUID() {
		return java.util.UUID.fromString( "4f72c01a-6d24-4f21-b4bd-9d5c5ca267eb" );
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return false;
	}
	public boolean isSyntaxNoiseDesired() {
		return true;
	}
}

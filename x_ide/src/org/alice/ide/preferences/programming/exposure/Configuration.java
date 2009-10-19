package org.alice.ide.preferences.programming.exposure;

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
		return java.util.UUID.fromString( "76bedbed-32d7-49f1-ab2a-df2997c1dd7c" );
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return true;
	}
	public boolean isSyntaxNoiseDesired() {
		return false;
	}
}

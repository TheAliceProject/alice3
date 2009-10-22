package org.alice.ide.preferences.programming.exposure;

public class ExposureConfiguration extends org.alice.ide.preferences.programming.BuiltInConfiguration {
	private static ExposureConfiguration singleton;
	public static ExposureConfiguration getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new ExposureConfiguration();
		}
		return singleton;
	}
	private ExposureConfiguration() {
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

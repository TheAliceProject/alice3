package org.alice.ide.preferences.programming.preparation;

public class PreparationConfiguration extends org.alice.ide.preferences.programming.BuiltInConfiguration {
	private static PreparationConfiguration singleton;
	public static PreparationConfiguration getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new PreparationConfiguration();
		}
		return singleton;
	}
	private PreparationConfiguration() {
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

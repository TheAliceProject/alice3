package org.alice.ide.preferences;

public class ProgrammingPreferences extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	private static ProgrammingPreferences singleton;
	public static ProgrammingPreferences getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new ProgrammingPreferences();
			singleton.initialize();
		}
		return singleton;
	}

	class CustomConfigurationsPreference extends edu.cmu.cs.dennisc.preference.ListPreference<org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration> {
		public CustomConfigurationsPreference() {
			super( new java.util.LinkedList<org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration>() );
		}
		@Override
		protected int getItemVersion() {
			return 1;
		}
		@Override
		protected org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration decode( int itemVersion, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
			if( itemVersion == 1 ) {
				return binaryDecoder.decodeBinaryEncodableAndDecodable(org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration.class);
			} else {
				return null;
			}
		}
		@Override
		protected void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, org.alice.ide.preferences.programming.BinaryEncodableAndDecodableConfiguration item ) {
			binaryEncoder.encode(item);
		}
	}
	public final CustomConfigurationsPreference listOfCustomProgrammingPreferencesPreference = new CustomConfigurationsPreference();
	public final edu.cmu.cs.dennisc.preference.UUIDPreference activePerspective = new edu.cmu.cs.dennisc.preference.UUIDPreference( org.alice.ide.preferences.programming.exposure.ExposureConfiguration.getSingleton().getUUID() );
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( true );
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isSyntaxNoiseDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( false );

	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 4;
		rv[ 0 ] = this.listOfCustomProgrammingPreferencesPreference;
		rv[ 1 ] = this.activePerspective;
		rv[ 2 ] = this.isDefaultFieldNameGenerationDesired;
		rv[ 3 ] = this.isSyntaxNoiseDesired;
		return rv;
	}
	
	private org.alice.ide.preferences.programming.Configuration[] builtInPreferenceNodes = new org.alice.ide.preferences.programming.Configuration[] {
			org.alice.ide.preferences.programming.exposure.ExposureConfiguration.getSingleton(),
			org.alice.ide.preferences.programming.preparation.PreparationConfiguration.getSingleton()	
	};
	public org.alice.ide.preferences.programming.Configuration[] getBuiltInPreferenceNodes() {
		return this.builtInPreferenceNodes;
	}
}

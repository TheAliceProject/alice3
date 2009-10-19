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
	
	public final edu.cmu.cs.dennisc.preference.UUIDPreference activePerspective = new edu.cmu.cs.dennisc.preference.UUIDPreference( org.alice.ide.preferences.programming.exposure.Configuration.getSingleton().getUUID() );
	//public final ListOfCustomProgrammingPreferencesPreference listOfCustomProgrammingPreferencesPreference = new ListOfCustomProgrammingPreferencesPreference();
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isDefaultFieldNameGenerationDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( true );
	public final transient edu.cmu.cs.dennisc.preference.BooleanPreference isSyntaxNoiseDesired = new edu.cmu.cs.dennisc.preference.BooleanPreference( false );

	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 3;
		rv[ 0 ] = this.activePerspective;
		rv[ 1 ] = this.isDefaultFieldNameGenerationDesired;
		rv[ 2 ] = this.isSyntaxNoiseDesired;
		return rv;
	}
	
	private org.alice.ide.preferences.programming.Configuration[] builtInPreferenceNodes = new org.alice.ide.preferences.programming.Configuration[] {
			org.alice.ide.preferences.programming.exposure.Configuration.getSingleton(),
			org.alice.ide.preferences.programming.preparation.Configuration.getSingleton()	
	};
	public org.alice.ide.preferences.programming.Configuration[] getBuiltInPreferenceNodes() {
		return this.builtInPreferenceNodes;
	}
}

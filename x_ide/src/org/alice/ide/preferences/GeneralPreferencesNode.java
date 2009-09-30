package org.alice.ide.preferences;

public class GeneralPreferencesNode extends PreferencesNode {
	private static GeneralPreferencesNode singleton;
	public static GeneralPreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new GeneralPreferencesNode();
		}
		return singleton;
	}
	
	public edu.cmu.cs.dennisc.preference.StringPreference locale = new edu.cmu.cs.dennisc.preference.StringPreference( "locale", java.util.Locale.getDefault().toString() );
	public edu.cmu.cs.dennisc.preference.IntPreference recentProjectCount = new edu.cmu.cs.dennisc.preference.IntPreference( "recentProjectCount", 4 );
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[] { locale, recentProjectCount };
	@Override
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		return this.preferences;
	}
}

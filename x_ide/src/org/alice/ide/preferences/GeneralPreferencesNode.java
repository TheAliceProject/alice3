package org.alice.ide.preferences;

public class GeneralPreferencesNode extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	private static GeneralPreferencesNode singleton;
	public static GeneralPreferencesNode getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new GeneralPreferencesNode();
			singleton.initialize();
		}
		return singleton;
	}
	
	public final edu.cmu.cs.dennisc.preference.StringPreference locale = new edu.cmu.cs.dennisc.preference.StringPreference( java.util.Locale.getDefault().toString() );
	public final edu.cmu.cs.dennisc.preference.IntPreference recentProjectCount = new edu.cmu.cs.dennisc.preference.IntPreference( 4 );
	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 2;
		rv[ 0 ] = this.locale;
		rv[ 1 ] = this.recentProjectCount;
		return rv;
	}
}

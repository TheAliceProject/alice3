package org.alice.ide.preferences;

public class GeneralPreferences extends edu.cmu.cs.dennisc.preference.CollectionOfPreferences {
	private static GeneralPreferences singleton;
	public static GeneralPreferences getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new GeneralPreferences();
			singleton.initialize();
		}
		return singleton;
	}
	
	public final LocalePreference locale = new LocalePreference( java.util.Locale.getDefault() );
	public final edu.cmu.cs.dennisc.preference.IntPreference recentProjectCount = new edu.cmu.cs.dennisc.preference.IntPreference( 4 );
	@Override
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder(edu.cmu.cs.dennisc.preference.Preference<?>[] rv) {
		assert rv.length == 2;
		rv[ 0 ] = this.locale;
		rv[ 1 ] = this.recentProjectCount;
		return rv;
	}
}

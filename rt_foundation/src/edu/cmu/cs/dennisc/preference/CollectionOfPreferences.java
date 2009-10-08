package edu.cmu.cs.dennisc.preference;

public abstract class CollectionOfPreferences {
	private java.util.prefs.Preferences utilPrefs;
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences;
	public void initialize() {
		assert this.preferences == null;
		assert this.utilPrefs == null;
		this.utilPrefs = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
		java.util.List< java.lang.reflect.Field > fields = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getPublicFinalFields( this.getClass(), Preference.class );
		this.preferences = new edu.cmu.cs.dennisc.preference.Preference<?>[ fields.size() ];
		int i = 0;
		for( java.lang.reflect.Field field : fields ) {
			this.preferences[ i ] = (Preference)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, this );
			this.preferences[ i ].initialize( this, field.getName() );
			i++;
		}
		this.setOrder( this.preferences );
	}
	public java.util.prefs.Preferences getUtilPrefs() {
		return this.utilPrefs;
	}
	protected edu.cmu.cs.dennisc.preference.Preference<?>[] setOrder( edu.cmu.cs.dennisc.preference.Preference<?>[] rv ) {
		return rv;
	}
	public final edu.cmu.cs.dennisc.preference.Preference<?>[] getPreferences() {
		assert this.preferences != null;
		return this.preferences;
	}
}

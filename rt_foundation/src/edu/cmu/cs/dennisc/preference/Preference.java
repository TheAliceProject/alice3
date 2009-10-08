package edu.cmu.cs.dennisc.preference;

public abstract class Preference< E > {
	private CollectionOfPreferences collectionOfPreferences;
	private String key;
	private E defaultValue;
	private java.util.List< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> > preferenceListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> >();
	public Preference( E defaultValue ) {
		this.defaultValue = defaultValue;
	}
	
	/*package private*/ void initialize( CollectionOfPreferences collectionOfPreferences, String key ) {
		this.collectionOfPreferences = collectionOfPreferences;
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	public E getDefaultValue() {
		return this.defaultValue;
	}
	protected abstract E getValue( java.util.prefs.Preferences utilPrefs, String key, E defaultValue );
	protected abstract void setAndCommitValue( java.util.prefs.Preferences utilPrefs, String key, E nextValue );
	public final E getValue() {
		java.util.prefs.Preferences utilPrefs = this.collectionOfPreferences.getUtilPrefs();
		return this.getValue( utilPrefs, this.key, this.defaultValue );
	}
	public final void setAndCommitValue( E nextValue ) {
		java.util.prefs.Preferences utilPrefs = this.collectionOfPreferences.getUtilPrefs();
		edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E> e;
		if( this.preferenceListeners != null ) {
			E prevValue = this.getValue( utilPrefs, this.key, this.defaultValue );
			e = new edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E>( this, prevValue, nextValue );
		} else {
			e = null;
		}
		if( e != null ) {
			this.firePropertyChanging(e);
		}
		this.setAndCommitValue(utilPrefs, this.key, nextValue);
		if( e != null ) {
			this.firePropertyChanged(e);
		}
	}

	public void addPropertyListener( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener ) {
		synchronized( this.preferenceListeners ) {
			this.preferenceListeners.add( propertyListener );
		}
	}
	public void removePropertyListener( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener ) {
		synchronized( this.preferenceListeners ) {
			this.preferenceListeners.remove( propertyListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> > getPropertyListeners() {
		return this.preferenceListeners;
	}

	private void firePropertyChanging( edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E> e ) {
		if( this.preferenceListeners != null ) {
			synchronized( this.preferenceListeners ) {
				for( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener : this.preferenceListeners ) {
					propertyListener.valueChanging( e );
				}
			}
		}
	}
	private void firePropertyChanged( edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E> e ) {
		if( this.preferenceListeners != null ) {
			synchronized( this.preferenceListeners ) {
				for( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener : this.preferenceListeners ) {
					propertyListener.valueChanged( e );
				}
			}
		}
	}
}

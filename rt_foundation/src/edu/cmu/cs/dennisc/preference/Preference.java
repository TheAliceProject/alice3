package edu.cmu.cs.dennisc.preference;

public abstract class Preference< E > {
	private String key;
	private E defaultValue;
	public Preference( String key, E defaultValue ) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	public String getKey() {
		return this.key;
	}
	public E getDefaultValue() {
		return this.defaultValue;
	}
	protected abstract E getValue( java.util.prefs.Preferences preferences, String key, E defaultValue );
	protected abstract void setAndCommitValue( java.util.prefs.Preferences preferences, String key, E nextValue );
	public final E getValue( java.util.prefs.Preferences preferences ) {
		return this.getValue( preferences, this.key, this.defaultValue );
	}
	public final void setAndCommitValue( java.util.prefs.Preferences preferences, E nextValue ) {
		edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E> e;
		if( this.preferenceListeners != null ) {
			E prevValue = this.getValue( preferences );
			e = new edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E>( this, prevValue, nextValue );
		} else {
			e = null;
		}
		if( e != null ) {
			this.firePropertyChanging(e);
		}
		this.setAndCommitValue(preferences, this.key, nextValue);
		if( e != null ) {
			this.firePropertyChanged(e);
		}
	}
	private java.util.List< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> > preferenceListeners = null;

	public void addPropertyListener( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener ) {
		if( this.preferenceListeners != null ) {
			//pass
		} else {
			this.preferenceListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> >();
		}
		synchronized( this.preferenceListeners ) {
			this.preferenceListeners.add( propertyListener );
		}
	}
	public void removePropertyListener( edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> propertyListener ) {
		assert this.preferenceListeners != null;
		synchronized( this.preferenceListeners ) {
			this.preferenceListeners.remove( propertyListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> > accessPropertyListeners() {
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

/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.preference;

/**
 * @author Dennis Cosgrove
 */
public abstract class Preference< E > {
	private CollectionOfPreferences collectionOfPreferences;
	private String key;
	private E defaultValue;
	private boolean isTransient;
	private E currentValueIfTransient;
	private java.util.List< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> > preferenceListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.preference.event.PreferenceListener<E> >();
	public Preference( E defaultValue ) {
		this.defaultValue = defaultValue;
	}
	
	/*package private*/ void initialize( CollectionOfPreferences collectionOfPreferences, String key, boolean isTransient ) {
		this.collectionOfPreferences = collectionOfPreferences;
		this.key = key;
		this.isTransient = isTransient;
		if( this.isTransient ) {
			this.currentValueIfTransient = this.defaultValue;
		}
	}
	
	public boolean isTransient() {
		return this.isTransient;
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
		if( this.isTransient ) {
			return this.currentValueIfTransient;
		} else {
			java.util.prefs.Preferences utilPrefs = this.collectionOfPreferences.getUtilPrefs();
			return this.getValue( utilPrefs, this.key, this.defaultValue );
		}
	}
	public final void setAndCommitValue( E nextValue ) {
		java.util.prefs.Preferences utilPrefs = this.collectionOfPreferences.getUtilPrefs();
		edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E> e;
		if( this.preferenceListeners != null ) {
			E prevValue;
			if( this.isTransient ) {
				prevValue = this.currentValueIfTransient;
			} else {
				prevValue = this.getValue( utilPrefs, this.key, this.defaultValue );
			}
			e = new edu.cmu.cs.dennisc.preference.event.PreferenceEvent<E>( this, prevValue, nextValue );
		} else {
			e = null;
		}
		if( e != null ) {
			this.firePropertyChanging(e);
		}
		if( this.isTransient ) {
			this.currentValueIfTransient = nextValue;
		} else {
			this.setAndCommitValue(utilPrefs, this.key, nextValue);
		}
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

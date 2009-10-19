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
			this.preferences[ i ].initialize( this, field.getName(), java.lang.reflect.Modifier.isTransient( field.getModifiers() ) );
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

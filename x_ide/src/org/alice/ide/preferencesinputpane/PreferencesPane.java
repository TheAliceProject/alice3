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
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
public class PreferencesPane extends edu.cmu.cs.dennisc.croquet.FormPane {
	private String title;
	private java.util.prefs.Preferences prefs;
	private java.util.List< edu.cmu.cs.dennisc.preference.Preference<?> > preferences;
	public PreferencesPane( String title, Class<?> clsWithinPackage, java.util.List< edu.cmu.cs.dennisc.preference.Preference<?> > preferences ) {
		this.title = title;
		this.prefs = java.util.prefs.Preferences.userNodeForPackage( clsWithinPackage );
		this.preferences = preferences;
	}
	public String getTitle() {
		return this.title;
	}
	@Override
	protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : preferences ) {
			String key = preference.getKey();
			java.awt.Component keyComponent = createLabel( key );
			java.awt.Component valueComponent = new javax.swing.JLabel( "todo: value" );
			rv.add( new java.awt.Component[] { keyComponent, valueComponent } );
		}
		return rv;
	}
}

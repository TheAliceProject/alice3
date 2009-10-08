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

abstract class PreferenceComponent< E > {
	private edu.cmu.cs.dennisc.preference.Preference<E> preference;
	public PreferenceComponent( edu.cmu.cs.dennisc.preference.Preference<E> preference ) {
		this.preference = preference;
	}
	public void commit() {
		
	}
}

/**
 * @author Dennis Cosgrove
 */
public class InnerPreferencesPane extends edu.cmu.cs.dennisc.croquet.PageAxisPane {
	private static java.awt.Component createUIFor( edu.cmu.cs.dennisc.preference.Preference<?> preference ) {
		java.awt.Component rv;
		if( preference instanceof edu.cmu.cs.dennisc.preference.BooleanPreference ) {
			edu.cmu.cs.dennisc.preference.BooleanPreference booleanPreference = (edu.cmu.cs.dennisc.preference.BooleanPreference)preference;
			javax.swing.JCheckBox checkbox = new javax.swing.JCheckBox( booleanPreference.getKey() );
			checkbox.setSelected( booleanPreference.getValue() );
			rv = checkbox;
		} else {
			edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( preference.getKey() + ": " );
			java.awt.Component editor;
			if( preference instanceof edu.cmu.cs.dennisc.preference.IntPreference ) {
				edu.cmu.cs.dennisc.preference.IntPreference intPreference = (edu.cmu.cs.dennisc.preference.IntPreference)preference;
				javax.swing.SpinnerNumberModel model = new javax.swing.SpinnerNumberModel( (int)intPreference.getValue(), 0, 16, 1 );
				javax.swing.JSpinner spinner = new javax.swing.JSpinner( model );
				editor = spinner;
			} else if( preference instanceof edu.cmu.cs.dennisc.preference.StringPreference ) {
				edu.cmu.cs.dennisc.preference.StringPreference stringPreference = (edu.cmu.cs.dennisc.preference.StringPreference)preference;
				javax.swing.JTextField textField = new javax.swing.JTextField();
				textField.setText( stringPreference.getValue() );
				editor = textField;
			} else {
				javax.swing.JTextField textField = new javax.swing.JTextField();
				textField.setText( "" + preference.getValue() );
				editor = textField;
			}
			rv = new edu.cmu.cs.dennisc.croquet.LineAxisPane( label, javax.swing.Box.createHorizontalStrut( 8 ), editor );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences;
	public InnerPreferencesPane( edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		assert collectionOfPreferences != null;
		this.collectionOfPreferences = collectionOfPreferences;
		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : this.collectionOfPreferences.getPreferences() ) {
			this.add( createUIFor( preference ) );
		}
		this.add( javax.swing.Box.createVerticalGlue() );
	}
	@Override
	public String toString() {
		return this.collectionOfPreferences.getUtilPrefs().name();
	}
}

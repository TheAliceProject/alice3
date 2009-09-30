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
class TitlePane extends edu.cmu.cs.dennisc.croquet.PageAxisPane {
	public TitlePane( String title ) {
		edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( title, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD );
		label.setFontToScaledFont( 2.0f );
		this.add( label );
		this.add( new javax.swing.JSeparator( javax.swing.SwingConstants.HORIZONTAL ) );
	}
}
/**
 * @author Dennis Cosgrove
 */
public class PreferencesPane extends edu.cmu.cs.dennisc.croquet.BorderPane {
	private String title;
	private java.util.prefs.Preferences prefs;
	private edu.cmu.cs.dennisc.preference.Preference<?>[] preferences;
	
	private java.awt.Component createUIFor( edu.cmu.cs.dennisc.preference.Preference<?> preference ) {
		java.awt.Component rv;
		if( preference instanceof edu.cmu.cs.dennisc.preference.BooleanPreference ) {
			edu.cmu.cs.dennisc.preference.BooleanPreference booleanPreference = (edu.cmu.cs.dennisc.preference.BooleanPreference)preference;
			javax.swing.JCheckBox checkbox = new javax.swing.JCheckBox( booleanPreference.getKey() );
			checkbox.setSelected( booleanPreference.getValue(this.prefs) );
			rv = checkbox;
		} else if( preference instanceof edu.cmu.cs.dennisc.preference.IntPreference ) {
			edu.cmu.cs.dennisc.preference.IntPreference intPreference = (edu.cmu.cs.dennisc.preference.IntPreference)preference;
			edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( preference.getKey() + ": " );
			javax.swing.SpinnerNumberModel model = new javax.swing.SpinnerNumberModel( (int)intPreference.getValue( this.prefs ), 0, 16, 1 );
			javax.swing.JSpinner spinner = new javax.swing.JSpinner( model );
			rv = new edu.cmu.cs.dennisc.croquet.LineAxisPane( label, javax.swing.Box.createHorizontalStrut( 8 ), spinner );
		} else {
			rv = new javax.swing.JLabel( preference.getKey() );
		}
		return rv;
	}
	private PreferencesPane( String title, Class<?> clsWithinPackage, edu.cmu.cs.dennisc.preference.Preference<?>[] preferences ) {
		this.title = title;
		this.prefs = java.util.prefs.Preferences.userNodeForPackage( clsWithinPackage );
		this.preferences = preferences;
		
		edu.cmu.cs.dennisc.zoot.ZLabel titleComponent = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( this.title, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD );
		titleComponent.setFontToScaledFont( 2.0f );

		edu.cmu.cs.dennisc.croquet.PageAxisPane preferencesPane = new edu.cmu.cs.dennisc.croquet.PageAxisPane();
		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : preferences ) {
			preferencesPane.add( createUIFor( preference ) );
		}
		preferencesPane.add( javax.swing.Box.createVerticalGlue() );
		
		edu.cmu.cs.dennisc.croquet.LineAxisPane buttonsPane = new edu.cmu.cs.dennisc.croquet.LineAxisPane(
				javax.swing.Box.createHorizontalGlue(),
				new javax.swing.JButton( "Restore Defaults" ),
				javax.swing.Box.createHorizontalStrut( 4 ),
				new javax.swing.JButton( "Apply" )
		);

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( preferencesPane );
		scrollPane.setBorder( null );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.add( new TitlePane( this.title ), java.awt.BorderLayout.NORTH );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
		this.add( buttonsPane, java.awt.BorderLayout.SOUTH );
	}
	public PreferencesPane( String title, org.alice.ide.preferences.PreferencesNode preferencesNode ) {
		this( title, preferencesNode.getClass(), preferencesNode.getPreferences() );
	}
	public String getTitle() {
		return this.title;
	}
//	@Override
//	protected java.util.List<java.awt.Component[]> addComponentRows(java.util.List<java.awt.Component[]> rv) {
//		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : preferences ) {
//			String key = preference.getKey();
//			java.awt.Component keyComponent = createLabel( key );
//			java.awt.Component valueComponent = new javax.swing.JLabel( "todo: value" );
//			rv.add( new java.awt.Component[] { keyComponent, valueComponent } );
//		}
//		return rv;
//	}
}

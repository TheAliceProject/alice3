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
public class OuterPreferencesPane extends edu.cmu.cs.dennisc.croquet.BorderPane {
	private String title;
	
	private OuterPreferencesPane( String title, Class<?> clsWithinPackage, edu.cmu.cs.dennisc.preference.Preference<?>[] preferences ) {
		this.title = title;
		
		edu.cmu.cs.dennisc.zoot.ZLabel titleComponent = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( this.title, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD );
		titleComponent.setFontToScaledFont( 2.0f );

		InnerPreferencesPane innerPreferencesPane = this.createInnerPreferencesPane(clsWithinPackage, preferences);
		
		edu.cmu.cs.dennisc.croquet.LineAxisPane buttonsPane = new edu.cmu.cs.dennisc.croquet.LineAxisPane(
				javax.swing.Box.createHorizontalGlue(),
				new javax.swing.JButton( "Restore Defaults" ),
				javax.swing.Box.createHorizontalStrut( 4 ),
				new javax.swing.JButton( "Apply" )
		);

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( innerPreferencesPane );
		scrollPane.setBorder( null );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.add( new TitlePane( this.title ), java.awt.BorderLayout.NORTH );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
		this.add( buttonsPane, java.awt.BorderLayout.SOUTH );
	}
	protected InnerPreferencesPane createInnerPreferencesPane( Class<?> clsWithinPackage, edu.cmu.cs.dennisc.preference.Preference<?>[] preferences ) {
		return new InnerPreferencesPane( clsWithinPackage, preferences );
	}
	public OuterPreferencesPane( String title, org.alice.ide.preferences.PreferencesNode preferencesNode ) {
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

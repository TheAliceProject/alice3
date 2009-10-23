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
class TitlePane extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane {
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
public class CollectionOfPreferencesPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	class RestoreDefaultsActionOperation extends org.alice.ide.operations.AbstractActionOperation {
		private boolean isAll;
		public RestoreDefaultsActionOperation() {
			this.putValue( javax.swing.Action.NAME, "Restore Defaults" );
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
			String name = CollectionOfPreferencesPane.this.getTitle();
			
			java.awt.Component parentComponent = this.getIDE();
			String message = "To what extent would you like to restore defaults?";
			String title = "Restore Defaults";
			int optionType = javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
			int messageType = javax.swing.JOptionPane.QUESTION_MESSAGE;
			javax.swing.Icon icon = null;
			Object[] options = { "Only " + name + " Preferences", "All Preferences", "Cancel" };
			Object initialValue = null; //options[ 0 ];
			int result = javax.swing.JOptionPane.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue );
			switch( result ) {
			case javax.swing.JOptionPane.YES_OPTION:
			case javax.swing.JOptionPane.NO_OPTION:
				this.isAll = result == javax.swing.JOptionPane.NO_OPTION;
				actionContext.commitAndInvokeRedoIfAppropriate();
				break;
			default:
				actionContext.cancel();
			}
		}
		@Override
		public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		}
		@Override
		public boolean canUndo() {
			//todo
			return false;
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			throw new javax.swing.undo.CannotUndoException();
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	class ApplyActionOperation extends org.alice.ide.operations.AbstractActionOperation {
		public ApplyActionOperation() {
			this.putValue( javax.swing.Action.NAME, "Apply" );
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	private String title;
	private RestoreDefaultsActionOperation restoreDefaultsActionOperation = new RestoreDefaultsActionOperation();
	private ApplyActionOperation applyActionOperation = new ApplyActionOperation();
	
	public CollectionOfPreferencesPane( String title, edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		assert collectionOfPreferences != null;
		this.title = title;
		
		edu.cmu.cs.dennisc.zoot.ZLabel titleComponent = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( this.title, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD );
		titleComponent.setFontToScaledFont( 2.0f );

		edu.cmu.cs.dennisc.croquet.swing.PageAxisPane centerComponent = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane();
		this.updateCenterComponent(centerComponent, collectionOfPreferences);
		centerComponent.add( javax.swing.Box.createVerticalGlue() );

		edu.cmu.cs.dennisc.croquet.swing.LineAxisPane buttonsPane = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane(
				javax.swing.Box.createHorizontalGlue(),
				new edu.cmu.cs.dennisc.zoot.ZButton( this.restoreDefaultsActionOperation ),
				javax.swing.Box.createHorizontalStrut( 4 ),
				new edu.cmu.cs.dennisc.zoot.ZButton( this.applyActionOperation )
		);

		javax.swing.JScrollPane scrollPane = wrapInScrollPane( centerComponent );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.add( new TitlePane( this.title ), java.awt.BorderLayout.NORTH );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
		this.add( buttonsPane, java.awt.BorderLayout.SOUTH );
	}
	public String getTitle() {
		return this.title;
	}
	protected static javax.swing.JScrollPane wrapInScrollPane( java.awt.Component component ) {
		javax.swing.JScrollPane rv = new javax.swing.JScrollPane( component );
		rv.setBorder( null );
		return rv;
	}

	protected PreferenceProxy createDefaultProxyFor( edu.cmu.cs.dennisc.preference.Preference preference ) {
		if( preference instanceof edu.cmu.cs.dennisc.preference.BooleanPreference ) {
			return new BooleanPreferenceCheckBoxProxy(preference);
		} else if( preference instanceof org.alice.ide.preferences.LocalePreference ) {
			return new LocalePreferenceComboBoxProxy( preference );
		} else {
			return new UnknownPreferenceProxy(preference);
		}
	}
	
	protected void updateCenterComponent( edu.cmu.cs.dennisc.croquet.swing.PageAxisPane centerComponent, edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : collectionOfPreferences.getPreferences() ) {
			if( preference.isTransient() ) {
				//pass
			} else {
				PreferenceProxy<?> proxy = this.createDefaultProxyFor(preference);
				if( proxy != null ) {
					centerComponent.add( proxy.getAWTComponent() );
					centerComponent.add( javax.swing.Box.createVerticalStrut( 4 ) );
				}
			}
		}
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

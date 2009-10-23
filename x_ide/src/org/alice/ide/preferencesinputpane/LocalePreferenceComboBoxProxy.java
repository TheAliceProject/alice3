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
public class LocalePreferenceComboBoxProxy extends PreferenceLabeledPaneProxy< java.util.Locale > {
	class LocaleSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< java.util.Locale > {
		public LocaleSelectionOperation() {
			super( new javax.swing.DefaultComboBoxModel( edu.cmu.cs.dennisc.util.LocaleUtilities.alphabetizeByDisplayName( java.util.Locale.getAvailableLocales() ) ) );
			this.getComboBoxModel().setSelectedItem( java.util.Locale.getDefault() );
		}

		@Override
		protected void handleSelectionChange( java.util.Locale value ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( value );
		}

		@Override
		public boolean isSignificant() {
			// todo?
			return false;
		}
	}

	private LocaleSelectionOperation operation;
	private edu.cmu.cs.dennisc.zoot.ZComboBox<java.util.Locale> comboBox;
	public LocalePreferenceComboBoxProxy( edu.cmu.cs.dennisc.preference.Preference< java.util.Locale > preference ) {
		super( preference );
		this.operation = new LocaleSelectionOperation();
		this.comboBox = new edu.cmu.cs.dennisc.zoot.ZComboBox<java.util.Locale>( new LocaleSelectionOperation() );
		this.comboBox.setRenderer( new edu.cmu.cs.dennisc.croquet.util.LocaleDisplayNameListCellRenderer() );
		this.createPane( this.comboBox );
	}

	public void setAndCommitValue() {
		this.getPreference().setAndCommitValue( this.comboBox.getLocale() );
	}
}

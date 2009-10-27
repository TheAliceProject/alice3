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
public class BooleanPreferenceCheckBoxProxy extends PreferenceProxy<Boolean> {
	class BooleanStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public BooleanStateOperation() {
			super( BooleanPreferenceCheckBoxProxy.this.getPreference().getValue() );
			this.putValue( javax.swing.Action.NAME, BooleanPreferenceCheckBoxProxy.this.getPreference().getKey() );
		}
		@Override
		protected void handleStateChange(boolean value) {
			
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
		
	}
	private BooleanStateOperation operation;
	private edu.cmu.cs.dennisc.zoot.ZCheckBox checkBox;
	public BooleanPreferenceCheckBoxProxy( edu.cmu.cs.dennisc.preference.Preference<Boolean> preference ) {
		super( preference );
		this.operation = new BooleanStateOperation();
		this.checkBox = new edu.cmu.cs.dennisc.zoot.ZCheckBox(this.operation);
	}
	@Override
	public java.awt.Component getAWTComponent() {
		return this.checkBox;
	}
	@Override
	public void setAndCommitValue() {
		this.getPreference().setAndCommitValue( this.checkBox.isSelected() );
	}
}

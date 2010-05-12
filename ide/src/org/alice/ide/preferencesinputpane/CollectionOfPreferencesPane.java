/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
class TitlePane extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	public TitlePane( String title ) {
		this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( title, 2.0f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ) );
		this.addComponent( new edu.cmu.cs.dennisc.croquet.HorizontalSeparator() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CollectionOfPreferencesPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	class RestoreDefaultsActionOperation extends org.alice.ide.operations.ActionOperation {
		private boolean isAll;
		public RestoreDefaultsActionOperation() {
			super( org.alice.ide.IDE.PREFERENCES_GROUP, java.util.UUID.fromString( "47826a1a-383e-4628-acc9-d07dcc2e55ac" ) );
			this.setName( "Restore Defaults" );
		}
		@Override
		protected final void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component ) {
			String name = CollectionOfPreferencesPane.this.getTitle();
			
			java.awt.Component parentComponent = org.alice.ide.IDE.getSingleton().getJFrame();
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
				context.todo();
				break;
			default:
				context.cancel();
			}
		}
	}

	class ApplyActionOperation extends org.alice.ide.operations.ActionOperation {
		public ApplyActionOperation() {
			super( org.alice.ide.IDE.PREFERENCES_GROUP, java.util.UUID.fromString( "e83b1957-5d47-492a-ab7b-8bfeaef148af" ) );
			this.setName( "Apply" );
		}
		@Override
		protected final void perform( edu.cmu.cs.dennisc.croquet.Context context, java.util.EventObject e, edu.cmu.cs.dennisc.croquet.Component<?> component ) {
			//todo
		}
	}

	private String title;
	private RestoreDefaultsActionOperation restoreDefaultsActionOperation = new RestoreDefaultsActionOperation();
	private ApplyActionOperation applyActionOperation = new ApplyActionOperation();
	
	public CollectionOfPreferencesPane( String title, edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		assert collectionOfPreferences != null;
		this.title = title;
		
		//javax.swing.JLabel titleComponent = edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabelWithScaledFont( this.title, 2.0f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		
		
		edu.cmu.cs.dennisc.croquet.PageAxisPanel centerComponent = new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
		this.updateCenterComponent(centerComponent, collectionOfPreferences);
		centerComponent.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalGlue() );

		edu.cmu.cs.dennisc.croquet.LineAxisPanel buttonsPane = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue(),
				this.restoreDefaultsActionOperation.createButton(),
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 4 ),
				this.applyActionOperation.createButton()
		);

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.addComponent( new TitlePane( this.title ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		this.addComponent( wrapInScrollPane( centerComponent ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		this.addComponent( buttonsPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.SOUTH );
	}
	public String getTitle() {
		return this.title;
	}
	protected static edu.cmu.cs.dennisc.croquet.ScrollPane wrapInScrollPane( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		edu.cmu.cs.dennisc.croquet.ScrollPane rv = new edu.cmu.cs.dennisc.croquet.ScrollPane( component );
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
	
	protected void updateCenterComponent( edu.cmu.cs.dennisc.croquet.PageAxisPanel centerComponent, edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
		for( edu.cmu.cs.dennisc.preference.Preference<?> preference : collectionOfPreferences.getPreferences() ) {
			if( preference.isTransient() ) {
				//pass
			} else {
				PreferenceProxy<?> proxy = this.createDefaultProxyFor(preference);
				if( proxy != null ) {
					centerComponent.addComponent( proxy.getComponent() );
					centerComponent.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 4 ) );
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

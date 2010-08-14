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
package org.alice.ide.croquet.models.menubar;

/**
 * @author Dennis Cosgrove
 */
public class WindowMenuModel extends edu.cmu.cs.dennisc.croquet.DefaultMenuModel {
	private static class SingletonHolder {
		private static WindowMenuModel instance = new WindowMenuModel();
	}
	public static WindowMenuModel getInstance() {
		return SingletonHolder.instance;
	}
	
	private static java.util.List<edu.cmu.cs.dennisc.croquet.Model> getModels() {
		java.util.List<edu.cmu.cs.dennisc.croquet.Model> rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		rv.add( org.alice.ide.croquet.models.history.IsProjectHistoryShowingState.getInstance() );
		//rv.add( org.alice.ide.croquet.models.history.IsUIHistoryShowingState.getInstance() );
		rv.add( org.alice.ide.croquet.models.ui.IsMemoryUsageShowingState.getInstance() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( org.alice.ide.IDE.DEBUG_PROPERTY_KEY ) ) {
			rv.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
			rv.add( InternalTestingMenuModel.getInstance() );
		}
		rv.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
		rv.add( PreferencesMenuModel.getInstance() );
		return rv;
	}
	private WindowMenuModel() {
		super( java.util.UUID.fromString( "58a7297b-a5f8-499a-abd1-db6fca4083c8" ), getModels() );
	}
}
//	private java.util.List< edu.cmu.cs.dennisc.croquet.BooleanState > booleanStatePreferences = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//	private edu.cmu.cs.dennisc.croquet.BooleanState createBooleanStatePreference( java.util.UUID id, boolean defaultInitialValue, String name ) {
//		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
//		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
//			try {
//				userPreferences.clear();
//			} catch( java.util.prefs.BackingStoreException bse ) {
//				throw new RuntimeException( bse );
//			}
//		}
//		Boolean initialValue = userPreferences.getBoolean( id.toString(), defaultInitialValue );
//		edu.cmu.cs.dennisc.croquet.BooleanState rv = new edu.cmu.cs.dennisc.croquet.BooleanState( PREFERENCES_GROUP, id, initialValue, name );
//		booleanStatePreferences.add( rv );
//		return rv;
//	}
//	private void preservePreferences() {
//		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
//		for( edu.cmu.cs.dennisc.croquet.BooleanState booleanState : booleanStatePreferences ) {
//			userPreferences.putBoolean( booleanState.getIndividualUUID().toString(), booleanState.getValue() );
//		}
//	}
//	private edu.cmu.cs.dennisc.croquet.BooleanState isExpressionTypeFeedbackDesiredState =
//		createBooleanStatePreference( java.util.UUID.fromString( "e80adbfe-9e1a-408f-8067-ddbd30d0ffb9" ), true, "Is Type Feedback Desired" );
//	private edu.cmu.cs.dennisc.croquet.BooleanState isInactiveFeedbackState =
//		createBooleanStatePreference( java.util.UUID.fromString( "2645a33c-3a37-41c1-83fe-521ed8dd0382" ), true, "Is Inactive Feedback Desired" );
//	private edu.cmu.cs.dennisc.croquet.BooleanState isOmissionOfThisForFieldAccessesDesiredState =
//		createBooleanStatePreference( java.util.UUID.fromString( "bcf1ce48-f54a-4e80-8b9e-42c2cc302b01" ), false, "Is Omission Of This For Field Accesses Desired" );
//	private edu.cmu.cs.dennisc.croquet.BooleanState isEmphasizingClassesState =
//		createBooleanStatePreference( java.util.UUID.fromString( "c6d27bf1-f8c0-470d-b9ef-3c9fa7e6f4b0" ), true, "Is Emphasizing Classes" );
//	private edu.cmu.cs.dennisc.croquet.BooleanState isDefaultFieldNameGenerationDesiredState =
//		createBooleanStatePreference( java.util.UUID.fromString( "3e551420-bb50-4e33-9175-9f29738998f0" ), false, "Is Default Field Name Generation Desired" );
//	
////	@Deprecated
////	public edu.cmu.cs.dennisc.croquet.BooleanState getIsSceneEditorExpandedState() {
////		return org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
////	}
//	private edu.cmu.cs.dennisc.croquet.BooleanState isRecursionEnabledState =
//		createBooleanStatePreference( java.util.UUID.fromString( "a5e1ded2-18c7-4ae5-8676-e6deca5650fe" ), false, "Is Recursion Enabled" );
//	
//	public edu.cmu.cs.dennisc.croquet.BooleanState getExpressionTypeFeedbackDesiredState() {
//		return this.isExpressionTypeFeedbackDesiredState;
//	}
//	public edu.cmu.cs.dennisc.croquet.BooleanState getEmphasizingClassesState() {
//		return this.isEmphasizingClassesState;
//	}
//	public boolean isInactiveFeedbackDesired() {
//		return this.isInactiveFeedbackState.getValue();
//	}
//	@Deprecated
//	public boolean isEmphasizingClasses() {
//		return this.isEmphasizingClassesState.getValue();
//	}
//	public edu.cmu.cs.dennisc.croquet.BooleanState getOmissionOfThisForFieldAccessesDesiredState() {
//		return this.isOmissionOfThisForFieldAccessesDesiredState;
//	}
//	public boolean isDefaultFieldNameGenerationDesired() {
//		return this.isDefaultFieldNameGenerationDesiredState.getValue();
//	}
//
//	private WindowMenuModel windowMenuModel = new WindowMenuModel( 
//			org.alice.ide.croquet.models.ui.ProgrammingLanguageSelectionState.getInstance(),
//			edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
//			this.isEmphasizingClassesState,
//			this.isInactiveFeedbackState,
//			this.isOmissionOfThisForFieldAccessesDesiredState,
//			this.isExpressionTypeFeedbackDesiredState,
//			this.isDefaultFieldNameGenerationDesiredState,
//			this.isRecursionEnabledState,
//			edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
//			org.alice.ide.croquet.models.ui.window.IsProjectHistoryShowingState.getInstance(),
//			org.alice.ide.croquet.models.ui.window.IsMemoryUsageShowingState.getInstance(),
//			edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
//			org.alice.ide.croquet.models.ui.window.IsHistoryTreeShowingState.getInstance()
//			//			windowOperations.add( this.isEmphasizingClassesOperation );
//			//			windowOperations.add( this.isOmissionOfThisForFieldAccessesDesiredOperation );
//			//			windowOperations.add( this.isExpressionTypeFeedbackDesiredOperation );
//			//			windowOperations.add( this.isDefaultFieldNameGenerationDesiredOperation );
//
//	//			class LocaleComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
//	//				private java.util.Locale[] candidates = { new java.util.Locale( "en", "US" ), new java.util.Locale( "en", "US", "java" ) };
//	//				private int selectedIndex;
//	//
//	//				public LocaleComboBoxModel() {
//	//					this.selectedIndex = 0;
//	//				}
//	//				public Object getElementAt( int index ) {
//	//					return this.candidates[ index ];
//	//				}
//	//				public int getSize() {
//	//					return this.candidates.length;
//	//				}
//	//				public Object getSelectedItem() {
//	//					if( 0 <= this.selectedIndex && this.selectedIndex < this.candidates.length ) {
//	//						return this.candidates[ this.selectedIndex ];
//	//					} else {
//	//						return null;
//	//					}
//	//				}
//	//				public void setSelectedItem( Object selectedItem ) {
//	//					int index = -1;
//	//					if( selectedItem != null ) {
//	//						int i = 0;
//	//						for( java.util.Locale locale : this.candidates ) {
//	//							if( selectedItem.equals( locale ) ) {
//	//								index = i;
//	//								break;
//	//							}
//	//							i++;
//	//						}
//	//					}
//	//					this.selectedIndex = index;
//	//				}
//	//			}
//	//
//	//			class LocaleItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< java.util.Locale > {
//	//				public LocaleItemSelectionOperation() {
//	//					super( new LocaleComboBoxModel() );
//	//				}
//	//				@Override
//	//				protected String getNameFor( int index, java.util.Locale locale ) {
//	//					if( locale != null ) {
//	//						String variant = locale.getVariant();
//	//						if( variant != null && variant.length() > 0 ) { //should not be null
//	//							return variant;
//	//						} else {
//	//							return "alice";
//	//						}
//	//					} else {
//	//						return "null";
//	//					}
//	//				}
//	//				@Override
//	//				protected void handleSelectionChange( java.util.Locale value ) {
//	//					IDE.this.setLocale( value );
//	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: support undo", this );
//	//				}
//	//			}
//	//
//	//			java.util.List< edu.cmu.cs.dennisc.zoot.Operation > windowOperations = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();
//	//
//	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//	//			windowOperations.add( this.isHistoryShowingOperation );
//	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//	//			windowOperations.add( this.isEmphasizingClassesOperation );
//	//			windowOperations.add( this.isOmissionOfThisForFieldAccessesDesiredOperation );
//	//			windowOperations.add( this.isExpressionTypeFeedbackDesiredOperation );
//	//			windowOperations.add( this.isDefaultFieldNameGenerationDesiredOperation );
//	//
//	//			//		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
//	//			//			//pass
//	//			//		} else {
//	//			//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//	//			//			windowOperations.add( this.getPreferencesOperation() );
//	//			//		}
//	//
//	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//	//			windowOperations.add( this.isMemoryUsageShowingOperation );
//	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//	//			windowOperations.add( this.isSceneEditorExpandedOperation );
//	);

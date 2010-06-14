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
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends org.alice.app.ProjectApplication {
	public static final edu.cmu.cs.dennisc.croquet.Group PREFERENCES_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "c090cda0-4a77-4e2c-a839-faf28c98c10c" ), "PREFERENCES_GROUP" );
	public static final edu.cmu.cs.dennisc.croquet.Group RUN_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "f7a87645-567c-42c6-bf5f-ab218d93a226" ), "RUN_GROUP" );

	public static final String DEBUG_PROPERTY_KEY = "org.alice.ide.DebugMode";

	private static org.alice.ide.issue.ExceptionHandler exceptionHandler;
	private static IDE singleton;
	private static java.util.HashSet< String > performSceneEditorGeneratedSetUpMethodNameSet = new java.util.HashSet< String >();

	public static final String GENERATED_SET_UP_METHOD_NAME = "performGeneratedSetUp";
	public static final String EDITOR_GENERATED_SET_UP_METHOD_NAME = "performEditorGeneratedSetUp";
	public static final String SCENE_EDITOR_GENERATED_SET_UP_METHOD_NAME = "performSceneEditorGeneratedSetUp";
	static {
		IDE.exceptionHandler = new org.alice.ide.issue.ExceptionHandler();

		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.IDE.isSupressionOfExceptionHandlerDesired" ) ) {
			//pass
		} else {
			Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
		}
		performSceneEditorGeneratedSetUpMethodNameSet.add( SCENE_EDITOR_GENERATED_SET_UP_METHOD_NAME );
		performSceneEditorGeneratedSetUpMethodNameSet.add( EDITOR_GENERATED_SET_UP_METHOD_NAME );
//		performSceneEditorGeneratedSetUpMethodNameSet.add( "performSceneEditorGeneratedSetUp" );
//		performSceneEditorGeneratedSetUpMethodNameSet.add( "performEditorGeneratedSetUp" );
		performSceneEditorGeneratedSetUpMethodNameSet.add( GENERATED_SET_UP_METHOD_NAME );
	}

	public static IDE getSingleton() {
		return IDE.singleton;
	}

	protected abstract edu.cmu.cs.dennisc.croquet.DialogOperation createRunOperation();
	protected abstract edu.cmu.cs.dennisc.croquet.Operation< ?,? > createRestartOperation();
	public abstract edu.cmu.cs.dennisc.croquet.Operation< ?,? > createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate );

	private edu.cmu.cs.dennisc.croquet.Operation< ?,? > preferencesOperation = this.createPreferencesOperation();
	private edu.cmu.cs.dennisc.croquet.Operation< ?,? > aboutOperation = this.createAboutOperation();
	private edu.cmu.cs.dennisc.croquet.DialogOperation runOperation = this.createRunOperation();
	private edu.cmu.cs.dennisc.croquet.Operation< ?,? > restartOperation = this.createRestartOperation();

	protected edu.cmu.cs.dennisc.croquet.Operation< ?,? > createPreferencesOperation() {
		return new org.alice.ide.operations.preferences.PreferencesOperation();
	}
	protected abstract edu.cmu.cs.dennisc.croquet.Operation< ?,? > createAboutOperation();

	public final edu.cmu.cs.dennisc.croquet.DialogOperation getRunOperation() {
		return this.runOperation;
	}
	public final edu.cmu.cs.dennisc.croquet.Operation< ?,? > getRestartOperation() {
		return this.restartOperation;
	}
	public final edu.cmu.cs.dennisc.croquet.Operation< ?,? > getPreferencesOperation() {
		return this.preferencesOperation;
	}
	public final edu.cmu.cs.dennisc.croquet.Operation< ?,? > getAboutOperation() {
		return this.aboutOperation;
	}

	private static class FileMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public FileMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "121c8088-7297-43d4-b7b7-61416f1d4eb0" ), "File", java.awt.event.KeyEvent.VK_F, operations );
		}
	}

	private static class RecentProjectsMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public RecentProjectsMenuModel() {
			super( IDE_GROUP, java.util.UUID.fromString( "f94dda45-71e1-48df-9291-a8681b08f1c0" ), "Recent Projects", java.awt.event.KeyEvent.VK_R );
		}
	}

	private static class EditMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public EditMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "dbfe00f8-a401-4858-be5c-a544cad7c938" ), "Edit", java.awt.event.KeyEvent.VK_E, operations );
		}
	}

	private static class ProjectMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public ProjectMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "f154f9a2-4ba1-4adb-9cb1-fb6cd36841c4" ), "Project", java.awt.event.KeyEvent.VK_P, operations );
		}
	}

	private static class RunMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public RunMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "e441d150-d53b-4bc1-9dbf-a61843a53a34" ), "Run", java.awt.event.KeyEvent.VK_R, operations );
		}
	}

	private static class WindowMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public WindowMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "58a7297b-a5f8-499a-abd1-db6fca4083c8" ), "Window", java.awt.event.KeyEvent.VK_W, operations );
		}
	}

	private static class HelpMenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public HelpMenuModel( edu.cmu.cs.dennisc.croquet.Model... operations ) {
			super( IDE_GROUP, java.util.UUID.fromString( "435770a7-fb94-49ee-8c4d-b55a80618a09" ), "Help", java.awt.event.KeyEvent.VK_H, operations );
		}
	}

	private FileMenuModel fileMenuModel = new FileMenuModel( this.getNewProjectOperation(), this.getOpenProjectOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR, new RecentProjectsMenuModel(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR, this
			.getSaveProjectOperation(), this.getSaveAsProjectOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR, this.getRevertProjectOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
			new org.alice.ide.operations.file.ExportVideoUploadToYouTubeOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR, this.getExitOperation() );
	private EditMenuModel editMenuModel = new EditMenuModel( this.getUndoOperation(), this.getRedoOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR, new org.alice.ide.operations.edit.CutOperation(),
			new org.alice.ide.operations.edit.CopyOperation(), new org.alice.ide.operations.edit.PasteOperation() );

	private ProjectMenuModel projectMenuModel = new ProjectMenuModel( new org.alice.ide.operations.project.ManageResourcesOperation() );
	private RunMenuModel runMenuModel = new RunMenuModel( this.runOperation );

	private edu.cmu.cs.dennisc.croquet.BooleanState isSceneEditorExpandedState = new edu.cmu.cs.dennisc.croquet.BooleanState( org.alice.app.ProjectApplication.IDE_GROUP, java.util.UUID.fromString( "704ea7d2-9da8-461f-b7dd-d086815c3e52" ), false,
			"Edit Code", "Edit Scene" );

	private org.alice.ide.operations.window.IsMemoryUsageShowingOperation isMemoryUsageShowingOperation = new org.alice.ide.operations.window.IsMemoryUsageShowingOperation();
	private org.alice.ide.operations.window.IsHistoryShowingOperation isHistoryShowingOperation = new org.alice.ide.operations.window.IsHistoryShowingOperation();

//	private static <E> E createBooleanOperation( Class< E > cls, Boolean defaultInitialValue ) {
//		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
//		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
//			try {
//				userPreferences.clear();
//			} catch( java.util.prefs.BackingStoreException bse ) {
//				throw new RuntimeException( bse );
//			}
//		}
//		Boolean initialValue = userPreferences.getBoolean( cls.getSimpleName(), defaultInitialValue );
//		Class< ? >[] parameterClses = { Boolean.class };
//		Object[] arguments = { initialValue };
//		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls, parameterClses, arguments );
//	}
//	private static void preservePreference( edu.cmu.cs.dennisc.croquet.BooleanState operation ) {
//		if( operation != null ) {
//			Class< ? > cls = operation.getClass();
//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
//			userPreferences.putBoolean( cls.getSimpleName(), operation.getState() );
//		}
//	}
	private java.util.List< edu.cmu.cs.dennisc.croquet.BooleanState > booleanStatePreferences = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private edu.cmu.cs.dennisc.croquet.BooleanState createBooleanStatePreference( java.util.UUID id, boolean defaultInitialValue, String name ) {
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
			try {
				userPreferences.clear();
			} catch( java.util.prefs.BackingStoreException bse ) {
				throw new RuntimeException( bse );
			}
		}
		Boolean initialValue = userPreferences.getBoolean( id.toString(), defaultInitialValue );
		edu.cmu.cs.dennisc.croquet.BooleanState rv = new edu.cmu.cs.dennisc.croquet.BooleanState( PREFERENCES_GROUP, id, initialValue, name );
		booleanStatePreferences.add( rv );
		return rv;
	}
	private void preservePreferences() {
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( this.getClass() );
		for( edu.cmu.cs.dennisc.croquet.BooleanState booleanState : booleanStatePreferences ) {
			userPreferences.putBoolean( booleanState.getIndividualUUID().toString(), booleanState.getValue() );
		}
	}
	private edu.cmu.cs.dennisc.croquet.BooleanState isExpressionTypeFeedbackDesiredState =
		createBooleanStatePreference( java.util.UUID.fromString( "e80adbfe-9e1a-408f-8067-ddbd30d0ffb9" ), true, "Is Type Feedback Desired" );
	private edu.cmu.cs.dennisc.croquet.BooleanState isOmissionOfThisForFieldAccessesDesiredState =
		createBooleanStatePreference( java.util.UUID.fromString( "bcf1ce48-f54a-4e80-8b9e-42c2cc302b01" ), false, "Is Omission Of This For Field Accesses Desired" );
	private edu.cmu.cs.dennisc.croquet.BooleanState isEmphasizingClassesOperation =
		createBooleanStatePreference( java.util.UUID.fromString( "c6d27bf1-f8c0-470d-b9ef-3c9fa7e6f4b0" ), true, "Is Emphasizing Classes" );
	private edu.cmu.cs.dennisc.croquet.BooleanState isDefaultFieldNameGenerationDesiredState =
		createBooleanStatePreference( java.util.UUID.fromString( "3e551420-bb50-4e33-9175-9f29738998f0" ), false, "Is Default Field Name Generation Desired" );
	public edu.cmu.cs.dennisc.croquet.BooleanState getIsSceneEditorExpandedState() {
		return this.isSceneEditorExpandedState;
	}
	
	public edu.cmu.cs.dennisc.croquet.BooleanState getExpressionTypeFeedbackDesiredState() {
		return this.isExpressionTypeFeedbackDesiredState;
	}
	public edu.cmu.cs.dennisc.croquet.BooleanState getEmphasizingClassesState() {
		return this.isEmphasizingClassesOperation;
	}
	@Deprecated
	public boolean isEmphasizingClasses() {
		return this.isEmphasizingClassesOperation.getValue();
	}
	public edu.cmu.cs.dennisc.croquet.BooleanState getOmissionOfThisForFieldAccessesDesiredState() {
		return this.isOmissionOfThisForFieldAccessesDesiredState;
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return this.isDefaultFieldNameGenerationDesiredState.getValue();
	}

	private WindowMenuModel windowMenuModel = new WindowMenuModel( 
			this.isEmphasizingClassesOperation,
			this.isOmissionOfThisForFieldAccessesDesiredState,
			this.isExpressionTypeFeedbackDesiredState,
			this.isDefaultFieldNameGenerationDesiredState
			//			windowOperations.add( this.isEmphasizingClassesOperation );
			//			windowOperations.add( this.isOmissionOfThisForFieldAccessesDesiredOperation );
			//			windowOperations.add( this.isExpressionTypeFeedbackDesiredOperation );
			//			windowOperations.add( this.isDefaultFieldNameGenerationDesiredOperation );

	//			class LocaleComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
	//				private java.util.Locale[] candidates = { new java.util.Locale( "en", "US" ), new java.util.Locale( "en", "US", "java" ) };
	//				private int selectedIndex;
	//
	//				public LocaleComboBoxModel() {
	//					this.selectedIndex = 0;
	//				}
	//				public Object getElementAt( int index ) {
	//					return this.candidates[ index ];
	//				}
	//				public int getSize() {
	//					return this.candidates.length;
	//				}
	//				public Object getSelectedItem() {
	//					if( 0 <= this.selectedIndex && this.selectedIndex < this.candidates.length ) {
	//						return this.candidates[ this.selectedIndex ];
	//					} else {
	//						return null;
	//					}
	//				}
	//				public void setSelectedItem( Object selectedItem ) {
	//					int index = -1;
	//					if( selectedItem != null ) {
	//						int i = 0;
	//						for( java.util.Locale locale : this.candidates ) {
	//							if( selectedItem.equals( locale ) ) {
	//								index = i;
	//								break;
	//							}
	//							i++;
	//						}
	//					}
	//					this.selectedIndex = index;
	//				}
	//			}
	//
	//			class LocaleItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< java.util.Locale > {
	//				public LocaleItemSelectionOperation() {
	//					super( new LocaleComboBoxModel() );
	//				}
	//				@Override
	//				protected String getNameFor( int index, java.util.Locale locale ) {
	//					if( locale != null ) {
	//						String variant = locale.getVariant();
	//						if( variant != null && variant.length() > 0 ) { //should not be null
	//							return variant;
	//						} else {
	//							return "alice";
	//						}
	//					} else {
	//						return "null";
	//					}
	//				}
	//				@Override
	//				protected void handleSelectionChange( java.util.Locale value ) {
	//					IDE.this.setLocale( value );
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: support undo", this );
	//				}
	//			}
	//
	//			java.util.List< edu.cmu.cs.dennisc.zoot.Operation > windowOperations = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();
	//
	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
	//			windowOperations.add( this.isHistoryShowingOperation );
	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
	//			windowOperations.add( this.isEmphasizingClassesOperation );
	//			windowOperations.add( this.isOmissionOfThisForFieldAccessesDesiredOperation );
	//			windowOperations.add( this.isExpressionTypeFeedbackDesiredOperation );
	//			windowOperations.add( this.isDefaultFieldNameGenerationDesiredOperation );
	//
	//			//		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
	//			//			//pass
	//			//		} else {
	//			//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
	//			//			windowOperations.add( this.getPreferencesOperation() );
	//			//		}
	//
	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
	//			windowOperations.add( this.isMemoryUsageShowingOperation );
	//			windowOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
	//			windowOperations.add( this.isSceneEditorExpandedOperation );
	);
	private HelpMenuModel helpMenuModel = new HelpMenuModel(
			new org.alice.ide.operations.help.HelpOperation(),
			edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
			//			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.IDE.isBogusExceptionGenerationDesired" ) ) {
			//				helpOperations.add( new org.alice.ide.operations.help.ThrowBogusExceptionOperation() );
			//				helpOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
			//			}
			//new org.alice.ide.operations.help.ThrowBogusExceptionOperation(),
			new org.alice.ide.operations.help.ReportBugOperation(), new org.alice.ide.operations.help.SuggestImprovementOperation(), new org.alice.ide.operations.help.RequestNewFeatureOperation(), edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR,
			new org.alice.ide.operations.help.WarningOperation( true ), new org.alice.ide.operations.help.DisplaySystemPropertiesOperation(), new org.alice.ide.operations.help.ReleaseNotesOperation(),
	//			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
	//				//pass
	//			} else {
//					helpOperations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
//					helpOperations.add( this.getAboutOperation() );
	//			}
			this.getAboutOperation()
	);

	private int rootDividerLocation = 320;
	private int leftDividerLocation = 240;

	private edu.cmu.cs.dennisc.javax.swing.components.JConcealedBin concealedBin = new edu.cmu.cs.dennisc.javax.swing.components.JConcealedBin();
	private org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor;
	private org.alice.ide.gallerybrowser.AbstractGalleryBrowser galleryBrowser;
	private org.alice.ide.memberseditor.MembersEditor membersEditor;
	private org.alice.ide.editorstabbedpane.EditorsTabSelectionStateOperation editorsTabbedPaneOperation;
	private org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane;

	private edu.cmu.cs.dennisc.croquet.VerticalSplitPane left = new edu.cmu.cs.dennisc.croquet.VerticalSplitPane();
	private edu.cmu.cs.dennisc.croquet.BorderPanel right = new edu.cmu.cs.dennisc.croquet.BorderPanel();
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane root = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane( left, right );

	public enum AccessorAndMutatorDisplayStyle {
		GETTER_AND_SETTER,
		ACCESS_AND_ASSIGNMENT
	}
	
	public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		if( field.getDeclaringType().isDeclaredInAlice() ) {
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		} else {
			//return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		}
	}
	
	private void setSceneEditorExpanded( boolean isSceneEditorExpanded ) {
		if( isSceneEditorExpanded ) {
			if( this.root.getAwtComponent().isValid() ) {
				this.rootDividerLocation = this.root.getDividerLocation();
			}
			if( this.left.getAwtComponent().isValid() ) {
				this.leftDividerLocation = this.left.getDividerLocation();
			}
			this.left.setResizeWeight( 1.0 );
			this.root.setLeftComponent( this.left );
			this.left.setTopComponent( this.sceneEditor );
			this.left.setBottomComponent( this.galleryBrowser );
			//this.root.setRightComponent( null );
			this.right.setVisible( false );
			this.root.setDividerSize( 0 );
			this.left.setDividerLocation( this.getFrame().getHeight() - 300 );
		} else {
			this.left.setResizeWeight( 0.0 );
			this.root.setLeftComponent( this.left );
			this.right.setVisible( true );
			//this.root.setRightComponent( this.right );
			this.root.setDividerLocation( this.rootDividerLocation );
			this.left.setTopComponent( this.sceneEditor );
			this.left.setBottomComponent( this.membersEditor );
			this.left.setDividerLocation( this.leftDividerLocation );
			//			if( this.right.getComponentCount() == 0 ) {
			//				this.right.add( this.ubiquitousPane, java.awt.BorderLayout.SOUTH );
			//				this.right.add( this.editorsTabbedPane, java.awt.BorderLayout.CENTER );
			//				this.right.add( this.declarationsUIResource, java.awt.BorderLayout.NORTH );
			//			}
			this.root.setDividerSize( this.left.getDividerSize() );
		}
	}

	@Override
	protected edu.cmu.cs.dennisc.croquet.MenuBarModel createMenuBarOperation() {
		edu.cmu.cs.dennisc.croquet.MenuBarModel rv = new edu.cmu.cs.dennisc.croquet.MenuBarModel( IDE_GROUP, java.util.UUID.fromString( "f621208a-244e-4cbe-8263-52ebb6916c2d" ) );
		rv.addMenuModel( this.fileMenuModel );
		rv.addMenuModel( this.editMenuModel );
		rv.addMenuModel( this.projectMenuModel );
		rv.addMenuModel( this.runMenuModel );
		rv.addMenuModel( this.windowMenuModel );
		rv.addMenuModel( this.helpMenuModel );
		return rv;
	}

	public edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getPerformEditorGeneratedSetUpMethod() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = this.getSceneType();
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : sceneType.methods ) {
			if( IDE.performSceneEditorGeneratedSetUpMethodNameSet.contains( method.name.getValue() ) ) {
				return method;
			}
		}
		return null;
	}

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getStrippedProgramType() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice rv = this.getProgramType();
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice setUpMethod = this.getPerformEditorGeneratedSetUpMethod();
		setUpMethod.body.getValue().statements.clear();
		return rv;
	}
	public java.util.List< edu.cmu.cs.dennisc.alice.ast.FieldAccess > getFieldAccesses( final edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = this.getStrippedProgramType();
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess >( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
				return fieldAccess.field.getValue() == field;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	public java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > getMethodInvocations( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = this.getStrippedProgramType();
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >(
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
				return methodInvocation.method.getValue() == method;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}

	private org.alice.ide.memberseditor.Factory templatesFactory = new org.alice.ide.memberseditor.Factory();

	public org.alice.ide.memberseditor.Factory getTemplatesFactory() {
		return this.templatesFactory;
	}

	private org.alice.ide.codeeditor.Factory codeFactory = new org.alice.ide.codeeditor.Factory();

	public org.alice.ide.codeeditor.Factory getCodeFactory() {
		return this.codeFactory;
	}

	private org.alice.ide.preview.Factory previewFactory = new org.alice.ide.preview.Factory();

	public org.alice.ide.preview.Factory getPreviewFactory() {
		return this.previewFactory;
	}

	public void refreshUbiquitousPane() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				if( IDE.this.ubiquitousPane != null ) {
					IDE.this.ubiquitousPane.refresh();
				}
			}
		} );
	}

	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getTypeDeclaredInAliceFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava superType ) {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > aliceTypes = this.addAliceTypes( new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType >() );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : aliceTypes ) {
			assert type != null;
			if( type.getFirstTypeEncounteredDeclaredInJava() == superType ) {
				return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
			}
		}
		String name = "My" + superType.getName();
		return org.alice.ide.ast.NodeUtilities.createType( name, superType );
	}
	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getTypeDeclaredInAliceFor( Class< ? > superCls ) {
		return getTypeDeclaredInAliceFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( superCls ) );

	}

	public edu.cmu.cs.dennisc.croquet.JComponent< ? > getOverrideComponent( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		return null;
	}

	public boolean isDropDownDesiredForFieldInitializer( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		return true;
	}
	public boolean isDropDownDesiredFor( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		return (expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression || expression instanceof edu.cmu.cs.dennisc.alice.ast.ResourceExpression) == false;
	}
	public org.alice.ide.common.TypeComponent getComponentFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		//todo:
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}
	public String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return null;
	}
	public javax.swing.Icon getIconFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new org.alice.ide.common.TypeIcon( type );
	}

	private edu.cmu.cs.dennisc.alice.ast.AbstractType[] typesForComboBoxes;

	protected java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > addJavaTypes( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( String.class ) );
		return rv;
	}

	protected java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > addAliceTypes( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType = this.getSceneType();
		if( sceneType != null ) {
			rv.add( sceneType );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> valueType = field.getValueType();
				if( valueType.isDeclaredInAlice() ) {
					if( rv.contains( valueType ) ) {
						//pass
					} else {
						rv.add( valueType );
					}
				}
			}
		}
		return rv;
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractType[] getTypesForComboBoxes() {
		if( this.typesForComboBoxes != null ) {
			//pass
		} else {
			java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.addJavaTypes( list );
			this.addAliceTypes( list );
			this.typesForComboBoxes = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( list, edu.cmu.cs.dennisc.alice.ast.AbstractType.class );
		}
		return this.typesForComboBoxes;
	}
	protected abstract org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor();
	public abstract java.io.File getGalleryRootDirectory();
	protected abstract org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser( java.io.File galleryRootDirectory );
	protected org.alice.ide.memberseditor.MembersEditor createClassMembersEditor() {
		return new org.alice.ide.memberseditor.MembersEditor();
	}
	protected org.alice.ide.editorstabbedpane.EditorsTabSelectionStateOperation createEditorsTabbedPaneOperation() {
		return new org.alice.ide.editorstabbedpane.EditorsTabSelectionStateOperation();
	}
	protected org.alice.ide.ubiquitouspane.UbiquitousPane createUbiquitousPane() {
		return new org.alice.ide.ubiquitouspane.UbiquitousPane();
	}

	private java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > expressionFillerInners;

	public org.alice.ide.ubiquitouspane.UbiquitousPane getUbiquitousPane() {
		return this.ubiquitousPane;
	}
	public org.alice.ide.editorstabbedpane.EditorsTabSelectionStateOperation getEditorsTabSelectionState() {
		return this.editorsTabbedPaneOperation;
	}
	public org.alice.ide.memberseditor.MembersEditor getMembersEditor() {
		return this.membersEditor;
	}
	public org.alice.ide.gallerybrowser.AbstractGalleryBrowser getGalleryBrowser() {
		return this.galleryBrowser;
	}
	public org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor() {
		return this.sceneEditor;
	}

	private int projectHistoryInsertionIndexOfCurrentFile = 0;

	private void updateHistoryLengthAtLastFileOperation() {
		edu.cmu.cs.dennisc.history.HistoryManager projectHistoryManager = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( edu.cmu.cs.dennisc.alice.Project.GROUP );
		this.projectHistoryInsertionIndexOfCurrentFile = projectHistoryManager.getInsertionIndex();
		this.updateTitle();
	}

	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		assert IDE.singleton == null;
		IDE.singleton = this;
		this.promptForLicenseAgreements();

		//org.alice.ide.preferences.GeneralPreferences.getSingleton().desiredRecentProjectCount.setAndCommitValue( 10 );
		//org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.clear();

		edu.cmu.cs.dennisc.history.HistoryManager.getInstance( edu.cmu.cs.dennisc.alice.Project.GROUP ).addHistoryListener( new edu.cmu.cs.dennisc.history.event.HistoryListener() {
			public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
			}
			public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
			}
			public void insertionIndexChanging( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
			}
			public void insertionIndexChanged( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
				updateTitle();
			}
			public void clearing( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
			}
			public void cleared( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
			}
		} );

		this.runOperation.setEnabled( false );

		this.sceneEditor = this.createSceneEditor();
		this.galleryBrowser = this.createGalleryBrowser( this.getGalleryRootDirectory() );
		this.membersEditor = this.createClassMembersEditor();
		this.editorsTabbedPaneOperation = this.createEditorsTabbedPaneOperation();
		this.ubiquitousPane = this.createUbiquitousPane();

		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = this.editorsTabbedPaneOperation.createEditorsFolderTabbedPane();
		tabbedPane.scaleFont( 2.0f );
		this.right.addComponent( this.ubiquitousPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		this.right.addComponent( tabbedPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		//this.right.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "hello" ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		//edu.cmu.cs.dennisc.swing.InputPane.setDefaultOwnerFrame( this );
		this.vmForRuntimeProgram = createVirtualMachineForRuntimeProgram();
		this.vmForSceneEditor = createVirtualMachineForSceneEditor();

		//this.setLocale( new java.util.Locale( "en", "US", "java" ) );
		//javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "en", "US", "java" ) );

		this.expressionFillerInners = this.addExpressionFillerInners( new java.util.LinkedList< org.alice.ide.cascade.fillerinners.ExpressionFillerInner >() );

		this.isSceneEditorExpandedState.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				setSceneEditorExpanded( nextValue );
			}
		} );

		this.addProjectObserver( new ProjectObserver() {
			public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			}
			public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
				getRunOperation().setEnabled( nextProject != null );
			}
		} );
	}

	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		rv.addMouseWheelListener( new edu.cmu.cs.dennisc.javax.swing.plaf.metal.FontMouseWheelAdapter() );
		rv.addComponent( this.root, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		rv.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.concealedBin ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );

		this.setSceneEditorExpanded( false );
		return rv;
	}

	private java.util.Map< Class< ? extends Enum >, org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner > map = new java.util.HashMap< Class< ? extends Enum >, org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner >();

	protected org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner getExpressionFillerInnerFor( Class< ? extends Enum > clsEnum ) {
		org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner rv = map.get( clsEnum );
		if( rv != null ) {
			//pass
		} else {
			rv = new org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( clsEnum );
			map.put( clsEnum, rv );
		}
		return rv;
	}
	//	protected org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner getExpressionFillerInnerFor( Class< ? extends Enum > clsEnum ) {
	//		return getExpressionFillerInnerFor( clsEnum, clsEnum );
	//	}

	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		rv.add( new org.alice.ide.cascade.fillerinners.NumberFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.IntegerFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.BooleanFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.StringFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.AudioResourceFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.ImageResourceFillerInner() );
		return rv;
	}

	public void addToConcealedBin( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		this.concealedBin.add( component.getAwtComponent() );
		this.concealedBin.revalidate();
	}
	public void removeFromConcealedBin( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		this.concealedBin.remove( component.getAwtComponent() );
	}

	public boolean isJava() {
		return this.getFrame().getAwtWindow().getLocale().getVariant().equals( "java" );
	}
	public String getTextForNull() {
		if( isJava() ) {
			return "null";
		} else {
			return "<unset>";
		}
	}

	private java.io.File applicationDirectory = null;

	protected java.io.File getDefaultApplicationRootDirectory() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			return new java.io.File( "/Applications/" + this.getApplicationName() + ".app/Contents/Resources/Java/application" );
		} else {
			return new java.io.File( "/Program Files/" + this.getApplicationName() + "3Beta/application" );
		}
	}
	private java.io.File getApplicationRootDirectory( String[] propertyKeys, String[] subPaths ) {
		for( String propertyKey : propertyKeys ) {
			for( String subPath : subPaths ) {
				java.io.File rv = new java.io.File( System.getProperty( propertyKey ), subPath );
				if( rv.exists() ) {
					return rv;
				}
			}
		}
		return null;
	}
	@Override
	public java.io.File getApplicationRootDirectory() {
		if( this.applicationDirectory != null ) {
			//pass
		} else {
			this.applicationDirectory = getApplicationRootDirectory( new String[] { "org.alice.ide.IDE.install.dir", "user.dir" }, new String[] { "application", "required/application/" + this.getVersionText() } );
			if( this.applicationDirectory != null ) {
				//pass
			} else {
				this.applicationDirectory = this.getDefaultApplicationRootDirectory();
			}
		}
		return this.applicationDirectory;
	}

	protected StringBuffer updateBugReportSubmissionTitle( StringBuffer rv ) {
		rv.append( "Please Submit Bug Report: " );
		this.updateTitlePrefix( rv );
		return rv;
	}
	private String getBugReportSubmissionTitle() {
		StringBuffer sb = new StringBuffer();
		updateBugReportSubmissionTitle( sb );
		return sb.toString();
	}
	@Override
	public String getApplicationName() {
		return "Alice";
	}
	@Override
	protected String getVersionText() {
		return edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText();
	}
	@Override
	protected String getVersionAdornment() {
		return " 3 BETA ";
	}
	//	private java.util.List< zoot.DropReceptor > dropReceptors = new java.util.LinkedList< zoot.DropReceptor >();

	public org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus() {
		return this.editorsTabbedPaneOperation.getCodeEditorInFocus();
	}

	private ComponentStencil stencil;
	private java.util.List< ? extends edu.cmu.cs.dennisc.croquet.Component< ? > > holes = null;
	private edu.cmu.cs.dennisc.croquet.DragComponent potentialDragSource;
	private edu.cmu.cs.dennisc.croquet.Component< ? > currentDropReceptorComponent;

	protected boolean isFauxStencilDesired() {
		return this.isDragInProgress;
		//return true;
	}

	private static java.awt.Stroke THIN_STROKE = new java.awt.BasicStroke( 1.0f );
	private static java.awt.Stroke THICK_STROKE = new java.awt.BasicStroke( 3.0f );//, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER );

	class ComponentStencil extends javax.swing.JPanel {
		public ComponentStencil() {
			this.setOpaque( false );
			this.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.HAND_CURSOR ) );
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "paint stencil" );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			if( IDE.this.holes != null ) {
				//java.awt.geom.Area area = new java.awt.geom.Area( g2.getClipBounds() );
				java.awt.geom.Area area = new java.awt.geom.Area( new java.awt.Rectangle( 0, 0, getWidth(), getHeight() ) );
				synchronized( IDE.this.holes ) {
					if( IDE.this.currentDropReceptorComponent != null ) {
						this.setForeground( new java.awt.Color( 0, 0, 127, 95 ) );
					} else {
						this.setForeground( new java.awt.Color( 0, 0, 127, 127 ) );
					}

					java.awt.Rectangle potentialDragSourceBounds;
					if( IDE.this.potentialDragSource != null ) {
						potentialDragSourceBounds = IDE.this.potentialDragSource.getBounds();
						potentialDragSourceBounds = IDE.this.potentialDragSource.getParent().convertRectangle( potentialDragSourceBounds, this );
					} else {
						potentialDragSourceBounds = null;
					}

					if( isFauxStencilDesired() ) {
						for( edu.cmu.cs.dennisc.croquet.Component< ? > component : IDE.this.holes ) {
							java.awt.Rectangle holeBounds = component.getBounds();
							holeBounds = component.getParent().convertRectangle( holeBounds, this );
							area.subtract( new java.awt.geom.Area( holeBounds ) );
						}

						if( potentialDragSourceBounds != null ) {
							area.subtract( new java.awt.geom.Area( potentialDragSourceBounds ) );
						}
						g2.fill( area );
					}

					g2.setStroke( THICK_STROKE );
					final int BUFFER = 6;
					for( edu.cmu.cs.dennisc.croquet.Component< ? > component : IDE.this.holes ) {
						java.awt.Rectangle holeBounds = component.getBounds();
						holeBounds = component.getParent().convertRectangle( holeBounds, this );
						holeBounds.x -= BUFFER;
						holeBounds.y -= BUFFER;
						holeBounds.width += 2 * BUFFER;
						holeBounds.height += 2 * BUFFER;

						g2.setColor( new java.awt.Color( 0, 0, 0 ) );
						g2.draw( holeBounds );
						if( IDE.this.currentDropReceptorComponent == component ) {
							g2.setColor( new java.awt.Color( 0, 255, 0 ) );
							g2.setStroke( THIN_STROKE );
							g2.draw( holeBounds );
							g2.setStroke( THICK_STROKE );
							g2.setColor( new java.awt.Color( 191, 255, 191, 63 ) );
							g2.fill( holeBounds );
						}
						//
						////						g2.translate( 1, 1 );
						////						g2.draw( holeBounds );
						////						g2.translate( -1, -1 );
						//						if( IDE.this.currentDropReceptorComponent == component ) {
						//							g2.setColor( new java.awt.Color( 0, 0, 0 ) );
						//							g2.draw( holeBounds );
						//						} else {
						////							g2.setColor( java.awt.Color.YELLOW );
						////							g2.draw3DRect( holeBounds.x, holeBounds.y, holeBounds.width, holeBounds.height, false );
						//							int x0 = holeBounds.x;
						//							int x1 = holeBounds.x+holeBounds.width;
						//							int y0 = holeBounds.y;
						//							int y1 = holeBounds.y+holeBounds.height;
						//							g2.setColor( new java.awt.Color( 63, 91, 63 ) );
						//							g2.drawLine( x0, y1, x0, y0 );
						//							g2.drawLine( x0, y0, x1, y0 );
						//							g2.setColor( new java.awt.Color( 160, 191, 160 ) );
						//							g2.drawLine( x0, y1, x1, y1 );
						//							g2.drawLine( x1, y1, x1, y0 );
						//						}
					}
					if( potentialDragSourceBounds != null ) {
						g2.setColor( java.awt.Color.BLUE );
						g2.draw( potentialDragSourceBounds );
					}
				}
			}
		}
	}

	//public abstract void handleDelete( edu.cmu.cs.dennisc.alice.ast.Node node );

	public void showStencilOver( edu.cmu.cs.dennisc.croquet.DragComponent potentialDragSource, final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			this.holes = codeEditor.createListOfPotentialDropReceptors( type );
			this.potentialDragSource = potentialDragSource;
			//java.awt.Rectangle bounds = codeEditor.getBounds();
			//bounds = javax.swing.SwingUtilities.convertRectangle( codeEditor, bounds, layeredPane );
			//this.stencil.setBounds( bounds );
			javax.swing.JLayeredPane layeredPane = this.getFrame().getAwtWindow().getLayeredPane();
			if( this.stencil != null ) {
				//pass
			} else {
				this.stencil = new ComponentStencil();
			}
			this.stencil.setBounds( layeredPane.getBounds() );
			layeredPane.add( this.stencil, null );
			layeredPane.setLayer( this.stencil, javax.swing.JLayeredPane.POPUP_LAYER - 1 );

			this.stencil.repaint();
		}
	}
	public void hideStencil() {
		javax.swing.JLayeredPane layeredPane = this.getFrame().getAwtWindow().getLayeredPane();
		if( this.stencil != null && this.stencil.getParent() == layeredPane ) {
			layeredPane.remove( this.stencil );
			layeredPane.repaint();
			this.holes = null;
			this.potentialDragSource = null;
		}
	}

	private java.util.Stack< ReasonToDisableSomeAmountOfRendering > reasonToDisableSomeAmountOfRenderingStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	public void disableRendering( ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
		this.reasonToDisableSomeAmountOfRenderingStack.push( reasonToDisableSomeAmountOfRendering );
		if( reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM ) {
			//pass
		} else {
			this.root.setIgnoreRepaint( true );
			this.left.setIgnoreRepaint( true );
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ignore repaint: true" );
		}
		this.sceneEditor.disableRendering( reasonToDisableSomeAmountOfRendering );
	}
	public void enableRendering() {
		if( reasonToDisableSomeAmountOfRenderingStack.size() > 0 ) {
			ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering = reasonToDisableSomeAmountOfRenderingStack.pop();
			if( reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM ) {
				//pass
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ignore repaint: false" );
				this.root.setIgnoreRepaint( false );
				this.left.setIgnoreRepaint( false );
			}
			this.sceneEditor.enableRendering( reasonToDisableSomeAmountOfRendering );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate extra enableRendering" );
		}
	}

	//	protected void setRenderingEnabled( boolean isRenderingEnabled, boolean isDrag ) {
	//		this.root.setIgnoreRepaint( isRenderingEnabled==false );
	//		this.left.setIgnoreRepaint( isRenderingEnabled==false );
	//		this.sceneEditor.setRenderingEnabled( isRenderingEnabled, isDrag );
	//	}

	public void handleDragStarted( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
		this.potentialDragSource = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
		ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering;
		if( (dragAndDropContext.getLatestMouseEvent().getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK) != 0 ) {
			reasonToDisableSomeAmountOfRendering = ReasonToDisableSomeAmountOfRendering.DRAG_AND_DROP;
		} else {
			reasonToDisableSomeAmountOfRendering = ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK;
		}
		this.disableRendering( reasonToDisableSomeAmountOfRendering );
	}
	public void handleDragEnteredDropReceptor( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
		//		this.currentDropReceptorComponent = dragAndDropContext.getCurrentDropReceptor().getAWTComponent();
		//		if( this.stencil != null && this.holes != null ) {
		//			this.stencil.repaint();
		//		}
	}
	public void handleDragExitedDropReceptor( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
		this.currentDropReceptorComponent = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragStopped( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
		this.enableRendering();
		//		new Thread() {
		//			@Override
		//			public void run() {
		//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 1000 );
		//				javax.swing.SwingUtilities.invokeLater( new Runnable() {
		//					public void run() {
		//						IDE.this.enableRendering();
		//					}
		//				} );
		//			}
		//		}.start();
	}

	//	public void setRenderingEnabled( boolean isRenderingEnabled ) {
	//		if( isRenderingEnabled ) {
	//			edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
	//		} else {
	//			edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
	//		}
	//		this.sceneEditor.setRenderingEnabled( isRenderingEnabled );
	//	}

	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForRuntimeProgram;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForSceneEditor;

	private edu.cmu.cs.dennisc.croquet.ListSelectionState< edu.cmu.cs.dennisc.alice.ast.AbstractField > fieldSelectionState = new edu.cmu.cs.dennisc.croquet.ListSelectionState< edu.cmu.cs.dennisc.alice.ast.AbstractField >( IDE_GROUP, java.util.UUID
			.fromString( "a6d09409-82b8-4dfe-b156-588f1983893c" ) ) {
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.AbstractField decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}
		@Override
		protected void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.alice.ast.AbstractField value ) {
			throw new RuntimeException( "todo" );
		}
	};

	public edu.cmu.cs.dennisc.croquet.ListSelectionState< edu.cmu.cs.dennisc.alice.ast.AbstractField > getFieldSelectionState() {
		return this.fieldSelectionState;
	}

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshFields();
		}
	};

	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	protected boolean isFieldDesired( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return field.getValueType().isArray() == false;
	}
	
	public void refreshFields() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshFields" );
		java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( this.rootField != null ) {
			fields.add( this.rootField );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
				if( this.isFieldDesired( field ) ) {
					fields.add( field );
				}
			}
		}
		this.fieldSelectionState.setListData( -1, fields );
	}
	private void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	@Override
	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		super.setProject( project );
		this.setRootField( this.getSceneField() );
	}

	public edu.cmu.cs.dennisc.alice.ast.Node createCopy( edu.cmu.cs.dennisc.alice.ast.Node original ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice root = this.getProgramType();
		java.util.Set< edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > abstractDeclarations = root.createDeclarationSet();
		original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations );
		java.util.Map< Integer, edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > map = edu.cmu.cs.dennisc.alice.ast.Node.createMapOfDeclarationsThatShouldNotBeCopied( abstractDeclarations );
		org.w3c.dom.Document xmlDocument = original.encode( abstractDeclarations );
		edu.cmu.cs.dennisc.alice.ast.Node dst = edu.cmu.cs.dennisc.alice.ast.Node.decode( xmlDocument, edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText(), map, false );

		//		if( original.isEquivalentTo( dst ) ) {
		//			return dst;
		//		} else {
		//			throw new RuntimeException( "copy not equivalent to original" );
		//		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: check copy" );
		return dst;
	}

	//	public abstract void handleRun( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType programType );
	//	public abstract void handlePreviewMethod( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation );
	//	public abstract void handleRestart( edu.cmu.cs.dennisc.croquet.ModelContext context );

	private boolean isDragInProgress = false;

	private edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus = null;

	public edu.cmu.cs.dennisc.alice.ast.Comment getCommentThatWantsFocus() {
		return this.commentThatWantsFocus;
	}
	public void setCommentThatWantsFocus( edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus ) {
		this.commentThatWantsFocus = commentThatWantsFocus;
	}

	protected abstract void promptForLicenseAgreements();

	private java.awt.Window splashScreen;

	public java.awt.Window getSplashScreen() {
		return this.splashScreen;
	}
	public void setSplashScreen( java.awt.Window splashScreen ) {
		this.splashScreen = splashScreen;
	}

	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
		if( this.splashScreen != null ) {
			this.splashScreen.setVisible( false );
		}
		if( this.getUri() != null ) {
			//pass
		} else {
			this.getNewProjectOperation().fire( e );
		}
	}
	@Override
	protected void handleAbout( java.util.EventObject e ) {
		this.getAboutOperation().fire( e );
	}
	@Override
	protected void handlePreferences( java.util.EventObject e ) {
		this.getPreferencesOperation().fire( e );
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		this.preservePreferences();
//		preservePreference( this.isEmphasizingClassesOperation );
//		preservePreference( this.isExpressionTypeFeedbackDesiredOperation );
//		preservePreference( this.isOmissionOfThisForFieldAccessesDesiredState );
//		preservePreference( this.isDefaultFieldNameGenerationDesiredOperation );

		this.getExitOperation().fire( e );
		//this.performIfAppropriate( this.getExitOperation(), e, true );
	}
	//	protected abstract void handleWindowClosing();

	public java.util.List< ? extends edu.cmu.cs.dennisc.croquet.DropReceptor > createListOfPotentialDropReceptors( edu.cmu.cs.dennisc.croquet.DragComponent source ) {
		assert source != null;
		org.alice.ide.codeeditor.CodeEditor codeEditor = this.getCodeEditorInFocus();
		if( codeEditor != null ) {
			if( source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance ) {
				org.alice.ide.common.ExpressionLikeSubstance expressionLikeSubstance = (org.alice.ide.common.ExpressionLikeSubstance)source.getSubject();
				return codeEditor.createListOfPotentialDropReceptors( expressionLikeSubstance.getExpressionType() );
			} else {
				java.util.List< edu.cmu.cs.dennisc.croquet.DropReceptor > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				rv.add( codeEditor );
				//			for( alice.ide.ast.DropReceptor dropReceptor : this.dropReceptors ) {
				//				if( dropReceptor.isPotentiallyAcceptingOf( source ) ) {
				//					rv.add( dropReceptor );
				//				}
				//			}
				return rv;
			}
		} else {
			//todo: investigate
			return new java.util.LinkedList< edu.cmu.cs.dennisc.croquet.DropReceptor >();
		}
	}

	//	private boolean addSeparatorIfNecessary( edu.cmu.cs.dennisc.cascade.Blank blank, String text, boolean isNecessary ) {
	//		if( isNecessary ) {
	//			blank.addSeparator( text );
	//		}
	//		return false;
	//	}
	protected void addFillInAndPossiblyPartFills( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.AbstractType type2 ) {
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.Expression >( expression ) );
	}

	private static Iterable< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice > getVariables( edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice >(
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}
	private static Iterable< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice > getConstants( edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice >(
				edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeInScope() {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus = this.getFocusedCode();
		if( codeInFocus != null ) {
			return codeInFocus.getDeclaringType();
		} else {
			return null;
		}
	}

	//todo: remove this
	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getActualTypeForDesiredParameterType( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return type;
	}

	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > updateAccessibleLocalsForBlockStatementAndIndex( java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index ) {
		while( index >= 1 ) {
			index--;
			edu.cmu.cs.dennisc.alice.ast.Statement statementI = blockStatement.statements.get( index );
			if( statementI instanceof edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement ) {
				edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement localDeclarationStatement = (edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement)statementI;
				rv.add( localDeclarationStatement.getLocal() );
			}
		}
		return rv;
	}
	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > updateAccessibleLocals( java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		edu.cmu.cs.dennisc.alice.ast.Node parent = statement.getParent();
		if( parent instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
			edu.cmu.cs.dennisc.alice.ast.Statement statementParent = (edu.cmu.cs.dennisc.alice.ast.Statement)parent;
			if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
				edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatementParent = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)statementParent;
				int index = blockStatementParent.statements.indexOf( statement );
				this.updateAccessibleLocalsForBlockStatementAndIndex(rv, blockStatementParent, index);
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.CountLoop ) {
				edu.cmu.cs.dennisc.alice.ast.CountLoop countLoopParent = (edu.cmu.cs.dennisc.alice.ast.CountLoop)statementParent;
				boolean areCountLoopLocalsViewable = this.isJava();
				if( areCountLoopLocalsViewable ) {
					rv.add( countLoopParent.variable.getValue() );
					rv.add( countLoopParent.constant.getValue() );
				}
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop forEachLoopParent = (edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop)statementParent;
				rv.add( forEachLoopParent.variable.getValue() );
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether eachInTogetherParent = (edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether)statementParent;
				rv.add( eachInTogetherParent.variable.getValue() );
			}
			updateAccessibleLocals( rv, statementParent );
		}
		return rv;
	}

	private Iterable< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > getAccessibleLocals( edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index ) {
		java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updateAccessibleLocalsForBlockStatementAndIndex( rv, blockStatement, index );
		updateAccessibleLocals( rv, blockStatement );
		return rv;
	}
	protected void addExpressionBonusFillInsForType( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus = this.getFocusedCode();
		if( codeInFocus != null ) {

			//todo: fix
			type = this.getActualTypeForDesiredParameterType( type );

			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> selectedType = this.getTypeInScope();
			//boolean isNecessary = true;
			if( type.isAssignableFrom( selectedType ) ) {
				//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
				this.addFillInAndPossiblyPartFills( blank, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), selectedType, type );
			}
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : selectedType.getDeclaredFields() ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType fieldType = field.getValueType();
				if( type.isAssignableFrom( fieldType ) ) {
					//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
					edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
					this.addFillInAndPossiblyPartFills( blank, fieldAccess, fieldType, type );
				}
				if( fieldType.isArray() ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractType fieldComponentType = fieldType.getComponentType();
					if( type.isAssignableFrom( fieldComponentType ) ) {
						//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
						edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
						//blank.addFillIn( new ArrayAccessFillIn( fieldType, fieldAccess ) );
					}
					if( type.isAssignableFrom( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) ) {
						//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
						edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
						edu.cmu.cs.dennisc.alice.ast.ArrayLength arrayLength = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( fieldAccess );
						blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.ArrayLength >( arrayLength ) );
					}
				}
			}
//			edu.cmu.cs.dennisc.alice.ast.Expression prevExpression = this.getPreviousExpression();
//			if( prevExpression != null ) {
//				edu.cmu.cs.dennisc.alice.ast.Statement statement = prevExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.Statement.class );
				if( this.dropParent != null && this.dropIndex != -1 ) {
					for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : codeInFocus.getParameters() ) {
						if( type.isAssignableFrom( parameter.getValueType() ) ) {
							//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
							this.addFillInAndPossiblyPartFills( blank, new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter ), parameter.getValueType(), type );
						}
					}
					for( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local : this.getAccessibleLocals( this.dropParent, this.dropIndex ) ) {
						if( type.isAssignableFrom( local.valueType.getValue() ) ) {
							edu.cmu.cs.dennisc.alice.ast.Expression expression;
							if( local instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
								edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)local;
								expression = new edu.cmu.cs.dennisc.alice.ast.VariableAccess( variable );
							} else if( local instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
								edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant = (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)local;
								expression = new edu.cmu.cs.dennisc.alice.ast.ConstantAccess( constant );
							} else {
								expression = null;
							}
							if( expression != null ) {
								this.addFillInAndPossiblyPartFills( blank, expression, local.valueType.getValue(), type );
							}
						}
					}
//				}
			}
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		return this.previousExpression;
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType interfaceType ) {
		return null;
	}
	protected void addFillInsForObjectType( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomStringFillIn() );
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomDoubleFillIn() );
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomIntegerFillIn() );
		blank.addSeparator();
		if( blank.getParentFillIn() != null ) {
			//pass
		} else {
			if( this.previousExpression != null ) {
				blank.addFillIn( new org.alice.ide.cascade.MostlyDeterminedStringConcatenationFillIn( this.previousExpression ) );
			}
			blank.addFillIn( new org.alice.ide.cascade.IncompleteStringConcatenationFillIn() );
			blank.addSeparator();
		}
	}
	protected void addCustomFillIns( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
	}

	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Number.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE;
		} else {
			return type;
		}
	}

	protected boolean areEnumConstantsDesired( edu.cmu.cs.dennisc.alice.ast.AbstractType enumType ) {
		return true;
	}
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type != null ) {
			if( this.previousExpression != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType prevExpressionType = this.previousExpression.getType();
				if( prevExpressionType != null && prevExpressionType.isAssignableTo( type ) ) {
					if( blank.getParentFillIn() != null ) {
						//pass
					} else {
						blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( this.previousExpression, "(current value)" ) );
						blank.addSeparator();
					}
				}
			}
			this.addCustomFillIns( blank, type );
			type = getTypeFor( type );
			if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) ) {
				this.addFillInsForObjectType( blank );
			} else {
				for( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner : this.expressionFillerInners ) {
					if( expressionFillerInner.isAssignableTo( type ) ) {
						expressionFillerInner.addFillIns( blank );
					}
				}
			}

			edu.cmu.cs.dennisc.alice.ast.AbstractType enumType;
			if( type.isInterface() ) {
				enumType = this.getEnumTypeForInterfaceType( type );
			} else {
				if( type.isAssignableTo( Enum.class ) ) {
					enumType = type;
				} else {
					enumType = null;
				}
			}
			if( enumType != null && this.areEnumConstantsDesired( enumType ) ) {
				org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner constantsOwningFillerInner = getExpressionFillerInnerFor( (Class< ? extends Enum >)enumType.getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification() );
				constantsOwningFillerInner.addFillIns( blank );
			}

			blank.addSeparator();
			this.addExpressionBonusFillInsForType( blank, type );
			blank.addSeparator();
			if( type.isArray() ) {
				blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomArrayFillIn() );
			}

			if( blank.getChildren().size() > 0 ) {
				//pass
			} else {
				blank.addFillIn( new edu.cmu.cs.dennisc.cascade.CancelFillIn( "sorry.  no fillins found for " + type.getName() + ". canceling." ) );
			}
		} else {
			//todo:
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.CancelFillIn( "type is <unset>.  canceling." ) );
		}

	}

	//	public void promptUserForStatement( java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
	//		throw new RuntimeException( "todo" );
	//	}

	private edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = null;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement dropParent = null;
	private int dropIndex = -1;

	private void setDropParentAndIndex( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node node = statement.getParent();
			if (node instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement) {
				edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement) node;
				int index = blockStatement.statements.indexOf( statement );
				if( index != -1 ) {
					this.dropParent = blockStatement;
					this.dropIndex = index;
				}
			}
		}
	}
	private edu.cmu.cs.dennisc.cascade.Blank createExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression ) {
		this.previousExpression = prevExpression;
		if( this.previousExpression != null ) {
			this.setDropParentAndIndex( this.previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.Statement.class ) );
		}
		return new org.alice.ide.cascade.ExpressionBlank( type );
	}
	private edu.cmu.cs.dennisc.cascade.FillIn createExpressionsFillIn( final edu.cmu.cs.dennisc.alice.ast.AbstractType[] types, final boolean isArrayLengthDesired ) {
		edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression[] > rv = new edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
			@Override
			protected void addChildren() {
				int N = types.length;
				int i = 0;
				for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : types ) {
					this.addBlank( new org.alice.ide.cascade.ExpressionBlank( type, i == N - 1 && isArrayLengthDesired ) );
					i++;
				}
			}
			@Override
			public edu.cmu.cs.dennisc.alice.ast.Expression[] getValue() {
				edu.cmu.cs.dennisc.alice.ast.Expression[] rv = new edu.cmu.cs.dennisc.alice.ast.Expression[ this.getChildren().size() ];
				int i = 0;
				for( edu.cmu.cs.dennisc.cascade.Node child : this.getChildren() ) {
					rv[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)child).getSelectedFillIn().getValue();
					i++;
				}
				return rv;
			}
			@Override
			public edu.cmu.cs.dennisc.alice.ast.Expression[] getTransientValue() {
				edu.cmu.cs.dennisc.alice.ast.Expression[] rv = new edu.cmu.cs.dennisc.alice.ast.Expression[ this.getChildren().size() ];
				int i = 0;
				for( edu.cmu.cs.dennisc.cascade.Node child : this.getChildren() ) {
					rv[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)child).getSelectedFillIn().getTransientValue();
					i++;
				}
				return rv;
			}
		};
		return rv;
	}
	public void promptUserForExpressions( edu.cmu.cs.dennisc.alice.ast.BlockStatement dropParent, int dropIndex, edu.cmu.cs.dennisc.alice.ast.AbstractType[] types, boolean isArrayLengthDesired, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > taskObserver ) {
		this.dropParent = dropParent;
		this.dropIndex = dropIndex;
		edu.cmu.cs.dennisc.cascade.FillIn fillIn = createExpressionsFillIn( types, isArrayLengthDesired );
		fillIn.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
	public void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, edu.cmu.cs.dennisc.croquet.ViewController< ?,? > viewController, java.awt.Point p,
			edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		edu.cmu.cs.dennisc.cascade.Blank blank = createExpressionBlank( type, prevExpression );
		if( p != null ) {
			//pass
		} else {
			p = new java.awt.Point( 0, viewController.getHeight() );
		}
		blank.showPopupMenu( viewController.getAwtComponent(), p.x, p.y, taskObserver );
	}
	public void promptUserForMore( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement statement, final edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter, edu.cmu.cs.dennisc.croquet.ViewController< ?,? > viewController, java.awt.Point p, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		this.setDropParentAndIndex( statement );
		final String parameterName = parameter.getName();
		edu.cmu.cs.dennisc.cascade.Blank blank;
		if( parameterName != null ) {
			blank = new edu.cmu.cs.dennisc.cascade.Blank() {
				@Override
				protected void addChildren() {
					edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.alice.ast.Expression > menuFillIn = new edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.alice.ast.Expression >( parameterName ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							org.alice.ide.IDE.getSingleton().addFillIns( blank, parameter.getDesiredValueType() );
						}
					};
					this.addFillIn( menuFillIn );
				}
			};
		} else {
			blank = createExpressionBlank( parameter.getValueType(), null );
		}
		if( p != null ) {
			//pass
		} else {
			p = new java.awt.Point( 0, viewController.getHeight() );
		}
		blank.showPopupMenu( viewController.getAwtComponent(), p.x, p.y, taskObserver );
	}
	public void unsetPreviousExpressionAndDropStatement() {
		this.previousExpression = null;
		this.dropParent = null;
		this.dropIndex = -1;
	}

	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForSceneEditor() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	//todo: remove?
	public final edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVirtualMachineForRuntimeProgram() {
		return this.vmForRuntimeProgram;
	}
	//todo: remove?
	public final edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVirtualMachineForSceneEditor() {
		return this.vmForSceneEditor;
	}

	public void revert() {
		java.io.File file = this.getFile();
		if( file != null ) {
			this.loadProjectFrom( file );
		} else {
			this.showMessageDialog( "You must have a project open in order to revert.", "Revert", edu.cmu.cs.dennisc.croquet.MessageType.INFORMATION );
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getFocusedCode() {
		return this.getEditorsTabSelectionState().getValue();
		//		org.alice.ide.codeeditor.CodeEditor codeEditor = (org.alice.ide.codeeditor.CodeEditor)this.getEditorsTabSelectionState().getCurrentTabStateOperation().getSingletonView();
		//		if( codeEditor != null ) {
		//			return codeEditor.getCode();
		//		} else {
		//			return null;
		//		}
	}
	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
		this.getEditorsTabSelectionState().edit( nextFocusedCode, false );
	}
	//
	//	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
	//		if( nextFocusedCode == this.focusedCode ) {
	//			//pass
	//		} else {
	//			edu.cmu.cs.dennisc.alice.ast.AbstractCode previousCode = this.focusedCode;
	//			for( CodeInFocusObserver codeInFocusObserver : this.codeInFocusObservers ) {
	//				codeInFocusObserver.focusedCodeChanging( previousCode, nextFocusedCode );
	//			}
	//			this.focusedCode = nextFocusedCode;
	//			for( CodeInFocusObserver codeInFocusObserver : this.codeInFocusObservers ) {
	//				codeInFocusObserver.focusedCodeChanged( previousCode, nextFocusedCode );
	//			}
	//
	//		}
	//	}

	//	@Deprecated
	//	public edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldSelection() {
	//		return this.fieldSelectionState.getValue();
	//	}
	//
	//	@Deprecated
	//	public void setFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection ) {
	////		edu.cmu.cs.dennisc.alice.ast.AbstractField previousField = this.getFieldSelection();
	////		for( FieldSelectionObserver fieldSelectionObserver : this.fieldSelectionObservers ) {
	////			fieldSelectionObserver.fieldSelectionChanging( previousField, fieldSelection );
	////		}
	//		this.fieldSelectionState.setValue( fieldSelection );
	////		for( FieldSelectionObserver fieldSelectionObserver : this.fieldSelectionObservers ) {
	////			fieldSelectionObserver.fieldSelectionChanged( previousField, fieldSelection );
	////		}
	//	}

	//	public edu.cmu.cs.dennisc.alice.ast.AbstractTransient getTransientSelection() {
	//		return this.transientSelection;
	//	}
	//
	//	public void setTransientSelection( edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection ) {
	//		org.alice.ide.event.TransientSelectionEvent e = new org.alice.ide.event.TransientSelectionEvent( this, this.transientSelection, transientSelection );
	//		fireTransientSelectionChanging( e );
	//		this.transientSelection = transientSelection;
	//		fireTransientSelectionChanged( e );
	//	}

	@Override
	public void ensureProjectCodeUpToDate() {
		this.generateCodeForSceneSetUp();
	}

	private static final String GENERATED_CODE_WARNING = "DO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT\n\nThis code is automatically generated.  Any work you perform in this method will be overwritten.\n\nDO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT";

	private void generateCodeForSceneSetUp() {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getPerformEditorGeneratedSetUpMethod();
		edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty = methodDeclaredInAlice.body.getValue().statements;
		bodyStatementsProperty.clear();
		bodyStatementsProperty.add( new edu.cmu.cs.dennisc.alice.ast.Comment( GENERATED_CODE_WARNING ) );
		this.sceneEditor.generateCodeForSetUp( bodyStatementsProperty );
	}

	@Deprecated
	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getProgramType() {
		edu.cmu.cs.dennisc.alice.Project project = this.getProject();
		if( project != null ) {
			return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType();
		} else {
			return null;
		}
	}
	@Deprecated
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = getProgramType();
		return getSceneFieldFromProgramType( programType );
	}

	@Deprecated
	protected static edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneFieldFromProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		if( programType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programAliceType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)programType;
			if( programAliceType.fields.size() > 0 ) {
				return programAliceType.fields.get( 0 );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	@Deprecated
	protected static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneTypeFromProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		if( programType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = getSceneFieldFromProgramType( programType );
			return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		} else {
			return null;
		}
	}

	@Deprecated
	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneType() {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = getSceneField();
		if( sceneField != null ) {
			return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		} else {
			return null;
		}
	}

	public String getInstanceTextForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, boolean isOutOfScopeTagDesired ) {
		String text;
		if( field != null ) {
			text = field.getName();
			edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
			if( focusedCode != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType scopeType = focusedCode.getDeclaringType();
				if( field.getValueType() == scopeType ) {
					text = "this (a.k.a. " + text + ")";
				} else if( field.getDeclaringType() == scopeType ) {
					if( this.isOmissionOfThisForFieldAccessesDesiredState.getValue() ) {
						//pass
					} else {
						text = "this." + text;
					}
				} else if( isOutOfScopeTagDesired ) {
					text = "out of scope: " + text;
				}
			}
		} else {
			text = null;
		}
		return text;
	}

	public edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) /*throws OutOfScopeException*/{
		edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
		if( focusedCode != null && field != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType focusedCodeDeclaringType = focusedCode.getDeclaringType();
			if( focusedCodeDeclaringType != null ) {
				edu.cmu.cs.dennisc.alice.ast.ThisExpression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
				if( focusedCodeDeclaringType.equals( field.getValueType() ) ) {
					return thisExpression;
				} else if( focusedCodeDeclaringType.equals( field.getDeclaringType() ) ) {
					return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public final edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression() /*throws OutOfScopeException*/{
		return createInstanceExpression( this.getFieldSelectionState().getValue() );
	}

	public boolean isFieldInScope( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return createInstanceExpression( field ) != null;
	}
	public final boolean isSelectedFieldInScope() {
		return isFieldInScope( this.getFieldSelectionState().getValue() );
	}

	@Override
	public boolean isDragInProgress() {
		return this.isDragInProgress;
	}
	@Override
	public void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
		this.currentDropReceptorComponent = null;
	}

	private java.util.Map< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node > mapUUIDToNode = new java.util.HashMap< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node >();

	private static final java.awt.Color DEFAULT_PROCEDURE_COLOR = new java.awt.Color( 0xb2b7d9 );
	private static final java.awt.Color DEFAULT_FUNCTION_COLOR = new java.awt.Color( 0xb0c9a4 );
	private static final java.awt.Color DEFAULT_CONSTRUCTOR_COLOR = new java.awt.Color( 0xadc0ab );
	private static final java.awt.Color DEFAULT_FIELD_COLOR = new java.awt.Color( 230, 230, 210 );

	public java.awt.Color getProcedureColor() {
		return DEFAULT_PROCEDURE_COLOR;
	}
	public java.awt.Color getFunctionColor() {
		return DEFAULT_FUNCTION_COLOR;
	}
	public java.awt.Color getConstructorColor() {
		return DEFAULT_CONSTRUCTOR_COLOR;
	}
	public java.awt.Color getFieldColor() {
		return DEFAULT_FIELD_COLOR;
	}
	public java.awt.Color getLocalColor() {
		return getFieldColor();
	}
	public java.awt.Color getParameterColor() {
		return getFieldColor();
	}

	public java.awt.Paint getPaintFor( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls, int x, int y, int width, int height ) {
		java.awt.Color color = this.getColorFor( cls );
		if( edu.cmu.cs.dennisc.alice.ast.Comment.class.isAssignableFrom( cls ) ) {
			return color;
		} else {
			if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.DoTogether.class, edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether.class, edu.cmu.cs.dennisc.alice.ast.DoInThread.class ) ) {
				java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 0.85 );
				java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 1.0, 1.15 );
				return new java.awt.GradientPaint( x, y, colorA, x + 200, y, colorB );
			} else {
				return color;
				//return new java.awt.GradientPaint( x, y, colorB, x, y + 64, color );
			}
		}
	}
	public java.awt.Color getColorFor( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls ) {
		if( edu.cmu.cs.dennisc.alice.ast.Statement.class.isAssignableFrom( cls ) ) {
			if( edu.cmu.cs.dennisc.alice.ast.Comment.class.isAssignableFrom( cls ) ) {
				return edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 245 );
			} else {
				return new java.awt.Color( 0xd3d7f0 );
			}
		} else if( edu.cmu.cs.dennisc.alice.ast.Expression.class.isAssignableFrom( cls ) ) {
			if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) ) {
				return new java.awt.Color( 0xd3e7c7 );
			} else if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.InfixExpression.class, edu.cmu.cs.dennisc.alice.ast.LogicalComplement.class,
					edu.cmu.cs.dennisc.alice.ast.StringConcatenation.class ) ) {
				return new java.awt.Color( 0xDEEBD3 );
			} else if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class, edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation.class ) ) {
				return new java.awt.Color( 0xbdcfb3 );
			} else if( edu.cmu.cs.dennisc.alice.ast.ResourceExpression.class.isAssignableFrom( cls ) ) {
				return new java.awt.Color( 0xffffff );
			} else {
				if( edu.cmu.cs.dennisc.alice.ast.NullLiteral.class.isAssignableFrom( cls ) ) {
					return java.awt.Color.RED;
				} else {
					return new java.awt.Color( 0xfdf6c0 );
				}
			}
		} else {
			return java.awt.Color.BLUE;
		}
	}
	public java.awt.Color getColorFor( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		if( node != null ) {
			return this.getColorFor( node.getClass() );
		} else {
			return java.awt.Color.RED;
		}
	}

	public java.awt.Color getCommentForegroundColor() {
		return new java.awt.Color( 0, 100, 0 );
	}

	public java.awt.Color getCodeDeclaredInAliceColor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
			if( methodDeclaredInAlice.isProcedure() ) {
				return getProcedureColor();
			} else {
				return getFunctionColor();
			}
		} else if( code instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			return getConstructorColor();
		} else {
			return java.awt.Color.GRAY;
		}
	}
	private static <E extends edu.cmu.cs.dennisc.alice.ast.Node> E getAncestor( edu.cmu.cs.dennisc.alice.ast.Node node, Class< E > cls ) {
		edu.cmu.cs.dennisc.alice.ast.Node ancestor = node.getParent();
		while( ancestor != null ) {
			if( cls.isAssignableFrom( ancestor.getClass() ) ) {
				break;
			} else {
				ancestor = ancestor.getParent();
			}
		}
		return (E)ancestor;
	}

	protected void ensureNodeVisible( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode = getAncestor( node, edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
		if( nextFocusedCode != null ) {
			this.setFocusedCode( nextFocusedCode );
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.Node getNodeForUUID( java.util.UUID uuid ) {
		edu.cmu.cs.dennisc.alice.ast.Node rv = mapUUIDToNode.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getProgramType();
			type.crawl( new edu.cmu.cs.dennisc.pattern.Crawler() {
				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)crawlable;
						mapUUIDToNode.put( node.getUUID(), node );
					}
				}
			}, true );
			rv = mapUUIDToNode.get( uuid );
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.croquet.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		return null;
	}

	public java.awt.Component getComponentForNode( edu.cmu.cs.dennisc.alice.ast.Node node, boolean scrollToVisible ) {
		if( node instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
			final edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)node;
			ensureNodeVisible( node );
			org.alice.ide.common.AbstractStatementPane rv = getCodeFactory().lookup( statement );
			if( scrollToVisible ) {
				//todo: use ScrollUtilities.scrollRectToVisible
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						org.alice.ide.common.AbstractStatementPane pane = getCodeFactory().lookup( statement );
						if( pane != null ) {
							pane.scrollToVisible();
						}
					}
				} );
			}
			return rv.getAwtComponent();
		} else {
			return null;
		}
	}
	public java.awt.Component getComponentForNode( java.util.UUID uuid, boolean scrollToVisible ) {
		return getComponentForNode( getNodeForUUID( uuid ), scrollToVisible );
	}
	public java.awt.Component getComponentForNode( java.util.UUID uuid ) {
		return getComponentForNode( uuid, false );
	}

	//todo: remove
	private String getSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}
	public java.io.File getMyTypesDirectory() {
		return edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyTypesDirectory( this.getSubPath() );
	}

	public boolean isInstanceLineDesired() {
		return true;
	}

	public String getTextForThis() {
		return edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( edu.cmu.cs.dennisc.alice.ast.ThisExpression.class, "edu.cmu.cs.dennisc.alice.ast.Templates" );
	}

	public boolean isDeclareFieldOfPredeterminedTypeSupported( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType ) {
		return true;
	}

	//	def _isFieldNameFree( self, name ):
	//		sceneType = self.getSceneType()
	//		if sceneType:
	//			for field in sceneType.fields.iterator():
	//				if field.getName() == name:
	//					return False
	//		return True 
	//
	//	def _getAvailableFieldName( self, superClassBaseName ):
	//		name = superClassBaseName[ 0 ].lower() + superClassBaseName[ 1: ]
	//		rv = name
	//		i = 2
	//		while not self._isFieldNameFree( rv ):
	//			rv = name + `i`
	//			i += 1
	//		return rv

	private static String getAvailableFieldName( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, String baseName ) {
		org.alice.ide.name.validators.FieldNameValidator validator = new org.alice.ide.name.validators.FieldNameValidator( declaringType );

		if( validator.isNameValid( baseName ) ) {
			//pass
		} else {
			baseName = "unnamed";
			assert validator.isNameValid( baseName );
		}

		int i = 2;
		String rv = baseName;
		while( validator.getExplanationIfOkButtonShouldBeDisabled( rv ) != null ) {
			rv = baseName + i;
			i++;
		}
		return rv;
	}
	public String getPotentialInstanceNameFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType ) {
		if( valueType != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
			if( typeInJava != null ) {
				if( this.isDefaultFieldNameGenerationDesired() ) {
					String typeName = typeInJava.getName();
					if( typeName != null && typeName.length() > 0 ) {
						StringBuffer sb = new StringBuffer();
						sb.append( Character.toLowerCase( typeName.charAt( 0 ) ) );
						sb.append( typeName.substring( 1 ) );
						return IDE.getAvailableFieldName( declaringType, sb.toString() );
					}
				}
			}
		}
		return "";
	}

	public abstract boolean isInstanceCreationAllowableFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice );
	public abstract edu.cmu.cs.dennisc.animation.Program createRuntimeProgram( edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType, int frameRate );

	public java.util.Set< org.alice.virtualmachine.Resource > getResources() {
		edu.cmu.cs.dennisc.alice.Project project = this.getProject();
		if( project != null ) {
			return project.getResources();
		} else {
			return null;
		}
	}

	private java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > > > nameClsPairsForRelationalFillIns = null;

	public java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > > > updateNameClsPairsForRelationalFillIns( java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > > > rv ) {
		return rv;
	}
	public Iterable< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > > > getNameClsPairsForRelationalFillIns() {
		if( this.nameClsPairsForRelationalFillIns != null ) {
			//pass
		} else {
			this.nameClsPairsForRelationalFillIns = new java.util.LinkedList< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? >> >();
			this.updateNameClsPairsForRelationalFillIns( this.nameClsPairsForRelationalFillIns );
		}
		return this.nameClsPairsForRelationalFillIns;
	}
	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getSceneField();
				if( sceneField != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
					setFocusedCode( runMethod );
				}
				final int N = fieldSelectionState.getItemCount();
				fieldSelectionState.setValue( fieldSelectionState.getItemAt( N - 1 ) );
			}
		} );
		//todo: find a better solution to concurrent modification exception
		//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
		//			public void run() {
		//				edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getSceneField();
		//				if( sceneField != null ) {
		//					edu.cmu.cs.dennisc.alice.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
		//					IDE.this.setFocusedCode( runMethod );
		//					java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneField.getValueType().getDeclaredFields();
		//					final int N = fields.size();
		//					int i = N - 1;
		//					while( i >= 0 ) {
		//						edu.cmu.cs.dennisc.alice.ast.AbstractField field = fields.get( i );
		//						if( field.getValueType().isArray() ) {
		//							//pass
		//						} else {
		//							IDE.this.getFieldSelectionState().setValue( field );
		//							break;
		//						}
		//						i--;
		//					}
		//				}
		//			}
		//		} );
	}
}

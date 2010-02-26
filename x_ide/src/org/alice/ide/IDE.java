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
package org.alice.ide;

import org.alice.ide.openprojectpane.TabContentPane;

import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine;
import edu.cmu.cs.dennisc.animation.Program;

class MenuItem extends javax.swing.JMenuItem {
	private java.io.File file;
	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			if( file != null ) {
				IDE.getSingleton().loadProjectFrom( MenuItem.this.file );
			}
		}
	};

	public MenuItem( int i, java.io.File file ) {
		this.file = file;
		this.setText( i + ") " + this.file.getAbsolutePath() );
		this.addActionListener( this.actionListener );
	}
	//	@Override
	//	public void addNotify() {
	//		this.addActionListener( this.actionListener );
	//		super.addNotify();
	//	}
	//	
	//	@Override
	//	public void removeNotify() {
	//		this.removeActionListener( this.actionListener );
	//		super.removeNotify();
	//	}
}

class RecentProjectsMenu extends javax.swing.JMenu {
	//	private edu.cmu.cs.dennisc.preference.event.PreferenceListener< org.alice.ide.preferences.PathsPreference > preferenceListener = new edu.cmu.cs.dennisc.preference.event.PreferenceListener< org.alice.ide.preferences.PathsPreference >() {
	//		public void valueChanging( edu.cmu.cs.dennisc.preference.event.PreferenceEvent< org.alice.ide.preferences.PathsPreference > e ) {
	//		}
	//		public void valueChanged( edu.cmu.cs.dennisc.preference.event.PreferenceEvent< org.alice.ide.preferences.PathsPreference > e ) {
	//		}
	//	};
	public RecentProjectsMenu() {
		this.setText( "Open Recent Projects" );
		this.addMenuListener( new javax.swing.event.MenuListener() {
			public void menuSelected( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuSelected( e );
			}
			public void menuDeselected( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuDeselected( e );
			}
			public void menuCanceled( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuCanceled( e );
			}
		} );
	}

	private void handleMenuSelected( javax.swing.event.MenuEvent e ) {
		this.removeAll();
		org.alice.ide.preferences.PathsPreference pathsPreference = org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths;
		int i = 1;
		for( String pathname : pathsPreference.getValue() ) {
			java.io.File file = new java.io.File( pathname );
			if( file.exists() ) {
				this.add( new MenuItem( i, file ) );
				i++;
			}
		}
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuSelected:", e );
	}
	private void handleMenuDeselected( javax.swing.event.MenuEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuDeselected:", e );
	}
	private void handleMenuCanceled( javax.swing.event.MenuEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuCanceled:", e );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends edu.cmu.cs.dennisc.zoot.ZFrame {
	public static final java.util.UUID PROJECT_GROUP = java.util.UUID.fromString( "a89d2513-6d9a-4378-a08b-4d773618244d" );
	public static final java.util.UUID PREFERENCES_GROUP = java.util.UUID.fromString( "c090cda0-4a77-4e2c-a839-faf28c98c10c" );
	public static final java.util.UUID IO_GROUP = java.util.UUID.fromString( "669018cd-2097-4568-9ce3-38cd102f6a2f" );
	public static final java.util.UUID INTERFACE_GROUP = java.util.UUID.fromString( "9d0f4665-a40e-4e0c-9139-6f54f288c016" );
	
	private static org.alice.ide.issue.ExceptionHandler exceptionHandler;
	private static IDE singleton;
	private static java.util.HashSet< String > performSceneEditorGeneratedSetUpMethodNameSet = new java.util.HashSet< String >();

	//public static String IS_PROJECT_CHANGED_KEY = "IS_PROJECT_AFFECTED_KEY";
	protected static final String GENERATED_SET_UP_METHOD_NAME = "performGeneratedSetUp";
	static {
		IDE.exceptionHandler = new org.alice.ide.issue.ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
		performSceneEditorGeneratedSetUpMethodNameSet.add( "performSceneEditorGeneratedSetUp" );
		performSceneEditorGeneratedSetUpMethodNameSet.add( "performEditorGeneratedSetUp" );
		performSceneEditorGeneratedSetUpMethodNameSet.add( GENERATED_SET_UP_METHOD_NAME );
	}

	public static IDE getSingleton() {
		return IDE.singleton;
	}

	public edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getPerformEditorGeneratedSetUpMethod() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = this.getSceneType();
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice rv;

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
		;
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

	private org.alice.ide.memberseditor.Factory templatesFactory = this.createTemplatesFactory();

	public org.alice.ide.memberseditor.Factory getTemplatesFactory() {
		return this.templatesFactory;
	}
	protected org.alice.ide.memberseditor.Factory createTemplatesFactory() {
		return new org.alice.ide.memberseditor.Factory();
	}

	private org.alice.ide.codeeditor.Factory codeFactory = this.createCodeFactory();

	public org.alice.ide.codeeditor.Factory getCodeFactory() {
		return this.codeFactory;
	}
	protected org.alice.ide.codeeditor.Factory createCodeFactory() {
		return new org.alice.ide.codeeditor.Factory();
	}

	private org.alice.ide.preview.Factory previewFactory = this.createPreviewFactory();

	public org.alice.ide.preview.Factory getPreviewFactory() {
		return this.previewFactory;
	}
	protected org.alice.ide.preview.Factory createPreviewFactory() {
		return new org.alice.ide.preview.Factory();
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
		java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > aliceTypes = this.addAliceTypes( new java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType >() );
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

	private edu.cmu.cs.dennisc.croquet.swing.ConcealedBin concealedBin = new edu.cmu.cs.dennisc.croquet.swing.ConcealedBin();
	private org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor;
	private org.alice.ide.gallerybrowser.AbstractGalleryBrowser galleryBrowser;
	private org.alice.ide.memberseditor.MembersEditor membersEditor;
	private org.alice.ide.listenerseditor.ListenersEditor listenersEditor;
	private org.alice.ide.editorstabbedpane.EditorsTabbedPane editorsTabbedPane;
	private org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane;

	//	private zoot.ZLabel feedback = zoot.ZLabel.acquire();

	public java.awt.Component getOverrideComponent( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		return null;
	}
	public boolean isDropDownDesiredFor( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		return (expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression || expression instanceof edu.cmu.cs.dennisc.alice.ast.ResourceExpression ) == false;
	}
	public org.alice.ide.common.TypeComponent getComponentFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		//todo:
		return new org.alice.ide.common.TypeComponent( type );
	}
	public String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return null;
	}
	public javax.swing.Icon getIconFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new org.alice.ide.common.TypeIcon( type );
	}

	private javax.swing.ComboBoxModel typeComboBoxModel;

	protected java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > addJavaTypes( java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( String.class ) );
		return rv;
	}

	protected java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > addAliceTypes( java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = this.getSceneType();
		if( sceneType != null ) {
			rv.add( sceneType );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType valueType = field.getValueType();
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
	public javax.swing.ComboBoxModel getTypeComboBoxModel() {
		if( this.typeComboBoxModel != null ) {
			//pass
		} else {
			java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > types = new java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType >();
			this.addJavaTypes( types );
			this.addAliceTypes( types );
			typeComboBoxModel = new javax.swing.DefaultComboBoxModel( types );
		}
		return this.typeComboBoxModel;
	}
	protected abstract org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor();
	public abstract java.io.File getGalleryRootDirectory();
	protected abstract org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser( java.io.File galleryRootDirectory );
	protected org.alice.ide.listenerseditor.ListenersEditor createListenersEditor() {
		return new org.alice.ide.listenerseditor.ListenersEditor();
	}
	protected org.alice.ide.memberseditor.MembersEditor createClassMembersEditor() {
		return new org.alice.ide.memberseditor.MembersEditor();
	}
	protected org.alice.ide.editorstabbedpane.EditorsTabbedPane createEditorsTabbedPane() {
		return new org.alice.ide.editorstabbedpane.EditorsTabbedPane();
	}
	protected org.alice.ide.ubiquitouspane.UbiquitousPane createUbiquitousPane() {
		return new org.alice.ide.ubiquitouspane.UbiquitousPane();
	}

	private java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > expressionFillerInners;

	private static <E> E createBooleanOperation( Class< E > cls, Boolean defaultInitialValue ) {
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
		Boolean initialValue = userPreferences.getBoolean( cls.getSimpleName(), defaultInitialValue );
		Class< ? >[] parameterClses = { Boolean.class };
		Object[] arguments = { initialValue };
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cls, parameterClses, arguments );
	}
	private static void preservePreference( org.alice.ide.operations.AbstractBooleanStateOperation operation ) {
		if( operation != null ) {
			Class< ? > cls = operation.getClass();
			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( cls );
			userPreferences.putBoolean( cls.getSimpleName(), operation.getState() );
		}
	}

	protected boolean isDefaultFieldNameGenerationDesiredByDefault() {
		return false;
	}

	private org.alice.ide.operations.window.IsMemoryUsageShowingOperation isMemoryUsageShowingOperation = new org.alice.ide.operations.window.IsMemoryUsageShowingOperation();
	private org.alice.ide.operations.window.IsHistoryShowingOperation isHistoryShowingOperation = new org.alice.ide.operations.window.IsHistoryShowingOperation();

	private org.alice.ide.operations.window.IsSceneEditorExpandedOperation isSceneEditorExpandedOperation = new org.alice.ide.operations.window.IsSceneEditorExpandedOperation( false );
	private org.alice.ide.operations.window.IsTypeFeedbackDesiredOperation isExpressionTypeFeedbackDesiredOperation = createBooleanOperation( org.alice.ide.operations.window.IsTypeFeedbackDesiredOperation.class, true );
	private org.alice.ide.operations.window.IsOmissionOfThisForFieldAccessesDesiredOperation isOmissionOfThisForFieldAccessesDesiredOperation = createBooleanOperation( org.alice.ide.operations.window.IsOmissionOfThisForFieldAccessesDesiredOperation.class,
			false );
	private org.alice.ide.operations.window.IsEmphasizingClassesOperation isEmphasizingClassesOperation = createBooleanOperation( org.alice.ide.operations.window.IsEmphasizingClassesOperation.class, true );
	private org.alice.ide.operations.window.IsDefaultFieldNameGenerationDesiredOperation isDefaultFieldNameGenerationDesiredOperation = createBooleanOperation( org.alice.ide.operations.window.IsDefaultFieldNameGenerationDesiredOperation.class, this
			.isDefaultFieldNameGenerationDesiredByDefault() );

	public org.alice.ide.operations.window.IsSceneEditorExpandedOperation getIsSceneEditorExpandedOperation() {
		return this.isSceneEditorExpandedOperation;
	}
	public boolean isExpressionTypeFeedbackDesired() {
		return this.isExpressionTypeFeedbackDesiredOperation.getButtonModel().isSelected();
	}
	public void setExpressionTypeFeedbackDesired( boolean isExpressionTypeFeedbackDesired ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			codeEditor.refresh();
		}
	}
	public boolean isEmphasizingClasses() {
		return this.isEmphasizingClassesOperation.getButtonModel().isSelected();
	}
	public void setEmphasizingClasses( boolean isEmphasizingClasses ) {
		this.editorsTabbedPane.setEmphasizingClasses( isEmphasizingClasses );
		this.membersEditor.setEmphasizingClasses( isEmphasizingClasses );
	}
	public boolean isOmittingThisFieldAccesses() {
		return this.isOmissionOfThisForFieldAccessesDesiredOperation.getButtonModel().isSelected();
	}
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.editorsTabbedPane.setOmittingThisFieldAccesses( isOmittingThisFieldAccesses );
		this.membersEditor.setOmittingThisFieldAccesses( isOmittingThisFieldAccesses );
		this.sceneEditor.setOmittingThisFieldAccesses( isOmittingThisFieldAccesses );
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return this.isDefaultFieldNameGenerationDesiredOperation.getButtonModel().isSelected();
	}

	private int rootDividerLocation = 320;
	private int leftDividerLocation = 240;

	private javax.swing.JSplitPane root;
	private javax.swing.JSplitPane left;

	class RightPane extends edu.cmu.cs.dennisc.croquet.swing.GridBagPane {
		public RightPane() {
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			this.add( IDE.this.ubiquitousPane, gbc );
			//			this.add( IDE.this.declarationsUIResource, gbc );
			gbc.weighty = 1.0;
			this.add( IDE.this.editorsTabbedPane, gbc );
		}
	}

	private javax.swing.JComponent rightPane;

	protected javax.swing.JComponent createRightPane() {
		return new RightPane();
	}
	protected javax.swing.JComponent getRightPane() {
		return this.rightPane;
	}

	protected javax.swing.JSplitPane getRootSplitPane() {
		return this.root;
	}
	protected java.awt.Component createRoot() {
		this.root = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
		this.left = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT );
		this.rightPane = this.createRightPane();
		return this.root;
	}
	public void setSceneEditorExpanded( boolean isSceneEditorExpanded ) {
		if( isSceneEditorExpanded ) {
			this.left.setResizeWeight( 1.0 );
			this.rootDividerLocation = this.root.getDividerLocation();
			this.leftDividerLocation = this.left.getDividerLocation();
			this.root.setLeftComponent( this.left );
			this.left.setTopComponent( this.sceneEditor );
			this.left.setBottomComponent( this.galleryBrowser );
			this.root.setRightComponent( null );
			this.root.setDividerSize( 0 );
			this.left.setDividerLocation( this.getHeight() - 300 );
		} else {
			this.left.setResizeWeight( 0.0 );
			this.root.setLeftComponent( this.left );
			this.root.setRightComponent( this.rightPane );
			this.root.setDividerLocation( this.rootDividerLocation );
			this.left.setDividerLocation( this.leftDividerLocation );
			this.left.setTopComponent( this.sceneEditor );
			this.left.setBottomComponent( this.membersEditor );
			//			if( this.right.getComponentCount() == 0 ) {
			//				this.right.add( this.ubiquitousPane, java.awt.BorderLayout.SOUTH );
			//				this.right.add( this.editorsTabbedPane, java.awt.BorderLayout.CENTER );
			//				this.right.add( this.declarationsUIResource, java.awt.BorderLayout.NORTH );
			//			}
			this.root.setDividerSize( this.left.getDividerSize() );
		}
		this.sceneEditor.handleExpandContractChange( isSceneEditorExpanded );
	}

	protected org.alice.ide.ubiquitouspane.UbiquitousPane getUbiquitousPane() {
		return this.ubiquitousPane;
	}
	protected org.alice.ide.editorstabbedpane.EditorsTabbedPane getEditorsTabbedPane() {
		return this.editorsTabbedPane;
	}
	protected org.alice.ide.memberseditor.MembersEditor getMembersEditor() {
		return this.membersEditor;
	}
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser getGalleryBrowser() {
		return this.galleryBrowser;
	}
	public org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor() {
		return this.sceneEditor;
	}

	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		assert IDE.singleton == null;
		IDE.singleton = this;
		this.promptForLicenseAgreements();

		edu.cmu.cs.dennisc.history.HistoryManager.get( IDE.PROJECT_GROUP ).addHistoryListener( new edu.cmu.cs.dennisc.history.event.HistoryListener() {
			public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
			}
			public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
				if( e.getEdit() != null ) {
					IDE.this.markChanged();
				}
			}
		} );

		this.runOperation.setEnabled( false );

		this.sceneEditor = this.createSceneEditor();
		this.galleryBrowser = this.createGalleryBrowser( this.getGalleryRootDirectory() );
		this.membersEditor = this.createClassMembersEditor();
		this.listenersEditor = this.createListenersEditor();
		this.editorsTabbedPane = this.createEditorsTabbedPane();
		this.ubiquitousPane = this.createUbiquitousPane();

		this.addIDEListener( this.sceneEditor );
		this.addIDEListener( this.membersEditor );
		this.addIDEListener( this.listenersEditor );
		this.addIDEListener( this.editorsTabbedPane );

		this.getContentPane().setLayout( new java.awt.BorderLayout() );
		this.getContentPane().add( this.createRoot(), java.awt.BorderLayout.CENTER );
		//		this.getContentPane().add( this.feedback, java.awt.BorderLayout.SOUTH );
		this.getContentPane().add( this.concealedBin, java.awt.BorderLayout.EAST );
		this.setSceneEditorExpanded( false );

		//edu.cmu.cs.dennisc.swing.InputPane.setDefaultOwnerFrame( this );
		this.vmForRuntimeProgram = createVirtualMachineForRuntimeProgram();
		this.vmForSceneEditor = createVirtualMachineForSceneEditor();

		getContentPane().addMouseWheelListener( new edu.cmu.cs.dennisc.swing.plaf.metal.FontMouseWheelAdapter() );

		//this.setLocale( new java.util.Locale( "en", "US", "java" ) );
		//javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "en", "US", "java" ) );

		this.expressionFillerInners = this.addExpressionFillerInners( new java.util.LinkedList< org.alice.ide.cascade.fillerinners.ExpressionFillerInner >() );
		javax.swing.JMenuBar menuBar = this.createMenuBar();
		this.setJMenuBar( menuBar );
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

	public void addToConcealedBin( java.awt.Component component ) {
		this.concealedBin.add( component );
		this.concealedBin.revalidate();
	}

	//	private java.util.UUID ideUndoManagerKey = java.util.UUID.randomUUID();
	//	private java.util.UUID sceneEditorUndoManagerKey = java.util.UUID.randomUUID();
	//	private java.util.UUID codeEditorUndoManagerKey = java.util.UUID.randomUUID();
	//	private java.util.UUID uncertainUndoManagerKey = java.util.UUID.randomUUID();
	//	public java.util.UUID getIDEUndoManagerKey() {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ideUndoManagerKey:", this.ideUndoManagerKey );
	//		return this.ideUndoManagerKey;
	//	}
	//	public java.util.UUID getSceneEditorUndoManagerKey() {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: sceneEditorUndoManagerKey:", this.sceneEditorUndoManagerKey );
	//		return this.sceneEditorUndoManagerKey;
	//	}
	//	public java.util.UUID getCodeEditorUndoManagerKey() {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: codeEditorUndoManagerKey:", this.codeEditorUndoManagerKey );
	//		return this.codeEditorUndoManagerKey;
	//	}
	//	public java.util.UUID getUncertainUndoManagerKey() {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: uncertainUndoManagerKey:", this.uncertainUndoManagerKey );
	//		return this.uncertainUndoManagerKey;
	//	}

	private edu.cmu.cs.dennisc.zoot.ActionOperation clearToProcedeWithChangedProjectOperation = new edu.cmu.cs.dennisc.zoot.AbstractActionOperation( IDE.IO_GROUP ) {
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			int option = javax.swing.JOptionPane.showConfirmDialog( IDE.this, "Your program has been modified.  Would you like to save it?", "Save changed project?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
			if( option == javax.swing.JOptionPane.YES_OPTION ) {
				edu.cmu.cs.dennisc.zoot.ActionContext saveActionContext = actionContext.perform( IDE.this.saveOperation, null, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
				if( saveActionContext.isCommitted() ) {
					actionContext.commit();
				} else {
					actionContext.cancel();
				}
			} else if( option == javax.swing.JOptionPane.NO_OPTION ) {
				actionContext.commit();
			} else {
				actionContext.cancel();
			}
		}
	};

	public edu.cmu.cs.dennisc.zoot.ActionOperation getClearToProcedeWithChangedProjectOperation() {
		return this.clearToProcedeWithChangedProjectOperation;
	}

	class SelectProjectOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
		private boolean isNew;

		public SelectProjectOperation( boolean isNew ) {
			super( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP );
			this.isNew = isNew;
		}
		public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			org.alice.ide.openprojectpane.OpenProjectPane openProjectPane = new org.alice.ide.openprojectpane.OpenProjectPane( IDE.this.getTemplatesTabContentPane() );
			openProjectPane.selectAppropriateTab( this.isNew );
			while( actionContext.isPending() ) {
				java.net.URI uri = openProjectPane.showInJDialog( IDE.this, "Open Project", true );
				if( uri != null ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( uri );
					java.io.File file = new java.io.File( uri );

					//todo: just load default project

					if( file != null ) {
						actionContext.put( org.alice.ide.operations.file.AbstractOpenProjectOperation.FILE_KEY, file );
						actionContext.commit();
					} else {
						if( IDE.this.getFile() == null ) {
							javax.swing.JOptionPane.showMessageDialog( IDE.this, "Please select a project to open." );
						} else {
							actionContext.cancel();
						}
					}
				} else {
					actionContext.cancel();
				}
			}
		}
	}

	private TabContentPane templatesPane;

	protected abstract TabContentPane createTemplatesPane();

	private TabContentPane getTemplatesTabContentPane() {
		if( this.templatesPane != null ) {
			//pass
		} else {
			this.templatesPane = this.createTemplatesPane();
		}
		return this.templatesPane;
	}

	private edu.cmu.cs.dennisc.zoot.ActionOperation selectNewProjectOperation = new SelectProjectOperation( true );
	private edu.cmu.cs.dennisc.zoot.ActionOperation selectOpenProjectOperation = new SelectProjectOperation( false );

	public edu.cmu.cs.dennisc.zoot.ActionOperation getSelectProjectToOpenOperation( boolean isNew ) {
		if( isNew ) {
			return this.selectNewProjectOperation;
		} else {
			return this.selectOpenProjectOperation;
		}
	}

	protected javax.swing.JMenuBar createMenuBar() {
		javax.swing.JMenuBar rv = new javax.swing.JMenuBar();

		RecentProjectsMenu recentProjectsMenu = new RecentProjectsMenu();

		javax.swing.JMenu fileMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "File", java.awt.event.KeyEvent.VK_F, this.newProjectOperation, edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, new org.alice.ide.operations.file.OpenProjectOperation(),
				edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, this.saveOperation, new org.alice.ide.operations.file.SaveAsProjectOperation(), edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, new org.alice.ide.operations.file.RevertProjectOperation(),
				edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, new org.alice.ide.operations.file.ExportVideoUploadToYouTubeOperation(), edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, this.exitOperation );

		fileMenu.add( recentProjectsMenu, 3 );

		javax.swing.JMenu editMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Edit", java.awt.event.KeyEvent.VK_E, new org.alice.ide.operations.edit.UndoOperation(), new org.alice.ide.operations.edit.RedoOperation(),
				edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR, new org.alice.ide.operations.edit.CutOperation(), new org.alice.ide.operations.edit.CopyOperation(), new org.alice.ide.operations.edit.PasteOperation() );

		javax.swing.JMenu projectMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Project", java.awt.event.KeyEvent.VK_P, new org.alice.ide.operations.project.ManageResourcesOperation() );
		javax.swing.JMenu runMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Run", java.awt.event.KeyEvent.VK_R, this.runOperation );

		class LocaleComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
			private java.util.Locale[] candidates = { 
					new java.util.Locale( "en", "US" ),
					new java.util.Locale( "en", "US", "java" ) };
			private int selectedIndex;

			public LocaleComboBoxModel() {
				this.selectedIndex = 0;
			}
			public Object getElementAt( int index ) {
				return this.candidates[ index ];
			}
			public int getSize() {
				return this.candidates.length;
			}
			public Object getSelectedItem() {
				if( 0 <= this.selectedIndex && this.selectedIndex < this.candidates.length ) {
					return this.candidates[ this.selectedIndex ];
				} else {
					return null;
				}
			}
			public void setSelectedItem( Object selectedItem ) {
				int index = -1;
				if( selectedItem != null ) {
					int i = 0;
					for( java.util.Locale locale : this.candidates ) {
						if( selectedItem.equals( locale ) ) {
							index = i;
							break;
						}
						i++;
					}
				}
				this.selectedIndex = index;
			}
		}

		class LocaleItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< java.util.Locale > {
			public LocaleItemSelectionOperation() {
				super( new LocaleComboBoxModel() );
			}
			@Override
			protected String getNameFor( int index, java.util.Locale locale ) {
				if( locale != null ) {
					String variant = locale.getVariant();
					if( variant != null && variant.length() > 0 ) {  //should not be null
						return variant;
					} else {
						return "alice";
					}
				} else {
					return "null";
				}
			}
			@Override
			protected void handleSelectionChange( java.util.Locale value ) {
				IDE.this.setLocale( value );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: support undo", this );
			}
		}

		java.util.List< edu.cmu.cs.dennisc.zoot.Operation > windowOperations = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();

		windowOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		windowOperations.add( this.isHistoryShowingOperation );
		windowOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		windowOperations.add( this.isEmphasizingClassesOperation );
		windowOperations.add( this.isOmissionOfThisForFieldAccessesDesiredOperation );
		windowOperations.add( this.isExpressionTypeFeedbackDesiredOperation );
		windowOperations.add( this.isDefaultFieldNameGenerationDesiredOperation );

		//		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
		//			//pass
		//		} else {
		//			windowOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		//			windowOperations.add( this.getPreferencesOperation() );
		//		}

		windowOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		windowOperations.add( this.isMemoryUsageShowingOperation );
		windowOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		windowOperations.add( this.isSceneEditorExpandedOperation );
	
		
		javax.swing.JMenu windowMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Window", java.awt.event.KeyEvent.VK_W, windowOperations );

		javax.swing.JMenu setLocaleMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Set Language", java.awt.event.KeyEvent.VK_L, new LocaleItemSelectionOperation() );
		windowMenu.add( setLocaleMenu, 0 );

		java.util.List< edu.cmu.cs.dennisc.zoot.Operation > helpOperations = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();
		helpOperations.add( new org.alice.ide.operations.help.HelpOperation() );
		helpOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );

		boolean isBogusExceptionGenerationDesired = "true".equals( System.getProperty( "org.alice.ide.IDE.isBogusExceptionGenerationDesired" ) );

		if( isBogusExceptionGenerationDesired ) {
			helpOperations.add( new org.alice.ide.operations.help.ThrowBogusExceptionOperation() );
			helpOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		}
		helpOperations.add( new org.alice.ide.operations.help.ReportBugOperation() );
		helpOperations.add( new org.alice.ide.operations.help.SuggestImprovementOperation() );
		helpOperations.add( new org.alice.ide.operations.help.RequestNewFeatureOperation() );
		helpOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		helpOperations.add( new org.alice.ide.operations.help.WarningOperation( true ) );
		helpOperations.add( new org.alice.ide.operations.help.DisplaySystemPropertiesOperation() );
		helpOperations.add( new org.alice.ide.operations.help.ReleaseNotesOperation() );

		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
			//pass
		} else {
			helpOperations.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
			helpOperations.add( this.getAboutOperation() );
		}

		javax.swing.JMenu helpMenu = edu.cmu.cs.dennisc.zoot.ZManager.createMenu( "Help", java.awt.event.KeyEvent.VK_H, helpOperations );
		rv.add( fileMenu );
		rv.add( editMenu );
		rv.add( projectMenu );
		rv.add( runMenu );
		rv.add( windowMenu );
		rv.add( helpMenu );
		return rv;
	}
	public boolean isJava() {
		return getLocale().getVariant().equals( "java" );
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
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
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
	public String getApplicationName() {
		return "Alice";
	}
	protected String getVersionText() {
		return edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText();
	}
	protected String getVersionAdornment() {
		return "BETA ";
	}
	protected StringBuffer updateTitlePrefix( StringBuffer rv ) {
		rv.append( this.getApplicationName() );
		rv.append( " " );
		rv.append( this.getVersionText() );
		//		rv.append( " " );
		//		rv.append( this.getVersionAdornment() ); 
		return rv;
	}
	protected StringBuffer updateTitle( StringBuffer rv ) {
		this.updateTitlePrefix( rv );
		if( this.file != null ) {
			rv.append( this.file.getAbsolutePath() );
			rv.append( " " );
		}
		if( this.isChanged() ) {
			rv.append( "*" );
		}
		return rv;
	}

	protected void updateTitle() {
		StringBuffer sb = new StringBuffer();
		this.updateTitle( sb );
		this.setTitle( sb.toString() );
	}

	//	private java.util.List< zoot.DropReceptor > dropReceptors = new java.util.LinkedList< zoot.DropReceptor >();

	protected org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus() {
		return (org.alice.ide.codeeditor.CodeEditor)this.editorsTabbedPane.getSelectedComponent();
	}

	private ComponentStencil stencil;
	private java.util.List< ? extends java.awt.Component > holes = null;
	private edu.cmu.cs.dennisc.zoot.ZDragComponent potentialDragSource;
	private java.awt.Component currentDropReceptorComponent;

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
						potentialDragSourceBounds = javax.swing.SwingUtilities.convertRectangle( IDE.this.potentialDragSource.getParent(), potentialDragSourceBounds, this );
					} else {
						potentialDragSourceBounds = null;
					}

					if( isFauxStencilDesired() ) {
						for( java.awt.Component component : IDE.this.holes ) {
							java.awt.Rectangle holeBounds = component.getBounds();
							holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent(), holeBounds, this );
							area.subtract( new java.awt.geom.Area( holeBounds ) );
						}

						if( potentialDragSourceBounds != null ) {
							area.subtract( new java.awt.geom.Area( potentialDragSourceBounds ) );
						}
						g2.fill( area );
					}

					g2.setStroke( THICK_STROKE );
					final int BUFFER = 6;
					for( java.awt.Component component : IDE.this.holes ) {
						java.awt.Rectangle holeBounds = component.getBounds();
						holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent(), holeBounds, this );
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

	public void showStencilOver( edu.cmu.cs.dennisc.zoot.ZDragComponent potentialDragSource, final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			this.holes = codeEditor.createListOfPotentialDropReceptors( type );
			this.potentialDragSource = potentialDragSource;
			//java.awt.Rectangle bounds = codeEditor.getBounds();
			//bounds = javax.swing.SwingUtilities.convertRectangle( codeEditor, bounds, layeredPane );
			//this.stencil.setBounds( bounds );
			javax.swing.JLayeredPane layeredPane = this.getLayeredPane();
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
		javax.swing.JLayeredPane layeredPane = this.getLayeredPane();
		if( this.stencil != null && this.stencil.getParent() == layeredPane ) {
			layeredPane.remove( this.stencil );
			layeredPane.repaint();
			this.holes = null;
			this.potentialDragSource = null;
		}
	}

	public void handleDragStarted( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		this.potentialDragSource = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
		this.sceneEditor.setRenderingEnabled( false );
	}
	public void handleDragEnteredDropReceptor( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		this.currentDropReceptorComponent = dragAndDropContext.getCurrentDropReceptor().getAWTComponent();
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragExitedDropReceptor( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		this.currentDropReceptorComponent = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragStopped( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		this.sceneEditor.setRenderingEnabled( true );
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

	private java.io.File file = null;
	private edu.cmu.cs.dennisc.alice.Project project = null;

	private edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection = null;

	private java.util.List< org.alice.ide.event.IDEListener > ideListeners = new java.util.LinkedList< org.alice.ide.event.IDEListener >();
	private org.alice.ide.event.IDEListener[] ideListenerArray = null;

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

	private edu.cmu.cs.dennisc.zoot.ActionOperation newProjectOperation = new org.alice.ide.operations.file.NewProjectOperation();
	private edu.cmu.cs.dennisc.zoot.ActionOperation runOperation = this.createRunOperation();
	private edu.cmu.cs.dennisc.zoot.ActionOperation exitOperation = this.createExitOperation();
	private edu.cmu.cs.dennisc.zoot.ActionOperation saveOperation = this.createSaveOperation();
	private edu.cmu.cs.dennisc.zoot.ActionOperation preferencesOperation = this.createPreferencesOperation();
	private edu.cmu.cs.dennisc.zoot.ActionOperation aboutOperation = this.createAboutOperation();

	protected edu.cmu.cs.dennisc.zoot.ActionOperation createRunOperation() {
		return new org.alice.ide.operations.run.RunOperation();
	}
	protected edu.cmu.cs.dennisc.zoot.ActionOperation createExitOperation() {
		return new org.alice.ide.operations.file.ExitOperation();
	}
	protected edu.cmu.cs.dennisc.zoot.ActionOperation createSaveOperation() {
		return new org.alice.ide.operations.file.SaveProjectOperation();
	}
	protected edu.cmu.cs.dennisc.zoot.ActionOperation createPreferencesOperation() {
		return new org.alice.ide.operations.preferences.PreferencesOperation();
	}
	protected abstract edu.cmu.cs.dennisc.zoot.ActionOperation createAboutOperation();

	public final edu.cmu.cs.dennisc.zoot.ActionOperation getRunOperation() {
		return this.runOperation;
	}
	public final edu.cmu.cs.dennisc.zoot.ActionOperation getExitOperation() {
		return this.exitOperation;
	}
	public final edu.cmu.cs.dennisc.zoot.ActionOperation getSaveOperation() {
		return this.saveOperation;
	}
	public final edu.cmu.cs.dennisc.zoot.ActionOperation getPreferencesOperation() {
		return this.preferencesOperation;
	}
	public final edu.cmu.cs.dennisc.zoot.ActionOperation getAboutOperation() {
		return this.aboutOperation;
	}

	public abstract void handleRun( edu.cmu.cs.dennisc.zoot.ActionContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType programType );
	public abstract void handlePreviewMethod( edu.cmu.cs.dennisc.zoot.ActionContext actionContext, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation );
	public final void handleRun( edu.cmu.cs.dennisc.zoot.ActionContext context ) {
		if( this.project != null ) {
			this.ensureProjectCodeUpToDate();
			this.handleRun( context, this.getSceneType() );
		} else {
			javax.swing.JOptionPane.showMessageDialog( this, "Please open a project first." );
		}
	}

	private boolean isDragInProgress = false;

	private edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus = null;

	public edu.cmu.cs.dennisc.alice.ast.Comment getCommentThatWantsFocus() {
		return this.commentThatWantsFocus;
	}
	public void setCommentThatWantsFocus( edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus ) {
		this.commentThatWantsFocus = commentThatWantsFocus;
	}

	private org.alice.ide.event.IDEListener[] getIDEListeners() {
		if( this.ideListenerArray != null ) {
			//pass
		} else {
			synchronized( this.ideListeners ) {
				this.ideListenerArray = new org.alice.ide.event.IDEListener[ this.ideListeners.size() ];
				this.ideListeners.toArray( this.ideListenerArray );
			}
		}
		return this.ideListenerArray;
	}
	protected abstract void promptForLicenseAgreements();

	public void performIfAppropriate( edu.cmu.cs.dennisc.zoot.ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( actionOperation, e, isCancelWorthwhile );
	}

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
		if( this.file != null ) {
			//pass
		} else {
			this.performIfAppropriate( this.newProjectOperation, null, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
		}
	}
	@Override
	protected void handleAbout( java.util.EventObject e ) {
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( this.getAboutOperation(), e, true );
	}
	@Override
	protected void handlePreferences( java.util.EventObject e ) {
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( this.getPreferencesOperation(), e, true );
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		preservePreference( this.isEmphasizingClassesOperation );
		preservePreference( this.isExpressionTypeFeedbackDesiredOperation );
		preservePreference( this.isOmissionOfThisForFieldAccessesDesiredOperation );
		preservePreference( this.isDefaultFieldNameGenerationDesiredOperation );
		this.performIfAppropriate( this.exitOperation, e, true );
	}
	//	protected abstract void handleWindowClosing();

	public java.util.List< ? extends edu.cmu.cs.dennisc.zoot.DropReceptor > createListOfPotentialDropReceptors( edu.cmu.cs.dennisc.zoot.ZDragComponent source ) {
		assert source != null;
		org.alice.ide.codeeditor.CodeEditor codeEditor = this.getCodeEditorInFocus();
		if( codeEditor != null ) {
			if( source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance ) {
				org.alice.ide.common.ExpressionLikeSubstance expressionLikeSubstance = (org.alice.ide.common.ExpressionLikeSubstance)source.getSubject();
				return codeEditor.createListOfPotentialDropReceptors( expressionLikeSubstance.getExpressionType() );
			} else {
				java.util.List< edu.cmu.cs.dennisc.zoot.DropReceptor > rv = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.DropReceptor >();
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
			return new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.DropReceptor >();
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

	protected void addExpressionBonusFillInsForType( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus = this.getFocusedCode();
		if( codeInFocus != null ) {

			//todo: fix
			type = this.getActualTypeForDesiredParameterType( type );

			edu.cmu.cs.dennisc.alice.ast.AbstractType selectedType = this.getTypeInScope();
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
			for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : codeInFocus.getParameters() ) {
				if( type.isAssignableFrom( parameter.getValueType() ) ) {
					//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
					this.addFillInAndPossiblyPartFills( blank, new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter ), parameter.getValueType(), type );
				}
			}
			for( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable : getVariables( codeInFocus ) ) {
				if( type.isAssignableFrom( variable.valueType.getValue() ) ) {
					//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
					this.addFillInAndPossiblyPartFills( blank, new edu.cmu.cs.dennisc.alice.ast.VariableAccess( variable ), variable.valueType.getValue(), type );
				}
			}
			for( edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant : getConstants( codeInFocus ) ) {
				if( type.isAssignableFrom( constant.valueType.getValue() ) ) {
					//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
					this.addFillInAndPossiblyPartFills( blank, new edu.cmu.cs.dennisc.alice.ast.ConstantAccess( constant ), constant.valueType.getValue(), type );
				}
			}
			//			if( isNecessary ) {
			//				//pass
			//			} else {
			//				blank.addSeparator();
			//			}
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
		blank.addFillIn( new org.alice.ide.cascade.StringConcatenationFillIn() );
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
				if( this.previousExpression.getType().isAssignableTo( type ) ) {
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
	public void createProjectFromBootstrap() {
		throw new RuntimeException( "todo" );
	}

	//	public void promptUserForStatement( java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
	//		throw new RuntimeException( "todo" );
	//	}

	private edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = null;

	private edu.cmu.cs.dennisc.cascade.Blank createExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression ) {
		this.previousExpression = prevExpression;
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
	public void promptUserForExpressions( edu.cmu.cs.dennisc.alice.ast.AbstractType[] types, boolean isArrayLengthDesired, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > taskObserver ) {
		edu.cmu.cs.dennisc.cascade.FillIn fillIn = createExpressionsFillIn( types, isArrayLengthDesired );
		fillIn.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
	public void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, java.awt.event.MouseEvent e,
			edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		edu.cmu.cs.dennisc.cascade.Blank blank = createExpressionBlank( type, prevExpression );
		blank.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
	public void promptUserForMore( final edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
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
		blank.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
	public void unsetPreviousExpression() {
		this.previousExpression = null;
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

	public void addIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.add( l );
			this.ideListenerArray = null;
		}
	}

	public void removeIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.remove( l );
			this.ideListenerArray = null;
		}
	}

	protected void fireLocaleChanging( org.alice.ide.event.LocaleEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.localeChanging( e );
		}
	}
	protected void fireLocaleChanged( org.alice.ide.event.LocaleEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.localeChanged( e );
		}
	}

	@Override
	public void setLocale( java.util.Locale locale ) {
		if( this.ideListeners != null ) {
			java.util.Locale prevLocale = this.getLocale();
			org.alice.ide.event.LocaleEvent e = new org.alice.ide.event.LocaleEvent( this, prevLocale, locale );
			fireLocaleChanging( e );
			super.setLocale( locale );

			//todo: remove
			javax.swing.JComponent.setDefaultLocale( locale );
			//

			fireLocaleChanged( e );
		} else {
			super.setLocale( locale );
		}
	}

	protected void fireProjectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.projectOpening( e );
		}
	}

	protected void fireProjectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.projectOpened( e );
		}
	}

	public edu.cmu.cs.dennisc.alice.Project getProject() {
		return this.project;
	}

	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		org.alice.ide.event.ProjectOpenEvent e = new org.alice.ide.event.ProjectOpenEvent( this, this.project, project );
		fireProjectOpening( e );
		this.project = project;
		this.runOperation.setEnabled( this.project != null );
		fireProjectOpened( e );
	}

	private void showMessageDialog( java.io.File file, boolean isValidZip ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "Unable to open project from file " );
		sb.append( edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( this.getApplicationName() );
		sb.append( " is able to open projects from files saved by " );
		sb.append( this.getApplicationName() );
		sb.append( ".\n\nLook for files with an " );
		sb.append( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION );
		sb.append( " extension." );
		javax.swing.JOptionPane.showMessageDialog( org.alice.ide.IDE.getSingleton(), sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE );
	}

	public java.io.File getFile() {
		return this.file;
	}
	public void setFile( java.io.File file ) {
		if( file.exists() ) {
			String lcFilename = file.getName().toLowerCase();
			if( lcFilename.endsWith( ".a2w" ) ) {
				javax.swing.JOptionPane.showMessageDialog( this, "Alice3 does not load Alice2 worlds", "Cannot read file", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			} else if( lcFilename.endsWith( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.TYPE_EXTENSION.toLowerCase() ) ) {
				javax.swing.JOptionPane.showMessageDialog( this, file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION
						+ " extension.", "Incorrect File Type", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			} else {
				boolean isWorthyOfException = lcFilename.endsWith( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION.toLowerCase() );
				java.util.zip.ZipFile zipFile;
				try {
					zipFile = new java.util.zip.ZipFile( file );
				} catch( java.io.IOException ioe ) {
					if( isWorthyOfException ) {
						throw new RuntimeException( file.getAbsolutePath(), ioe );
					} else {
						this.showMessageDialog( file, false );
						zipFile = null;
					}
				}
				if( zipFile != null ) {
					edu.cmu.cs.dennisc.alice.Project project;
					try {
						project = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( zipFile );
					} catch( java.io.IOException ioe ) {
						if( isWorthyOfException ) {
							throw new RuntimeException( file.getAbsolutePath(), ioe );
						} else {
							this.showMessageDialog( file, true );
							project = null;
						}
					}
					if( project != null ) {
						this.setProject( project );
						this.file = file;
						this.updateTitle();
					} else {
						//actionContext.cancel();
					}
				} else {
					//actionContext.cancel();
				}
			}
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "Cannot read project from file:\n\t" );
			sb.append( file.getAbsolutePath() );
			sb.append( "\nIt does not exist." );
			javax.swing.JOptionPane.showMessageDialog( this, sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE );
		}
	}

	public void revert() {
		java.io.File file = this.getFile();
		if( file != null ) {
			this.loadProjectFrom( file );
		} else {
			javax.swing.JOptionPane.showMessageDialog( this, "You must have a project open in order to revert.", "Revert", javax.swing.JOptionPane.INFORMATION_MESSAGE );
		}
	}

	protected void fireMethodFocusChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.focusedCodeChanging( e );
		}
	}

	protected void fireMethodFocusChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.focusedCodeChanged( e );
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getFocusedCode() {
		return this.focusedCode;
	}

	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
		if( nextFocusedCode == this.focusedCode ) {
			//pass
		} else {
			org.alice.ide.event.FocusedCodeChangeEvent e = new org.alice.ide.event.FocusedCodeChangeEvent( this, this.focusedCode, nextFocusedCode );
			fireMethodFocusChanging( e );
			this.focusedCode = nextFocusedCode;
			fireMethodFocusChanged( e );
		}
	}

	protected void fireFieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.fieldSelectionChanging( e );
		}
	}

	protected void fireFieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.fieldSelectionChanged( e );
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldSelection() {
		return this.fieldSelection;
	}

	public void setFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection ) {
		org.alice.ide.event.FieldSelectionEvent e = new org.alice.ide.event.FieldSelectionEvent( this, this.fieldSelection, fieldSelection );
		fireFieldSelectionChanging( e );
		this.fieldSelection = fieldSelection;
		fireFieldSelectionChanged( e );
	}

	protected void fireTransientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.transientSelectionChanging( e );
		}
	}

	protected void fireTransientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
		for( org.alice.ide.event.IDEListener l : this.getIDEListeners() ) {
			l.transientSelectionChanged( e );
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractTransient getTransientSelection() {
		return this.transientSelection;
	}

	public void setTransientSelection( edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection ) {
		org.alice.ide.event.TransientSelectionEvent e = new org.alice.ide.event.TransientSelectionEvent( this, this.transientSelection, transientSelection );
		fireTransientSelectionChanging( e );
		this.transientSelection = transientSelection;
		fireTransientSelectionChanged( e );
	}

	private java.util.Stack< edu.cmu.cs.dennisc.zoot.Operation > history = new java.util.Stack< edu.cmu.cs.dennisc.zoot.Operation >();
	private int historyLengthAtLastFileOperation = 0;
	private boolean isMarkedChanged = false;

	private boolean isChanged() {
		return this.isMarkedChanged || this.history.size() > this.historyLengthAtLastFileOperation;
	}
	public boolean isProjectUpToDateWithFile() {
		if( this.file != null ) {
			return isChanged() == false;
		} else {
			return true;
		}
	}
	private void markChanged() {
		this.isMarkedChanged = true;
		this.updateTitle();
	}
	//	@Deprecated
	//	public void markChanged( String description ) {
	//		this.markChanged();
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: convert change to operation ( " + description + " )" );
	//	}

	//	protected void addToHistory( Operation operation ) {
	//		this.history.push( operation );
	//		updateTitle();
	//	}
	//	protected void handlePreparedOperation( Operation operation, java.util.EventObject e, java.util.List< java.util.EventObject > preparationUpdates, Operation.PreparationResult preparationResult ) {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handlePreparedOperation", operation, preparationResult );
	//		if( preparationResult != null ) {
	//			if( preparationResult == Operation.PreparationResult.CANCEL ) {
	//				//pass
	//			} else {
	//				operation.perform();
	//				if( preparationResult == Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY ) {
	//					addToHistory( operation );
	//				}
	//			}
	//		}
	//	}
	//	public void performIfAppropriate( Operation operation, java.util.EventObject e ) {
	//		final java.util.List< java.util.EventObject > preparationUpdates = new java.util.LinkedList< java.util.EventObject >();
	//		Operation.PreparationResult preparationResult = operation.prepare( e, new Operation.PreparationObserver() {
	//			public void update( java.util.EventObject e ) {
	//				preparationUpdates.add( e );
	//			}
	//		} );
	//		handlePreparedOperation( operation, e, preparationUpdates, preparationResult );
	//	}

	private void updateHistoryLengthAtLastFileOperation() {
		//this.history.clear();
		this.historyLengthAtLastFileOperation = this.history.size();
		this.isMarkedChanged = false;
		this.updateTitle();
	}
	public void loadProjectFrom( java.io.File file ) {
		this.mapUUIDToNode.clear();
		this.updateHistoryLengthAtLastFileOperation();
		this.restoreProjectProperties();
		setFile( file );
		//todo: find a better solution to concurrent modification exception
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				//edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getProject().getProgramType().getDeclaredFields().get( 0 );
				edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getSceneField();
				if( sceneField != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
					IDE.this.setFocusedCode( runMethod );
					java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneField.getValueType().getDeclaredFields();
					final int N = fields.size();
					int i = N - 1;
					while( i >= 0 ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractField field = fields.get( i );
						if( field.getValueType().isArray() ) {
							//pass
						} else {
							IDE.this.setFieldSelection( field );
							break;
						}
						i--;
					}
				}
			}
		} );
	}
	public void loadProjectFrom( String path ) {
		loadProjectFrom( new java.io.File( path ) );
	}

	public void ensureProjectCodeUpToDate() {
		this.generateCodeForSceneSetUp();
	}

	private void generateCodeForSceneSetUp() {
		this.sceneEditor.generateCodeForSetUp();
	}
	protected void preserveProjectProperties() {
		this.sceneEditor.preserveProjectProperties();
	}
	protected void restoreProjectProperties() {
		this.sceneEditor.restoreProjectProperties();
	}

	protected abstract java.awt.image.BufferedImage createThumbnail() throws Throwable;

	public void saveProjectTo( java.io.File file ) throws java.io.IOException {
		edu.cmu.cs.dennisc.alice.Project project = getProject();
		this.ensureProjectCodeUpToDate();
		this.preserveProjectProperties();

		edu.cmu.cs.dennisc.zip.DataSource[] dataSources;
		try {
			final java.awt.image.BufferedImage thumbnailImage = createThumbnail();
			if( thumbnailImage != null ) {
				if( thumbnailImage.getWidth() > 0 && thumbnailImage.getHeight() > 0 ) {
					//pass
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new NullPointerException();
			}
			dataSources = new edu.cmu.cs.dennisc.zip.DataSource[] { new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return "thumbnail.png";
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, os, thumbnailImage );
				}
			} };
		} catch( Throwable t ) {
			dataSources = new edu.cmu.cs.dennisc.zip.DataSource[] {};
		}
		edu.cmu.cs.dennisc.alice.project.ProjectUtilities.writeProject( file, project, dataSources );
		this.file = file;
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "project saved to: ", file.getAbsolutePath() );
		this.updateHistoryLengthAtLastFileOperation();
	}
	public void saveProjectTo( String path ) throws java.io.IOException {
		saveProjectTo( new java.io.File( path ) );
	}

	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getProgramType() {
		edu.cmu.cs.dennisc.alice.Project project = getProject();
		if( project != null ) {
			return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType();
		} else {
			return null;
		}
	}
	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = getProgramType();
		return getSceneFieldFromProgramType( programType );
	}
	
	@Deprecated
	protected static edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneFieldFromProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		if( programType instanceof TypeDeclaredInAlice ) {
			TypeDeclaredInAlice programAliceType = (TypeDeclaredInAlice)programType;
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
		if( programType instanceof TypeDeclaredInAlice ) {
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
					if( this.isOmittingThisFieldAccesses() ) {
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
		return createInstanceExpression( getFieldSelection() );
	}

	public boolean isFieldInScope( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return createInstanceExpression( field ) != null;
	}
	public final boolean isSelectedFieldInScope() {
		return isFieldInScope( getFieldSelection() );
	}

	public boolean isDragInProgress() {
		return this.isDragInProgress;
	}
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
			if( edu.cmu.cs.dennisc.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.DoTogether.class, edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether.class, edu.cmu.cs.dennisc.alice.ast.DoInThread.class ) ) {
				java.awt.Color colorA = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 0.85 );
				java.awt.Color colorB = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( color, 1.0, 1.0, 1.15 );
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
				return edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 245 );
			} else {
				return new java.awt.Color( 0xd3d7f0 );
			}
		} else if( edu.cmu.cs.dennisc.alice.ast.Expression.class.isAssignableFrom( cls ) ) {
			if( edu.cmu.cs.dennisc.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) ) {
				return new java.awt.Color( 0xBAD1A7 );
			} else if( edu.cmu.cs.dennisc.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.InfixExpression.class, edu.cmu.cs.dennisc.alice.ast.LogicalComplement.class, edu.cmu.cs.dennisc.alice.ast.StringConcatenation.class ) ) {
				return new java.awt.Color( 0xDEEBD3 );
			} else if( edu.cmu.cs.dennisc.lang.ClassUtilities.isAssignableToAtLeastOne( cls, edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class, edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation.class ) ) {
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

	public java.awt.Component getPrefixPaneForFieldAccessIfAppropriate( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
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
							pane.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( pane ) );
						}
					}
				} );
			}
			return rv;
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
	public java.io.File getMyProjectsDirectory() {
		return edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyProjectsDirectory( this.getSubPath() );
	}
	public java.io.File getMyTypesDirectory() {
		return edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyTypesDirectory( this.getSubPath() );
	}

	public edu.cmu.cs.dennisc.zoot.ActionOperation getRestartOperation() {
		//todo
		return this.getRunOperation();
	}

	public boolean isInstanceLineDesired() {
		return true;
	}

	public String getTextForThis() {
		return edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( edu.cmu.cs.dennisc.alice.ast.ThisExpression.class, "edu.cmu.cs.dennisc.alice.ast.Templates" );
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
		while( validator.isNameValidAndAvailable( rv ) == false ) {
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

	public abstract boolean isInstanceCreationAllowableFor( TypeDeclaredInAlice typeInAlice );
	public abstract Program createRuntimeProgram( VirtualMachine vm, TypeDeclaredInAlice sceneType, int frameRate );

	public java.util.Set< org.alice.virtualmachine.Resource > getResources() {
		edu.cmu.cs.dennisc.alice.Project project = this.getProject();
		if( project != null ) {
			return project.getResources();
		} else {
			return null;
		}
	}
}

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

import org.alice.ide.ubiquitouspane.UbiquitousPane;

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends org.alice.ide.ProjectApplication {
	public static final edu.cmu.cs.dennisc.croquet.Group RUN_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "f7a87645-567c-42c6-bf5f-ab218d93a226" ), "RUN_GROUP" );
	public static final edu.cmu.cs.dennisc.croquet.Group PROGRAMMING_LANGUAGE_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "1fc6d8ce-3ce8-4b8d-91be-bc74d0d02c3e" ), "PROGRAMMING_LANGUAGE_GROUP" );

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
		performSceneEditorGeneratedSetUpMethodNameSet.add( GENERATED_SET_UP_METHOD_NAME );
	}

	public static IDE getSingleton() {
		return IDE.singleton;
	}

	private edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver isAlwaysShowingBlocksObserver = new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
		public void changing( boolean nextValue ) {
		}
		public void changed( boolean nextValue ) {
			if( nextValue ) {
				IDE.this.right.addComponent( IDE.this.ubiquitousPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
			} else {
				IDE.this.right.removeComponent( IDE.this.ubiquitousPane );
			}
			IDE.this.right.revalidateAndRepaint();
		}
	};

	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		assert IDE.singleton == null;
		IDE.singleton = this;

		//initialize locale
		org.alice.ide.croquet.models.ui.locale.LocaleSelectionState.getInstance().addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< java.util.Locale >() {
			public void changed( java.util.Locale nextValue ) {
				edu.cmu.cs.dennisc.croquet.Application.getSingleton().setLocale( nextValue );
			}
		} );

		this.promptForLicenseAgreements();

		this.getRunOperation().setEnabled( false );

		this.galleryBrowser = this.createGalleryBrowser( this.getGalleryRoot() );
		this.membersEditor = this.createClassMembersEditor();
		this.ubiquitousPane = this.createUbiquitousPane();

		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().createEditorsFolderTabbedPane();
		tabbedPane.scaleFont( 2.0f );

		final int MINIMUM_SIZE = 24;
		this.right.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );
		this.left.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );

		//this.right.addComponent( this.ubiquitousPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );
		this.right.addComponent( tabbedPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		//this.right.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "hello" ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().addAndInvokeValueObserver( this.isAlwaysShowingBlocksObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().addAndInvokeValueObserver( this.accessibleSelectionObserver );

		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
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

		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
			public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextValue ) {
				refreshAccessibles();
			}
		} );
	}
	private int rootDividerLocation = 340;
	private int leftDividerLocation = 240;

	private edu.cmu.cs.dennisc.javax.swing.components.JConcealedBin concealedBin = new edu.cmu.cs.dennisc.javax.swing.components.JConcealedBin();
	private edu.cmu.cs.dennisc.croquet.JComponent< ? > galleryBrowser;
	private org.alice.ide.memberseditor.MembersEditor membersEditor;
	private org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane;


	private edu.cmu.cs.dennisc.croquet.VerticalSplitPane left = new edu.cmu.cs.dennisc.croquet.VerticalSplitPane();
	private edu.cmu.cs.dennisc.croquet.BorderPanel right = new edu.cmu.cs.dennisc.croquet.BorderPanel();
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane root = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane( left, right );

	@Override
	public void initialize( java.lang.String[] args ) {
		super.initialize( args );
		edu.cmu.cs.dennisc.croquet.Frame frame = this.getFrame();
		frame.setMenuBarModel( org.alice.ide.croquet.models.MenuBarModel.getInstance() );		
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		rv.addMouseWheelListener( new edu.cmu.cs.dennisc.javax.swing.plaf.metal.FontMouseWheelAdapter() );
		rv.addComponent( this.root, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		rv.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.concealedBin ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END );

		this.setSceneEditorExpanded( false );
		return rv;
	}

	@Override
	public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor( edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		if( dropSite instanceof org.alice.ide.codeeditor.BlockStatementIndexPair ) {
			org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair = (org.alice.ide.codeeditor.BlockStatementIndexPair)dropSite;
			edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = blockStatementIndexPair.getBlockStatement();
			edu.cmu.cs.dennisc.alice.ast.AbstractCode code = blockStatement.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
			System.err.println( "todo: getDropReceptor: " + dropSite );
			return getCodeEditorInFocus();
		}
		return null;
	}
	private void setSceneEditorExpanded( boolean isSceneEditorExpanded ) {
		this.refreshAccessibles();
		if( isSceneEditorExpanded ) {
			if( this.root.getAwtComponent().isValid() ) {
				this.rootDividerLocation = this.root.getDividerLocation();
			}
			if( this.left.getAwtComponent().isValid() ) {
				this.leftDividerLocation = this.left.getDividerLocation();
			}
			this.left.setResizeWeight( 1.0 );
			this.root.setLeftComponent( this.left );
			this.left.setTopComponent( this.getSceneEditor() );
			this.left.setBottomComponent( this.galleryBrowser );
			//this.root.setRightComponent( null );
			this.right.setVisible( false );
			this.root.setDividerSize( 0 );
			this.left.setDividerLocation( this.getFrame().getHeight() - 304 );
		} else {
			this.left.setResizeWeight( 0.0 );
			this.root.setLeftComponent( this.left );
			this.right.setVisible( true );
			//this.root.setRightComponent( this.right );
			this.root.setDividerLocation( this.rootDividerLocation );
			this.left.setTopComponent( this.getSceneEditor() );
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

	private Theme theme;

	protected Theme createTheme() {
		return new DefaultTheme();
	}
	public Theme getTheme() {
		if( this.theme != null ) {
			//pass
		} else {
			this.theme = this.createTheme();
		}
		return this.theme;
	}

	@Override
	public void loadProjectFrom( java.net.URI uri ) {
		super.loadProjectFrom( uri );
		this.mapUUIDToNode.clear();
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getSceneField();
		if( sceneField != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
			setFocusedCode( runMethod );
		}
	}

	protected edu.cmu.cs.dennisc.alice.ast.Expression createPredeterminedExpressionIfAppropriate( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type ) {
		return null;
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression[] createPredeterminedExpressionsIfAppropriate( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? >[] types ) {
		if( types == null || types.length == 0 ) {
			return new edu.cmu.cs.dennisc.alice.ast.Expression[] {};
		} else {
			if( types.length == 1 ) {
				edu.cmu.cs.dennisc.alice.ast.Expression predeterminedExpression = org.alice.ide.IDE.getSingleton().createPredeterminedExpressionIfAppropriate( types[ 0 ] );
				if( predeterminedExpression != null ) {
					return new edu.cmu.cs.dennisc.alice.ast.Expression[] { predeterminedExpression };
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
	public final edu.cmu.cs.dennisc.croquet.Operation< ? > getPreferencesOperation() {
		return null;
	}
	public abstract edu.cmu.cs.dennisc.croquet.Operation< ? > getRunOperation();
	public abstract edu.cmu.cs.dennisc.croquet.Operation< ? > getRestartOperation();
	public abstract edu.cmu.cs.dennisc.croquet.Operation< ? > getAboutOperation();

	public abstract edu.cmu.cs.dennisc.croquet.Operation< ? > createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate );

	public enum AccessorAndMutatorDisplayStyle {
		GETTER_AND_SETTER, ACCESS_AND_ASSIGNMENT
	}

	public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > declaringType = field.getDeclaringType();
		if( declaringType != null && declaringType.isDeclaredInAlice() ) {
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		} else {
			//return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getPerformEditorGeneratedSetUpMethod() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = this.getSceneType();
		if( sceneType != null ) {
			for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : sceneType.methods ) {
				if( IDE.performSceneEditorGeneratedSetUpMethodNameSet.contains( method.name.getValue() ) ) {
					return method;
				}
			}
		}
		return null;
	}

	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getStrippedProgramType() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice rv = this.getProgramType();
		if( rv != null ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice setUpMethod = this.getPerformEditorGeneratedSetUpMethod();
			setUpMethod.body.getValue().statements.clear();
		}
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
		java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > aliceTypes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addAliceTypes( aliceTypes, true );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type : aliceTypes ) {
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
	public org.alice.ide.common.TypeComponent getComponentFor( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type ) {
		//todo:
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}
	public String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type ) {
		return null;
	}

	protected java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > addPrimeTimeJavaTypes( java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv ) {
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( String.class ) );
		return rv;
	}
	protected java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > addSecondaryJavaTypes( java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv ) {
		return rv;
	}

	protected boolean isInclusionOfTypeDesired( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > valueTypeInAlice ) {
		return true;
		//return valueTypeInAlice.methods.size() > 0 || valueTypeInAlice.fields.size() > 0;
	}

	protected java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > addAliceTypes( java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > rv, boolean isInclusionOfTypesWithoutMembersDesired ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = this.getSceneType();
		if( sceneType != null ) {
			rv.add( sceneType );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > valueType = field.getValueType();
				if( valueType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
					edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueTypeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)valueType;
					if( rv.contains( valueType ) ) {
						//pass
					} else {
						if( isInclusionOfTypesWithoutMembersDesired || isInclusionOfTypeDesired( valueTypeInAlice ) ) {
							rv.add( valueTypeInAlice );
						}
					}
				}
			}
		}
		return rv;
	}

	public java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > getPrimeTimeSelectableTypesDeclaredInJava() {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addPrimeTimeJavaTypes( rv );
		return rv;
	}
	public java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > getSecondarySelectableTypesDeclaredInJava() {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addSecondaryJavaTypes( rv );
		return rv;
	}
	public java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > getTypesDeclaredInAlice() {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addAliceTypes( rv, true );
		return rv;
	}

	public abstract edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > getGalleryRoot();
	protected abstract edu.cmu.cs.dennisc.croquet.JComponent< ? > createGalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > root );
	protected org.alice.ide.memberseditor.MembersEditor createClassMembersEditor() {
		return new org.alice.ide.memberseditor.MembersEditor();
	}
	protected org.alice.ide.ubiquitouspane.UbiquitousPane createUbiquitousPane() {
		return new org.alice.ide.ubiquitouspane.UbiquitousPane();
	}

	public org.alice.ide.ubiquitouspane.UbiquitousPane getUbiquitousPane() {
		return this.ubiquitousPane;
	}
	public org.alice.ide.memberseditor.MembersEditor getMembersEditor() {
		return this.membersEditor;
	}
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > getGalleryBrowser() {
		return this.galleryBrowser;
	}
	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractCode, edu.cmu.cs.dennisc.alice.ast.Accessible > mapCodeToAccessible = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Accessible > accessibleSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Accessible >() {
		public void changed( edu.cmu.cs.dennisc.alice.ast.Accessible nextValue ) {
			if( nextValue != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractCode code = IDE.this.getFocusedCode();
				if( code != null ) {
					mapCodeToAccessible.put( code, nextValue );
				}
			}
		}
	};

	public abstract org.alice.ide.cascade.CascadeManager getCascadeManager();

	public void addToConcealedBin( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		this.concealedBin.add( component.getAwtComponent() );
		this.concealedBin.revalidate();
	}
	public void removeFromConcealedBin( edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		this.concealedBin.remove( component.getAwtComponent() );
	}

	public boolean isJava() {
		return org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem() == org.alice.ide.formatter.JavaFormatter.getInstance();
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
		return org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().getCodeEditorInFocus();
	}

	private ComponentStencil stencil;
	private java.util.List< ? extends edu.cmu.cs.dennisc.croquet.Component< ? > > holes = null;
	private edu.cmu.cs.dennisc.croquet.DragComponent potentialDragSource;
	private edu.cmu.cs.dennisc.croquet.Component< ? > currentDropReceptorComponent;

	protected boolean isFauxStencilDesired() {
		return this.isDragInProgress();
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
						potentialDragSourceBounds = javax.swing.SwingUtilities.convertRectangle( IDE.this.potentialDragSource.getParent().getAwtComponent(), IDE.this.potentialDragSource.getBounds(), this );
					} else {
						potentialDragSourceBounds = null;
					}

					if( isFauxStencilDesired() ) {
						for( edu.cmu.cs.dennisc.croquet.Component< ? > component : IDE.this.holes ) {
							java.awt.Rectangle holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent().getAwtComponent(), component.getBounds(), this );
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
						java.awt.Rectangle holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent().getAwtComponent(), component.getBounds(), this );
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
					//					if( potentialDragSourceBounds != null ) {
					//						g2.setColor( java.awt.Color.BLUE );
					//						g2.draw( potentialDragSourceBounds );
					//					}
				}
			}
		}
	}

	//public abstract void handleDelete( edu.cmu.cs.dennisc.alice.ast.Node node );

	public void showStencilOver( edu.cmu.cs.dennisc.croquet.DragComponent potentialDragSource, final edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			this.holes = codeEditor.createListOfPotentialDropReceptors( type );
			this.potentialDragSource = potentialDragSource;
			//java.awt.Rectangle bounds = codeEditor.getBounds();
			//bounds = javax.swing.SwingUtilities.convertRectangle( codeEditor, bounds, layeredPane );
			//this.stencil.setBounds( bounds );
			javax.swing.JLayeredPane layeredPane = this.getFrame().getAwtComponent().getLayeredPane();
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
		javax.swing.JLayeredPane layeredPane = this.getFrame().getAwtComponent().getLayeredPane();
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
		this.getSceneEditor().disableRendering( reasonToDisableSomeAmountOfRendering );
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
			this.getSceneEditor().enableRendering( reasonToDisableSomeAmountOfRendering );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate extra enableRendering" );
		}
	}
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
	}

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshAccessibles();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshAccessibles();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshAccessibles();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			IDE.this.refreshAccessibles();
		}
	};

	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	protected boolean isAccessibleDesired( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		return accessible.getValueType().isArray() == false;
	}

	
	private boolean isRespondingToRefreshAccessibles = true;
	public void refreshAccessibles() {
		if( isRespondingToRefreshAccessibles ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshAccessibles" );

			edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getFocusedCode();
			edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();

			java.util.List< edu.cmu.cs.dennisc.alice.ast.Accessible > accessibles = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			if( this.rootField != null ) {
				accessibles.add( this.rootField );
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
					if( this.isAccessibleDesired( field ) ) {
						accessibles.add( field );
					}
				}
			}

			int indexOfLastField = accessibles.size() - 1;
			if( code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
				edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)code;
				for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : codeDeclaredInAlice.getParamtersProperty() ) {
					if( this.isAccessibleDesired( parameter ) ) {
						accessibles.add( parameter );
					}
				}
				for( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable : IDE.getVariables( code ) ) {
					if( this.isAccessibleDesired( variable ) ) {
						accessibles.add( variable );
					}
				}
				for( edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant : IDE.getConstants( code ) ) {
					if( this.isAccessibleDesired( constant ) ) {
						accessibles.add( constant );
					}
				}
			}

			int selectedIndex;
			if( accessible != null ) {
				selectedIndex = accessibles.indexOf( accessible );
			} else {
				selectedIndex = -1;
			}
			if( selectedIndex == -1 ) {
				if( code != null ) {
					accessible = this.mapCodeToAccessible.get( code );
					selectedIndex = accessibles.indexOf( accessible );
				}
			}
			if( selectedIndex == -1 ) {
				selectedIndex = indexOfLastField;
			}
			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setListData( selectedIndex, accessibles );
		}
	}
	
	private void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshAccessibles();
	}
	@Override
	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		super.setProject( project );
		this.isRespondingToRefreshAccessibles = false;
		try {
			this.setRootField( this.getSceneField() );
		} finally {
			this.isRespondingToRefreshAccessibles = true;
			this.refreshAccessibles();
		}
	}

	public <N extends edu.cmu.cs.dennisc.alice.ast.AbstractNode> N createCopy( N original ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice root = this.getProgramType();
		java.util.Set< edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > abstractDeclarations = root.createDeclarationSet();
		original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations );
		java.util.Map< Integer, edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration > map = edu.cmu.cs.dennisc.alice.ast.AbstractNode.createMapOfDeclarationsThatShouldNotBeCopied( abstractDeclarations );
		org.w3c.dom.Document xmlDocument = original.encode( abstractDeclarations );
		edu.cmu.cs.dennisc.alice.ast.AbstractNode dst = edu.cmu.cs.dennisc.alice.ast.AbstractNode.decode( xmlDocument, edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText(), map, false );

		//		if( original.isEquivalentTo( dst ) ) {
		//			return dst;
		//		} else {
		//			throw new RuntimeException( "copy not equivalent to original" );
		//		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: check copy" );
		return (N)dst;
	}
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
			org.alice.ide.croquet.models.projecturi.NewProjectOperation.getInstance().fire( e );
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
	protected void preservePreferences() {
		PreferenceManager.preservePreferences();
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		this.preservePreferences();
		org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation.getInstance().fire( e );
	}

//	public java.util.List< ? extends edu.cmu.cs.dennisc.croquet.DropReceptor > createListOfPotentialDropReceptors( edu.cmu.cs.dennisc.croquet.DragComponent source ) {
//		if( source instanceof org.alice.stageide.gallerybrowser.GalleryDragComponent ) {
//			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.getSceneEditor() );
//		} else {
//			assert source != null;
//			org.alice.ide.codeeditor.CodeEditor codeEditor = this.getCodeEditorInFocus();
//			if( codeEditor != null ) {
//				if( source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance ) {
//					org.alice.ide.common.ExpressionLikeSubstance expressionLikeSubstance = (org.alice.ide.common.ExpressionLikeSubstance)source.getSubject();
//					return codeEditor.createListOfPotentialDropReceptors( expressionLikeSubstance.getExpressionType() );
//				} else {
//					java.util.List< edu.cmu.cs.dennisc.croquet.DropReceptor > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//					rv.add( codeEditor );
//					//			for( alice.ide.ast.DropReceptor dropReceptor : this.dropReceptors ) {
//					//				if( dropReceptor.isPotentiallyAcceptingOf( source ) ) {
//					//					rv.add( dropReceptor );
//					//				}
//					//			}
//					return rv;
//				}
//			} else {
//				//todo: investigate
//				return java.util.Collections.emptyList();
//			}
//		}
//	}

	private static Iterable< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice > getVariables( edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}
	private static Iterable< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice > getConstants( edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getTypeInScope() {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus = this.getFocusedCode();
		if( codeInFocus != null ) {
			return codeInFocus.getDeclaringType();
		} else {
			return null;
		}
	}

	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForSceneEditor;
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForSceneEditor() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	public final edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVirtualMachineForSceneEditor() {
		if( this.vmForSceneEditor != null ) {
			//pass
		} else {
			this.vmForSceneEditor = this.createVirtualMachineForSceneEditor();
		}
		return this.vmForSceneEditor;
	}

	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getFocusedCode() {
		if( org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().getValue() ) {
			return this.getPerformEditorGeneratedSetUpMethod();
		} else {
			return org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().getSelectedItem();
		}
	}
	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().edit( nextFocusedCode, false );
	}

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
		this.getSceneEditor().generateCodeForSetUp( bodyStatementsProperty, this.getSceneEditor() );
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
	protected static edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneFieldFromProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > programType ) {
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
	protected static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneTypeFromProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > programType ) {
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

	public String getInstanceTextForAccessible( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		String text;
		if( accessible != null ) {
			if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)accessible;
				text = field.getName();
				edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
				if( focusedCode != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > scopeType = focusedCode.getDeclaringType();
					if( field.getValueType() == scopeType ) {
						text = "this";
					} else if( field.getDeclaringType() == scopeType ) {
						if( org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().getValue() ) {
							text = "this." + text;
						}
					}
				}
			} else {
				text = accessible.getValidName();
			}
		} else {
			text = null;
		}
		return text;
	}

	private edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) /*throws OutOfScopeException*/{
		edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
		if( focusedCode != null ) {
			if( accessible != null ) {
				if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)accessible;
					edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > focusedCodeDeclaringType = focusedCode.getDeclaringType();
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
					if( focusedCode == accessible.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractCode.class ) ) {
						if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractParameter ) {
							edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter = (edu.cmu.cs.dennisc.alice.ast.AbstractParameter)accessible;
							return new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
						} else if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
							edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)accessible;
							return new edu.cmu.cs.dennisc.alice.ast.VariableAccess( variable );
						} else if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
							edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant = (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)accessible;
							return new edu.cmu.cs.dennisc.alice.ast.ConstantAccess( constant );
						} else {
							assert false : accessible;
							return null;
						}
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public final edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression() /*throws OutOfScopeException*/{
		edu.cmu.cs.dennisc.alice.ast.Expression rv = this.createInstanceExpression( org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem() );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField partField = org.alice.ide.croquet.models.members.PartSelectionState.getInstance().getSelectedItem();
		if( partField != null ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field = org.alice.ide.croquet.models.members.PartSelectionState.getInstance().getSelectedItem();
			if( field != null ) {
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava fieldType = field.getValueType();
				Class< ? > cls = fieldType.getClassReflectionProxy().getReification();
				Class< ? > enclosingCls = cls.getEnclosingClass();
				if( enclosingCls != null ) {
					try {
						java.lang.reflect.Method mthd = enclosingCls.getMethod( "getPart", cls );
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava method = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( mthd );
						return org.alice.ide.ast.NodeUtilities.createMethodInvocation( rv, method, new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( fieldType ), field ) );
					} catch( NoSuchMethodException nsme ) {
						//pass
					}
				}
			}
		}
		return rv;
	}

	public boolean isAccessibleInScope( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		return createInstanceExpression( accessible ) != null;
	}
	public final boolean isSelectedAccessibleInScope() {
		return isAccessibleInScope( org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem() );
	}

	@Override
	public void setDragInProgress( boolean isDragInProgress ) {
		super.setDragInProgress( isDragInProgress );
		this.currentDropReceptorComponent = null;
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

	private java.util.Map< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node > mapUUIDToNode = new java.util.HashMap< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node >();

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
}

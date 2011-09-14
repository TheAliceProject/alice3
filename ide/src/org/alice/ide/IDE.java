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

import org.lgna.croquet.components.JComponent;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends org.alice.ide.ProjectApplication {
	public static final org.lgna.croquet.Group RUN_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "f7a87645-567c-42c6-bf5f-ab218d93a226" ), "RUN_GROUP" );

	public static final String DEBUG_PROPERTY_KEY = "org.alice.ide.DebugMode";
	public static final String DEBUG_DRAW_PROPERTY_KEY = "org.alice.ide.DebugDrawMode";

	private static org.alice.ide.issue.ExceptionHandler exceptionHandler;
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

	private org.lgna.croquet.State.ValueObserver< Boolean > isAlwaysShowingBlocksObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			if( nextValue ) {
				IDE.this.right.addComponent( IDE.this.ubiquitousPane, org.lgna.croquet.components.BorderPanel.Constraint.NORTH );
			} else {
				IDE.this.right.removeComponent( IDE.this.ubiquitousPane );
			}
			IDE.this.right.revalidateAndRepaint();
		}
	};

	public static IDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.Application.getActiveInstance(), IDE.class );
	}

	private final org.alice.ide.typeeditor.TypeEditor typeEditor = new org.alice.ide.typeeditor.TypeEditor();
	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		//initialize locale
		org.alice.ide.croquet.models.ui.locale.LocaleSelectionState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.ListSelectionState.ValueObserver< java.util.Locale >() {
			public void changing( org.lgna.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
				org.lgna.croquet.Application.getActiveInstance().setLocale( nextValue );
			}
		} );

		this.promptForLicenseAgreements();

		this.getRunOperation().setEnabled( false );

		//this.galleryBrowser = this.createGalleryBrowser( this.getGalleryRoot() );
		this.galleryBrowser = this.createClassGalleryBrowser(this.getClassGalleryRoot());
		this.membersEditor = this.createClassMembersEditor();
		this.ubiquitousPane = this.createUbiquitousPane();

		final int MINIMUM_SIZE = 24;
		this.right.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );
		this.left.getAwtComponent().setMinimumSize( new java.awt.Dimension( MINIMUM_SIZE, MINIMUM_SIZE ) );

		this.right.addComponent( this.typeEditor, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		
		//this.right.addComponent( new org.lgna.croquet.Label( "hello" ), org.lgna.croquet.BorderPanel.Constraint.CENTER );

		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().addAndInvokeValueObserver( this.isAlwaysShowingBlocksObserver );
		org.alice.ide.instancefactory.InstanceFactoryState.getInstance().addAndInvokeValueObserver( this.instanceFactorySelectionObserver );

		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.State.ValueObserver< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				setSceneEditorExpanded( nextValue );
			}
		} );

		this.addProjectObserver( new ProjectObserver() {
			public void projectOpening( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
			}
			public void projectOpened( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
				getRunOperation().setEnabled( nextProject != null );
			}
		} );

		org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addAndInvokeValueObserver( new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >() {
			public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
				refreshAccessibles();
			}
		} );
	}
	private int rootDividerLocation = 340;
	private int leftDividerLocation = 240;

	private org.lgna.croquet.components.JComponent< ? > galleryBrowser;
	private org.alice.ide.memberseditor.MembersEditor membersEditor;
	private org.alice.ide.ubiquitouspane.UbiquitousPane ubiquitousPane;


	private org.lgna.croquet.components.VerticalSplitPane left = new org.lgna.croquet.components.VerticalSplitPane();
	private org.lgna.croquet.components.BorderPanel right = new org.lgna.croquet.components.BorderPanel();
	private org.lgna.croquet.components.HorizontalSplitPane root = new org.lgna.croquet.components.HorizontalSplitPane( left, right );

	
	public abstract ApiConfigurationManager getApiConfigurationManager();
	
	@Override
	public void initialize( java.lang.String[] args ) {
		super.initialize( args );
		org.lgna.croquet.components.Frame frame = this.getFrame();
		frame.setMenuBarModel( org.alice.ide.croquet.models.MenuBarComposite.getInstance() );		
	}
	@Override
	protected org.lgna.croquet.components.Component< ? > createContentPane() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		rv.addMouseWheelListener( new edu.cmu.cs.dennisc.javax.swing.plaf.metal.FontMouseWheelAdapter() );
		rv.addComponent( this.root, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		this.setSceneEditorExpanded( false );
		return rv;
	}

	@Override
	public org.lgna.croquet.DropReceptor getDropReceptor( org.lgna.croquet.DropSite dropSite ) {
		if( dropSite instanceof org.alice.ide.codeeditor.BlockStatementIndexPair ) {
			org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair = (org.alice.ide.codeeditor.BlockStatementIndexPair)dropSite;
			org.lgna.project.ast.BlockStatement blockStatement = blockStatementIndexPair.getBlockStatement();
			org.lgna.project.ast.AbstractCode code = blockStatement.getFirstAncestorAssignableTo( org.lgna.project.ast.AbstractCode.class );
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
		org.lgna.project.ast.AbstractField sceneField = getSceneField();
		if( sceneField != null ) {
			org.lgna.project.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
			setFocusedCode( runMethod );
		}
	}

	protected org.lgna.project.ast.Expression createPredeterminedExpressionIfAppropriate( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return null;
	}
	public org.lgna.project.ast.Expression[] createPredeterminedExpressionsIfAppropriate( org.lgna.project.ast.AbstractType< ?, ?, ? >[] types ) {
		if( types == null || types.length == 0 ) {
			return new org.lgna.project.ast.Expression[] {};
		} else {
			if( types.length == 1 ) {
				org.lgna.project.ast.Expression predeterminedExpression = org.alice.ide.IDE.getActiveInstance().createPredeterminedExpressionIfAppropriate( types[ 0 ] );
				if( predeterminedExpression != null ) {
					return new org.lgna.project.ast.Expression[] { predeterminedExpression };
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
	public final org.lgna.croquet.Operation< ? > getPreferencesOperation() {
		return null;
	}
	public abstract org.lgna.croquet.Operation< ? > getRunOperation();
	public abstract org.lgna.croquet.Operation< ? > getRestartOperation();
	public abstract org.lgna.croquet.Operation< ? > getAboutOperation();

	public abstract org.lgna.croquet.Operation< ? > createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate );

	public enum AccessorAndMutatorDisplayStyle {
		GETTER_AND_SETTER, ACCESS_AND_ASSIGNMENT
	}

	public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle( org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.AbstractType< ?, ?, ? > declaringType = field.getDeclaringType();
		if( declaringType != null && declaringType.isDeclaredInAlice() ) {
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		} else {
			//return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		}
	}

	public org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod() {
		org.lgna.project.ast.NamedUserType sceneType = this.getSceneType();
		if( sceneType != null ) {
			for( org.lgna.project.ast.UserMethod method : sceneType.methods ) {
				if( IDE.performSceneEditorGeneratedSetUpMethodNameSet.contains( method.name.getValue() ) ) {
					return method;
				}
			}
		}
		return null;
	}

	public org.lgna.project.ast.NamedUserType getStrippedProgramType() {
		org.lgna.project.ast.NamedUserType rv = this.getProgramType();
		if( rv != null ) {
			org.lgna.project.ast.UserMethod setUpMethod = this.getPerformEditorGeneratedSetUpMethod();
			setUpMethod.body.getValue().statements.clear();
		}
		return rv;
	}
	public java.util.List< org.lgna.project.ast.FieldAccess > getFieldAccesses( final org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.FieldAccess >( org.lgna.project.ast.FieldAccess.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.FieldAccess fieldAccess ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( fieldAccess.field.getValue() );
				return fieldAccess.field.getValue() == field;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	public java.util.List< org.lgna.project.ast.MethodInvocation > getMethodInvocations( final org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.MethodInvocation >(
				org.lgna.project.ast.MethodInvocation.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.MethodInvocation methodInvocation ) {
				return methodInvocation.method.getValue() == method;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	public java.util.List< org.lgna.project.ast.ArgumentListProperty > getArgumentLists( final org.lgna.project.ast.UserCode code ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		assert programType != null;
		class ArgumentListCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
			private final java.util.List< org.lgna.project.ast.ArgumentListProperty > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
				if( crawlable instanceof org.lgna.project.ast.MethodInvocation ) {
					org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)crawlable;
					if( methodInvocation.method.getValue() == code ) {
						this.list.add( methodInvocation.arguments );
					}
				} else if( crawlable instanceof org.lgna.project.ast.InstanceCreation ) {
					org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)crawlable;
					if( instanceCreation.constructor.getValue() == code ) {
						this.list.add( instanceCreation.arguments );
					}
				}
			}
			public java.util.List< org.lgna.project.ast.ArgumentListProperty > getList() {
				return this.list;
			}
		}
		ArgumentListCrawler crawler = new ArgumentListCrawler();
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	

//	private org.alice.ide.memberseditor.Factory templatesFactory = new org.alice.ide.memberseditor.Factory();
//
//	public org.alice.ide.memberseditor.Factory getTemplatesFactory() {
//		return this.templatesFactory;
//	}
//
//	private org.alice.ide.codeeditor.Factory codeFactory = new org.alice.ide.codeeditor.Factory();
//
//	public org.alice.ide.codeeditor.Factory getCodeFactory() {
//		return this.codeFactory;
//	}
//
//	private org.alice.ide.preview.Factory previewFactory = new org.alice.ide.preview.Factory();
//
//	public org.alice.ide.preview.Factory getPreviewFactory() {
//		return this.previewFactory;
//	}

	public void refreshUbiquitousPane() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				if( IDE.this.ubiquitousPane != null ) {
					IDE.this.ubiquitousPane.refresh();
				}
			}
		} );
	}

	public org.lgna.project.ast.NamedUserType getTypeDeclaredInAliceFor( org.lgna.project.ast.JavaType superType ) {
		java.util.List< org.lgna.project.ast.NamedUserType > aliceTypes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addAliceTypes( aliceTypes, true );
		for( org.lgna.project.ast.AbstractType< ?, ?, ? > type : aliceTypes ) {
			assert type != null;
			if( type.getFirstTypeEncounteredDeclaredInJava() == superType ) {
				return (org.lgna.project.ast.NamedUserType)type;
			}
		}
		String name = "My" + superType.getName();
		return org.alice.ide.ast.AstUtilities.createType( name, superType );
	}
	public org.lgna.project.ast.NamedUserType getTypeDeclaredInAliceFor( Class< ? > superCls ) {
		return getTypeDeclaredInAliceFor( org.lgna.project.ast.JavaType.getInstance( superCls ) );

	}

	public org.lgna.croquet.components.JComponent< ? > getOverrideComponent( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.Expression expression ) {
		return null;
	}

	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		return (expression instanceof org.lgna.project.ast.TypeExpression || expression instanceof org.lgna.project.ast.ResourceExpression) == false;
	}
	public org.alice.ide.common.TypeComponent getComponentFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		//todo:
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}
	public String getTextFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return null;
	}

	protected java.util.List< ? super org.lgna.project.ast.JavaType > addPrimeTimeJavaTypes( java.util.List< ? super org.lgna.project.ast.JavaType > rv ) {
		rv.add( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE );
		rv.add( org.lgna.project.ast.JavaType.getInstance( String.class ) );
		return rv;
	}
	protected java.util.List< ? super org.lgna.project.ast.JavaType > addSecondaryJavaTypes( java.util.List< ? super org.lgna.project.ast.JavaType > rv ) {
		return rv;
	}

	protected boolean isInclusionOfTypeDesired( org.lgna.project.ast.UserType< ? > valueTypeInAlice ) {
		return true;
		//return valueTypeInAlice.methods.size() > 0 || valueTypeInAlice.fields.size() > 0;
	}

	protected java.util.List< ? super org.lgna.project.ast.NamedUserType > addAliceTypes( java.util.List< ? super org.lgna.project.ast.NamedUserType > rv, boolean isInclusionOfTypesWithoutMembersDesired ) {
		org.lgna.project.ast.NamedUserType sceneType = this.getSceneType();
		if( sceneType != null ) {
			rv.add( sceneType );
			for( org.lgna.project.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				org.lgna.project.ast.AbstractType< ?, ?, ? > valueType = field.getValueType();
				if( valueType instanceof org.lgna.project.ast.NamedUserType ) {
					org.lgna.project.ast.NamedUserType valueTypeInAlice = (org.lgna.project.ast.NamedUserType)valueType;
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

	public java.util.List< org.lgna.project.ast.JavaType > getPrimeTimeSelectableTypesDeclaredInJava() {
		java.util.List< org.lgna.project.ast.JavaType > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addPrimeTimeJavaTypes( rv );
		return rv;
	}
	public java.util.List< org.lgna.project.ast.JavaType > getSecondarySelectableTypesDeclaredInJava() {
		java.util.List< org.lgna.project.ast.JavaType > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addSecondaryJavaTypes( rv );
		return rv;
	}
	public java.util.List< org.lgna.project.ast.NamedUserType > getTypesDeclaredInAlice() {
		java.util.List< org.lgna.project.ast.NamedUserType > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.addAliceTypes( rv, true );
		return rv;
	}

	public abstract edu.cmu.cs.dennisc.javax.swing.models.TreeNode<JavaType> getClassGalleryRoot();
	protected abstract JComponent< ? > createClassGalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<JavaType> root );
	
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
	public org.lgna.croquet.components.JComponent< ? > getGalleryBrowser() {
		return this.galleryBrowser;
	}
	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

	private java.util.Map< org.lgna.project.ast.AbstractCode, org.alice.ide.instancefactory.InstanceFactory > mapCodeToInstanceFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private org.lgna.croquet.State.ValueObserver<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.State.ValueObserver<org.alice.ide.instancefactory.InstanceFactory>() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			if( nextValue != null ) {
				org.lgna.project.ast.AbstractCode code = IDE.this.getFocusedCode();
				if( code != null ) {
					mapCodeToInstanceFactory.put( code, nextValue );
				}
			}
		}
	};
	public abstract org.alice.ide.cascade.CascadeManager getCascadeManager();
	public boolean isJava() {
		return org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem() == org.alice.ide.formatter.JavaFormatter.getInstance();
	}

	private java.io.File applicationDirectory = null;
	private java.io.File galleryDirectory = null;

	protected java.io.File getDefaultApplicationRootDirectory() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			return new java.io.File( "/Applications/" + this.getApplicationName() + ".app/Contents/Resources/Java/application" );
		} else {
			return new java.io.File( "/Program Files/" + this.getApplicationName() + "3Beta/application" );
		}
	}
	protected java.io.File getDefaultGalleryRootDirectory() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			return new java.io.File( "/Applications/" + this.getApplicationName() + ".app/Contents/Resources/Java/gallery" );
		} else {
			return new java.io.File( "/Program Files/" + this.getApplicationName() + "3Beta/gallery" );
		}
	}
	public static java.io.File getPathFromProperties( String[] propertyKeys, String[] subPaths ) {
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
			this.applicationDirectory = getPathFromProperties( new String[] { "org.alice.ide.IDE.install.dir", "user.dir" }, new String[] { "application", "required/application/" + this.getVersionText() } );
			if( this.applicationDirectory != null ) {
				//pass
			} else {
				this.applicationDirectory = this.getDefaultApplicationRootDirectory();
			}
		}
		return this.applicationDirectory;
	}
	public java.io.File getGalleryRootDirectory() {
		if( this.galleryDirectory != null ) {
			//pass
		} else {
			this.galleryDirectory = getPathFromProperties( new String[] { "org.alice.ide.IDE.install.dir", "user.dir" }, new String[] { "gallery", "required/gallery/" + this.getVersionText() } );
			if( this.galleryDirectory != null ) {
				//pass
			} else {
				this.galleryDirectory = this.getDefaultGalleryRootDirectory();
			}
		}
		return this.galleryDirectory;
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
		return org.lgna.project.Version.getCurrentVersionText();
	}
	@Override
	protected String getVersionAdornment() {
		return " 3 BETA ";
	}

	private ComponentStencil stencil;
	private java.util.List< org.lgna.croquet.DropReceptor > holes = null;
	private org.lgna.croquet.components.DragComponent potentialDragSource;
	private org.lgna.croquet.components.Component< ? > currentDropReceptorComponent;

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
						for( org.lgna.croquet.DropReceptor dropReceptor : IDE.this.holes ) {
							org.lgna.croquet.components.Component< ? > component = (org.lgna.croquet.components.Component< ? >)dropReceptor;
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
					for( org.lgna.croquet.DropReceptor dropReceptor : IDE.this.holes ) {
						org.lgna.croquet.components.Component< ? > component = (org.lgna.croquet.components.Component< ? >)dropReceptor;
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

	public void showStencilOver( org.lgna.croquet.components.DragComponent potentialDragSource, final org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			this.holes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			codeEditor.addPotentialDropReceptors( this.holes, type );
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
	public void handleDragStarted( org.lgna.croquet.history.DragStep dragAndDropContext ) {
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
	public void handleDragEnteredDropReceptor( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		//		this.currentDropReceptorComponent = dragAndDropContext.getCurrentDropReceptor().getAWTComponent();
		//		if( this.stencil != null && this.holes != null ) {
		//			this.stencil.repaint();
		//		}
	}
	public void handleDragExitedDropReceptor( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.currentDropReceptorComponent = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragStopped( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.enableRendering();
	}

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserField > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserField >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			IDE.this.refreshAccessibles();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			IDE.this.refreshAccessibles();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			IDE.this.refreshAccessibles();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			IDE.this.refreshAccessibles();
		}
	};

	private org.lgna.project.ast.UserField rootField;

	private org.lgna.project.ast.NamedUserType getRootTypeDeclaredInAlice() {
		return (org.lgna.project.ast.NamedUserType)this.rootField.valueType.getValue();
	}
	protected boolean isAccessibleDesired( org.lgna.project.ast.Accessible accessible ) {
		return accessible.getValueType().isArray() == false;
	}

	
	private boolean isRespondingToRefreshAccessibles = true;
	public void refreshAccessibles() {
//		if( isRespondingToRefreshAccessibles ) {
//			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshAccessibles" );
//
//			edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getFocusedCode();
//			edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
//
//			java.util.List< edu.cmu.cs.dennisc.alice.ast.Accessible > accessibles = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//			if( this.rootField != null ) {
//				accessibles.add( this.rootField );
//				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
//					if( this.isAccessibleDesired( field ) ) {
//						accessibles.add( field );
//					}
//				}
//			}
//
//			int indexOfLastField = accessibles.size() - 1;
//			if( code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
//				edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)code;
//				for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : codeDeclaredInAlice.getParamtersProperty() ) {
//					if( this.isAccessibleDesired( parameter ) ) {
//						accessibles.add( parameter );
//					}
//				}
//				for( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable : IDE.getVariables( code ) ) {
//					if( this.isAccessibleDesired( variable ) ) {
//						accessibles.add( variable );
//					}
//				}
//				for( edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant : IDE.getConstants( code ) ) {
//					if( this.isAccessibleDesired( constant ) ) {
//						accessibles.add( constant );
//					}
//				}
//			}
//
//			int selectedIndex;
//			if( accessible != null ) {
//				selectedIndex = accessibles.indexOf( accessible );
//			} else {
//				selectedIndex = -1;
//			}
//			if( selectedIndex == -1 ) {
//				if( code != null ) {
//					accessible = this.mapCodeToAccessible.get( code );
//					selectedIndex = accessibles.indexOf( accessible );
//				}
//			}
//			if( selectedIndex == -1 ) {
//				selectedIndex = indexOfLastField;
//			}
//			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setListData( selectedIndex, accessibles );
//		}
	}
	
	private void setRootField( org.lgna.project.ast.UserField rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshAccessibles();
		org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().setValue( (NamedUserType)rootField.getValueType() );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				org.alice.ide.instancefactory.InstanceFactoryState.getInstance().setValue( org.alice.ide.instancefactory.ThisInstanceFactory.SINGLETON );
			}
		} );
	}
	@Override
	public void setProject( org.lgna.project.Project project ) {
		super.setProject( project );
		this.isRespondingToRefreshAccessibles = false;
		try {
			this.setRootField( this.getSceneField() );
		} finally {
			this.isRespondingToRefreshAccessibles = true;
			this.refreshAccessibles();
		}
	}

	public <N extends org.lgna.project.ast.AbstractNode> N createCopy( N original ) {
		org.lgna.project.ast.NamedUserType root = this.getProgramType();
		java.util.Set< org.lgna.project.ast.AbstractDeclaration > abstractDeclarations = root.createDeclarationSet();
		original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations );
		java.util.Map< Integer, org.lgna.project.ast.AbstractDeclaration > map = org.lgna.project.ast.AbstractNode.createMapOfDeclarationsThatShouldNotBeCopied( abstractDeclarations );
		org.w3c.dom.Document xmlDocument = original.encode( abstractDeclarations );
		org.lgna.project.ast.AbstractNode dst = org.lgna.project.ast.AbstractNode.decode( xmlDocument, org.lgna.project.Version.getCurrentVersionText(), map, false );

		//		if( original.isEquivalentTo( dst ) ) {
		//			return dst;
		//		} else {
		//			throw new RuntimeException( "copy not equivalent to original" );
		//		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: check copy" );
		return (N)dst;
	}
	private org.lgna.project.ast.Comment commentThatWantsFocus = null;

	public org.lgna.project.ast.Comment getCommentThatWantsFocus() {
		return this.commentThatWantsFocus;
	}
	public void setCommentThatWantsFocus( org.lgna.project.ast.Comment commentThatWantsFocus ) {
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
			org.alice.ide.croquet.models.projecturi.NewProjectOperation.getInstance().fire( new org.lgna.croquet.triggers.WindowEventTrigger( e ) );
		}
	}
	@Override
	protected void handleAbout( org.lgna.croquet.triggers.Trigger trigger ) {
		this.getAboutOperation().fire( trigger );
	}
	@Override
	protected void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger ) {
	}

	@Override
	protected void handlePreferences( org.lgna.croquet.triggers.Trigger trigger ) {
		this.getPreferencesOperation().fire( trigger );
	}
	protected void preservePreferences() {
		try {
			PreferenceManager.preservePreferences();
		} catch( java.util.prefs.BackingStoreException bse ) {
			bse.printStackTrace();
		}
	}
	@Override
	protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
		this.preservePreferences();
		org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation.getInstance().fire( trigger );
	}

//	public java.util.List< ? extends org.lgna.croquet.DropReceptor > createListOfPotentialDropReceptors( org.lgna.croquet.DragComponent source ) {
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
//					java.util.List< org.lgna.croquet.DropReceptor > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
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

	private static Iterable< org.lgna.project.ast.UserVariable > getVariables( org.lgna.project.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.UserVariable > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.UserVariable.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}
	private static Iterable< org.lgna.project.ast.UserConstant > getConstants( org.lgna.project.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.UserConstant > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.UserConstant.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}

	public org.lgna.project.ast.AbstractType< ?, ?, ? > getTypeInScope() {
		org.lgna.project.ast.AbstractCode codeInFocus = this.getFocusedCode();
		if( codeInFocus != null ) {
			return codeInFocus.getDeclaringType();
		} else {
			return null;
		}
	}

	private org.lgna.project.virtualmachine.VirtualMachine vmForSceneEditor;
	protected org.lgna.project.virtualmachine.VirtualMachine createVirtualMachineForSceneEditor() {
		return new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	}
	protected abstract void registerAdapters( org.lgna.project.virtualmachine.VirtualMachine vm );
	public final org.lgna.project.virtualmachine.VirtualMachine getVirtualMachineForSceneEditor() {
		if( this.vmForSceneEditor != null ) {
			//pass
		} else {
			this.vmForSceneEditor = this.createVirtualMachineForSceneEditor();
			this.registerAdapters( this.vmForSceneEditor );
		}
		return this.vmForSceneEditor;
	}

	public org.lgna.project.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
		return new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	}
	
	public org.lgna.project.ast.AbstractDeclaration getFocusedDeclaration() {
		if( org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().getValue() ) {
			return this.getPerformEditorGeneratedSetUpMethod();
		} else {
			org.alice.ide.croquet.models.typeeditor.DeclarationComposite item = org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().getSelectedItem();
			if( item != null ) {
				return item.getDeclaration();
			} else {
				return null;
			}
		}
	}
	public org.lgna.project.ast.AbstractCode getFocusedCode() {
		org.lgna.project.ast.AbstractDeclaration declaration = this.getFocusedDeclaration();
		if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			return (org.lgna.project.ast.AbstractCode)declaration;
		} else {
			return null;
		}
	}
	public void setFocusedCode( org.lgna.project.ast.AbstractCode nextFocusedCode ) {
		if( nextFocusedCode != null ) {
			org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().setValue( (NamedUserType)nextFocusedCode.getDeclaringType() );
			org.alice.ide.croquet.models.typeeditor.DeclarationComposite composite = org.alice.ide.croquet.models.typeeditor.DeclarationComposite.getInstance( nextFocusedCode );
			if( org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().containsItem( composite ) ) {
				//pass
			} else {
				org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addItem( composite );
			}
			org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().setSelectedItem( composite );
		}
	}
	public org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus() {
		org.lgna.project.ast.AbstractCode code = this.getFocusedCode();
		if( code != null ) {
			return this.typeEditor.getCodeEditorInFocus();
		} else {
			return null;
		}
	}
	

	@Override
	public void ensureProjectCodeUpToDate() {
		this.generateCodeForSceneSetUp();
	}

	private static final String GENERATED_CODE_WARNING = "DO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT\n\nThis code is automatically generated.  Any work you perform in this method will be overwritten.\n\nDO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT";
	private void generateCodeForSceneSetUp() {
		org.lgna.project.ast.UserMethod methodDeclaredInAlice = this.getPerformEditorGeneratedSetUpMethod();
		org.lgna.project.ast.StatementListProperty bodyStatementsProperty = methodDeclaredInAlice.body.getValue().statements;
		bodyStatementsProperty.clear();
		bodyStatementsProperty.add( new org.lgna.project.ast.Comment( GENERATED_CODE_WARNING ) );
		this.getSceneEditor().generateCodeForSetUp( bodyStatementsProperty );
	}

	@Deprecated
	public org.lgna.project.ast.NamedUserType getProgramType() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			return project.getProgramType();
		} else {
			return null;
		}
	}
	@Deprecated
	public org.lgna.project.ast.UserField getSceneField() {
		org.lgna.project.ast.NamedUserType programType = getProgramType();
		return getSceneFieldFromProgramType( programType );
	}

	@Deprecated
	protected static org.lgna.project.ast.UserField getSceneFieldFromProgramType( org.lgna.project.ast.AbstractType< ?, ?, ? > programType ) {
		if( programType instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType programAliceType = (org.lgna.project.ast.NamedUserType)programType;
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
	protected static org.lgna.project.ast.NamedUserType getSceneTypeFromProgramType( org.lgna.project.ast.AbstractType< ?, ?, ? > programType ) {
		if( programType instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.UserField sceneField = getSceneFieldFromProgramType( programType );
			return (org.lgna.project.ast.NamedUserType)sceneField.getValueType();
		} else {
			return null;
		}
	}

	@Deprecated
	public org.lgna.project.ast.NamedUserType getSceneType() {
		org.lgna.project.ast.UserField sceneField = getSceneField();
		if( sceneField != null ) {
			return (org.lgna.project.ast.NamedUserType)sceneField.getValueType();
		} else {
			return null;
		}
	}

	public String getInstanceTextForAccessible( org.lgna.project.ast.Accessible accessible ) {
		String text;
		if( accessible != null ) {
			if( accessible instanceof org.lgna.project.ast.AbstractField ) {
				org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)accessible;
				text = field.getName();
				org.lgna.project.ast.AbstractCode focusedCode = getFocusedCode();
				if( focusedCode != null ) {
					org.lgna.project.ast.AbstractType< ?, ?, ? > scopeType = focusedCode.getDeclaringType();
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

	@Deprecated
	public final boolean isAccessibleInScope( org.lgna.project.ast.Accessible accessible ) {
		return true;
	}
	@Deprecated
	public final boolean isSelectedAccessibleInScope() {
		return true;
	}

	@Override
	public void setDragInProgress( boolean isDragInProgress ) {
		super.setDragInProgress( isDragInProgress );
		this.currentDropReceptorComponent = null;
	}

	private static <E extends org.lgna.project.ast.Node> E getAncestor( org.lgna.project.ast.Node node, Class< E > cls ) {
		org.lgna.project.ast.Node ancestor = node.getParent();
		while( ancestor != null ) {
			if( cls.isAssignableFrom( ancestor.getClass() ) ) {
				break;
			} else {
				ancestor = ancestor.getParent();
			}
		}
		return (E)ancestor;
	}

	private java.util.Map< java.util.UUID, org.lgna.project.ast.Node > mapUUIDToNode = new java.util.HashMap< java.util.UUID, org.lgna.project.ast.Node >();

	protected void ensureNodeVisible( org.lgna.project.ast.Node node ) {
		org.lgna.project.ast.AbstractCode nextFocusedCode = getAncestor( node, org.lgna.project.ast.AbstractCode.class );
		if( nextFocusedCode != null ) {
			this.setFocusedCode( nextFocusedCode );
		}
	}
	private org.lgna.project.ast.Node getNodeForUUID( java.util.UUID uuid ) {
		org.lgna.project.ast.Node rv = mapUUIDToNode.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			org.lgna.project.ast.NamedUserType type = this.getProgramType();
			type.crawl( new edu.cmu.cs.dennisc.pattern.Crawler() {
				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof org.lgna.project.ast.Node ) {
						org.lgna.project.ast.Node node = (org.lgna.project.ast.Node)crawlable;
						mapUUIDToNode.put( node.getUUID(), node );
					}
				}
			}, true );
			rv = mapUUIDToNode.get( uuid );
		}
		return rv;
	}

	public org.lgna.croquet.components.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		return null;
	}

//	public org.lgna.croquet.components.Component< ? > getComponentForNode( org.lgna.project.ast.Node node, boolean scrollToVisible ) {
//		if( node instanceof org.lgna.project.ast.Statement ) {
//			final org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)node;
//			ensureNodeVisible( node );
//			org.alice.ide.common.AbstractStatementPane rv = getCodeFactory().lookup( statement );
//			if( scrollToVisible ) {
//				//todo: use ScrollUtilities.scrollRectToVisible
//				javax.swing.SwingUtilities.invokeLater( new Runnable() {
//					public void run() {
//						org.alice.ide.common.AbstractStatementPane pane = getCodeFactory().lookup( statement );
//						if( pane != null ) {
//							pane.scrollToVisible();
//						}
//					}
//				} );
//			}
//			return rv;
//		} else {
//			return null;
//		}
//	}
//	public org.lgna.croquet.components.Component< ? > getComponentForNode( java.util.UUID uuid, boolean scrollToVisible ) {
//		return getComponentForNode( getNodeForUUID( uuid ), scrollToVisible );
//	}
//	public org.lgna.croquet.components.Component< ? > getComponentForNode( java.util.UUID uuid ) {
//		return getComponentForNode( uuid, false );
//	}

	public String getApplicationSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}

	public java.io.File getMyTypesDirectory() {
		return org.alice.ide.croquet.models.ui.preferences.UserTypesDirectoryState.getInstance().getDirectoryEnsuringExistance();
	}

	public boolean isInstanceLineDesired() {
		return true;
	}

	public abstract boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType typeInAlice );
	public abstract edu.cmu.cs.dennisc.animation.Program createRuntimeProgramForMovieEncoding( org.lgna.project.virtualmachine.VirtualMachine vm, org.lgna.project.ast.NamedUserType programType, int frameRate );

	public java.util.Set< org.alice.virtualmachine.Resource > getResources() {
		org.lgna.project.Project project = this.getProject();
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

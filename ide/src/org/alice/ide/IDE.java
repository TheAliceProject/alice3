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
public abstract class IDE extends org.alice.ide.ProjectApplication {
	public static final org.lgna.croquet.Group RUN_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "f7a87645-567c-42c6-bf5f-ab218d93a226" ), "RUN_GROUP" );
	public static final org.lgna.croquet.Group EXPORT_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "624d4db6-2e1a-43c2-b1df-c0bfd6407b35" ), "EXPORT_GROUP" );

	public static final String DEBUG_PROPERTY_KEY = "org.alice.ide.DebugMode";
	private static org.alice.ide.issue.ExceptionHandler exceptionHandler;
	static {
		IDE.exceptionHandler = new org.alice.ide.issue.ExceptionHandler();

		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.IDE.isSupressionOfExceptionHandlerDesired" ) ) {
			//pass
		} else {
			Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
		}
	}

	public static IDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.Application.getActiveInstance(), IDE.class );
	}

	private final org.lgna.croquet.State.ValueListener< org.alice.ide.perspectives.ProjectPerspective > perspectiveListener = new org.lgna.croquet.State.ValueListener< org.alice.ide.perspectives.ProjectPerspective >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.perspectives.ProjectPerspective > state, org.alice.ide.perspectives.ProjectPerspective prevValue, org.alice.ide.perspectives.ProjectPerspective nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.perspectives.ProjectPerspective > state, org.alice.ide.perspectives.ProjectPerspective prevValue, final org.alice.ide.perspectives.ProjectPerspective nextValue, boolean isAdjusting ) {
			IDE.this.setPerspective( nextValue );
		}
	};

	private org.lgna.cheshire.simple.stencil.SimplePresentation simplePresentation = null;
	private org.alice.ide.stencil.PotentialDropReceptorsFeedbackView potentialDropReceptorsStencil = null;

	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( getApplicationName() );
		//initialize locale
		org.alice.ide.croquet.models.ui.locale.LocaleSelectionState.getInstance().addAndInvokeValueListener( new org.lgna.croquet.ListSelectionState.ValueListener< java.util.Locale >() {
			public void changing( org.lgna.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< java.util.Locale > state, java.util.Locale prevValue, java.util.Locale nextValue, boolean isAdjusting ) {
				org.lgna.croquet.Application.getActiveInstance().setLocale( nextValue );
			}
		} );

		this.promptForLicenseAgreements();

		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeValueListener( this.instanceFactorySelectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().addValueListener( this.isAlwaysShowingBlocksListener );
	}

	public abstract ApiConfigurationManager getApiConfigurationManager();

	@Override
	public void initialize( String[] args ) {
		super.initialize( args );
		this.getPerspectiveState().addValueListener( this.perspectiveListener );
	}

	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

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
	public org.lgna.croquet.Operation getPreferencesOperation() {
		return null;
	}
	public abstract org.lgna.croquet.ListSelectionState< org.alice.ide.perspectives.ProjectPerspective > getPerspectiveState();
	public abstract org.lgna.croquet.Operation getRunOperation();
	public abstract org.lgna.croquet.Operation getRestartOperation();

	public abstract org.lgna.croquet.Operation createPreviewOperation( org.alice.ide.members.components.templates.ProcedureInvocationTemplate procedureInvocationTemplate );

	public enum AccessorAndMutatorDisplayStyle {
		GETTER_AND_SETTER, ACCESS_AND_ASSIGNMENT
	}

	public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle( org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.AbstractType< ?, ?, ? > declaringType = field.getDeclaringType();
		if( declaringType != null && declaringType.isUserAuthored() ) {
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		} else {
			//return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		}
	}

	public abstract org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod();

	private boolean isSetupMethodCleared = false;
	public org.lgna.project.ast.NamedUserType getStrippedProgramType() {
		org.lgna.project.ast.NamedUserType rv = this.getProgramType();
		if( rv != null ) {
			org.lgna.project.ast.UserMethod setUpMethod = this.getPerformEditorGeneratedSetUpMethod();
			setUpMethod.body.getValue().statements.clear();
			this.isSetupMethodCleared = true;
		}
		return rv;
	}
	
	private static class UnacceptableFieldAccessCrawler extends edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.FieldAccess > {
		private final java.util.Set<org.lgna.project.ast.UserField> unacceptableFields;
		public UnacceptableFieldAccessCrawler( java.util.Set<org.lgna.project.ast.UserField> unacceptableFields ) {
			super( org.lgna.project.ast.FieldAccess.class );
			this.unacceptableFields = unacceptableFields;
		}
		@Override
		protected boolean isAcceptable( org.lgna.project.ast.FieldAccess fieldAccess ) {
			return this.unacceptableFields.contains( fieldAccess.field.getValue() );
		}
	}
	private String reorganizeTypeFieldsIfNecessary( org.lgna.project.ast.NamedUserType namedUserType, int startIndex, java.util.Set<org.lgna.project.ast.UserField> alreadyMovedFields ) {
		java.util.List<org.lgna.project.ast.UserField> fields = namedUserType.fields.getValue().subList( startIndex, namedUserType.fields.size() );
		java.util.Set<org.lgna.project.ast.UserField> unacceptableFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( fields );
		org.lgna.project.ast.UserField fieldToMoveToTheEnd = null;
		java.util.List< org.lgna.project.ast.FieldAccess > accessesForFieldToMoveToTheEnd = null;
		for( org.lgna.project.ast.UserField field : fields ) {
			org.lgna.project.ast.Expression initializer = field.initializer.getValue();
			UnacceptableFieldAccessCrawler crawler = new UnacceptableFieldAccessCrawler( unacceptableFields );
			initializer.crawl( crawler, org.lgna.project.ast.CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
			java.util.List< org.lgna.project.ast.FieldAccess > fieldAccesses = crawler.getList();
			if( fieldAccesses.size() > 0 ) {
				fieldToMoveToTheEnd = field;
				accessesForFieldToMoveToTheEnd = fieldAccesses;
				break;
			}
			unacceptableFields.remove( field );
		}
		if( fieldToMoveToTheEnd != null ) {
			if( alreadyMovedFields.contains( fieldToMoveToTheEnd ) ) {
				//todo: better cycle detection?
				StringBuilder sb = new StringBuilder();
				sb.append( "<html>Possible cycle detected.<br>The field <strong>\"" );
				sb.append( fieldToMoveToTheEnd.getName() );
				sb.append( "\"</strong> on type <strong>\"" );
				sb.append( fieldToMoveToTheEnd.getDeclaringType().getName() );
				sb.append( "\"</strong> is referencing: " );
				String prefix = "<strong>\"";
				for( org.lgna.project.ast.FieldAccess fieldAccess : accessesForFieldToMoveToTheEnd ) {
					org.lgna.project.ast.AbstractField accessedField = fieldAccess.field.getValue();
					sb.append( prefix );
					sb.append( accessedField.getName() );
					prefix = "\"</strong>, <strong>\"";
				}
				sb.append( "\"</strong><br>" );
				sb.append( getApplicationName() );
				sb.append( " already attempted to move it once." );
				sb.append( "<br><br><strong>Your program may fail.</strong></html>" );
				return sb.toString();
			} else {
				for( org.lgna.project.ast.FieldAccess fieldAccess : accessesForFieldToMoveToTheEnd ) {
					org.lgna.project.ast.AbstractField accessedField = fieldAccess.field.getValue();
					if( accessedField == fieldToMoveToTheEnd ) {
						StringBuilder sb = new StringBuilder();
						sb.append( "<html>The field <strong>\"" );
						sb.append( fieldToMoveToTheEnd.getName() );
						sb.append( "\"</strong> on type <strong>\"" );
						sb.append( fieldToMoveToTheEnd.getDeclaringType().getName() );
						sb.append( "\"</strong> is referencing <strong>itself</strong>." );
						sb.append( "<br><br><strong>Your program may fail.</strong></html>" );
						return sb.toString();
					}
				}
				int prevIndex = namedUserType.fields.indexOf( fieldToMoveToTheEnd );
				int nextIndex = namedUserType.fields.size() - 1;
				namedUserType.fields.slide( prevIndex, nextIndex );
				alreadyMovedFields.add( fieldToMoveToTheEnd );
				return this.reorganizeTypeFieldsIfNecessary( namedUserType, prevIndex, alreadyMovedFields );
			}
		} else {
			return null;
		}
	}
	private void reorganizeFieldsIfNecessary() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			for( org.lgna.project.ast.NamedUserType namedUserType : project.getNamedUserTypes() ) {
				java.util.Set<org.lgna.project.ast.UserField> alreadyMovedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
				String message = this.reorganizeTypeFieldsIfNecessary( namedUserType, 0, alreadyMovedFields );
				if( message != null ) {
					this.showMessageDialog( message, "Unable to Recover", org.lgna.croquet.MessageType.ERROR );
				}
			}
		}
	}
	
	@Override
	public void ensureProjectCodeUpToDate() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			if( this.isSetupMethodCleared || this.isProjectUpToDateWithFile()==false ) {
				synchronized( project.getLock() ) {
					this.generateCodeForSceneSetUp();
					this.reorganizeFieldsIfNecessary();
				}
			}
		}
	}
	public org.lgna.project.ast.NamedUserType getUpToDateProgramType() {
		org.lgna.project.Project project = this.getUpToDateProject();
		if( project != null ) {
			return project.getProgramType();
		} else {
			return null;
		}
	}

	public java.util.List< org.lgna.project.ast.FieldAccess > getFieldAccesses( final org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		return org.lgna.project.ProgramTypeUtilities.getFieldAccesses( programType, field );
	}
	public java.util.List< org.lgna.project.ast.MethodInvocation > getMethodInvocations( final org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		return org.lgna.project.ProgramTypeUtilities.getMethodInvocations( programType, method );
	}
	public java.util.List< org.lgna.project.ast.SimpleArgumentListProperty > getArgumentLists( final org.lgna.project.ast.UserCode code ) {
		org.lgna.project.ast.NamedUserType programType = this.getStrippedProgramType();
		return org.lgna.project.ProgramTypeUtilities.getArgumentLists( programType, code );
	}

	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		if( org.lgna.project.ast.AstUtilities.isKeywordExpression( expression ) ) {
			return false;
		}
		return (expression instanceof org.lgna.project.ast.TypeExpression || expression instanceof org.lgna.project.ast.ResourceExpression) == false;
	}

	private java.util.Map< org.lgna.project.ast.AbstractCode, org.alice.ide.instancefactory.InstanceFactory > mapCodeToInstanceFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private final org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
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
	private final org.lgna.croquet.State.ValueListener< Boolean > isAlwaysShowingBlocksListener = new org.lgna.croquet.State.ValueListener< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "restart required" );
		}
	};

	public abstract org.alice.ide.cascade.ExpressionCascadeManager getExpressionCascadeManager();
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

	public org.alice.ide.stencil.PotentialDropReceptorsFeedbackView getPotentialDropReceptorsFeedbackView() {
		if ( this.potentialDropReceptorsStencil == null ) {
			this.potentialDropReceptorsStencil = new org.alice.ide.stencil.PotentialDropReceptorsFeedbackView( this.getFrame() );
		}
		return this.potentialDropReceptorsStencil;
	}

	public void showDropReceptorsStencilOver( org.lgna.croquet.components.DragComponent potentialDragSource, final org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		this.getPotentialDropReceptorsFeedbackView().showStencilOver( potentialDragSource, type );
	}
	public void hideDropReceptorsStencil() {
		this.getPotentialDropReceptorsFeedbackView().hideStencil();
	}

	@Deprecated
	@Override
	public void setDragInProgress( boolean isDragInProgress ) {
		super.setDragInProgress( isDragInProgress );
		this.getPotentialDropReceptorsFeedbackView().setDragInProgress( isDragInProgress );
	}

	protected boolean isAccessibleDesired( org.lgna.project.ast.Accessible accessible ) {
		return accessible.getValueType().isArray() == false;
	}

	private void setRootField( org.lgna.project.ast.UserField rootField ) {
		org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( (org.lgna.project.ast.NamedUserType)rootField.getValueType() );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				org.lgna.project.ast.NamedUserType sceneType = IDE.this.getSceneType();
				if( sceneType != null ) {
					final int N = sceneType.fields.size();
					int i = N;
					while( i > 0 ) {
						i--;
						org.lgna.project.ast.UserField field = sceneType.fields.get( i );
						if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
							org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().setValue( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) );
							break;
						}
					}
				}
			}
		} );
		org.alice.ide.ast.AstEventManager.fireTypeHierarchyListeners();
	}

	@Override
	public void setProject( org.lgna.project.Project project ) {
		super.setProject( project );
		this.isSetupMethodCleared = false;
		org.lgna.croquet.Perspective perspective = this.getPerspective();
		if( perspective == null || perspective == org.alice.ide.perspectives.noproject.NoProjectPerspective.getInstance() ) {
			this.setPerspective( org.alice.stageide.perspectives.PerspectiveState.getInstance().getValue() );
		}

		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().pushIgnoreAstChanges();
		try {
			this.setRootField( this.getSceneField() );
		} finally {
			org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().popIgnoreAstChanges();
		}
	}

	public <N extends org.lgna.project.ast.AbstractNode> N createCopy( N original ) {
		org.lgna.project.ast.NamedUserType root = this.getProgramType();
		return org.lgna.project.ast.AstUtilities.createCopy( original, root );
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
			this.setPerspective( org.alice.ide.perspectives.noproject.NoProjectPerspective.getInstance() );
			org.alice.ide.croquet.models.projecturi.NewProjectOperation.getInstance().fire( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
		}
	}

	@Override
	protected void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger ) {
	}

	protected void preservePreferences() {
		try {
			org.lgna.croquet.preferences.PreferenceManager.preservePreferences();
		} catch( java.util.prefs.BackingStoreException bse ) {
			bse.printStackTrace();
		}
	}
	@Override
	protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
		this.preservePreferences();
		org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation.getInstance().fire( trigger );
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

	public org.lgna.project.ast.AbstractCode getFocusedCode() {
		org.lgna.project.ast.AbstractDeclaration declaration = org.alice.ide.MetaDeclarationFauxState.getInstance().getValue();
		if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			return (org.lgna.project.ast.AbstractCode)declaration;
		} else {
			return null;
		}
	}
	public void setFocusedCode( org.lgna.project.ast.AbstractCode nextFocusedCode ) {
		this.selectDeclaration( nextFocusedCode );
	}

	public void selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite declarationComposite ) {
		if( declarationComposite != null ) {
			org.lgna.project.ast.AbstractDeclaration declaration = declarationComposite.getDeclaration();
			org.lgna.project.ast.AbstractType< ?,?,? > type;
			if( declaration instanceof org.lgna.project.ast.AbstractType< ?,?,? > ) {
				type = (org.lgna.project.ast.AbstractType< ?,?,? >)declaration;
			} else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
				org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)declaration;
				type = code.getDeclaringType();
			} else {
				type = null;
			}
			if( type instanceof org.lgna.project.ast.NamedUserType ) {
				org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( (org.lgna.project.ast.NamedUserType)type );
			}
			if( org.alice.ide.declarationseditor.DeclarationTabState.getInstance().containsItem( declarationComposite ) ) {
				//pass
			} else {
				org.alice.ide.declarationseditor.DeclarationTabState.getInstance().addItem( declarationComposite );
			}
			org.alice.ide.declarationseditor.DeclarationTabState.getInstance().setValueTransactionlessly( declarationComposite );
		}
	}
	private void selectDeclaration( org.lgna.project.ast.AbstractDeclaration declaration ) {
		this.selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( declaration ) );
	}

	public org.alice.ide.codedrop.CodePanelWithDropReceptor getCodeEditorInFocus() {
		org.alice.ide.perspectives.ProjectPerspective perspective = this.getPerspectiveState().getValue();
		if( perspective != null ) {
			return perspective.getCodeDropReceptorInFocus();
		} else {
			return null;
		}
	}

	private static final String GENERATED_CODE_WARNING = "DO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT\n\nThis code is automatically generated.  Any work you perform in this method will be overwritten.\n\nDO NOT EDIT\nDO NOT EDIT\nDO NOT EDIT";
	private void generateCodeForSceneSetUp() {
		org.lgna.project.ast.UserMethod userMethod = this.getPerformEditorGeneratedSetUpMethod();
		org.lgna.project.ast.StatementListProperty bodyStatementsProperty = userMethod.body.getValue().statements;
		bodyStatementsProperty.clear();
		bodyStatementsProperty.add( new org.lgna.project.ast.Comment( GENERATED_CODE_WARNING ) );
		this.getSceneEditor().generateCodeForSetUp( bodyStatementsProperty );
		this.isSetupMethodCleared = false;
	}

	public org.lgna.project.ast.NamedUserType getProgramType() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			return project.getProgramType();
		} else {
			return null;
		}
	}
	@Deprecated
	protected static org.lgna.project.ast.UserField getSceneFieldFromProgramType( org.lgna.project.ast.NamedUserType programType ) {
		if( programType != null ) {
			if( programType.fields.size() > 0 ) {
				return programType.fields.get( 0 );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	@Deprecated
	protected static org.lgna.project.ast.NamedUserType getSceneTypeFromProgramType( org.lgna.project.ast.NamedUserType programType ) {
		org.lgna.project.ast.UserField sceneField = getSceneFieldFromProgramType( programType );
		if( sceneField != null ) {
			return (org.lgna.project.ast.NamedUserType)sceneField.getValueType();
		} else {
			return null;
		}
	}
	@Deprecated
	public org.lgna.project.ast.UserField getSceneField() {
		return getSceneFieldFromProgramType( this.getProgramType() );
	}
	@Deprecated
	public org.lgna.project.ast.NamedUserType getSceneType() {
		return getSceneTypeFromProgramType( this.getProgramType() );
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

	protected static <E extends org.lgna.project.ast.Node> E getAncestor( org.lgna.project.ast.Node node, Class< E > cls ) {
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

	protected void ensureNodeVisible( org.lgna.project.ast.Node node ) {
		org.lgna.project.ast.AbstractCode nextFocusedCode = getAncestor( node, org.lgna.project.ast.AbstractCode.class );
		if( nextFocusedCode != null ) {
			this.setFocusedCode( nextFocusedCode );
		}
	}
	public org.lgna.croquet.components.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		return null;
	}
	public org.lgna.croquet.components.Component< ? > getPrefixPaneForInstanceCreationIfAppropriate( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		return null;
	}

	public static final String getApplicationSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}

	public abstract boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType userType );

	public java.util.Set< org.lgna.common.Resource > getResources() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			return project.getResources();
		} else {
			return null;
		}
	}

	public org.lgna.cheshire.simple.stencil.SimplePresentation getSimplePresentation() {
		if ( this.simplePresentation == null ) {
			this.simplePresentation = new org.lgna.cheshire.simple.stencil.SimplePresentation( this );
		}
		return simplePresentation;
	}

	private static final Integer HIGHLIGHT_STENCIL_LAYER = javax.swing.JLayeredPane.POPUP_LAYER - 2;
	private org.alice.ide.highlight.IdeHighlightStencil highlightStencil;
	public org.alice.ide.highlight.IdeHighlightStencil getHighlightStencil() {
		if( this.highlightStencil != null ) {
			//pass
		} else {
			this.highlightStencil = new org.alice.ide.highlight.IdeHighlightStencil( this.getFrame(), HIGHLIGHT_STENCIL_LAYER );
		}
		return this.highlightStencil;
	}
}

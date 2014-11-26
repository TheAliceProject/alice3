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

	private static org.alice.ide.issue.DefaultExceptionHandler exceptionHandler;
	static {
		IDE.exceptionHandler = new org.alice.ide.issue.DefaultExceptionHandler();

		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.IDE.isSupressionOfExceptionHandlerDesired" ) ) {
			//pass
		} else {
			Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
		}
	}

	public static IDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.Application.getActiveInstance(), IDE.class );
	}

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective> perspectiveListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.perspectives.ProjectPerspective> e ) {
			IDE.this.setPerspective( e.getNextValue() );
		}
	};

	private org.alice.ide.stencil.PotentialDropReceptorsFeedbackView potentialDropReceptorsStencil;

	private java.io.File projectFileToLoadOnWindowOpened;

	private final IdeConfiguration ideConfiguration;
	private final edu.cmu.cs.dennisc.crash.CrashDetector crashDetector;

	public IDE( IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager, edu.cmu.cs.dennisc.crash.CrashDetector crashDetector ) {
		this.ideConfiguration = ideConfiguration;
		this.crashDetector = crashDetector;
		StringBuffer sb = new StringBuffer();
		sb.append( "Please Submit Bug Report: " );
		sb.append( getApplicationName() );
		IDE.exceptionHandler.setTitle( sb.toString() );
		IDE.exceptionHandler.setApplicationName( getApplicationName() );
		this.projectDocumentFrame = new ProjectDocumentFrame( ideConfiguration, apiConfigurationManager );

		//initialize locale

		org.alice.ide.croquet.models.ui.locale.LocaleState.getInstance().addAndInvokeNewSchoolValueListener( new org.lgna.croquet.event.ValueListener<java.util.Locale>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<java.util.Locale> e ) {
				org.lgna.croquet.Application.getActiveInstance().setLocale( e.getNextValue() );
			}
		} );

	}

	public IdeConfiguration getIdeConfiguration() {
		return this.ideConfiguration;
	}

	public ProjectDocumentFrame getProjectDocumentFrame() {
		return this.projectDocumentFrame;
	}

	public org.alice.stageide.perspectives.CodePerspective getCodePerspective() {
		return this.projectDocumentFrame.getCodePerspective();
	}

	public org.alice.stageide.perspectives.SetupScenePerspective getSetupScenePerspective() {
		return this.projectDocumentFrame.getSetupScenePerspective();
	}

	public final org.alice.stageide.perspectives.PerspectiveState getPerspectiveState() {
		return this.projectDocumentFrame.getPerspectiveState();
	}

	public org.lgna.croquet.Operation getSetToCodePerspectiveOperation() {
		return this.getPerspectiveState().getItemSelectionOperation( this.getCodePerspective() );
	}

	public org.lgna.croquet.Operation getSetToSetupScenePerspectiveOperation() {
		return this.getPerspectiveState().getItemSelectionOperation( this.getSetupScenePerspective() );
	}

	public void setToCodePerspectiveTransactionlessly() {
		this.getPerspectiveState().setValueTransactionlessly( this.getCodePerspective() );
	}

	public void setToSetupScenePerspectiveTransactionlessly() {
		this.getPerspectiveState().setValueTransactionlessly( this.getSetupScenePerspective() );
	}

	public boolean isInCodePerspective() {
		return this.getPerspectiveState().getValue() == this.getCodePerspective();
	}

	public boolean isInSetupScenePerspective() {
		return this.getPerspectiveState().getValue() == this.getSetupScenePerspective();
	}

	public final ApiConfigurationManager getApiConfigurationManager() {
		return this.projectDocumentFrame.getApiConfigurationManager();
	}

	private static final javax.swing.KeyStroke CAPTURE_ENTIRE_WINDOW_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.SHIFT_MASK );
	private static final javax.swing.KeyStroke CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK );
	private static final javax.swing.KeyStroke CAPTURE_RECTANGLE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, 0 );

	private void registerScreenCaptureKeyStrokes( org.lgna.croquet.views.AbstractWindow<?> window ) {
		org.alice.ide.capture.ImageCaptureComposite imageCaptureComposite = org.alice.ide.capture.ImageCaptureComposite.getInstance();
		window.getContentPane().registerKeyboardAction( imageCaptureComposite.getCaptureEntireContentPaneOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW );
		window.getContentPane().registerKeyboardAction( imageCaptureComposite.getCaptureEntireWindowOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_WINDOW_KEY_STROKE, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW );
		if( window == this.getDocumentFrame().getFrame() ) {
			//pass
		} else {
			window.getContentPane().registerKeyboardAction( imageCaptureComposite.getCaptureRectangleOperation().getImp().getSwingModel().getAction(), CAPTURE_RECTANGLE_KEY_STROKE, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW );
		}
	}

	private void unregisterScreenCaptureKeyStrokes( org.lgna.croquet.views.AbstractWindow<?> window ) {
		window.getContentPane().unregisterKeyboardAction( CAPTURE_ENTIRE_WINDOW_KEY_STROKE );
		window.getContentPane().unregisterKeyboardAction( CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE );
		window.getContentPane().unregisterKeyboardAction( CAPTURE_RECTANGLE_KEY_STROKE );
	}

	@Override
	public void pushWindow( final org.lgna.croquet.views.AbstractWindow<?> window ) {
		this.registerScreenCaptureKeyStrokes( window );
		super.pushWindow( window );
	}

	@Override
	public org.lgna.croquet.views.AbstractWindow<?> popWindow() {
		org.lgna.croquet.views.AbstractWindow<?> window = super.popWindow();
		this.unregisterScreenCaptureKeyStrokes( window );
		return window;
	}

	@Override
	public void initialize( String[] args ) {
		super.initialize( args );
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeNewSchoolValueListener( this.instanceFactorySelectionObserver );
		this.getPerspectiveState().addNewSchoolValueListener( this.perspectiveListener );
		this.registerScreenCaptureKeyStrokes( this.getDocumentFrame().getFrame() );
	}

	public abstract org.alice.ide.sceneeditor.AbstractSceneEditor getSceneEditor();

	private Theme theme;

	protected Theme createTheme() {
		return new DefaultTheme();
	}

	public final Theme getTheme() {
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

	public abstract org.lgna.croquet.Operation createPreviewOperation( org.alice.ide.members.components.templates.ProcedureInvocationTemplate procedureInvocationTemplate );

	public enum AccessorAndMutatorDisplayStyle {
		GETTER_AND_SETTER,
		ACCESS_AND_ASSIGNMENT
	}

	public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle( org.lgna.project.ast.AbstractField field ) {
		if( field != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
			if( ( declaringType != null ) && declaringType.isUserAuthored() ) {
				return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
			} else {
				//return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
				return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
			}
		} else {
			return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
		}
	}

	public abstract org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod();

	protected abstract edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.Declaration> getDeclarationFilter();

	public void crawlFilteredProgramType( edu.cmu.cs.dennisc.pattern.Crawler crawler ) {
		org.lgna.project.ast.NamedUserType programType = this.getProgramType();
		if( programType != null ) {
			programType.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE, this.getDeclarationFilter() );
		}
	}

	private static class UnacceptableFieldAccessCrawler extends edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.FieldAccess> {
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
		java.util.Set<org.lgna.project.ast.UserField> unacceptableFields = edu.cmu.cs.dennisc.java.util.Sets.newHashSet( fields );
		org.lgna.project.ast.UserField fieldToMoveToTheEnd = null;
		java.util.List<org.lgna.project.ast.FieldAccess> accessesForFieldToMoveToTheEnd = null;
		for( org.lgna.project.ast.UserField field : fields ) {
			org.lgna.project.ast.Expression initializer = field.initializer.getValue();
			UnacceptableFieldAccessCrawler crawler = new UnacceptableFieldAccessCrawler( unacceptableFields );
			initializer.crawl( crawler, org.lgna.project.ast.CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
			java.util.List<org.lgna.project.ast.FieldAccess> fieldAccesses = crawler.getList();
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
				java.util.Set<org.lgna.project.ast.UserField> alreadyMovedFields = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
				String message = this.reorganizeTypeFieldsIfNecessary( namedUserType, 0, alreadyMovedFields );
				if( message != null ) {
					new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( message )
							.title( "Unable to Recover"
							)
							.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
							.buildAndShow();
				}
			}
		}
	}

	@Override
	public void ensureProjectCodeUpToDate() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			if( this.isProjectUpToDateWithSceneSetUp() == false ) {
				synchronized( project.getLock() ) {
					this.generateCodeForSceneSetUp();
					this.reorganizeFieldsIfNecessary();
					this.updateHistoryIndexSceneSetUpSync();
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

	public java.util.List<org.lgna.project.ast.FieldAccess> getFieldAccesses( final org.lgna.project.ast.AbstractField field ) {
		return org.lgna.project.ProgramTypeUtilities.getFieldAccesses( this.getProgramType(), field, this.getDeclarationFilter() );
	}

	public java.util.List<org.lgna.project.ast.MethodInvocation> getMethodInvocations( final org.lgna.project.ast.AbstractMethod method ) {
		return org.lgna.project.ProgramTypeUtilities.getMethodInvocations( this.getProgramType(), method, this.getDeclarationFilter() );
	}

	public java.util.List<org.lgna.project.ast.SimpleArgumentListProperty> getArgumentLists( final org.lgna.project.ast.UserCode code ) {
		return org.lgna.project.ProgramTypeUtilities.getArgumentLists( this.getProgramType(), code, this.getDeclarationFilter() );
	}

	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		if( org.lgna.project.ast.AstUtilities.isKeywordExpression( expression ) ) {
			return false;
		}
		return ( ( expression instanceof org.lgna.project.ast.TypeExpression ) || ( expression instanceof org.lgna.project.ast.ResourceExpression ) ) == false;
	}

	private final java.util.Map<org.lgna.project.ast.AbstractCode, org.alice.ide.instancefactory.InstanceFactory> mapCodeToInstanceFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.instancefactory.InstanceFactory> e ) {
			org.alice.ide.instancefactory.InstanceFactory nextValue = e.getNextValue();
			if( nextValue != null ) {
				org.lgna.project.ast.AbstractCode code = IDE.this.getFocusedCode();
				if( code != null ) {
					mapCodeToInstanceFactory.put( code, nextValue );
				}
			}
		}
	};

	public abstract org.alice.ide.cascade.ExpressionCascadeManager getExpressionCascadeManager();

	public org.alice.ide.stencil.PotentialDropReceptorsFeedbackView getPotentialDropReceptorsFeedbackView() {
		if( this.potentialDropReceptorsStencil == null ) {
			this.potentialDropReceptorsStencil = new org.alice.ide.stencil.PotentialDropReceptorsFeedbackView( this.getDocumentFrame().getFrame() );
		}
		return this.potentialDropReceptorsStencil;
	}

	public void showDropReceptorsStencilOver( org.lgna.croquet.views.DragComponent potentialDragSource, final org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
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

	@Override
	public void setProject( org.lgna.project.Project project ) {
		boolean isScenePerspectiveDesiredByDefault = edu.cmu.cs.dennisc.java.lang.SystemUtilities.getBooleanProperty( "org.alice.ide.IDE.isScenePerspectiveDesiredByDefault", false );
		org.alice.ide.perspectives.ProjectPerspective defaultPerspective = isScenePerspectiveDesiredByDefault ? this.getSetupScenePerspective() : this.getCodePerspective();
		this.getPerspectiveState().setValueTransactionlessly( defaultPerspective );
		super.setProject( project );
		org.lgna.croquet.Perspective perspective = this.getPerspective();
		if( ( perspective == null ) || ( perspective == org.alice.ide.perspectives.noproject.NoProjectPerspective.getInstance() ) ) {
			this.setPerspective( this.getPerspectiveState().getValue() );
		}
	}

	public <N extends org.lgna.project.ast.Node> N createCopy( N original ) {
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

	public java.io.File getProjectFileToLoadOnWindowOpened() {
		return this.projectFileToLoadOnWindowOpened;
	}

	public void setProjectFileToLoadOnWindowOpened( java.io.File projectFileToLoadOnWindowOpened ) {
		this.projectFileToLoadOnWindowOpened = projectFileToLoadOnWindowOpened;
	}

	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
		this.promptForLicenseAgreements();
		if( this.projectFileToLoadOnWindowOpened != null ) {
			this.loadProjectFrom( this.projectFileToLoadOnWindowOpened );
			this.projectFileToLoadOnWindowOpened = null;
		}

		if( this.getUri() != null ) {
			//pass
		} else {
			this.setPerspective( org.alice.ide.perspectives.noproject.NoProjectPerspective.getInstance() );
			org.alice.ide.croquet.models.projecturi.NewProjectOperation.getInstance().fire( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
		}
	}

	@Override
	protected void handleOpenFiles( java.util.List<java.io.File> files ) {
	}

	protected void preservePreferences() {
		try {
			org.lgna.croquet.preferences.PreferenceManager.preservePreferences();
		} catch( java.util.prefs.BackingStoreException bse ) {
			bse.printStackTrace();
		}
	}

	private final org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation clearanceCheckingExitOperation = new org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation();

	@Override
	public final void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
		this.preservePreferences();
		if( this.crashDetector != null ) {
			this.crashDetector.close();
		}
		this.clearanceCheckingExitOperation.fire( trigger );
	}

	protected org.lgna.project.virtualmachine.VirtualMachine createVirtualMachineForSceneEditor() {
		return new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	}

	protected abstract void registerAdaptersForSceneEditorVm( org.lgna.project.virtualmachine.VirtualMachine vm );

	public final org.lgna.project.virtualmachine.VirtualMachine createRegisteredVirtualMachineForSceneEditor() {
		org.lgna.project.virtualmachine.VirtualMachine vm = this.createVirtualMachineForSceneEditor();
		this.registerAdaptersForSceneEditorVm( vm );
		return vm;
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
			//			org.lgna.project.ast.AbstractType<?, ?, ?> type;
			//			if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			//				type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			//			} else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			//				org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)declaration;
			//				type = code.getDeclaringType();
			//			} else {
			//				type = null;
			//			}
			//			if( type instanceof org.lgna.project.ast.NamedUserType ) {
			//				org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( (org.lgna.project.ast.NamedUserType)type );
			//			}
			org.alice.ide.declarationseditor.DeclarationTabState tabState = org.alice.ide.declarationseditor.DeclarationsEditorComposite.getInstance().getTabState();
			//			if( tabState.containsItem( declarationComposite ) ) {
			//				//pass
			//			} else {
			//				tabState.addItem( declarationComposite );
			//			}
			tabState.setValueTransactionlessly( declarationComposite );
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
	}

	public org.lgna.project.ast.NamedUserType getProgramType() {
		org.lgna.project.Project project = this.getProject();
		if( project != null ) {
			return project.getProgramType();
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
					org.lgna.project.ast.AbstractType<?, ?, ?> scopeType = focusedCode.getDeclaringType();
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

	protected static <E extends org.lgna.project.ast.Node> E getAncestor( org.lgna.project.ast.Node node, Class<E> cls ) {
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

	public org.lgna.croquet.views.AwtComponentView<?> getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		return null;
	}

	public org.lgna.croquet.views.AwtComponentView<?> getPrefixPaneForInstanceCreationIfAppropriate( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		return null;
	}

	public abstract boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType userType );

	private static final Integer HIGHLIGHT_STENCIL_LAYER = javax.swing.JLayeredPane.POPUP_LAYER - 2;
	private org.alice.ide.highlight.IdeHighlightStencil highlightStencil;

	public org.alice.ide.highlight.IdeHighlightStencil getHighlightStencil() {
		if( this.highlightStencil != null ) {
			//pass
		} else {
			this.highlightStencil = new org.alice.ide.highlight.IdeHighlightStencil( this.getDocumentFrame().getFrame(), HIGHLIGHT_STENCIL_LAYER );
		}
		return this.highlightStencil;
	}

	private final ProjectDocumentFrame projectDocumentFrame;
}

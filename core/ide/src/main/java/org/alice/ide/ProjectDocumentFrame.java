/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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

import org.alice.ide.declarationseditor.DeclarationComposite;

/**
 * @author Dennis Cosgrove
 */
public class ProjectDocumentFrame extends org.lgna.croquet.PerspectiveDocumentFrame {
	public ProjectDocumentFrame( IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager ) {
		this.apiConfigurationManager = apiConfigurationManager;

		this.noProjectPerspective = new org.alice.ide.perspectives.noproject.NoProjectPerspective( this );
		org.alice.ide.croquet.models.AliceMenuBar aliceMenuBar = new org.alice.ide.croquet.models.AliceMenuBar( this );
		this.codePerspective = new org.alice.stageide.perspectives.CodePerspective( this, aliceMenuBar );
		this.setupScenePerspective = new org.alice.stageide.perspectives.SetupScenePerspective( this, aliceMenuBar );
		this.perspectiveState = new org.alice.stageide.perspectives.PerspectiveState( this.codePerspective, this.setupScenePerspective );

		this.metaDeclarationFauxState = new MetaDeclarationFauxState( this );
		this.instanceFactoryState = new org.alice.ide.instancefactory.croquet.InstanceFactoryState( this );
		this.findComposite = new org.alice.ide.croquet.models.project.find.croquet.FindComposite( this );
		this.uploadOperations = ideConfiguration != null ? ideConfiguration.createUploadOperations( this ) : new org.lgna.croquet.Operation[ 0 ];
		this.iconFactoryManager = apiConfigurationManager.createIconFactoryManager();
	}

	private static final javax.swing.KeyStroke CAPTURE_ENTIRE_WINDOW_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.SHIFT_MASK );
	private static final javax.swing.KeyStroke CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK );
	private static final javax.swing.KeyStroke CAPTURE_RECTANGLE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F12, 0 );

	private void registerScreenCaptureKeyStrokes( org.lgna.croquet.views.AbstractWindow<?> window ) {
		org.alice.ide.capture.ImageCaptureComposite imageCaptureComposite = org.alice.ide.capture.ImageCaptureComposite.getInstance();
		window.getContentPane().registerKeyboardAction( imageCaptureComposite.getCaptureEntireContentPaneOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_CONTENT_PANE_KEY_STROKE, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW );
		window.getContentPane().registerKeyboardAction( imageCaptureComposite.getCaptureEntireWindowOperation().getImp().getSwingModel().getAction(), CAPTURE_ENTIRE_WINDOW_KEY_STROKE, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW );
		if( window == this.getFrame() ) {
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

	/*package-private*/void initialize() {
		this.registerScreenCaptureKeyStrokes( this.getFrame() );
		this.getInstanceFactoryState().addAndInvokeNewSchoolValueListener( this.instanceFactoryListener );
		org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().addNewSchoolValueListener( this.formatterListener );
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

	public void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
		this.stack.push( reasonToDisableSomeAmountOfRendering );
		org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().disableRendering( reasonToDisableSomeAmountOfRendering );
	}

	public void enableRendering() {
		if( this.stack.isEmpty() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
		} else {
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering = this.stack.pop();
			org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().enableRendering( reasonToDisableSomeAmountOfRendering );
		}
	}

	@Override
	public ProjectDocument getDocument() {
		return org.alice.ide.project.ProjectDocumentState.getInstance().getValue();
	}

	public ApiConfigurationManager getApiConfigurationManager() {
		return this.apiConfigurationManager;
	}

	public org.lgna.croquet.Operation[] getUploadOperations() {
		return this.uploadOperations;
	}

	public org.alice.ide.instancefactory.croquet.InstanceFactoryState getInstanceFactoryState() {
		return this.instanceFactoryState;
	}

	public MetaDeclarationFauxState getMetaDeclarationFauxState() {
		return this.metaDeclarationFauxState;
	}

	public org.alice.ide.perspectives.noproject.NoProjectPerspective getNoProjectPerspective() {
		return this.noProjectPerspective;
	}

	public org.alice.stageide.perspectives.CodePerspective getCodePerspective() {
		return this.codePerspective;
	}

	public org.alice.stageide.perspectives.SetupScenePerspective getSetupScenePerspective() {
		return this.setupScenePerspective;
	}

	public org.lgna.croquet.ItemState<org.alice.ide.perspectives.ProjectPerspective> getPerspectiveState() {
		return this.perspectiveState;
	}

	public boolean isInCodePerspective() {
		return this.getPerspectiveState().getValue() == this.getCodePerspective();
	}

	public boolean isInSetupScenePerspective() {
		return this.getPerspectiveState().getValue() == this.getSetupScenePerspective();
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

	public org.alice.ide.croquet.models.project.find.croquet.FindComposite getFindComposite() {
		return this.findComposite;
	}

	public org.lgna.croquet.meta.MetaState<org.lgna.project.ast.NamedUserType> getTypeMetaState() {
		if( this.typeMetaState != null ) {
			//pass
		} else {
			org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = this.declarationsEditorComposite.getTabState();
			this.typeMetaState = new org.lgna.croquet.meta.StateTrackingMetaState<org.lgna.project.ast.NamedUserType, DeclarationComposite<?, ?>>( declarationTabState ) {
				@Override
				protected org.lgna.project.ast.NamedUserType getValue( org.lgna.croquet.State<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> state ) {
					DeclarationComposite<?, ?> declarationComposite = state.getValue();
					if( declarationComposite != null ) {
						return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( declarationComposite.getType(), org.lgna.project.ast.NamedUserType.class );
					} else {
						return null;
					}
				}
			};
		}
		return this.typeMetaState;
	}

	public org.alice.ide.iconfactory.IconFactoryManager getIconFactoryManager() {
		return this.iconFactoryManager;
	}

	public org.alice.ide.declarationseditor.DeclarationsEditorComposite getDeclarationsEditorComposite() {
		return this.declarationsEditorComposite;
	}

	public org.lgna.croquet.Operation getResourcesDialogLaunchOperation() {
		return this.resourcesDialogLaunchOperation;
	}

	public org.lgna.croquet.BooleanState getStasticsFrameIsShowingState() {
		return this.stasticsFrameIsShowingState;
	}

	private static final Integer HIGHLIGHT_STENCIL_LAYER = javax.swing.JLayeredPane.POPUP_LAYER - 2;

	public org.alice.ide.highlight.IdeHighlightStencil getHighlightStencil() {
		if( this.highlightStencil != null ) {
			//pass
		} else {
			this.highlightStencil = new org.alice.ide.highlight.IdeHighlightStencil( this.getFrame(), HIGHLIGHT_STENCIL_LAYER );
		}
		return this.highlightStencil;
	}

	public org.lgna.project.ast.AbstractCode getFocusedCode() {
		org.lgna.project.ast.AbstractDeclaration declaration = this.getMetaDeclarationFauxState().getValue();
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
			org.alice.ide.declarationseditor.DeclarationTabState tabState = this.getDeclarationsEditorComposite().getTabState();
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

	public org.lgna.croquet.Operation getNewProjectOperation() {
		return this.newProjectOperation;
	}

	public org.lgna.croquet.Operation getOpenProjectOperation() {
		return this.openProjectOperation;
	}

	public org.lgna.croquet.Operation getUndoOperation() {
		return this.undoOperation;
	}

	public org.lgna.croquet.Operation getRedoOperation() {
		return this.redoOperation;
	}

	private final ApiConfigurationManager apiConfigurationManager;

	private org.lgna.croquet.meta.MetaState<org.lgna.project.ast.NamedUserType> typeMetaState;

	private final org.lgna.croquet.Operation[] uploadOperations;

	private final org.alice.ide.croquet.models.project.find.croquet.FindComposite findComposite;

	private final org.alice.ide.perspectives.noproject.NoProjectPerspective noProjectPerspective;
	private final org.alice.stageide.perspectives.CodePerspective codePerspective;
	private final org.alice.stageide.perspectives.SetupScenePerspective setupScenePerspective;

	private final org.alice.stageide.perspectives.PerspectiveState perspectiveState;

	private final MetaDeclarationFauxState metaDeclarationFauxState;

	private final org.alice.ide.instancefactory.croquet.InstanceFactoryState instanceFactoryState;

	private final org.alice.ide.iconfactory.IconFactoryManager iconFactoryManager;

	private final org.alice.ide.declarationseditor.DeclarationsEditorComposite declarationsEditorComposite = new org.alice.ide.declarationseditor.DeclarationsEditorComposite();

	private final org.lgna.croquet.Operation resourcesDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.ide.resource.manager.ResourceManagerComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.resource.manager.ResourceManagerComposite>() {
				@Override
				protected org.alice.ide.resource.manager.ResourceManagerComposite create() {
					return new org.alice.ide.resource.manager.ResourceManagerComposite( ProjectDocumentFrame.this );
				}
			}, org.lgna.croquet.Application.DOCUMENT_UI_GROUP ).getLaunchOperation();

	private final org.lgna.croquet.BooleanState stasticsFrameIsShowingState = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createInstance(
			org.lgna.croquet.Application.INFORMATION_GROUP,
			org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite>() {
				@Override
				protected org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite create() {
					return new org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite( ProjectDocumentFrame.this );
				}
			} );
	private final edu.cmu.cs.dennisc.java.util.DStack<org.alice.ide.ReasonToDisableSomeAmountOfRendering> stack = edu.cmu.cs.dennisc.java.util.Stacks.newStack();

	private final java.util.Map<org.lgna.project.ast.AbstractCode, org.alice.ide.instancefactory.InstanceFactory> mapCodeToInstanceFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactoryListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.instancefactory.InstanceFactory> e ) {
			org.alice.ide.instancefactory.InstanceFactory nextValue = e.getNextValue();
			if( nextValue != null ) {
				org.lgna.project.ast.AbstractCode code = getFocusedCode();
				if( code != null ) {
					mapCodeToInstanceFactory.put( code, nextValue );
				}
			}
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.formatter.Formatter> formatterListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.formatter.Formatter>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.formatter.Formatter> e ) {
			edu.cmu.cs.dennisc.java.awt.ComponentUtilities.revalidateTree( getFrame().getAwtComponent() );
		}
	};

	private org.alice.ide.highlight.IdeHighlightStencil highlightStencil;

	private final org.lgna.croquet.Operation newProjectOperation = new org.alice.ide.croquet.models.projecturi.NewProjectOperation( this );
	private final org.lgna.croquet.Operation openProjectOperation = new org.alice.ide.croquet.models.projecturi.OpenProjectOperation( this );

	private final org.lgna.croquet.Operation undoOperation = new org.alice.ide.croquet.models.history.UndoOperation( this );
	private final org.lgna.croquet.Operation redoOperation = new org.alice.ide.croquet.models.history.RedoOperation( this );
}

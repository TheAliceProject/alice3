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
		this.findComposite = new org.alice.ide.croquet.models.project.find.croquet.FindComposite( this );
		this.perspectiveState = new org.alice.stageide.perspectives.PerspectiveState();
		this.uploadOperations = ideConfiguration != null ? ideConfiguration.createUploadOperations( this ) : new org.lgna.croquet.Operation[ 0 ];
		org.alice.ide.croquet.models.AliceMenuBar aliceMenuBar = new org.alice.ide.croquet.models.AliceMenuBar( this );
		this.codePerspective = new org.alice.stageide.perspectives.CodePerspective( this, aliceMenuBar );
		this.setupScenePerspective = new org.alice.stageide.perspectives.SetupScenePerspective( this, aliceMenuBar );
		this.perspectiveState.addItem( this.codePerspective );
		this.perspectiveState.addItem( this.setupScenePerspective );
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

	public ApiConfigurationManager getApiConfigurationManager() {
		return this.apiConfigurationManager;
	}

	public org.lgna.croquet.Operation[] getUploadOperations() {
		return this.uploadOperations;
	}

	public org.alice.stageide.perspectives.CodePerspective getCodePerspective() {
		return this.codePerspective;
	}

	public org.alice.stageide.perspectives.SetupScenePerspective getSetupScenePerspective() {
		return this.setupScenePerspective;
	}

	public org.alice.stageide.perspectives.PerspectiveState getPerspectiveState() {
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

	private final ApiConfigurationManager apiConfigurationManager;

	private org.lgna.croquet.meta.MetaState<org.lgna.project.ast.NamedUserType> typeMetaState;

	private final org.lgna.croquet.Operation[] uploadOperations;

	private final org.alice.ide.croquet.models.project.find.croquet.FindComposite findComposite;

	private final org.alice.stageide.perspectives.CodePerspective codePerspective;
	private final org.alice.stageide.perspectives.SetupScenePerspective setupScenePerspective;

	private final org.alice.stageide.perspectives.PerspectiveState perspectiveState;

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
}

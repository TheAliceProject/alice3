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
package org.alice.stageide.sceneeditor;

import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryClassOperation;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryFileOperation;
import org.lgna.croquet.components.DragComponent;
import org.lookingglassandalice.storytelling.ImplementationAccessor;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.StatementListProperty;
import edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent;

/**
 * @author dculyba
 * 
 */
public class StorytellingSceneEditor extends AbstractSceneEditor implements
		org.lgna.croquet.DropReceptor,
		edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener {

	private static class SingletonHolder {
		private static StorytellingSceneEditor instance = new StorytellingSceneEditor();
	}
	public static StorytellingSceneEditor getInstance() {
		return SingletonHolder.instance;
	}
	private StorytellingSceneEditor() {
	}

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			StorytellingSceneEditor.this.animator.update();
		}
	};
	
	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory
			.getSingleton().createLightweightOnscreenLookingGlass();

	private class LookingGlassPanel extends
			org.lgna.croquet.components.JComponent<javax.swing.JPanel> {
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = StorytellingSceneEditor.this.onscreenLookingGlass
					.getJPanel();
			rv.setLayout(new javax.swing.SpringLayout());
			return rv;
		}
	}
	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
	
	private org.lookingglassandalice.storytelling.implementation.CameraImplementation sceneCameraImplementation;
	
	@Override
	protected void setProgramInstance(InstanceInAlice programInstance) 
	{
		super.setProgramInstance(programInstance);
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setOnscreenLookingGlass(this.onscreenLookingGlass);
	}
	
	protected void setSceneCamera(edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraField)
	{
		this.sceneCameraImplementation = getImplementation(cameraField);
	}
	
	@Override
	protected void setActiveScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		super.setActiveScene(sceneField);
		for (edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneField.getDesiredValueType().getDeclaredFields())
		{
			if( field.getDesiredValueType().isAssignableTo(org.lookingglassandalice.storytelling.Camera.class)) 
			{
				this.setSceneCamera((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field);
			}
		}
	}
	
	@Override
	public void enableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(true);
		}
	}

	@Override
	public void disableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(false);
		}
	}

	@Override
	public void putInstanceForInitializingPendingField(
			FieldDeclaredInAlice field, Object instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getInstanceInJavaForUndo(FieldDeclaredInAlice field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateCodeForSetUp( StatementListProperty bodyStatementsProperty ) {
		// TODO Auto-generated method stub

	}
	
	private boolean HACK_isDisplayableAlreadyHandled = false;
	@Override
	protected void handleDisplayable() {
		if( HACK_isDisplayableAlreadyHandled ) {
			System.err.println( "TODO: investigate is displayed" );
		} else {
			super.handleDisplayable();
			HACK_isDisplayableAlreadyHandled = true;
		}
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.addComponent( this.lookingGlassPanel, Constraint.CENTER );
	}

	@Override
	protected void handleUndisplayable() {
		this.removeComponent( this.lookingGlassPanel );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		super.handleUndisplayable();
	}

// ######### Begin implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter
	public void initialized(LookingGlassInitializeEvent e) {
	}

	public void cleared(LookingGlassRenderEvent e) {
	}

	public void rendered(LookingGlassRenderEvent e) {
		// if (this.onscreenLookingGlass.getCameraCount() > 0 &&
		// this.onscreenLookingGlass.getCameraAt(0) instanceof
		// OrthographicCamera){
		// paintHorizonLine(e.getGraphics2D(), this.onscreenLookingGlass,
		// (OrthographicCamera)this.onscreenLookingGlass.getCameraAt(0));
		// }
	}

	public void resized(LookingGlassResizeEvent e) {
		// updateCameraMarkers();
	}

	public void displayChanged(LookingGlassDisplayChangeEvent e) {
	}
// ######### End implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter

	
// ######### Begin implementation of org.lgna.croquet.DropReceptor
	public org.lgna.croquet.resolvers.CodableResolver<StorytellingSceneEditor> getCodableResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver<StorytellingSceneEditor>(
				this);
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShape(
			org.lgna.croquet.DropSite potentialDropSite) {
		return this;
	}

	public boolean isPotentiallyAcceptingOf(DragComponent source) {
		return source instanceof org.alice.stageide.gallerybrowser.GalleryDragComponent;
	}

	public org.lgna.croquet.components.JComponent<?> getViewController() {
		return this;
	}

	public void dragStarted(org.lgna.croquet.history.DragStep dragAndDropContext) {
		DragComponent dragSource = dragAndDropContext.getDragSource();
		dragSource.showDragProxy();
		org.lgna.croquet.Model model = dragSource.getLeftButtonClickModel();
		if (model instanceof GalleryFileOperation) {
			((GalleryFileOperation) model).setDesiredTransformation(null);
		}
	}

	public void dragEntered(org.lgna.croquet.history.DragStep dragAndDropContext) {
	}

	private boolean isDropLocationOverLookingGlass(
			org.lgna.croquet.history.DragStep dragAndDropContext) {
		java.awt.event.MouseEvent eSource = dragAndDropContext
				.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities
				.convertPoint(eSource.getComponent(), eSource.getPoint(),
						this.lookingGlassPanel.getAwtComponent());
		return this.lookingGlassPanel.getAwtComponent().contains(
				pointInLookingGlass);
	}

	private boolean overLookingGlass = false;

	public org.lgna.croquet.DropSite dragUpdated(
			org.lgna.croquet.history.DragStep dragAndDropContext) {
		if (isDropLocationOverLookingGlass(dragAndDropContext)) {
			if (!overLookingGlass) {
				overLookingGlass = true;
				// this.globalDragAdapter.dragEntered(dragAndDropContext);
			}
			// this.globalDragAdapter.dragUpdated(dragAndDropContext);
		} else {
			if (overLookingGlass) {
				overLookingGlass = false;
				// this.globalDragAdapter.dragExited(dragAndDropContext);
			}
		}
		return null;
	}

	public org.lgna.croquet.Model dragDropped(
			org.lgna.croquet.history.DragStep dragAndDropContext) {
		DragComponent dragSource = dragAndDropContext.getDragSource();
		if (isDropLocationOverLookingGlass(dragAndDropContext)) {
			org.lgna.croquet.Model model = dragSource.getLeftButtonClickModel();
			if (model instanceof GalleryFileOperation) {
				// AffineMatrix4x4 dropTargetPosition =
				// this.globalDragAdapter.getDropTargetTransformation();
				// ((GalleryFileOperation)model).setDesiredTransformation(dropTargetPosition);
			} else if (model instanceof GalleryClassOperation) {
				// AffineMatrix4x4 dropTargetPosition =
				// this.globalDragAdapter.getDropTargetTransformation();
				// ((GalleryClassOperation)model).setDesiredTransformation(dropTargetPosition);
			}
			return model;
		}
		return null;
	}

	public void dragExited(
			org.lgna.croquet.history.DragStep dragAndDropContext,
			boolean isDropRecipient) {
	}

	public void dragStopped(org.lgna.croquet.history.DragStep dragAndDropContext) {
		// this.globalDragAdapter.dragExited(dragAndDropContext);
	}

	public String getTutorialNoteText(org.lgna.croquet.Model model,
			org.lgna.croquet.edits.Edit<?> edit,
			org.lgna.croquet.UserInformation userInformation) {
		return "Drop...";
	}
// ######### End implementation of org.lgna.croquet.DropReceptor
	
}

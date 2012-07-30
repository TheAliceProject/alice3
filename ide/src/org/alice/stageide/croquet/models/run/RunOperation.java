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
package org.alice.stageide.croquet.models.run;

/**
 * @author Dennis Cosgrove
 */
public class RunOperation extends org.lgna.croquet.PlainDialogOperation {
	private static class SingletonHolder {
		private static RunOperation instance = new RunOperation();
	}
	public static RunOperation getInstance() {
		return SingletonHolder.instance;
	}
	private RunOperation() {
		super( org.alice.ide.IDE.RUN_GROUP, java.util.UUID.fromString( "985b3795-e1c7-4114-9819-fae4dcfe5676" ) );
		this.setSmallIcon( new RunIcon() );
	}
	
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit ) {
		rv.append( " to preview your program." );
		return rv;
	}
//	@Override
//	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, org.lgna.croquet.history.CompletionStep< ? > step, org.lgna.croquet.UserInformation userInformation ) {
//		return this.updateTutorialStepText( rv, step, step.getEdit(), userInformation );
//	}
	private transient org.alice.stageide.program.RunProgramContext programContext;
	public static final double WIDTH_TO_HEIGHT_RATIO = 16.0 / 9.0;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = (int)(DEFAULT_WIDTH/WIDTH_TO_HEIGHT_RATIO);
	private java.awt.Point location = new java.awt.Point( 100, 100 );
	private java.awt.Dimension size = null;
	@Override
	protected java.awt.Point getDesiredDialogLocation() {
		return this.location;
	}
	@Override
	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
		if( this.size != null ) {
			dialog.setSize( this.size );
		} else {
			this.programContext.getOnscreenLookingGlass().getAWTComponent().setPreferredSize( new java.awt.Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ) );
			dialog.pack();
		}
	}
	
	private class ProgramRunnable implements Runnable { 
		private final java.awt.Container awtContainer;
		public ProgramRunnable( java.awt.Container awtContainer ) {
			this.awtContainer = awtContainer;
			RunOperation.this.programContext = new org.alice.stageide.program.RunProgramContext();
			RunOperation.this.programContext.getProgramImp().setRestartAction( RunOperation.this.restartAction );
			RunOperation.this.programContext.initializeInContainer( this.awtContainer );
		}
		public void run() {
			RunOperation.this.programContext.setActiveScene();
		}		
	}
	private void startProgram( java.awt.Container awtContainer ) {
		new org.lgna.common.ComponentThread( new ProgramRunnable( awtContainer ), RunOperation.this.getName() ).start();
	}
	private java.awt.Container stopProgram() {
		if( this.programContext != null ) {
			java.awt.Container rv = this.programContext.getContainer(); 
			this.programContext.cleanUpProgram();
			this.programContext = null;
			return rv;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this );
			return null;
		}
	}
	private class RestartAction extends javax.swing.AbstractAction {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			java.awt.Container awtContainer = RunOperation.this.stopProgram();
			if( awtContainer != null ) {
				RunOperation.this.startProgram( awtContainer );
			} else {
				//todo: prompt w/ dialog that can submit world to bugs database
				String message = "Unable to restart";
				String title = null;
				org.lgna.croquet.Application.getActiveInstance().showMessageDialog( message, title, org.lgna.croquet.MessageType.ERROR );
			}
		}
	};
	private final RestartAction restartAction = new RestartAction();
	@Override
	protected void localize() {
		super.localize();
		this.restartAction.putValue( javax.swing.Action.NAME, "restart" );
	}
	
	@Override
	protected org.lgna.croquet.components.Container< ? > createContentPane( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
		final org.alice.stageide.StageIDE ide = (org.alice.stageide.StageIDE)org.alice.ide.IDE.getActiveInstance();
		if( ide.getProject() != null ) {
			org.lgna.croquet.components.BorderPanel container = new org.lgna.croquet.components.BorderPanel();
			org.lgna.croquet.components.FixedAspectRatioPanel rv = new org.lgna.croquet.components.FixedAspectRatioPanel( container, WIDTH_TO_HEIGHT_RATIO );
			rv.setBackgroundColor( java.awt.Color.BLACK );
			this.startProgram( container.getAwtComponent() );
			return rv;
		} else {
			ide.showMessageDialog( "Please open a project first." );
			return null;
		}
	}
	@Override
	protected void releaseContentPane( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		//todo: investigate		
		this.location = dialog.getLocation();
		this.size = dialog.getSize();
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "releaseContentPane" );
		step.finish();
	}
	
	@Override
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		super.handleFinally( step, dialog, contentPane );
		this.stopProgram();
	}
}

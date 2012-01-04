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
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		rv.append( "Preview your program." );
		return rv;
	}
//	@Override
//	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, org.lgna.croquet.steps.CompletionStep< ? > step, org.lgna.croquet.UserInformation userInformation ) {
//		return this.updateTutorialStepText( rv, step, step.getEdit(), userInformation );
//	}
	private transient org.lgna.story.implementation.ProgramImp programImp;
	private java.awt.Point location = new java.awt.Point( 100, 100 );
	private java.awt.Dimension size = new java.awt.Dimension( 640, 480 );
	@Override
	protected java.awt.Point getDesiredDialogLocation() {
		return this.location;
	}
	@Override
	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
		dialog.setSize( this.size );
	}
	
	private class RestartAction extends javax.swing.AbstractAction {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "todo" );
		}
	};
	private final RestartAction restartAction = new RestartAction();
	@Override
	protected void localize() {
		super.localize();
		this.restartAction.putValue( javax.swing.Action.NAME, "restart" );
	}
	
	@Override
	protected org.lgna.croquet.components.Container< ? > createContentPane( org.lgna.croquet.history.PlainDialogOperationStep step, org.lgna.croquet.components.Dialog dialog ) {
		final org.alice.stageide.StageIDE ide = (org.alice.stageide.StageIDE)org.alice.ide.IDE.getActiveInstance();
		if( ide.getProject() != null ) {
			final org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
			org.alice.stageide.program.ProgramLaunchUtilties.launchProgramInContainerForNormalPlay( rv.getAwtComponent(), this.restartAction, new org.alice.stageide.program.ProgramLaunchUtilties.LaunchObserver() {
				public void programCreated( org.lgna.story.Program program ) {
					RunOperation.this.programImp = org.lgna.story.ImplementationAccessor.getImplementation( program );
				}
			} );
			return rv;
		} else {
			ide.showMessageDialog( "Please open a project first." );
			return null;
		}
	}
	@Override
	protected void releaseContentPane( org.lgna.croquet.history.PlainDialogOperationStep step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		//todo: investigate		
		this.location = dialog.getLocation();
		this.size = dialog.getSize();
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "releaseContentPane" );
		RunOperation.this.programImp.shutDown();
		RunOperation.this.programImp = null;
		step.finish();
	}
	
	@Override
	protected void handleFinally( org.lgna.croquet.history.PlainDialogOperationStep step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		super.handleFinally( step, dialog, contentPane );
		org.lgna.story.Program program = null;
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "track program" );
		org.alice.stageide.program.ProgramLaunchUtilties.cleanUpProgram( program );
	}
}

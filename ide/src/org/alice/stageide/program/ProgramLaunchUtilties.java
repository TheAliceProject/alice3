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
package org.alice.stageide.program;

/**
 * @author Dennis Cosgrove
 */
public class ProgramLaunchUtilties {
	private ProgramLaunchUtilties() {
		throw new AssertionError();
	}
	
	public static interface LaunchObserver {
		public void programCreated( org.lgna.story.Program program );
	}
	
	private static void launchProgramInContainer( final org.lgna.project.ast.NamedUserType programType, final org.lgna.project.virtualmachine.VirtualMachine vm, final java.awt.Container container, final javax.swing.Action restartAction, final LaunchObserver launchObserver ) {
		new Thread() {
			@Override
			public void run() {
				vm.registerAnonymousAdapter( org.lgna.story.Scene.class, org.alice.stageide.ast.SceneAdapter.class );
				vm.registerAnonymousAdapter( org.lgna.story.event.MouseButtonListener.class, org.alice.stageide.apis.story.event.MouseButtonAdapter.class );
				vm.registerAnonymousAdapter( org.lgna.story.event.KeyListener.class, org.alice.stageide.apis.story.event.KeyAdapter.class );
				//String[] args = {};
				final org.lgna.project.virtualmachine.UserInstance programInstance = vm.ENTRY_POINT_createInstance( programType );
				org.lgna.story.Program program = programInstance.getJavaInstance( org.lgna.story.Program.class );
				
				if( launchObserver != null ) {
					launchObserver.programCreated( program );
				}
				
				org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( program );
				
				//edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "set restart operation" );
				//RunOperation.this.programImp.setRestartAction( RestartOperation.getInstance().getSwingModel().getAction() );
				programImp.setRestartAction( restartAction );
				
				programImp.initializeInAwtContainer( container );
				org.lgna.project.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
					public void run() {
						vm.ENTRY_POINT_invoke( programInstance, programType.methods.get( 0 ) );
					}
				} );
			}
		}.start();
	}
	
	private static org.lgna.project.ast.NamedUserType getUpToDateProgramType() {
		final org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
		if( ide != null ) {
			return ide.getUpToDateProgramType();
		} else {
			return null;
		}
	}
	private static void disableRendering() {
		org.alice.stageide.StageIDE.getActiveInstance().getPerspectiveState().getValue().disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM );
	}
	private static void enableRendering() {
		org.alice.stageide.StageIDE.getActiveInstance().getPerspectiveState().getValue().enableRendering();
	}
	
	public static void launchProgramInContainerForNormalPlay( java.awt.Container container, javax.swing.Action restartAction, LaunchObserver launchObserver ) {
		disableRendering();
		launchProgramInContainer( getUpToDateProgramType(), new org.lgna.project.virtualmachine.ReleaseVirtualMachine(), container, restartAction, launchObserver );
	}
	public static void launchProgramInContainerForVideoEncoding( java.awt.Container container, LaunchObserver launchObserver ) {
		enableRendering();
		launchProgramInContainer( getUpToDateProgramType(), new org.lgna.project.virtualmachine.ReleaseVirtualMachine(), container, null, launchObserver );
	}
	public static void cleanUpProgram( org.lgna.story.Program program ) {
		enableRendering();
	}
}

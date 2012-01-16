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

import org.alice.stageide.croquet.models.cascade.adapters.ProximityEventListenerAdapterFillIn;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProgramContext {
	private static org.lgna.project.ast.NamedUserType getUpToDateProgramType() {
		final org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
		if( ide != null ) {
			return ide.getUpToDateProgramType();
		} else {
			return null;
		}
	}

	private final org.lgna.project.virtualmachine.UserInstance programInstance;
	private final org.lgna.project.virtualmachine.VirtualMachine vm;

	public ProgramContext() {
		this.vm = this.createVirtualMachine();
		this.vm.registerAnonymousAdapter( org.lgna.story.Scene.class, org.alice.stageide.ast.SceneAdapter.class );
		this.vm.registerAnonymousAdapter( org.lgna.story.event.SceneActivationListener.class, org.alice.stageide.apis.story.event.SceneActivationAdapter.class );
		this.vm.registerAnonymousAdapter( org.lgna.story.event.MouseButtonListener.class, org.alice.stageide.apis.story.event.MouseButtonAdapter.class );
		this.vm.registerAnonymousAdapter( org.lgna.story.event.KeyListener.class, org.alice.stageide.apis.story.event.KeyAdapter.class );
		this.vm.registerAnonymousAdapter( org.lgna.story.event.CollisionListener.class, org.alice.stageide.apis.story.event.CollisionAdapter.class );
		this.vm.registerAnonymousAdapter( org.lgna.story.event.ProximityEventListener.class, ProximityEventListenerAdapterFillIn.class );
		//String[] args = {};
		final org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
		if( ide != null ) {
			org.lgna.project.ast.NamedUserType programType = getUpToDateProgramType();
			this.programInstance = vm.ENTRY_POINT_createInstance( programType );
		} else {
			throw new RuntimeException();
		}
	}
	protected org.lgna.project.virtualmachine.VirtualMachine createVirtualMachine() {
		return new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	}
	public org.lgna.project.virtualmachine.UserInstance getProgramInstance() {
		return this.programInstance;
	}
	public org.lgna.story.Program getProgram() {
		return this.programInstance.getJavaInstance( org.lgna.story.Program.class );
	}
	public org.lgna.story.implementation.ProgramImp getProgramImp() {
		return org.lgna.story.ImplementationAccessor.getImplementation( this.getProgram() );
	}
	public org.lgna.project.virtualmachine.VirtualMachine getVirtualMachine() {
		return this.vm;
	}

	private org.alice.ide.ReasonToDisableSomeAmountOfRendering rendering;
	
	protected void disableRendering() {
		this.rendering = org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM;
		org.alice.stageide.StageIDE.getActiveInstance().getPerspectiveState().getValue().disableRendering( rendering );
	}
	

	public void setActiveScene() {
		org.lgna.project.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
			public void run() {
				org.lgna.project.ast.UserField sceneField = null;
				for( org.lgna.project.ast.UserField field : programInstance.getType().fields ) {
					if( field.valueType.getValue().isAssignableTo( org.lgna.story.Scene.class ) ) {
						sceneField = field;
					}
				}
				assert sceneField != null;
				org.lgna.project.virtualmachine.UserInstance programInstance = ProgramContext.this.getProgramInstance();
				ProgramContext.this.getVirtualMachine().ENTRY_POINT_invoke( 
						programInstance, 
						org.alice.stageide.StoryApiConfigurationManager.SET_ACTIVE_SCENE_METHOD,
						programInstance.getFieldValue( sceneField )
				);
			}
		} );
	}
	public void cleanUpProgram() {
		this.getProgramImp().shutDown();
		if( this.rendering != null ) {
			org.alice.stageide.StageIDE.getActiveInstance().getPerspectiveState().getValue().enableRendering();
			this.rendering = null;
		}
	}
}
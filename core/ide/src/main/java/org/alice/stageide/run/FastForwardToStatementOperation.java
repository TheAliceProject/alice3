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
package org.alice.stageide.run;

/**
 * @author Dennis Cosgrove
 */
public class FastForwardToStatementOperation extends org.lgna.croquet.ActionOperation {
	public FastForwardToStatementOperation( org.lgna.project.ast.Statement statement ) {
		super( org.alice.ide.IDE.RUN_GROUP, java.util.UUID.fromString( "7b7bef33-917d-47a9-b8a8-9e43153dc4a4" ) );
		this.statement = statement;
	}

	@Override
	protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		RunComposite.getInstance().setFastForwardToStatementOperation( this );
		RunComposite.getInstance().getLaunchOperation().fire();
	}

	public void pre( org.alice.stageide.program.RunProgramContext runProgramContext ) {
		this.runProgramContext = runProgramContext;
		this.runProgramContext.getVirtualMachine().addStatementListener( this.statementListener );
		this.runProgramContext.getProgramImp().setSimulationSpeedFactor( 10.0 );
	}

	public void post() {
		//todo: removeStatementListener without locking
		if( this.runProgramContext != null ) {
			runProgramContext.getVirtualMachine().removeStatementListener( this.statementListener );
			runProgramContext = null;
		}
	}

	private final org.lgna.project.virtualmachine.events.StatementListener statementListener = new org.lgna.project.virtualmachine.events.StatementListener() {
		public void statementExecuting( org.lgna.project.virtualmachine.events.StatementEvent statementEvent ) {
			if( statementEvent.getStatement() == statement ) {
				if( runProgramContext != null ) {
					org.lgna.story.implementation.ProgramImp programImp = runProgramContext.getProgramImp();
					if( programImp != null ) {
						programImp.setSimulationSpeedFactor( 1.0 );
					}
					runProgramContext.getVirtualMachine().removeStatementListener( this );
					runProgramContext = null;
				}
				//} else {
				//	edu.cmu.cs.dennisc.java.util.logging.Logger.outln( statementEvent.getStatement() );
			}
		}

		public void statementExecuted( org.lgna.project.virtualmachine.events.StatementEvent statementEvent ) {
		}
	};

	private final org.lgna.project.ast.Statement statement;
	private org.alice.stageide.program.RunProgramContext runProgramContext;
}

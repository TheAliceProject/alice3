/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide.run;

import org.alice.ide.IDE;
import org.alice.stageide.program.RunProgramContext;
import org.lgna.croquet.Operation;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.Statement;
import org.lgna.project.virtualmachine.events.CountLoopIterationEvent;
import org.lgna.project.virtualmachine.events.EachInTogetherItemEvent;
import org.lgna.project.virtualmachine.events.ExpressionEvaluationEvent;
import org.lgna.project.virtualmachine.events.ForEachLoopIterationEvent;
import org.lgna.project.virtualmachine.events.StatementExecutionEvent;
import org.lgna.project.virtualmachine.events.VirtualMachineListener;
import org.lgna.project.virtualmachine.events.WhileLoopIterationEvent;
import org.lgna.story.SProgram;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class FastForwardToStatementOperation extends Operation {
  public FastForwardToStatementOperation(Statement statement) {
    super(IDE.RUN_GROUP, UUID.fromString("7b7bef33-917d-47a9-b8a8-9e43153dc4a4"));
    this.statement = statement;
  }

  @Override
  protected void performInActivity(UserActivity userActivity) {
    RunComposite.getInstance().setFastForwardToStatementOperation(this);
    RunComposite.getInstance().getLaunchOperation().fire();
  }

  public void pre(RunProgramContext runProgramContext) {
    this.runProgramContext = runProgramContext;
    this.runProgramContext.getVirtualMachine().addVirtualMachineListener(this.virtualMachineListener);
    this.runProgramContext.getProgram().setSimulationSpeedFactor(10.0);
  }

  public void post() {
    //todo: removeStatementListener without locking
    if (this.runProgramContext != null) {
      runProgramContext.getVirtualMachine().removeVirtualMachineListener(this.virtualMachineListener);
      runProgramContext = null;
    }
  }

  private final VirtualMachineListener virtualMachineListener = new VirtualMachineListener() {
    @Override
    public void statementExecuting(StatementExecutionEvent statementExecutionEvent) {
      if (statementExecutionEvent.getStatement() == statement) {
        if (runProgramContext != null) {
          SProgram program = runProgramContext.getProgram();
          if (program != null) {
            program.setSimulationSpeedFactor(1.0);
          }
          runProgramContext.getVirtualMachine().removeVirtualMachineListener(this);
          runProgramContext = null;
        }
        //} else {
        //  edu.cmu.cs.dennisc.java.util.logging.Logger.outln( statementEvent.getStatement() );
      }
    }

    @Override
    public void statementExecuted(StatementExecutionEvent statementExecutionEvent) {
    }

    @Override
    public void whileLoopIterating(WhileLoopIterationEvent whileLoopIterationEvent) {
    }

    @Override
    public void whileLoopIterated(WhileLoopIterationEvent whileLoopIterationEvent) {
    }

    @Override
    public void countLoopIterating(CountLoopIterationEvent countLoopIterationEvent) {
    }

    @Override
    public void countLoopIterated(CountLoopIterationEvent countLoopIterationEvent) {
    }

    @Override
    public void forEachLoopIterating(ForEachLoopIterationEvent forEachLoopIterationEvent) {
    }

    @Override
    public void forEachLoopIterated(ForEachLoopIterationEvent forEachLoopIterationEvent) {
    }

    @Override
    public void eachInTogetherItemExecuting(EachInTogetherItemEvent eachInTogetherItemEvent) {
    }

    @Override
    public void eachInTogetherItemExecuted(EachInTogetherItemEvent eachInTogetherItemEvent) {
    }

    @Override
    public void expressionEvaluated(ExpressionEvaluationEvent expressionEvaluationEvent) {
    }
  };

  private final Statement statement;
  private RunProgramContext runProgramContext;
}

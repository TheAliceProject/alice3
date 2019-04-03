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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class BlockStatement extends Statement {
  public BlockStatement() {
  }

  public BlockStatement(Statement... statements) {
    this.statements.add(statements);
  }

  @Override
  public void appendCode(SourceCodeGenerator generator) {
    generator.appendBlock(this);
  }

  @Override
  public boolean containsAtLeastOneEnabledReturnStatement() {
    if (!isEnabled.getValue()) {
      return false;
    }
    for (Statement statement : statements) {
      if (statement.containsAtLeastOneEnabledReturnStatement()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsAReturnForEveryPath() {
    if (!isEnabled.getValue()) {
      return false;
    }
    // Whether the block is executed sequentially or in a parallel, a single
    // step that returns will be enough.
    for (Statement statement : statements) {
      if (statement.containsAReturnForEveryPath()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsUnreachableCode() {
    if (!isEnabled.getValue()) {
      return false;
    }
    final int N = statements.size();
    boolean foundEnabledCodeAtEnd = false;
    for (int i = N - 1; i >= 0; i--) {
      Statement statement = statements.get(i);
      if (statement.containsUnreachableCode()) {
        return true;
      }
      if (foundEnabledCodeAtEnd) {
        if (statement.containsAReturnForEveryPath()) {
          return true;
        }
      } else {
        foundEnabledCodeAtEnd = statement.isEnabledNonCommment();
      }
    }
    return false;
  }

  public final StatementListProperty statements = new StatementListProperty(this);
}

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
package org.alice.ide.ast.code.edits;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.IDE;
import org.alice.ide.ast.code.SwapParametersOperation;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class SwapParametersEdit extends AbstractEdit<SwapParametersOperation> {
  private final UserMethod method;
  private final int aIndex;

  public SwapParametersEdit(UserActivity userActivity, UserCode code, int aIndex) {
    super(userActivity);
    //todo: handle constructors
    this.method = (UserMethod) code;
    this.aIndex = aIndex;
  }

  public SwapParametersEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    IDE ide = IDE.getActiveInstance();
    Project project = ide.getProject();
    UUID methodId = binaryDecoder.decodeId();
    this.method = ProgramTypeUtilities.lookupNode(project, methodId);
    this.aIndex = binaryDecoder.decodeInt();
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    binaryEncoder.encode(this.method.getId());
    binaryEncoder.encode(this.aIndex);
  }

  private void swap() {
    List<MethodInvocation> methodInvocations = IDE.getActiveInstance().getMethodInvocations(method);
    UserParameter aParam = method.requiredParameters.get(aIndex);
    UserParameter bParam = method.requiredParameters.get(aIndex + 1);
    method.requiredParameters.set(aIndex, bParam, aParam);
    for (MethodInvocation methodInvocation : methodInvocations) {
      SimpleArgument aArg = methodInvocation.requiredArguments.get(aIndex);
      SimpleArgument bArg = methodInvocation.requiredArguments.get(aIndex + 1);
      assert aArg.parameter.getValue() == aParam;
      assert bArg.parameter.getValue() == bParam;
      methodInvocation.requiredArguments.set(aIndex, bArg, aArg);
    }
  }

  @Override
  protected final void doOrRedoInternal(boolean isDo) {
    this.swap();
  }

  @Override
  protected final void undoInternal() {
    this.swap();
  }

  @Override
  protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
    UserParameter aParam = method.requiredParameters.get(aIndex);
    UserParameter bParam = method.requiredParameters.get(aIndex + 1);
    Locale locale = null;
    rv.append("Swap Parameters ");
    NodeUtilities.safeAppendRepr(rv, aParam, locale);
    rv.append(" ");
    NodeUtilities.safeAppendRepr(rv, bParam, locale);
  }
}

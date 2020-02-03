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
package org.alice.ide.croquet.edits.ast;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.IDE;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.declarationseditor.CodeComposite;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.UserType;

import javax.swing.undo.CannotUndoException;

/**
 * @author Dennis Cosgrove
 */
public final class DeclareMethodEdit extends AbstractEdit<CompletionModel> {
  private UserType<?> declaringType;
  private final String methodName;
  private AbstractType<?, ?, ?> returnType;
  private BlockStatement body;

  private transient UserMethod method;
  private transient DeclarationComposite prevDeclarationComposite;

  public DeclareMethodEdit(UserActivity userActivity, UserType<?> declaringType, String methodName, AbstractType<?, ?, ?> returnType, BlockStatement body) {
    super(userActivity);
    this.declaringType = declaringType;
    this.methodName = methodName;
    this.returnType = returnType;
    this.body = body;
  }

  public DeclareMethodEdit(UserActivity userActivity, UserType<?> declaringType, String methodName, AbstractType<?, ?, ?> returnType) {
    this(userActivity, declaringType, methodName, returnType, new BlockStatement());
  }

  public DeclareMethodEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    this.declaringType = NodeCodec.getInstance(UserType.class).decodeValue(binaryDecoder);
    this.methodName = binaryDecoder.decodeString();
    this.returnType = NodeCodec.getInstance(AbstractType.class).decodeValue(binaryDecoder);
    this.body = NodeCodec.getInstance(BlockStatement.class).decodeValue(binaryDecoder);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    NodeCodec.getInstance(UserType.class).encodeValue(binaryEncoder, this.declaringType);
    binaryEncoder.encode(this.methodName);
    NodeCodec.getInstance(AbstractType.class).encodeValue(binaryEncoder, this.returnType);
    NodeCodec.getInstance(BlockStatement.class).encodeValue(binaryEncoder, this.body);
  }

  @Override
  protected void preCopy() {
    super.preCopy();
    NodeCodec.addNodeToGlobalMap(this.body);
  }

  @Override
  protected void postCopy(AbstractEdit<?> result) {
    NodeCodec.removeNodeFromGlobalMap(this.body);
    super.postCopy(result);
  }

  public UserType<?> getDeclaringType() {
    return this.declaringType;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public AbstractType<?, ?, ?> getReturnType() {
    return this.returnType;
  }

  //  public org.lgna.project.ast.UserMethod getMethod() {
  //    return this.method;
  //  }

  public void EPIC_HACK_FOR_TUTORIAL_GENERATION_setMethod(UserMethod method) {
    this.method = method;
  }

  @Override
  protected final void doOrRedoInternal(boolean isDo) {
    if (isDo) {
      //todo: create new every time?
      this.method = new UserMethod(this.methodName, this.returnType, new UserParameter[0], this.body);
    }
    DeclarationTabState declarationTabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
    this.prevDeclarationComposite = declarationTabState.getValue();
    this.declaringType.methods.add(this.method);
    declarationTabState.setValueTransactionlessly(CodeComposite.getInstance(this.method));
  }

  @Override
  protected final void undoInternal() {
    int index = this.declaringType.methods.indexOf(this.method);
    if (index != -1) {
      this.declaringType.methods.remove(index);
      DeclarationTabState declarationTabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
      if (this.prevDeclarationComposite != null) {
        if (declarationTabState.containsItem(this.prevDeclarationComposite)) {
          declarationTabState.setValueTransactionlessly(this.prevDeclarationComposite);
        }
      }
      declarationTabState.removeAllOrphans();
    } else {
      throw new CannotUndoException();
    }
  }

  @Override
  protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
    rv.append("declare: ");
    rv.append(this.methodName);
  }
}

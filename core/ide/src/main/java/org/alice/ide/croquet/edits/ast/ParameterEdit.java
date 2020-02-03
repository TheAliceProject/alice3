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
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserParameter;

import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class ParameterEdit extends AbstractEdit<CompletionModel> {
  private final UserCode code;
  private final UserParameter parameter;
  private transient Map<SimpleArgumentListProperty, SimpleArgument> map = Maps.newHashMap();

  public ParameterEdit(UserActivity userActivity, UserCode code, UserParameter parameter) {
    super(userActivity);
    this.code = code;
    this.parameter = parameter;
  }

  public ParameterEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    this.code = NodeCodec.getInstance(UserCode.class).decodeValue(binaryDecoder);
    this.parameter = NodeCodec.getInstance(UserParameter.class).decodeValue(binaryDecoder);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    NodeCodec.getInstance(UserCode.class).encodeValue(binaryEncoder, this.code);
    NodeCodec.getInstance(UserParameter.class).encodeValue(binaryEncoder, this.parameter);
  }

  public UserCode getCode() {
    return this.code;
  }

  public UserParameter getParameter() {
    return this.parameter;
  }

  protected final NodeListProperty<UserParameter> getParametersProperty() {
    return this.code.getRequiredParamtersProperty();
  }

  protected void addParameter(int index) {
    NodeListProperty<UserParameter> parametersProperty = this.getParametersProperty();
    //todo
    UserCode code = (UserCode) parametersProperty.getOwner();
    AstUtilities.addParameter(this.map, parametersProperty, this.parameter, index, IDE.getActiveInstance().getArgumentLists(code));
    ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
  }

  protected void removeParameter(int index) {
    NodeListProperty<UserParameter> parametersProperty = this.getParametersProperty();
    //todo
    UserCode code = (UserCode) parametersProperty.getOwner();
    AstUtilities.removeParameter(this.map, parametersProperty, this.parameter, index, IDE.getActiveInstance().getArgumentLists(code));
    ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
  }

  @Override
  public boolean canUndo() {
    return true;
  }

  @Override
  public boolean canRedo() {
    return true;
  }
}

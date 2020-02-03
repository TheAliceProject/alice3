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

package org.alice.ide.ast.draganddrop;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Objects;
import org.alice.ide.croquet.codecs.PropertyOfNodeCodec;
import org.alice.ide.declarationseditor.CodeComposite;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.DropSite;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.Node;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropSite implements DropSite {
  private final ExpressionProperty expressionProperty;

  public ExpressionPropertyDropSite(ExpressionProperty expressionProperty) {
    this.expressionProperty = expressionProperty;
  }

  public ExpressionPropertyDropSite(BinaryDecoder binaryDecoder) {
    PropertyOfNodeCodec<ExpressionProperty> codec = PropertyOfNodeCodec.getInstance(ExpressionProperty.class);
    this.expressionProperty = codec.decodeValue(binaryDecoder);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    PropertyOfNodeCodec<ExpressionProperty> codec = PropertyOfNodeCodec.getInstance(ExpressionProperty.class);
    codec.encodeValue(binaryEncoder, this.expressionProperty);
  }

  public ExpressionProperty getExpressionProperty() {
    return this.expressionProperty;
  }

  @Override
  public DropReceptor getOwningDropReceptor() {
    Node node = (Node) this.expressionProperty.getOwner();
    AbstractCode code = node.getFirstAncestorAssignableTo(AbstractCode.class);
    return CodeComposite.getInstance(code).getView().getCodePanelWithDropReceptor().getDropReceptor();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ExpressionPropertyDropSite) {
      ExpressionPropertyDropSite epds = (ExpressionPropertyDropSite) o;
      return Objects.equals(this.expressionProperty, epds.expressionProperty);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int rv = 17;
    if (this.expressionProperty != null) {
      rv = (37 * rv) + this.expressionProperty.hashCode();
    }
    return rv;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getName());
    sb.append("[expressionProperty=");
    sb.append(this.expressionProperty);
    sb.append("]");
    return sb.toString();
  }
}

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
package org.alice.ide.members.components.templates;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.croquet.views.DragComponent;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;

import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class TemplateFactory {
  private TemplateFactory() {
    throw new AssertionError();
  }

  private static Map<AbstractMethod, DragComponent> mapMethodToProcedureInvocationTemplate = Maps.newHashMap();
  private static Map<AbstractMethod, DragComponent> mapMethodToFunctionInvocationTemplate = Maps.newHashMap();

  private static Map<AbstractField, DragComponent> mapMethodToAccessorTemplate = Maps.newHashMap();
  private static Map<AbstractField, DragComponent> mapMethodToAccessArrayAtIndexTemplate = Maps.newHashMap();
  private static Map<AbstractField, DragComponent> mapMethodToArrayLengthTemplate = Maps.newHashMap();

  private static Map<AbstractField, DragComponent> mapMethodToMutatorTemplate = Maps.newHashMap();
  private static Map<AbstractField, DragComponent> mapMethodToMutateArrayAtIndexTemplate = Maps.newHashMap();

  public static DragComponent getProcedureInvocationTemplate(AbstractMethod method) {
    DragComponent rv = mapMethodToProcedureInvocationTemplate.get(method);
    if (rv != null) {
      //pass
    } else {
      rv = new ProcedureInvocationTemplate(method);
      mapMethodToProcedureInvocationTemplate.put(method, rv);
    }
    return rv;
  }

  public static DragComponent getFunctionInvocationTemplate(AbstractMethod method) {
    DragComponent rv = mapMethodToFunctionInvocationTemplate.get(method);
    if (rv != null) {
      //pass
    } else {
      rv = new FunctionInvocationTemplate(method);
      mapMethodToFunctionInvocationTemplate.put(method, rv);
    }
    return rv;
  }

  public static DragComponent getMethodInvocationTemplate(AbstractMethod method) {
    if (method.isProcedure()) {
      return getProcedureInvocationTemplate(method);
    } else {
      return getFunctionInvocationTemplate(method);
    }
  }

  public static DragComponent getAccessorTemplate(AbstractField field) {
    DragComponent rv = mapMethodToAccessorTemplate.get(field);
    if (rv != null) {

    } else {
      rv = new GetterTemplate(field);
      mapMethodToAccessorTemplate.put(field, rv);
    }
    return rv;
  }

  public static DragComponent getAccessArrayAtIndexTemplate(AbstractField field) {
    DragComponent rv = mapMethodToAccessArrayAtIndexTemplate.get(field);
    if (rv != null) {

    } else {
      rv = new AccessFieldArrayAtIndexTemplate(field);
      mapMethodToAccessArrayAtIndexTemplate.put(field, rv);
    }
    return rv;
  }

  public static DragComponent getArrayLengthTemplate(AbstractField field) {
    DragComponent rv = mapMethodToArrayLengthTemplate.get(field);
    if (rv != null) {

    } else {
      rv = new FieldArrayLengthTemplate(field);
      mapMethodToArrayLengthTemplate.put(field, rv);
    }
    return rv;
  }
}

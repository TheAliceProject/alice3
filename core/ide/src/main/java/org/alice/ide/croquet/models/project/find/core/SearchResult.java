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
package org.alice.ide.croquet.models.project.find.core;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.lgna.project.ast.*;

import javax.swing.Icon;
import java.util.List;

/**
 * @author Matt May
 */
public class SearchResult {

  private final AbstractDeclaration declaration;
  private final List<Expression> references = Lists.newArrayList();

  @SuppressWarnings("unchecked") private static final List<Class<? extends AbstractDeclaration>> clsList = Lists.newArrayList(AbstractField.class, AbstractMethod.class, UserParameter.class, UserLocal.class);

  public SearchResult(AbstractDeclaration object) {
    assert checkClass(object);
    this.declaration = object;
  }

  private boolean checkClass(AbstractDeclaration object) {
    for (Class<?> cls : clsList) {
      if (cls.isAssignableFrom(object.getClass())) {
        return true;
      }
    }
    return false;
  }

  public AbstractDeclaration getDeclaration() {
    return this.declaration;
  }

  public String getName() {
    return declaration.getName();
  }

  public void addReference(Expression reference) {
    references.add(reference);
  }

  public List<Expression> getReferences() {
    return references;
  }

  public Icon getIcon() {
    //TODO Add icon for AbstractParameter and UserLocal?
    return switch (declaration) {
      case AbstractMethod method ->
          method.isProcedure() ? DeclarationTabState.getProcedureIcon() : DeclarationTabState.getFunctionIcon();
      case AbstractField ignored -> DeclarationTabState.getFieldIcon();
      case AbstractConstructor ignored -> DeclarationTabState.getConstructorIcon();
      default -> {
        Logger.info("No icon set for declaration type: ", declaration.getClass().getSimpleName());
        yield null;
      }
    };
  }

  public void stencilHighlightForReference(Expression reference) {
    assert reference != null;
    switch (declaration) {
      case AbstractField ignored -> {
        assert reference instanceof FieldAccess;
        IDE.getActiveInstance().getDocumentFrame().getHighlightStencil().showHighlightOverExpression(reference);
      }
      case AbstractMethod ignored -> {
        assert reference instanceof MethodInvocation;
        Statement statement = reference.getFirstAncestorAssignableTo(Statement.class);
        assert statement != null;
        IDE.getActiveInstance().getDocumentFrame().getHighlightStencil().showHighlightOverStatement(statement);
      }
      case UserParameter ignored -> {
        assert reference instanceof ParameterAccess;
        IDE.getActiveInstance().getDocumentFrame().getHighlightStencil().showHighlightOverExpression(reference);
      }
      case UserLocal ignored -> {
        assert reference instanceof LocalAccess;
        IDE.getActiveInstance().getDocumentFrame().getHighlightStencil().showHighlightOverExpression(reference);
      }
      default -> {
        assert false : declaration.getClass();
      }
    }
  }
}

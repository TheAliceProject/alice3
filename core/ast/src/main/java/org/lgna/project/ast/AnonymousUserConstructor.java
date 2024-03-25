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

import org.lgna.project.annotations.Visibility;
import org.lgna.project.virtualmachine.VirtualMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class AnonymousUserConstructor extends UserConstructor {
  private static Map<AnonymousUserType, AnonymousUserConstructor> s_map;

  public static AnonymousUserConstructor get(AnonymousUserType type) {
    if (type != null) {
      if (s_map == null) {
        s_map = new HashMap<AnonymousUserType, AnonymousUserConstructor>();
      }
      AnonymousUserConstructor rv = s_map.get(type);
      if (rv == null) {
        rv = new AnonymousUserConstructor(type);
      }
      return rv;
    } else {
      return null;
    }
  }

  @Override
  public Object evaluate(VirtualMachine vm, AbstractType fallbackType, Object[] arguments) {
    throw new RuntimeException("todo");
  }

  private ArrayList<AbstractParameter> parameters = new ArrayList<AbstractParameter>();

  private AnonymousUserConstructor(AnonymousUserType type) {
    this.type = type;
  }

  @Override
  public boolean isSignatureLocked() {
    return true;
  }

  @Override
  public AccessLevel getAccessLevel() {
    return null;
  }

  @Override
  public AnonymousUserType getDeclaringType() {
    return this.type;
  }

  @Override
  public AbstractCode getNextLongerInChain() {
    return null;
  }

  @Override
  public AbstractCode getNextShorterInChain() {
    return null;
  }

  @Override
  public List<AbstractParameter> getRequiredParameters() {
    return this.parameters;
  }

  @Override
  public Visibility getVisibility() {
    return null;
  }

  @Override
  public void process(AstProcessor processor) {
    //todo?
  }

  private final AnonymousUserType type;
}

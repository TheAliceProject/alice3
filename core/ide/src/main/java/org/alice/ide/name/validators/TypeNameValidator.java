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

package org.alice.ide.name.validators;

import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.story.SThing;

public class TypeNameValidator extends NodeNameValidator {
  public TypeNameValidator() {
    super(null);
  }

  @Override
  public boolean isNameAvailable(String name) {
    return !isGalleryClassName(name) && !isSystemClassName(name) && !isAliceClassName(name);
  }

  // This catches Cube, Biped, and other types that exist in Alice. It also rejects Thing
  // and Turnable, which are not problems. This takes the more cautious approach, rejecting
  // some valid, but perhaps confusing, names.
  private boolean isAliceClassName(String name) {
    return isSystemClassName("S" + name);
  }

  private boolean isSystemClassName(String name) {
    try {
      Class c = Class.forName("org.lgna.story." + name);
      return SThing.class.isAssignableFrom(c);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private boolean isGalleryClassName(String name) {
    return hasNodeNamed(TreeUtilities.getTreeBasedOnClassHierarchy(), name);
  }

  private boolean hasNodeNamed(ResourceNode node, String name) {
    if (name.equals(node.getResourceKey().getInternalName())) {
      return true;
    }
    return node.getNodeChildren().stream().anyMatch(child -> hasNodeNamed(child, name));
  }
}

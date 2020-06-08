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

package org.lgna.story.resources;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.serialization.tweedle.Encoder;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.code.InstantiableTweedleNode;
import org.lgna.project.code.IdentifiableTweedleNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Dennis Cosgrove
 */
public class JointId implements InstantiableTweedleNode, IdentifiableTweedleNode {

  private final JointId parent;
  private final Class<? extends JointedModelResource> containingClass;
  private Field fld;

  public JointId(JointId parent, Class<? extends JointedModelResource> containingClass) {
    this.parent = parent;
    this.containingClass = containingClass;
  }

  public JointId getParent() {
    return this.parent;
  }

  public Visibility getVisibility() {
    Field jointField = getPublicStaticFinalFld();
    if (jointField != null && jointField.isAnnotationPresent(FieldTemplate.class)) {
      FieldTemplate propertyFieldTemplate = jointField.getAnnotation(FieldTemplate.class);
      return propertyFieldTemplate.visibility();
    }
    return Visibility.PRIME_TIME;
  }

  public Field getPublicStaticFinalFld() {
    if (this.fld != null) {
      //pass
    } else if (this.containingClass != null) {
      for (Field fld : this.containingClass.getFields()) {
        int modifiers = fld.getModifiers();
        if (Modifier.isPublic(modifiers)) {
          if (Modifier.isStatic(modifiers)) {
            if (Modifier.isFinal(modifiers)) {
              try {
                Object o = fld.get(null);
                if (o == this) {
                  this.fld = fld;
                  break;
                }
              } catch (IllegalAccessException iae) {
                Logger.throwable(iae, fld);
              }
            }
          }
        }
      }
    }
    return this.fld;
  }

  public int hierarchyDepth() {
    return getParent() == null ? 0 : 1 + getParent().hierarchyDepth();
  }

  @Override
  public String toString() {
    Field fld = this.getPublicStaticFinalFld();
    if (fld != null) {
      return fld.getName();
    } else {
      return super.toString();
    }
  }

  public int descendantComparison(JointId b) {
    if (isMyDescendant(b)) {
      return 1;
    }
    if (b.isMyDescendant(this)) {
      return -1;
    }
    return 0;
  }

  private boolean isMyDescendant(JointId b) {
    return b != null && (b.parent == this || isMyDescendant(b.parent));
  }


  protected String getJointName(Encoder encoder) {
    return toString();
  }

  @Override
  public void encodeDefinition(Encoder encoder) {
    encoder.appendNewJointId(getJointName(encoder),
                             parent == null ? "null" : parent.getCodeIdentifier(encoder));
  }

  @Override
  public String getCodeIdentifier(Encoder encoder) {
    return encoder.getFieldReference(containingClass.getSimpleName(), getJointName(encoder));
  }
}

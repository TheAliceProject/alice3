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
package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.pattern.HowMuch;

import java.util.LinkedList;

/**
 * @author Dennis Cosgrove
 */
public class HierarchyUtilities {
  private HierarchyUtilities() {
    throw new AssertionError();
  }

  public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.COMPONENT_AND_DESCENDANTS;

  private static boolean isAcceptedByAll(AwtComponentView<?> component, Criterion<?>... criterions) {
    if (criterions != null) {
      for (Criterion criterion : criterions) {
        if (!criterion.accept(component)) {
          return false;
        }
      }
    }
    return true;
  }

  private static <E extends AwtComponentView<?>> E getFirstToAccept(boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    assert component != null;
    E rv = null;
    if (isComponentACandidate && (cls == null || cls.isAssignableFrom(component.getClass())) && isAcceptedByAll(component, criterions)) {
      rv = (E) component;
    } else {
      if (isChildACandidate && component instanceof AwtContainerView<?>) {
        AwtContainerView<?> container = (AwtContainerView<?>) component;
        for (AwtComponentView<?> componentI : container.getComponents()) {
          rv = getFirstToAccept(isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, componentI, cls, criterions);
          if (rv != null) {
            break;
          }
        }
      }
    }
    return rv;
  }

  private static <E extends AwtComponentView<?>> void updateAllToAccept(boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.util.List<E> list, AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    assert component != null;

    if (isComponentACandidate && (cls == null || cls.isAssignableFrom(component.getClass())) && isAcceptedByAll(component, criterions)) {
      list.add((E) component);
    }

    if (isChildACandidate) {
      if (component instanceof AwtContainerView<?>) {
        AwtContainerView<?> container = (AwtContainerView<?>) component;
        for (AwtComponentView<?> componentI : container.getComponents()) {
          updateAllToAccept(isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions);
        }
      }
    }
  }

  private static <E extends AwtComponentView<?>> E getFirstToAccept(HowMuch candidateMask, AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    return getFirstToAccept(candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions);
  }

  private static <E extends AwtComponentView<?>> void updateAllToAccept(HowMuch candidateMask, java.util.List<E> list, AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    updateAllToAccept(candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions);
  }

  public static <E extends AwtComponentView<?>> E findFirstMatch(AwtComponentView<?> component, HowMuch howMuch, Class<E> cls, Criterion<?>... criterions) {
    return HierarchyUtilities.getFirstToAccept(howMuch, component, cls, criterions);
  }

  public static <E extends AwtComponentView<?>> E findFirstMatch(AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    return findFirstMatch(component, DEFAULT_HOW_MUCH, cls, criterions);
  }

  public static <E extends AwtComponentView<?>> E findFirstMatch(AwtComponentView<?> component, Class<E> cls) {
    return findFirstMatch(component, cls, (Criterion<?>[]) null);
  }

  public static AwtComponentView<?> findFirstMatch(AwtComponentView<?> component, Criterion<?>... criterions) {
    return findFirstMatch(component, null, criterions);
  }

  public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches(AwtComponentView<?> component, HowMuch howMuch, Class<E> cls, Criterion<?>... criterions) {
    java.util.List<E> list = new LinkedList<E>();
    HierarchyUtilities.updateAllToAccept(howMuch, list, component, cls, criterions);
    return list;
  }

  public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches(AwtComponentView<?> component, Class<E> cls, Criterion<?>... criterions) {
    return findAllMatches(component, DEFAULT_HOW_MUCH, cls, criterions);
  }

  public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches(AwtComponentView<?> component, Class<E> cls) {
    return findAllMatches(component, cls, (Criterion<?>[]) null);
  }

  public static java.util.List<AwtComponentView<?>> findAllMatches(AwtComponentView<?> component, Criterion<?>... criterions) {
    return findAllMatches(component, null, criterions);
  }

  public static java.util.List<AwtComponentView<?>> findAllMatches(AwtComponentView<?> component) {
    return findAllMatches(component, null, (Criterion<?>[]) null);
  }

  public static <E extends AwtComponentView<?>> E findFirstAncestor(AwtComponentView<?> component, boolean isComponentIncludedInSearch, Class<E> cls, Criterion<?>... criterions) {
    AwtComponentView<?> c;
    if (isComponentIncludedInSearch) {
      c = component;
    } else {
      c = component.getParent();
    }
    while (c != null) {
      boolean isAcceptedByAll;
      if ((cls == null || cls.isAssignableFrom(c.getClass())) && isAcceptedByAll(component, criterions)) {
        return (E) c;
      }
      c = c.getParent();
    }
    return null;
  }

  public static <E extends AwtComponentView<?>> E findFirstAncestor(AwtComponentView<?> component, boolean isComponentIncludedInSearch, Class<E> cls) {
    return findFirstAncestor(component, isComponentIncludedInSearch, cls, (Criterion<?>[]) null);
  }

  public static AwtComponentView<?> findFirstAncestor(AwtComponentView<?> component, boolean isComponentIncludedInSearch, Criterion<?>... criterions) {
    return findFirstAncestor(component, isComponentIncludedInSearch, null, criterions);
  }
}

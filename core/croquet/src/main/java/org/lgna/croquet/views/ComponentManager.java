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

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Queues;
import org.lgna.croquet.Manager;
import org.lgna.croquet.Model;

import java.util.Map;
import java.util.Queue;

/**
 * @author Dennis Cosgrove
 */
public class ComponentManager {
  private ComponentManager() {
    throw new AssertionError();
  }

  public static final Map<Model, Queue<SwingComponentView<?>>> map = Maps.newHashMap();

  public static Queue<SwingComponentView<?>> getComponents(Model model) {
    synchronized (map) {
      Queue<SwingComponentView<?>> rv = map.get(model);
      if (rv != null) {
        //pass
      } else {
        rv = Queues.newConcurrentLinkedQueue();
        map.put(model, rv);
      }
      return rv;
    }
  }

  public static void addComponent(Model model, SwingComponentView<?> component) {
    synchronized (map) {
      Queue<SwingComponentView<?>> components = getComponents(model);
      if (components.size() == 0) {
        Manager.registerModel(model);
      }
      components.add(component);
      //  component.getAwtComponent().setEnabled( this.isEnabled );
      //component.setToolTipText( model.getToolTipText() );
    }
  }

  public static void removeComponent(Model model, SwingComponentView<?> component) {
    synchronized (map) {
      Queue<SwingComponentView<?>> components = getComponents(model);
      components.remove(component);
      if (components.size() == 0) {
        Manager.unregisterModel(model);
      }
    }
  }

  /* package-private */
  static void repaintAllComponents(Model model) {
    synchronized (map) {
      for (SwingComponentView<?> component : getComponents(model)) {
        component.repaint();
      }
    }
  }

  public static void revalidateAndRepaintAllComponents(Model model) {
    synchronized (map) {
      for (SwingComponentView<?> component : getComponents(model)) {
        component.revalidateAndRepaint();
      }
    }
  }

  @Deprecated
  public static <J extends SwingComponentView<?>> J getFirstComponent(Model model, Class<J> cls, boolean isVisibleAcceptable) {
    Iterable<SwingComponentView<?>> components = getComponents(model);

    //    edu.cmu.cs.dennisc.print.PrintUtilities.println( "getFirstComponent:", this );
    //    edu.cmu.cs.dennisc.print.PrintUtilities.println( "count:", this.components.size() );
    for (SwingComponentView<?> component : components) {
      if (cls.isAssignableFrom(component.getClass())) {
        if (component.getAwtComponent().isShowing()) {
          //          edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
          return cls.cast(component);
        }
      }
    }
    if (isVisibleAcceptable) {
      for (SwingComponentView<?> component : components) {
        if (cls.isAssignableFrom(component.getClass())) {
          if (component.getAwtComponent().isVisible()) {
            //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "isShowing:", component.getAwtComponent().getClass() );
            return cls.cast(component);
          }
        }
      }
    }
    return null;
  }

  @Deprecated
  public static SwingComponentView<?> getFirstComponent(Model model, boolean isVisibleAcceptable) {
    return getFirstComponent(model, SwingComponentView.class, isVisibleAcceptable);
  }

  @Deprecated
  public static SwingComponentView<?> getFirstComponent(Model model) {
    return getFirstComponent(model, false);
  }
}

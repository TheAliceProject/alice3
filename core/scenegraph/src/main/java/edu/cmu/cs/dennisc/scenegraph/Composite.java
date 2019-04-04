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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Visitor;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent;
import edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener;

import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class Composite extends Component {
  @Override
  public void accept(Visitor visitor) {
    super.accept(visitor);
    for (Component child : this.children) {
      child.accept(visitor);
    }
  }

  @Override
  protected void actuallyRelease() {
    super.actuallyRelease();
    for (Component child : this.children) {
      child.release();
    }
  }

  public boolean isAncestorOf(Component component) {
    if (component == null) {
      return false;
    } else {
      return component.isDescendantOf(this);
    }
  }

  protected void fireChildAdded(Component child) {
    assert child != this;
    this.children.add(child);
    ComponentAddedEvent e = new ComponentAddedEvent(this, child);
    for (ComponentsListener childrenListener : this.childrenListeners) {
      childrenListener.componentAdded(e);
    }
  }

  protected void fireChildRemoved(Component child) {
    this.children.remove(child);
    ComponentRemovedEvent e = new ComponentRemovedEvent(this, child);
    for (ComponentsListener childrenListener : this.childrenListeners) {
      childrenListener.componentRemoved(e);
    }
  }

  public void addComponent(Component component) {
    assert component != this;
    component.setParent(this);
  }

  public void removeComponent(Component component) {
    if (component.getParent() == this) {
      component.setParent(null);
    } else {
      throw new RuntimeException();
    }
  }

  public Iterable<Component> getComponents() {
    return this.children;
  }

  public int getComponentCount() {
    return this.children.size();
  }

  public int getIndexOfComponent(Component component) {
    return this.children.indexOf(component);
  }

  public Component getComponentAt(int i) {
    return this.children.get(i);
  }

  public Component[] getComponentsAsArray() {
    return this.children.toArray(new Component[this.children.size()]);
  }

  public void addChildrenListener(ComponentsListener childrenListener) {
    this.childrenListeners.add(childrenListener);
  }

  public void removeChildrenListener(ComponentsListener childrenListener) {
    this.childrenListeners.remove(childrenListener);
  }

  public Iterable<ComponentsListener> getChildrenListeners() {
    return this.childrenListeners;
  }

  @Override
  protected void fireAbsoluteTransformationChange() {
    super.fireAbsoluteTransformationChange();
    for (Component child : this.children) {
      child.fireAbsoluteTransformationChange();
    }
  }

  @Override
  protected void fireHierarchyChanged() {
    super.fireHierarchyChanged();
    for (Component child : this.children) {
      child.fireHierarchyChanged();
    }
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    super.encode(binaryEncoder, map);
    binaryEncoder.encode(this.children.size());
    for (Component component : this.children) {
      binaryEncoder.encode(component, map);
    }
  }

  @Override
  public void decode(BinaryDecoder binaryDecoder, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    super.decode(binaryDecoder, map);
    final int N = binaryDecoder.decodeInt();
    for (int i = 0; i < N; i++) {
      this.addComponent((Component) binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable(map));
    }
  }

  @Override
  public Element newCopy() {
    Composite rv = (Composite) super.newCopy();
    for (Component component : this.children) {
      Component rvComponent = (Component) component.newCopy();
      rvComponent.setParent(rv);
    }
    return rv;
  }

  private final List<Component> children = Lists.newCopyOnWriteArrayList();
  private final List<ComponentsListener> childrenListeners = Lists.newCopyOnWriteArrayList();
}

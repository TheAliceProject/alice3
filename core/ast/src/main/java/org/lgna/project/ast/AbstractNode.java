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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import edu.cmu.cs.dennisc.pattern.Crawler;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.ListProperty;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import org.lgna.project.ast.localizer.AstLocalizer;
import org.lgna.project.ast.localizer.AstLocalizerFactory;
import org.lgna.project.ast.localizer.DefaultAstLocalizerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractNode extends Element implements Node {

  private static AstLocalizerFactory astLocalizerFactory = new DefaultAstLocalizerFactory();

  public static AstLocalizerFactory getAstLocalizerFactory() {
    return AbstractNode.astLocalizerFactory;
  }

  public static void setAstLocalizerFactory(AstLocalizerFactory astLocalizerFactory) {
    AbstractNode.astLocalizerFactory = astLocalizerFactory;
  }

  private UUID id = UUID.randomUUID();
  private AbstractNode parent;

  @Override
  public final UUID getId() {
    return this.id;
  }

  public final void setId(UUID id) {
    this.id = id;
  }

  @Override
  public Node getParent() {
    return this.parent;
  }

  private void setParent(AbstractNode parent) {
    if (this.parent != parent) {
      this.parent = parent;
    }
  }

  @Override
  public <N extends Node> N getFirstAncestorAssignableTo(Class<N> cls, boolean isThisIncludedInSearch) {
    Node rv;
    if (isThisIncludedInSearch) {
      rv = this;
    } else {
      rv = this.getParent();
    }
    while (rv != null) {
      if (cls.isAssignableFrom(rv.getClass())) {
        break;
      }
      rv = rv.getParent();
    }
    return (N) rv;
  }

  @Override
  public final <N extends Node> N getFirstAncestorAssignableTo(Class<N> cls) {
    return getFirstAncestorAssignableTo(cls, false);
  }

  @Override
  public void firePropertyChanging(PropertyEvent e) {
    super.firePropertyChanging(e);
    InstanceProperty<?> property = e.getTypedSource();
    if (property instanceof NodeProperty<?>) {
      if (!(property instanceof DeclarationProperty<?>
          && ((DeclarationProperty<?>) property).isReference())) {
        AbstractNode node = (AbstractNode) ((NodeProperty<?>) property).getValue();
        if (node != null) {
          node.setParent(null);
        }
      }
    }
  }

  @Override
  public void firePropertyChanged(PropertyEvent e) {
    InstanceProperty<?> property = e.getTypedSource();
    if (property instanceof NodeProperty<?>) {
      if (!(property instanceof DeclarationProperty<?>
          && ((DeclarationProperty<?>) property).isReference())) {
        AbstractNode node = (AbstractNode) ((NodeProperty<?>) property).getValue();
        if (node != null) {
          node.setParent(this);
        }
      }
    }
    super.firePropertyChanged(e);
  }

  @Override
  public void fireClearing(ClearListPropertyEvent e) {
    super.fireClearing(e);
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      NodeListProperty<?> nodeListProperty = (NodeListProperty<?>) listProperty;
      for (Node node : nodeListProperty) {
        if (node instanceof AbstractNode) {
          ((AbstractNode) node).setParent(null);
        }
      }
    }
  }

  @Override
  public void fireRemoving(RemoveListPropertyEvent e) {
    super.fireRemoving(e);
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      //NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
      for (Object o : e.getElements()) {
        if (o instanceof AbstractNode) {
          ((AbstractNode) o).setParent(null);
        }
      }
    }
  }

  @Override
  public void fireSetting(SetListPropertyEvent e) {
    super.fireSetting(e);
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      //NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
      for (Object o : e.getElements()) {
        if (o instanceof AbstractNode) {
          ((AbstractNode) o).setParent(null);
        }
      }
    }
  }

  @Override
  public void fireSet(SetListPropertyEvent e) {
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      //NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
      for (Object o : e.getElements()) {
        if (o instanceof AbstractNode) {
          ((AbstractNode) o).setParent(this);
        }
      }
    }
    super.fireSet(e);
  }

  @Override
  public void fireAdding(AddListPropertyEvent e) {
    super.fireAdding(e);
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      //NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
      for (Object o : e.getElements()) {
        if (o instanceof AbstractNode) {
          ((AbstractNode) o).setParent(null);
        }
      }
    }
  }

  @Override
  public void fireAdded(AddListPropertyEvent e) {
    ListProperty<?> listProperty = (ListProperty<?>) e.getSource();
    if (listProperty instanceof NodeListProperty<?>) {
      //NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
      for (Object o : e.getElements()) {
        if (o instanceof AbstractNode) {
          ((AbstractNode) o).setParent(this);
        }
      }
    }
    super.fireAdded(e);
  }

  private static void acceptIfCrawlable(Crawler crawler, Set<Crawlable> visited, Object value, CrawlPolicy crawlPolicy, Criterion<Declaration> declarationFilter) {
    if (value instanceof AbstractNode) {
      AbstractNode node = (AbstractNode) value;
      if (declarationFilter != null) {
        if (node instanceof Declaration) {
          Declaration declaration = (Declaration) node;
          if (!declarationFilter.accept(declaration)) {
            Logger.errln("skipping", declaration);
            return;
          }
        }
      }
      node.accept(crawler, visited, crawlPolicy, declarationFilter);
    } else if (value instanceof Crawlable) {
      Crawlable crawlable = (Crawlable) value;
      crawlable.accept(crawler, visited);
    }
  }

  @Override
  public void accept(Crawler crawler, Set<Crawlable> visited) {
    this.accept(crawler, visited, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY, null);
  }

  private void accept(Crawler crawler, Set<Crawlable> visited, CrawlPolicy crawlPolicy, Criterion<Declaration> declarationFilter) {
    if (!visited.contains(this)) {
      visited.add(this);
      crawler.visit(this);

      // Look through this nodes properties to see if any have anything to crawl
      for (InstanceProperty<?> property : this.getProperties()) {
        // Check if this is a reference
        if (property instanceof DeclarationProperty<?>) {
          DeclarationProperty<?> declarationProperty = (DeclarationProperty<?>) property;
          if (declarationProperty.isReference() && !crawlPolicy.isReferenceTunneledInto()) {
            if (crawlPolicy.isReferenceIncluded()) {
              Declaration declaration = declarationProperty.getValue();
              if (!visited.contains(declaration)) {
                visited.add(declaration);
                crawler.visit(declaration);
              }
            }
            continue;
          }
        }

        Object value = property.getValue();
        if (value instanceof Iterable<?>) {
          Iterable<?> iterable = (Iterable<?>) value;
          for (Object item : iterable) {
            acceptIfCrawlable(crawler, visited, item, crawlPolicy, declarationFilter);
          }
        } else if (value instanceof Object[]) {
          Object[] array = (Object[]) value;
          for (Object item : array) {
            acceptIfCrawlable(crawler, visited, item, crawlPolicy, declarationFilter);
          }
        } else {
          acceptIfCrawlable(crawler, visited, value, crawlPolicy, declarationFilter);
        }
      }
    }
  }

  @Override
  public final synchronized void crawl(Crawler crawler, CrawlPolicy crawlPolicy, Criterion<Declaration> criterion) {
    this.accept(crawler, new HashSet<Crawlable>(), crawlPolicy, criterion);
  }

  public final synchronized void crawl(Crawler crawler, CrawlPolicy crawlPolicy) {
    this.crawl(crawler, crawlPolicy, null);
  }

  protected Set<AbstractDeclaration> fillInDeclarationSet(Set<AbstractDeclaration> rv, Set<AbstractNode> nodes) {
    nodes.add(this);
    for (InstanceProperty<?> property : this.getProperties()) {
      Object value = property.getValue();
      if (value instanceof AbstractNode) {
        if (!nodes.contains(value)) {
          ((AbstractNode) value).fillInDeclarationSet(rv, nodes);
        }
      } else if (value instanceof Iterable<?>) {
        for (Object item : (Iterable<?>) value) {
          if (item instanceof AbstractNode) {
            if (!nodes.contains(item)) {
              ((AbstractNode) item).fillInDeclarationSet(rv, nodes);
            }
          }
        }
      }
    }
    return rv;
  }

  public Set<AbstractDeclaration> createDeclarationSet() {
    Set<AbstractDeclaration> rv = new HashSet<AbstractDeclaration>();
    fillInDeclarationSet(rv, new HashSet<AbstractNode>());
    return rv;
  }

  private Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied(Set<AbstractDeclaration> rv, Set<AbstractNode> nodes) {
    nodes.add(this);
    for (InstanceProperty<?> property : this.getProperties()) {
      if (property instanceof DeclarationProperty) {
        DeclarationProperty<? extends AbstractDeclaration> declarationProperty = (DeclarationProperty<? extends AbstractDeclaration>) property;
        if (!declarationProperty.isReference()) {
          rv.remove(declarationProperty.getValue());
        }
      }
      Object value = property.getValue();
      if (value instanceof AbstractNode) {
        if (!nodes.contains(value)) {
          ((AbstractNode) value).removeDeclarationsThatNeedToBeCopied(rv, nodes);
        }
      } else if (value instanceof Iterable<?>) {
        for (Object item : (Iterable<?>) value) {
          if (item instanceof AbstractNode && !nodes.contains(item)) {
            ((AbstractNode) item).removeDeclarationsThatNeedToBeCopied(rv, nodes);
          }
        }
      }
    }
    return rv;
  }

  public Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied(Set<AbstractDeclaration> rv) {
    return removeDeclarationsThatNeedToBeCopied(rv, new HashSet<AbstractNode>());
  }

  public static void safeAppendRepr(AstLocalizer localizer, Node node) {
    if (node instanceof AbstractNode) {
      ((AbstractNode) node).appendRepr(localizer);
    } else {
      if (node != null) {
        localizer.appendText(node.getRepr());
      } else {
        localizer.appendNull();
      }
    }
  }

  //protected abstract void appendRepr( AstLocalizer localizer );
  protected void appendRepr(AstLocalizer localizer) {
    localizer.appendText(this.getClass().getSimpleName());
  }

  @Override
  public final String getRepr() {
    final StringBuilder sb = new StringBuilder();
    this.appendRepr(astLocalizerFactory.createInstance(sb));
    return sb.toString();
  }

  @Override
  public String generateLocalName(UserLocal local) {
    // This is overridden in the loop subclasses that will display the name.
    return "unusedName";
  }

  protected StringBuilder appendStringDetails(StringBuilder rv) {
    return rv;
  }

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    this.appendStringDetails(sb);
    sb.append("]");
    return sb.toString();
  }
}

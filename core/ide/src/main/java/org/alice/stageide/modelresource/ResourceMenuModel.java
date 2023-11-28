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
package org.alice.stageide.modelresource;

import org.alice.ide.Theme;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.imp.cascade.ItemNode;

import javax.swing.Icon;
import javax.swing.JComponent;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ResourceMenuModel extends CascadeMenuModel<ResourceNode> {
  private final ResourceNode resourceNode;

  public ResourceMenuModel(ResourceNode resourceNode) {
    super(UUID.fromString("5f0b30f5-6b05-4914-9281-c116ce6efbec"));
    this.resourceNode = resourceNode;
  }

  @Override
  protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<ResourceNode> blankNode) {
    for (ResourceNode child : resourceNode.getNodeChildren()) {
      blankChildren.add(child.getAddFieldBlankChild());
    }
  }

  @Override
  protected JComponent createMenuItemIconProxy(ItemNode<? super ResourceNode, ResourceNode> itemNode) {
    throw new Error();
  }

  @Override
  public String getMenuItemText() {
    return this.resourceNode.getText();
  }

  @Override
  public Icon getMenuItemIcon(ItemNode<? super ResourceNode, ResourceNode> node) {
    IconFactory iconFactory = this.resourceNode.getIconFactory();
    return iconFactory != null ? iconFactory.getIconToFit(Theme.SMALL_RECT_ICON_SIZE) : null;
  }

}

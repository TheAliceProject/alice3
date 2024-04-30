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

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ResourceNode extends ResourceGalleryDragModel implements Comparable<ResourceNode> {
  private ResourceNode parent;
  private final ResourceKey resourceKey;
  private final List<ResourceNode> children;
  private final CascadeBlankChild<ResourceNode> blankChild;
  private final boolean isBreadcrumbButtonIconDesired;

  public ResourceNode(UUID migrationId, ResourceKey key) {
    this(migrationId, key, Collections.emptyList(), false);
  }

  public ResourceNode(ResourceKey resourceKey, List<ResourceNode> children) {
    this(resourceKey, children, false);
  }

  public ResourceNode(ResourceKey resourceKey, List<ResourceNode> children, boolean isBreadcrumbButtonDesired) {
    this(UUID.fromString("cdf2a199-6160-4f87-9001-17a6b8f0c355"), resourceKey, children, isBreadcrumbButtonDesired);
  }

  private ResourceNode(UUID migrationId, ResourceKey resourceKey, List<ResourceNode> children, boolean isBreadcrumbButtonIconDesired) {
    super(migrationId);
    this.resourceKey = resourceKey;
    this.isBreadcrumbButtonIconDesired = isBreadcrumbButtonIconDesired;
    for (ResourceNode child : children) {
      assert child.parent == null : parent;
      child.parent = this;
    }
    this.children = children;
    if (this.resourceKey.isLeaf()) {
      if (this.resourceKey instanceof ClassResourceKey) {
        this.blankChild = null;
      } else {
        this.blankChild = new ResourceFillIn(this);
      }
    } else {
      this.blankChild = new ResourceMenuModel(this);
    }
  }

  @Override
  protected void localize() {
  }

  public ResourceNode getParent() {
    return this.parent;
  }

  public ResourceKey getResourceKey() {
    return this.resourceKey;
  }

  public boolean isUserDefinedModel() {
    return (this.resourceKey instanceof DynamicResourceKey);
  }

  boolean isSubclassable() {
    return resourceKey.isInterface() && !getFirstChild().isSubclassable();
  }

  boolean isSubclassableAndDoesNotRequirePoses() {
    return !resourceKey.getInternalText().equals("Slitherer") && !resourceKey.getInternalText().equals("Flyer") && isSubclassable();
  }

  @Override
  public List<ResourceNode> getNodeChildren() {
    return this.children;
  }

  void addNodeChild(ResourceNode nodeChild) {
    nodeChild.parent = this;
    this.children.add(nodeChild);
    Collections.sort(children);
  }

  void addNodeChild(int index, ResourceNode nodeChild) {
    nodeChild.parent = this;
    this.children.add(index, nodeChild);
  }

  @Override
  public String getText() {
    return this.resourceKey.getLocalizedCreationText();
  }

  @Override
  public IconFactory getIconFactory() {
    return this.resourceKey.getIconFactory();
  }

  public CascadeBlankChild<ResourceNode> getAddFieldBlankChild() {
    if (this.resourceKey instanceof ClassResourceKey) {
      ClassResourceKey classResourceKey = (ClassResourceKey) this.resourceKey;
      if (classResourceKey.isLeaf()) {
        return this.children.get(0).getAddFieldBlankChild();
      }
    }
    return this.blankChild;
  }

  @Override
  public Triggerable getDropOperation(DragStep step, DropSite dropSite) {
    return resourceKey.getDropOperation(this, step, dropSite);
  }

  public boolean isBreadcrumbButtonIconDesired() {
    return isBreadcrumbButtonIconDesired;
  }

  private static boolean ACCEPTABLE_HACK_FOR_GALLERY_QA_isLeftClickModelAlwaysNull = false;

  public static void ACCEPTABLE_HACK_FOR_GALLERY_QA_setLeftClickModelAlwaysNull(boolean ACCEPTABLE_HACK_FOR_GALLERY_QA_isLeftClickModelAlwaysNull) {
    ResourceNode.ACCEPTABLE_HACK_FOR_GALLERY_QA_isLeftClickModelAlwaysNull = ACCEPTABLE_HACK_FOR_GALLERY_QA_isLeftClickModelAlwaysNull;
  }

  @Override
  public Triggerable getLeftButtonClickOperation(SingleSelectTreeState<ResourceNode> controller) {
    if (ACCEPTABLE_HACK_FOR_GALLERY_QA_isLeftClickModelAlwaysNull) {
      return null;
    } else {
      return resourceKey.getLeftClickOperation(this, controller);
    }
  }

  ResourceNode getFirstChild() {
    if (this.children.size() > 0) {
      return this.children.get(0);
    } else {
      return null;
    }
  }

  public boolean isInstanceCreator() {
    return this.resourceKey.isInstanceCreator();
  }

  private InstanceCreatorKey getInstanceCreatorKey() {
    return (resourceKey instanceof InstanceCreatorKey) ? (InstanceCreatorKey) resourceKey : null;
  }

  @Override
  public AxisAlignedBox getBoundingBox() {
    InstanceCreatorKey key = getInstanceCreatorKey();
    return (key != null) ? key.getBoundingBox() : null;
  }

  @Override
  public boolean placeOnGround() {
    InstanceCreatorKey key = getInstanceCreatorKey();
    return key != null && key.getPlaceOnGround();
  }

  @Override
  public int compareTo(ResourceNode other) {
    return this.getText().toLowerCase().compareTo(other.getText().toLowerCase());
  }

  @Override
  protected void appendRepr(StringBuilder sb) {
    super.appendRepr(sb);
    sb.append("key=");
    sb.append(this.resourceKey);
  }

  public String getSimpleClassName() {
    return getResourceKey().getSearchText();
  }
}

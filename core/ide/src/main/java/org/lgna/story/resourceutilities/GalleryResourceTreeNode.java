package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.stageide.modelresource.ResourceKey;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public abstract class GalleryResourceTreeNode implements javax.swing.tree.TreeNode, Iterable<GalleryResourceTreeNode>, Comparable<GalleryResourceTreeNode> {

  protected TypeDefinedGalleryTreeNode parent;
  protected List<GalleryResourceTreeNode> children = Lists.newLinkedList();
  protected String name;
  private boolean isSorted = false;
  private ResourceKey resourceKey;

  GalleryResourceTreeNode(String name) {
    this.name = name;
  }

  public ResourceKey getResourceKey() {
    if (resourceKey == null) {
      resourceKey = createResourceKey();
    }
    return resourceKey;
  }

  abstract ResourceKey createResourceKey();

  public String getName() {
    return this.name;
  }

  @Override
  public TypeDefinedGalleryTreeNode getParent() {
    return this.parent;
  }

  public void setParent(TypeDefinedGalleryTreeNode parent) {
    if (this.parent == parent) {
      return;
    }
    if (this.parent != null) {
      this.parent.removeChild(this);
    }
    this.parent = parent;
    if (this.parent != null) {
      this.parent.addChild(this);
    }
  }

  @Override
  public boolean getAllowsChildren() {
    return true;
  }

  private List<GalleryResourceTreeNode> getSortedChildren() {
    if (this.isSorted) {
      //pass
    } else {
      Collections.sort(this.children);
      this.isSorted = true;
    }
    return this.children;
  }

  public List<? extends GalleryResourceTreeNode> childrenList() {
    return this.getSortedChildren();
  }

  @Override
  public Enumeration<? extends GalleryResourceTreeNode> children() {
    return Collections.enumeration(this.getSortedChildren());
  }

  @Override
  public Iterator iterator() {
    return this.children.iterator();
  }

  @Override
  public GalleryResourceTreeNode getChildAt(int childIndex) {
    return this.getSortedChildren().get(childIndex);
  }

  @Override
  public int getChildCount() {
    return this.children.size();
  }

  @Override
  public boolean isLeaf() {
    return this.children.size() == 0;
  }

  @Override
  public int getIndex(javax.swing.tree.TreeNode node) {
    return this.getSortedChildren().indexOf(node);
  }

  public void addChild(GalleryResourceTreeNode node) {
    this.children.add(node);
    this.isSorted = false;
  }

  public void removeChild(GalleryResourceTreeNode node) {
    this.children.remove(node);
  }

  @Override
  public int compareTo(GalleryResourceTreeNode other) {
    if (this.getAllowsChildren()) {
      if (other.getAllowsChildren()) {
        return this.getName().compareToIgnoreCase(other.getName());
      } else {
        return -1;
      }
    } else {
      if (other.getAllowsChildren()) {
        return 1;
      } else {
        return this.getName().compareToIgnoreCase(other.getName());
      }
    }
  }
}

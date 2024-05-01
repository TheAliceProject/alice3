package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.ide.typemanager.TypeManager;
import org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.*;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.ModelResource;

import java.util.Set;

public class DynamicResourceKey extends InstanceCreatorKey {
  private DynamicResource dynamicResource;

  public DynamicResourceKey(DynamicResource resource) {
    dynamicResource = resource;
  }

  @Override
  public Class<? extends ModelResource> getModelResourceCls() {
    return dynamicResource.getClass();
  }

  @Override
  public String getInternalName() {
    return dynamicResource.getInternalModelClassName();
  }

  @Override
  public String getSearchText() {
    return dynamicResource.getModelClassName();
  }

  @Override
  public String getLocalizedCreationText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    return String.format(formatter.getNewFormat(), dynamicResource.getModelClassName(), "");
  }

  @Override
  public IconFactory getIconFactory() {
    return IconFactoryManager.getIconFactoryForModelStructure(dynamicResource);
  }

  @Override
  public InstanceCreation createInstanceCreation(Set<NamedUserType> typeCache) {
    InstanceCreation resourceInstanceCreation = AstUtilities.createInstanceCreation(dynamicResource.getClass(), new Class<?>[] {String.class, String.class}, new StringLiteral(dynamicResource.getModelClassName()), new StringLiteral(dynamicResource.getModelVariantName()));

    JavaType resourceType = getAbstractionTypeForResourceType(JavaType.getInstance(dynamicResource.getClass()));
    NamedUserType type = TypeManager.getNamedUserTypeFromDynamicResourceInstanceCreation(resourceType, resourceInstanceCreation, dynamicResource.getModelClassName());

    return AstUtilities.createInstanceCreation(type.getFirstDeclaredConstructor(), resourceInstanceCreation);
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public String[] getTags() {
    return dynamicResource.getTags();
  }

  @Override
  public String[] getGroupTags() {
    return dynamicResource.getGroupTags();
  }

  @Override
  public String[] getThemeTags() {
    return dynamicResource.getThemeTags();
  }

  @Override
  public Triggerable getLeftClickOperation(ResourceNode node, SingleSelectTreeState<ResourceNode> controller) {
    return node.getDropOperation(null, null);
  }

  @Override
  public Triggerable getDropOperation(ResourceNode node, DragStep step, DropSite dropSite) {
    return AddResourceKeyManagedFieldComposite.getInstance().
        getLaunchOperationToCreateValue(this, true);
  }

  @Override
  public AxisAlignedBox getBoundingBox() {
    return dynamicResource.getBoundingBox();
  }

  @Override
  public boolean getPlaceOnGround() {
    return dynamicResource.getPlaceOnGround();
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof DynamicResourceKey && dynamicResource == ((DynamicResourceKey) o).dynamicResource;
  }

  @Override
  public int hashCode() {
    return dynamicResource.hashCode();
  }

  @Override
  protected void appendRep(StringBuilder sb) {
    sb.append(dynamicResource);
  }
}

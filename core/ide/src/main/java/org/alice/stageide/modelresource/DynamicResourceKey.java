package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resources.ModelStructure;


public class DynamicResourceKey extends InstanceCreatorKey {
	private ModelStructure resourceObject;

	public DynamicResourceKey( ModelStructure resource ) {
		resourceObject = resource;
	}

	@Override
	public Class<? extends ModelResource> getModelResourceCls() {
		return resourceObject.getClass();
	}

	@Override
	public String getInternalText() {
		return resourceObject.getInternalModelClassName();
	}

	@Override
	public String getSearchText() {
		return resourceObject.getModelClassName();
	}

	@Override
	public String getLocalizedDisplayText() {
		Formatter formatter = FormatterState.getInstance().getValue();
		return String.format(formatter.getNewFormat(), resourceObject.getModelClassName(), "");
	}

	@Override
	public IconFactory getIconFactory() {
		return IconFactoryManager.getIconFactoryForResourceInstance( resourceObject );
	}

	@Override
	public InstanceCreation createInstanceCreation() {
		return null;
		// TODO
		// return AstUtilities.createInstanceCreation( constructor, argumentExpressions );
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public String[] getTags() {
		return resourceObject.getTags();
	}

	@Override
	public String[] getGroupTags() {
		return resourceObject.getGroupTags();
	}

	@Override
	public String[] getThemeTags() {
		return resourceObject.getThemeTags();
	}

	@Override
	public AxisAlignedBox getBoundingBox() {
		return resourceObject.getBoundingBox();
	}

	@Override
	public boolean getPlaceOnGround() {
		return resourceObject.getPlaceOnGround();
	}

	@Override
	public boolean equals( Object o ) {
		return this == o ||
						o instanceof DynamicResourceKey && resourceObject == ((DynamicResourceKey) o).resourceObject;
	}

	@Override
	public int hashCode() {
		return resourceObject.hashCode();
	}

	@Override
	protected void appendRep( StringBuilder sb ) {
		sb.append( resourceObject );
	}
}

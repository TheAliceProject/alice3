package org.lgna.story.resources;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.lgna.story.SThing;
import org.lgna.story.implementation.JointedModelImp;

public class ReflectionBasedResource<I extends JointedModelImp,T extends SThing> implements ModelStructure<I,T>{

	Class<? extends ModelResource> resourceCls;

	public ReflectionBasedResource() {

	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		//	return this.resourceType.getFactory( this );
		return null;
	}

	@Override public JointId[] getRootJointIds() {
		return new JointId[0];
	}

	@Override
	public I createImplementation( T abstraction ) {
		return null;//resourceCls.createImplementation(abstraction);
	}

	@Override public String[] getTags() {
		return new String[0];
	}

	@Override public String[] getGroupTags() {
		return new String[0];
	}

	@Override public String getModelClassName() {
		return "";
	}

	@Override public String getInternalModelClassName() {
		return "";
	}

	@Override public String[] getThemeTags() {
		return new String[0];
	}

	@Override public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override public boolean getPlaceOnGround() {
		return false;
	}
}

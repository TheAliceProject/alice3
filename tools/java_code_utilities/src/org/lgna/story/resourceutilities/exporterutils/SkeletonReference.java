package org.lgna.story.resourceutilities.exporterutils;

import org.w3c.dom.Element;

public class SkeletonReference extends AssetReference {

	public static String DIFFERENT_BIND_POSE = "differentBindPose";
	
	private boolean hasDifferentBindPose = false;
	
	public SkeletonReference(String name, String assetReference, boolean hasDifferentBindPose) {
		super(name, assetReference);
		this.hasDifferentBindPose = hasDifferentBindPose;
	}
	
	public SkeletonReference(String name, String assetReference) {
		this(name, assetReference, false);
	}
	
	public SkeletonReference(Element e) {
		super(e);
		if (e.hasAttribute(DIFFERENT_BIND_POSE)) {
			this.hasDifferentBindPose = e.getAttribute(DIFFERENT_BIND_POSE).equalsIgnoreCase("true");
		}
	}

	public boolean hasDifferentBindPose() {
		return hasDifferentBindPose;
	}

	public void setHasDifferentBindPose(boolean hasDifferentBindPose) {
		this.hasDifferentBindPose = hasDifferentBindPose;
	}

	@Override
	protected void setAttributes(Element element) {
		super.setAttributes(element);
		if (hasDifferentBindPose) {
        	element.setAttribute(DIFFERENT_BIND_POSE, "true");
        }
	}
}

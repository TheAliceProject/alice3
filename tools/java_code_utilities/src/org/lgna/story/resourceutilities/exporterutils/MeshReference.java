package org.lgna.story.resourceutilities.exporterutils;

import org.w3c.dom.Element;

public class MeshReference extends AssetReference {
	public static String CULL_BACKFACES = "cullBackfaces";
	
	private boolean cullBackfaces = true;

	public MeshReference(String name, String assetReference) {
		super(name, assetReference);
	}
	
	public MeshReference(Element e) {
		super(e);
		if (e.hasAttribute(CULL_BACKFACES)) {
			this.cullBackfaces = e.getAttribute(CULL_BACKFACES).equalsIgnoreCase("true");
		}
	}

	public boolean cullBackfaces() {
		return cullBackfaces;
	}

	public void setCullBackfaces(boolean cullBackfaces) {
		this.cullBackfaces = cullBackfaces;
	}

	@Override
	protected void setAttributes(Element element) {
		super.setAttributes(element);
		if (!cullBackfaces) {
        	element.setAttribute(CULL_BACKFACES, "false");
        }
	}
}

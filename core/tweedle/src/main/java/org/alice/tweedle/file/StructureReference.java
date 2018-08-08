package org.alice.tweedle.file;

public class StructureReference extends ResourceReference {
	public static final String CONTENT_TYPE = "skeletonMesh";

	public ModelManifest.BoundingBox boundingBox;

	public StructureReference() {
		super();
	}

	@Override public String getContentType() {
		return CONTENT_TYPE;
	}

}

package org.alice.tweedle.file;

public class StructureReference extends ResourceReference {
	private static final String SKELETON_MESH = "skeletonMesh";

	public ModelManifest.BoundingBox boundingBox;

	public StructureReference() {
		super();
	}

	@Override public String getContentType() {
		return SKELETON_MESH;
	}

}

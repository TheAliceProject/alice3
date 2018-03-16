package org.alice.tweedle.file;

public class StructureReference extends ResourceReference {
	private static final String SKELETON_MESH = "skeletonMesh";

	@Override public String getContentType() {
		return SKELETON_MESH;
	}
}

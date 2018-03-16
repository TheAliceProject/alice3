package org.alice.tweedle.file;

import org.lgna.project.annotations.Visibility;

import java.util.List;

public class ModelManifest extends Manifest {

	public List<String> rootJoints;
	public List<Joint> additionalJoints;
	public List<String> additionalJointArrays;
	public List<Pose> poses;
	public BoundingBox boundingBox;
	public List<TextureReference> textureSets;
	public List<StructureReference> structures;
	public List<ModelVariant> models;

	public class BoundingBox
	{
		public List<Float> min;
		public List<Float> max;
	}

	private class ModelVariant {
		public String structure;
		public String textureSet;
		public String icon;
	}

	private class Joint {
		public String name;
		public String parent;
		public Visibility visibility;
	}

	private class Pose {
		public String name;
		public List<JointTransform> transforms;
	}

	private class JointTransform {
		public String jointName;
		public List<Float> orientation;
		public List<Float> position;
	}
}

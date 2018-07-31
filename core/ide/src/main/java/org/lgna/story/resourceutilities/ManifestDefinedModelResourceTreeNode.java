package org.lgna.story.resourceutilities;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.ast.*;
import org.lgna.story.SModel;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.ModelResource;

import java.lang.reflect.Field;

public class ManifestDefinedModelResourceTreeNode extends ModelResourceTreeNode{

	private final ModelManifest modelManifest;

	public ManifestDefinedModelResourceTreeNode(ModelManifest modelManifest) {
		super(null, null, null);
		this.modelManifest = modelManifest;
		this.name = this.modelManifest.description.name;
	}

	public ModelManifest getModelManifest() {
		return modelManifest;
	}
}

package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.OkDialog;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.ide.icons.Icons;
import org.alice.stageide.StageIDE;
import org.alice.stageide.gallerybrowser.views.ImportGalleryResourceView;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.croquet.*;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.Panel;
import org.lgna.project.ProjectVersion;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.io.GalleryModelIo;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;
import org.lgna.story.resourceutilities.JointedModelColladaImporter;
import org.lgna.story.resourceutilities.ModelLoadingException;
import org.lgna.story.resourceutilities.PipelineException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ImportGalleryResourceComposite extends ValueCreatorInputDialogCoreComposite<Panel, DynamicResource> {

	private Edit browseForCollada( CompletionStep<?> step, InternalActionOperation source ) {
		File file = Application.getActiveInstance().getDocumentFrame().showOpenFileDialog(
				UUID.randomUUID(),
				"Import Collada",
				FileUtilities.getDefaultDirectory(),
				SystemUtilities.isWindows() ? "*.dae" : null,
				filenameFilter() );
		if (file != null) {
			selectedFileState.setValueTransactionlessly( file.getAbsolutePath() );
			return null;
		}
		throw new CancelException();
	}

	private FilenameFilter filenameFilter() {
		return ( dir, path ) -> !new File( dir, path ).isDirectory()
				&& "dae".equals( FileUtilities.getExtension( path ) );
	}

	private Edit loadColladaFile( CompletionStep<?> step, InternalActionOperation source ) {
		final String fileName = selectedFileState.getValue();
		if ( fileName != null ) {
			importCollada( fileName );
		}
		return null;
	}

	private static class SingletonHolder {
		private static ImportGalleryResourceComposite instance = new ImportGalleryResourceComposite();
	}

	public static ImportGalleryResourceComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final StringState selectedFileState = this.createStringState( "selectedFileState" );

	private final Operation browseForColladaOperation = this
			.createActionOperation( "browseForColladaOperation", this::browseForCollada );

	private final Operation loadColladaOperation = this
			.createActionOperation( "loadColladaOperation", this::loadColladaFile );

	public ImportGalleryResourceComposite() {
		super( UUID.fromString( "50604ac0-6f77-473b-8cca-0a18948501ae" ) );
		this.browseForColladaOperation.setButtonIcon( Icons.FOLDER_ICON_SMALL );
	}

	public StringState getSelectedFileState() {
		return this.selectedFileState;
	}

	public Operation getBrowseOperation() {
		return this.browseForColladaOperation;
	}

	public Operation getLoadOperation() {
		return loadColladaOperation;
	}

	@Override
	protected DynamicResource createValue() {
		return null;
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return null;
	}

	@Override
	protected Panel createView() {
		return new ImportGalleryResourceView( this );
	}

	public void importCollada( String colladaFileName ) {
		Logger modelLogger = Logger.getLogger( getClass().getCanonicalName() );
		File colladaFile = new File( colladaFileName );

		JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter( colladaFile, modelLogger );
		colladaImporter.setFlipModel( false );

		SkeletonVisual sv = null;
		try {
			sv = colladaImporter.loadSkeletonVisual();
		} catch (ModelLoadingException e) {
			final OkDialog.Builder builder = new OkDialog.Builder( e.getLocalizedMessage() + "\nTry to correct it and reimport." );
			builder.title( "Problem during import" );
			builder.buildAndShow();
			return;
		}

		// TODO get a user name
		String creatorName = "Dave";
		// TODO get a parent class
		String parentClassName = "Prop";
		Class<? extends ModelResource> parentClass = AliceResourceClassUtilities.getResourceClassForAliceName(parentClassName);

		List<ModelManifest.Joint> baseJoints = getBaseJoints(parentClass);

		if (sv.skeleton.getValue() == null && !baseJoints.isEmpty()) {
			PipelineException p = new PipelineException("Required skeleton missing on " + sv.getName() + ". Base class is " + parentClass);
			final OkDialog.Builder builder = new OkDialog.Builder( p.getLocalizedMessage() + "\nTry to correct it and reimport." );
			builder.title( "Problem during import" );
			builder.buildAndShow();
			return;
		}

		List<ModelManifest.Joint> modelJoints = getModelJoints(sv);
		List<ModelManifest.Joint> missingJoints = getMissingJoints(modelJoints, baseJoints);
		if (!missingJoints.isEmpty()) {
			StringBuilder missingJointsMessage = new StringBuilder("Base class "+parentClass+"requires the following joints:");
			for (ModelManifest.Joint joint : baseJoints) {
				missingJointsMessage.append('\t');
				missingJointsMessage.append(joint.name+":");
				if (missingJoints.contains(joint)) {
					missingJointsMessage.append("\tMISSING");
				}
				else {
					missingJointsMessage.append("\tFOUND");
				}
				missingJointsMessage.append('\n');
			}
			missingJointsMessage.append("Correct for these missing joints and reimport.");
			final OkDialog.Builder builder = new OkDialog.Builder( missingJointsMessage.toString() );
			builder.title( "Problem during import" );
			builder.buildAndShow();
			return;
		}
		List<ModelManifest.Joint> extraJoints = getExtraJoints(modelJoints, baseJoints, true);

		ModelManifest modelManifest = createSimpleManifest( sv, creatorName, parentClassName );
		modelManifest.additionalJoints = extraJoints;
		for (ModelManifest.Joint rootJoint : getRootJoints(modelJoints) ) {
			rootJoint.visibility = Visibility.COMPLETELY_HIDDEN;
			modelManifest.rootJoints.add(rootJoint.name);
		}

		BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance( 160, 120 ).createThumbnail( sv );
		GalleryModelIo modelIo = new GalleryModelIo( sv, thumbnail, modelManifest );
		try {
			modelIo.writeModel( StageIDE.getActiveInstance().getGalleryDirectory() );
		} catch (IOException e) {
			throw new RuntimeException( "Unable to store model in gallery.", e );
		}
	}

	private static void addMissingJoints(SkeletonVisual sv, List<ModelManifest.Joint> missingJoints) {
		int index = 0;
		while (!missingJoints.isEmpty()) {
			ModelManifest.Joint currentJoint = missingJoints.get(index);
			if (currentJoint.parent != null) {
				Joint parentJoint = sv.skeleton.getValue().getJoint(currentJoint.parent);
				if (parentJoint != null) {
					Joint newJoint = new Joint();
					newJoint.jointID.setValue(currentJoint.name);
					newJoint.setParent(parentJoint);
					missingJoints.remove(index);
					System.out.println("   ADDED JOINT "+currentJoint.toString());
				} else {
					System.out.println("   NO JOINT FOUND ON SKELETON FOR "+currentJoint.parent+", SKIPPING IT");
					index = (index + 1) % missingJoints.size();
				}
			}
			else {
				System.out.println("   NO PARENT JOINT FOR "+currentJoint.toString()+", CAN'T ADD IT");
			}
		}
	}

	private static void addJointsToList( Joint sgJoint, List<ModelManifest.Joint> jointList ) {
		jointList.add(createJoint(sgJoint));
		for (Component c : sgJoint.getComponents()) {
			if (c instanceof Joint) {
				addJointsToList((Joint)c, jointList);
			}
		}
	}

	private static List<ModelManifest.Joint> getModelJoints(SkeletonVisual sv) {
		List<ModelManifest.Joint> joints = new ArrayList<>();
		addJointsToList(sv.skeleton.getValue(), joints);
		return joints;
	}

	private static List<ModelManifest.Joint> getRootJoints(List<ModelManifest.Joint> jointList) {
		List<ModelManifest.Joint> rootJoints = new ArrayList<>();
		for (ModelManifest.Joint joint : jointList) {
			if (joint.parent == null) {
				rootJoints.add(joint);
			}
		}
		return rootJoints;
	}

	private static ModelManifest.Joint createJoint(JointId jointId) {
		ModelManifest.Joint joint = new ModelManifest.Joint();
		joint.name = jointId.toString();
		joint.parent = jointId.getParent().toString();
		joint.visibility = jointId.getVisibility();
		return joint;
	}

	private static ModelManifest.Joint createJoint(Joint sgJoint) {
		ModelManifest.Joint joint = new ModelManifest.Joint();
		joint.name = sgJoint.jointID.getValue();
		if (sgJoint.getParent() instanceof Joint) {
			joint.parent = ((Joint)sgJoint.getParent()).jointID.getValue();
		}
		else {
			joint.parent = null;
		}
		joint.visibility = null;
		return joint;
	}

	private static List<ModelManifest.Joint> getBaseJoints(Class<? extends ModelResource> resourceClass) {
		List<ModelManifest.Joint> baseJoints = new ArrayList<>();
		List<JointId> baseJointIds = AliceResourceClassUtilities.getJoints(resourceClass);
		for (JointId jointId : baseJointIds) {
			baseJoints.add(createJoint(jointId));
		}
		return baseJoints;
	}

	private static List<ModelManifest.Joint> getExtraJoints(List<ModelManifest.Joint> modelJoints, List<ModelManifest.Joint> baseJoints, boolean setVisible) {
		List<ModelManifest.Joint> extraJoints = new ArrayList<>();
		for (ModelManifest.Joint joint : modelJoints) {
			if (!baseJoints.contains(joint)) {
				if (setVisible) {
					joint.visibility = Visibility.PRIME_TIME;
				}
				extraJoints.add(joint);
			}
		}
		return extraJoints;
	}

	private static List<ModelManifest.Joint> getMissingJoints(List<ModelManifest.Joint> modelJoints, List<ModelManifest.Joint> requiredJoints) {
		List<ModelManifest.Joint> missingJoints = new ArrayList<>();
		for (ModelManifest.Joint joint : requiredJoints) {
			if (!modelJoints.contains(joint)) {
				missingJoints.add(joint);
			}
		}
		return missingJoints;
	}

	private static ModelManifest createSimpleManifest( SkeletonVisual sv, String creatorName, String parentClassName) {
		String modelName = sv.getName();
		AxisAlignedBox boundingBox = sv.getAxisAlignedMinimumBoundingBox();

		ModelManifest modelManifest = new ModelManifest();
		modelManifest.parentClass = parentClassName;
		modelManifest.description.name = modelName;
		modelManifest.provenance.creator = creatorName;
		modelManifest.provenance.aliceVersion = ProjectVersion.getCurrentVersionText();

		modelManifest.boundingBox = new ModelManifest.BoundingBox();
		modelManifest.boundingBox.max = boundingBox.getMaximum().getAsFloatList();
		modelManifest.boundingBox.min = boundingBox.getMinimum().getAsFloatList();

		StructureReference structureReference = new StructureReference();
		structureReference.name = modelName;
		modelManifest.resources.add( structureReference );

		ModelManifest.TextureSet textureSet = new ModelManifest.TextureSet();
		textureSet.name = modelName;
		modelManifest.textureSets.add( textureSet );

		ModelManifest.ModelVariant modelVariant = new ModelManifest.ModelVariant();
		modelVariant.name = modelName;
		modelVariant.structure = structureReference.name;
		modelVariant.textureSet = textureSet.name;
		modelManifest.models.add( modelVariant );

		return modelManifest;
	}
}


package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.name.NameValidator;
import org.alice.ide.name.validators.TypeNameValidator;
import org.alice.stageide.StageIDE;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite;
import org.lgna.croquet.SplitComposite;
import org.lgna.croquet.StringState;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.FormPanel;
import org.lgna.croquet.views.LabeledFormRow;
import org.lgna.croquet.views.Panel;
import org.lgna.ik.poser.croquet.PoserComposite;
import org.lgna.ik.poser.scene.AbstractPoserScene;
import org.lgna.project.ProjectVersion;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.StaticAnalysisUtilities;
import org.lgna.project.ast.UserPackage;
import org.lgna.project.io.GalleryModelIo;
import org.lgna.story.SJointedModel;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ImportGalleryResourceComposite extends SingleValueCreatorInputDialogCoreComposite<Panel, Boolean> {
	private final StageIDE ide = StageIDE.getActiveInstance();

	private final SkeletonPreviewComposite previewComposite;
	private final ModelDetailsComposite detailsComposite;
	private final SplitComposite splitComposite;
	private final ErrorStatus errorStatus;

	private final SkeletonVisual skeletonVisual;

	private final NameValidator typeValidator = new TypeNameValidator();

	public ImportGalleryResourceComposite( SkeletonVisual sv ) {
		super( UUID.fromString( "39f498c3-ce50-48af-91de-0ef10e174cf5" ) );
		skeletonVisual = sv;
		previewComposite = new SkeletonPreviewComposite();
		previewComposite.getView().setSkeletonVisual( sv );
		detailsComposite = new ModelDetailsComposite( UUID.randomUUID());
		splitComposite = this.createHorizontalSplitComposite( this.previewComposite, this.detailsComposite, 0.35f );
		errorStatus = createErrorStatus( "errorStatus" );
	}

	@Override
	protected Panel createView() {
		return new BorderPanel.Builder().center( this.splitComposite.getView() ).build();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromWidth() {
		return 1000;
	}

	@Override
	protected void handlePreShowDialog( Dialog dialog) {
		previewComposite.getView().positionAndOrientCamera();
		ide.getDocumentFrame().disableRendering( ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN );
		super.handlePreShowDialog( dialog );
	}

	@Override
	protected Status getStatusPreRejectorCheck() {
		String candidate = detailsComposite.modelName.getValue();
		String explanation = typeValidator.getExplanationIfOkButtonShouldBeDisabled( candidate );
		if( explanation != null ) {
			errorStatus.setText( explanation );
			return errorStatus;
		}
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected void handleFinally( Dialog dialog ) {
		super.handleFinally( dialog );
		ide.getDocumentFrame().enableRendering();
	}

	@Override
	protected Boolean createValue() {
		saveToMyGallery( skeletonVisual );
		return true;
	}

	private void saveToMyGallery( SkeletonVisual skeleton ) {
		ide.setAuthorName( detailsComposite.author.getValue() );
		BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance( 160, 120 ).createThumbnail( skeleton );
		ModelManifest modelManifest = createSimpleManifest( detailsComposite.modelName.getValue(), detailsComposite.author.getValue() );
		GalleryModelIo modelIo = new GalleryModelIo( skeleton, thumbnail, modelManifest );
		try {
			modelIo.writeModel( StageIDE.getActiveInstance().getGalleryDirectory() );
			modelIo.writeModel( ide.getGalleryDirectory() );
		} catch (IOException e) {
			throw new CancelException( "Unable to store model in gallery.", e );
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
		if (sgJoint != null) {
			jointList.add(createJoint(sgJoint));
			for (Component c : sgJoint.getComponents()) {
				if (c instanceof Joint) {
					addJointsToList((Joint) c, jointList);
				}
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

	private class ModelDetailsComposite extends SimpleComposite<BorderPanel> {
		StringState author = createStringState( "author" );
		StringState modelName = createStringState( "modelName" );


		ModelDetailsComposite( UUID id ) {
			super( id );
			author.setValueTransactionlessly( ide.getAuthorName() );
			modelName.setValueTransactionlessly( StaticAnalysisUtilities.getConventionalClassName( skeletonVisual.getName() ) );
		}

		@Override
		protected BorderPanel createView() {
			final BorderPanel view = new BorderPanel( this );
			FormPanel centerComponent = new FormPanel() {
				@Override
				protected void appendRows( List<LabeledFormRow> rows ) {
					rows.add( new LabeledFormRow( modelName.getSidekickLabel(), modelName.createTextField() ) );
					rows.add( new LabeledFormRow( author.getSidekickLabel(), author.createTextField() ) );
				}

			};
			centerComponent.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
			view.addCenterComponent( centerComponent );
			return view;
		}
	}
}

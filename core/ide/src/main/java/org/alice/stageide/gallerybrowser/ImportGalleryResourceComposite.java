package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.croquet.components.SuperclassPopupButton;
import org.alice.ide.icons.Icons;
import org.alice.ide.name.NameValidator;
import org.alice.ide.name.validators.TypeNameValidator;
import org.alice.stageide.StageIDE;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.TreeUtilities;
import org.alice.stageide.modelviewer.SkeletonVisualViewer;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.croquet.*;
import org.lgna.croquet.data.MutableListData;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.imp.dialog.views.GatedCommitDialogContentPane;
import org.lgna.croquet.views.*;
import org.lgna.project.ProjectVersion;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.StaticAnalysisUtilities;
import org.lgna.project.io.GalleryModelIo;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ImportGalleryResourceComposite extends SingleValueCreatorInputDialogCoreComposite<Panel, Boolean> {
	private final StageIDE ide = StageIDE.getActiveInstance();

	private final ModelPreviewComposite previewComposite;
	private final ModelDetailsComposite detailsComposite;
	private final SplitComposite splitComposite;
	private final ErrorStatus errorStatus;

	private final SkeletonVisual skeletonVisual;
	private Class<? extends ModelResource> parentJavaClass;
	private Double appliedScale = 1.0;

	private final NameValidator typeValidator = new TypeNameValidator();

	public ImportGalleryResourceComposite( SkeletonVisual sv ) {
		super( UUID.fromString( "39f498c3-ce50-48af-91de-0ef10e174cf5" ) );
		skeletonVisual = sv;
		previewComposite = new ModelPreviewComposite(sv);
		detailsComposite = new ModelDetailsComposite();
		splitComposite = this.createHorizontalSplitComposite( this.previewComposite, this.detailsComposite, 0.35f );
		errorStatus = createErrorStatus( "errorStatus" );
	}

	@Override
	protected Panel createView() {
		final SplitPane splitPane = splitComposite.getView();
		splitPane.setDividerLocation( 450 );
		return new BorderPanel.Builder().center( splitPane ).build();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromWidth() {
		return 1000;
	}

	@Override
	protected void handlePreShowDialog( Dialog dialog) {
		previewComposite.positionAndOrientCamera();
		ide.getDocumentFrame().disableRendering( ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN );
		super.handlePreShowDialog( dialog );
	}

	@Override
	protected Status getStatusPreRejectorCheck() {
		String candidate = detailsComposite.modelName.getValue();
		String nameExplanation = typeValidator.getExplanationIfOkButtonShouldBeDisabled( candidate );
		String sClassExplanation = getSuperclassExplanation();
		if( errorStatus.setText( sClassExplanation, nameExplanation ) ) {
			return errorStatus;
		}
		return IS_GOOD_TO_GO_STATUS;
	}

	private String getSuperclassExplanation() {
		if (parentJavaClass == null) {
			return findLocalizedText( "errorMissingSuper" );
		}
		List<ModelManifest.Joint> baseJoints = getBaseJoints( parentJavaClass );

		if ( skeletonVisual.skeleton.getValue() == null && !baseJoints.isEmpty() ) {
			return findLocalizedText( "errorMissingSkeleton" ).replace( "</skeleton/>", skeletonVisual.getName() );
		}

		List<ModelManifest.Joint> modelJoints = getModelJoints( skeletonVisual );
		List<ModelManifest.Joint> missingJoints = getMissingJoints( modelJoints, baseJoints );
		if ( missingJoints.isEmpty() ) {
			detailsComposite.jointStatus.setText( "" );
		} else {
			final String aliceClassName = AliceResourceClassUtilities.getAliceClassName( parentJavaClass );
			String missingJointsMessage = findLocalizedText( "errorMissingJointsMessage" )
				.replace( "</class/>", aliceClassName )
				.replace( "</joints/>", missingJoints.stream().map( ModelManifest.Joint::toString ).collect( Collectors.joining( ", " ) ) );
			detailsComposite.jointStatus.setText( missingJointsMessage );
			return findLocalizedText( "errorMissingJoints" ).replace( "</class/>", aliceClassName );
		}
		return null;
	}

	private void setSuperclass( ResourceNode nextValue ) {
		if ( nextValue != null ) {
			final ResourceKey key = nextValue.getResourceKey();
			if ( key instanceof ClassResourceKey ) {
				parentJavaClass =  ((ClassResourceKey) key).getModelResourceCls();
				refreshStatus();
			}
		}
	}

	private void scaleModel(Double newScale) {
		if ( newScale != null ) {
			double change = newScale/appliedScale;
			skeletonVisual.scale(new Vector3(change, change, change));
			appliedScale = newScale;
			previewComposite.updateView();
		}
	}

	private void rotateSkeleton(AffineMatrix4x4 rotation) {
		skeletonVisual.skeleton.getValue().applyTransformation(rotation, AsSeenBy.SCENE);
	}

	@Override
	protected void releaseView( CompositeView<?, ?> view ) {
		splitComposite.releaseView();
		super.releaseView( view );
	}

	@Override
	protected void handleFinally( Dialog dialog ) {
		super.handleFinally( dialog );
		ide.getDocumentFrame().enableRendering();
	}

	@Override
	protected Boolean createValue() {
		saveToMyGallery();
		return true;
	}

	private void saveToMyGallery() {
		ide.setAuthorName( detailsComposite.author.getValue() );

		List<ModelManifest.Joint> baseJoints = getBaseJoints( parentJavaClass );
		List<ModelManifest.Joint> modelJoints = getModelJoints( skeletonVisual );
		List<ModelManifest.Joint> extraJoints = getExtraJoints( modelJoints, baseJoints, true );

		ModelManifest modelManifest = createSimpleManifest(
			detailsComposite.modelName.getValue(),
			detailsComposite.author.getValue(),
			AliceResourceClassUtilities.getAliceClassName( parentJavaClass ),
			skeletonVisual.getAxisAlignedMinimumBoundingBox() );
		modelManifest.additionalJoints = extraJoints;
		for ( ModelManifest.Joint rootJoint : getRootJoints( modelJoints ) ) {
			rootJoint.visibility = Visibility.COMPLETELY_HIDDEN;
			modelManifest.rootJoints.add( rootJoint.name );
		}

		BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance( 160, 120 ).createThumbnail( skeletonVisual );

		GalleryModelIo modelIo = new GalleryModelIo( skeletonVisual, thumbnail, modelManifest );
		try {
			modelIo.writeModel( ide.getGalleryDirectory() );
			TreeUtilities.updateTreeBasedOnClassHierarchy();
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
			} else {
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
		if (jointId.getParent() != null) {
			joint.parent = jointId.getParent().toString();
		}
		joint.visibility = jointId.getVisibility();
		return joint;
	}

	private static ModelManifest.Joint createJoint(Joint sgJoint) {
		ModelManifest.Joint joint = new ModelManifest.Joint();
		joint.name = sgJoint.jointID.getValue();
		if (sgJoint.getParent() instanceof Joint) {
			joint.parent = ((Joint)sgJoint.getParent()).jointID.getValue();
		} else {
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

	private static ModelManifest createSimpleManifest( String modelName, String creatorName, String parentClassName, AxisAlignedBox boundingBox ) {
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

	private static final AffineMatrix4x4 ROTATE_LEFT_AROUND_Y = new AffineMatrix4x4(new OrthogonalMatrix3x3(
		new Vector3(0, 0, -1),
		new Vector3(0, 1, 0),
		new Vector3(1, 0, 0)), Point3.ORIGIN);
	private static final AffineMatrix4x4 ROTATE_RIGHT_AROUND_Y = new AffineMatrix4x4(new OrthogonalMatrix3x3(
		new Vector3(0, 0, 1),
		new Vector3(0, 1, 0),
		new Vector3(-1, 0, 0)), Point3.ORIGIN);

	private class ModelDetailsComposite extends SimpleComposite<BorderPanel> {
		StringState author = createStringState( "author" );
		StringState modelName = createStringState( "modelName" );
		StringState sClass = createStringState( "sClass" );
		PlainStringValue jointStatus = createStringValue( "jointStatus" );
		// This is here ONLY to satisfy the way LabeledFormRow creates and localizes labels
		// TODO Remove this once localization is fixed
		StringState rotateModel = createStringState( "rotateModel" );
		BoundedDoubleState resizeModel = createBoundedDoubleState(
			"resizeModel", new BoundedDoubleDetails().initialValue( 1.0 ).minimum( 0.01 ).maximum( 100.0 ).stepSize( 0.01 ) );

		private ActionOperation rotateOperation(String rotateLeft, AffineMatrix4x4 rotation) {
			return createActionOperation(rotateLeft, (userActivity, source) -> {
				rotateSkeleton(rotation);
				return null;
			});
		}

		ModelDetailsComposite() {
			super( UUID.randomUUID() );
			author.setValueTransactionlessly( ide.getAuthorName() );
			modelName.setValueTransactionlessly( StaticAnalysisUtilities.getConventionalClassName( skeletonVisual.getName() ) );
		}

		@Override
		protected BorderPanel createView() {
			Operation rotateLeft = rotateOperation("rotateLeft", ROTATE_LEFT_AROUND_Y);
			Operation rotateRight = rotateOperation("rotateRight", ROTATE_RIGHT_AROUND_Y);
			rotateLeft.setButtonIcon(Icons.PREVIOUS_SMALL);
			rotateRight.setButtonIcon(Icons.NEXT_SMALL);
			final BorderPanel view = new BorderPanel( this );
			final Border emptyBorder = BorderFactory.createEmptyBorder( 8, 8, 8, 8 );

			FormPanel centerComponent = new FormPanel() {
				@Override
				protected void appendRows( List<LabeledFormRow> rows ) {
					rows.add( new LabeledFormRow( modelName.getSidekickLabel(), modelName.createTextField() ) );
					rows.add( new LabeledFormRow( author.getSidekickLabel(), author.createTextField() ) );

					final SingleSelectListState<ResourceNode, MutableListData<ResourceNode>> classList = TreeUtilities.getSClassListState();
					classList.clearSelection();
					classList.addNewSchoolValueListener( classSelectionListener );
					String selectLabel = findLocalizedText( "unselectedSuper" );
					final SwingComponentView<?> classButton = new SuperclassPopupButton( classList, selectLabel );
					rows.add( new LabeledFormRow( sClass.getSidekickLabel(), classButton ) );

					String forwardMessage = "    (" + findLocalizedText( "forwardMessage" ) + ")";
					FlowPanel rotateButtons = new FlowPanel(
						FlowPanel.Alignment.LEFT,
						rotateLeft.createButton(),
						rotateRight.createButton(),
						new Label(forwardMessage, TextPosture.OBLIQUE)
					);
					rows.add( new LabeledFormRow( rotateModel.getSidekickLabel(), rotateButtons ) );

					resizeModel.addNewSchoolValueListener(resizeListener);
					rows.add( new LabeledFormRow( resizeModel.getSidekickLabel(), resizeModel.createSpinner() ) );
				}

			};
			centerComponent.setBorder( emptyBorder );
			view.addCenterComponent( centerComponent );

			final ImmutableTextArea textArea = jointStatus.createImmutableTextArea(  );
			textArea.setForegroundColor( GatedCommitDialogContentPane.ERROR_COLOR );
			textArea.setBorder( emptyBorder );
			view.addPageEndComponent( textArea );
			return view;
		}

		@Override
		public void releaseView() {
			TreeUtilities.getSClassListState().removeNewSchoolValueListener( classSelectionListener );
			super.releaseView();
		}

		private final ValueListener<ResourceNode> classSelectionListener = e -> setSuperclass( e.getNextValue() );
		private final ValueListener<Double> resizeListener = e -> scaleModel( e.getNextValue() );
	}

	private class ModelPreviewComposite extends SimpleComposite<BorderPanel> {
		final SkeletonPreviewComposite skeletonPreview;
		BooleanState isUnitCubeShowing = createBooleanState( "isUnitCubeShowing", true );
		BooleanState areAxesShowing = createBooleanState( "areAxesShowing", true );

		ModelPreviewComposite( SkeletonVisual sv ) {
			super( UUID.randomUUID() );
			skeletonPreview = new SkeletonPreviewComposite();
			skeletonPreview.getView().setSkeletonVisual(sv );
		}

		@Override
		protected BorderPanel createView() {
			final BorderPanel view = new BorderPanel( this );
			FlowPanel controls = new FlowPanel(
				FlowPanel.Alignment.CENTER,
				isUnitCubeShowing.getSidekickLabel().createLabel(),
				isUnitCubeShowing.createCheckBox(),
				new Label( "        "),
				areAxesShowing.getSidekickLabel().createLabel(),
				areAxesShowing.createCheckBox()
			);
			isUnitCubeShowing.addAndInvokeNewSchoolValueListener( showUnitBoxListener );
			areAxesShowing.addAndInvokeNewSchoolValueListener( showAxesListener );
			view.addCenterComponent(skeletonPreview.getView());
			view.addPageEndComponent( controls );
			return view;
		}

		void positionAndOrientCamera() {
			skeletonPreview.getView().positionAndOrientCamera();
		}

		void setShowUnitBox(Boolean showBox) {
			skeletonPreview.getView().setShowUnitBox(showBox);
		}

		void setShowAxes(Boolean showBox) {
			skeletonPreview.getView().setShowAxes(showBox);
		}
		private final ValueListener<Boolean> showAxesListener = e -> setShowAxes( e.getNextValue() );
		private final ValueListener<Boolean> showUnitBoxListener = e -> setShowUnitBox( e.getNextValue() );

		void updateView() {
			final SkeletonVisualViewer view = skeletonPreview.getView();
			view.updateScale();
			if (isUnitCubeShowing.getValue()) {
				// Refresh the cube shown
				view.setShowUnitBox(true);
			}
			view.positionAndOrientCamera();
		}
	}


}

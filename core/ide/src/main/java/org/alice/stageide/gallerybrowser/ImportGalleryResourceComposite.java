package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
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
import org.lgna.project.io.GalleryModelIo;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;
import org.lgna.story.resourceutilities.JointedModelColladaImporter;
import org.lgna.story.resourceutilities.ModelLoadingException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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

		// TODO pick a model name
		String modelName = "Alien2";

		JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter( colladaFile, modelName,
																																									 modelLogger );
		colladaImporter.setFlipModel( false );

		SkeletonVisual sv = null;
		try {
			sv = colladaImporter.loadSkeletonVisual();
			sv.setName( modelName );
		} catch (ModelLoadingException e) {
			throw new RuntimeException( "Failed to load model", e );
		}

		BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance( 160, 120 ).createThumbnail( sv );
		// TODO get a user name
		ModelManifest modelManifest = createSimpleManifest( modelName, "Dave" );
		GalleryModelIo modelIo = new GalleryModelIo( sv, thumbnail, modelManifest );
		try {
			modelIo.writeModel( StageIDE.getActiveInstance().getGalleryDirectory() );
		} catch (IOException e) {
			throw new RuntimeException( "Unable to store model in gallery.", e );
		}
	}

	private static ModelManifest createSimpleManifest( String modelName, String creatorName ) {
		ModelManifest modelManifest = new ModelManifest();

		modelManifest.description.name = modelName;
		modelManifest.provenance.creator = creatorName;
		modelManifest.provenance.aliceVersion = ProjectVersion.getCurrentVersionText();

		StructureReference structureReference = new StructureReference();
		structureReference.id = modelName;
		modelManifest.structures.add( structureReference );

		ModelManifest.TextureSet textureSet = new ModelManifest.TextureSet();
		textureSet.id = "DEFAULT";
		modelManifest.textureSets.add( textureSet );

		ModelManifest.ModelVariant modelVariant = new ModelManifest.ModelVariant();
		modelVariant.structure = structureReference.id;
		modelVariant.textureSet = textureSet.id;
		modelManifest.models.add( modelVariant );

		return modelManifest;
	}
}


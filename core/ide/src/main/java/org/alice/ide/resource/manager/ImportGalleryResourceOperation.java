package org.alice.ide.resource.manager;

import edu.cmu.cs.dennisc.javax.swing.icons.LineAxisIcon;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.ide.icons.Icons;
import org.alice.stageide.StageIDE;
import org.alice.stageide.gallerybrowser.ImportGalleryResourceComposite;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.croquet.Application;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FileDialogValueCreator;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;
import org.lgna.story.resourceutilities.JointedModelColladaImporter;
import org.lgna.story.resourceutilities.ModelLoadingException;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportGalleryResourceOperation extends SingleThreadIteratingOperation {

	public ImportGalleryResourceOperation() {
		super( Application.DOCUMENT_UI_GROUP, UUID.fromString( "178d0562-a78b-40a2-907e-3cef46a4811d" ) );
		this.setButtonIcon( new LineAxisIcon( Icons.FOLDER_ICON_SMALL, PlusIconFactory.getInstance().getIcon(
				new Dimension( Icons.FOLDER_ICON_SMALL.getIconWidth(), Icons.FOLDER_ICON_SMALL.getIconHeight() ) ) ) );
	}

	@Override
	protected boolean hasNext( List<UserActivity> finishedSteps ) {
		return finishedSteps.size() < 2;
	}

	@Override
	protected Triggerable getNext( List<UserActivity> finishedSteps ) {
		switch (finishedSteps.size()) {
		case 0:
			return new FileDialogValueCreator( null, StageIDE.getActiveInstance().getModelImportDirectory(), "dae" );
		case 1:
			SkeletonVisual skeleton = readFile( getLastValueProduced( finishedSteps ) );
			return new ImportGalleryResourceComposite( skeleton ).getValueCreator();
		default:
			return null;
		}
	}

	private SkeletonVisual readFile( Object selectedFile ) {
		if ( !( selectedFile instanceof File ) ){
			throw new CancelException();
		}
		try {
			final File colladaModelFile = (File) selectedFile;
			// Save parent directory for next import
			StageIDE.getActiveInstance().setModelImportDirectory( colladaModelFile.getParentFile() );
			JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter( colladaModelFile, modelLogger );
			return colladaImporter.loadSkeletonVisual();

		} catch (ModelLoadingException e) {
			Dialogs.showInfo( findLocalizedText( "title" ),
							  e.getLocalizedMessage() + "\n" + findLocalizedText( "tryAgain" ) );
			throw new CancelException( e.getLocalizedMessage() );
		} catch (Throwable t) {
			modelLogger.log( Level.WARNING, "An unknown error occurred attempting to read the file.", t );
			Dialogs.showInfo( findLocalizedText( "title" ),
							  "An unknown error occurred attempting to read the file.\n" + findLocalizedText( "tryAgain" ) );
			throw new CancelException( t.getLocalizedMessage(), t );
		}
	}

	private final Logger modelLogger = Logger.getLogger( getClass().getCanonicalName() );
}

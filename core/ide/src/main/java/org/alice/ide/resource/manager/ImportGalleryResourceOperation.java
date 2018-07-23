package org.alice.ide.resource.manager;

import edu.cmu.cs.dennisc.javax.swing.icons.LineAxisIcon;
import org.alice.ide.icons.Icons;
import org.alice.stageide.gallerybrowser.ImportGalleryResourceComposite;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.croquet.Application;
import org.lgna.croquet.Model;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Step;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ImportGalleryResourceOperation extends SingleThreadIteratingOperation {
	private final ImportGalleryResourceComposite importGalleryResourceComposite = new ImportGalleryResourceComposite();

	public ImportGalleryResourceOperation() {
		super( Application.DOCUMENT_UI_GROUP, UUID.fromString( "178d0562-a78b-40a2-907e-3cef46a4811d" ) );
		this.setButtonIcon( new LineAxisIcon( Icons.FOLDER_ICON_SMALL, PlusIconFactory.getInstance().getIcon(
				new Dimension( Icons.FOLDER_ICON_SMALL.getIconWidth(), Icons.FOLDER_ICON_SMALL.getIconHeight() ) ) ) );
	}

	@Override
	protected boolean hasNext( CompletionStep<?> step, java.util.List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		return subSteps.size() < 2;
	}

	@Override
	protected Model getNext( CompletionStep<?> step, java.util.List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		switch (subSteps.size()) {
		case 0:
			return importGalleryResourceComposite.getBrowseOperation();
		case 1:
			return importGalleryResourceComposite.getLoadOperation();
		default:
			return null;
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( CompletionStep<?> step, List<Step<?>> subSteps ) {
		step.finish();
	}
}
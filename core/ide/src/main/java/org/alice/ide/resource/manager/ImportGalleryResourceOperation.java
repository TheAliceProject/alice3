package org.alice.ide.resource.manager;

import edu.cmu.cs.dennisc.javax.swing.icons.LineAxisIcon;
import org.alice.ide.icons.Icons;
import org.alice.stageide.gallerybrowser.ImportGalleryResourceComposite;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.croquet.Application;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;

import java.awt.*;
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
	protected boolean hasNext( List<UserActivity> finishedSteps ) {
		return finishedSteps.size() < 3;
	}

	@Override
	protected Triggerable getNext( List<UserActivity> finishedSteps ) {
		switch (finishedSteps.size()) {
		case 0:
			return importGalleryResourceComposite.getBrowseOperation();
		case 1:
			return importGalleryResourceComposite.getLoadOperation();
		case 2:
			return importGalleryResourceComposite.getSaveOperation();
		default:
			return null;
		}
	}
}
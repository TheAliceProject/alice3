package org.alice.stageide.gallerybrowser.views;

import org.alice.stageide.gallerybrowser.ImportGalleryResourceComposite;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.MigPanel;

import javax.swing.*;

public class ImportGalleryResourceView extends BorderPanel {
    protected static final int PAD = 4;

    public ImportGalleryResourceView(ImportGalleryResourceComposite composite ) {
        super( composite, 0, PAD );
        this.setBorder( BorderFactory.createEmptyBorder( PAD, PAD, PAD, PAD ) );

        MigPanel panel = new MigPanel( null, "insets 0, fill", "[shrink]4[grow]4[shrink]16[shrink]" );
        panel.addComponent( composite.getBrowseOperation().createButton() );
        panel.addComponent( composite.getSelectedFileState().getSidekickLabel().createLabel() );
        panel.addComponent( composite.getSelectedFileState().createSubduedTextField(), "growx 100" );
        this.addCenterComponent( panel );
        this.setBackgroundColor( GalleryView.BACKGROUND_COLOR );

    }

}



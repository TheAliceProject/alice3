package org.alice.ide.resource.manager;

import org.alice.stageide.gallerybrowser.ImportGalleryResourceComposite;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.Element;
import org.lgna.croquet.history.CompletionStep;

import java.util.UUID;

public class ImportGalleryResourceOperation  extends ActionOperation {
    public ImportGalleryResourceOperation() {
        super( Application.DOCUMENT_UI_GROUP, UUID.fromString( "178d0562-a78b-40a2-907e-3cef46a4811d" ) );
    }

    @Override
    protected Class<? extends Element> getClassUsedForLocalization() {
        return ImportGalleryResourceComposite.class;
    }


    @Override
    protected void perform( CompletionStep<?> step ) {
        //TODO: What should we do here? Should we access the ImportGalleryResourceComposite somehow and display it?
    }
}
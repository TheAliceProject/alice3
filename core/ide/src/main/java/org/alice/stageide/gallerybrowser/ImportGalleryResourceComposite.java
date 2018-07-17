package org.alice.stageide.gallerybrowser;

import org.alice.ide.icons.Icons;
import org.alice.stageide.gallerybrowser.views.ImportGalleryResourceView;
import org.lgna.croquet.*;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.Panel;
import org.lgna.story.resources.DynamicResource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.UUID;

public class ImportGalleryResourceComposite extends ValueCreatorInputDialogCoreComposite<Panel, DynamicResource> {

    private static class SingletonHolder {
        private static ImportGalleryResourceComposite instance = new ImportGalleryResourceComposite();
    }

    public static ImportGalleryResourceComposite getInstance() {
        return SingletonHolder.instance;
    }



    private final StringState selectedFileState = this.createStringState( "selectedFileState" );

    private final Operation browseForColladaOperation = this.createActionOperation( "browseForColladaOperation", new Action() {
        @Override
        public Edit perform(CompletionStep<?> step, AbstractComposite.InternalActionOperation source ) throws CancelException {
            JFileChooser fileChooser = new JFileChooser();

            File directory = new File( selectedFileState.getValue() );
            if( directory.isDirectory() ) {
                fileChooser.setCurrentDirectory( directory );
            }
            fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
            fileChooser.setFileFilter( new FileNameExtensionFilter( "*.dae", "dae" ) );
            int option = fileChooser.showOpenDialog( null );
            switch( option ) {
                case JFileChooser.APPROVE_OPTION:
                    File file = fileChooser.getSelectedFile();
                    selectedFileState.setValueTransactionlessly( file.getAbsolutePath() );
                    break;
                default:
                    throw new CancelException();
            }
            return null;
        }
    } );


    private ImportGalleryResourceComposite() {
        super( UUID.fromString( "50604ac0-6f77-473b-8cca-0a18948501ae" ) );
        this.browseForColladaOperation.setButtonIcon(Icons.FOLDER_ICON_SMALL);
    }

    public StringState getSelectedFileState() {
        return this.selectedFileState;
    }
    public Operation getBrowseOperation() {
        return this.browseForColladaOperation;
    }

    @Override
    protected DynamicResource createValue() {
        return null;
    }

    @Override
    protected Status getStatusPreRejectorCheck(CompletionStep<?> step) {
        return null;
    }

    @Override
    protected Panel createView() {
        return new ImportGalleryResourceView(this);
    }

}


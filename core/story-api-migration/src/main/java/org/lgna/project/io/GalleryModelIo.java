package org.lgna.project.io;

import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.project.ProjectVersion;
import org.lgna.story.resourceutilities.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class GalleryModelIo extends DataSourceIo {

    private final SkeletonVisual skeletonVisual;
    private final BufferedImage thumbnail;
    private final ModelManifest modelManifest;

    public GalleryModelIo(SkeletonVisual skeletonVisual, BufferedImage thumbnail, ModelManifest modelManifest) {
        this.skeletonVisual = skeletonVisual;
        this.thumbnail = thumbnail;
        this.modelManifest = modelManifest;
    }

    public void writeModel(OutputStream os, String basePath) throws IOException {
        JsonModelIo modelIo = new JsonModelIo(modelManifest, skeletonVisual, thumbnail, JsonModelIo.ExportFormat.ALICE);
        writeDataSources( os, modelIo.createDataSources(basePath) );
    }

    public void writeModel(File outputDirectory) throws IOException {
        JsonModelIo modelIo = new JsonModelIo(modelManifest, skeletonVisual, thumbnail, JsonModelIo.ExportFormat.ALICE);
        writeDataSources( outputDirectory, modelIo.createDataSources("") );
    }

    private static ModelManifest createSimpleManifest(String modelName, String creatorName) {
        ModelManifest modelManifest = new ModelManifest();

        modelManifest.description.name = modelName;
        modelManifest.provenance.creator = creatorName;
        modelManifest.provenance.aliceVersion = ProjectVersion.getCurrentVersionText();

        StructureReference structureReference = new StructureReference();
        structureReference.name = modelName;
        modelManifest.resources.add(structureReference);

        ModelManifest.TextureSet textureSet = new ModelManifest.TextureSet();
        textureSet.name = modelName;
        modelManifest.textureSets.add(textureSet);

        ModelManifest.ModelVariant modelVariant = new ModelManifest.ModelVariant();
        modelVariant.name = modelName;
        modelVariant.structure = structureReference.name;
        modelVariant.textureSet = textureSet.name;
        modelManifest.models.add(modelVariant);

        return modelManifest;
    }

    public static void main( String[] args ) {
        File colladaFile  = new File( "C:\\Users\\dculyba\\Documents\\Alice3\\MyProjects\\alienExport\\models\\Alien\\Alien_Alien.dae" );
        Logger modelLogger = Logger.getLogger( "org.lgna.story.resourceutilities.AliceColladaModelLoader" );
        JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter(colladaFile, modelLogger);
        colladaImporter.setFlipModel(false);

        SkeletonVisual sv = null;
        try {
            sv = colladaImporter.loadSkeletonVisual();
        }
        catch (ModelLoadingException e) {
            e.printStackTrace();
            System.exit(1);
        }

        BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance(160, 120).createThumbnail(sv);

        ModelManifest modelManifest = createSimpleManifest(sv.getName(), "Dave");

        GalleryModelIo modelIo = new GalleryModelIo(sv, thumbnail, modelManifest);
        try {
            modelIo.writeModel(new File("/Users/dculyba/Alice3/MyGallery"));
//            modelIo.writeModel(new File("C:\\Users\\dculyba\\Documents\\Alice3\\MyGallery"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}

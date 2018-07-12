package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.tweedle.file.Manifest;
import org.lgna.common.Resource;
import org.lgna.project.Project;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class GalleryModelIo extends DataSourceIo {

    private final SkeletonVisual skeletonVisual;
    private final BufferedImage thumbnail;
    private final ModelResourceInfo modelInfo;

    public GalleryModelIo(SkeletonVisual skeletonVisual, BufferedImage thumbnail, ModelResourceInfo modelInfo) {
        this.skeletonVisual = skeletonVisual;
        this.thumbnail = thumbnail;
        this.modelInfo = modelInfo;
    }

    public void writeModel(OutputStream os, String basePath) throws IOException {
        JsonModelIo modelIo = new JsonModelIo(modelInfo, skeletonVisual, thumbnail, JsonModelIo.ExportFormat.ALICE);
        writeDataSources( os, modelIo.createDataSources(basePath) );
    }

    public void writeModel(File outputDirectory) throws IOException {
        JsonModelIo modelIo = new JsonModelIo(modelInfo, skeletonVisual, thumbnail, JsonModelIo.ExportFormat.ALICE);
        writeDataSources( outputDirectory, modelIo.createDataSources("") );
    }

    public static void main( String[] args ) {
        File colladaFile  = new File( "C:\\Users\\dculyba\\Documents\\Alice3\\MyProjects\\alienExport\\models\\Alien\\Alien_Alien.dae" );
        String modelName = "Alien2";
        Logger modelLogger = Logger.getLogger( "org.lgna.story.resourceutilities.AliceColladaModelLoader" );
        JointedModelColladaImporter colladaImporter = new JointedModelColladaImporter(colladaFile, modelName, modelLogger);

        SkeletonVisual sv = null;
        try {
            sv = colladaImporter.loadSkeletonVisual();
        }
        catch (ModelLoadingException e) {
            e.printStackTrace();
            System.exit(1);
        }

        BufferedImage thumbnail = AdaptiveRecenteringThumbnailMaker.getInstance(160, 120).createThumbnail(sv);
        ModelResourceInfo parentInfo = new ModelResourceInfo( null, modelName, "Dave Culyba", 2018, sv.baseBoundingBox.getValue(), new String[]{}, new String[]{}, new String[]{}, "", "", false, true );
        ModelResourceInfo modelInfo = new ModelResourceInfo( parentInfo, modelName, null, 2018, null, null, null, null, modelName, "DEFAULT", false, true );
        parentInfo.addSubResource(modelInfo);
        GalleryModelIo modelIo = new GalleryModelIo(sv, thumbnail, parentInfo);
        try {
            modelIo.writeModel(new File("C:\\Users\\dculyba\\Documents\\Alice3\\MyGallery"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}

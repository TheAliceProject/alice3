package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.junit.Test;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.exporterutils.collada.COLLADA;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ModelExportTest {

  private static Map<JointedModelResource, SkeletonVisual> resourceToVisualMap = new HashMap<>();
  private static Map<JointedModelResource, ModelResourceInfo> resourceToInfoMap = new HashMap<>();

  private static SkeletonVisual getVisualForResource(JointedModelResource resource) {
    if (resourceToVisualMap.containsKey(resource)) {
      return resourceToVisualMap.get(resource);
    }
    SkeletonVisual sv = (SkeletonVisual) ImplementationAndVisualType.ALICE.getFactory(resource).createVisualData().getSgVisuals()[0];
    resourceToVisualMap.put(resource, sv);
    return sv;
  }

  private static ModelResourceInfo getModelInfoForResource(JointedModelResource resource) {
    if (resourceToInfoMap.containsKey(resource)) {
      return resourceToInfoMap.get(resource);
    }
    ModelResourceInfo modelInfo = AliceResourceUtilties.getModelResourceInfo(resource.getClass(), resource.toString());
    resourceToInfoMap.put(resource, modelInfo);
    return modelInfo;
  }

  //    private static final SkeletonVisual ALIEN_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(AlienResource.DEFAULT).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo ALIEN_MODEL_INFO = AliceResourceUtilties.getModelResourceInfo( AlienResource.class, AlienResource.DEFAULT.toString() );
  //
  //    private static final SkeletonVisual PIRATE_SHIP_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(PirateShipResource.DEFAULT).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo PIRATE_SHIP_INFO = AliceResourceUtilties.getModelResourceInfo( PirateShipResource.class, PirateShipResource.DEFAULT.toString() );
  //
  //    private static final SkeletonVisual ALICE_WONDERLAND_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(AliceResource.WONDERLAND).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo ALICE_WONDERLAND_INFO = AliceResourceUtilties.getModelResourceInfo( AliceResource.class, AliceResource.WONDERLAND.toString() );
  //
  //    private static final SkeletonVisual ALICE_CARNEGIE_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(AliceResource.CARNEGIE_MELLON).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo ALICE_CARNEGIE_INFO = AliceResourceUtilties.getModelResourceInfo( AliceResource.class, AliceResource.CARNEGIE_MELLON.toString() );
  //
  //    private static final SkeletonVisual DRAGON_DEFAULT_BLUE_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(DragonResource.DEFAULT_BLUE).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo DRAGON_DEFAULT_BLUE_MODEL_INFO = AliceResourceUtilties.getModelResourceInfo( DragonResource.class, DragonResource.DEFAULT_BLUE.toString() );
  //
  //    private static final SkeletonVisual DRAGON_DEFAULT_GREEN_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(DragonResource.DEFAULT_GREEN).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo DRAGON_DEFAULT_GREEN_MODEL_INFO = AliceResourceUtilties.getModelResourceInfo( DragonResource.class, DragonResource.DEFAULT_GREEN.toString() );
  //
  //    private static final SkeletonVisual DRAGON_TUTU_GREEN_VISUAL = (SkeletonVisual)ImplementationAndVisualType.ALICE.getFactory(DragonResource.TUTU_GREEN).createVisualData().getSgVisuals()[ 0 ];
  //    private static final ModelResourceInfo DRAGON_TUTU_GREEN_MODEL_INFO = AliceResourceUtilties.getModelResourceInfo( DragonResource.class, DragonResource.TUTU_GREEN.toString() );

    /*
        //Create the collada model data source
        DataSource structureDataSource = exporter.createColladaDataSource(resourcePath);
        //Link manifest entries to the files created by the exporter
        StructureReference structureReference = getStructureReference(manifest, modelInfo);
        //Strip the base and model path from the name to make it relative to the manifest
        structureReference.file = structureDataSource.getName().substring(resourcePath.length() + 1);
        structureReference.format = ExportFormat.COLLADA.modelExtension;
        //Only add new model files to the list to be written
        if (!dataSources.contains(structureDataSource)) {
            dataSources.add(structureDataSource);
        }

        //Get the existing texture set defined by the modelInfo.
        ModelManifest.TextureSet textureSet = getTextureSet(manifest, modelInfo);
        //Now that we have a model and exporter, use this to get the name to image map
        textureSet.idToResourceMap = exporter.createTextureIdToImageMap();
        //Get all the textures from the model as data sources
        List<DataSource> imageDataSources = exporter.createImageDataSources(resourcePath);
        for (DataSource imageDataSource : imageDataSources) {
            String imageFileName = imageDataSource.getName();
            //For each new image, create a new image reference and add it to the manifest
            if (!dataSources.contains(imageDataSource)) {
                Integer imageId = exporter.getTextureIdForName(imageFileName);
                String imageName = textureSet.idToResourceMap.get(imageId);
                ImageReference imageReference = new ImageReference(exporter.createImageResourceForTexture(imageId));
                //ResourceReference have their name initialized to the filename of the resource.
                //We need it to be the imageName from the idToResourceMap
                imageReference.name = imageName;
                manifest.images.add(imageReference);
                dataSources.add(imageDataSource);
            }
        }
     */

  //    private void aJointedModelColladaExporterShouldBeCreatedFromASkeletonVisualAndAModelResourceInfo(JointedModelResource resource) {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(getVisualForResource(resource), getModelInfoForResource(resource));
  //        assertNotNull("The JointedModelColladaExporter should have returned something being constructed from "+resource.getClass()+"."+resource, exporter );
  //    }
  //
  //
  //    @Test
  //    public void aJointedModelColladaExporterShouldBeCreatedFromASkeletonVisualAndAModelResourceInfo() {
  //        aJointedModelColladaExporterShouldBeCreatedFromASkeletonVisualAndAModelResourceInfo(AlienResource.DEFAULT);
  //    }
  //
  //    @Test
  //    public void aJointedModelColladaExporterShouldBeCreatedFromASkeletonVisualAndAString() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, "Alien");
  //        assertNotNull("The JointedModelColladaExporter should have returned something being constructed from Alien.DEFAULT.", exporter );
  //    }
  //
  //    @Test
  //    public void colladaDataShouldBeCreatedFromASkeletonVisualAndAString() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        COLLADA collada = exporter.createCollada();
  //        assertNotNull("createCollada() should have returned something being constructed from Alien and 'Alien'.", collada );
  //    }
  //
  //
  //    @Test
  //    public void aDataSourceForTheColladaFileShouldBeCreatedForAnEmptyPath() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        String exportPath = "";
  //        DataSource structureDataSource = exporter.createColladaDataSource(exportPath);
  //        assertNotNull("A DataSource for the Collada file should have been created for the export path '"+exportPath+"'", structureDataSource );
  //    }
  //
  //    @Test
  //    public void dataSourceForTheColladaFileShouldHaveANameWhenCreatedForAnEmptyPath() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        String exportPath = "";
  //        DataSource structureDataSource = exporter.createColladaDataSource(exportPath);
  //        assertNotNull("DataSource for the Collada file should have a name when created with the export path '"+exportPath+"'", structureDataSource.getName() );
  //    }
  //
  //    @Test
  //    public void dataSourceForTheColladaFileShouldHaveNonZeroLengthNameWhenCreatedForAnEmptyPath() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        String exportPath = "";
  //        DataSource structureDataSource = exporter.createColladaDataSource(exportPath);
  //        assertNotEquals("DataSource for the Collada file should have a name of non-zero length when created with the export path '"+exportPath+"'", structureDataSource.getName().length(), 0 );
  //    }
  //
  //    @Test
  //    public void dataSourceForTheColladaFileShouldHaveAValidFileNameWhenCreatedForAnEmptyPath() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        String exportPath = "";
  //        DataSource structureDataSource = exporter.createColladaDataSource(exportPath);
  //        assertTrue("DataSource for the Collada file should have a valid file name when created with the export path '"+exportPath+"'", FileUtilities.isValidPath(structureDataSource.getName()) );
  //    }
  //
  //    @Test
  //    public void dataSourcesForTheTextureFilesShouldBeCreatedForAnEmptyPath() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        String exportPath = "";
  //        List<DataSource> textureDataSources = exporter.createImageDataSources(exportPath);
  //        assertNotNull("DataSources for the texture files should have been created for the export path '"+exportPath+"'", textureDataSources );
  //    }
  //
  //    @Test
  //    public void idToImageMapShouldBeCreated() {
  //        JointedModelColladaExporter exporter = new JointedModelColladaExporter(ALIEN_VISUAL, ALIEN_MODEL_INFO);
  //        Map<Integer, String> idToResourceMap = exporter.createTextureIdToImageMap();
  //        assertNotNull("idToResourceMap should have been created", idToResourceMap );
  //    }
}

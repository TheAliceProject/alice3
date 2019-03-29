package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import org.alice.tweedle.file.*;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.Pose;
import org.lgna.story.implementation.ImageFactory;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.*;
import org.lgna.story.resourceutilities.JointedModelColladaExporter;
import org.lgna.story.resourceutilities.ModelResourceInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JsonModelIo extends DataSourceIo{

    private static final boolean NORMALIZE_WEIGHTS = true;

    private ModelManifest modelManifest;
    private List<JointedModelResource> modelResources;
    private List<SkeletonVisual> skeletonVisuals;
    private List<BufferedImage> thumbnails;
    private final ExportFormat exportFormat;

    public enum ExportFormat {
        COLLADA("dae", "png"),
        ALICE("a3r", "a3t");

        public final String modelExtension;
        public final String textureExtension;

        ExportFormat( String modelExtension, String textureExtension ) {
            this.modelExtension = modelExtension;
            this.textureExtension = textureExtension;
        }
    }

    private JsonModelIo(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }

    public JsonModelIo(ModelManifest modelManifest, SkeletonVisual skeletonVisual, BufferedImage thumbnail, ExportFormat exportFormat) {
        this(exportFormat);
        this.modelManifest = modelManifest;
        this.skeletonVisuals = new ArrayList<>();
        this.skeletonVisuals.add(skeletonVisual);

        this.thumbnails = new ArrayList<>();
        this.thumbnails.add(thumbnail);
    }

    public JsonModelIo( List<JointedModelResource> modelResources, ExportFormat exportFormat) {
        this(exportFormat);
        this.modelManifest = createModelManifest(modelResources);
        this.modelResources = modelResources;
    }

    //AliceResourcesUtilities.getTextureResourceName returns null for resources that use "default" texture names
    //Catch this and return the default texture name
    private static String getTextureName(JointedModelResource modelResource) {
        String textureName = AliceResourceUtilties.getTextureResourceName(modelResource);
        if (textureName.length() == 0) {
            textureName = AliceResourceUtilties.getDefaultTextureEnumName(modelResource.toString());
        }
        return textureName;
    }

    //Build a new ModelResourceInfo that includes only the resources in the modelResources list
    private static ModelResourceInfo createModelResourceInfo( List<JointedModelResource> modelResources ) {
        //Get the ModelResourceInfo from the first resource in the list.
        //The get the parent info for this ModelResourceInfo. This will be the ModelResourceInfo that represents the model class.
        ModelResource firstResource = modelResources.get(0);
        ModelResourceInfo rootInfo = AliceResourceUtilties.getModelResourceInfo(firstResource.getClass(), firstResource.toString()).getParent();

        //Make a copy of the rootInfo and then go through all the passed in modelResources and add ModelResourceInfos for them
        ModelResourceInfo toReturn = rootInfo.createShallowCopy();
        for (JointedModelResource modelResource : modelResources) {
            String visualName = AliceResourceUtilties.getVisualResourceName(modelResource);
            String textureName = getTextureName(modelResource);
            ModelResourceInfo subResource = rootInfo.getSubResource(visualName, textureName);
            ModelResourceInfo newSubResource = subResource.createShallowCopy();
            toReturn.addSubResource(newSubResource);
        }

        return toReturn;
    }

    //Build a new ModelResourceInfo that includes only the resources in the modelResources list
    private static ModelManifest createModelManifest( List<JointedModelResource> modelResources ) {
        //Get the ModelResourceInfo from the first resource in the list.
        //The get the parent info for this ModelResourceInfo. This will be the ModelResourceInfo that represents the model class.
        ModelResource firstResource = modelResources.get(0);
        ModelResourceInfo rootInfo = AliceResourceUtilties.getModelResourceInfo(firstResource.getClass(), firstResource.toString()).getParent();


        //Make a copy of the rootInfo and then go through all the passed in modelResources and add ModelResourceInfos for them
        ModelResourceInfo modelInfo = rootInfo.createShallowCopy();
        for (JointedModelResource modelResource : modelResources) {
            String visualName = AliceResourceUtilties.getVisualResourceName(modelResource);
            String textureName = getTextureName(modelResource);
            ModelResourceInfo subResource = rootInfo.getSubResource(visualName, textureName);
            ModelResourceInfo newSubResource = subResource.createShallowCopy();
            modelInfo.addSubResource(newSubResource);
        }

        ModelManifest modelManifest = modelInfo.createModelManifest();
        //Alice resources are enums that implement the base resource interfaces. For instance, the Alien implements the BipedResource interface
        Class superClass = firstResource.getClass().getInterfaces()[0];
        String parentClassName = AliceResourceClassUtilities.getAliceClassName( superClass );
        modelManifest.parentClass = parentClassName;
        //If this model is defined by JointedModelResources, then add the model data from that
        if (modelResources != null && modelResources.size() > 0) {
            //We use the first resource because the poses are defined on the resource enum, not on each resource instance
            addModelDataFromResource(modelManifest, modelResources.get(0));
        }

        return modelManifest;
    }

    private static boolean isFieldVisible( Field field ) {
        if( field.isAnnotationPresent( FieldTemplate.class ) ) {
            FieldTemplate propertyFieldTemplate = field.getAnnotation( FieldTemplate.class );
            return propertyFieldTemplate.visibility() != Visibility.COMPLETELY_HIDDEN;
        } else {
            return true;
        }
    }

    private static List<Float> getOrientationAsFloatList(OrthogonalMatrix3x3 orientation) {
        UnitQuaternion quaternion = orientation.createUnitQuaternion();
        List<Float> orientationList = new ArrayList<>(4);

        orientationList.add((float)quaternion.w);
        orientationList.add((float)quaternion.x);
        orientationList.add((float)quaternion.y);
        orientationList.add((float)quaternion.z);

        return orientationList;
    }


    private static ModelManifest.Pose createPose(Field poseField, JointedModelResource modelResource) {
        ModelManifest.Pose newPose = new ModelManifest.Pose();
        newPose.name = poseField.getName();
        JointedModelPose modelPose = null;
        try {
            modelPose = (JointedModelPose)poseField.get(modelResource);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        for (JointIdTransformationPair jointData : modelPose.getJointIdTransformationPairs()) {
            ModelManifest.JointTransform jointTransform = new ModelManifest.JointTransform();
            jointTransform.jointName = jointData.getJointId().toString();
            jointTransform.orientation = getOrientationAsFloatList(jointData.getTransformation().orientation);
            jointTransform.position = jointData.getTransformation().translation.getAsFloatList();
            newPose.transforms.add(jointTransform);
        }
        return newPose;
    }

    private static ModelManifest.Joint createJoint(Field jointField, JointedModelResource modelResource) {
        ModelManifest.Joint newJoint = new ModelManifest.Joint();
        newJoint.name = jointField.getName();
        if( jointField.isAnnotationPresent( FieldTemplate.class ) ) {
            FieldTemplate propertyFieldTemplate = jointField.getAnnotation( FieldTemplate.class );
            newJoint.visibility = propertyFieldTemplate.visibility();
        }
        JointId jointId = null;
        try {
            jointId = (JointId)jointField.get(modelResource);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        JointId parent = jointId.getParent();
        newJoint.parent = parent != null ? parent.toString() : null;

        return newJoint;
    }

    private static ModelManifest.JointArray createJointArray(Field jointArrayField, JointedModelResource modelResource) {
        ModelManifest.JointArray newJointArray = new ModelManifest.JointArray();
        newJointArray.name = jointArrayField.getName();
        if( jointArrayField.isAnnotationPresent( FieldTemplate.class ) ) {
            FieldTemplate propertyFieldTemplate = jointArrayField.getAnnotation( FieldTemplate.class );
            newJointArray.visibility = propertyFieldTemplate.visibility();
        }
        JointId[] jointIds = null;
        try {
            jointIds = (JointId[])jointArrayField.get(modelResource);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        for (JointId id : jointIds) {
            newJointArray.jointIds.add(id.toString());
        }

        return newJointArray;
    }

    private static ModelManifest.JointArrayId createJointArrayId(Field jointArrayIdField, JointedModelResource modelResource) {
        ModelManifest.JointArrayId newJointArrayId = new ModelManifest.JointArrayId();
        newJointArrayId.name = jointArrayIdField.getName();
        if( jointArrayIdField.isAnnotationPresent( FieldTemplate.class ) ) {
            FieldTemplate propertyFieldTemplate = jointArrayIdField.getAnnotation( FieldTemplate.class );
            newJointArrayId.visibility = propertyFieldTemplate.visibility();
        }
        JointArrayId jointArrayId = null;
        try {
            jointArrayId = (JointArrayId)jointArrayIdField.get(modelResource);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        newJointArrayId.patternId = jointArrayId.getElementNamePattern();
        newJointArrayId.rootJoint = jointArrayId.getRoot().toString();

        return newJointArrayId;
    }

    private static List<String> createRootJoints(Method getRootJointsMethod, JointedModelResource modelResource) {
        List<String> rootJoints = new ArrayList<>();

        JointId[] rootJointIds = null;
        try {
            rootJointIds = (JointId[])getRootJointsMethod.invoke(modelResource);
        }
        catch (InvocationTargetException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        for (JointId jointId : rootJointIds) {
            rootJoints.add(jointId.toString());
        }

        return rootJoints;
    }

    //Add poses, additionalJoints, additionalJointArrays, additionalJointArrayIds, and rootJoints
    //Derive this information from the given JointedModelResource via reflection
    private static void addModelDataFromResource(ModelManifest manifest, JointedModelResource modelResource) {
        //Loop through the fields and use their type to determine what to add
        for( Field field : modelResource.getClass().getDeclaredFields() ) {
            if (Pose.class.isAssignableFrom(field.getType()) && isFieldVisible(field)) {
                manifest.poses.add(createPose(field, modelResource));
            } else if (JointId.class.isAssignableFrom(field.getType())) {
                manifest.additionalJoints.add(createJoint(field, modelResource));
            } else if (JointId[].class.isAssignableFrom(field.getType())) {
                manifest.additionalJointArrays.add(createJointArray(field, modelResource));
            } else if (JointArrayId.class.isAssignableFrom(field.getType())) {
                manifest.additionalJointArrayIds.add(createJointArrayId(field, modelResource));
            }
        }
        try {
            Method rootJointMethod = modelResource.getClass().getMethod("getRootJointIds");
            manifest.rootJoints.addAll(createRootJoints(rootJointMethod, modelResource));
        }
        catch (NoSuchMethodException e) {
            Logger.warning("No getRootJointIds found on model "+manifest.description.name);
        }
    }

    private JointedModelResource getResourceForVariant(ModelManifest.ModelVariant modelVariant) {
        if (modelResources != null) {
            for (JointedModelResource resource : modelResources) {
                if (modelVariant.name.equals(resource.toString())) {
                    return resource;
                }
            }
        }
        return null;
    }

    private SkeletonVisual getVisualForModelVariant(ModelManifest.ModelVariant modelVariant) {
        if (skeletonVisuals != null) {
            for (SkeletonVisual visual : skeletonVisuals) {
                if (visual.getName().equals(modelVariant.structure)){
                    return visual;
                }
            }
            Logger.warning("Could not find matching visual for "+modelVariant.structure+". Returning first skeleton visual in list.");
            return skeletonVisuals.get(0);
        }
        else if (modelResources != null){
            JointedModelResource modelResource = getResourceForVariant(modelVariant);
            JointedModelImp.VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory( modelResource ).createVisualData();
            SkeletonVisual sv = (SkeletonVisual)v.getSgVisuals()[ 0 ];
            if (NORMALIZE_WEIGHTS) {
                sv.normalizeWeightedMeshes();
            }
            return sv;
        }
        return null;
    }

    private BufferedImage getThumbnailImageForSkeletonVisual(SkeletonVisual sv) {
        if (skeletonVisuals != null) {
           int index = this.skeletonVisuals.indexOf(sv);
           return this.thumbnails.get(index);
        }
        return null;
    }

    private BufferedImage getThumbnailImageForModelVariant(ModelManifest.ModelVariant modelVariant) {
        if (modelResources != null){
            JointedModelResource modelResource = getResourceForVariant(modelVariant);
            BufferedImage classThumbnailImage = AliceResourceUtilties.getThumbnail(modelResource.getClass());
            return classThumbnailImage;
        }
        return null;
    }

    private String getModelName() {
        return modelManifest.getName();
    }

    public ModelReference createModelReference(String basePath) {
        ModelReference modelReference = new ModelReference();
        modelReference.name = getModelName();
        modelReference.format = "json";
        modelReference.file = basePath +"/"+ getModelName() +"/" + getModelName()+".json";

        return modelReference;
    }


    private void addAliceDataSources(List<DataSource> dataSources, SkeletonVisual sv, ModelManifest manifest, ModelManifest.ModelVariant modelVariant, String resourcePath) throws IOException{
        TexturedAppearance[] texturedAppearancesToSave = sv.textures.getValue();
        // Null out the appearance since we save the textures separately
        sv.textures.setValue(new TexturedAppearance[0]);
        String modelFileName = AliceResourceUtilties.getVisualResourceFileNameFromModelName(modelVariant.structure);
        DataSource structureDataSource = createAliceStructureDataSource(resourcePath + "/" + modelFileName, sv);

        if (!dataSources.contains(structureDataSource)) {
            dataSources.add(structureDataSource);

            StructureReference structureReference = manifest.getStructure(modelVariant.structure);
            //Strip the base and model path from the name to make it relative to the manifest
            structureReference.file = new File(structureDataSource.getName()).getName();
            structureReference.format = ExportFormat.ALICE.modelExtension;
        }

        String textureName = AliceResourceUtilties.getTextureResourceFileName(modelVariant.structure, modelVariant.textureSet);
        DataSource textureDataSource = createAliceTextureDataSource(resourcePath + "/" + textureName, texturedAppearancesToSave);

        if (!dataSources.contains(textureDataSource)) {
            dataSources.add(textureDataSource);

            AliceTextureReference aliceTextureReference = new AliceTextureReference();
            aliceTextureReference.name = modelVariant.textureSet+"-textureReference";
            aliceTextureReference.file = new File(textureDataSource.getName()).getName();
            aliceTextureReference.format = "a3t";
            manifest.resources.add(aliceTextureReference);

            ModelManifest.TextureSet textureSet = manifest.getTextureSet(modelVariant.textureSet);
            textureSet.idToResourceMap.put(0, aliceTextureReference.name);
        }
    }


    //Uses the skeleton visual to create collada file and texture files
    //Adds appropriate DataSources and updates the manifest
    private void addColladaDataSources(List<DataSource> dataSources, SkeletonVisual sv, ModelManifest manifest, ModelManifest.ModelVariant modelVariant, String resourcePath) throws IOException{
        JointedModelColladaExporter exporter = new JointedModelColladaExporter(sv, modelVariant, manifest.description.name);
        //Create the collada model data source
        DataSource structureDataSource = exporter.createColladaDataSource(resourcePath);
        //Link manifest entries to the files created by the exporter
        StructureReference structureReference = manifest.getStructure(modelVariant.structure);
        //Strip the base and model path from the name to make it relative to the manifest
        structureReference.file = structureDataSource.getName().substring(resourcePath.length() + 1);
        structureReference.format = ExportFormat.COLLADA.modelExtension;
        if (structureReference.boundingBox == null) {
            AxisAlignedBox boundingBox = sv.getAxisAlignedMinimumBoundingBox();
            structureReference.boundingBox = new ModelManifest.BoundingBox();
            structureReference.boundingBox.max = boundingBox.getMaximum().getAsFloatList();
            structureReference.boundingBox.min = boundingBox.getMinimum().getAsFloatList();
        }

        //Only add new model files to the list to be written
        if (!dataSources.contains(structureDataSource)) {
            dataSources.add(structureDataSource);
        }

        //Get the existing texture set defined by the modelInfo.
        ModelManifest.TextureSet textureSet = manifest.getTextureSet(modelVariant.textureSet);
        //Now that we have a model and exporter, use this to get the id to image map
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
                //ResourceReference have their id initialized to the filename of the resource.
                //We need it to be the imageName from the idToResourceMap
                imageReference.name = imageName;
                manifest.resources.add(imageReference);
                dataSources.add(imageDataSource);
            }
        }
    }

    public List<DataSource> createDataSources(String baseModelPath) throws IOException {
        List<DataSource> dataToWrite = new ArrayList<>();

        //Model resources and manifest go into a folder named the model name
        String resourcePath = baseModelPath + "/"+ getModelName();
        for (ModelManifest.ModelVariant modelVariant : modelManifest.models) {
            SkeletonVisual sv = getVisualForModelVariant(modelVariant);
            if (exportFormat == ExportFormat.COLLADA) {
                addColladaDataSources(dataToWrite, sv,  modelManifest, modelVariant, resourcePath);
            }
            else if (exportFormat == ExportFormat.ALICE) {
                addAliceDataSources(dataToWrite, sv, modelManifest, modelVariant, resourcePath);
            }
            else {
                Logger.warning("Not exporting "+getModelName()+"--Unsupported export format: "+exportFormat);
            }
            //Add DataSources for the thumbnails
            BufferedImage thumbnailImage;
            if (this.skeletonVisuals != null) {
                thumbnailImage = getThumbnailImageForSkeletonVisual(sv);
            }
            else {
                thumbnailImage = getThumbnailImageForModelVariant(modelVariant);
            }
            String thumbnailName = modelVariant.name +".png";
            modelVariant.icon = thumbnailName;
            DataSource thumbnailDataSource = createAndAddImageDataSource(modelManifest, thumbnailImage, resourcePath, thumbnailName);
            dataToWrite.add(thumbnailDataSource);
        }


        BufferedImage thumbnailImage;
        if (this.thumbnails != null) {
            thumbnailImage = this.thumbnails.get(0);
        }
        else {
            thumbnailImage = getThumbnailImageForModelVariant(modelManifest.models.get(0));
        }
        String classIconName = getModelName()+"_cls.png";
        DataSource thumbnailDataSource = createAndAddImageDataSource(modelManifest, thumbnailImage, resourcePath, classIconName);
        dataToWrite.add(thumbnailDataSource);
        modelManifest.description.icon = classIconName;

        //The model manifest goes in the base model path directory
        dataToWrite.add(createDataSource(resourcePath +"/"+ getModelName()+".json", ManifestEncoderDecoder.toJson( modelManifest )));
        return dataToWrite;
    }

    private DataSource createAndAddImageDataSource(ModelManifest modelManifest, BufferedImage image, String resourcePath, String imageName) throws IOException {
        DataSource imageDataSource = createPNGImageDataSource(resourcePath +"/"+ imageName, image);
        ImageReference imageReference = new ImageReference(ImageFactory.createImageResource(image, imageName));
        imageReference.name = imageName;
        modelManifest.resources.add(imageReference);
        return imageDataSource;
    }

    private static DataSource createAliceStructureDataSource(final String fileName, final SkeletonVisual sv) {
        return new DataSource() {
            @Override
            public String getName() { return fileName; }

            @Override
            public void write(OutputStream os) throws IOException {
                AliceResourceUtilties.encodeVisual(sv, os);
            }
        };
    }

    private static DataSource createAliceTextureDataSource(final String fileName, final TexturedAppearance[] textures) {
        return new DataSource() {
            @Override
            public String getName() { return fileName; }

            @Override
            public void write(OutputStream os) throws IOException {
                AliceResourceUtilties.encodeTexture(textures, os);
            }
        };
    }

    private static DataSource createPNGImageDataSource(final String fileName, final BufferedImage image) {
        return new DataSource() {
            @Override public String getName() { return fileName; }

            @Override public void write( OutputStream os ) throws IOException {
                ImageIO.write(image, "png", os);
            }
        };
    }

    public void writeModel(OutputStream os, String baseModelPath) throws IOException {
        List<DataSource> dataToWrite = createDataSources(baseModelPath);
        writeDataSources( os, dataToWrite );
    }

    public void writeModel( OutputStream os, List<JointedModelResource> modelResources ) throws IOException {
        ModelResourceInfo modelInfo = createModelResourceInfo(modelResources);

    }

}

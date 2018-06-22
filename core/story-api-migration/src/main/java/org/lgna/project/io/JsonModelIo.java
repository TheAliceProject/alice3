package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import org.alice.tweedle.file.*;
import org.lgna.project.ProjectVersion;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.Pose;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.JointedModelImp;
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

    private ModelResourceInfo modelInfo;
    private List<JointedModelResource> modelResources;
    private List<SkeletonVisual> skeletonVisuals;
    private final ExportFormat exportFormat;

    public enum ExportFormat {
        COLLADA ("dae", "png"),
        ALICE ("a3r", "a3t");

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

    public JsonModelIo(ModelResourceInfo modelInfo, List<SkeletonVisual> skeletonVisuals, ExportFormat exportFormat) {
        this(exportFormat);
        this.modelInfo = modelInfo;
        this.skeletonVisuals = skeletonVisuals;
    }

    public JsonModelIo( List<JointedModelResource> modelResources, ExportFormat exportFormat) {
        this(exportFormat);
        this.modelInfo = createModelResourceInfo(modelResources);
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

    private static ModelManifest.BoundingBox createBoundingBox(ModelResourceInfo modelInfo) {
        ModelManifest.BoundingBox boundingBox = new ModelManifest.BoundingBox();
        boundingBox.max = modelInfo.getBoundingBox().getMaximum().getAsFloatList();
        boundingBox.min = modelInfo.getBoundingBox().getMinimum().getAsFloatList();
        return boundingBox;
    }

    private static String getTextureReferenceId(ModelResourceInfo modelInfo) {
        //Textures sets are model specific, so must prepend the model name to the id
        return modelInfo.getModelName() + "_"+ modelInfo.getTextureName();
    }

    private static ModelManifest.TextureSet createTextureSet(ModelResourceInfo modelInfo) {
        ModelManifest.TextureSet textureReference = new ModelManifest.TextureSet();
        textureReference.id = getTextureReferenceId(modelInfo);
        return textureReference;
    }

    private static StructureReference createStructureReference(ModelResourceInfo modelInfo) {
        StructureReference structureReference = new StructureReference();
        structureReference.boundingBox = createBoundingBox(modelInfo);
        structureReference.id = modelInfo.getModelName();
        return structureReference;
    }

    private static String getThumbnailFilename(ModelResourceInfo modelInfo) {
        //Use the resource name (TUTU_GREEN, etc.) as the thumbnail name
        return modelInfo.getResourceName() + ".png";
        //This is different than the AliceResourceUtilities process, but should produce comparable results
//        return AliceResourceUtilties.getThumbnailResourceFileName(modelInfo.getModelName(), getTextureReferenceId(modelInfo));
    }

    private static ModelManifest.ModelVariant createModelVariant(ModelResourceInfo modelInfo, StructureReference structure, ModelManifest.TextureSet texture) {
        ModelManifest.ModelVariant modelVariant = new ModelManifest.ModelVariant();
        modelVariant.id = modelInfo.getResourceName();
        modelVariant.structure = structure.id;
        modelVariant.textureSet = texture.id;
        modelVariant.icon = getThumbnailFilename(modelInfo);
        return modelVariant;
    }

    private static boolean isFieldVisible( Field field ) {
        if( field.isAnnotationPresent( FieldTemplate.class ) ) {
            FieldTemplate propertyFieldTemplate = field.getAnnotation( FieldTemplate.class );
            return propertyFieldTemplate.visibility() != Visibility.COMPLETELY_HIDDEN;
        } else {
            return true;
        }
    }

    private static List<Float> getOrientationAsFloatList(AffineMatrix4x4 transform) {
        UnitQuaternion quaternion = transform.orientation.createUnitQuaternion();
        List<Float> orientation = new ArrayList<>(4);

        orientation.add((float)quaternion.w);
        orientation.add((float)quaternion.x);
        orientation.add((float)quaternion.y);
        orientation.add((float)quaternion.z);

        return orientation;
    }

    private static List<Float> getPositionAsFloatList(AffineMatrix4x4 transform) {
        List<Float> position = new ArrayList<>(3);

        position.add((float)transform.translation.x);
        position.add((float)transform.translation.y);
        position.add((float)transform.translation.z);

        return position;
    }

    private ModelManifest.Pose createPose(Field poseField, JointedModelResource modelResource) {
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
            jointTransform.orientation = getOrientationAsFloatList(jointData.getTransformation());
            jointTransform.position = getPositionAsFloatList(jointData.getTransformation());
            newPose.transforms.add(jointTransform);
        }
        return newPose;
    }

    private ModelManifest.Joint createJoint(Field jointField, JointedModelResource modelResource) {
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

    private ModelManifest.JointArray createJointArray(Field jointArrayField, JointedModelResource modelResource) {
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

    private ModelManifest.JointArrayId createJointArrayId(Field jointArrayIdField, JointedModelResource modelResource) {
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

    private List<String> createRootJoints(Method getRootJointsMethod, JointedModelResource modelResource) {
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
    private void addModelDataFromResource(ModelManifest manifest, JointedModelResource modelResource) {
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

    //Add TexturesReferences, StructureReferences and ModelVariants to the ModelManifest
    private void addModelVariantInfo(ModelManifest manifest, ModelResourceInfo modelInfo) {
        StructureReference structure = createStructureReference(modelInfo);
        ModelManifest.TextureSet textureSet = createTextureSet(modelInfo);
        //Only add the structures and textures if they aren't already in the manifest
        //Multiple resources may reference the same structure or texture so repeats are likely
        boolean hasStructure = false;
        for (StructureReference existingStructure : manifest.structures) {
            if (existingStructure.id.equals(structure.id)) {
                hasStructure = true;
                break;
            }
        }
        if (!hasStructure) {
            manifest.structures.add(structure);
        }
        boolean hasTexture = false;
        for (ModelManifest.TextureSet existingTexture : manifest.textureSets) {
            if (existingTexture.id.equals(textureSet.id)) {
                hasTexture = true;
                break;
            }
        }
        if (!hasTexture) {
            manifest.textureSets.add(textureSet);
        }
        //Add the model variant. Model variants are all unique.
        ModelManifest.ModelVariant modelVariant = createModelVariant(modelInfo, structure, textureSet);
        manifest.models.add(modelVariant);
    }

    private ModelManifest createModelManifest(){
        ModelManifest manifest = new ModelManifest();

        manifest.description.name = getModelName();
        manifest.description.icon = getModelName()+".png";
        manifest.description.tags.addAll(Arrays.asList(modelInfo.getTags()));
        manifest.description.themeTags.addAll(Arrays.asList(modelInfo.getThemeTags()));
        manifest.description.groupTags.addAll(Arrays.asList(modelInfo.getGroupTags()));

        manifest.provenance.aliceVersion = ProjectVersion.getCurrentVersion().toString();
        manifest.provenance.creationYear = String.valueOf(modelInfo.getCreationYear());
        manifest.provenance.creator = modelInfo.getCreator();

        manifest.metadata.identifier.id = modelInfo.getResourceName();
        manifest.metadata.identifier.type = Manifest.ProjectType.Model;
		manifest.metadata.formatVersion = "0.1+alpha";

        manifest.placeOnGround = modelInfo.getPlaceOnGround();
        manifest.boundingBox = createBoundingBox(modelInfo);

        //If this model is defined by JointedModelResources, then add the model data from that
        if (modelResources != null && modelResources.size() > 0) {
            //We use the first resource because the poses are defined on the resource enum, not on each resource instance
            addModelDataFromResource(manifest, modelResources.get(0));
        }
        else {
            //TODO: initialize model data (poses, additionalJoints, additionalJointArrays, additionalJointArrayIds, and rootJoints) without a JointedModelResource
        }
        //Add the structures, textures, and model variants
        for (ModelResourceInfo subResource : modelInfo.getSubResources()) {
            addModelVariantInfo(manifest, subResource);
        }

        return manifest;
    }

    private ImageReference getImageReference(ModelManifest manifest, ModelResourceInfo modelInfo) {
        String textureId = getTextureReferenceId(modelInfo);
        for (ImageReference image : manifest.images) {
            if (image.id.equals(textureId)) {
                return image;
            }
        }
        return null;
    }

    private ModelManifest.TextureSet getTextureSet(ModelManifest manifest, ModelResourceInfo modelInfo) {
        String textureId = getTextureReferenceId(modelInfo);
        for (ModelManifest.TextureSet textureSet : manifest.textureSets) {
            if (textureSet.id.equals(textureId)) {
                return textureSet;
            }
        }
        return null;
    }

    private StructureReference getStructureReference(ModelManifest manifest, ModelResourceInfo modelInfo) {
        String structureId = modelInfo.getModelName();
        for (StructureReference structureReference : manifest.structures) {
            if (structureReference.id.equals(structureId)) {
                return structureReference;
            }
        }
        return null;
    }

    private SkeletonVisual getVisualForModelResource(ModelResourceInfo modelResourceInfo) {
        if (skeletonVisuals != null) {
            for (SkeletonVisual visual : skeletonVisuals) {
                if (true /* TODO: implement this logic. perhaps require a visual based model to define this explicitly*/){
                    return visual;
                }
            }
        }
        else if (modelResources != null){
            for (JointedModelResource modelResource : modelResources) {
                if (modelResourceInfo.matchesModelAndTexture(AliceResourceUtilties.getVisualResourceName(modelResource), getTextureName(modelResource)) ) {
                    JointedModelImp.VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory( modelResource ).createVisualData();
                    SkeletonVisual sv = (SkeletonVisual)v.getSgVisuals()[ 0 ];
                    return sv;
                }
            }
        }
        return null;
    }

    private BufferedImage getThumbnailImageForModelResource(ModelResourceInfo modelResourceInfo) {
        if (skeletonVisuals != null) {
            //TODO: Implement a way to get thumbnails for models based on skeleton visuals
            return null;
        }
        else if (modelResources != null){
            for (JointedModelResource modelResource : modelResources) {
                if (modelResourceInfo.matchesModelAndTexture(AliceResourceUtilties.getVisualResourceName(modelResource), getTextureName(modelResource)) ) {
                    BufferedImage thumbnailImage = AliceResourceUtilties.getThumbnail(modelResource.getClass(), modelResource.toString());
                    return thumbnailImage;
                }
            }
            //If the modelResourceInfo doesn't match a JointedModelResource, return the class level image
            BufferedImage classThumbnailImage = AliceResourceUtilties.getThumbnail(modelResources.get(0).getClass());
            return classThumbnailImage;
        }
        return null;
    }

    private String getModelName() {
        return modelInfo.getModelName();
    }

    public ModelReference createModelReference(String basePath) {
        ModelReference modelReference = new ModelReference();
        modelReference.id = getModelName();
        modelReference.format = "json";
        modelReference.file = basePath +"/"+ getModelName() +"/" + getModelName()+".json";

        return modelReference;
    }

    private void updateManifestResourceReference(ModelManifest manifest, ModelResourceInfo modelInfo, DataSource dataSource, String format) {
        StructureReference resourceReference = getStructureReference(manifest, modelInfo);
        //Strip the base and model path from the name to make it relative to the manifest
        resourceReference.file = new File(dataSource.getName()).getName();
        resourceReference.format = format;
    }

    private void addAliceDataSources(List<DataSource> dataSources, SkeletonVisual sv, ModelResourceInfo modelInfo, ModelManifest manifest, String resourcePath) throws IOException{
        TexturedAppearance[] texturedAppearancesToSave = sv.textures.getValue();
        // Null out the appearance since we save the textures separately
        sv.textures.setValue(new TexturedAppearance[0]);
        String modelName = AliceResourceUtilties.getVisualResourceFileNameFromModelName(modelInfo.getModelName());
        DataSource modelDataSource = createAliceStructureDataSource(resourcePath + "/" + modelName, sv);

        if (!dataSources.contains(modelDataSource)) {
            dataSources.add(modelDataSource);
            updateManifestResourceReference(manifest, modelInfo, modelDataSource, ExportFormat.ALICE.modelExtension);
        }

        String textureName = AliceResourceUtilties.getTextureResourceFileName(modelInfo.getModelName(), modelInfo.getTextureName());
        DataSource textureDataSource = createAliceTextureDataSource(resourcePath + "/" + textureName, texturedAppearancesToSave);
        if (!dataSources.contains(textureDataSource)) {
            dataSources.add(textureDataSource);
            updateManifestResourceReference(manifest, modelInfo, textureDataSource, ExportFormat.ALICE.textureExtension);
        }
    }


    //Uses the skeleton visual to create collada file and texture files
    //Adds appropriate DataSources and updates the manifest
    private void addColladaDataSources(List<DataSource> dataSources, SkeletonVisual sv, ModelResourceInfo modelInfo, ModelManifest manifest, String resourcePath) throws IOException{
        JointedModelColladaExporter exporter = new JointedModelColladaExporter(sv, modelInfo);
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
        //Now that we have a model and exporter, use this to get the id to image map
        textureSet.idToImageMap = exporter.createTextureIdToImageMap();
        //Get all the textures from the model as data sources
        List<DataSource> imageDataSources = exporter.createImageDataSources(resourcePath);
        for (DataSource imageDataSource : imageDataSources) {
            String imageFileName = imageDataSource.getName();
            //For each new image, create a new image reference and add it to the manifest
            if (!dataSources.contains(imageDataSource)) {
                Integer imageId = exporter.getTextureIdForName(imageFileName);
                String imageName = textureSet.idToImageMap.get(imageId);
                ImageReference imageReference = new ImageReference(exporter.createImageResourceForTexture(imageId));
                //ResourceReference have their id initialized to the filename of the resource.
                //We need it to be the imageName from the idToImageMap
                imageReference.id = imageName;
                manifest.images.add(imageReference);
                dataSources.add(imageDataSource);
            }
        }
    }

    public List<DataSource> createDataSources(String baseModelPath) throws IOException {
        List<DataSource> dataToWrite = new ArrayList<>();
        ModelManifest manifest = createModelManifest();

        //Model resources and manifest go into a folder named the model name
        String resourcePath = baseModelPath + "/"+ getModelName();
        for (ModelResourceInfo subInfo : modelInfo.getSubResources()) {
            SkeletonVisual sv = getVisualForModelResource(subInfo);
            if (exportFormat == ExportFormat.COLLADA) {
                addColladaDataSources(dataToWrite, sv, subInfo, manifest, resourcePath);
            }
            else if (exportFormat == ExportFormat.ALICE) {
                addAliceDataSources(dataToWrite, sv, subInfo, manifest, resourcePath);
            }
            else {
                Logger.warning("Not exporting "+getModelName()+"--Unsupported export format: "+exportFormat);
            }
            //Add DataSources for the thumbnails
            BufferedImage thumbnailImage = getThumbnailImageForModelResource(subInfo);
            dataToWrite.add(createPNGImageDataSource(resourcePath +"/"+ getThumbnailFilename(subInfo), thumbnailImage));
        }

        //The model manifest goes in the base model path directory
        dataToWrite.add(createDataSource(resourcePath +"/"+ getModelName()+".json", ManifestEncoderDecoder.toJson( manifest )));
        //Add the class thumbnail
        BufferedImage thumbnailImage = getThumbnailImageForModelResource(modelInfo);
        dataToWrite.add(createPNGImageDataSource(resourcePath +"/"+ getModelName()+"_cls.png", thumbnailImage));
        return dataToWrite;
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

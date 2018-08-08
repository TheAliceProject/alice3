package org.alice.tweedle.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManifestEncoderTest {
	private static final String SAMPLE_LIBRARY = "{ \"description\": {"
					+ " \"name\": \"Alice SThing Library\", \"icon\": \"thumbnail.png\", \"tags\": [],"
					+ " \"groupTags\": [], \"themeTags\": [] }, \"metadata\": {"
					+ " \"formatVersion\": \"0.1\", \"identifier\": { \"id\": \"SystemLibrary\","
					+ " \"type\": \"Library\", \"version\": \"0.7\" } },"
					+ " \"provenance\": { \"aliceVersion\": \"3.4.1.0\", \"creationYear\": \"2018\","
					+ " \"creator\": \"Not Dennis\" }, \"prerequisites\": []}";
	
	private static final String SAMPLE_MODEL = "{ \"description\": {"
					+ " \"name\": \"Alien\", \"icon\": \"thumbnail.png\","
					+ " \"tags\": [ \"alien\", \"space\" ], \"groupTags\": [ \"characters\" ],"
					+ " \"themeTags\": [ \"*outer space\" ] }, \"package\": {"
					+ " \"formatVersion\": \"0.1\", \"identifier\": { \"id\": \"Alien\","
					+ " \"type\": \"Model\", \"version\": \"1.0\" } },"
					+ " \"provenance\": { \"aliceVersion\": \"3.4.1.0\", \"creationYear\": \"2011\","
					+ " \"creator\": \"Laura Paoletti\" }, \"rootJoints\": [ \"ROOT\" ],"
					+ " \"additionalJoints\": ["
					+ " { \"name\": \"LOWER_LIP\", \"parent\": \"MOUTH\", \"visibility\": \"completelyHidden\" }"
					+ " ], \"additionalJointArrays\": [], \"poses\": [], \"boundingBox\": {"
					+ " \"min\": [ \"-0.2\", \"7.2\", \"-0.1\" ],"
					+ " \"max\": [ \"0.2\", \"1.4\", \"0.3\" ] },"
					+ " \"textureSets\": [ { \"id\": \"default\","
					+ " \"contentType\": \"image\", \"format\": \"png\","
					+ " \"files\": [ \"alien_diffuse.png\" ] } ], \"structures\": [ {"
					+ " \"id\": \"default\", \"contentType\": \"skeleton-mesh\","
					+ " \"format\": \"collada\", \"files\": [\"alien.dae\"], \"boundingBox\": {"
					+ " \"min\": [ \"-0.2\", \"7.2\", \"-0.1\" ],"
					+ " \"max\": [ \"0.2\", \"1.4\", \"0.3\" ]"
					+ " } } ], \"models\": [ { \"skeleton\": \"default\","
					+ " \"textureSet\": \"default\", \"icon\": \"thumbnail.png\" } ]}";

	private static final String SAMPLE_MODEL_WITH_3_RESOURCES = "{\"rootJoints\":[],\"additionalJoints\":[{\"name\":\"LOWER_LIP\",\"parent\":\"MOUTH\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"LEFT_THUMB_TIP\",\"parent\":\"LEFT_THUMB_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"LEFT_INDEX_FINGER_TIP\",\"parent\":\"LEFT_INDEX_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"LEFT_MIDDLE_FINGER_TIP\",\"parent\":\"LEFT_MIDDLE_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"LEFT_PINKY_FINGER_TIP\",\"parent\":\"LEFT_PINKY_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"RIGHT_THUMB_TIP\",\"parent\":\"RIGHT_THUMB_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"RIGHT_INDEX_FINGER_TIP\",\"parent\":\"RIGHT_INDEX_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"RIGHT_MIDDLE_FINGER_TIP\",\"parent\":\"RIGHT_MIDDLE_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"RIGHT_PINKY_FINGER_TIP\",\"parent\":\"RIGHT_PINKY_FINGER_KNUCKLE\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"LEFT_TOES\",\"parent\":\"LEFT_FOOT\",\"visibility\":\"COMPLETELY_HIDDEN\"},{\"name\":\"RIGHT_TOES\",\"parent\":\"RIGHT_FOOT\",\"visibility\":\"COMPLETELY_HIDDEN\"}],\"additionalJointArrays\":[],\"additionalJointArrayIds\":[],\"poses\":[],\"boundingBox\":{\"min\":[-0.281617,7.213192E-4,-0.18244708],\"max\":[0.2846615,1.4431626,0.39562756]},\"placeOnGround\":false,\"textureSets\":[{\"id\":\"Alien_DEFAULT\",\"idToResourceMap\":{\"1\":\"Alien_DEFAULT_texture_1_diffuseMap\"}}],\"structures\":[{\"boundingBox\":{\"min\":[-0.281617,7.213192E-4,-0.18244708],\"max\":[0.2846615,1.4431626,0.39562756]},\"id\":\"Alien\",\"format\":\"dae\",\"file\":\"Alien_Alien.dae\",\"type\":\"skeletonMesh\"}],\"models\":[{\"id\":\"DEFAULT\",\"structure\":\"Alien\",\"textureSet\":\"Alien_DEFAULT\",\"icon\":\"DEFAULT.png\"}],\"description\":{\"name\":\"Alien\",\"icon\":\"Alien_cls.png\",\"tags\":[\"alien\",\"space\"],\"groupTags\":[\"characters\"],\"themeTags\":[\"*outer space\"]},\"provenance\":{\"aliceVersion\":\"3.4.0.0-alpha\",\"creationYear\":\"2011\",\"creator\":\"Laura Paoletti\"},\"metadata\":{\"formatVersion\":\"0.1+alpha\",\"identifier\":{\"version\":\"1.0\",\"type\":\"Model\"}},\"prerequisites\":[],\"resources\":[{\"uuid\":\"ec707422-033d-4c74-99f2-f4ebeea77642\",\"height\":512.0,\"width\":512.0,\"id\":\"Alien_DEFAULT_texture_1_diffuseMap\",\"format\":\"image/png\",\"file\":\"Alien_DEFAULT_texture_1_diffuseMap.png\",\"type\":\"image\"},{\"uuid\":\"d1cb1b8c-e08f-4e9a-8f89-3b6847fc1dbb\",\"height\":120.0,\"width\":43.0,\"id\":\"DEFAULT.png\",\"format\":\"image/png\",\"file\":\"DEFAULT.png\",\"type\":\"image\"},{\"uuid\":\"463c8b29-2fad-4203-9abe-5b7acad26544\",\"height\":120.0,\"width\":43.0,\"id\":\"Alien_cls.png\",\"format\":\"image/png\",\"file\":\"Alien_cls.png\",\"type\":\"image\"}]}";

	@Test
	public void aLibraryManifestShouldBeCreatedFromLibraryManifestJson() {
		LibraryManifest lib = ManifestEncoderDecoder.fromJson( SAMPLE_LIBRARY, LibraryManifest.class );

		assertNotNull("The encoder should have returned something.", lib );
	}

	@Test
	public void somethingShouldBeCreatedForSerializedLibraryManifest() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		assertNotNull("The encoder should have returned a String.", json );
	}

	@Test
	public void aJsonObjectShouldBeCreatedForSerializedLibraryManifest() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);

		assertTrue("The encoder should have returned JSON.", element.isJsonObject() );
	}

	@Test
	public void serializedLibraryManifestShouldNotHaveTopLevelId() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonManifest = element.getAsJsonObject();

		assertNull("The json object should not have an id.", jsonManifest.get( "id" ) );
	}

	@Test
	public void serializedLibraryManifestShouldHaveMetadata() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonManifest = element.getAsJsonObject();

		assertNotNull("The json object should have metadata object.", jsonManifest.get( "metadata" ) );
	}

	@Test
	public void serializedLibraryManifestsMetadataShouldHaveIdentifier() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonManifest = element.getAsJsonObject();
		JsonObject metadata = jsonManifest.get( "metadata" ).getAsJsonObject();

		assertNotNull("The json metadata object should have an identifier.", metadata.get( "identifier" ) );
	}

	@Test
	public void serializedLibraryManifestsMetadataIdentifierShouldHaveId() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonManifest = element.getAsJsonObject();
		JsonObject metadata = jsonManifest.get( "metadata" ).getAsJsonObject();
		JsonObject identifier = metadata.get( "identifier" ).getAsJsonObject();
		String id = identifier.get( "id" ).getAsString();

		assertEquals("The metadata identifier should have an id of 'testProject'.", "testProject", id );
	}

	@Test
	public void serializedLibraryManifestShouldNotHaveTopLevelName() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonObject = element.getAsJsonObject();

		assertNull("The json object should not have an name.", jsonObject.get( "name" ) );
	}

	@Test
	public void serializedLibraryManifestsDescriptionShouldHaveName() {
		String json = ManifestEncoderDecoder.toJson( getSimpleLibraryManifest() );

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject jsonManifest = element.getAsJsonObject();
		JsonObject description = jsonManifest.get( "description" ).getAsJsonObject();
		String name = description.get( "name" ).getAsString();

		assertEquals("The decription should have a name of 'A test project'.", "A test project", name );
	}

	@Test
	public void aModelManifestShouldBeCreatedFromModelManifestJson() {
		ModelManifest model = ManifestEncoderDecoder.fromJson( SAMPLE_MODEL, ModelManifest.class );

		assertNotNull("The encoder should have returned something.", model );
	}

	@Test
	public void aModelManifestShouldBeCreatedFromModelWithResourcesManifestJson() {
		ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class );

		assertNotNull("The encoder should have returned something.", model );
	}

	@Test
	public void aResourcesShouldBeCreatedFromModelWithResourcesManifestJson() {
		ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class );

		assertNotNull("The model.resources should have been initialized to something.", model.resources );
	}

	@Test
	public void theRightNumberOfResourceShouldBeCreatedFromModelWithResourcesManifestJson() {
		ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class );

		assertTrue("The model.resources should have 3 resources in it.", model.resources.size() == 3 );
	}

	private LibraryManifest getSimpleLibraryManifest() {
		LibraryManifest lib = new LibraryManifest();
		lib.metadata = new Manifest.MetaData();
		final Manifest.ProjectIdentifier projectIdentifier = new Manifest.ProjectIdentifier();
		projectIdentifier.id = "testProject";
		projectIdentifier.version = "0.1";
		projectIdentifier.type = Manifest.ProjectType.Library;
		lib.metadata.identifier = projectIdentifier;
		lib.metadata.formatVersion = "0.3";

		final Manifest.Description desc = new Manifest.Description();
		desc.name = "A test project";
		lib.description = desc;
		return lib;
	}
}
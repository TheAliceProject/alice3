package org.alice.tweedle.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.IOException;

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
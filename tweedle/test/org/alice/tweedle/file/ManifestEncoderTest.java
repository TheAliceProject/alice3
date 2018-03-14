package org.alice.tweedle.file;

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

	@Test
	public void somethingShouldBeCreatedFromLibraryManifestJson() {

		LibraryManifest lib = ManifestEncoderDecoder.fromJson( SAMPLE_LIBRARY, LibraryManifest.class );

		assertNotNull("The encoder should have returned something.", lib );
	}

	@Test
	public void somethingShouldBeCreatedForARootClass() throws IOException {
		LibraryManifest lib = getSimpleLibraryManifest();

		String json = ManifestEncoderDecoder.toJson( lib );

		assertNotNull("The encoder should have returned something.", json );
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
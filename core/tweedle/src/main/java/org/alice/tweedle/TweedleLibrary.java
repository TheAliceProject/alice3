package org.alice.tweedle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweedleLibrary {


/*	aliceVersion - The version of Alice that generated the file.
	projectType - What kind of project it it. Current plans are to support TweedleLibrary, World, Class, and Model.
	main - An optional list of tweedle statements to be executed to start this project. Specific to World projects.*/

	private String name;
	private String id;
	// TODO convert String to Version (SemVer)
	private String version;

	// Where it is on disk may be needed for resource access.
	private File storageLocation;

	private final Map<String, String> prerequisites = new HashMap<String,String>(); // name -> version
	private final Map<String, TweedleClass> classes = new HashMap<String,TweedleClass>();
	private final Map<String, TweedleResource> resources = new HashMap<String,TweedleResource>();

	public TweedleLibrary( List<TweedleType> types ) {
		// TODO store the types
	}

	public TweedleType getTypeNamed(String name) {
		// TODO check Primitive Types
		return classes.get( name );
	}
}

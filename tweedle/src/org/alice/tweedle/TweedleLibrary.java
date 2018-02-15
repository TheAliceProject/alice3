package org.alice.tweedle;

import java.io.File;
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

	private Map<String, String> prerequisites; // name -> version
	private Map<String, TweedleClass> classes;
	private Map<String, TweedleResource> resources;

	public TweedleClass newClass(String name) {
		if (classes.containsKey( name ) ){
			// TODO - reject names that are primitives
			// TODO - handle repeated definitions
		}
		return classes.put( name, new TweedleClass(name) );
	}

	public TweedleType getTypeNamed(String name) {
		// TODO check Primitive Types
		return classes.get( name );
	}
}

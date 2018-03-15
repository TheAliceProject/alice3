package org.alice.tweedle.file;

import java.util.ArrayList;
import java.util.List;

public class Manifest {

	public Description description;
	public Provenance provenance;
	public MetaData metadata;
	public List<ProjectIdentifier> prerequisites = new ArrayList<>();
	public List<ResourceReference> resources = new ArrayList<>();

	public String getId() {
		return metadata.identifier.id;
	}

	public String getName() {
		return description.name;
	}

	static class Description {
		public String name;
		public String icon;
		public List<String> tags;
		public List<String> groupTags;
		public List<String> themeTags;
	}

	static class Provenance {
		public String aliceVersion;
		public String creationYear;
		public String creator;
	}

	static class MetaData {
		public String formatVersion;
		public ProjectIdentifier identifier;
	}

	static class ProjectIdentifier {
		public String id;
		public String version;
		public ProjectType type;
	}

	public enum ProjectType
	{
		Library, World, Model
	}
}

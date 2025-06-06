package org.alice.tweedle.file;

import org.junit.Test;

import static org.junit.Assert.*;

public class ManifestEncoderTest {
  private static final String SAMPLE_LIBRARY = """
    {
      "description": {
        "name": "Alice SThing Library",
        "icon": "thumbnail.png",
        "tags": [],
        "groupTags": [],
        "themeTags": []
      },
      "metadata": {
        "formatVersion": "0.1",
        "identifier": {
          "name": "SystemLibrary",
          "type": "Library",
          "version": "0.7"
        }
      },
      "provenance": {
        "aliceVersion": "3.4.1.0",
        "created": "2018",
        "creator": "Not Dennis"
      },
      "prerequisites": []
    }""";

  private static final String LIBRARY_WITH_DATE = """
    {
      "description": {
        "name": "Alice SThing Library",
        "icon": "thumbnail.png",
        "tags": [],
        "groupTags": [],
        "themeTags": []
      },
      "metadata": {
        "formatVersion": "0.1",
        "identifier": {
          "name": "SystemLibrary",
          "type": "Library",
          "version": "0.7"
        }
      },
      "provenance": {
        "aliceVersion": "3.4.1.0",
        "created": "2010-01-01T12:00:00+01:00",
        "creator": "Not Dennis"
      },
      "prerequisites": []
    }""";

  private static final String SAMPLE_MODEL = """
    {
        "description": {
          "name": "Alien",
          "icon": "thumbnail.png",
          "tags": [
            "alien",
            "space"
          ],
          "groupTags": [
            "characters"
          ],
          "themeTags": [
            "*outer space"
          ]
        },
        "metadata": {
          "formatVersion": "0.1",
          "identifier": {
            "name": "Alien",
            "type": "Model",
            "version": "1.0"
          }
        },
        "provenance": {
          "aliceVersion": "3.4.1.0",
          "created": "2011",
          "creator": "Laura Paoletti"
        },
        "rootJoints": [
          "ROOT"
        ],
        "additionalJoints": [
          {
            "name": "LOWER_LIP",
            "parent": "MOUTH",
            "visibility": "COMPLETELY_HIDDEN"
          }
        ],
        "additionalJointArrays": [],
        "poses": [],
        "boundingBox": {
          "min": [
            "-0.2",
            "7.2",
            "-0.1"
          ],
          "max": [
            "0.2",
            "1.4",
            "0.3"
          ]
        },
        "textureSets": [
          {
            "name":"Alien_DEFAULT",
            "idToResourceMap":{"1":"Alien_DEFAULT_texture_1_diffuseMap"}
          }
        ],
        "models": [
          {
            "structure": "defaultSkeletonVisual",
            "textureSet": "default",
            "icon": "thumbnail.png"
          }
        ]
      }""";

  private static final String SAMPLE_MODEL_WITH_3_RESOURCES = """
    {
      "rootJoints": [],
      "additionalJoints": [
        {
          "name": "LOWER_LIP",
          "parent": "MOUTH",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "LEFT_THUMB_TIP",
          "parent": "LEFT_THUMB_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "LEFT_INDEX_FINGER_TIP",
          "parent": "LEFT_INDEX_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "LEFT_MIDDLE_FINGER_TIP",
          "parent": "LEFT_MIDDLE_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "LEFT_PINKY_FINGER_TIP",
          "parent": "LEFT_PINKY_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "RIGHT_THUMB_TIP",
          "parent": "RIGHT_THUMB_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "RIGHT_INDEX_FINGER_TIP",
          "parent": "RIGHT_INDEX_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "RIGHT_MIDDLE_FINGER_TIP",
          "parent": "RIGHT_MIDDLE_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "RIGHT_PINKY_FINGER_TIP",
          "parent": "RIGHT_PINKY_FINGER_KNUCKLE",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "LEFT_TOES",
          "parent": "LEFT_FOOT",
          "visibility": "COMPLETELY_HIDDEN"
        },
        {
          "name": "RIGHT_TOES",
          "parent": "RIGHT_FOOT",
          "visibility": "COMPLETELY_HIDDEN"
        }
      ],
      "additionalJointArrays": [],
      "additionalJointArrayIds": [],
      "poses": [],
      "boundingBox": {
        "min": [
          -0.281617,
          7.213192E-4,
          -0.18244708
        ],
        "max": [
          0.2846615,
          1.4431626,
          0.39562756
        ]
      },
      "placeOnGround": false,
      "textureSets": [
        {
          "name": "Alien_DEFAULT",
          "idToResourceMap": {
            "1": "Alien_DEFAULT_texture_1_diffuseMap"
          }
        }
      ],
      "models": [
        {
          "name": "DEFAULT",
          "structure": "Alien",
          "textureSet": "Alien_DEFAULT",
          "icon": "DEFAULT.png"
        }
      ],
      "description": {
        "name": "Alien",
        "icon": "Alien_cls.png",
        "tags": [
          "alien",
          "space"
        ],
        "groupTags": [
          "characters"
        ],
        "themeTags": [
          "*outer space"
        ]
      },
      "provenance": {
        "aliceVersion": "3.4.0.0-alpha",
        "created": "2011",
        "creator": "Laura Paoletti"
      },
      "metadata": {
        "formatVersion": "0.1+alpha",
        "identifier": {
          "version": "1.0",
          "type": "Model"
        }
      },
      "prerequisites": [],
      "resources": [
        {
          "uuid": "ec707422-033d-4c74-99f2-f4ebeea77642",
          "height": 512.0,
          "width": 512.0,
          "name": "Alien_DEFAULT_texture_1_diffuseMap",
          "format": "image/png",
          "file": "Alien_DEFAULT_texture_1_diffuseMap.png",
          "type": "image"
        },
        {
          "uuid": "d1cb1b8c-e08f-4e9a-8f89-3b6847fc1dbb",
          "height": 120.0,
          "width": 43.0,
          "name": "DEFAULT.png",
          "format": "image/png",
          "file": "DEFAULT.png",
          "type": "image"
        },
        {
          "uuid": "463c8b29-2fad-4203-9abe-5b7acad26544",
          "height": 120.0,
          "width": 43.0,
          "name": "Alien_cls.png",
          "format": "image/png",
          "file": "Alien_cls.png",
          "type": "image"
        }
      ]
    }""";

  @Test
  public void aLibraryManifestShouldBeCreatedFromLibraryManifestJson() {
    LibraryManifest lib = ManifestEncoderDecoder.fromJson(SAMPLE_LIBRARY, LibraryManifest.class);

    assertNotNull("The encoder should have returned something.", lib);
  }

  @Test
  public void aLibraryManifestShouldHaveAProvenance() {
    LibraryManifest lib = ManifestEncoderDecoder.fromJson(SAMPLE_LIBRARY, LibraryManifest.class);

    assertNotNull("The manifest should have a provenance.", lib.provenance);
  }

  @Test
  public void aLibraryManifestProvenanceShouldHaveACreatedYear() {
    LibraryManifest lib = ManifestEncoderDecoder.fromJson(SAMPLE_LIBRARY, LibraryManifest.class);

    assertNotNull("The manifest's provenance should have a created year.", lib.provenance.created);
  }

  @Test
  public void aLibraryManifestProvenanceShouldHaveACreatedDate() {
    LibraryManifest lib = ManifestEncoderDecoder.fromJson(LIBRARY_WITH_DATE, LibraryManifest.class);

    assertNotNull("The manifest's provenance should have a created date.", lib.provenance.created);
  }

  @Test
  public void somethingShouldBeCreatedForSerializedLibraryManifest() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());

    assertNotNull("The encoder should have returned a String.", json);
  }

  @Test
  public void aJsonObjectShouldBeCreatedForSerializedLibraryManifest() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());

    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The decoder should have returned a manifest.", manifest);
  }

  @Test
  public void serializedLibraryManifestShouldHaveMetadata() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The json object should have metadata object.", manifest.metadata);
  }

  @Test
  public void serializedLibraryManifestsMetadataShouldHaveIdentifier() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The metadata object should have an identifier.", manifest.metadata.identifier);
  }

  @Test
  public void serializedLibraryManifestsMetadataIdentifierShouldHaveId() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);
    String id = manifest.metadata.identifier.name;

    assertEquals("The metadata identifier should have an name of 'testProject'.", "testProject", id);
  }

  @Test
  public void serializedLibraryManifestsDescriptionShouldHaveName() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);
    String name = manifest.description.name;

    assertEquals("The description should have a name of 'A test project'.", "A test project", name);
  }

  @Test
  public void serializedLibraryManifestShouldHaveProvenance() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The manifest should have a provenance.", manifest.provenance);
  }

  @Test
  public void serializedLibraryManifestShouldHaveProvenanceWithCreated() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The manifest's provenance should have a created.", manifest.provenance.created);
  }

  @Test
  public void serializedLibraryManifestProvenanceCreatedShouldBeReadAsTemporal() {
    String json = ManifestEncoderDecoder.toJson(getSimpleLibraryManifest());
    Manifest manifest = ManifestEncoderDecoder.fromJson(json, Manifest.class);

    assertNotNull("The manifest's provenance should have a parsed created.", manifest.provenance.created);
  }

  @Test
  public void aModelManifestShouldBeCreatedFromModelManifestJson() {
    ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL, ModelManifest.class);

    assertNotNull("The encoder should have returned something.", model);
  }

  @Test
  public void aModelManifestShouldBeCreatedFromModelWithResourcesManifestJson() {
    ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class);

    assertNotNull("The encoder should have returned something.", model);
  }

  @Test
  public void aResourcesShouldBeCreatedFromModelWithResourcesManifestJson() {
    ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class);

    assertNotNull("The model.resources should have been initialized to something.", model.resources);
  }

  @Test
  public void theRightNumberOfResourceShouldBeCreatedFromModelWithResourcesManifestJson() {
    ModelManifest model = ManifestEncoderDecoder.fromJson(SAMPLE_MODEL_WITH_3_RESOURCES, ModelManifest.class);

    assertEquals("The model.resources should have 3 resources in it.", 3, model.resources.size());
  }

  private LibraryManifest getSimpleLibraryManifest() {
    LibraryManifest lib = new LibraryManifest();
    lib.metadata = new Manifest.MetaData();
    final Manifest.ProjectIdentifier projectIdentifier = new Manifest.ProjectIdentifier();
    projectIdentifier.name = "testProject";
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

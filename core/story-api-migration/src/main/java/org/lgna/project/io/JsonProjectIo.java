/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.util.zip.ByteArrayDataSource;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.alice.serialization.tweedle.TweedleEncoderDecoder;
import org.alice.tweedle.file.*;
import org.lgna.common.Resource;
import org.lgna.common.resources.AudioResource;
import org.lgna.common.resources.ImageResource;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;
import org.lgna.project.ast.*;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.ResourceTypeHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

//TODO add migration on read - ProjectMigrationManager, MigrationManager, and DecodedVersion
public class JsonProjectIo extends DataSourceIo implements ProjectIo {
  private static final String TWEEDLE_EXTENSION = "twe";
  private static final String TWEEDLE_FORMAT = "tweedle";

  public static JsonProjectReader reader(ZipEntryContainer container) {
    return new JsonProjectReader(container);
  }

  public static JsonProjectWriter writer() {
    return new JsonProjectWriter();
  }

  private static class JsonProjectReader implements ProjectReader {
    JsonProjectReader(ZipEntryContainer container) {
    }

    @Override
    public Project readProject(boolean makeVrReady) throws IOException {
      ProjectManifest manifest = readManifest();
      Set<Resource> resources = readResources(manifest);
      //TODO Read manifest and content for program type
      return new Project(manifest, null, Collections.emptySet(), resources);
    }

    @Override
    public TypeResourcesPair readType() throws IOException {
      Manifest manifest = readManifest();
      Set<Resource> resources = readResources(manifest);
      return new TypeResourcesPair(null, resources);
    }

    @Override
    public Version checkForFutureVersion() throws IOException {
      // TODO
      return null;
    }

    @Override
    public void setResourceTypeHelper(ResourceTypeHelper typeHelper) {
      // Ignored for now
    }

    private ProjectManifest readManifest() {
      return null;
    }

    // On XML side this reads the resources.xml and files in the referenced files in the resource directory.
    // It relies on further XML decoding inside Resource class as well.
    private static Set<Resource> readResources(Manifest manifest) throws IOException {
      Set<Resource> resources = new HashSet<>();
      for (ResourceReference resource : manifest.resources) {
        resources.add(readResource(resource));
      }
      return resources;
    }

    private static Resource readResource(ResourceReference resourceReference) {
      String contentType = resourceReference.getContentType();
      String id = resourceReference.name;
      String entry = resourceReference.file;
      if ((contentType != null) && (id != null) && (entry != null)) {
        // TODO Read all entries
        //      byte[] data = InputStreamUtilities.getBytes( container.getInputStream( entryName ) );
        //      if( data != null ) {
        // TODO translate contentType to Resource subclass and fill in data
        Resource resource = null;
        return resource;
        //      } else {
        //        PrintUtilities.println( "WARNING: no data for resource:", entryName );
        //      }
      }
      return null;
    }
  }

  private static class JsonProjectWriter implements ProjectWriter {
    JsonProjectWriter() {
    }

    private final TweedleEncoderDecoder coder = new TweedleEncoderDecoder();

    @Override
    public void writeType(OutputStream os, NamedUserType type, DataSource... dataSources) throws IOException {
      TypeManifest manifest = createTypeManifest(type);
      Set<Resource> resources = getResources(type, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY);

      List<DataSource> entries = collectEntries(manifest, resources, dataSources);
      entries.add(dataSourceForType(manifest, type));
      entries.add(manifestDataSource(manifest));
      writeDataSources(os, entries);
    }

    private TypeManifest createTypeManifest(AbstractType<?, ?, ?> type) {
      final TypeManifest manifest = new TypeManifest();
      manifest.description.name = type.getName();
      manifest.provenance.aliceVersion = ProjectVersion.getCurrentVersion().toString();
      manifest.metadata.identifier.name = type.getId().toString();
      manifest.metadata.identifier.type = Manifest.ProjectType.Library;
      return manifest;
    }

    @Override
    public void writeProject(OutputStream os, final Project project, DataSource... dataSources) throws IOException {
      final JsonModelIo.ExportFormat format = JsonModelIo.ExportFormat.GLTF;
      Manifest manifest = project.createExportManifest();
      Set<Resource> resources = getResources(project.getProgramType(), CrawlPolicy.COMPLETE);
      compareResources(project.getResources(), resources);

      List<DataSource> entries = collectEntries(manifest, resources, dataSources);
      ModelResourceCrawler crawler = new ModelResourceCrawler();
      project.getProgramType().crawl(crawler, CrawlPolicy.COMPLETE);
      entries.addAll(createEntriesForTypes(manifest, crawler.activeUserTypes));
      Map<String, Set<JointedModelResource>> modelResources = crawler.modelResources;
      for (Set<JointedModelResource> resourceSet : modelResources.values()) {
        JsonModelIo modelIo = new JsonModelIo(resourceSet, format);
        entries.addAll(modelIo.createDataSources("models"));
        entries.addAll(createEntriesForResourceTypes(manifest, resourceSet));
        manifest.resources.add(modelIo.createModelReference("models"));
      }
      final Set<InstanceCreation> personResourceCreations = crawler.personCreations;
      if (!personResourceCreations.isEmpty()) {
        JsonModelIo modelIo = new JsonPersonIo(personResourceCreations, format);
        entries.addAll(modelIo.createDataSources("models"));
        manifest.resources.add(modelIo.createModelReference("models"));
      }
      for (DynamicResource<?, ?> dynamicResource: crawler.dynamicResources) {
        JsonModelIo modelIo = new JsonModelIo(dynamicResource, format);
        entries.addAll(modelIo.createDataSources("models"));
        entries.add(createEntryForResourceTypes(manifest, dynamicResource));
        manifest.resources.add(modelIo.createModelReference("models"));
      }
      entries.add(manifestDataSource(manifest));
      writeDataSources(os, entries);
    }

    private Collection<? extends DataSource> createEntriesForTypes(Manifest manifest, Set<NamedUserType> userTypes) {
      return userTypes.stream().sorted(Comparator.comparingInt(AbstractType::hierarchyDepth)).map(ut -> dataSourceForType(manifest, ut)).collect(Collectors.toList());
    }

    private DataSource dataSourceForType(Manifest manifest, NamedUserType ut) {
      // Special case to catch older models from starter worlds
      String likelyTypeName = ut.getName();
      String typeName = "SandDunes".equals(likelyTypeName) ? "Terrain" : likelyTypeName;

      if (manifest.resources.stream().noneMatch(x -> x.name.equals(typeName))) {
        final String fileName = "src/" + typeName + '.' + TWEEDLE_EXTENSION;
        manifest.resources.add(new TypeReference(typeName, fileName, TWEEDLE_FORMAT));
        return new ByteArrayDataSource(fileName, serializedClass(ut));
      }
      return null;
    }

    private String serializedClass(NamedUserType userType) {
      return coder.encode(userType);
    }

    private Collection<? extends DataSource> createEntriesForResourceTypes(Manifest manifest, Set<JointedModelResource> resources) {
      Set<Class<?>> distinctResources = new HashSet<>();
      return resources.stream()
                      .filter(resource -> !distinctResources.contains(resource.getClass()))
                      .map(resource -> {
                        distinctResources.add(resource.getClass());
                        return dataSourceForResource(manifest, resource);
                      })
                      .collect(Collectors.toList());
    }

    private DataSource createEntryForResourceTypes(Manifest manifest, DynamicResource<?, ?> resource) {
      String typeName = resource.getModelVariantName() + "Resource";
      final String fileName = "src/" + typeName + '.' + TWEEDLE_EXTENSION;
      manifest.resources.add(new TypeReference(typeName, fileName, TWEEDLE_FORMAT));
      return new ByteArrayDataSource(fileName, coder.encodeProcessable(resource));
    }

    private DataSource dataSourceForResource(Manifest manifest, JointedModelResource resource) {
      String typeName = resource.getClass().getSimpleName();
      final String fileName = "src/" + typeName + '.' + TWEEDLE_EXTENSION;
      manifest.resources.add(new TypeReference(typeName, fileName, TWEEDLE_FORMAT));
      return new ByteArrayDataSource(fileName, coder.encodeProcessable(resource));
    }

    private void compareResources(Set<Resource> projectResources, Set<Resource> crawledResources) {
      for (Resource crawledResource : crawledResources) {
        if (!projectResources.contains(crawledResource)) {
          PrintUtilities.println("WARNING: added missing resource", crawledResource);
        }
      }
    }

    private List<DataSource> collectEntries(Manifest manifest, Set<Resource> resources, DataSource[] dataSources) {
      List<DataSource> entries = new ArrayList<>();
      Collections.addAll(entries, dataSources);
      entries.add(versionDataSource());
      addResources(manifest, entries, resources);
      return entries;
    }

    private static DataSource versionDataSource() {
      return new ByteArrayDataSource(VERSION_ENTRY_NAME, ProjectVersion.getCurrentVersion().toString());
    }

    private static DataSource manifestDataSource(Manifest manifest) {
      return new ByteArrayDataSource(MANIFEST_ENTRY_NAME, ManifestEncoderDecoder.toJson(manifest));
    }

    private Set<Resource> getResources(AbstractType<?, ?, ?> type, CrawlPolicy crawlPolicy) {
      IsInstanceCrawler<ResourceExpression> crawler = new IsInstanceCrawler<>(ResourceExpression.class) {
        @Override
        protected boolean isAcceptable(ResourceExpression resourceExpression1) {
          return true;
        }
      };

      type.crawl(crawler, crawlPolicy);
      Set<Resource> resources = new HashSet<>();
      for (ResourceExpression resourceExpression : crawler.getList()) {
        resources.add(resourceExpression.resource.getValue());
      }
      return resources;
    }

    private static void addResources(Manifest manifest, List<DataSource> dataSources, Set<Resource> resources) {
      Set<String> usedEntryNames = new HashSet<>();
      for (Resource resource : resources) {
        String entryName = generateEntryName(resource, usedEntryNames);
        usedEntryNames.add(entryName);
        addResourceReference(manifest, resource, entryName);
        // TODO Expand to cover arbitrary data files
        dataSources.add(new ByteArrayDataSource(entryName, resource.getData()));
      }
    }

    private static void addResourceReference(Manifest manifest, Resource resource, String entryName) {
      final ResourceReference resourceReference = resourceReference(resource);
      resourceReference.file = entryName;
      manifest.resources.add(resourceReference);
    }

    private static ResourceReference resourceReference(Resource resource) {
      if (resource instanceof AudioResource) {
        return new AudioReference((AudioResource) resource);
      }
      if (resource instanceof ImageResource) {
        return new ImageReference((ImageResource) resource);
      }
      throw new RuntimeException("Resource of unexpected type " + resource);
    }

    private static String generateEntryName(Resource resource, Set<String> usedEntryNames) {
      String fileName = resource.getOriginalFileName();
      String entryName = potentialEntryName(fileName, "");
      int i = 1;
      while (usedEntryNames.contains(entryName)) {
        i++;
        entryName = potentialEntryName(fileName, String.valueOf(i));
      }
      return entryName;
    }

    private static String potentialEntryName(String validFilename, String i) {
      return "resources" + i + "/" + validFilename;
    }
  }
}

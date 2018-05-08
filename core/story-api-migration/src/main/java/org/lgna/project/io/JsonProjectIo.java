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
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.alice.serialization.tweedle.TweedleEncoderDecoder;
import org.alice.tweedle.file.AudioReference;
import org.alice.tweedle.file.ImageReference;
import org.alice.tweedle.file.Manifest;
import org.alice.tweedle.file.ManifestEncoderDecoder;
import org.alice.tweedle.file.ResourceReference;
import org.alice.tweedle.file.TypeReference;
import org.lgna.common.Resource;
import org.lgna.common.resources.AudioResource;
import org.lgna.common.resources.ImageResource;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ResourceExpression;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

//TODO add migration on read - ProjectMigrationManager, MigrationManagerDecodedVersionPair, & AstMigrationUtilities
public class JsonProjectIo implements ProjectIo{

	private static final String MANIFEST_ENTRY_NAME = "manifest.json";
	private static final String TWEEDLE_EXTENSION = "twe";
	private static final String TWEEDLE_FORMAT = "tweedle";

	private TweedleEncoderDecoder coder = new TweedleEncoderDecoder();

	@Override
	public Project readProject( ZipEntryContainer container ) throws IOException, VersionNotSupportedException {
		Manifest manifest = readManifest( container);
		Set<Resource> resources = readResources( container, manifest );
		//TODO Read manifest and content for program type
		return new Project( null, Collections.emptySet(), resources );
	}

	@Override
	public TypeResourcesPair readType( ZipEntryContainer container ) throws IOException, VersionNotSupportedException {
		Manifest manifest = readManifest( container );
		Set<Resource> resources = readResources( container, manifest );
		return new TypeResourcesPair( null, resources );
	}

	private Manifest readManifest( ZipEntryContainer container ) {
		return null;
	}

	// On XML side this reads the resources.xml and files in the referenced files in the resource directory.
	// It relies on further XML decoding inside Resource class as well.
	private static Set<Resource> readResources( ZipEntryContainer container, Manifest manifest ) throws IOException {
		Set<Resource> resources = new HashSet<>();
		for( ResourceReference resource : manifest.resources ) {
			resources.add( readResource( container, resource ) );
		}
		return resources;
	}

	private static Resource readResource( ZipEntryContainer container, ResourceReference resourceReference ) {
		String contentType = resourceReference.getContentType();
		String id = resourceReference.id;
		List<String> entries = resourceReference.files;
		if( ( contentType != null ) && ( id != null ) && ( entries != null ) && ( entries.size() > 0 ) ) {
			// TODO Read all entries
//			byte[] data = InputStreamUtilities.getBytes( container.getInputStream( entryName ) );
//			if( data != null ) {
			  // TODO translate contentType to Resource subclass and fill in data
				Resource resource = null;
				return resource;
//			} else {
//				PrintUtilities.println( "WARNING: no data for resource:", entryName );
//			}
		}
		return null;
	}

	@Override
	public void writeType( OutputStream os, NamedUserType type, DataSource... dataSources ) throws IOException {
		Manifest manifest = createTypeManifest( type );
		Set<Resource> resources = getResources( type, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );

		List<DataSource> entries = collectEntries( manifest, resources, dataSources );
		entries.add( dataSourceForType( manifest, type ) );
		entries.add( manifestDataSource( manifest ) );
		writeDataSources( os, entries );
	}

	private Manifest createTypeManifest( AbstractType<?, ?, ?> type ) {
		final Manifest manifest = new Manifest();
		manifest.description.name = type.getName();
		manifest.provenance.aliceVersion = ProjectVersion.getCurrentVersion().toString();
		manifest.metadata.identifier.id = type.getId().toString();
		manifest.metadata.identifier.type = Manifest.ProjectType.Library;
		return manifest;
	}

	@Override
	public void writeProject( OutputStream os, final Project project, DataSource... dataSources ) throws IOException {
		Manifest manifest = createProjectManifest( project );
		Set<Resource> resources = getResources( project.getProgramType(), CrawlPolicy.COMPLETE);
		compareResources( project.getResources(), resources );

		List<DataSource> entries = collectEntries( manifest, resources, dataSources );
		entries.addAll( createEntriesForTypes( manifest, project.getNamedUserTypes()) );
		entries.add( manifestDataSource( manifest ) );
		writeDataSources( os, entries );
	}

	private Collection<? extends DataSource> createEntriesForTypes( Manifest manifest, Set<NamedUserType> userTypes ) {
		return userTypes.stream().map( ut -> dataSourceForType( manifest, ut ) ).collect( Collectors.toList() );
	}

	private DataSource dataSourceForType( Manifest manifest, NamedUserType ut ) {
		final String fileName = "src/" + ut.getName() + '.' + TWEEDLE_EXTENSION;
		manifest.resources.add( new TypeReference( ut.getName(), fileName, TWEEDLE_FORMAT ) );
		return createDataSource( fileName, serializedClass( ut) );
	}

	private String serializedClass( NamedUserType userType ) {
		return coder.encode( userType );
	}

	private Manifest createProjectManifest( Project project ) {
		final Manifest manifest = new Manifest();
		manifest.description.name = project.getProgramType().getName(); // probably "Program"
		manifest.provenance.aliceVersion = ProjectVersion.getCurrentVersion().toString();
		manifest.metadata.identifier.id = project.getProgramType().getId().toString();
		manifest.metadata.identifier.type = Manifest.ProjectType.World;
		return manifest;
	}

	private void compareResources( Set<Resource> projectResources, Set<Resource> crawledResources ) {
		for( Resource crawledResource : crawledResources ) {
			if (!projectResources.contains( crawledResource )) {
				PrintUtilities.println( "WARNING: added missing resource", crawledResource );
			}
		}
	}

	private List<DataSource> collectEntries( Manifest manifest, Set<Resource> resources, DataSource[] dataSources ){
		List<DataSource> entries = new ArrayList<>(  );
		Collections.addAll( entries, dataSources );
		entries.add( versionDataSource() );
		addResources( manifest, entries, resources );
		return entries;
	}

	private static DataSource versionDataSource( ) {
		return createDataSource( VERSION_ENTRY_NAME, ProjectVersion.getCurrentVersion().toString() );
	}

	private static DataSource manifestDataSource( Manifest manifest ) {
		return createDataSource( MANIFEST_ENTRY_NAME, ManifestEncoderDecoder.toJson( manifest ) );
	}

	private static DataSource createDataSource( String name, String content ) {
		return new DataSource() {
			@Override public String getName() { return name; }

			@Override public void write( OutputStream os ) throws IOException {
				os.write( content.getBytes() );
			}
		};
	}

	private Set<Resource> getResources( AbstractType<?, ?, ?> type, CrawlPolicy crawlPolicy ) {
		IsInstanceCrawler<ResourceExpression> crawler =
						new IsInstanceCrawler<ResourceExpression>( ResourceExpression.class ) {
			@Override protected boolean isAcceptable( ResourceExpression resourceExpression1 ) {
				return true;
			}
		};
		type.crawl( crawler, crawlPolicy );
		Set<Resource> resources = new HashSet<>();
		for( ResourceExpression resourceExpression : crawler.getList() ) {
			resources.add( resourceExpression.resource.getValue() );
		}
		return resources;
	}

	private void writeDataSources( OutputStream os, List<DataSource> dataSources ) throws IOException {
		ZipOutputStream zos = new ZipOutputStream( os );
		for( DataSource dataSource : dataSources ) {
			ZipUtilities.write( zos, dataSource );
		}
		zos.flush();
		zos.close();
	}

	private static void addResources( Manifest manifest, List<DataSource> dataSources, Set<Resource> resources ) {
		Set<String> usedEntryNames = new HashSet<>();
		for( Resource resource : resources ) {
			// TODO add entries to manifest avoiding duplicating names
			String entryName = generateEntryName( resource, usedEntryNames );
			usedEntryNames.add( entryName );
			addResourceReference( manifest, resource );
			// TODO Expand to cover arbitrary data files
			dataSources.add( new ByteArrayDataSource( entryName, resource.getData() ) );
		}
	}

	private static void addResourceReference( Manifest manifest, Resource resource ) {
		if (resource instanceof AudioResource) {
			manifest.resources.add(  new AudioReference( (AudioResource) resource ) );
		}
		if (resource instanceof ImageResource) {
			manifest.resources.add(  new ImageReference( (ImageResource) resource) );
		}
	}

	// TODO Reconsider and rework since this is cloned from XmlProjectIo
	private static String generateEntryName( Resource resource, Set<String> usedEntryNames ) {
		String validFilename = getValidName( resource.getOriginalFileName() );
		final String DESIRED_DIRECTORY_NAME = "resources";
		int i = 1;
		while( true ) {
			StringBuilder sb = new StringBuilder();
			sb.append( DESIRED_DIRECTORY_NAME );
			if( i > 1 ) {
				sb.append( i );
			}
			sb.append( "/" );
			sb.append( validFilename );
			String potentialEntryName = sb.toString();
			if( usedEntryNames.contains( potentialEntryName ) ) {
				i += 1;
			} else {
				return potentialEntryName;
			}
		}
	}

	private static String getValidName( String name ) {
		//todo
		return name;
	}
}

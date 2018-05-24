/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */

import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Sets;
import org.alice.ide.story.AliceIde;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.TreeUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class GenerateI18nSeed {

	private static void appendGalleryNames( StringBuilder sbNames, ResourceNode node ) throws IllegalAccessException, IOException {
		final String RESOURCE_SUFFIX = "Resource";
		ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)resourceKey;
			Class<?> cls = classResourceKey.getModelResourceCls();
			String clsFullName = cls.getName();
			if( clsFullName.endsWith( RESOURCE_SUFFIX ) ) {
				sbNames.append( clsFullName.substring( 0, clsFullName.length() - RESOURCE_SUFFIX.length() ) );
				sbNames.append( " = " );
				String clsSimpleName = cls.getSimpleName();
				sbNames.append( clsSimpleName.substring( 0, clsSimpleName.length() - RESOURCE_SUFFIX.length() ) );
				sbNames.append( "\n" );
			}
		}
		for( ResourceNode child : node.getNodeChildren() ) {
			appendGalleryNames( sbNames, child );
		}
	}

	private static void appendGalleryTags( Set<String> tags, ResourceNode node ) throws IllegalAccessException, IOException {
		ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey != null ) {
			for( String[] tagArray : new String[][] { resourceKey.getTags(), resourceKey.getGroupTags(), resourceKey.getThemeTags() } ) {
				if( tagArray != null ) {
					for( String tag : tagArray ) {
						//						if( tag.contains( "fishing" ) ) {
						//							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( tag );
						//						}
						if( tag.startsWith( "*" ) ) {
							tag = tag.substring( 1 );
						}
						for( String subTag : tag.split( ":" ) ) {
							tags.add( subTag );
						}
					}
				}
			}
		}
		for( ResourceNode child : node.getNodeChildren() ) {
			appendGalleryTags( tags, child );
		}
	}

	public static void main( String[] args ) throws Exception {
		Object usedOnlyForSideEffect = new AliceIde( null );
		StringBuilder sbNames = new StringBuilder();
		ResourceNode rootGalleryNode = TreeUtilities.getTreeBasedOnClassHierarchy();
		appendGalleryNames( sbNames, rootGalleryNode );
		Set<String> tags = Sets.newHashSet();
		appendGalleryTags( tags, rootGalleryNode );
		List<String> list = Lists.newArrayList( tags );
		Collections.sort( list );
		StringBuilder sbTags = new StringBuilder();
		for( String tag : list ) {
			sbTags.append( tag.replaceAll( " ", "_" ) );
			sbTags.append( " = " );
			sbTags.append( tag );
			sbTags.append( "\n" );
		}

		File repoRoot = new File( System.getProperty( "user.dir" ), "../../../" );
		File srcRoot = new File( repoRoot, "core/i18n/src/main/resources" );//"core/ide/src/main/java/" );
		File packageDirectory = new File( srcRoot, "org/lgna/story/resources" );
		File namesFile = new File( packageDirectory, "GalleryNames.properties" );
		TextFileUtilities.write( namesFile, sbNames.toString() );

		File tagsFile = new File( packageDirectory, "GalleryTags.properties" );
		TextFileUtilities.write( tagsFile, sbTags.toString() );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( file, file.exists() );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sbNames );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sbTags );
	}
}

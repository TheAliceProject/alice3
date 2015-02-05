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

/**
 * @author Dennis Cosgrove
 */
public class GenerateI18nSeed {

	private static void appendGalleryNames( StringBuilder sbNames, org.alice.stageide.modelresource.ResourceNode node ) throws IllegalAccessException, java.io.IOException {
		final String RESOURCE_SUFFIX = "Resource";
		org.alice.stageide.modelresource.ResourceKey resourceKey = node.getResourceKey();
		if( resourceKey instanceof org.alice.stageide.modelresource.ClassResourceKey ) {
			org.alice.stageide.modelresource.ClassResourceKey classResourceKey = (org.alice.stageide.modelresource.ClassResourceKey)resourceKey;
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
		for( org.alice.stageide.modelresource.ResourceNode child : node.getNodeChildren() ) {
			appendGalleryNames( sbNames, child );
		}
	}

	private static void appendGalleryTags( java.util.Set<String> tags, org.alice.stageide.modelresource.ResourceNode node ) throws IllegalAccessException, java.io.IOException {
		org.alice.stageide.modelresource.ResourceKey resourceKey = node.getResourceKey();
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
		for( org.alice.stageide.modelresource.ResourceNode child : node.getNodeChildren() ) {
			appendGalleryTags( tags, child );
		}
	}

	public static void main( String[] args ) throws Exception {
		Object usedOnlyForSideEffect = new org.alice.ide.story.AliceIde( null );
		StringBuilder sbNames = new StringBuilder();
		org.alice.stageide.modelresource.ResourceNode rootGalleryNode = org.alice.stageide.modelresource.TreeUtilities.getTreeBasedOnClassHierarchy();
		appendGalleryNames( sbNames, rootGalleryNode );
		java.util.Set<String> tags = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		appendGalleryTags( tags, rootGalleryNode );
		java.util.List<String> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( tags );
		java.util.Collections.sort( list );
		StringBuilder sbTags = new StringBuilder();
		for( String tag : list ) {
			sbTags.append( tag.replaceAll( " ", "_" ) );
			sbTags.append( " = " );
			sbTags.append( tag );
			sbTags.append( "\n" );
		}

		java.io.File repoRoot = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "/gits/alice/" );
		java.io.File srcRoot = new java.io.File( repoRoot, "core/i18n/src/main/resources" );//"core/ide/src/main/java/" );
		java.io.File packageDirectory = new java.io.File( srcRoot, "org/lgna/story/resources" );
		java.io.File namesFile = new java.io.File( packageDirectory, "GalleryNames.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( namesFile, sbNames.toString() );

		java.io.File tagsFile = new java.io.File( packageDirectory, "GalleryTags.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( tagsFile, sbTags.toString() );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( file, file.exists() );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sbNames );
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sbTags );
	}
}

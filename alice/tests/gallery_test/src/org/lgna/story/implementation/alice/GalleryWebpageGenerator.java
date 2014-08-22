package org.lgna.story.implementation.alice;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Icon;

import org.alice.stageide.modelresource.ClassHierarchyBasedResourceNode;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.GroupBasedResourceNode;
import org.alice.stageide.modelresource.GroupTagKey;
import org.alice.stageide.modelresource.IdeAliceResourceUtilities;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.ThemeBasedResourceNode;
import org.alice.stageide.modelresource.ThemeTagKey;
import org.alice.stageide.modelresource.TreeUtilities;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

/*
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
 * @author dculyba
 * 
 */
public class GalleryWebpageGenerator {

	private static final String HTML_HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
			"<style type=\"text/css\" media=\"screen\">\n" +
			"\ttable { border: 1px solid black; border-collapse:collapse;}\n" +
			"\ttd { border: 1px solid black; text-align:center; padding:4px;}\n" +
			"\t.noBorder { border: 0px; }\n" +
			"\t.wrapTable { border: 1px solid black;float:left;}\n" +
			"\t.newLine { clear: both;  display: block; position: relative; }\n" +
			"</style>\n" +
			"<head>\n" +
			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n";

	private static final String HTML_FOOTER = "</body>\n</html>\n";
	private static final String TABLE_BORDER = "border=\"1\"";

	private static String getClassName( ResourceNode node ) {
		if( hasClassData( node ) ) {
			return IdeAliceResourceUtilities.getModelClassName( node.getResourceKey() );
		}
		return null;
	}

	private static String getHTMLName( ResourceNode node ) {
		String name = getDisplayNameForNode( node );
		if( name == null ) {
			if( node instanceof ClassHierarchyBasedResourceNode ) {
				return "Classes";
			}
			else if( node instanceof ThemeBasedResourceNode ) {
				return "Themes";
			}
			else if( node instanceof GroupBasedResourceNode ) {
				return "Groups";
			}
		}
		return name;
	}

	private static boolean hasClassData( ResourceNode node ) {
		if( node == null ) {
			return false;
		}
		ResourceKey key = node.getResourceKey();
		return ( key instanceof ClassResourceKey ) ||
				( key instanceof EnumConstantResourceKey );
	}

	private static boolean hasImageData( ResourceNode node ) {
		if( node == null ) {
			return false;
		}
		ResourceKey key = node.getResourceKey();
		return ( key instanceof ClassResourceKey ) ||
				( key instanceof EnumConstantResourceKey ) ||
				( key instanceof GroupTagKey ) ||
				( key instanceof ThemeTagKey );
	}

	private static String getRelativePath( String absPath, String absPathOther ) {
		if( absPathOther == null ) {
			return absPath;
		}
		String[] pathSplit = absPath.split( "/" );
		String[] relativeToSplit = absPathOther.split( "/" );

		int startIndex = 0;
		while( ( startIndex < pathSplit.length ) && ( startIndex < relativeToSplit.length ) ) {
			if( pathSplit[ startIndex ].equals( relativeToSplit[ startIndex ] ) ) {
				startIndex++;
			}
			else {
				break;
			}
		}
		StringBuilder relativePath = new StringBuilder();
		for( int i = startIndex; i < ( relativeToSplit.length - 1 ); i++ ) {
			relativePath.append( "../" );
		}
		for( int i = startIndex; i < pathSplit.length; i++ ) {
			relativePath.append( pathSplit[ i ] );
			if( i < ( pathSplit.length - 1 ) ) {
				relativePath.append( "/" );
			}
		}
		return relativePath.toString();
	}

	private static String getRelativePagePath( ResourceNode node, ResourceNode relativeTo ) {
		String absPath = getAbsoluteURLForClassPage( node );
		String absPathOther = getAbsoluteURLForClassPage( relativeTo );
		return getRelativePath( absPath, absPathOther );
	}

	private static String getRelativeThumbnailPath( ResourceNode node, ResourceNode relativeTo ) {
		String absPath = getAbsoluteURLForThumbnail( node );
		String absPathOther = getAbsoluteURLForClassPage( relativeTo );
		return getRelativePath( absPath, absPathOther );
	}

	private static String getResourcePath( ResourceNode node ) {
		StringBuilder sb = new StringBuilder();
		ResourceNode parent = node.getParent();
		while( parent != null ) {
			String className = getFilenameForNode( parent );
			if( className != null ) {
				sb.insert( 0, className + "/" );
			}
			parent = parent.getParent();
		}
		return sb.toString();
	}

	private static String getFilenameForNode( ResourceNode node ) {
		ResourceKey key = node.getResourceKey();
		String name = null;
		if( key instanceof ClassResourceKey ) {
			name = getClassName( node );
		}
		else if( key instanceof EnumConstantResourceKey ) {
			name = getClassName( node ) + ( (EnumConstantResourceKey)key ).getEnumConstant().toString();
		}
		else if( key instanceof ThemeTagKey ) {
			name = "theme_" + key.getDisplayText();
		}
		else if( key instanceof GroupTagKey ) {
			name = "group_" + key.getDisplayText();
		}
		return name;
	}

	private static String getDisplayNameForNode( ResourceNode node ) {
		ResourceKey key = node.getResourceKey();
		String name = null;
		if( key instanceof ClassResourceKey ) {
			name = getClassName( node );
		}
		else if( key instanceof EnumConstantResourceKey ) {
			name = getClassName( node ) + ( (EnumConstantResourceKey)key ).getEnumConstant().toString();
		}
		else if( key instanceof ThemeTagKey ) {
			name = key.getDisplayText();
		}
		else if( key instanceof GroupTagKey ) {
			name = key.getDisplayText();
		}
		return name;
	}

	private static String getAbsoluteURLForClassPage( ResourceNode node ) {
		String name = getFilenameForNode( node );
		if( name != null ) {
			return getResourcePath( node ) + name + ".html";
		}
		return "index.html";
	}

	private static String getAbsoluteURLForThumbnail( ResourceNode node ) {
		String imageName = getFilenameForNode( node );
		if( imageName != null ) {
			return getResourcePath( node ) + imageName + ".png";
		}
		return null;
	}

	private static String getLinkForNode( ResourceNode node, ResourceNode relativeTo ) {
		return "<a href=\"" + getRelativePagePath( node, relativeTo ) + "\">" + getHTMLName( node ) + "</a>";
	}

	private static void saveThumbnailForNode( ResourceNode node, String basePath ) {
		if( hasImageData( node ) ) {
			java.awt.image.BufferedImage image = null;
			ResourceKey key = node.getResourceKey();
			if( ( key instanceof ThemeTagKey ) || ( key instanceof GroupTagKey ) ) {
				org.lgna.croquet.icon.IconFactory iconFactory = node.getIconFactory();
				Dimension d = iconFactory.getDefaultSizeForHeight( 120 );
				Icon icon = iconFactory.getIcon( d );
				image = new java.awt.image.BufferedImage( d.width, d.height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
				Graphics2D g2d = (Graphics2D)image.getGraphics();
				icon.paintIcon( null, g2d, 0, 0 );
				g2d.dispose();
			}
			else {
				image = IdeAliceResourceUtilities.getThumbnail( node.getResourceKey() );
				if( image == null ) {
					StringBuilder sb = new StringBuilder();
					sb.append( "images/" );
					sb.append( IdeAliceResourceUtilities.getClassFromKey( node.getResourceKey() ).getName().replace( ".", "/" ) );
					sb.append( ".png" );
					try {
						image = javax.imageio.ImageIO.read( org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel.class.getResource( sb.toString() ) );
					} catch( Exception e ) {
						e.printStackTrace();
						image = null;
					}
				}
			}
			if( image != null ) {
				String thumbPath = getAbsoluteURLForThumbnail( node );
				File thumbFile = new File( basePath + "/" + thumbPath );
				edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( thumbFile );
				//TODO: Do better error handling
				try {
					edu.cmu.cs.dennisc.image.ImageUtilities.write( thumbFile, image );
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}

	}

	private static String getImageCodeForNode( ResourceNode node, ResourceNode relativeTo ) {
		if( hasImageData( node ) ) {
			return "<img src=\"" + getRelativeThumbnailPath( node, relativeTo ) + "\" alt=\"" + getHTMLName( node ) + "\"/>";
		}
		return "";
	}

	private static String getImageLinkForNode( ResourceNode node, ResourceNode relativeTo ) {
		return "<a href=\"" + getRelativePagePath( node, relativeTo ) + "\">" + getImageCodeForNode( node, relativeTo ) + "</a>";
	}

	private static String getNavBar( ResourceNode node ) {
		ResourceNode parent = node.getParent();
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		while( parent != null ) {
			String link = getLinkForNode( parent, node );
			if( isFirst ) {
				isFirst = false;
			}
			else {
				link += "/";
			}
			sb.insert( 0, link );
			parent = parent.getParent();
		}
		return sb.toString();
	}

	private static String getSubclassTableLink( ResourceNode node, ResourceNode relativeTo ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<table class=\"wrapTable\">\n" );
		sb.append( "\t<tr>\n" );
		sb.append( "\t\t<td align=\"center\" class=\"noBorder\" width=160 height=120>" + getImageLinkForNode( node, relativeTo ) + "</td>\n" );
		sb.append( "\t</tr>\n" );
		sb.append( "\t<tr>\n" );
		sb.append( "\t\t<td align=\"center\" class=\"noBorder\"><b>" + getLinkForNode( node, relativeTo ) + "</b></td>\n" );
		sb.append( "\t</tr>\n" );
		sb.append( "</table>\n" );
		return sb.toString();
	}

	private static String getMethodHTML( org.lgna.project.ast.AbstractMethod method ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<i>" + method.getReturnType().getName() + "</i> <b>" + method.getName() + "</b>( " );
		boolean isFirst = true;
		for( org.lgna.project.ast.AbstractParameter p : method.getRequiredParameters() ) {
			if( !isFirst ) {
				sb.append( ", " );
			}
			sb.append( "<i>" + p.getValueType().getName() + "</i>" );
			isFirst = false;
		}
		sb.append( " )" );
		return sb.toString();
	}

	private static String getCodeHTML( ResourceNode node ) {
		StringBuilder sb = new StringBuilder();
		if( hasClassData( node ) ) {
			org.lgna.project.ast.NamedUserType nut = null;
			ResourceNode parent = node.getParent();
			ResourceKey parentKey = parent.getResourceKey();
			if( hasClassData( parent ) && ( parentKey instanceof ClassResourceKey ) ) {
				if( hasLeaves( node ) ) {
					ClassResourceKey classKey = (ClassResourceKey)parentKey;
					ResourceNode child = childAt( node, 0 );
					EnumConstantResourceKey enumKey = (EnumConstantResourceKey)child.getResourceKey();
					JavaType ancestorType = enumKey.getField().getDeclaringType();
					JavaField argumentField = enumKey.getField();
					nut = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( ancestorType, argumentField );
				}
			}
			else {
				nut = null; //org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromSuperType(node.getUserType().getFirstEncounteredJavaType());
			}
			if( nut != null ) {
				sb.append( "<blockquote>\n" );
				if( nut.superType.getValue() instanceof org.lgna.project.ast.JavaType ) {
					for( org.lgna.project.ast.AbstractMethod m : nut.superType.getValue().getDeclaredMethods() ) {
						sb.append( "<p>" + getMethodHTML( m ) + "</p>\n" );
					}
				}
				for( org.lgna.project.ast.AbstractMethod m : nut.getDeclaredMethods() ) {
					sb.append( "<p>" + getMethodHTML( m ) + "</p>\n" );
				}
				sb.append( "</blockquote>\n" );
			}
		}
		return sb.toString();
	}

	private static String getInfoHeaderRow() {
		StringBuilder sb = new StringBuilder();
		sb.append( "<tr>\n" );
		sb.append( "\t<td><strong>Thumbnail</strong></td>" );
		sb.append( "<td><strong>Name</strong></td>" );
		sb.append( "<td><strong>Tags</strong></td>" );
		sb.append( "<td><strong>Bounding Box<strong></td>" );
		sb.append( "<td><strong>Creator</strong></td>" );
		sb.append( "<td><strong>Creation Year</strong></td>\n" );
		sb.append( "</tr>\n" );
		return sb.toString();
	}

	private static String getBBoxString( edu.cmu.cs.dennisc.math.AxisAlignedBox bbox ) {
		if( bbox == null ) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		java.util.Formatter formatter = new java.util.Formatter( sb, java.util.Locale.US );
		formatter.format( "min[%f.2, %f.2, %f.2]<br>max[%f.2, %f.2, %f.2]", bbox.getMinimum().x, bbox.getMinimum().y, bbox.getMinimum().z, bbox.getMaximum().x, bbox.getMaximum().y, bbox.getMaximum().z );
		return sb.toString();
	}

	private static String createInfoRow( ResourceNode node, ResourceNode relativeTo ) {
		Class<?> modelResource = IdeAliceResourceUtilities.getClassFromKey( node.getResourceKey() );
		String resourceName = IdeAliceResourceUtilities.getEnumNameFromKey( node.getResourceKey() );
		String[] tags = IdeAliceResourceUtilities.getTags( node.getResourceKey(), null );
		String tagString = "";
		for( String s : tags ) {
			if( tagString.length() > 0 ) {
				tagString += ", ";
			}
			tagString += s;
		}
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = IdeAliceResourceUtilities.getBoundingBox( node.getResourceKey() );
		String bboxString = getBBoxString( bbox );
		String creator = org.lgna.story.implementation.alice.AliceResourceUtilties.getCreator( modelResource, resourceName );
		if( creator == null ) {
			creator = "";
		}
		int creationYear = org.lgna.story.implementation.alice.AliceResourceUtilties.getCreationYear( modelResource, resourceName );
		StringBuilder sb = new StringBuilder();
		sb.append( "<tr>\n" );
		sb.append( "\t<td>" + getImageCodeForNode( node, relativeTo ) + "</td>\n" );
		sb.append( "\t<td><strong>" + node.getResourceKey().getDisplayText() + "</strong></td>\n" );
		sb.append( "\t<td>" + tagString + "</td>\n" );
		sb.append( "\t<td>" + bboxString + "</td>\n" );
		sb.append( "\t<td>" + creator + "</td>\n" );
		sb.append( "\t<td>" + creationYear + "</td>\n" );
		sb.append( "</tr>\n" );
		return sb.toString();
	}

	private static int childCount( ResourceNode node ) {
		java.util.List<ResourceNode> children = node.getNodeChildren();
		if( children != null ) {
			return children.size();
		}
		return 0;
	}

	private static boolean isLeaf( ResourceNode node ) {
		java.util.List<ResourceNode> children = node.getNodeChildren();
		return ( children == null ) || children.isEmpty();
	}

	private static boolean hasLeaves( ResourceNode node ) {
		for( ResourceNode child : node.getNodeChildren() ) {
			if( child.getResourceKey() instanceof EnumConstantResourceKey )
			{
				return true;
			}
		}
		return false;
	}

	private static ResourceNode childAt( ResourceNode node, int index ) {
		java.util.List<ResourceNode> children = node.getNodeChildren();
		if( children != null ) {
			return children.get( index );
		}
		return null;
	}

	private static List<String> getClassList( ResourceNode node ) {
		ArrayList<String> classList = new ArrayList<String>();
		if( node.getResourceKey() instanceof ThemeTagKey )
		{
			classList.add( "THEME: " + node.getResourceKey().getDisplayText() );
		}
		if( node.getResourceKey() instanceof ClassResourceKey )
		{
			classList.add( getClassName( node ) );
		}
		for( ResourceNode childNode : node.getNodeChildren() ) {
			if( !isLeaf( childNode ) ) {
				classList.addAll( getClassList( childNode ) );
			}
		}
		return classList;
	}

	private static String createClassWebpageBodyHTML( ResourceNode node ) {
		StringBuilder html = new StringBuilder();

		String className = getHTMLName( node );
		html.append( "<h1>" + getNavBar( node ) + "/" + className + "</h1>\n" );

		for( ResourceNode childNode : node.getNodeChildren() ) {
			if( !isLeaf( childNode ) ) {
				html.append( getSubclassTableLink( childNode, node ) );
			}
		}
		if( hasLeaves( node ) ) {
			html.append( "<table>\n" );
			html.append( getInfoHeaderRow() );
			html.append( createInfoRow( node, node ) );
			for( ResourceNode childNode : node.getNodeChildren() ) {
				html.append( createInfoRow( childNode, node ) );
			}
			html.append( "</table>\n" );
		}
		html.append( "<div class=\"newLine\"/>\n" );
		//		if (hasClassData(node)) {
		//			html.append("<br/>\n");
		//			html.append("<hr width=\"100%\">\n");
		//			html.append("<h3>Methods</h3>\n");
		//			html.append(getCodeHTML(node));
		//		}
		//		
		Class<?> resourceClass = IdeAliceResourceUtilities.getClassFromKey( node.getResourceKey() );

		if( ( resourceClass != null ) && !isLeaf( node ) ) {
			String javaCode = org.lgna.story.implementation.alice.AliceResourceUtilties.getJavaCode( resourceClass );
			if( javaCode != null ) {
				html.append( "<br/>\n" );
				html.append( "<hr width=\"100%\">\n" );
				html.append( "<h3>Resource File</h3>\n" );
				html.append( "<pre>\n" );
				html.append( javaCode );
				html.append( "\n</pre>\n" );
			}
		}

		return html.toString();
	}

	private static File createClassWebpage( ResourceNode node, String basePath ) {
		String className = getHTMLName( node );

		StringBuilder html = new StringBuilder();
		html.append( HTML_HEADER );
		html.append( "<title>" + className + "</title>\n</head>\n" );
		html.append( "<body>\n" );

		html.append( createClassWebpageBodyHTML( node ) );

		html.append( HTML_FOOTER );

		String relativeURL = getAbsoluteURLForClassPage( node );
		File htmlFile = new File( basePath + "/" + relativeURL );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( htmlFile );

		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( htmlFile, html.toString() );

		return htmlFile;
	}

	private static void createHTMLTree( ResourceNode currentNode, String rootPath ) {
		saveThumbnailForNode( currentNode, rootPath );
		for( ResourceNode childNode : currentNode.getNodeChildren() ) {
			if( isLeaf( childNode ) ) {
				saveThumbnailForNode( childNode, rootPath );
			}
		}
		createClassWebpage( currentNode, rootPath );
		for( ResourceNode childNode : currentNode.getNodeChildren() ) {
			if( !isLeaf( childNode ) ) {
				createHTMLTree( childNode, rootPath );
			}
		}
	}

	public static int getClassCount( ResourceNode currentNode ) {
		int classCount = 0;
		ResourceKey key = currentNode.getResourceKey();
		if( key instanceof ClassResourceKey ) {
			classCount = 1;
		}
		for( ResourceNode childNode : currentNode.getNodeChildren() ) {
			classCount += getClassCount( childNode );
		}
		return classCount;
	}

	public static int getEnumCount( ResourceNode currentNode ) {
		int enumCount = 0;
		ResourceKey key = currentNode.getResourceKey();
		if( key instanceof EnumConstantResourceKey ) {
			enumCount = 1;
		}
		for( ResourceNode childNode : currentNode.getNodeChildren() ) {
			enumCount += getEnumCount( childNode );
		}
		return enumCount;
	}

	public static void printGalleryStats( ResourceNode currentNode ) {
		int classCount = getClassCount( currentNode );
		int enumCount = getEnumCount( currentNode );

		System.out.println( "Class count: " + classCount );
		System.out.println( "Enum count: " + enumCount );
	}

	public static void standAlonePrintGalleryStats() {
		org.alice.stageide.StageIDE usedOnlyForSideEffect = new org.alice.ide.story.AliceIde( null );
		ResourceNode classBasedNode = TreeUtilities.getTreeBasedOnClassHierarchy();
		printGalleryStats( classBasedNode );
		usedOnlyForSideEffect = null;
		System.gc();
	}

	public static void buildGalleryWebpage( String webpageDir ) {
		org.alice.stageide.StageIDE usedOnlyForSideEffect = new org.alice.ide.story.AliceIde( null );
		//		org.alice.ide.ProjectApplication.getActiveInstance().loadProjectFrom( new org.alice.ide.uricontent.BlankSlateProjectLoader( org.alice.stageide.openprojectpane.models.TemplateUriState.Template.GRASS ) );

		StringBuilder indexPageContent = new StringBuilder();
		ResourceNode classBasedNode = TreeUtilities.getTreeBasedOnClassHierarchy();
		ResourceNode themeBasedNode = TreeUtilities.getTreeBasedOnTheme();
		ResourceNode groupBasedNode = TreeUtilities.getTreeBasedOnGroup();
		createHTMLTree( classBasedNode, webpageDir );
		createHTMLTree( themeBasedNode, webpageDir );
		createHTMLTree( groupBasedNode, webpageDir );

		indexPageContent.append( createClassWebpageBodyHTML( classBasedNode ) );
		indexPageContent.append( "<br/>\n" );
		indexPageContent.append( createClassWebpageBodyHTML( themeBasedNode ) );
		indexPageContent.append( "<br/>\n" );
		indexPageContent.append( createClassWebpageBodyHTML( groupBasedNode ) );

		StringBuilder html = new StringBuilder();
		html.append( HTML_HEADER );
		html.append( "<title>Alice 3.1 Gallery</title>\n</head>\n" );
		html.append( "<body>\n" );
		html.append( indexPageContent );
		html.append( HTML_FOOTER );

		String relativeURL = "index.html";
		File htmlFile = new File( webpageDir + "/" + relativeURL );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( htmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( htmlFile, html.toString() );

		System.out.println( "\nDONE" );
		System.out.println( "DONE" );
		System.out.println( "DONE!!!\n\n" );

		List<String> themeNames = getClassList( themeBasedNode );
		List<String> classNames = getClassList( classBasedNode );
		List<String> missingNames = new ArrayList<String>();
		for( String name : classNames )
		{
			if( !themeNames.contains( name ) )
			{
				missingNames.add( name );
			}
		}
		for( String name : themeNames )
		{
			System.out.println( name );
		}
		System.out.println( "THEME: themeless" );
		for( String name : missingNames )
		{
			System.out.println( name );
		}

		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for( String name : themeNames )
		{
			int count = 1;
			if( countMap.containsKey( name ) )
			{
				count += countMap.get( name );
			}
			countMap.put( name, count );
		}
		System.out.println( "\n\nDuplicates:" );
		for( Entry<String, Integer> entry : countMap.entrySet() )
		{
			if( entry.getValue() > 1 )
			{
				System.out.println( entry.getKey() + " : " + entry.getValue() );
			}
		}

		printGalleryStats( classBasedNode );

		usedOnlyForSideEffect = null;
		System.gc();

	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.java.util.logging.Logger.setLevel( java.util.logging.Level.INFO );

		if( SystemUtilities.isPropertyTrue( "org.alice.batch.galleryStatsOnly" ) )
		{
			standAlonePrintGalleryStats();
		}
		else {
			String webpageDir = "C:/batchOutput/webpage";

			String webpageDirProp = System.getProperty( "org.alice.batch.webpageDir" );
			if( webpageDirProp != null ) {
				webpageDir = webpageDirProp;
			}

			FileUtilities.delete( webpageDir );
			GalleryWebpageGenerator.buildGalleryWebpage( webpageDir );
		}

	}

}

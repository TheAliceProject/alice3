package org.lgna.story.resourceutilities;
import java.io.File;

import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.ResourceKey;

import edu.cmu.cs.dennisc.java.io.FileUtilities;

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

	private static final String HTML_HEADER =	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"+
												"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"+
												"<style type=\"text/css\" media=\"screen\">\n"+
												"\ttable { border: 1px solid black; border-collapse:collapse;}\n"+
												"\ttd { border: 1px solid black; text-align:center; padding:4px;}\n"+
												"\t.noBorder { border: 0px; }\n"+
												"\t.wrapTable { border: 1px solid black;float:left;}\n"+
												"\t.newLine { clear: both;  display: block; position: relative; }\n"+
												"</style>\n"+
												"<head>\n"+
												"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n";
	
	private static final String HTML_FOOTER =	"</body>\n</html>\n";
	private static final String TABLE_BORDER = 	"border=\"1\"";
	
	private static String getClassName(ModelResourceTreeNode node) {
		if (node.getUserType() != null) {
			return node.getUserType().getName();
		}
		return null;
	}
	
	private static String getHTMLFileName(ModelResourceTreeNode node) {
		if (node.getUserType() != null) {
			return node.getUserType().getName();
		}
		return null;
	}
	
	private static String getHTMLName(ModelResourceTreeNode node) {
		if (node.getUserType() == null) {
			return "Main";
		}
		else {
			return getClassName(node);
		}
	}
	
	private static String getRelativePath(String absPath, String absPathOther) {
		if (absPathOther == null) {
			return absPath;
		}
		String[] pathSplit = absPath.split("/");
		String[] relativeToSplit = absPathOther.split("/");
		
		int startIndex = 0;
		while (startIndex < pathSplit.length && startIndex < relativeToSplit.length) {
			if (pathSplit[startIndex].equals(relativeToSplit[startIndex])) {
				startIndex++;
			}
			else {
				break;
			}
		}
		StringBuilder relativePath = new StringBuilder();
		for (int i=startIndex; i<relativeToSplit.length-1; i++) {
			relativePath.append("../");
		}
		for (int i=startIndex; i<pathSplit.length; i++) {
			relativePath.append(pathSplit[i]);
			if (i < pathSplit.length-1) {
				relativePath.append("/");
			}
		}
		return relativePath.toString();
	}
	
	private static String getRelativePagePath(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		String absPath = getAbsoluteURLForClassPage(node);
		String absPathOther = getAbsoluteURLForClassPage(relativeTo);
		return getRelativePath(absPath, absPathOther);
	}
	
	private static String getRelativeThumbnailPath(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		String absPath = getAbsoluteURLForThumbnail(node);
		String absPathOther = getAbsoluteURLForClassPage(relativeTo);
		return getRelativePath(absPath, absPathOther);
	}
	
	private static String getResourcePath(ModelResourceTreeNode node) {
		StringBuilder sb =  new StringBuilder();
		ModelResourceTreeNode parent = (ModelResourceTreeNode)node.getParent();
		while (parent != null) {
			String className = getClassName(parent);
			if (className != null) {
				sb.insert(0, className+"/");
			}
			parent = (ModelResourceTreeNode)parent.getParent();
		}
		return sb.toString();
	}
	
	private static String getAbsoluteURLForClassPage(ModelResourceTreeNode node) {
		String className = getClassName(node);
		if (className != null) {
			return getResourcePath(node) + className+".html";
		}
		return "index.html";
	}
	
	private static String getAbsoluteURLForThumbnail(ModelResourceTreeNode node) {
		String className = getClassName(node);
		if (className != null) {
			return getResourcePath(node) + className+".png";
		}
		return null;
	}
	
	private static String getLinkForNode(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		return "<a href=\""+getRelativePagePath(node, relativeTo)+"\">"+getHTMLName(node)+"</a>";
	}
	
	private static void saveThumbnailForNode(ModelResourceTreeNode node, String basePath) {
		if (node.getUserType() != null) {
			java.awt.image.BufferedImage image = null;
			if (node.isLeaf()) {
				image = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(node.getResourceClass(), node.getJavaField().getName());
			}
			else if (node.getChildAt(0).isLeaf()){
				image = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(node.getResourceClass());
			}
			else {
				StringBuilder sb = new StringBuilder();
				sb.append( "images/" );
				sb.append( node.getResourceClass().getName().replace( ".", "/" ) );
				sb.append( ".png" );
				try {
					image = javax.imageio.ImageIO.read(org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel.class.getResource( sb.toString() ));
				}
				catch (Exception e) {
					e.printStackTrace();
					image = null;
				}
			}
			if (image != null) {
				String thumbPath = getAbsoluteURLForThumbnail(node);
				File thumbFile = new File(basePath + "/"+thumbPath);
				edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary(thumbFile);
				edu.cmu.cs.dennisc.image.ImageUtilities.write(thumbFile, image);
			}
		}
		
	}
	
	private static String getImageCodeForNode(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		if (node.getUserType() != null) {
			return "<img src=\""+getRelativeThumbnailPath(node, relativeTo)+"\" alt=\""+getHTMLName(node)+"\"/>";
		}
		return "";
	}
	
	private static String getImageLinkForNode(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		return "<a href=\""+getRelativePagePath(node, relativeTo)+"\">"+getImageCodeForNode(node, relativeTo)+"</a>";
	}
	
	private static String getNavBar(ModelResourceTreeNode node) {
		ModelResourceTreeNode parent = (ModelResourceTreeNode)node.getParent();
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		while (parent != null) {
			String link = getLinkForNode(parent, node);
			if (isFirst) {
				isFirst = false;
			}
			else {
				link += "/";
			}
			sb.insert(0, link);
			parent = (ModelResourceTreeNode)parent.getParent();
		}
		return sb.toString();
	}
	
	private static String getSubclassTableLink(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"wrapTable\">\n");
		sb.append("\t<tr>\n");
		sb.append("\t\t<td align=\"center\" class=\"noBorder\">"+getImageLinkForNode(node, relativeTo)+"</td>\n");
		sb.append("\t</tr>\n");
		sb.append("\t<tr>\n");
		sb.append("\t\t<td align=\"center\" class=\"noBorder\"><b>"+getLinkForNode(node, relativeTo)+"</b></td>\n");
		sb.append("\t</tr>\n");
		sb.append("</table>\n");
		return sb.toString();
	}
	
	private static String getMethodHTML(org.lgna.project.ast.AbstractMethod method) {
		StringBuilder sb = new StringBuilder();
		sb.append("<i>"+method.getReturnType().getName()+"</i> <b>"+method.getName()+"</b>( ");
		boolean isFirst = true;
		for (org.lgna.project.ast.AbstractParameter p : method.getRequiredParameters()) {
			if (!isFirst) {
				sb.append(", ");
			}
			sb.append("<i>"+p.getValueType().getName()+"</i>");
			isFirst = false;
		}
		sb.append(" )");
		return sb.toString();
	}
	
	private static String getCodeHTML(ModelResourceTreeNode node) {
		StringBuilder sb = new StringBuilder();
		if (node.getUserType() != null) {
			org.lgna.project.ast.NamedUserType nut = null;
			ModelResourceTreeNode parent = (ModelResourceTreeNode)node.getParent();
			if (parent.getUserType() != null) {
				if (node.getChildCount() > 0 && node.getChildAt(0).isLeaf()) {
					ModelResourceTreeNode child = (ModelResourceTreeNode)node.getChildAt(0);
					nut = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField(parent.getUserType().getFirstEncounteredJavaType(), child.getJavaField());
				}
			}
			else {
				nut = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromSuperType(node.getUserType().getFirstEncounteredJavaType());
			}
			if (nut != null) {
				sb.append("<blockquote>\n");
				if (nut.superType.getValue() instanceof org.lgna.project.ast.JavaType) {
					for (org.lgna.project.ast.AbstractMethod m : nut.superType.getValue().getDeclaredMethods()) {
						sb.append("<p>"+getMethodHTML(m)+"</p>\n");
					}
				}
				for (org.lgna.project.ast.AbstractMethod m : nut.getDeclaredMethods()) {
					sb.append("<p>"+getMethodHTML(m)+"</p>\n");
				}
				sb.append("</blockquote>\n");
			}
		}
		return sb.toString();
	}
	
	private static String getInfoHeaderRow() {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>\n");
		sb.append("\t<td><strong>Thumbnail</strong></td>");
		sb.append("<td><strong>Name</strong></td>");
		sb.append("<td><strong>Tags</strong></td>");
		sb.append("<td><strong>Bounding Box<strong></td>");
		sb.append("<td><strong>Creator</strong></td>");
		sb.append("<td><strong>Creation Year</strong></td>\n");
		sb.append("</tr>\n");
		return sb.toString();
	}
	
	private static String getBBoxString(edu.cmu.cs.dennisc.math.AxisAlignedBox bbox) {
		if (bbox == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		java.util.Formatter formatter = new java.util.Formatter(sb, java.util.Locale.US);
		 formatter.format("min[%f.2, %f.2, %f.2]<br>max[%f.2, %f.2, %f.2]", bbox.getMinimum().x, bbox.getMinimum().y, bbox.getMinimum().z, bbox.getMaximum().x, bbox.getMaximum().y, bbox.getMaximum().z );
		 return sb.toString();
	}
	
	private static String createInfoRow(ModelResourceTreeNode node, ModelResourceTreeNode relativeTo) {
		Class<?> modelResource = node.getResourceClass();
		String resourceName = node.getJavaField() != null ? node.getJavaField().getName() : null;
		String name = getHTMLName(node);
		String[] tags = org.lgna.story.implementation.alice.AliceResourceUtilties.getTags(modelResource, resourceName, null);
		String tagString = "";
		for (String s : tags) {
			if (tagString.length() > 0) {
				tagString += ", ";
			}
			tagString += s;
		}
		ResourceKey key;
		
		if( node.getJavaField() != null ) {
			try {
				key = new EnumConstantResourceKey( (Enum<? extends org.lgna.story.resources.ModelResource>)node.getJavaField().getFieldReflectionProxy().getReification().get( null ) );
			} catch( IllegalAccessException iae ) {
				throw new RuntimeException( iae );
			}
		} else {
			key = new ClassResourceKey( (Class<? extends org.lgna.story.resources.ModelResource>)modelResource );
		}
		
		
		
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = org.lgna.story.implementation.alice.AliceResourceUtilties.getBoundingBox(key);
		String bboxString = getBBoxString(bbox);
		String creator = org.lgna.story.implementation.alice.AliceResourceUtilties.getCreator(modelResource, resourceName);
		if (creator == null) { creator = ""; }
		int creationYear = org.lgna.story.implementation.alice.AliceResourceUtilties.getCreationYear(modelResource, resourceName);
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>\n");
		sb.append("\t<td>"+getImageCodeForNode(node, relativeTo)+"</td>\n");
		sb.append("\t<td><strong>"+name+"</strong></td>\n");
		sb.append("\t<td>"+tagString+"</td>\n");
		sb.append("\t<td>"+bboxString+"</td>\n");
		sb.append("\t<td>"+creator+"</td>\n");
		sb.append("\t<td>"+creationYear+"</td>\n");
		sb.append("</tr>\n");
		return sb.toString();
	}
	
	private static File createClassWebpage(ModelResourceTreeNode node, String basePath) {
		String className = getHTMLName(node);
		
		StringBuilder html = new StringBuilder();
		html.append(HTML_HEADER);
		html.append("<title>"+className+"</title>\n</head>\n");
		html.append("<body>\n");
		
		html.append("<h1>"+getNavBar(node)+"/"+className+"</h1>\n");
		
		for (ModelResourceTreeNode childNode : node.childrenList()) {
			if (!childNode.isLeaf()) {
				html.append(getSubclassTableLink(childNode, node));
			}
		}
		if (node.getChildCount() > 0 && node.getChildAt(0).isLeaf()) {
			html.append("<table>\n");
			html.append(getInfoHeaderRow());
			html.append(createInfoRow(node, node));
			for (ModelResourceTreeNode childNode : node.childrenList()) {
				html.append(createInfoRow(childNode, node));
			}
			html.append("</table>\n");
		}
		html.append("<div class=\"newLine\"/>\n");
		if (node.getUserType() != null) {
			html.append("<br/>\n");
			html.append("<hr width=\"100%\">\n");
			html.append("<h3>Methods</h3>\n");
			html.append(getCodeHTML(node));
		}
		
		Class<?> resourceClass = node.getResourceClass();
		
		
		
		if (resourceClass != null && !node.isLeaf()) {
			String javaCode = org.lgna.story.implementation.alice.AliceResourceUtilties.getJavaCode(resourceClass);
			if (javaCode != null) {
				html.append("<br/>\n");
				html.append("<hr width=\"100%\">\n");
				html.append("<h3>Resource File</h3>\n");
				html.append("<pre>\n");
				html.append(javaCode);
				html.append("\n</pre>\n");
			}
		}
		html.append(HTML_FOOTER);
		
		
		String relativeURL = getAbsoluteURLForClassPage(node);
		File htmlFile = new File(basePath + "/"+relativeURL);
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary(htmlFile);
		
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write(htmlFile, html.toString());
		
		return htmlFile;
	}
	
	private static void createHTMLTree(ModelResourceTreeNode currentNode, String rootPath) {
		saveThumbnailForNode(currentNode, rootPath);
		for (ModelResourceTreeNode childNode : currentNode.childrenList()) {
			if (childNode.isLeaf()) {
				saveThumbnailForNode(childNode, rootPath);
			}
		}
		createClassWebpage(currentNode, rootPath);
		for (ModelResourceTreeNode childNode : currentNode.childrenList()) {
			if (!childNode.isLeaf()) {
				createHTMLTree(childNode, rootPath);
			}
		}
	}
	
	public static void buildGalleryWebpage(String webpageDir) {
		org.alice.stageide.StageIDE usedOnlyForSideEffect = new org.alice.stageide.StageIDE();
		org.alice.ide.ProjectApplication.getActiveInstance().loadProjectFrom( new org.alice.ide.uricontent.BlankSlateProjectLoader( org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template.GRASS ) );
		ModelResourceTreeNode galleryTree = org.lgna.story.resourceutilities.StorytellingResources.getInstance().getGalleryTree();
		createHTMLTree(galleryTree, webpageDir);
	}
	
	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.java.util.logging.Logger.setLevel( java.util.logging.Level.INFO );
		String webpageDir = "C:/batchOutput/webpage";
		FileUtilities.delete(webpageDir);
		GalleryWebpageGenerator.buildGalleryWebpage("C:/batchOutput/webpage");
		
	}

}

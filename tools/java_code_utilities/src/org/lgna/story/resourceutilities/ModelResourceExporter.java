/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.resourceutilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.w3c.dom.Document;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

public class ModelResourceExporter {

	private static boolean REMOVE_ROOT_JOINTS = false;

	private static final String ROOT_IDS_FIELD_NAME = "JOINT_ID_ROOTS";

	private class NamedFile
	{
		public String name;
		public File file;

		public NamedFile( String name, File file )
		{
			this.name = name;
			this.file = file;
		}
	}

	private String resourceName;
	private String className;
	private List<String> tags = new LinkedList<String>();
	private List<String> groupTags = new LinkedList<String>();
	private List<String> themeTags = new LinkedList<String>();
	private Map<String, AxisAlignedBox> boundingBoxes = new HashMap<String, AxisAlignedBox>();
	private File xmlFile;
	private File javaFile;
	private List<String> jointIdsToSuppress = new ArrayList<String>();
	private Map<ModelSubResourceExporter, Image> thumbnails = new HashMap<ModelSubResourceExporter, Image>();
	private Map<String, File> existingThumbnails = null;
	private List<ModelSubResourceExporter> subResources = new LinkedList<ModelSubResourceExporter>();
	private boolean isSims = false;
	private boolean hasNewData = false;
	private boolean forceRebuildCode = false;
	private boolean forceRebuildXML = false;
	private Date lastEdited = null;
	private boolean shouldRecenter = false;
	private boolean recenterXZ = false;
	private boolean moveCenterToBottom = true;
	private List<String> forcedOverridingEnumNames = new ArrayList<String>();
	private Map<String, List<String>> forcedEnumNamesMap = new HashMap<String, List<String>>();
	private Map<String, String> customArrayNameMap = new HashMap<String, String>();
	private boolean exportGalleryResources = true;
	private boolean isDeprecated = false;
	private boolean placeOnGround = false;

	private String attributionName;
	private String attributionYear;

	private Class<?> jointAndVisualFactory = org.lgna.story.implementation.alice.JointImplementationAndVisualDataFactory.class;

	private ModelClassData classData;
	private List<Tuple2<String, String>> jointList;

	public ModelResourceExporter( String className )
	{
		this.className = className;
		if( Character.isLowerCase( this.className.charAt( 0 ) ) )
		{
			this.className = this.className.substring( 0, 1 ).toUpperCase() + this.className.substring( 1 );
		}
	}

	public ModelResourceExporter( String className, String resourceName )
	{
		this( className );
		this.resourceName = resourceName;
	}

	public ModelResourceExporter( String className, ModelClassData classData )
	{
		this( className );
		this.classData = classData;
	}

	public ModelResourceExporter( String className, String resourceName, ModelClassData classData )
	{
		this( className, resourceName );
		this.classData = classData;
	}

	public ModelResourceExporter( String className, ModelClassData classData, Class<?> jointAndVisualFactoryClass )
	{
		this( className, classData );
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}

	public ModelResourceExporter( String className, String resourceName, ModelClassData classData, Class<?> jointAndVisualFactoryClass )
	{
		this( className, resourceName, classData );
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}

	public void setResourceName( String resourceName )
	{
		this.resourceName = resourceName;
	}

	public void setHasNewData( boolean hasNewData ) {
		this.hasNewData = hasNewData;
	}

	public void setIsDeprecated( boolean isDeprecated ) {
		this.isDeprecated = isDeprecated;
	}

	public void setPlaceOnGround( boolean placeOnGround ) {
		this.placeOnGround = placeOnGround;
	}

	public void setShouldRecenter( boolean shouldRecenter ) {
		this.shouldRecenter = shouldRecenter;
		if( !this.shouldRecenter ) {
			this.moveCenterToBottom = false;
			this.recenterXZ = false;
		}
	}

	public boolean shouldRecenter() {
		return this.shouldRecenter;
	}

	public void setMoveCenterToBottom( boolean moveCenter ) {
		this.moveCenterToBottom = moveCenter;
	}

	public boolean shouldMoveCenterToBottom() {
		return this.moveCenterToBottom;
	}

	public void setRecenterXZ( boolean recenterXZ ) {
		this.recenterXZ = recenterXZ;
	}

	public boolean shouldRecenterXZ() {
		return this.recenterXZ;
	}

	public boolean hasNewData() {
		return this.hasNewData;
	}

	public void setLastEdited( Date lastEdited ) {
		this.lastEdited = lastEdited;
	}

	public void addLastEditedDate( Date date ) {
		if( date == null ) {
			return;
		}
		if( this.lastEdited == null ) {
			this.lastEdited = date;
		}
		else if( date.after( this.lastEdited ) ) {
			this.lastEdited = date;
		}
	}

	public void setExportGalleryResources( boolean exportResources ) {
		this.exportGalleryResources = exportResources;
	}

	public boolean getExportGalleryResources() {
		return this.exportGalleryResources;
	}

	public static boolean isMoreRecentThan( Date dataDate, File file ) {
		if( dataDate == null ) {
			return false;
		}
		Date fileDate = new Date( file.lastModified() );
		boolean isNewer = dataDate.after( fileDate );
		return isNewer;
	}

	public static boolean isMoreRecentThan( Date dataData, Date otherDate ) {
		if( dataData == null ) {
			return false;
		}
		return dataData.after( otherDate );
	}

	public String getResourceName()
	{
		return this.resourceName;
	}

	public ModelClassData getClassData()
	{
		return this.classData;
	}

	public void setForceRebuildCode( boolean rebuildCode ) {
		this.forceRebuildCode = rebuildCode;
	}

	public void setForceRebuildXML( boolean rebuildXML ) {
		this.forceRebuildXML = rebuildXML;
	}

	public boolean getForceRebuildCode() {
		return this.forceRebuildCode;
	}

	public boolean getForceRebuildXML() {
		return this.forceRebuildXML;
	}

	public void setJointAndVisualFactory( Class<?> jointAndVisualFactoryClass )
	{
		this.jointAndVisualFactory = jointAndVisualFactoryClass;
	}

	public boolean hasJointMap() {
		return this.jointList != null;
	}

	public List<Tuple2<String, String>> getJointMap() {
		return this.jointList;
	}

	private boolean hasParent( List<Tuple2<String, String>> listToCheck, String parent ) {
		if( ( parent == null ) || ( parent.length() == 0 ) ) {
			return true;
		}
		for( Tuple2<String, String> entry : listToCheck ) {
			if( entry.getA().equalsIgnoreCase( parent ) ) {
				return true;
			}
		}
		return false;
	}

	public void addJointIdsToSuppress( List<String> jointIds ) {
		for( String jointId : jointIds ) {
			if( !this.jointIdsToSuppress.contains( jointId ) ) {
				this.jointIdsToSuppress.add( jointId );
			}
		}
	}

	public void addJointIdsToSuppress( String[] jointIds ) {
		for( String jointId : jointIds ) {
			if( !this.jointIdsToSuppress.contains( jointId ) ) {
				this.jointIdsToSuppress.add( jointId );
			}
		}
	}

	static final Pattern arrayPattern = Pattern.compile( "(_\\d*$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL );

	public static int getArrayIndexForJoint( String jointName ) {
		Matcher match = arrayPattern.matcher( jointName );
		if( match.find() ) {
			String indexStr = jointName.substring( jointName.lastIndexOf( '_' ) + 1 );
			return Integer.decode( indexStr );
		}
		else {
			return -1;
		}
	}

	public static String getArrayNameForJoint( String jointName, Map<String, String> customArrayNameMap ) {
		Matcher match = arrayPattern.matcher( jointName );
		if( match.find() ) {
			String nameStr = jointName.substring( 0, jointName.lastIndexOf( '_' ) );
			if( ( customArrayNameMap != null ) && customArrayNameMap.containsKey( nameStr ) ) {
				nameStr = customArrayNameMap.get( nameStr );
			}
			return nameStr;
		}
		else {
			return null;
		}
	}

	public static Map<String, List<String>> getArrayEntriesFromJointList( List<Tuple2<String, String>> jointList, Map<String, String> customArrayNameMap, List<String> jointsToSuppress ) {
		List<String> jointNames = new LinkedList<String>();
		for( Tuple2<String, String> joint : jointList ) {
			jointNames.add( joint.getA() );
		}
		return getArrayEntries( jointNames, customArrayNameMap, jointsToSuppress );
	}

	public static Map<String, List<String>> getArrayEntries( List<String> jointNames, Map<String, String> customArrayNameMap, List<String> jointsToSuppress ) {
		//Array name, joint name entries
		Map<String, List<String>> arrayEntries = new HashMap<String, List<String>>();

		for( String jointName : jointNames ) {
			if( ( jointsToSuppress == null ) || !jointsToSuppress.contains( jointName ) ) {
				String arrayName = getArrayNameForJoint( jointName, customArrayNameMap );
				if( arrayName != null ) {
					if( arrayEntries.containsKey( arrayName ) ) {
						arrayEntries.get( arrayName ).add( jointName );
					}
					else {
						List<String> arrayElements = new ArrayList<String>();
						arrayElements.add( jointName );
						arrayEntries.put( arrayName, arrayElements );
					}
				}
			}
		}
		//How to sort array entry names that are not really comparable?
		// Like entries with different names (ENGINE_WHEEL_1 vs COAL_BOX_WHEEL_1) but have a clear spatial ordering
		// We don't handle cases like this 
		for( Entry<String, List<String>> arrayEntry : arrayEntries.entrySet() ) {
			Collections.sort( arrayEntry.getValue(), new Comparator<String>() {
				public int compare( String o1, String o2 ) {
					int index1 = getArrayIndexForJoint( o1 );
					int index2 = getArrayIndexForJoint( o2 );
					if( index1 == index2 ) {
						System.err.println( "ERROR COMPARING ARRAY NAME INDICES: " + o1 + " == " + o2 );
					}
					//We don't handle cases where the index is the same.
					assert index1 != index2;
					return index1 - index2;
				}
			} );
		}

		return arrayEntries;
	}

	private static List<Tuple2<String, String>> removeEntry( List<Tuple2<String, String>> sourceList, String toRemove ) {
		Tuple2<String, String> entryToRemove = null;
		//Find the entry to remove
		for( Tuple2<String, String> entry : sourceList ) {
			if( entry.getA().equalsIgnoreCase( toRemove ) ) {
				entryToRemove = entry;
				break;
			}
		}
		if( entryToRemove != null ) {
			//Remap any existing joints that are children of the joint to remove to be children of the parent of the joint to remove
			for( Tuple2<String, String> entry : sourceList ) {
				if( ( entry.getB() != null ) && entry.getB().equalsIgnoreCase( entryToRemove.getA() ) ) {
					entry.setB( entryToRemove.getB() );
				}
			}
			//Remove the joint
			sourceList.remove( entryToRemove );
		}
		return sourceList;
	}

	private static boolean isRootJoint( String jointName ) {
		return jointName.equalsIgnoreCase( "root" );
	}

	private List<Tuple2<String, String>> makeCodeReadyTree( List<Tuple2<String, String>> sourceList ) {
		if( sourceList != null ) {
			List<Tuple2<String, String>> cleaned = new ArrayList<Tuple2<String, String>>();
			for( Tuple2<String, String> entry : sourceList ) {
				if( REMOVE_ROOT_JOINTS ) {
					if( isRootJoint( entry.getA() ) && ( ( entry.getB() == null ) || ( entry.getB().length() == 0 ) ) ) {
						continue;
					}
					else if( ( entry.getB() != null ) && isRootJoint( entry.getB() ) ) {
						entry.setB( null );
					}
				}
				cleaned.add( entry );
			}
			List<Tuple2<String, String>> sorted = new ArrayList<Tuple2<String, String>>();
			while( sorted.size() != cleaned.size() ) {
				for( Tuple2<String, String> entry : cleaned ) {
					if( !sorted.contains( entry ) && hasParent( sorted, entry.getB() ) ) {
						sorted.add( entry );
					}
				}
			}

			//Remove joints that are in the "to suppress" list
			//			for (String toSuppress : this.jointIdsToSuppress) {
			//				sorted = removeEntry(sorted, toSuppress);
			//			}

			return sorted;
		}
		return null;
	}

	public void setJointMap( List<Tuple2<String, String>> jointList )
	{
		this.jointList = jointList;
		//		if (this.classData == null)
		//		{
		//			this.classData = ModelResourceExporter.getBestClassDataForJointList(jointList);
		//		}
	}

	public void addSubResource( ModelSubResourceExporter subResource ) {
		this.subResources.add( subResource );
	}

	//	public void addResource( String modelName, String textureName, String resourceType ) {
	//		this.addSubResource( new ModelSubResourceExporter( modelName, textureName, resourceType ) );
	//	}

	public void addResource( String modelName, String textureName, String resourceType, String attributionName, String attributionYear ) {
		String attributionNameToUse = null;
		String attributionYearToUse = null;
		if( ( attributionName != null ) && !attributionName.equals( this.attributionName ) ) {
			attributionNameToUse = attributionName;
		}
		if( ( attributionYear != null ) && !attributionYear.equals( this.attributionYear ) ) {
			attributionYearToUse = attributionYear;
		}
		if( ( attributionNameToUse != null ) && ( attributionNameToUse.length() == 0 ) ) {
			attributionNameToUse = null;
		}
		if( ( attributionYearToUse != null ) && ( attributionYearToUse.length() == 0 ) ) {
			attributionYearToUse = null;
		}
		this.addSubResource( new ModelSubResourceExporter( modelName, textureName, resourceType, attributionNameToUse, attributionYearToUse ) );
	}

	public void addSubResourceTags( String modelName, String textureName, String... tags ) {
		if( ( tags != null ) && ( tags.length > 0 ) ) {
			for( ModelSubResourceExporter subResource : this.subResources ) {
				if( subResource.getModelName().equalsIgnoreCase( modelName ) ) {
					if( ( textureName == null ) || subResource.getTextureName().equalsIgnoreCase( textureName ) ) {
						subResource.addTags( tags );
					}
				}
			}
		}
	}

	public void addSubResourceGroupTags( String modelName, String textureName, String... tags ) {
		if( ( tags != null ) && ( tags.length > 0 ) ) {
			for( ModelSubResourceExporter subResource : this.subResources ) {
				if( subResource.getModelName().equalsIgnoreCase( modelName ) ) {
					if( ( textureName == null ) || subResource.getTextureName().equalsIgnoreCase( textureName ) ) {
						subResource.addGroupTags( tags );
					}
				}
			}
		}
	}

	public void addSubResourceThemeTags( String modelName, String textureName, String... tags ) {
		if( ( tags != null ) && ( tags.length > 0 ) ) {
			for( ModelSubResourceExporter subResource : this.subResources ) {
				if( subResource.getModelName().equalsIgnoreCase( modelName ) ) {
					if( ( textureName == null ) || subResource.getTextureName().equalsIgnoreCase( textureName ) ) {
						subResource.addThemeTags( tags );
					}
				}
			}
		}
	}

	public void addAttribution( String name, String year )
	{
		this.attributionName = name;
		this.attributionYear = year;
	}

	public void addTags( String... tags ) {
		if( tags != null ) {
			for( String s : tags ) {
				this.tags.add( s );
			}
		}
	}

	public void addGroupTags( String... tags ) {
		if( tags != null ) {
			for( String s : tags ) {
				this.groupTags.add( s );
			}
		}
	}

	public void addThemeTags( String... tags ) {
		if( tags != null ) {
			for( String s : tags ) {
				this.themeTags.add( s );
			}
		}
	}

	public boolean isSims() {
		return this.isSims;
	}

	public void setIsSims( boolean isSims ) {
		this.isSims = isSims;
	}

	public String getClassName()
	{
		return this.className;
	}

	public String getPackageString()
	{
		return this.classData.packageString;
	}

	public void setBoundingBox( String modelName, AxisAlignedBox boundingBox )
	{
		this.boundingBoxes.put( modelName, boundingBox );
	}

	//	public void addThumbnail( String modelName, String textureName, String resourceType, String attributionName, String attributionYear, Image thumbnail )
	//	{
	//		this.thumbnails.put( new ModelSubResourceExporter( modelName, textureName, resourceType, attributionName, attributionYear ), thumbnail );
	//	}

	public void addExistingThumbnail( String name, File thumbnailFile ) {
		if( ( thumbnailFile != null ) && thumbnailFile.exists() ) {
			if( this.existingThumbnails == null ) {
				this.existingThumbnails = new HashMap<String, File>();
			}
			this.existingThumbnails.put( name, thumbnailFile );
		}
		else {
			System.err.println( "FAILED TO ADDED THUMBAIL: " + thumbnailFile + " does not exist." );
		}
	}

	public void setXMLFile( File xmlFile )
	{
		this.xmlFile = xmlFile;
	}

	public void setJavaFile( File javaFile )
	{
		this.javaFile = javaFile;
	}

	private static org.w3c.dom.Element createBoundingBoxElement( Document doc, AxisAlignedBox bbox )
	{
		org.w3c.dom.Element bboxElement = doc.createElement( "BoundingBox" );
		org.w3c.dom.Element minElement = doc.createElement( "Min" );
		minElement.setAttribute( "x", Double.toString( bbox.getXMinimum() ) );
		minElement.setAttribute( "y", Double.toString( bbox.getYMinimum() ) );
		minElement.setAttribute( "z", Double.toString( bbox.getZMinimum() ) );
		org.w3c.dom.Element maxElement = doc.createElement( "Max" );
		maxElement.setAttribute( "x", Double.toString( bbox.getXMaximum() ) );
		maxElement.setAttribute( "y", Double.toString( bbox.getYMaximum() ) );
		maxElement.setAttribute( "z", Double.toString( bbox.getZMaximum() ) );

		bboxElement.appendChild( minElement );
		bboxElement.appendChild( maxElement );

		return bboxElement;
	}

	private static org.w3c.dom.Element createTagsElement( Document doc, List<String> tagList )
	{
		org.w3c.dom.Element tagsElement = doc.createElement( "Tags" );
		for( String tag : tagList ) {
			org.w3c.dom.Element tagElement = doc.createElement( "Tag" );
			tagElement.setTextContent( tag );
			tagsElement.appendChild( tagElement );
		}
		return tagsElement;
	}

	private static org.w3c.dom.Element createGroupTagsElement( Document doc, List<String> tagList )
	{
		org.w3c.dom.Element tagsElement = doc.createElement( "GroupTags" );
		for( String tag : tagList ) {
			org.w3c.dom.Element tagElement = doc.createElement( "GroupTag" );
			tagElement.setTextContent( tag );
			tagsElement.appendChild( tagElement );
		}
		return tagsElement;
	}

	private static org.w3c.dom.Element createThemeTagsElement( Document doc, List<String> tagList )
	{
		org.w3c.dom.Element tagsElement = doc.createElement( "ThemeTags" );
		for( String tag : tagList ) {
			org.w3c.dom.Element tagElement = doc.createElement( "ThemeTag" );
			tagElement.setTextContent( tag );
			tagsElement.appendChild( tagElement );
		}
		return tagsElement;
	}

	private static org.w3c.dom.Element createSubResourceElement( Document doc, ModelSubResourceExporter subResource, ModelResourceExporter parentMRE )
	{
		org.w3c.dom.Element resourceElement = doc.createElement( "Resource" );
		resourceElement.setAttribute( "textureName", AliceResourceUtilties.makeEnumName( subResource.getTextureName() ) );
		resourceElement.setAttribute( "resourceName", createResourceEnumName( parentMRE, subResource ) );
		if( subResource.getModelName() != null ) {
			resourceElement.setAttribute( "modelName", subResource.getModelName() );
		}
		if( subResource.getAttributionName() != null ) {
			resourceElement.setAttribute( "creator", subResource.getAttributionName() );
		}
		if( subResource.getAttributionYear() != null ) {
			resourceElement.setAttribute( "creationYear", subResource.getAttributionYear() );
		}
		if( subResource.getModelName() != null ) {
			resourceElement.setAttribute( "modelName", subResource.getModelName() );
		}
		if( subResource.getBbox() != null ) {
			resourceElement.appendChild( createBoundingBoxElement( doc, subResource.getBbox() ) );
		}
		if( subResource.getTags().size() > 0 ) {
			List<String> uniqueTags = new ArrayList<String>();
			for( String t : subResource.getTags() ) {
				if( ( parentMRE.tags == null ) || !parentMRE.tags.contains( t ) ) {
					uniqueTags.add( t );
				}
			}
			if( !uniqueTags.isEmpty() ) {
				resourceElement.appendChild( createTagsElement( doc, uniqueTags ) );
			}
		}

		if( subResource.getGroupTags().size() > 0 ) {
			List<String> uniqueTags = new ArrayList<String>();
			for( String t : subResource.getGroupTags() ) {
				if( ( parentMRE.groupTags == null ) || !parentMRE.groupTags.contains( t ) ) {
					uniqueTags.add( t );
				}
			}
			if( !uniqueTags.isEmpty() ) {
				resourceElement.appendChild( createGroupTagsElement( doc, uniqueTags ) );
			}
		}

		if( subResource.getThemeTags().size() > 0 ) {
			List<String> uniqueTags = new ArrayList<String>();
			for( String t : subResource.getThemeTags() ) {
				if( ( parentMRE.themeTags == null ) || !parentMRE.themeTags.contains( t ) ) {
					uniqueTags.add( t );
				}
			}
			if( !uniqueTags.isEmpty() ) {
				resourceElement.appendChild( createThemeTagsElement( doc, uniqueTags ) );
			}
		}
		return resourceElement;
	}

	private Document createXMLDocument()
	{
		try
		{
			Document doc = XMLUtilities.createDocument();
			org.w3c.dom.Element modelRoot = doc.createElement( "AliceModel" );
			modelRoot.setAttribute( "name", this.className );
			if( ( this.attributionName != null ) && ( this.attributionName.length() > 0 ) )
			{
				modelRoot.setAttribute( "creator", this.attributionName );
			}
			if( ( this.attributionYear != null ) && ( this.attributionYear.length() > 0 ) )
			{
				modelRoot.setAttribute( "creationYear", this.attributionYear );
			}
			if( this.isDeprecated )
			{
				modelRoot.setAttribute( "deprecated", "TRUE" );
			}
			if( this.placeOnGround )
			{
				modelRoot.setAttribute( "placeOnGround", "TRUE" );
			}
			doc.appendChild( modelRoot );
			if( this.boundingBoxes.get( this.className ) == null ) {
				AxisAlignedBox superBox = new AxisAlignedBox();
				for( Entry<String, AxisAlignedBox> entry : this.boundingBoxes.entrySet() ) {
					superBox.union( entry.getValue() );
				}
				this.boundingBoxes.put( this.className, superBox );
			}
			modelRoot.appendChild( createBoundingBoxElement( doc, this.boundingBoxes.get( this.className ) ) );
			modelRoot.appendChild( createTagsElement( doc, this.tags ) );
			modelRoot.appendChild( createGroupTagsElement( doc, this.groupTags ) );
			modelRoot.appendChild( createThemeTagsElement( doc, this.themeTags ) );

			for( ModelSubResourceExporter subResource : this.subResources ) {
				if( !subResource.getModelName().equalsIgnoreCase( this.className ) && this.boundingBoxes.containsKey( subResource.getModelName() ) ) {
					subResource.setBbox( this.boundingBoxes.get( subResource.getModelName() ) );
				}
				modelRoot.appendChild( createSubResourceElement( doc, subResource, this ) );
			}

			return doc;
		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}

	private static List<ModelClassData> POTENTIAL_MODEL_CLASS_DATA_OPTIONS = null;

	public static ModelClassData getBestClassDataForJointList( List<Tuple2<String, String>> jointList )
	{
		if( POTENTIAL_MODEL_CLASS_DATA_OPTIONS == null )
		{
			POTENTIAL_MODEL_CLASS_DATA_OPTIONS = new LinkedList<ModelClassData>();
			Field[] dataFields = AliceResourceClassUtilities.getFieldsOfType( ModelClassData.class, ModelClassData.class );
			for( Field f : dataFields )
			{
				ModelClassData data = null;
				try {
					Object o = f.get( null );
					if( ( o != null ) && ( o instanceof ModelClassData ) )
					{
						data = (ModelClassData)o;
					}
				} catch( Exception e ) {
				}
				if( data != null )
				{
					POTENTIAL_MODEL_CLASS_DATA_OPTIONS.add( data );
				}
			}
		}

		int highScore = Integer.MIN_VALUE;
		ModelClassData bestFit = null;
		for( ModelClassData mcd : POTENTIAL_MODEL_CLASS_DATA_OPTIONS )
		{
			List<Tuple2<String, String>> modelDataJoints = getExistingJointIdPairs( mcd.superClass );
			int score = -Math.abs( modelDataJoints.size() - jointList.size() );
			for( Tuple2<String, String> inputPair : jointList )
			{
				for( Tuple2<String, String> testPair : modelDataJoints )
				{
					if( inputPair.equals( testPair ) )
					{
						score++;
						break;
					}
				}
			}
			if( score > highScore )
			{
				highScore = score;
				bestFit = mcd;
			}
		}
		return bestFit;
	}

	private static List<Tuple2<String, String>> getExistingJointIdPairs( Class<?> resourceClass )
	{
		List<Tuple2<String, String>> ids = new LinkedList<Tuple2<String, String>>();
		Field[] fields = resourceClass.getDeclaredFields();
		for( Field f : fields )
		{
			if( org.lgna.story.resources.JointId.class.isAssignableFrom( f.getType() ) )
			{
				String fieldName = f.getName();
				String parentName = null;
				org.lgna.story.resources.JointId fieldData = null;
				try {
					Object o = f.get( null );
					if( ( o != null ) && ( o instanceof org.lgna.story.resources.JointId ) )
					{
						fieldData = (org.lgna.story.resources.JointId)o;
					}
				} catch( Exception e ) {
				}
				if( ( fieldData != null ) && ( fieldData.getParent() != null ) ) {
					parentName = fieldData.getParent().toString();
				}

				ids.add( Tuple2.createInstance( fieldName, parentName ) );
			}
		}
		Class<?>[] interfaces = resourceClass.getInterfaces();
		for( Class<?> i : interfaces )
		{
			ids.addAll( getExistingJointIdPairs( i ) );
		}
		return ids;
	}

	private static List<String> getExistingJointIds( Class<?> resourceClass )
	{
		List<String> ids = new LinkedList<String>();
		Field[] fields = resourceClass.getDeclaredFields();
		for( Field f : fields )
		{
			if( org.lgna.story.resources.JointId.class.isAssignableFrom( f.getType() ) )
			{
				String fieldName = f.getName();
				ids.add( fieldName );
			}
		}
		Class<?>[] interfaces = resourceClass.getInterfaces();
		for( Class<?> i : interfaces )
		{
			ids.addAll( getExistingJointIds( i ) );
		}
		return ids;
	}

	private java.lang.reflect.Field getJointRootsField( Class<?> cls )
	{
		if( cls == null )
		{
			return null;
		}
		java.lang.reflect.Field[] rootFields = AliceResourceClassUtilities.getFieldsOfType( cls, org.lgna.story.resources.JointId[].class );
		if( rootFields.length == 1 )
		{
			return rootFields[ 0 ];
		}
		else {
			Class[] interfaces = cls.getInterfaces();
			for( Class i : interfaces )
			{
				java.lang.reflect.Field rootField = getJointRootsField( i );
				if( rootField != null )
				{
					return rootField;
				}
			}
		}
		return null;
	}

	private boolean needsToDefineRootsMethod( Class<?> cls )
	{
		if( cls == null )
		{
			return false;
		}
		java.lang.reflect.Method[] methods = cls.getMethods();
		for( java.lang.reflect.Method m : methods )
		{
			if( org.lgna.story.resources.JointId[].class.isAssignableFrom( m.getReturnType() ) )
			{
				return true;
			}
		}
		Class[] interfaces = cls.getInterfaces();
		for( Class i : interfaces )
		{
			boolean needToDefineMethod = needsToDefineRootsMethod( i );
			if( needToDefineMethod )
			{
				return needToDefineMethod;
			}
		}
		return false;
	}

	public static String getAccessorMethodsForResourceClass( Class<? extends org.lgna.story.resources.JointedModelResource> resourceClass )
	{
		StringBuilder sb = new StringBuilder();
		List<String> jointIds = getExistingJointIds( resourceClass );
		for( String id : jointIds )
		{
			sb.append( "public Joint get" + AliceResourceClassUtilities.getAliceMethodNameForEnum( id ) + "() {\n" );
			sb.append( "\t return org.lgna.story.Joint.getJoint( this, " + resourceClass.getCanonicalName() + "." + id + ");\n" );
			sb.append( "}\n" );
		}
		return sb.toString();
	}

	private static String createResourceEnumName( ModelResourceExporter parentExporter, String modelName, String textureName ) {
		if( modelName.equalsIgnoreCase( parentExporter.getClassName() ) ) {
			return AliceResourceUtilties.makeEnumName( textureName );
		}
		else if( modelName.equalsIgnoreCase( textureName ) || textureName.equalsIgnoreCase( AliceResourceUtilties.getDefaultTextureEnumName( modelName ) ) || textureName.equalsIgnoreCase( AliceResourceUtilties.makeEnumName( modelName ) ) ) {
			return AliceResourceUtilties.makeEnumName( modelName );
		}
		else {
			return AliceResourceUtilties.makeEnumName( modelName ) + "_" + AliceResourceUtilties.makeEnumName( textureName );
		}
	}

	private static String createResourceEnumName( ModelResourceExporter parentExporter, ModelSubResourceExporter resource ) {
		return createResourceEnumName( parentExporter, resource.getModelName(), resource.getTextureName() );
	}

	public String createResourceEnumName( String modelName, String textureName ) {
		return createResourceEnumName( this, modelName, textureName );
	}

	private boolean shouldSuppressJoint( String jointString ) {
		if( this.jointIdsToSuppress.contains( jointString ) ) {
			return true;
		}
		if( isRootJoint( jointString ) ) {
			return true;
		}
		return false;
	}

	private boolean isJointInAnArray( String jointString, Map<String, List<String>> arrayEntries ) {
		for( Entry<String, List<String>> entry : arrayEntries.entrySet() ) {
			if( entry.getValue().contains( jointString ) ) {
				return true;
			}
		}
		return false;
	}

	private String getArrayAccessorMethodName( String arrayName ) {
		return "get" + AliceResourceUtilties.enumToCamelCase( arrayName );
	}

	//If a parent interface has declared an accessor for a given array, return true
	// otherwisse return false
	private boolean needsAccessorMethodForArray( ModelClassData classData, String arrayName ) {
		String arrayAccessorMethodName = getArrayAccessorMethodName( arrayName );
		Method m = null;
		try {
			m = classData.superClass.getDeclaredMethod( arrayAccessorMethodName );
			if( ( m != null ) && m.getDeclaringClass().isInterface() ) {
				return true;
			}
		} catch( NoSuchMethodException me ) {
		}
		return false;
	}

	public String createJavaCode()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( JavaCodeUtilities.getCopyrightComment() );
		sb.append( JavaCodeUtilities.LINE_RETURN );
		sb.append( "package " + this.classData.packageString + ";" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN );
		sb.append( "import org.lgna.project.annotations.*;" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "import org.lgna.story.resources.ImplementationAndVisualType;" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN );
		if( this.isDeprecated ) {
			sb.append( "@Deprecated" + JavaCodeUtilities.LINE_RETURN );
		}
		sb.append( "public enum " + this.getJavaClassName() + " implements " + this.classData.superClass.getCanonicalName() + " {" + JavaCodeUtilities.LINE_RETURN );
		assert this.subResources.size() > 0;
		boolean isFirst = true;
		for( int i = 0; i < this.subResources.size(); i++ )
		{
			ModelSubResourceExporter resource = this.subResources.get( i );
			String resourceEnumName = createResourceEnumName( this, resource );
			if( isValidEnumName( resource.getModelName(), resourceEnumName ) ) {
				if( !isFirst ) {
					sb.append( "," + JavaCodeUtilities.LINE_RETURN );
				}
				String typeString = "";
				if( !resource.getTypeString().equals( ImplementationAndVisualType.ALICE.toString() ) ) {
					typeString = "( ImplementationAndVisualType." + resource.getTypeString() + " )";
				}
				sb.append( "\t" + resourceEnumName + typeString );
				isFirst = false;
			}
			else {
				System.out.println( "SKIPPING ENUM NAME: " + resourceEnumName );
			}
		}
		sb.append( ";" + JavaCodeUtilities.LINE_RETURN );
		List<String> existingIds = getExistingJointIds( this.classData.superClass );
		boolean addedRoots = false;
		List<Tuple2<String, String>> trimmedSkeleton = makeCodeReadyTree( this.jointList );
		if( trimmedSkeleton != null )
		{
			Map<String, List<String>> arrayEntries = getArrayEntriesFromJointList( trimmedSkeleton, this.customArrayNameMap, this.jointIdsToSuppress );
			List<String> rootJoints = new LinkedList<String>();
			sb.append( JavaCodeUtilities.LINE_RETURN );
			for( Tuple2<String, String> entry : trimmedSkeleton )
			{
				String jointString = entry.getA();
				String parentString = entry.getB();
				if( existingIds.contains( jointString ) )
				{
					continue;
				}
				if( ( parentString == null ) || ( parentString.length() == 0 ) )
				{
					parentString = "null";
					rootJoints.add( jointString );
					addedRoots = true;
				}
				if( shouldSuppressJoint( jointString ) || isJointInAnArray( jointString, arrayEntries ) ) {
					sb.append( "@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)" + JavaCodeUtilities.LINE_RETURN );
				}
				else {
					sb.append( "@FieldTemplate(visibility=Visibility.PRIME_TIME)" + JavaCodeUtilities.LINE_RETURN );
				}
				sb.append( "\tpublic static final org.lgna.story.resources.JointId " + jointString + " = new org.lgna.story.resources.JointId( " + parentString + ", " + this.getJavaClassName() + ".class );" + JavaCodeUtilities.LINE_RETURN );
			}

			if( addedRoots )
			{
				sb.append( "\n\t@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )" );
				sb.append( "\n\tpublic static final org.lgna.story.resources.JointId[] " + ROOT_IDS_FIELD_NAME + " = { " );
				for( int i = 0; i < rootJoints.size(); i++ ) {
					sb.append( rootJoints.get( i ) );
					if( i < ( rootJoints.size() - 1 ) ) {
						sb.append( ", " );
					}
				}
				sb.append( " };" + JavaCodeUtilities.LINE_RETURN );
			}

			if( !arrayEntries.isEmpty() ) {
				for( Entry<String, List<String>> arrayEntry : arrayEntries.entrySet() ) {
					List<String> arrayElements = arrayEntry.getValue();
					String arrayName = arrayEntry.getKey();// + "_ARRAY";
					boolean needsAccessor = needsAccessorMethodForArray( classData, arrayName );

					//If an accessor is needed, add a "COMPLETELY_HIDDEN" annotation.
					// The accessor is used to retrieve the array via a parent class and therefore the array itself is essentially already handled
					// If there is no accessor, then we want the code generation system to create an Alice level accessor at runtime (which this annotation prevents)
					if( needsAccessor ) {
						sb.append( "\n\t@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )" );
					}
					sb.append( "\n\tpublic static final org.lgna.story.resources.JointId[] " + arrayName + " = { " );
					for( int i = 0; i < arrayElements.size(); i++ ) {
						sb.append( arrayElements.get( i ) );
						if( i < ( arrayElements.size() - 1 ) ) {
							sb.append( ", " );
						}
					}
					sb.append( " };" + JavaCodeUtilities.LINE_RETURN );
					if( needsAccessor ) {
						String arrayAccessorName = getArrayAccessorMethodName( arrayName );
						sb.append( "\tpublic org.lgna.story.resources.JointId[] " + arrayAccessorName + "(){" + JavaCodeUtilities.LINE_RETURN );
						sb.append( "\t\treturn " + this.getJavaClassName() + "." + arrayName + ";" + JavaCodeUtilities.LINE_RETURN );
						sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN );
					}
				}
			}
		}
		sb.append( JavaCodeUtilities.LINE_RETURN );
		sb.append( "\tprivate final ImplementationAndVisualType resourceType;" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\tprivate " + this.getJavaClassName() + "() {" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t\tthis( ImplementationAndVisualType.ALICE );" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\tprivate " + this.getJavaClassName() + "( ImplementationAndVisualType resourceType ) {" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t\tthis.resourceType = resourceType;" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN + JavaCodeUtilities.LINE_RETURN );
		if( needsToDefineRootsMethod( this.classData.superClass ) )
		{
			sb.append( "\tpublic org.lgna.story.resources.JointId[] getRootJointIds(){" + JavaCodeUtilities.LINE_RETURN );
			if( addedRoots )
			{
				sb.append( "\t\treturn " + this.getJavaClassName() + "." + ROOT_IDS_FIELD_NAME + ";" + JavaCodeUtilities.LINE_RETURN );
			}
			else
			{
				java.lang.reflect.Field rootsField = getJointRootsField( this.classData.superClass );
				if( rootsField != null )
				{
					sb.append( "\t\treturn " + rootsField.getDeclaringClass().getCanonicalName() + "." + rootsField.getName() + ";" + JavaCodeUtilities.LINE_RETURN );
				}
				else
				{
					sb.append( "\t\treturn new org.lgna.story.resources.JointId[0];" + JavaCodeUtilities.LINE_RETURN );
				}
			}
			sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN );
		}
		sb.append( "\n\tpublic org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t\treturn this.resourceType.getFactory( this );" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\tpublic " + this.classData.implementationClass.getCanonicalName() + " createImplementation( " + this.classData.abstractionClass.getCanonicalName() + " abstraction ) {" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t\treturn new " + this.classData.implementationClass.getCanonicalName() + "( abstraction, this.resourceType.getFactory( this ) );" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "\t}" + JavaCodeUtilities.LINE_RETURN );
		sb.append( "}" + JavaCodeUtilities.LINE_RETURN );

		return sb.toString();
	}

	private void add( File source, JarOutputStream target, String destPathPrefix, boolean recursive ) throws IOException
	{
		if( destPathPrefix == null ) {
			destPathPrefix = "";
		}
		if( ( destPathPrefix != null ) && ( destPathPrefix.length() > 0 ) ) {
			destPathPrefix = destPathPrefix.replace( "\\", "/" );
			if( !destPathPrefix.endsWith( "/" ) ) {
				destPathPrefix += "/";
			}
			if( destPathPrefix.startsWith( "/" ) || destPathPrefix.startsWith( "\\" ) ) {
				destPathPrefix = destPathPrefix.substring( 1 );
			}
		}

		String root = source.getAbsolutePath().replace( "\\", "/" ) + "/";
		this.add( source, target, root, destPathPrefix, recursive );
	}

	private void add( File source, JarOutputStream target, String root, String destPathPrefix, boolean recursive ) throws IOException
	{
		BufferedInputStream in = null;
		try
		{
			if( source.isDirectory() )
			{
				String name = source.getPath().replace( "\\", "/" );
				if( name.length() > 0 )
				{
					if( !name.endsWith( "/" ) ) {
						name += "/";
					}
					name = name.substring( root.length() );
					if( name.startsWith( "/" ) ) {
						name = name.substring( 1 );
					}
					if( name.length() > 0 )
					{
						name = destPathPrefix + name;
						JarEntry entry = new JarEntry( name );
						entry.setTime( source.lastModified() );
						try
						{
							System.out.println( "   Adding: " + name );
							target.putNextEntry( entry );
							target.closeEntry();
						} catch( ZipException ze )
						{
							System.err.println( ze.getMessage() );
						}
					}
				}
				for( File nestedFile : source.listFiles() )
				{
					if( !nestedFile.isDirectory() || recursive )
					{
						add( nestedFile, target, root, destPathPrefix, recursive );
					}
				}
				return;
			}

			String entryName = source.getPath().replace( "\\", "/" );
			entryName = entryName.substring( root.length() );
			if( entryName.startsWith( "/" ) || entryName.startsWith( "\\" ) ) {
				entryName = entryName.substring( 1 );
			}
			entryName = destPathPrefix + entryName;
			JarEntry entry = new JarEntry( entryName );
			entry.setTime( source.lastModified() );
			target.putNextEntry( entry );
			in = new BufferedInputStream( new FileInputStream( source ) );

			byte[] buffer = new byte[ 1024 ];
			while( true )
			{
				int count = in.read( buffer );
				if( count == -1 ) {
					break;
				}
				target.write( buffer, 0, count );
			}
			target.closeEntry();
		} finally
		{
			if( in != null ) {
				in.close();
			}
		}
	}

	private String getJavaClassName() {
		return this.className + AliceResourceClassUtilities.RESOURCE_SUFFIX;
	}

	private File getJavaCodeDir( String root )
	{
		String packageDirectory = JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString );
		return new File( root + packageDirectory );
	}

	private File getJavaClassFile( String root )
	{
		String filename = JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString ) + this.getJavaClassName() + ".class";
		return new File( root + filename );
	}

	private File getJavaFile( String root )
	{
		String filename = JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString ) + this.getJavaClassName() + ".java";
		return new File( root + filename );
	}

	private File createJavaCode( String root )
	{
		String packageDirectory = JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString );
		System.out.println( packageDirectory );
		String javaCode = createJavaCode();
		System.out.println( javaCode );
		System.out.println( System.getProperty( "java.class.path" ) );
		File javaFile = getJavaFile( root );
		TextFileUtilities.write( javaFile, javaCode );
		return javaFile;
	}

	private String createXMLString()
	{
		Document doc = this.createXMLDocument();
		if( doc != null )
		{
			try
			{
				TransformerFactory transfac = TransformerFactory.newInstance();
				transfac.setAttribute( "indent-number", new Integer( 4 ) );
				Transformer trans = transfac.newTransformer();
				//	                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				trans.setOutputProperty( OutputKeys.INDENT, "yes" );

				//create string from xml tree
				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult( sw );
				DOMSource source = new DOMSource( doc );
				trans.transform( source, result );
				String xmlString = sw.toString();

				return xmlString;
			} catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	private File getXMLFile( String root ) {
		if( !root.endsWith( "/" ) && !root.endsWith( "\\" ) ) {
			root += "/";
		}
		String resourceDirectory = root + JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString ) + org.lgna.story.implementation.alice.ModelResourceIoUtilities.getResourceSubDirWithSeparator( "" );
		File xmlFile = new File( resourceDirectory, this.className + ".xml" );
		return xmlFile;
	}

	private File createXMLFile( String root, boolean forceRebuild )
	{
		File outputFile = getXMLFile( root );
		try
		{
			if( !forceRebuild && ( this.xmlFile != null ) && this.xmlFile.exists() )
			{
				if( !outputFile.exists() )
				{
					FileUtilities.createParentDirectoriesIfNecessary( outputFile );
					outputFile.createNewFile();
				}
				FileUtilities.copyFile( this.xmlFile, outputFile );
				return outputFile;
			}
			else
			{
				//This path does not indent the xml
				//	        	Document doc = this.createXMLDocument();
				//	        	XMLUtilities.write(doc, outputFile);

				//This path does indenting
				String xmlString = this.createXMLString();
				if( !outputFile.exists() )
				{
					FileUtilities.createParentDirectoriesIfNecessary( outputFile );
					outputFile.createNewFile();
				}
				FileWriter fw = new FileWriter( outputFile );
				fw.write( xmlString );
				fw.close();

				return outputFile;
			}
		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return null;

	}

	private File saveImageToFile( String fileName, Image image )
	{
		try {
			int width = image.getWidth( null );
			int height = image.getHeight( null );
			if( ( width == 0 ) || ( height == 0 ) ) {
				return null;
			}
		} catch( Exception e ) {
			return null;
		}
		File outputFile = new File( fileName );
		try {
			if( !outputFile.exists() ) {
				FileUtilities.createParentDirectoriesIfNecessary( outputFile );
				outputFile.createNewFile();
			}
			ImageUtilities.write( outputFile, image );
			return outputFile;
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public String getThumbnailPath( String rootPath, String thumbnailName ) {
		if( !rootPath.endsWith( "/" ) && !rootPath.endsWith( "\\" ) ) {
			rootPath += "/";
		}
		String resourceDirectory = rootPath + JavaCodeUtilities.getDirectoryStringForPackage( this.classData.packageString ) + org.lgna.story.implementation.alice.ModelResourceIoUtilities.getResourceSubDirWithSeparator( this.className );
		return resourceDirectory + thumbnailName;
	}

	public static BufferedImage createClassThumb( BufferedImage imgSrc ) {
		//		ColorConvertOp colorConvert =
		//				new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null );
		//		if( imgSrc == null ) {
		//			System.out.println( "NULL!" );
		//		}
		//		try {
		//			colorConvert.filter( imgSrc, imgSrc );
		//		} catch( NullPointerException e ) {
		//			e.printStackTrace();
		//			throw e;
		//		}
		return imgSrc;
	}

	private List<File> saveThumbnailsToDir( String root )
	{
		List<File> thumbnailFiles = new LinkedList<File>();
		List<String> thumbnailsCreated = new LinkedList<String>();
		if( ( this.existingThumbnails != null ) && !this.existingThumbnails.isEmpty() ) {
			for( Entry<String, File> entry : this.existingThumbnails.entrySet() )
			{
				if( entry.getValue().exists() ) {
					thumbnailFiles.add( entry.getValue() );
					thumbnailsCreated.add( entry.getKey() );
				}
				else {
					System.err.println( "FAILED TO FIND THUMBNAIL FILE '" + entry.getValue() + "'" );
					return null;
				}
			}
		}
		for( Entry<ModelSubResourceExporter, Image> entry : this.thumbnails.entrySet() )
		{
			if( !thumbnailsCreated.contains( entry.getKey() ) ) {
				String thumbnailName = AliceResourceUtilties.getThumbnailResourceFileName( entry.getKey().getModelName(), entry.getKey().getTextureName() );
				File f = saveImageToFile( getThumbnailPath( root, thumbnailName ), entry.getValue() );
				if( f != null ) {
					thumbnailsCreated.add( thumbnailName );
					thumbnailFiles.add( f );
				}
			}
		}
		if( this.subResources.size() == 0 ) {
			System.err.println( "NO SUB RESOURCES ON " + this.resourceName );
		}
		ModelSubResourceExporter firstSubResource = this.subResources.get( 0 );
		String firstThumbName = AliceResourceUtilties.getThumbnailResourceFileName( firstSubResource.getModelName(), firstSubResource.getTextureName() );
		String classThumbName = AliceResourceUtilties.getThumbnailResourceFileName( this.getClassName(), null );
		File firstThumbFile = new File( getThumbnailPath( root, firstThumbName ) );
		File classThumbFile = new File( getThumbnailPath( root, classThumbName ) );

		//TODO: Handle this error in a better way
		try {
			BufferedImage classThumb = createClassThumb( ImageUtilities.read( firstThumbFile ) );

			ImageUtilities.write( classThumbFile, classThumb );
			thumbnailFiles.add( classThumbFile );
		} catch( IOException ioe ) {
			ioe.printStackTrace();
			System.err.println( "Error reading thumbnail " + firstThumbFile + ", Deleting it..." );
			firstThumbFile.delete();
		}

		return thumbnailFiles;
	}

	public ModelResourceInfo addToJar( String sourceDirectory, String resourceDirectory, JarOutputStream resourceJarStream, JarOutputStream sourceJarStream, String destResourceDirPrefix, boolean rebuildJavaFile, boolean rebuildXmlFile ) throws IOException
	{
		if( !sourceDirectory.endsWith( "/" ) && !sourceDirectory.endsWith( "\\" ) ) {
			sourceDirectory += File.separator;
		}
		xmlFile = getXMLFile( resourceDirectory );
		if( ( rebuildXmlFile || !xmlFile.exists() || ( xmlFile.length() == 0 ) || isMoreRecentThan( this.lastEdited, xmlFile ) ) && ( resourceJarStream != null ) ) {
			xmlFile = createXMLFile( resourceDirectory, rebuildXmlFile );
		}
		boolean shouldAddResources = ( resourceJarStream != null ) && this.getExportGalleryResources();
		boolean addResources = false;
		File resourceDir = null;
		if( shouldAddResources && ( xmlFile != null ) ) {
			addResources = true;
			resourceDir = xmlFile.getParentFile();
			List<File> thumbnailFiles = saveThumbnailsToDir( resourceDirectory );
		}
		else if( shouldAddResources ) {
			throw new IOException( "FAILED TO MAKE XML FILE FOR " + this.getClassName() + "--NOT ADDING IT TO JARS." );
		}
		boolean addClassData = false;
		File sourceDir = null;
		boolean shouldAddClassData = ( this.classData != null ) && ( sourceJarStream != null );
		if( shouldAddClassData ) {
			sourceDir = getJavaCodeDir( sourceDirectory );
			javaFile = getJavaFile( sourceDirectory );
			File classFile = getJavaClassFile( sourceDirectory );
			if( rebuildJavaFile || !javaFile.exists() || !classFile.exists() || ( javaFile.length() == 0 ) || isMoreRecentThan( this.lastEdited, javaFile ) ) {
				try {
					javaFile = createJavaCode( sourceDirectory );
				} catch( Exception e ) {
					e.printStackTrace();
					throw new IOException( "FAILED TO MAKE JAVA FILE FOR " + this.getClassName() + "--NOT ADDING IT TO JARS.\n" + e.toString() );
				}
			}
			try
			{
				JavaCodeUtilities.compileJavaFile( javaFile );
				addClassData = true;
			} catch( IOException ioe )
			{
				throw ioe;
			}

		}
		if( shouldAddResources && addResources ) {
			try
			{
				System.out.println( "Adding " + resourceDir );
				add( resourceDir, resourceJarStream, resourceDirectory, destResourceDirPrefix, true );
			} catch( Exception e )
			{
				throw new IOException( "FAILED ADDING RESROUCES TO RESOURCE JAR." + e );
			}
		}
		if( shouldAddClassData && addClassData ) {
			try
			{
				System.out.println( "Adding " + sourceDir );
				add( sourceDir, sourceJarStream, sourceDirectory, "", false );
			} catch( Exception e )
			{
				throw new IOException( "FAILED ADDING RESROUCES TO SOURCE JAR." + e );
			}
		}
		Document doc = XMLUtilities.read( xmlFile );
		ModelResourceInfo returnInfo = new ModelResourceInfo( doc );
		return returnInfo;
	}

	public boolean isValidEnumName( String modelName, String enumName ) {
		if( this.forcedEnumNamesMap.containsKey( modelName ) ) {
			List<String> validEnums = this.forcedEnumNamesMap.get( modelName );
			for( String e : validEnums ) {
				String otherToCheck = modelName.toUpperCase() + "_" + e;
				if( e.equalsIgnoreCase( enumName ) || otherToCheck.equalsIgnoreCase( enumName ) ) {
					return true;
				}
			}
			return false;
		}
		//If we aren't forcing any enum names then all enum names are valid
		if( this.forcedOverridingEnumNames.isEmpty() ) {
			return true;
		}
		for( String e : this.forcedOverridingEnumNames ) {
			if( e.equalsIgnoreCase( enumName ) ) {
				return true;
			}
		}
		return false;
	}

	public void addCustomArrayName( String arrayName, String customName ) {
		this.customArrayNameMap.put( arrayName, customName );
	}

	public void addCustomArrayNames( Map<String, String> customArrayNames ) {
		for( Entry<String, String> entry : customArrayNames.entrySet() ) {
			this.customArrayNameMap.put( entry.getKey(), entry.getValue() );
		}
	}

	public void addForcedEnumNames( String resourceName, List<String> enumNames ) {
		if( ( enumNames != null ) && ( enumNames.size() > 0 ) ) {
			if( resourceName == null ) {
				this.forcedOverridingEnumNames.addAll( enumNames );
			}
			else {
				List<String> nameList;
				if( !this.forcedEnumNamesMap.containsKey( resourceName ) ) {
					nameList = new ArrayList<String>();
					this.forcedEnumNamesMap.put( resourceName, nameList );
				}
				else {
					nameList = this.forcedEnumNamesMap.get( resourceName );
				}
				nameList.addAll( enumNames );
			}
		}
	}

	public ModelResourceInfo addToJar( String sourceDirectory, String resourceDirectory, JarOutputStream jos, String destResourceDirPrefix ) throws IOException
	{
		return addToJar( sourceDirectory, resourceDirectory, jos, jos, destResourceDirPrefix, true, true );
	}

	public ModelResourceInfo addToJar( String sourceDirectory, String resourceDirectory, JarOutputStream jos, String destResourceDirPrefix, boolean rebuildFiles ) throws IOException
	{
		return addToJar( sourceDirectory, resourceDirectory, jos, jos, destResourceDirPrefix, rebuildFiles, rebuildFiles );
	}

	public File export( String sourceDirectory, String resourceDirectory, String outputDir, String destResourceDirPrefix )
	{

		File outputFile = new File( outputDir + this.getJavaClassName() + ".jar" );
		try
		{
			FileUtilities.createParentDirectoriesIfNecessary( outputFile );
			FileOutputStream fos = new FileOutputStream( outputFile );
			JarOutputStream jos = new JarOutputStream( fos );
			addToJar( sourceDirectory, resourceDirectory, jos, destResourceDirPrefix );
			jos.close();
		} catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
		return outputFile;
	}

	/*
	 * public Joint getRightWrist() {
	 * return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.BipedResource.RIGHT_WRIST );
	 * }
	 */

	public static String getJointAccessCodeForClass( Class<?> resourceClass ) {
		List<String> ids = getExistingJointIds( resourceClass );
		StringBuilder sb = new StringBuilder();
		for( String id : ids ) {
			sb.append( "public org.lgna.story.Joint get" + AliceResourceClassUtilities.getAliceMethodNameForEnum( id ) + "() {\n" );
			sb.append( "\treturn org.lgna.story.Joint.getJoint( this, " + resourceClass.getName() + "." + id + " );\n" );
			sb.append( "}\n" );
		}

		return sb.toString();

	}

	public static void main( String[] args ) {
		System.out.println( getJointAccessCodeForClass( BipedResource.class ) );
	}
}

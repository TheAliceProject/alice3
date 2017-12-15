/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lgna.story.resourceutilities.exporterutils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 *
 * @author Administrator
 */
public class AliceModelExporter {

	public static URL EULA_RESOURCE = null;
	public static String EULA_STRING = null;

	private List<TextureReference> textureMapReferences = new LinkedList<TextureReference>();
	private List<MeshReference> meshReferences = new LinkedList<MeshReference>();
	private List<SkeletonReference> skeletonReferences = new LinkedList<SkeletonReference>();
	private List<ImageReference> extraTextureAssetReferences = new LinkedList<ImageReference>();
	private List<AssetReference> enumMapReferences = new LinkedList<AssetReference>();
	private List<Map<String, Short>> jointNameMap = new LinkedList<Map<String, Short>>();
	private Map<String, List<Integer>> meshToTextureReferenceMap = new HashMap<String, List<Integer>>();
	private Map<Short, BoundingBox> jointToBoundingBox = new HashMap<Short, BoundingBox>();
	//    private Map<String, Map<Short, AffineMatrix4x4>> poseMap = new HashMap<String, Map<Short,AffineMatrix4x4>>();
	private List<Pose> poses = new LinkedList<Pose>();
	private Pose defaultPose;

	private List<Tuple2<String, String>> jointParentList;
	private Map<Short, String> jointIDToNameMap;
	private File xmlFile = null;
	private File enumMapFile = null;
	private File rootPath = null;
	private String assetName;
	private String parentClassName;
	private BoundingBox boundingBox = new BoundingBox();
	private File rootDir = null;
	private boolean cullBackfaces = true;
	private String attributionName;
	private String attributionYear;
	private boolean isRebuiltData = false;
	private List<String> namesToPreserve;

	public AliceModelExporter( String assetName, String parentClassName ) {
		this.assetName = assetName;
		this.parentClassName = parentClassName;
	}

	public AliceModelExporter( String assetName, String parentClassName, File dataFile, File resourcePath ) throws IOException {
		this( assetName, parentClassName );
		xmlFile = dataFile;
		if( xmlFile.exists() ) {
			InputStream is = new FileInputStream( xmlFile );
			if( is != null ) {
				Document doc = XMLUtilities.read( is );
				ModelResourceInfo info = new ModelResourceInfo( doc );
				boundingBox = new BoundingBox( info.getBoundingBox().getMinimum(), info.getBoundingBox().getMaximum() );
				LinkedList<String> tagList = new LinkedList<String>();
				NodeList textureReferenceNodeList = doc.getElementsByTagName( "TextureReference" );

				TextureReference mainTextureReference = null;
				for( int i = 0; i < textureReferenceNodeList.getLength(); i++ ) {
					Element trNode = (Element)textureReferenceNodeList.item( i );
					TextureReference tr = new TextureReference( trNode );
					if( tr.isMain() ) {
						mainTextureReference = tr;
					}
					tr.setResourcePath( resourcePath );
					if( !tr.referencesExist() ) {
						throw new IOException( "FAILED TO LOCATE REFERENCE: " + tr.getName() );
					}
					this.textureMapReferences.add( tr );
				}

				if( mainTextureReference == null ) {
					int largestSize = 0;
					for( TextureReference reference : this.textureMapReferences ) {
						if( !reference.isSecondary() && ( reference.getAssetReferences().size() > largestSize ) ) {
							mainTextureReference = reference;
							largestSize = mainTextureReference.getAssetReferences().size();
						}
					}
					if( mainTextureReference != null ) {
						mainTextureReference.setIsMain( true );
					}
				}
				NodeList jointNodeList = doc.getElementsByTagName( "Joint" );
				jointParentList = new ArrayList<Tuple2<String, String>>();
				this.jointIDToNameMap = new HashMap<Short, String>();
				for( int i = 0; i < jointNodeList.getLength(); i++ ) {
					Element jointElement = (Element)jointNodeList.item( i );
					Element nameElement = XMLUtilities.getSingleChildElementByTagName( jointElement, "Name" );
					Element parentElement = XMLUtilities.getSingleChildElementByTagName( jointElement, "Parent" );
					Element referenceElement = XMLUtilities.getSingleChildElementByTagName( jointElement, "Reference" );
					if( ( nameElement != null ) && ( parentElement != null ) && ( referenceElement != null ) ) {
						String jointName = nameElement.getTextContent();
						String parentName = parentElement.getTextContent();
						if( parentName.equals( "-1" ) ) {
							parentName = null;
						}
						boolean shouldPreserveName = nameElement.hasAttribute( "preserve" );
						jointParentList.add( Tuple2.createInstance( jointName, parentName ) );
						Short referenceValue = Short.valueOf( referenceElement.getTextContent() );
						//We're working with maya joint names in this context--convert them to Alice joint names so we can use them for code generation later
						String aliceJointName = PipelineNamingUtilities.getAliceJointNameForMayaJointName( jointName, shouldPreserveName );
						this.jointIDToNameMap.put( referenceValue, aliceJointName );
					}
				}

				NodeList meshReferenceNodeList = doc.getElementsByTagName( "Mesh" );
				for( int i = 0; i < meshReferenceNodeList.getLength(); i++ ) {
					Element node = (Element)meshReferenceNodeList.item( i );
					MeshReference ref = new MeshReference( node );
					ref.setResourcePath( resourcePath );
					if( !ref.referenceExists() ) {
						throw new IOException( "FAILED TO LOCATE REFERENCE: " + ref.getName() );
					}
					this.meshReferences.add( ref );
				}
				NodeList skeletonReferenceNodeList = doc.getElementsByTagName( "Skeleton" );
				for( int i = 0; i < skeletonReferenceNodeList.getLength(); i++ ) {
					Element node = (Element)skeletonReferenceNodeList.item( i );
					SkeletonReference ref = new SkeletonReference( node );
					ref.setResourcePath( resourcePath );
					if( !ref.referenceExists() ) {
						throw new IOException( "FAILED TO LOCATE REFERENCE: " + ref.getName() );
					}
					this.skeletonReferences.add( ref );
				}

				NodeList posesNodeList = doc.getElementsByTagName( "Pose" );
				for( int i = 0; i < posesNodeList.getLength(); i++ ) {
					Element poseElement = (Element)posesNodeList.item( i );
					Pose pose = Pose.createPoseFromXMLElement( poseElement );

					if( pose.name.equals( Pose.DEFAULT_NAME ) ) {
						this.defaultPose = pose;
					} else {
						this.poses.add( pose );
					}
				}
			}
		} else {
			throw new IOException( "Can't find xml file " + xmlFile.getAbsolutePath() );
		}
	}

	public static void setEULAResource( URL eula ) {
		EULA_RESOURCE = eula;
		EULA_STRING = null;
	}

	public static void setEULAString( String eula ) {
		EULA_STRING = eula;
		EULA_RESOURCE = null;
	}

	private static final int BUFFER = 2048;
	private static byte data[] = new byte[ BUFFER ];

	public boolean hasXML() {
		return this.xmlFile != null;
	}

	public void setDefaultPose( Pose pose ) {
		this.defaultPose = pose;
	}

	public Pose getDefaultPose() {
		return this.defaultPose;
	}

	public void addPose( Pose pose ) {
		this.poses.add( pose );
	}

	public Pose getPose( String poseName ) {
		for( Pose pose : this.poses ) {
			if( pose.name.equals( poseName ) ) {
				return pose;
			}
		}
		return null;
	}

	public List<Pose> getPoses() {
		return this.poses;
	}

	public void removePose( Pose p ) {
		this.poses.remove( p );
	}

	//Removes joint refernces from poses that aren't part of the model's joint structure
	//  (the model may have had some joints removed via the spreadsheet data)
	//Also removes joints from all the non-default poses that match the default pose
	//  This makes poses act only on the joints that they specifically move and not affect the rest of the model
	public void minimizePoseData() {
		assert this.skeletonReferences.size() == 1;
		Map<String, Short> jointMap = this.jointNameMap.get( 0 );

		this.defaultPose.removeUnusedReferencesFromPose( jointMap );

		for( Pose pose : this.poses ) {
			pose.removeUnusedReferencesFromPose( jointMap );
			pose.removeMatchingOrientationsFromPose( this.defaultPose );
		}
	}

	private void writeStreamToZip( ZipOutputStream zip, String entryName, InputStream in ) throws IOException {
		zip.putNextEntry( new ZipEntry( entryName ) );
		int c;
		while( ( c = in.read( data, 0, BUFFER ) ) != -1 ) {
			zip.write( data, 0, c );
		}
		zip.closeEntry();
	}

	private void writeFileToZip( ZipOutputStream zip, File file ) throws IOException {
		FileInputStream in = new FileInputStream( file );
		writeStreamToZip( zip, file.getName(), in );
		in.close();
	}

	private void writeStringToZip( ZipOutputStream zip, String name, String data ) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream( data.getBytes() );
		writeStreamToZip( zip, name, bais );
		bais.close();
	}

	public static String convertStreamToString( InputStream is ) {
		String toReturn = null;
		if( is != null ) {
			Writer writer = new StringWriter();

			char[] buffer = new char[ 1024 ];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader( is, "UTF-8" ) );
				int n;
				while( ( n = reader.read( buffer ) ) != -1 ) {
					writer.write( buffer, 0, n );
				}
				toReturn = writer.toString();
			} catch( Exception e ) {
				toReturn = null;
			} finally {
				try {
					is.close();
				} catch( Exception e ) {
					toReturn = null;
				}
			}
		}
		return toReturn;
	}

	public TextureReference getMutableTextureReference() {
		for( TextureReference reference : this.textureMapReferences ) {
			if( reference.isMain() ) {
				return reference;
			}
		}
		return null;
	}

	public TextureReference getSecondaryTextureReference() {
		for( TextureReference reference : this.textureMapReferences ) {
			if( reference.isSecondary() ) {
				return reference;
			}
		}
		return null;
	}

	public boolean isRebuiltData() {
		return this.isRebuiltData;
	}

	public void setIsRebuiltData( boolean isRebuiltData ) {
		this.isRebuiltData = isRebuiltData;
	}

	public List<TextureReference> getTextureReferences() {
		return this.textureMapReferences;
	}

	public File getBundleFile( String outputPath ) {
		if( !outputPath.endsWith( "/" ) || outputPath.endsWith( "\\" ) ) {
			outputPath += File.separator;
		}
		return new File( outputPath + this.getFullAssetName( true ) + ".nebundle" );
	}

	public boolean bundleAlreadyExists( String outputPath ) {
		File outputFile = getBundleFile( outputPath );
		return outputFile.exists();
	}

	public void addExtraTextureAsset( ImageReference ar ) {
		this.extraTextureAssetReferences.add( ar );
	}

	public void addExtraTextureAsset( String name, File path ) {
		this.addExtraTextureAsset( new ImageReference( name, path.getAbsolutePath() ) );
	}

	public List<ImageReference> getExtraTextureReferences() {
		return this.extraTextureAssetReferences;
	}

	public void addDirectResourceRefenceAsset( AssetReference ar ) {
		this.enumMapReferences.add( ar );
	}

	public List<AssetReference> getEnumMapReferences() {
		return this.enumMapReferences;
	}

	public void addDirectResourceRefenceAsset( String name, File path ) {
		this.addDirectResourceRefenceAsset( new AssetReference( name, path.getAbsolutePath() ) );
	}

	public String getFullAssetName() {
		return getFullAssetName( false );
	}

	public String getFullAssetName( boolean condenseDuplicates ) {
		if( ( this.parentClassName != null ) && ( this.parentClassName.length() > 0 ) ) {
			if( condenseDuplicates && this.parentClassName.equals( this.assetName ) ) {
				return this.parentClassName;
			}
			return this.parentClassName + "_" + this.assetName;
		}
		return this.assetName;
	}

	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public AxisAlignedBox getAxisAlignedBoundingBox() {
		if( this.boundingBox != null ) {
			AxisAlignedBox aabb = new AxisAlignedBox( this.boundingBox.m_minimum, this.boundingBox.m_maximum );
			return aabb;
		}
		return null;
	}

	public String getBoundingBoxCode() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.boundingBox.getCodeString() + "\n\n" );
		for( Map.Entry<Short, BoundingBox> jointBox : this.jointToBoundingBox.entrySet() ) {
			sb.append( jointBox.getKey() + " : " + jointBox.getValue().getCodeString() + "\n" );
		}
		return sb.toString();
	}

	public void setAttribution( String attributionName, String attributionYear ) {
		this.attributionName = attributionName;
		this.attributionYear = attributionYear;
	}

	public String getAttributionName() {
		return this.attributionName;
	}

	public String getAttributionYear() {
		return this.attributionYear;
	}

	public void addToZipBundle( ZipOutputStream zipOutput, File xmlFile, File enumMapFile ) throws IOException {
		if( xmlFile != null ) {
			writeFileToZip( zipOutput, xmlFile );
		}
		if( enumMapFile != null ) {
			writeFileToZip( zipOutput, enumMapFile );
		}

		for( TextureReference reference : this.textureMapReferences ) {
			File[] files = reference.getAssetFiles();
			for( File f : files ) {
				writeFileToZip( zipOutput, f );
			}
		}
		for( AssetReference reference : this.extraTextureAssetReferences ) {
			writeFileToZip( zipOutput, reference.getAssetFile() );
		}
		for( AssetReference reference : this.meshReferences ) {
			writeFileToZip( zipOutput, reference.getAssetFile() );
		}
		for( AssetReference reference : this.skeletonReferences ) {
			writeFileToZip( zipOutput, reference.getAssetFile() );
		}
		for( AssetReference reference : this.enumMapReferences ) {
			writeFileToZip( zipOutput, reference.getAssetFile() );
		}
		if( EULA_STRING != null ) {
			writeStringToZip( zipOutput, "EULA_Alice3.txt", EULA_STRING );
		} else if( EULA_RESOURCE != null ) {
			writeStreamToZip( zipOutput, "EULA_Alice3.txt", EULA_RESOURCE.openStream() );
		} else {
			throw new IOException( "No EULA set on exporter. Failing." );
		}
	}

	public File createZipBundle( String outputPath, File xmlFile, File enumMapFile ) throws IOException {
		if( !outputPath.endsWith( "/" ) || outputPath.endsWith( "\\" ) ) {
			outputPath += File.separator;
		}
		File outputFile = this.getBundleFile( outputPath );
		FileUtilities.createParentDirectoriesIfNecessary( outputFile );
		FileOutputStream fileOutput = new FileOutputStream( outputFile );
		ZipOutputStream zipOutput = new ZipOutputStream( fileOutput );

		this.addToZipBundle( zipOutput, xmlFile, enumMapFile );

		zipOutput.flush();
		zipOutput.close();
		return outputFile;
	}

	public void setUsesAlphaTest( boolean usesAlphaTest ) {
		for( TextureReference tr : this.textureMapReferences ) {
			tr.setUsesAlphaTest( usesAlphaTest );
		}
		for( ImageReference ir : this.extraTextureAssetReferences ) {
			ir.setUsesAlphaTest( usesAlphaTest );
		}
	}

	public void setUsesAlphaBlend( boolean usesAlphaBlend ) {
		for( TextureReference tr : this.textureMapReferences ) {
			tr.setUsesAlphaBlend( usesAlphaBlend );
		}
		for( ImageReference ir : this.extraTextureAssetReferences ) {
			ir.setUsesAlphaBlend( usesAlphaBlend );
		}
	}

	public void setUsesAlphaBlend( String textureName, boolean usesAlphaBlend ) {
		for( TextureReference tr : this.textureMapReferences ) {
			if( tr.getName().equalsIgnoreCase( textureName ) || tr.hasMatchingFile( textureName ) ) {
				tr.setUsesAlphaBlend( usesAlphaBlend );
			}
		}
		for( ImageReference ir : this.extraTextureAssetReferences ) {
			if( ir.getName().equalsIgnoreCase( textureName ) ) {
				ir.setUsesAlphaBlend( usesAlphaBlend );
			}
		}
	}

	public void setUsesAlphaTest( String textureName, boolean usesAlphaTest ) {
		for( TextureReference tr : this.textureMapReferences ) {
			if( tr.getName().equalsIgnoreCase( textureName ) || tr.hasMatchingFile( textureName ) ) {
				tr.setUsesAlphaTest( usesAlphaTest );
			}
		}
		for( ImageReference ir : this.extraTextureAssetReferences ) {
			if( ir.getName().equalsIgnoreCase( textureName ) ) {
				ir.setUsesAlphaTest( usesAlphaTest );
			}
		}
	}

	public void setCullBackfaces( boolean cullBackfaces ) {
		for( MeshReference mr : this.meshReferences ) {
			mr.setCullBackfaces( cullBackfaces );
		}
	}

	public void setNamesToPreserve( List<String> namesToPreserve ) {
		this.namesToPreserve = namesToPreserve;
	}

	public boolean deleteResources() {
		File tempDirectory = this.rootPath;
		File[] files = tempDirectory.listFiles();
		boolean success = true;
		for( File file : files ) {
			if( !file.delete() ) {
				success = false;
			}
		}
		boolean deletedDir = tempDirectory.delete();
		if( !deletedDir ) {
			success = false;
		}
		return success;
	}

	public File createZipBundle( String outputPath ) throws IOException {
		return createZipBundle( outputPath, this.xmlFile, this.enumMapFile );
	}

	public void setRootPath( File rootPath ) {
		this.rootPath = rootPath;
	}

	public static Element createBoundingBoxElement( Document doc, BoundingBox bbox ) {
		Element bboxElement = doc.createElement( "BoundingBox" );

		Element minElement = doc.createElement( "Min" );
		minElement.setAttribute( "x", Double.toString( bbox.getMinimum().x ) );
		minElement.setAttribute( "y", Double.toString( bbox.getMinimum().y ) );
		minElement.setAttribute( "z", Double.toString( bbox.getMinimum().z ) );

		Element maxElement = doc.createElement( "Max" );
		maxElement.setAttribute( "x", Double.toString( bbox.getMaximum().x ) );
		maxElement.setAttribute( "y", Double.toString( bbox.getMaximum().y ) );
		maxElement.setAttribute( "z", Double.toString( bbox.getMaximum().z ) );

		bboxElement.appendChild( minElement );
		bboxElement.appendChild( maxElement );

		return bboxElement;
	}

	public Element createJointBoundingBoxElement( short jointRef, Document doc ) {
		if( this.jointToBoundingBox.containsKey( jointRef ) ) {
			BoundingBox bbox = this.jointToBoundingBox.get( jointRef );
			return createBoundingBoxElement( doc, bbox );
		}
		return null;
	}

	private String getParentForJoint( String jointName ) {
		if( this.jointParentList != null ) {
			for( Tuple2<String, String> entry : this.jointParentList ) {
				if( entry.getA().equalsIgnoreCase( jointName ) ) {
					if( entry.getB() == null ) {
						return "-1";
					}
					return entry.getB();
				}
			}
		}
		return "-1";
	}

	public Element createJointNameReferenceElement( Map<String, Short> map, List<String> namesToPreserve, Document doc ) {
		Element element = doc.createElement( "Joints" );
		for( Map.Entry<String, Short> entry : map.entrySet() ) {
			Element jointMapElement = doc.createElement( "Joint" );
			Element nameElement = doc.createElement( "Name" );
			nameElement.setTextContent( entry.getKey() );
			if( namesToPreserve != null ) {
				if( PipelineNamingUtilities.shouldPreserveName( entry.getKey(), namesToPreserve ) ) {
					nameElement.setAttribute( "preserve", "true" );
				}
			}
			Element referenceElement = doc.createElement( "Reference" );
			referenceElement.setTextContent( entry.getValue().toString() );
			jointMapElement.appendChild( nameElement );
			jointMapElement.appendChild( referenceElement );
			Element parentElement = doc.createElement( "Parent" );
			parentElement.setTextContent( getParentForJoint( entry.getKey() ) );
			jointMapElement.appendChild( parentElement );
			element.appendChild( jointMapElement );
			Element bboxElement = createJointBoundingBoxElement( entry.getValue(), doc );
			if( bboxElement != null ) {
				jointMapElement.appendChild( bboxElement );
			}
		}
		return element;
	}

	public String getRelativePathForAssetName( String assetName, boolean includeBaseAssetNameInPath ) {
		String basePath = includeBaseAssetNameInPath ? getFullAssetName() + "/" : "";
		if( assetName.equalsIgnoreCase( "mesh" ) ) {
			return basePath + "meshes/";
		} else if( assetName.equalsIgnoreCase( "skeleton" ) ) {
			return basePath + "skeletons/";
		} else if( assetName.equalsIgnoreCase( "texture" ) ) {
			return basePath + "textures/";
		}
		return basePath;

	}

	public String getRelativePathForAssetName( String assetName ) {
		return getRelativePathForAssetName( assetName, true );
	}

	public String getName() {
		return assetName;
	}

	public Document createXMLDocument( boolean absoluteReferences ) {
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element modelRoot = doc.createElement( "AliceModel" );
			modelRoot.setAttribute( "name", this.assetName );

			doc.appendChild( modelRoot );

			Element boundingBoxElement = createBoundingBoxElement( doc, this.boundingBox );
			modelRoot.appendChild( boundingBoxElement );

			Element meshes = doc.createElement( "Meshes" );
			for( AssetReference reference : this.meshReferences ) {
				Element meshElement;
				if( absoluteReferences ) {
					meshElement = reference.createAbsoluteXMLElement( "Mesh", doc );
				} else {
					meshElement = reference.createRelativeXMLElement( "Mesh", doc, getRelativePathForAssetName( "Mesh" ) );
				}
				System.out.print( "Looking for reference for " + reference.getName() + "..." );
				if( this.meshToTextureReferenceMap.containsKey( reference.getName() ) ) {
					for( Integer textureId : this.meshToTextureReferenceMap.get( reference.getName() ) ) {
						System.out.println( "Found '" + textureId + "'" );
						Element textureIDElement = doc.createElement( "TextureID" );
						textureIDElement.setTextContent( textureId.toString() );
						meshElement.appendChild( textureIDElement );
					}
				} else {
					System.out.println( "None found." );
				}
				meshes.appendChild( meshElement );
			}
			modelRoot.appendChild( meshes );

			Element textures = doc.createElement( "Textures" );
			for( TextureReference reference : this.textureMapReferences ) {
				if( absoluteReferences ) {
					textures.appendChild( reference.createAbsoluteXMLElement( "TextureReference", doc ) );
				} else {
					textures.appendChild( reference.createRelativeXMLElement( "TextureReference", doc, getRelativePathForAssetName( "Texture", false ) ) );
				}
			}
			modelRoot.appendChild( textures );

			if( this.skeletonReferences.size() > 0 ) {
				Element skeletons = doc.createElement( "Skeletons" );
				for( int i = 0; i < this.skeletonReferences.size(); i++ ) {
					SkeletonReference reference = this.skeletonReferences.get( i );
					Map<String, Short> jointMap = this.jointNameMap.get( i );

					Element skeletonElement;
					if( absoluteReferences ) {
						skeletonElement = reference.createAbsoluteXMLElement( "Skeleton", doc );
					} else {
						skeletonElement = reference.createRelativeXMLElement( "Skeleton", doc, getRelativePathForAssetName( "Skeleton" ) );
					}
					Element jointMapElement = createJointNameReferenceElement( jointMap, this.namesToPreserve, doc );
					skeletonElement.appendChild( jointMapElement );
					skeletons.appendChild( skeletonElement );
				}
				modelRoot.appendChild( skeletons );
			}

			if( !this.poses.isEmpty() ) {
				Element posesElement = doc.createElement( "Poses" );
				if( this.defaultPose != null ) {
					posesElement.appendChild( Pose.createPoseXMLElement( doc, this.defaultPose ) );
				}
				for( Pose pose : this.poses ) {
					posesElement.appendChild( Pose.createPoseXMLElement( doc, pose ) );
				}
				modelRoot.appendChild( posesElement );
			}

			return doc;
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public String createEnumMapString( boolean absoluteReferences ) {
		StringBuilder sb = new StringBuilder();
		for( AssetReference reference : this.enumMapReferences ) {

			String fileReference;
			if( absoluteReferences ) {
				fileReference = reference.getAbsoluteReference();
			} else {
				fileReference = reference.getRelativeResourceReference( getRelativePathForAssetName( "", false ) );
			}
			sb.append( reference.getName() + "|" + fileReference + "\n" );
		}
		return sb.toString();
	}

	public String createXMLString( boolean absoluteReferences ) {
		Document doc = this.createXMLDocument( absoluteReferences );
		if( doc != null ) {
			try {
				TransformerFactory transfac = TransformerFactory.newInstance();
				transfac.setAttribute( "indent-number", 4 );
				Transformer trans = transfac.newTransformer();
				//                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				trans.setOutputProperty( OutputKeys.INDENT, "yes" );

				//create string from xml tree
				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult( sw );
				DOMSource source = new DOMSource( doc );
				trans.transform( source, result );
				String xmlString = sw.toString();

				return xmlString;
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void writeEnumMapData( Writer writer, boolean absoluteReferences ) {
		String enumMapString = this.createEnumMapString( absoluteReferences );
		try {
			writer.write( enumMapString );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public void writeModelXML( Writer writer, boolean absoluteReferences ) {
		String xmlString = this.createXMLString( absoluteReferences );
		try {
			writer.write( xmlString );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public File getXMLFile() {
		return this.xmlFile;
	}

	public File getEnumMapFile() {
		return this.enumMapFile;
	}

	public String getXMLFileName() {
		return this.getFullAssetName() + ".xml";
	}

	public File writeAndSetModelXML( String outputPath, boolean absoluteReferences ) {
		File outputFile = new File( outputPath, getXMLFileName() );
		try {
			FileUtilities.createParentDirectoriesIfNecessary( outputFile );
			FileWriter fw = new FileWriter( outputFile );
			writeModelXML( fw, absoluteReferences );
			fw.close();
			this.xmlFile = outputFile;
			return this.xmlFile;
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public File writeAndSetEnumMapFile( String outputPath, boolean absoluteReferences ) {
		if( !this.enumMapReferences.isEmpty() ) {
			File outputFile = new File( outputPath, EnumsToTextureResources.MAP_FILE_NAME );
			try {
				FileWriter fw = new FileWriter( outputFile );
				writeEnumMapData( fw, absoluteReferences );
				fw.close();
				this.enumMapFile = outputFile;
				return this.enumMapFile;
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void addTextureMapReference( TextureReference reference ) {
		this.addReference( reference, textureMapReferences );
	}

	public void addTextureToReference( int referenceId, ImageReference texture ) {
		for( TextureReference reference : this.textureMapReferences ) {
			if( reference.getID() == referenceId ) {
				reference.addTexture( texture );
			}
		}
	}

	public void addTextureMapReference( String referenceName, String referenceAsset ) {
		TextureReference textureReference = new TextureReference( referenceName, this.textureMapReferences.size() );
		textureReference.addTexture( createImageReference( referenceAsset ) );
		this.addTextureMapReference( textureReference );
	}

	public void addMeshToTextureReference( String meshName, int textureReferenceID ) {
		System.out.println( "Adding reference: " + meshName + " -> " + textureReferenceID );
		if( meshToTextureReferenceMap.containsKey( meshName ) ) {
			List<Integer> references = meshToTextureReferenceMap.get( meshName );
			if( !references.contains( textureReferenceID ) ) {
				references.add( textureReferenceID );
			}
		} else {
			List<Integer> references = new ArrayList<Integer>();
			references.add( textureReferenceID );
			meshToTextureReferenceMap.put( meshName, references );
		}

	}

	public void addMeshReference( String reference ) {
		MeshReference ref = createMeshReference( reference );
		this.addReference( ref, meshReferences );
	}

	public void addMeshReference( MeshReference reference ) {
		this.addReference( reference, meshReferences );
	}

	public void addMeshReference( String referenceName, String referenceAsset ) {
		MeshReference ref = createMeshReference( referenceName, referenceAsset );
		this.addReference( ref, meshReferences );
	}

	public List<MeshReference> getMeshReferences() {
		return this.meshReferences;
	}

	private SkeletonReference createSkeletonReference( String referenceName, String referenceAsset ) {
		return new SkeletonReference( referenceName, referenceAsset );
	}

	private SkeletonReference createSkeletonReference( String referenceName, String referenceAsset, boolean hasDifferentBindpose ) {
		return new SkeletonReference( referenceName, referenceAsset, hasDifferentBindpose );
	}

	//    public void addSkeletonReference(String reference, Map<String, Short> referenceMap) {
	//        this.addReference(reference, skeletonReferences);
	//        this.jointNameMap.add(referenceMap);
	//    }

	//    public void addSkeletonReference(AssetReference reference, Map<String, Short> referenceMap) {
	//        this.addReference(reference, skeletonReferences);
	//        this.jointNameMap.add(referenceMap);
	//    }

	public void addSkeletonReference( String referenceName, String referenceAsset, boolean hasDifferentBindPose, Map<String, Short> referenceMap ) {
		SkeletonReference sr = createSkeletonReference( referenceName, referenceAsset, hasDifferentBindPose );
		this.addReference( sr, skeletonReferences );
		this.jointNameMap.add( referenceMap );
	}

	public List<SkeletonReference> getSkeletonReferences() {
		return this.skeletonReferences;
	}

	public void setJointParentList( List<Tuple2<String, String>> jointParentList ) {
		this.jointParentList = jointParentList;
	}

	public void setJointIDToNameMap( Map<Short, String> referenceMap ) {
		this.jointIDToNameMap = referenceMap;
	}

	public Map<Short, String> getJointIDToNameMap() {
		return this.jointIDToNameMap;
	}

	public List<Tuple2<String, String>> getJointParentList() {
		return this.jointParentList;
	}

	public void resetBoundingBox() {
		this.boundingBox = new BoundingBox();
	}

	public void addPointsToBoundingBox( float[] points ) {
		System.out.println( "Adding points to bounding box: " + this.boundingBox );
		for( int i = 0; i < ( points.length / 3 ); i++ ) {
			System.out.println( "  Adding (" + points[ i * 3 ] + ", " + points[ ( i * 3 ) + 1 ] + ", " + points[ ( i * 3 ) + 2 ] + ")" );
			this.boundingBox.union( points[ i * 3 ], points[ ( i * 3 ) + 1 ], points[ ( i * 3 ) + 2 ] );
		}
		System.out.println( "Box is now: " + this.boundingBox );
		System.out.println();
	}

	public void translateBoundingBox( double x, double y, double z ) {
		this.boundingBox.setMaximum( this.boundingBox.getMaximum().x + x, this.boundingBox.getMaximum().y + y, this.boundingBox.getMaximum().y + y );
		this.boundingBox.setMinimum( this.boundingBox.getMinimum().x + x, this.boundingBox.getMinimum().y + y, this.boundingBox.getMinimum().y + y );
	}

	public void addBoundingBoxForJoint( short jointReference, float[] min, float[] max ) {
		if( this.jointToBoundingBox.containsKey( jointReference ) ) {
			BoundingBox bbox = this.jointToBoundingBox.get( jointReference );
			bbox.union( min[ 0 ], min[ 1 ], min[ 2 ] );
			bbox.union( max[ 0 ], max[ 1 ], max[ 2 ] );
		} else {
			BoundingBox bbox = new BoundingBox();
			bbox.union( min[ 0 ], min[ 1 ], min[ 2 ] );
			bbox.union( max[ 0 ], max[ 1 ], max[ 2 ] );
			this.jointToBoundingBox.put( jointReference, bbox );
		}
	}

	private void addReference( AssetReference reference, List<AssetReference> referenceList ) {
		for( AssetReference ref : referenceList ) {
			if( ref.getAssetFile().equals( reference.getAssetFile() ) ) {
				return;
			}
		}
		referenceList.add( reference );
	}

	private void addReference( MeshReference reference, List<MeshReference> referenceList ) {
		for( MeshReference ref : referenceList ) {
			if( ref.getAssetFile().equals( reference.getAssetFile() ) ) {
				return;
			}
		}
		referenceList.add( reference );
	}

	private void addReference( SkeletonReference reference, List<SkeletonReference> referenceList ) {
		for( SkeletonReference ref : referenceList ) {
			if( ref.getAssetFile().equals( reference.getAssetFile() ) ) {
				return;
			}
		}
		referenceList.add( reference );
	}

	private void addReference( TextureReference reference, List<TextureReference> referenceList ) {
		for( TextureReference ref : referenceList ) {
			if( ref.matches( reference ) ) {
				return;
			}
		}
		referenceList.add( reference );
	}

	private MeshReference createMeshReference( String referenceName, String referenceAsset ) {
		return new MeshReference( referenceName, referenceAsset );
	}

	private MeshReference createMeshReference( String referenceAsset ) {
		return createMeshReference( AssetReference.getAssetName( referenceAsset ), referenceAsset );
	}

	private ImageReference createImageReference( String referenceName, String referenceAsset ) {
		return new ImageReference( referenceName, referenceAsset );
	}

	private ImageReference createImageReference( String referenceAsset ) {
		return createImageReference( AssetReference.getAssetName( referenceAsset ), referenceAsset );
	}

	private AssetReference createReference( String referenceName, String referenceAsset ) {
		return new AssetReference( referenceName, referenceAsset );
	}

	private AssetReference createReference( String referenceAsset ) {
		return createReference( AssetReference.getAssetName( referenceAsset ), referenceAsset );
	}

	private void addReference( String referenceName, String referenceAsset, List<AssetReference> referenceList ) {
		addReference( createReference( referenceName, referenceAsset ), referenceList );
	}

	private void addReference( String reference, List<AssetReference> referenceList ) {
		addReference( createReference( reference ), referenceList );
	}

	private void setReferences( Collection<String> references, List<AssetReference> referenceList ) {
		referenceList.clear();
		for( String reference : references ) {
			addReference( reference, referenceList );
		}
	}

	public void setReferences( String[] references, List<AssetReference> referenceList ) {
		referenceList.clear();
		for( String reference : references ) {
			addReference( reference, referenceList );
		}
	}
}

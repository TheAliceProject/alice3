/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lgna.story.resourceutilities.exporterutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.cs.dennisc.java.io.FileUtilities;

/**
 *
 * @author alice
 */
public class TextureReference {
	public static String USES_ALPHA_TEST_KEY = "usesAlphaTest";
	public static String USES_ALPHA_BLEND_KEY = "usesAlphaBlend";
	public static String MATERIAL_TYPE_KEY = "materialType";
	public static String IS_SECONDARY_KEY = "isSecondary";
	public static String IS_MAIN_KEY = "isMain";

	private List<ImageReference> fileReferences = new ArrayList<ImageReference>();
	private String name;
	private int id;
	private String subPath;
	private String materialType = "UNKNOWN";
	private boolean usesAlphaTest = false;
	private boolean usesAlphaBlend = false;
	private boolean isSecondary = false;
	private boolean isMain = false;

	public TextureReference( String name, int id ) {
		this.name = name;
		this.id = id;
	}

	public TextureReference( String name, int id, String materialType ) {
		this.name = name;
		this.id = id;
		this.materialType = materialType;
	}

	public TextureReference( int id, String imageName, File imageFile ) {
		this( id );
		fileReferences.add( new ImageReference( imageName, imageFile.getAbsolutePath() ) );
	}

	public TextureReference( int id, String imageName, String imageFile ) {
		this( id );
		fileReferences.add( new ImageReference( imageName, imageFile ) );
	}

	public TextureReference( int id, String imageName, String subPath, File imageFile ) {
		this( id, imageName, imageFile );
		this.subPath = subPath;
	}

	public TextureReference( int id, String imageName, String subPath, String imageFile ) {
		this( id, imageName, imageFile );
		this.subPath = subPath;
	}

	public TextureReference( int id ) {
		this( "NO_NAME", id );
	}

	public TextureReference( String name ) {
		this( name, -1 );
	}

	public TextureReference() {
		this( "NO_NAME" );
	}

	public TextureReference( Element element ) {
		this.name = element.getAttribute( "name" );
		this.id = Integer.parseInt( element.getAttribute( "id" ) );
		if( element.hasAttribute( TextureReference.USES_ALPHA_TEST_KEY ) ) {
			boolean alphaTest = element.getAttribute( TextureReference.USES_ALPHA_TEST_KEY ).equalsIgnoreCase( "true" );
			this.setUsesAlphaTest( alphaTest );
		}
		if( element.hasAttribute( TextureReference.USES_ALPHA_BLEND_KEY ) ) {
			boolean alphaBlend = element.getAttribute( TextureReference.USES_ALPHA_BLEND_KEY ).equalsIgnoreCase( "true" );
			this.setUsesAlphaBlend( alphaBlend );
		}
		if( element.hasAttribute( TextureReference.MATERIAL_TYPE_KEY ) ) {
			String materialTypeVal = element.getAttribute( TextureReference.MATERIAL_TYPE_KEY );
			this.setMaterialType( materialTypeVal );
		}
		if( element.hasAttribute( TextureReference.IS_SECONDARY_KEY ) ) {
			boolean isSecondaryVal = element.getAttribute( TextureReference.IS_SECONDARY_KEY ).equalsIgnoreCase( "true" );
			this.setIsSecondary( isSecondaryVal );
		}
		if( element.hasAttribute( TextureReference.IS_MAIN_KEY ) ) {
			boolean isMainVal = element.getAttribute( TextureReference.IS_MAIN_KEY ).equalsIgnoreCase( "true" );
			this.setIsMain( isMainVal );
		}
		NodeList textureNodeList = element.getElementsByTagName( "Texture" );
		for( int i = 0; i < textureNodeList.getLength(); i++ ) {
			Element e = (Element)textureNodeList.item( i );
			this.addTexture( new ImageReference( e ) );
		}
	}

	public void setIsSecondary( boolean isSecondary ) {
		this.isSecondary = isSecondary;
	}

	public boolean isSecondary() {
		return this.isSecondary;
	}

	public void setIsMain( boolean isMain ) {
		this.isMain = isMain;
	}

	public boolean isMain() {
		return this.isMain;
	}

	public String getMaterialType() {
		return this.materialType;
	}

	public void setMaterialType( String materialType ) {
		this.materialType = materialType;
	}

	public String getName() {
		return this.name;
	}

	public String getSubPath() {
		return this.subPath;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public void setUsesAlphaTest( boolean usesAlphaTest ) {
		this.usesAlphaTest = usesAlphaTest;
	}

	public boolean usesAlphaTest() {
		return this.usesAlphaTest;
	}

	public void setUsesAlphaBlend( boolean usesAlphaBlend ) {
		this.usesAlphaBlend = usesAlphaBlend;
	}

	public void setUsesAlphaBlend( String textureName, boolean usesAlphaBlend ) {
		if( this.getName().equalsIgnoreCase( textureName ) ) {
			this.setUsesAlphaBlend( usesAlphaBlend );
		}
	}

	public void setUsesAlphaTest( String textureName, boolean usesAlphaTest ) {
		if( this.getName().equalsIgnoreCase( textureName ) ) {
			this.setUsesAlphaTest( usesAlphaTest );
		}
	}

	public boolean usesAlphaBlend() {
		return this.usesAlphaBlend;
	}

	public int getID() {
		return this.id;
	}

	public void setID( int id ) {
		this.id = id;
	}

	public void addTexture( String fileName ) {
		String name = AssetReference.getAssetName( fileName );
		File file = new File( fileName );
		addTexture( name, file );
	}

	public void addTexture( String name, File file ) {
		ImageReference ref = new ImageReference( name, file.getAbsolutePath() );
		this.addTexture( ref );
	}

	public void addTexture( ImageReference texture ) {
		for( ImageReference ref : this.fileReferences ) {
			if( ref.getAssetFile().equals( texture.getAssetFile() ) ) {
				return;
			}
		}
		this.fileReferences.add( texture );
	}

	//    public void cullUnwantedTextures(PipelineData data) {
	//    	List<AssetReference> toRemove = new ArrayList<AssetReference>();
	//    	for (AssetReference ref : this.fileReferences)
	//        {
	//    		String baseName = FileUtilities.getBaseName(ref.getAssetFile());
	//    		String javaName = ConvertMayaModel.getJavaReadyTextureName(baseName);
	//    		if (data.shouldSkip(baseName) || data.shouldSkip(javaName)) {
	//    			toRemove.add(ref);
	//    		}
	//        }
	//    	for (AssetReference ref : toRemove) {
	//    		this.fileReferences.remove(ref);
	//    	}
	//    }

	//    public void cullMissingTextures(List<PipelineData> data) {
	//    	List<ImageReference> foundReferences = new ArrayList<ImageReference>();
	//    	
	//		for (PipelineData pd : data) {
	//			String baseToKeep = FileUtilities.getBaseName(pd.resourceFile);
	//			for (ImageReference ref : this.fileReferences)
	//	        {
	//	    		String baseName = FileUtilities.getBaseName(ref.getAssetFile());
	//	    		String javaName = ConvertMayaModel.getJavaReadyTextureName(JavaAliceModel.getAliceTextureName(baseName));
	//				if (baseToKeep.equalsIgnoreCase(baseName) || baseToKeep.equalsIgnoreCase(javaName)) {
	//					foundReferences.add(ref);
	//					break;
	//				}
	//	        }
	//        }
	//    	if (foundReferences.size() > 0) {
	//    		this.fileReferences = foundReferences;
	//    	}
	//    }
	//    
	public boolean matches( TextureReference other ) {
		boolean namesMatch = this.name.equalsIgnoreCase( other.name );
		boolean idsMatch = this.id == other.id;
		if( namesMatch != idsMatch ) {
			System.out.println( this.name + ", " + this.id + " != " + other.name + ", " + other.id );
		}
		return ( this.name.equals( other.name ) && ( this.id == other.id ) );
	}

	public boolean containsFile( File file ) {
		for( AssetReference ref : this.fileReferences ) {
			if( ref.getAssetFile().equals( file ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean containsFileName( String fileName ) {
		return this.containsFile( new File( fileName ) );
	}

	public boolean hasMatchingFile( String fileName ) {
		String baseToCheck = FileUtilities.getBaseName( fileName );
		for( AssetReference ref : this.fileReferences ) {
			String baseName = FileUtilities.getBaseName( ref.getAssetFile() );
			if( baseName.equalsIgnoreCase( baseToCheck ) ) {
				return true;
			}
		}
		return false;
	}

	public File[] getAssetFiles() {
		File[] files = new File[ this.fileReferences.size() ];
		for( int i = 0; i < this.fileReferences.size(); i++ ) {
			files[ i ] = this.fileReferences.get( i ).getAssetFile();
		}
		return files;
	}

	public List<ImageReference> getAssetReferences() {
		return this.fileReferences;
	}

	public List<File> getOutOfDirectoryFiles( String dirPath ) {
		ArrayList<File> outOfDirFiles = new ArrayList<File>();
		File dirFile = new File( dirPath );
		for( File f : getAssetFiles() ) {
			if( !FileUtilities.isDescendantOf( f, dirFile ) ) {
				outOfDirFiles.add( f );
			}
		}
		return outOfDirFiles;
	}

	private void setAttributes( Element element ) {
		element.setAttribute( "name", this.name );
		element.setAttribute( "id", Integer.toString( this.id ) );
		element.setAttribute( MATERIAL_TYPE_KEY, this.materialType );
		if( this.usesAlphaTest ) {
			element.setAttribute( USES_ALPHA_TEST_KEY, "true" );
		}
		if( this.usesAlphaBlend ) {
			element.setAttribute( USES_ALPHA_BLEND_KEY, "true" );
		}
		if( this.isSecondary ) {
			element.setAttribute( IS_SECONDARY_KEY, "true" );
		}
		if( this.isMain ) {
			element.setAttribute( IS_MAIN_KEY, "true" );
		}
	}

	public Element createAbsoluteXMLElement( String elementName, Document doc ) {
		Element element = doc.createElement( elementName );
		setAttributes( element );
		for( AssetReference ref : this.fileReferences ) {
			element.appendChild( ref.createAbsoluteXMLElement( "Texture", doc ) );
		}
		return element;
	}

	public Element createRelativeXMLElement( String elementName, Document doc, String relativePath ) {
		Element element = doc.createElement( elementName );
		setAttributes( element );
		for( AssetReference ref : this.fileReferences ) {
			element.appendChild( ref.createRelativeXMLElement( "Texture", doc, relativePath ) );
		}
		return element;
	}

	public void setResourcePath( File resourcePath ) {
		for( AssetReference ref : this.fileReferences ) {
			ref.setResourcePath( resourcePath );
		}
	}

	public boolean referencesExist() {
		for( AssetReference ref : this.fileReferences ) {
			if( !ref.referenceExists() ) {
				return false;
			}
		}
		return true;
	}
}

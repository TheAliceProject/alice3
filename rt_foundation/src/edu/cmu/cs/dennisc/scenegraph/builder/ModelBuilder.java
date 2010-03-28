package edu.cmu.cs.dennisc.scenegraph.builder;

public class ModelBuilder {
	private ModelPart root;
	private java.util.Set< edu.cmu.cs.dennisc.scenegraph.Geometry > geometries;
	private java.util.Set< edu.cmu.cs.dennisc.texture.BufferedImageTexture > textures;

	private static final String MAIN_ENTRY_PATH = "main.bin";
	private static final String INDEXED_TRIANGLE_ARRAY_PREFIX = "indexedTriangleArrays/";
	private static final String MESH_PREFIX = "meshes/";
	private static final String GEOMETRY_POSTFIX = ".bin";
	private static final String BUFFERED_IMAGE_TEXTURE_PREFIX = "bufferedImageTextures/";
	private static final String BUFFERED_IMAGE_TEXTURE_POSTFIX = ".png";

	private ModelBuilder() {
	}

	private static void safeEncode( edu.cmu.cs.dennisc.codec.BinaryEncoder encoder, short[] array ) {
		boolean isNotNull = array != null;
		encoder.encode( isNotNull );
		if( isNotNull ) {
			encoder.encode( array );
		}
	}
	private static short[] safeDecodeShortArray( edu.cmu.cs.dennisc.codec.BinaryDecoder decoder ) {
		boolean isNotNull = decoder.decodeBoolean();
		short[] rv;
		if( isNotNull ) {
			rv = decoder.decodeShortArray();
		} else {
			rv = null;
		}
		return rv;
	}

	private static java.util.Map< java.io.File, ModelBuilder > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public java.util.Set< edu.cmu.cs.dennisc.scenegraph.Geometry > getGeometries() {
		return this.geometries;
	}
	public void replaceGeometries( java.util.Map< edu.cmu.cs.dennisc.scenegraph.Geometry, edu.cmu.cs.dennisc.scenegraph.Geometry > map ) {
		this.geometries.clear();
		this.geometries.addAll( map.values() );
		this.root.replaceGeometries( map );
	}
	public static void forget( java.io.File file ) {
		map.remove( file );
	}
	public static ModelBuilder getInstance( java.io.File file ) throws java.io.IOException {
		ModelBuilder rv = map.get( file );
		if( rv != null ) {
			//pass
		} else {
			rv = new ModelBuilder();
			try {
				java.util.Map< Integer, edu.cmu.cs.dennisc.scenegraph.Geometry > mapIdToGeometry = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
				java.util.Map< Integer, edu.cmu.cs.dennisc.texture.BufferedImageTexture > mapIdToTexture = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

				java.io.FileInputStream fis = new java.io.FileInputStream( file );
				java.util.Map< String, byte[] > map = edu.cmu.cs.dennisc.zip.ZipUtilities.extract( fis );

				java.io.InputStream isMainEntry = null;
				for( String entryPath : map.keySet() ) {
					java.io.InputStream is = new java.io.ByteArrayInputStream( map.get( entryPath ) );
					if( entryPath.startsWith( INDEXED_TRIANGLE_ARRAY_PREFIX ) ) {
						String s = entryPath.substring( INDEXED_TRIANGLE_ARRAY_PREFIX.length(), entryPath.length() - GEOMETRY_POSTFIX.length() );
						int id = Integer.parseInt( s );
						edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
						edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ita = new edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray();
						ita.vertices.setValue( decoder.decodeBinaryEncodableAndDecodableArray( edu.cmu.cs.dennisc.scenegraph.Vertex.class ) );
						ita.polygonData.setValue( decoder.decodeIntArray() );
						mapIdToGeometry.put( id, ita );

					} else if( entryPath.startsWith( MESH_PREFIX ) ) {
						String s = entryPath.substring( MESH_PREFIX.length(), entryPath.length() - GEOMETRY_POSTFIX.length() );
						int id = Integer.parseInt( s );
						edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( is );
						edu.cmu.cs.dennisc.scenegraph.Mesh mesh = new edu.cmu.cs.dennisc.scenegraph.Mesh();
						mesh.xyzs.setValue( decoder.decodeDoubleArray() );
						mesh.ijks.setValue( decoder.decodeFloatArray() );
						mesh.uvs.setValue( decoder.decodeFloatArray() );
						mesh.xyzTriangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mesh.ijkTriangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mesh.uvTriangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mesh.xyzQuadrangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mesh.ijkQuadrangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mesh.uvQuadrangleIndices.setValue( safeDecodeShortArray( decoder ) );
						mapIdToGeometry.put( id, mesh );
					} else if( entryPath.startsWith( BUFFERED_IMAGE_TEXTURE_PREFIX ) ) {
						String s = entryPath.substring( BUFFERED_IMAGE_TEXTURE_PREFIX.length(), entryPath.length() - BUFFERED_IMAGE_TEXTURE_POSTFIX.length() );

						boolean isPotentiallyAlphaBlended = s.charAt( 0 ) == 't';
						int id = Integer.parseInt( s.substring( 1 ) );

						java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageUtilities.read( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is );
						edu.cmu.cs.dennisc.texture.BufferedImageTexture texture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
						texture.setBufferedImage( bufferedImage );
						texture.setPotentiallyAlphaBlended( isPotentiallyAlphaBlended );
						mapIdToTexture.put( id, texture );
					} else {
						assert entryPath.equals( MAIN_ENTRY_PATH ) : entryPath;
						isMainEntry = is;
					}
				}
				edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( isMainEntry );
				rv.root = decoder.decodeBinaryEncodableAndDecodable( ModelPart.class );
				rv.root.resolve( mapIdToGeometry, mapIdToTexture );

				rv.geometries = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( mapIdToGeometry.values() );
				rv.textures = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( mapIdToTexture.values() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( file.toString(), ioe );
			}
			map.put( file, rv );
		}
		return rv;
	}
	
	public void encode( java.io.File file ) throws java.io.IOException {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );

		java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
		for( final edu.cmu.cs.dennisc.scenegraph.Geometry geometry : this.geometries ) {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return getEntryPath( geometry );
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( os );
					if( geometry instanceof edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ) {
						edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ita = (edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray)geometry;
						encoder.encode( ita.vertices.getValue() );
						encoder.encode( ita.polygonData.getValue() );
					} else if( geometry instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
						edu.cmu.cs.dennisc.scenegraph.Mesh mesh = (edu.cmu.cs.dennisc.scenegraph.Mesh)geometry;
						encoder.encode( mesh.xyzs.getValue() );
						encoder.encode( mesh.ijks.getValue() );
						encoder.encode( mesh.uvs.getValue() );
						safeEncode( encoder, mesh.xyzTriangleIndices.getValue() );
						safeEncode( encoder, mesh.ijkTriangleIndices.getValue() );
						safeEncode( encoder, mesh.uvTriangleIndices.getValue() );
						safeEncode( encoder, mesh.xyzQuadrangleIndices.getValue() );
						safeEncode( encoder, mesh.ijkQuadrangleIndices.getValue() );
						safeEncode( encoder, mesh.uvQuadrangleIndices.getValue() );
					} else {
						assert false;
					}
					encoder.flush();
				}
			} );
		}
		for( final edu.cmu.cs.dennisc.texture.BufferedImageTexture texture : this.textures ) {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return getEntryPath( texture );
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, os, texture.getBufferedImage() );
				}
			} );
		}
		edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
			public String getName() {
				return MAIN_ENTRY_PATH;
			}
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( os );
				encoder.encode( root );
				encoder.flush();
			}
		} );
		zos.flush();
		zos.close();
	}

	public static ModelBuilder newInstance( edu.cmu.cs.dennisc.scenegraph.Transformable transformable ) {
		ModelBuilder rv = new ModelBuilder();
		rv.geometries = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		rv.textures = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		rv.root = ModelPart.newInstance( transformable, rv.geometries, rv.textures );
		return rv;
	}
	public edu.cmu.cs.dennisc.scenegraph.Transformable buildTransformable() {
		edu.cmu.cs.dennisc.scenegraph.Transformable rv = this.root.build();
		return rv;
	}

	private static String getEntryPath( edu.cmu.cs.dennisc.scenegraph.Geometry geometry ) {
		if( geometry instanceof edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ) {
			return INDEXED_TRIANGLE_ARRAY_PREFIX + geometry.hashCode() + GEOMETRY_POSTFIX;
		} else if( geometry instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
			return MESH_PREFIX + geometry.hashCode() + GEOMETRY_POSTFIX;
		} else {
			return null;
		}
	}
	private static String getEntryPath( edu.cmu.cs.dennisc.texture.Texture texture ) {
		if( texture instanceof edu.cmu.cs.dennisc.texture.BufferedImageTexture ) {
			edu.cmu.cs.dennisc.texture.BufferedImageTexture bufferedImageTexture = (edu.cmu.cs.dennisc.texture.BufferedImageTexture)texture;
			char c;
			if( bufferedImageTexture.isPotentiallyAlphaBlended() ) {
				c = 't';
			} else {
				c = 'f';
			}
			return BUFFERED_IMAGE_TEXTURE_PREFIX + c + texture.hashCode() + ".png";
		} else {
			return null;
		}
	}
}

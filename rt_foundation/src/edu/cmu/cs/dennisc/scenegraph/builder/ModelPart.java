package edu.cmu.cs.dennisc.scenegraph.builder;

public class ModelPart implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private String name;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTransformation;
	private edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray geometry;
	private edu.cmu.cs.dennisc.texture.BufferedImageTexture texture;
	private java.util.List< ModelPart > children;

	private Integer geometryID = null;
	private Integer textureID = null;

	private ModelPart() {
	}
	public ModelPart( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.decode( binaryDecoder );
	}
	public static ModelPart newInstance( edu.cmu.cs.dennisc.scenegraph.Transformable parent, java.util.Set< edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray > geometries, java.util.Set< edu.cmu.cs.dennisc.texture.BufferedImageTexture > textures ) {
		ModelPart rv = new ModelPart();
		rv.name = parent.getName();
		rv.localTransformation = parent.localTransformation.getValue();
		rv.children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		for( edu.cmu.cs.dennisc.scenegraph.Component component : parent.accessComponents() ) {
			if( component instanceof edu.cmu.cs.dennisc.scenegraph.Visual ) {
				edu.cmu.cs.dennisc.scenegraph.Visual visual = (edu.cmu.cs.dennisc.scenegraph.Visual)component;
				edu.cmu.cs.dennisc.scenegraph.Appearance front = visual.frontFacingAppearance.getValue();
				if( front instanceof edu.cmu.cs.dennisc.scenegraph.SingleAppearance ) {
					edu.cmu.cs.dennisc.scenegraph.SingleAppearance singleAppearance = (edu.cmu.cs.dennisc.scenegraph.SingleAppearance)front;
					edu.cmu.cs.dennisc.texture.Texture texture = singleAppearance.diffuseColorTexture.getValue();
					if( texture != null ) {
						if( texture instanceof edu.cmu.cs.dennisc.texture.BufferedImageTexture ) {
							rv.texture = (edu.cmu.cs.dennisc.texture.BufferedImageTexture)texture;
							textures.add( rv.texture );
						} else {
							assert false;
						}
					} else {
						System.err.println( "warning: no texture for " + rv.name ); 
					}
				} else {
					assert false;
				}
				edu.cmu.cs.dennisc.math.Matrix3x3 scale = visual.scale.getValue();
				edu.cmu.cs.dennisc.scenegraph.Geometry geometry = visual.getGeometry();
				if( geometry != null ) {
					if( geometry instanceof edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ) {
						rv.geometry = (edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray)geometry;
						
						if( scale.isIdentity() ) {
							//pass
						} else {
//							System.err.println( "fixing scale for: " + rv.name + " " + scale.right.x + " " + scale.up.y + " " + scale.backward.z );
							for( edu.cmu.cs.dennisc.scenegraph.Vertex v : rv.geometry.vertices.getValue() ) {
								v.position.x *= scale.right.x;
								v.position.y *= scale.up.y;
								v.position.z *= scale.backward.z;
							}
						}
					} else {
						assert false;
					}
					geometries.add( rv.geometry );
				} else {
					System.err.println( "warning: no geometry for " + rv.name ); 
				}
			} else if( component instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
				edu.cmu.cs.dennisc.scenegraph.Transformable transformable = (edu.cmu.cs.dennisc.scenegraph.Transformable)component;
				rv.children.add( newInstance( transformable, geometries, textures ) );
			}
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.scenegraph.Transformable build() {
		edu.cmu.cs.dennisc.scenegraph.Transformable rv = new edu.cmu.cs.dennisc.scenegraph.Transformable();
		edu.cmu.cs.dennisc.scenegraph.Visual visual = new edu.cmu.cs.dennisc.scenegraph.Visual();
		edu.cmu.cs.dennisc.scenegraph.SingleAppearance appearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
		appearance.setDiffuseColorTexture( this.texture );
		visual.frontFacingAppearance.setValue( appearance );
		assert this.geometry != null;
		
		visual.setGeometry( this.geometry );

		rv.addComponent( visual );
		
		rv.localTransformation.setValue( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( this.localTransformation ) );
		for( ModelPart child : this.children ) {
			rv.addComponent( child.build() );
		}
		return rv;
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.name = binaryDecoder.decodeString();
		this.localTransformation = binaryDecoder.decodeBinaryEncodableAndDecodable( edu.cmu.cs.dennisc.math.AffineMatrix4x4.class );
		this.geometryID = binaryDecoder.decodeInt();
		this.textureID = binaryDecoder.decodeInt();
		final int N = binaryDecoder.decodeInt();
		java.util.ArrayList< ModelPart > arrayList = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		arrayList.ensureCapacity( N );
		for( int i = 0; i < N; i++ ) {
			arrayList.add( binaryDecoder.decodeBinaryEncodableAndDecodable( ModelPart.class ) );
		}
		this.children = arrayList;
	}
	public void resolve( java.util.Map< Integer, edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray > mapIdToGeometry, java.util.Map< Integer, edu.cmu.cs.dennisc.texture.BufferedImageTexture > mapIdToTexture ) {
		if( this.geometryID != 0 ) {
			assert mapIdToGeometry.containsKey( this.geometryID ) : geometryID;
			this.geometry = mapIdToGeometry.get( this.geometryID );
			assert this.geometry != null : this.name;
		}
		if( this.textureID != 0 ) {
			assert mapIdToTexture.containsKey( this.textureID ) : textureID;
			this.texture = mapIdToTexture.get( this.textureID );
			assert this.texture != null : this.name;
		}

		this.geometryID = null;
		this.textureID = null;
		for( ModelPart child : this.children ) {
			child.resolve( mapIdToGeometry, mapIdToTexture );
		}
	}
	
	private static int getID( Object o ) {
		int rv;
		if( o != null ) {
			rv = o.hashCode();
			assert rv != 0;
		} else {
			rv = 0;
		}
		return rv;
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.name );
		binaryEncoder.encode( this.localTransformation );
		binaryEncoder.encode( getID( this.geometry ) );
		binaryEncoder.encode( getID( this.texture ) );
		binaryEncoder.encode( this.children.size() );
		for( ModelPart child : this.children ) {
			binaryEncoder.encode( child );
		}
	}
}

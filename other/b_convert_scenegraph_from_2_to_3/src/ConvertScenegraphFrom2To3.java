/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
interface Handler< O, N > {
	public N convert( O oldValue );
}

/**
 * @author Dennis Cosgrove
 */
class PassThroughHandler< E > implements Handler< E, E > {
	public E convert(E oldValue) {
		return oldValue;
	}
}

/**
 * @author Dennis Cosgrove
 */
class AngleHandler implements Handler< Double, edu.cmu.cs.dennisc.math.Angle > {
	public edu.cmu.cs.dennisc.math.Angle convert(Double oldValue) {
		return new edu.cmu.cs.dennisc.math.AngleInRevolutions( oldValue );
	}
}

/**
 * @author Dennis Cosgrove
 */
class ColorHandler implements Handler< edu.cmu.cs.stage3.alice.scenegraph.Color, edu.cmu.cs.dennisc.color.Color4f > {
	public edu.cmu.cs.dennisc.color.Color4f convert(edu.cmu.cs.stage3.alice.scenegraph.Color oldColor) {
		if( Float.isNaN( oldColor.red ) ) {
			return null;
		} else {
			return new edu.cmu.cs.dennisc.color.Color4f( oldColor.red, oldColor.green, oldColor.blue, oldColor.alpha );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class AffineMatrix4x4Handler implements Handler< javax.vecmath.Matrix4d, edu.cmu.cs.dennisc.math.AffineMatrix4x4 > {
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 convert(javax.vecmath.Matrix4d oldM) {
		edu.cmu.cs.dennisc.math.Matrix4x4 newM = new edu.cmu.cs.dennisc.math.Matrix4x4( oldM.m00, oldM.m01, oldM.m02, oldM.m03, oldM.m10, oldM.m11, oldM.m12, oldM.m13, oldM.m20, oldM.m21, oldM.m22, oldM.m23, oldM.m30, oldM.m31, oldM.m32, oldM.m33 );
		newM.transpose();
		edu.cmu.cs.dennisc.math.SpaceChangeUtilities.negateZ( newM );
		return new edu.cmu.cs.dennisc.math.AffineMatrix4x4( newM );
	}
}

/**
 * @author Dennis Cosgrove
 */
class Matrix3x3Handler implements Handler< javax.vecmath.Matrix3d, edu.cmu.cs.dennisc.math.Matrix3x3 > {
	public edu.cmu.cs.dennisc.math.Matrix3x3 convert(javax.vecmath.Matrix3d oldM) {
		//assert oldM.m00 == oldM.m11 && oldM.m00 == oldM.m22 : oldM;
		
		assert oldM.m01 == 0.0;
		assert oldM.m02 == 0.0;
		assert oldM.m10 == 0.0;
		assert oldM.m12 == 0.0;
		assert oldM.m20 == 0.0;
		assert oldM.m21 == 0.0;
		
		//todo: transpose?  shouldn't matter since really a vector along diagonal, but...
		return new edu.cmu.cs.dennisc.math.Matrix3x3( 
				new edu.cmu.cs.dennisc.math.Vector3( oldM.m00, oldM.m01, oldM.m02 ), 
				new edu.cmu.cs.dennisc.math.Vector3( oldM.m10, oldM.m11, oldM.m12 ), 
				new edu.cmu.cs.dennisc.math.Vector3( oldM.m20, oldM.m21, oldM.m22 )
		);
	}
}

/**
 * @author Dennis Cosgrove
 */
class EnumHandler<O,N extends Enum<?>> implements Handler<O, N> {
	private java.util.Map<O, N> map;
	public EnumHandler( java.util.Map<O, N> map ) {
		this.map = map;
	}
	public N convert(O oldValue) {
		return this.map.get( oldValue );
	}
}

/**
 * @author Dennis Cosgrove
 */
class AppearanceHandler implements Handler< edu.cmu.cs.stage3.alice.scenegraph.Appearance, SingleAppearance > {
	java.util.Map< edu.cmu.cs.stage3.alice.scenegraph.TextureMap, edu.cmu.cs.dennisc.texture.BufferedImageTexture > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();  
	public SingleAppearance convert(edu.cmu.cs.stage3.alice.scenegraph.Appearance oldValue) {
		SingleAppearance rv = new SingleAppearance();
		edu.cmu.cs.stage3.alice.scenegraph.TextureMap oldTextureMap = oldValue.getDiffuseColorMap();
		if( oldTextureMap != null ) {
			java.awt.Image image = oldTextureMap.getImage();
			if (image instanceof java.awt.image.BufferedImage) {
				java.awt.image.BufferedImage bufferedImage = (java.awt.image.BufferedImage) image;
				edu.cmu.cs.dennisc.texture.BufferedImageTexture newTexture = map.get( oldTextureMap );
				if( newTexture != null ) {
					//pass
				} else {
					newTexture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
					map.put( oldTextureMap, newTexture );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "newTexture", bufferedImage.getWidth(), bufferedImage.getHeight() );
				}
				newTexture.setBufferedImage( bufferedImage );
				rv.diffuseColorTexture.setValue( newTexture );
			}
		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
class IndexedTriangleArrayHandler implements Handler< edu.cmu.cs.stage3.alice.scenegraph.IndexedTriangleArray, Geometry[] > {
	public Geometry[] convert( edu.cmu.cs.stage3.alice.scenegraph.IndexedTriangleArray oldValue) {
		edu.cmu.cs.stage3.alice.scenegraph.Vertex3d[] oldVertices = oldValue.getVertices();
		if( oldVertices != null ) {
			IndexedTriangleArray ita = new IndexedTriangleArray();
			edu.cmu.cs.dennisc.scenegraph.Vertex[] newVertices = new edu.cmu.cs.dennisc.scenegraph.Vertex[ oldVertices.length ];
			for( int i = 0; i < newVertices.length; i++ ) {
				edu.cmu.cs.stage3.alice.scenegraph.Vertex3d v = oldVertices[ i ];
				newVertices[ i ] = edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( v.position.x, v.position.y, -v.position.z, (float)v.normal.x, (float)v.normal.y, (float)-v.normal.z, v.textureCoordinate0.x, 1.0f - v.textureCoordinate0.y );
			}
			ita.vertices.setValue( newVertices );
			
			//clobber old data
			int[] triangleData = oldValue.getIndices();
			for( int i = 0; i < triangleData.length; i += 3 ) {
				int a = triangleData[ i ];
				triangleData[ i ] = triangleData[ i + 2 ];
				triangleData[ i + 2 ] = a;
			}
			ita.polygonData.setValue( triangleData );
			return new Geometry[] { ita };
		} else {
			System.err.println( "warning: vertices null" );
			return new Geometry[] {};
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class ScenegraphBatch extends edu.cmu.cs.dennisc.batch.Batch {
	private static java.util.Set< edu.cmu.cs.stage3.alice.scenegraph.Property > ignoredProperties;
	private static java.util.Map< Class<?>, Handler<?,?> > mapOldValueClsToHandler;
	private static java.util.Map< edu.cmu.cs.stage3.alice.scenegraph.Property, Handler<?,?> > mapOldPropertyToHandler;
	private static java.util.Map< String, String > mapPropertyName;
	
	static {
		ignoredProperties = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( 
				edu.cmu.cs.stage3.alice.scenegraph.Element.NAME_PROPERTY, 
				edu.cmu.cs.stage3.alice.scenegraph.Element.BONUS_PROPERTY, 
				edu.cmu.cs.stage3.alice.scenegraph.Component.PARENT_PROPERTY,
				edu.cmu.cs.stage3.alice.scenegraph.Transformable.IS_FIRST_CLASS_PROPERTY,
				edu.cmu.cs.stage3.alice.scenegraph.Light.RANGE_PROPERTY,
				edu.cmu.cs.stage3.alice.scenegraph.Visual.DISABLED_AFFECTORS_PROPERTY 
		);

		java.util.Map< edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle, ShadingStyle > mapShadingStyle = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		mapShadingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle.NONE, ShadingStyle.NONE );
		mapShadingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle.FLAT, ShadingStyle.FLAT );
		mapShadingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle.SMOOTH, ShadingStyle.SMOOTH );
		
		java.util.Map< edu.cmu.cs.stage3.alice.scenegraph.FillingStyle, FillingStyle > mapFillingStyle = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		mapFillingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.FillingStyle.SOLID, FillingStyle.SOLID );
		mapFillingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.FillingStyle.WIREFRAME, FillingStyle.WIREFRAME );
		mapFillingStyle.put( edu.cmu.cs.stage3.alice.scenegraph.FillingStyle.POINTS, FillingStyle.POINTS );

		mapOldValueClsToHandler = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		mapOldValueClsToHandler.put( Boolean.class, new PassThroughHandler<Boolean>() );
		mapOldValueClsToHandler.put( Double.class, new PassThroughHandler<Boolean>() );
		mapOldValueClsToHandler.put( Float.class, new PassThroughHandler<Float>() );
		mapOldValueClsToHandler.put( edu.cmu.cs.stage3.alice.scenegraph.Color.class, new ColorHandler() );
		mapOldValueClsToHandler.put( javax.vecmath.Matrix4d.class, new AffineMatrix4x4Handler() );
		mapOldValueClsToHandler.put( javax.vecmath.Matrix3d.class, new Matrix3x3Handler() );
		mapOldValueClsToHandler.put( edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle.class, new EnumHandler<edu.cmu.cs.stage3.alice.scenegraph.ShadingStyle, ShadingStyle>( mapShadingStyle ) );
		mapOldValueClsToHandler.put(edu.cmu.cs.stage3.alice.scenegraph.Appearance.class, new AppearanceHandler() );
		mapOldValueClsToHandler.put(edu.cmu.cs.stage3.alice.scenegraph.IndexedTriangleArray.class, new IndexedTriangleArrayHandler() );
		
		mapPropertyName = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		mapPropertyName.put( "Geometry", "Geometries" );

		AngleHandler angleHandler = new AngleHandler();
		mapOldPropertyToHandler = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		mapOldPropertyToHandler.put( edu.cmu.cs.stage3.alice.scenegraph.SpotLight.INNER_BEAM_ANGLE_PROPERTY, angleHandler );
		mapOldPropertyToHandler.put( edu.cmu.cs.stage3.alice.scenegraph.SpotLight.OUTER_BEAM_ANGLE_PROPERTY, angleHandler );
	}
	private static String NEW_SCHOOL_PACKAGE_NAME = Element.class.getPackage().getName();
	private edu.cmu.cs.stage3.alice.core.World oldWorld;
	public ScenegraphBatch( String path ) {
		try {
			this.oldWorld = (edu.cmu.cs.stage3.alice.core.World)edu.cmu.cs.stage3.alice.core.Element.load( new java.io.File( path ), null );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
	
	private static <E extends Element> E createNewSchool( edu.cmu.cs.stage3.alice.scenegraph.Element oldElement ) {
		if( oldElement != null ) {
			String  oldClsSimpleName = oldElement.getClass().getSimpleName();
			E rv = (E)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( NEW_SCHOOL_PACKAGE_NAME + "." + oldClsSimpleName );
			rv.setName( oldElement.getName() );

			java.util.Enumeration<edu.cmu.cs.stage3.alice.scenegraph.Property> enm = edu.cmu.cs.stage3.alice.scenegraph.Property.getProperties( oldElement.getClass() ).elements();
			while( enm.hasMoreElements() ) {
				edu.cmu.cs.stage3.alice.scenegraph.Property oldProperty = enm.nextElement();
				if( ignoredProperties.contains( oldProperty ) ) {
					//pass
				} else {
					String propertyName = oldProperty.getMixedCaseName();
					if( mapPropertyName.containsKey( propertyName ) ) {
						propertyName = mapPropertyName.get( propertyName );
					}
					edu.cmu.cs.dennisc.property.Property newProperty = rv.getPropertyNamed( propertyName ); 
					if( newProperty != null ) {
						Object oldValue = oldProperty.get( oldElement );
						
						if( oldProperty == edu.cmu.cs.stage3.alice.scenegraph.Light.BRIGHTNESS_PROPERTY ) {
							oldValue = ((Number)oldValue).floatValue();
						}
						
						Object newValue;
						if( oldValue != null ) {
							Handler handler = mapOldPropertyToHandler.get( oldProperty );
							if( handler != null ) {
								//pass
							} else {
								handler = mapOldValueClsToHandler.get( oldValue.getClass() );
							}
							if( handler != null ) {
								newValue = handler.convert( oldValue );
							} else {
								throw new AssertionError( oldValue.getClass() );
							}
						} else {
							if( oldProperty == edu.cmu.cs.stage3.alice.scenegraph.Visual.GEOMETRY_PROPERTY ) {
								newValue = new Geometry[ 0 ];
							} else {
								newValue = null;
							}
						}
						try {
							newProperty.setValue(rv, newValue);
						} catch( Throwable t ) {
							throw new AssertionError( newProperty.toString() );
						}
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "\t", propertyName );
					}
				}
			}
			
			if (rv instanceof Composite) {
				Composite parent = (Composite)rv;
				edu.cmu.cs.stage3.alice.scenegraph.Container oldParent = (edu.cmu.cs.stage3.alice.scenegraph.Container)oldElement;
				
				for( edu.cmu.cs.stage3.alice.scenegraph.Component oldComponent : oldParent.getChildren() ) {
					Component component = createNewSchool(oldComponent);
					component.setParent(parent);
				}
			}
			return rv;
		} else {
			return null;
		}
	}
	
	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return edu.cmu.cs.dennisc.lang.SystemUtilities.isPropertyTrue( "isSkipExistingOutFilesDesirable" );
	}
	@Override
	protected void handle(java.io.File inFile, java.io.File outFile) {
		try {
			edu.cmu.cs.stage3.alice.core.Element oldSchool = edu.cmu.cs.stage3.alice.core.Element.load( inFile, this.oldWorld );
			if (oldSchool instanceof edu.cmu.cs.stage3.alice.core.Transformable) {
				edu.cmu.cs.stage3.alice.core.Transformable oldTransformable = (edu.cmu.cs.stage3.alice.core.Transformable) oldSchool;
				edu.cmu.cs.stage3.alice.scenegraph.Transformable oldSGTranformable = oldTransformable.getSceneGraphTransformable();
				Transformable sgTransformable = createNewSchool( oldSGTranformable );
				edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder modelBuilder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.newInstance( sgTransformable );
				modelBuilder.encode( outFile );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( outFile );
			} else {
				throw new AssertionError( inFile.toString() );
			}
		} catch( Exception e ) {
//			throw new AssertionError( e );
			if( outFile.exists() ) {
				outFile.delete();
			}
			throw new RuntimeException(inFile.toString(), e);
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ConvertScenegraphFrom2To3 {
	public static void main(String[] args) throws Exception {
		final String ROOT = System.getProperty( "user.home" ) + "/Desktop/gallery_src/";
		String subsetOrFull;
		if( args.length > 0 ) {
			subsetOrFull = args[ 0 ];
		} else {
			subsetOrFull = "full";
		}
		ScenegraphBatch scenegraphBatch = new ScenegraphBatch( ROOT + "default.a2w" );
		scenegraphBatch.process( ROOT + subsetOrFull + "/src2/", ROOT + subsetOrFull + "/converted/", "a2c", "zip");
	}
}

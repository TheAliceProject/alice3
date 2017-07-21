/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.raytrace;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AmbientLight;
import edu.cmu.cs.dennisc.scenegraph.Appearance;
import edu.cmu.cs.dennisc.scenegraph.Background;
import edu.cmu.cs.dennisc.scenegraph.Box;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.DirectionalLight;
import edu.cmu.cs.dennisc.scenegraph.Disc;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Light;
import edu.cmu.cs.dennisc.scenegraph.PlanarReflector;
import edu.cmu.cs.dennisc.scenegraph.PointLight;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.TriangleFan;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public class POVRayUtilities {
	private static String toString( edu.cmu.cs.dennisc.color.Color4f color ) {
		return "color rgb <" + color.red + ", " + color.green + ", " + color.blue + ">";
	}

	private static String toString( edu.cmu.cs.dennisc.color.Color4f color, float opacity ) {
		return "color rgbt <" + color.red + ", " + color.green + ", " + color.blue + ", " + ( 1.0 - ( color.alpha * opacity ) ) + ">";
	}

	private static String toString( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		return "matrix < " +
				m.orientation.right.x + ", " + m.orientation.right.y + ", " + -m.orientation.right.z + ", " +
				m.orientation.up.x + ", " + m.orientation.up.y + ", " + -m.orientation.up.z + ", " +
				-m.orientation.backward.x + ", " + -m.orientation.backward.y + ", " + m.orientation.backward.z + ", " +
				m.translation.x + ", " + m.translation.y + ", " + -m.translation.z + " >";
	}

	private static String toString( double x, double y, double z ) {
		return "<" + x + ", " + y + ", " + -z + ">";
	}

	private static String toString( edu.cmu.cs.dennisc.math.Tuple3 t ) {
		return toString( t.x, t.y, t.z );
	}

	private static void exportBackground( java.io.PrintWriter pw, Background sgBackground ) {
		pw.println( "background {" );
		pw.println( toString( sgBackground.color.getValue() ) );
		pw.println( "}" );
	}

	private static void exportGeometry( java.io.PrintWriter pw, Geometry sgGeometry, Appearance sgAppearance, double reflection, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		if( sgGeometry instanceof Sphere ) {
			Sphere sgSphere = (Sphere)sgGeometry;
			pw.println( "sphere {" );
			pw.println( "<" + m.translation.x + ", " + m.translation.y + ", " + -m.translation.z + ">, " + sgSphere.radius.getValue() );
		} else if( sgGeometry instanceof Torus ) {
			Torus sgTorus = (Torus)sgGeometry;
			pw.println( "torus {" );
			pw.println( sgTorus.majorRadius.getValue() + ", " + sgTorus.minorRadius.getValue() );
			pw.println( toString( m ) );
		} else if( sgGeometry instanceof Cylinder ) {
			Cylinder sgCylinder = (Cylinder)sgGeometry;
			pw.println( "cone {" );
			edu.cmu.cs.dennisc.math.Point3 base = sgCylinder.getCenterOfBottom();
			edu.cmu.cs.dennisc.math.Point3 cap = sgCylinder.getCenterOfTop();
			pw.println( toString( base ) );
			pw.println( sgCylinder.bottomRadius.getValue() + ", " );
			pw.println( toString( cap ) );
			pw.println( sgCylinder.getActualTopRadius() );

			if( sgCylinder.hasTopCap.getValue() ) {
				if( sgCylinder.hasBottomCap.getValue() ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "UNHANDLED CYLINDER CAP STATE: " + sgCylinder );
				}
			} else {
				if( sgCylinder.hasBottomCap.getValue() ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "UNHANDLED CYLINDER CAP STATE: " + sgCylinder );
				} else {
					pw.println( "open" );
				}
			}
			pw.println( toString( m ) );
		} else if( sgGeometry instanceof Disc ) {
			Disc sgDisc = (Disc)sgGeometry;
			pw.println( "disc {" );
			pw.println( "<0,0,0>, <0,0,-1>, " + sgDisc.outerRadius.getValue() + ", " + sgDisc.innerRadius.getValue() );
			pw.println( toString( m ) );
		} else if( sgGeometry instanceof Box ) {
			Box sgBox = (Box)sgGeometry;
			edu.cmu.cs.dennisc.math.Point3 minimum = sgBox.getMinimum();
			edu.cmu.cs.dennisc.math.Point3 maximum = sgBox.getMaximum();
			pw.println( "box {" );
			pw.print( toString( minimum ) );
			pw.print( ", " );
			pw.println( toString( maximum ) );
			pw.println( toString( m ) );
		} else if( sgGeometry instanceof TriangleFan ) {
			TriangleFan sgTriangleFan = (TriangleFan)sgGeometry;
			pw.println( "polygon {" );
			int n = sgTriangleFan.vertices.getLength();
			pw.println( n + "," );
			Vertex[] sgVertices = sgTriangleFan.vertices.getValue();
			for( int i = 0; i < n; i++ ) {
				Vertex sgVertex = sgVertices[ i ];
				edu.cmu.cs.dennisc.math.Point3 p = new edu.cmu.cs.dennisc.math.Point3( sgVertex.position );
				m.transform( p );
				pw.print( toString( p ) );
				if( i < ( n - 1 ) ) {
					pw.println( "," );
				} else {
					pw.println();
				}
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "UNHANDLED GEOMETRY: " + sgGeometry );
			return;
		}

		edu.cmu.cs.dennisc.color.Color4f color = null;
		float opacity = Float.NaN;
		float specular = Float.NaN;
		if( sgAppearance instanceof TexturedAppearance ) {
			TexturedAppearance sgTexturedAppearance = (TexturedAppearance)sgAppearance;
			color = sgTexturedAppearance.diffuseColor.getValue();
			opacity = sgTexturedAppearance.opacity.getValue();
			specular = sgTexturedAppearance.specularHighlightExponent.getValue();
		}
		pw.println( "texture {" );
		if( color != null ) {
			pw.println( "pigment { " );
			pw.println( toString( color, opacity ) );
			pw.println( "}" );
		}
		pw.println( "finish { " );
		if( reflection > 0 ) {
			pw.println( "reflection  " + reflection );
		} else {
			pw.println( "phong 1" );
			if( Float.isNaN( specular ) == false ) {
				pw.println( "specular " + specular );
			}
		}
		pw.println( "}" );
		pw.println( "}" );
		pw.println( "}" );
	}

	private static void exportVisual( java.io.PrintWriter pw, Visual sgVisual ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgVisual.getAbsoluteTransformation();
		Appearance sgAppearance = sgVisual.frontFacingAppearance.getValue();
		double reflection;
		if( sgVisual instanceof PlanarReflector ) {
			reflection = 0.6;
		} else {
			reflection = 0.0;
		}
		for( Geometry sgGeometry : sgVisual.geometries.getValue() ) {
			exportGeometry( pw, sgGeometry, sgAppearance, reflection, m );
		}
	}

	private static void exportLight( java.io.PrintWriter pw, Light sgLight ) {
		if( sgLight instanceof AmbientLight ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "UNHANDLED AMBIENT LIGHT: " + sgLight );
			return;
		}
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgLight.getAbsoluteTransformation();
		pw.println( "light_source { " );
		pw.println( "<" + m.translation.x + ", " + m.translation.y + ", " + -m.translation.z + ">" );
		pw.println( toString( sgLight.color.getValue() ) );
		if( sgLight instanceof PointLight ) {
			//PointLight sgPointLight = (PointLight)sgLight;
		} else if( sgLight instanceof DirectionalLight ) {
			//DirectionalLight sgDirectionalLight = (DirectionalLight)sgLight;
			pw.println( "parallel" );

			double x = m.translation.x - m.orientation.backward.x;
			double y = m.translation.y - m.orientation.backward.y;
			double z = m.translation.z - m.orientation.backward.z;

			pw.print( "point_at " );
			pw.println( toString( x, y, z ) );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "UNHANDLED LIGHT: " + sgLight );
		}
		pw.println( "}" );
	}

	public static void export( java.io.PrintWriter pw, AbstractCamera sgCamera ) {
		//AbstractCamera sgCamera = lookingGlass.getCameraAt( 0 );
		Composite sgRoot = sgCamera.getRoot();
		if( sgRoot instanceof Scene ) {
			Scene sgScene = (Scene)sgRoot;
			Background background = sgCamera.background.getValue();
			if( background != null ) {
				//pass
			} else {
				background = sgScene.background.getValue();
			}
			exportBackground( pw, background );

			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgCamera.getAbsoluteTransformation();
			pw.println( "camera {" );
			pw.println( toString( m ) );
			if( sgCamera instanceof SymmetricPerspectiveCamera ) {
				SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera = (SymmetricPerspectiveCamera)sgCamera;
				edu.cmu.cs.dennisc.math.Angle angle = sgSymmetricPerspectiveCamera.horizontalViewingAngle.getValue();
				double degrees;
				if( angle.isNaN() ) {
					//todo
					degrees = 45;
				} else {
					degrees = angle.getAsDegrees();
				}
				//pw.println( "angle " + lookingGlass.getActualHorizontalViewingAngle( sgSymmetricPerspectiveCamera ).getAsDegrees() );
				pw.println( "angle " + degrees );
			}
			pw.println( "}" );

			for( Visual sgVisual : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( sgScene, Visual.class ) ) {
				exportVisual( pw, sgVisual );
			}
			for( Light sgLight : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( sgScene, Light.class ) ) {
				exportLight( pw, sgLight );
			}
			pw.flush();
		}
	}
	//	public static void export( java.io.File file, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
	//		try {
	//			file.getParentFile().mkdirs();
	//			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
	//			java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( fos );
	//			export( new java.io.PrintWriter( bos ), lookingGlass );
	//			bos.flush();
	//			fos.flush();
	//			fos.close();
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//	public static void export( String path, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass ) {
	//		export( new java.io.File( path ), lookingGlass );
	//	}
	//	
	//	public static int exec( String path ) {
	//		return edu.cmu.cs.dennisc.java.lang.RuntimeUtilities.exec( new java.io.File( "s:/povray" ), "pvengine", "/RENDER", "test.pov" );
	//	}
}

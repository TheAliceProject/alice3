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

package edu.cmu.cs.dennisc.render;

//todo: unify w/ scenegraph.util.TransformationUtilities?
/**
 * @author Dennis Cosgrove
 */
public class PicturePlaneUtils {
	private static edu.cmu.cs.dennisc.math.Vector4 transformFromAWTToViewport( edu.cmu.cs.dennisc.math.Vector4 rv, java.awt.Point p, double z, java.awt.Rectangle actualViewport ) {
		rv.x = p.x;
		rv.y = actualViewport.height - p.y;
		rv.z = z;
		rv.w = 1.0;
		return rv;
	}

	private static java.awt.Point transformFromViewportToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, java.awt.Rectangle actualViewport ) {
		rv.x = (int)( xyzw.x / xyzw.w );
		rv.y = (int)( xyzw.y / xyzw.w );
		rv.y = actualViewport.height - rv.y;
		return rv;
	}

	private static double getNear( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( sgCamera instanceof edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera ) {
			return ( (edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera)sgCamera ).nearClippingPlaneDistance.getValue();
		} else {
			//todo?
			return Double.NaN;
		}
	}

	private static double getFar( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( sgCamera instanceof edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera ) {
			return ( (edu.cmu.cs.dennisc.scenegraph.AbstractNearPlaneAndFarPlaneCamera)sgCamera ).farClippingPlaneDistance.getValue();
		} else {
			//todo?
			return Double.NaN;
		}
	}

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromViewportToProjection_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle actualViewport ) {
		rv.multiply( 1.0 / rv.w );

		rv.x = ( rv.x - actualViewport.x ) / actualViewport.width;
		rv.y = ( rv.y - actualViewport.y ) / actualViewport.height;

		double zNear = getNear( sgCamera );
		double zFar = getFar( sgCamera );
		rv.z = ( rv.z - zNear ) / ( zFar - zNear );

		rv.x = ( rv.x * 2.0 ) - 1.0;
		rv.y = ( rv.y * 2.0 ) - 1.0;
		rv.z = ( rv.z * 2.0 ) - 1.0;

		return rv;
	}

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromProjectionToViewport_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle actualViewport ) {
		rv.multiply( 1.0 / rv.w );

		rv.x = ( rv.x * 0.5 ) + 0.5;
		rv.y = ( rv.y * 0.5 ) + 0.5;
		rv.z = ( rv.z * 0.5 ) + 0.5;

		rv.x = ( rv.x * actualViewport.width ) + actualViewport.x;
		rv.y = ( rv.y * actualViewport.height ) + actualViewport.y;
		double zNear = getNear( sgCamera );
		double zFar = getFar( sgCamera );
		rv.z = ( rv.z * ( zFar - zNear ) ) + zNear;

		return rv;
	}

	private static edu.cmu.cs.dennisc.math.Matrix4x4 s_actualProjectionBuffer = new edu.cmu.cs.dennisc.math.Matrix4x4();

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromProjectionToCamera_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		rv.multiply( 1.0 / rv.w );
		synchronized( s_actualProjectionBuffer ) {
			renderTarget.getActualProjectionMatrix( s_actualProjectionBuffer, sgCamera );
			s_actualProjectionBuffer.invert();
			s_actualProjectionBuffer.transform( rv );
			return rv;
		}
	}

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromCameraToProjection_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		rv.multiply( 1.0 / rv.w );
		synchronized( s_actualProjectionBuffer ) {
			renderTarget.getActualProjectionMatrix( s_actualProjectionBuffer, sgCamera );
			s_actualProjectionBuffer.transform( rv );
			return rv;
		}
	}

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromViewportToCamera_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle actualViewport ) {
		transformFromViewportToProjection_AffectReturnValuePassedIn( rv, sgCamera, actualViewport );
		transformFromProjectionToCamera_AffectReturnValuePassedIn( rv, renderTarget, sgCamera );
		return rv;
	}

	private static edu.cmu.cs.dennisc.math.Vector4 transformFromCameraToViewport_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle actualViewport ) {
		transformFromCameraToProjection_AffectReturnValuePassedIn( rv, renderTarget, sgCamera );
		transformFromProjectionToViewport_AffectReturnValuePassedIn( rv, sgCamera, actualViewport );
		return rv;
	}

	private static java.awt.Rectangle s_actualViewportBuffer = new java.awt.Rectangle();

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromViewportToCamera_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		synchronized( s_actualViewportBuffer ) {
			renderTarget.getActualViewportAsAwtRectangle( s_actualViewportBuffer, sgCamera );
			transformFromViewportToCamera_AffectReturnValuePassedIn( rv, renderTarget, sgCamera, s_actualViewportBuffer );
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromCameraToViewport_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		synchronized( s_actualViewportBuffer ) {
			renderTarget.getActualViewportAsAwtRectangle( s_actualViewportBuffer, sgCamera );
			transformFromCameraToViewport_AffectReturnValuePassedIn( rv, renderTarget, sgCamera, s_actualViewportBuffer );
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromViewportToCamera( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		rv.set( xyzw );
		return transformFromViewportToCamera_AffectReturnValuePassedIn( rv, renderTarget, sgCamera );
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromCameraToViewport( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		rv.set( xyzw );
		return transformFromCameraToViewport_AffectReturnValuePassedIn( rv, renderTarget, sgCamera );
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromViewportToCamera_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return transformFromViewportToCamera( new edu.cmu.cs.dennisc.math.Vector4(), xyzw, renderTarget, sgCamera );
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromCameraToViewport_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return transformFromCameraToViewport( new edu.cmu.cs.dennisc.math.Vector4(), xyzw, renderTarget, sgCamera );
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromAWTToCamera( edu.cmu.cs.dennisc.math.Vector4 rv, java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		synchronized( s_actualViewportBuffer ) {
			renderTarget.getActualViewportAsAwtRectangle( s_actualViewportBuffer, sgCamera );
			transformFromAWTToViewport( rv, p, z, s_actualViewportBuffer );
			transformFromViewportToCamera_AffectReturnValuePassedIn( rv, renderTarget, sgCamera, s_actualViewportBuffer );
		}
		return rv;
	}

	private static edu.cmu.cs.dennisc.math.Vector4 s_vector4dBuffer = new edu.cmu.cs.dennisc.math.Vector4();

	public static java.awt.Point transformFromCameraToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		synchronized( s_vector4dBuffer ) {
			s_vector4dBuffer.set( xyzw );
			synchronized( s_actualViewportBuffer ) {
				renderTarget.getActualViewportAsAwtRectangle( s_actualViewportBuffer, sgCamera );
				transformFromCameraToViewport_AffectReturnValuePassedIn( s_vector4dBuffer, renderTarget, sgCamera, s_actualViewportBuffer );
				transformFromViewportToAWT( rv, s_vector4dBuffer, s_actualViewportBuffer );
			}
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.Point3 transformFromAWTToCamera_NewPoint3d( java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		edu.cmu.cs.dennisc.math.Vector4 xyzw = transformFromAWTToCamera( new edu.cmu.cs.dennisc.math.Vector4(), p, z, renderTarget, sgCamera );
		return new edu.cmu.cs.dennisc.math.Point3( xyzw.x / xyzw.w, xyzw.y / xyzw.w, xyzw.z / xyzw.w );
	}

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromAWTToCamera_NewVector4d( java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return transformFromAWTToCamera( new edu.cmu.cs.dennisc.math.Vector4(), p, z, renderTarget, sgCamera );
	}

	public static java.awt.Point transformFromCameraToAWT_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return transformFromCameraToAWT( new java.awt.Point(), xyzw, renderTarget, sgCamera );
	}

	public static java.awt.Point transformFromCameraToAWT_New( edu.cmu.cs.dennisc.math.Point3 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return transformFromCameraToAWT( new java.awt.Point(), new edu.cmu.cs.dennisc.math.Vector4( xyzw.x, xyzw.y, xyzw.z, 1.0 ), renderTarget, sgCamera );
	}

	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromAWTToViewport( edu.cmu.cs.dennisc.math.Vector4d rv, java.awt.Point p, double z, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_actualViewportBuffer ) {
	//			renderTarget.getActualViewport( s_actualViewportBuffer, sgCamera );
	//			rv.x = p.x;
	//			rv.y = s_actualViewportBuffer.height - p.y;
	//			rv.z = z;
	//			rv.w = 1.0;
	//		}
	//		return rv;
	//	}
	//	public static java.awt.Point transformFromViewportToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_actualViewportBuffer ) {
	//			renderTarget.getActualViewport( s_actualViewportBuffer, sgCamera );
	//			rv.x = (int)(xyzw.x / xyzw.w);
	//			rv.y = (int)(xyzw.y / xyzw.w);
	//			rv.y = s_actualViewportBuffer.height - rv.y;
	//		}
	//		return rv;
	//	}
	//
	//	
	//	
	//	
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromViewportToProjection( edu.cmu.cs.dennisc.math.Vector4d rv, edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_actualViewportBuffer ) {
	//			renderTarget.getActualViewport( s_actualViewportBuffer, sgCamera );
	//
	//			rv.set( xyzw );
	//			rv.scale( 1.0 / rv.w );
	//
	//			rv.x = (rv.x - s_actualViewportBuffer.x) / s_actualViewportBuffer.width;
	//			rv.y = (rv.y - s_actualViewportBuffer.y) / s_actualViewportBuffer.height;
	//
	//			double zNear = sgCamera.getNearClippingPlaneDistance();
	//			double zFar = sgCamera.getFarClippingPlaneDistance();
	//			rv.z = (rv.z - zNear) / (zFar - zNear);
	//
	//			rv.x = rv.x * 2.0 - 1.0;
	//			rv.y = rv.y * 2.0 - 1.0;
	//			rv.z = rv.z * 2.0 - 1.0;
	//
	//			return rv;
	//		}
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromProjectionToCamera( edu.cmu.cs.dennisc.math.Vector4d rv, edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_matrix4dBuffer ) {
	//			renderTarget.getActualProjectionMatrix( s_matrix4dBuffer, sgCamera );
	//			s_matrix4dBuffer.invert();
	//			rv.set( xyzw );
	//			s_matrix4dBuffer.transform( rv );
	//			rv.scale( 1.0 / rv.w );
	//			return rv;
	//		}
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromCameraToProjection( edu.cmu.cs.dennisc.math.Vector4d rv, edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_matrix4dBuffer ) {
	//			renderTarget.getActualProjectionMatrix( s_matrix4dBuffer, sgCamera );
	//			rv.set( xyzw );
	//			s_matrix4dBuffer.transform( rv );
	//			rv.scale( 1.0 / rv.w );
	//			return rv;
	//		}
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromProjectionToViewport( edu.cmu.cs.dennisc.math.Vector4d rv, edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		synchronized( s_actualViewportBuffer ) {
	//			renderTarget.getActualViewport( s_actualViewportBuffer, sgCamera );
	//
	//			rv.set( xyzw );
	//
	//			rv.x = rv.x * 0.5 + 0.5;
	//			rv.y = rv.y * 0.5 + 0.5;
	//			rv.z = rv.z * 0.5 + 0.5;
	//
	//			rv.x = (rv.x * s_actualViewportBuffer.width) + s_actualViewportBuffer.x;
	//			rv.y = (rv.y * s_actualViewportBuffer.height) + s_actualViewportBuffer.y;
	//			double zNear = sgCamera.getNearClippingPlaneDistance();
	//			double zFar = sgCamera.getFarClippingPlaneDistance();
	//			rv.z = (rv.z * (zFar - zNear)) + zNear;
	//
	//			rv.scale( 1.0 / rv.w );
	//			return rv;
	//		}
	//	}
	//
	//
	//	
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromAWTToViewport_New( java.awt.Point p, double z, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromAWTToViewport( new edu.cmu.cs.dennisc.math.Vector4d(), p, z, renderTarget, sgCamera );
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromViewportToProjection_New( edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromViewportToProjection( new edu.cmu.cs.dennisc.math.Vector4d(), xyzw, renderTarget, sgCamera );
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromProjectionToCamera_New( edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromProjectionToCamera( new edu.cmu.cs.dennisc.math.Vector4d(), xyzw, renderTarget, sgCamera );
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromCameraToProjection_New( edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromCameraToProjection( new edu.cmu.cs.dennisc.math.Vector4d(), xyzw, renderTarget, sgCamera );
	//	}
	//	public static edu.cmu.cs.dennisc.math.Vector4d transformFromProjectionToViewport_New( edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromProjectionToViewport( new edu.cmu.cs.dennisc.math.Vector4d(), xyzw, renderTarget, sgCamera );
	//	}
	//	public static java.awt.Point transformFromViewportToAWT_New( edu.cmu.cs.dennisc.math.Vector4d xyzw, edu.cmu.cs.dennisc.renderer.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.Camera sgCamera ) {
	//		return transformFromViewportToAWT( new java.awt.Point(), xyzw, renderTarget, sgCamera );
	//	}

}

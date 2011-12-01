/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

import static javax.media.opengl.GL.GL_LIGHTING;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dennis Cosgrove
 */
public class PersonAdapter extends GenericModelAdapter< Person > {
	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {	
		super.renderGeometry( rc );
		//DEBUG RENDERING
//        if (edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
//        {
//        	for (org.lgna.story.resources.JointId root : org.lgna.story.resources.BipedResource.JOINT_ID_ROOTS) {
//        		rc.gl.glPushMatrix();
//            	AffineMatrix4x4 absoluteTransform = this.m_element.getSGParent().getAbsoluteTransformation();
//            	absoluteTransform.getAsColumnMajorArray16(m_local);
//                rc.gl.glLoadMatrixd(m_localBuffer);
//        		debugRenderJointVisualization(rc, root);
//        		rc.gl.glPopMatrix();
//        	}
//        	
//        }
	}
	
	private double[] m_local = new double[ 16 ];
    private java.nio.DoubleBuffer m_localBuffer = java.nio.DoubleBuffer.wrap( m_local );
	
    protected void debugRenderJointVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, org.lgna.story.resources.JointId jointId) {
    	if ( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            rc.gl.glPushMatrix();
            debugRenderSingleJointVisualization(rc, jointId);
            debugRenderChildrenJointVisualization(rc, jointId);
            rc.gl.glPopMatrix();
        }
    }
    
    protected void debugRenderChildrenJointVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, org.lgna.story.resources.JointId jointId)
    {
        if ( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY)) {
        	for (org.lgna.story.resources.JointId j : jointId.getChildren(org.lgna.story.resources.BipedResource.class) ) {
        		debugRenderJointVisualization(rc, j);
        	}
        }
    }
    
	protected void debugRenderSingleJointVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, org.lgna.story.resources.JointId jointId) 
    {
        if (edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            final double UNIT_LENGTH = .2;
            AffineMatrix4x4 localTransformation = this.m_element.getLocalTransformationForJoint(jointId);
            localTransformation.getAsColumnMajorArray16(m_local);
            rc.gl.glMultMatrixd(m_localBuffer);
            {
                rc.gl.glDisable( GL_LIGHTING );
                rc.gl.glDisable( GL_TEXTURE_2D );
                rc.gl.glBegin( GL_LINES );
        
                rc.gl.glColor3f( 1.0f, 0.0f, 0.0f );
                rc.gl.glVertex3d( 0, 0, 0 );
                rc.gl.glVertex3d( UNIT_LENGTH, 0, 0 );
        
                rc.gl.glColor3f( 0.0f, 1.0f, 0.0f );
                rc.gl.glVertex3d( 0, 0, 0 );
                rc.gl.glVertex3d( 0, UNIT_LENGTH, 0 );
        
                rc.gl.glColor3f( 0.0f, 0.0f, 1.0f );
                rc.gl.glVertex3d( 0, 0, 0 );
                rc.gl.glVertex3d( 0, 0, UNIT_LENGTH );  
                
                rc.gl.glEnable( GL_TEXTURE_2D );
                rc.gl.glEnable( GL_LIGHTING );
            }
        }
    }

}

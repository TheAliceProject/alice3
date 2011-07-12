/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.lookingglass.graphics;

import java.awt.Color;
import java.awt.Point;

import edu.cmu.cs.dennisc.math.Point3;

public class CharactersFrameOverlayAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.GraphicAdapter< edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.CharactersFrameOverlay > {
	@Override
	protected void render( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel[] models = m_element.getModels();
		assert models != null && models.length > 0;
//		Vector<Point> pointsVector = new Vector<Point>();
//		Point center = new Point();
		org.alice.apis.moveandturn.AbstractCamera camera = models[ 0 ].getCamera();
		Point min = new Point( Integer.MAX_VALUE, Integer.MAX_VALUE );
		Point max = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
		for (int i = 0; i < models.length; ++i){
			edu.cmu.cs.dennisc.math.AxisAlignedBox bBox = models[i].getAxisAlignedMinimumBoundingBox();
			for( Point3 p3 : bBox.getHexahedron().getPoints() ) {
				Point xy = models[ i ].transformToAWT(p3, camera);
				min.x = Math.min( min.x, xy.x );
				min.y = Math.min( min.y, xy.y );
				max.x = Math.max( max.x, xy.x );
				max.y = Math.max( max.y, xy.y );
			}
//			edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = models[i].getSGComposite();
//			
//			Point3 bottomLeftFront3D = new Point3(bBox.getXMinimum(),bBox.getYMinimum(),bBox.getZMinimum());
//			Point3 topLeftFront3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMinimum());
//			Point3 topRightFront3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMinimum());
//			Point3 bottomRightFront3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMinimum());
//			
//			Point3 bottomLeftBack3D = new Point3(bBox.getXMinimum(),bBox.getYMinimum(),bBox.getZMaximum());
//			Point3 topLeftBack3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMaximum());
//			Point3 topRightBack3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMaximum());
//			Point3 bottomRightBack3D = new Point3(bBox.getXMinimum(),bBox.getYMaximum(),bBox.getZMaximum());
//
//			Point bottomLeftFront2D = sgComposite.transformToAWT_New(bottomLeftFront3D, lookingGlass, sgCamera);
//			Point topLeftFront2D = sgComposite.transformToAWT_New(topLeftFront3D, lookingGlass, sgCamera);
//			Point topRightFront2D = sgComposite.transformToAWT_New(topRightFront3D, lookingGlass, sgCamera);
//			Point bottomRightFront2D = sgComposite.transformToAWT_New(bottomRightFront3D, lookingGlass, sgCamera);
//
//			center = sgComposite.transformToAWT_New(bBox.getCenter(), lookingGlass, sgCamera);
//			
//			Point bottomLeftBack2D = sgComposite.transformToAWT_New(bottomLeftBack3D, lookingGlass, sgCamera);
//			Point topLeftBack2D = sgComposite.transformToAWT_New(topLeftBack3D, lookingGlass, sgCamera);
//			Point topRightBack2D = sgComposite.transformToAWT_New(topRightBack3D, lookingGlass, sgCamera);
//			Point bottomRightBack2D = sgComposite.transformToAWT_New(bottomRightBack3D, lookingGlass, sgCamera);
//			
//			pointsVector.add(bottomLeftFront2D);
//			pointsVector.add(topLeftFront2D);
//			pointsVector.add(topRightFront2D);
//			pointsVector.add(bottomRightFront2D);
//
//			pointsVector.add(bottomLeftBack2D);
//			pointsVector.add(topLeftBack2D);
//			pointsVector.add(topRightBack2D);
//			pointsVector.add(bottomRightBack2D);
		}
		
//		double smallestY = pointsVector.firstElement().getY();
//		double smallestX = pointsVector.firstElement().getX();
//		double largestY = pointsVector.firstElement().getY();
//		double largestX = pointsVector.firstElement().getX();
//		
//		for (int i = 1; i < pointsVector.size(); ++i){
//			smallestY = smallestY < pointsVector.get(i).y ? smallestY : pointsVector.get(i).y;
//			smallestX = smallestX < pointsVector.get(i).x ? smallestX : pointsVector.get(i).x;
//			largestY = largestY > pointsVector.get(i).y ? largestY : pointsVector.get(i).y;
//			largestX = largestX > pointsVector.get(i).x ? largestX : pointsVector.get(i).x;
//		}
		g2.setColor(Color.red.darker());
		g2.setStroke(new java.awt.BasicStroke(4,java.awt.BasicStroke.CAP_ROUND,java.awt.BasicStroke.JOIN_ROUND));	
		//g2.drawRect((int)(center.x-10), (int)(center.y-10), 20, 20);
		//g2.drawRoundRect((int)smallestX, (int)smallestY,(int)(largestX-smallestX),(int)(largestY-smallestY),10,10);
		g2.draw(new java.awt.geom.RoundRectangle2D.Double((int)(min.x-((max.x-min.x)*.1)), (int)(min.y-((max.y-min.y)*.1)), (int)((max.x-min.x)+((max.x-min.x)*.2)), (int)((max.y-min.y)+((max.y-min.y)*.2)),10,10));
//		g2.drawRoundRect((int)(min.x-((max.x-min.x)*.1)), (int)(min.y-((max.y-min.y)*.1)), (int)((max.x-min.x)+((max.x-min.x)*.2)), (int)((max.y-min.y)+((max.y-min.y)*.2)),10,10);

	}
	@Override
	protected void forget( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
	}
}

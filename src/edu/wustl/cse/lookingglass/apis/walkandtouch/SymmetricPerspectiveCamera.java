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
package edu.wustl.cse.lookingglass.apis.walkandtouch;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import edu.cmu.cs.dennisc.alice.annotations.*;

import org.alice.apis.moveandturn.Color;
import org.alice.apis.moveandturn.Font;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.Transformable;
import org.alice.apis.moveandturn.TurnDirection;


import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.wustl.cse.lookingglass.apis.walkandtouch.animation.ExampleCameraPathAnimation;
import edu.wustl.cse.lookingglass.apis.walkandtouch.animation.KineticTextAnimation;



import edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.CharacterFrameOverlay;
import edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.KineticText;

public class SymmetricPerspectiveCamera extends org.alice.apis.moveandturn.SymmetricPerspectiveCamera {
	//Using a map so that it can do quick lookups via Strings while also holding the animations that need to (un)highlight 
	private HighlightedContainerSet highlightedContainers = new HighlightedContainerSet();
	private Scene scene = null;
	
	// FOR ROSS BEGIN
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public void followCameraPath( Vector<AffineMatrix4x4> cameraTransforms, double durationPerSegment) {
		perform( new ExampleCameraPathAnimation( this, cameraTransforms, durationPerSegment));
	}
	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	protected void displayPathOverlay( Path2DOverlay pathOverlay, double duration ) {
//		perform( new Path2DOverlayAnimation( this.getOwner(), pathOverlay, 0.0, duration, 0.0 ) );
//	}
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void showPathOverlay(double duration) {
//		displayPathOverlay( new Path2DOverlay(), duration );
//	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void displayCharacterFrameOverlay(PolygonalModel model, double duration) {
		displayGraphic( new CharacterFrameOverlay(model), duration );
	}
	
	// FOR ROSS END
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	protected void displayGraphic( edu.cmu.cs.dennisc.scenegraph.Graphic graphic, double duration ) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.graphic.animation.ShowHideDurationOverlayGraphicAnimation( this, duration, graphic ) );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showTitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor, org.alice.apis.moveandturn.Color backgroundColor) {
		edu.cmu.cs.dennisc.scenegraph.graphics.MainTitle mainTitle = new edu.cmu.cs.dennisc.scenegraph.graphics.MainTitle();
		mainTitle.text.setValue( text );
		mainTitle.font.setValue( font.getAsAWTFont() );
		mainTitle.textColor.setValue( textColor.getInternal() );
		mainTitle.fillColor.setValue( backgroundColor.getInternal() );
		mainTitle.outlineColor.setValue( edu.cmu.cs.dennisc.color.Color4f.createNaN() );
		displayGraphic( mainTitle, duration );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showTitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor  ) {
		showTitle( text, duration, font, textColor, Color.WHITE);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showTitle( String text, Double duration, org.alice.apis.moveandturn.Font font ) {
		showTitle( text, duration, font, Color.BLACK);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showTitle( String text, Double duration ) {
		Font font = new org.alice.apis.moveandturn.Font();
		showTitle( text, duration, font.deriveSizeFont(32));
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showTitle( String text ) {
		showTitle( text, 1.0 );
	}
	
	
	//Adam's code
	
	

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	protected void displayKineticTypo( KineticText kineticText, double duration, double cDuration) {
		perform( new KineticTextAnimation( this, 0.0, duration, cDuration, kineticText ) );
	}

	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndResizeWords( String s,TextSize startSize, TextSize endSize) {
		int DEPR = 0;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
		Point p = new Point(width/2,height/2);
		displayKineticTypo( new KineticText(s,p,DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 ), 2.0, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndResizeWords( String s,TextSize startSize, TextSize endSize, double duration) {
		int DEPR = 0;
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
		displayKineticTypo( new KineticText(s,new Point(width/2,height/2),DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 ), duration, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showAndResizeWords( String s, TextSize startSize, TextSize endSize, double duration , Transformable t) {
		int DEPR = 0;
		Point corners[]= getCornerPoints(t);
		Point p= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		displayKineticTypo( new KineticText(s,p,DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 ), duration, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndResizeWordsAtPoint( String s, TextSize startSize, TextSize endSize, TextLocation tl) {
		int DEPR = 0;
		int max = Math.max(startSize.getSize(), endSize.getSize());

		Point p  = getPoint(tl.getLoc(),0, max,s);
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, 2.0) ;
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndResizeWordsAtPoint( String s, TextSize startSize, TextSize endSize, TextLocation tl, double duration) {
		int DEPR = 0;
		int max = Math.max(startSize.getSize(), endSize.getSize());

		Point p  = getPoint(tl.getLoc(),0, max,s);
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, duration) ;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showAndResizeWordsAtPoint( String s, TextSize startSize, TextSize endSize, TextLocation tl, double duration, double percentMarginLeft ) {
		if(percentMarginLeft>1) percentMarginLeft =1;
		int DEPR = 0;
		int max = Math.max(startSize.getSize(), endSize.getSize());

		Point p  = getPoint(tl.getLoc(),percentMarginLeft, max,s);
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,startSize.getSize(), endSize.getSize(), 0, 0, 7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, duration) ;
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndTurnWords( String s) {
		int DEPR = 0;
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
	
		int endD2 = 360;
		displayKineticTypo( new KineticText(s,new Point(width/2,height/2),DEPR,DEPR, 1,20, 20,0, endD2,  7 ), 2.0, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndTurnWords( String s, TextSpin ts) {
		int DEPR = 0;
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
	
		int endD2 = 360*ts.getDir();
		displayKineticTypo( new KineticText(s,new Point(width/2,height/2),DEPR,DEPR, 1,20, 20,0, endD2,  7 ), 2.0, 0.0 );

	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndTurnWords( String s, TextSpin ts, double endD) {
		int DEPR = 0;
		//if(endD>1) endD =1;
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
		endD = endD*360*ts.getDir();
		int endD2 = (int)endD;
		displayKineticTypo( new KineticText(s,new Point(width/2,height/2),DEPR,DEPR, 1,20, 20,0, endD2,  7 ), 2.0, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndTurnWords( String s,  TextSpin ts, double endD, double duration) {
		int DEPR = 0;
		//if(endD>1) endD =1;
		//Point corners[]= getCornerPoints(t,bb);
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
		endD = endD*360*ts.getDir();
		int endD2 = (int)endD;
		displayKineticTypo( new KineticText(s,new Point(width/2,height/2),DEPR,DEPR, 1,20, 20,0, endD2,  7 ), duration, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void showAndTurnWords( String s,   TextSpin ts, double endD,double duration , Transformable t) {
		int DEPR = 0;
	
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point corners[]= getCornerPoints(t);
		Point p= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		int endD2 = (int)(endD*360*ts.getDir());
		displayKineticTypo( new KineticText(s,p,DEPR,DEPR, 1,20, 20,0, endD2,  7 ),duration, 0.0 );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndTurnWordsAtPoint( String s,  TextSpin ts ,double endD2) {
		int DEPR = 0;
		TextLocation tl = TextLocation.MidCenter;
		Point p= getPoint(tl.getLoc(),0,20,s);
		int endD = (int)(endD2*360*ts.getDir());


		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0, endD,  7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt,2.0, 0.0) ;

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndTurnWordsAtPoint( String s,  TextSpin ts ,double endD2,  TextLocation tl) {
		int DEPR = 0;

		Point p= getPoint(tl.getLoc(),0,20,s);
		int endD = (int)(endD2*360*ts.getDir());


		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0, endD,  7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt,2.0, 0.0) ;

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void showAndTurnWordsAtPoint( String s,  TextSpin ts ,double endD2,  TextLocation tl, double duration) {
		int DEPR = 0;

		Point p= getPoint(tl.getLoc(),0,20,s);
		int endD = (int)(endD2*360*ts.getDir());

		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0, endD,  7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt,duration, 0.0);

	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void showAndTurnWordsAtPoint( String s,   TextSpin ts ,double endD2,TextLocation tl, double duration, double percentageFromLeft) {
		int DEPR = 0;
		if(percentageFromLeft>1) percentageFromLeft =1;
		int endD = (int)(endD2*360*ts.getDir());
		
		Point p= getPoint(tl.getLoc(),percentageFromLeft,20,s);
		
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0, endD,  7 );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt,duration, 0.0) ;

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndMoveWords( String s, Transformable t, TextMoveDirection dir) {
		int DEPR = 0;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point corners[]= getCornerPoints(t);
		Point p= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		p = new Point(0,30);
		
		displayKineticTypo( new KineticText(s,p,DEPR,DEPR, 1,20, 20,0,0,  dir.getDir() ), 1.0, 2.0 );

	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showAndMoveWords( String s, Transformable t, TextMoveDirection dir , double duration) {
		int DEPR = 0;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point corners[]= getCornerPoints(t);
		Point p= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		p = new Point(0,30);
		
		displayKineticTypo( new KineticText(s,p,DEPR,DEPR, 1,20, 20,0,0,  dir.getDir() ), 1.0, duration );

	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndMoveWordsAtPoint( String s, TextLocation tl, TextMoveDirection dir) {
		int DEPR = 0;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point p  = getPoint(tl.getLoc(),0,20,s);
		
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0,0,  dir.getDir() );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, 2.0) ;


	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showAndMoveWordsAtPoint( String s, TextLocation tl, TextMoveDirection dir , double duration) {
		int DEPR = 0;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point p  = getPoint(tl.getLoc(),0,20,s);
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0,0,  dir.getDir() );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, duration) ;

	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showAndMoveWordsAtPoint( String s, TextLocation tl, TextMoveDirection dir , double duration, double percentMarginLeft) {
		int DEPR = 0;
		if(percentMarginLeft>1) percentMarginLeft =1;
		//Point corners[]= getCornerPoints(t,bb);
		//Point topLeft= new Point(((corners[2].x+corners[3].x)/2),corners[2].y);
		Point p  = getPoint(tl.getLoc(),percentMarginLeft, 20,s);
		KineticText kt = new KineticText(s,p,DEPR,DEPR, 1,20, 20,0,0,  dir.getDir() );
		setOffset(kt,tl.getLoc());
		displayKineticTypo(kt, 1.0, duration) ;

	}
	
	private void setOffset(KineticText kt, int i){
		if(i == 1) {
			kt.left = true;
			kt.top = true;
		}
		else if(i == 2) {
			kt.top = true;
			kt.right = true;
		}
		else if(i ==3) {
		
			kt.top = true;
		}
		else if(i ==4) {
			kt.left = true;
		}
		else if(i ==5) {
			kt.right = true;
		}
		else if(i ==7) {
			kt.left = true;
			kt.bottom = true;
		}
		else if(i ==8) {
			kt.bottom = true;
			kt.right = true;
		}
		else if(i ==9) {
		
			kt.bottom = true;
		}

	}
	
	private Point getPoint(int i, double margin, int size, String theString ){
		int width = this.getLookingGlass().getWidth();
		int height = this.getLookingGlass().getHeight();
		Point p = new Point(0,0);
		if(i ==1) p= new Point(0,0);
		else if (i ==2) p= new Point(width,0);
		else if (i ==3) p= new Point(width/2,0);
		else if (i ==4) p= new Point(0,height/2);
		else if (i ==5) p= new Point(width,height/2);
		else if (i ==6) p= new Point(width/2,height/2);
		else if (i ==7) p= new Point(0,height);
		else if (i ==8) p= new Point(width,height);
		else if (i ==9) p= new Point(width/2,height);
		double temp  = width - p.x;
		temp = temp*margin;
		p.x = p.x+(int)temp;
		
		return p;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor, org.alice.apis.moveandturn.Color backgroundColor) {
		edu.cmu.cs.dennisc.scenegraph.graphics.Subtitle subtitle = new edu.cmu.cs.dennisc.scenegraph.graphics.Subtitle();
		subtitle.text.setValue( text );
		subtitle.font.setValue( font.getAsAWTFont() );
		subtitle.textColor.setValue( textColor.getInternal() );
		subtitle.fillColor.setValue( backgroundColor.getInternal() );
		subtitle.outlineColor.setValue( edu.cmu.cs.dennisc.color.Color4f.createNaN() );
		displayGraphic( subtitle, duration );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor  ) {
		showSubtitle( text, duration, font, textColor, Color.WHITE);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font ) {
		showSubtitle( text, duration, font, Color.BLACK);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showSubtitle( String text, Double duration ) {
		Font font = new org.alice.apis.moveandturn.Font();
		showSubtitle( text, duration, font.deriveSizeFont(24));
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showSubtitle( String text ) {
		showSubtitle( text, 1.0 );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void showUpperSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor, org.alice.apis.moveandturn.Color backgroundColor) {
		edu.cmu.cs.dennisc.scenegraph.graphics.Overtitle overtitle = new edu.cmu.cs.dennisc.scenegraph.graphics.Overtitle();
		overtitle.text.setValue( text );
		overtitle.font.setValue( font.getAsAWTFont() );
		overtitle.textColor.setValue( textColor.getInternal() );
		overtitle.fillColor.setValue( backgroundColor.getInternal() );
		overtitle.outlineColor.setValue( edu.cmu.cs.dennisc.color.Color4f.createNaN() );
		displayGraphic( overtitle, duration );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showUpperSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font, org.alice.apis.moveandturn.Color textColor  ) {
		showUpperSubtitle( text, duration, font, textColor, Color.WHITE);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showUpperSubtitle( String text, Double duration, org.alice.apis.moveandturn.Font font ) {
		showUpperSubtitle( text, duration, font, Color.BLACK);
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showUpperSubtitle( String text, Double duration ) {
		Font font = new org.alice.apis.moveandturn.Font();
		showUpperSubtitle( text, duration, font.deriveSizeFont(24));
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void showUpperSubtitle( String text ) {
		showUpperSubtitle( text, 1.0 );
	}

	
//	public CharacterViewAnimation ( edu.wustl.cse.ckelleher.apis.walkandtouch.SymmetricPerspectiveCamera subject, org.alice.apis.moveandturn.Transformable asSeenBy, double duration ) {
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void getCharacterView(org.alice.apis.moveandturn.Transformable target, double duration) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.CharacterViewAnimation(this, target, duration));
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private boolean isHighlighted(PolygonalModel model){
		return highlightedContainers.contains(model);
	}
	
	//Changes model to have 20% opacity
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void translucentize(org.alice.apis.moveandturn.PolygonalModel model){
		model.setOpacity(0,.1);
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void fadeUnusedModels(){
		assert scene != null;
		for (Transformable m : scene.getComponents()){
			if (m instanceof org.alice.apis.moveandturn.PolygonalModel && (!(m instanceof PolygonalModel) || !isHighlighted((PolygonalModel)m)))
				translucentize((org.alice.apis.moveandturn.PolygonalModel)m);
		}
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void makeOpaque(org.alice.apis.moveandturn.PolygonalModel model){
		model.setOpacity(1,0);
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void returnModelsToNormal(){
		assert scene != null;
		for (Transformable m : scene.getComponents()){
			if (m instanceof org.alice.apis.moveandturn.PolygonalModel) 
				makeOpaque((org.alice.apis.moveandturn.PolygonalModel)m);
		}
		
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void doHighlight(HighlightedContainer container) {
		//todo: check for null?
		container.getDirectAccess().highlight();
		for (PolygonalModel model : container.getModels()){
			makeOpaque(model);
		}
		highlightedContainers.add(container);
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void highlightActionsOf(PolygonalModel model) {
		if (scene == null) scene = model.getScene();
		delay(0);
		doHighlight(new HighlightedContainer(new PolygonalModel[] {model},this));
		
		fadeUnusedModels();
		delay(0);
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void highlightActionsOf(PolygonalModel model1, PolygonalModel model2) {
		if (scene == null) scene = model1.getScene();
		delay(0);		
		doHighlight(new HighlightedContainer(new PolygonalModel[] {model1,model2},this));
		
		fadeUnusedModels();
		delay(0);
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	private void doUnHighlight(PolygonalModel[] models){
		delay(0);
		highlightedContainers.remove(models);
		if (highlightedContainers.isEmpty()){
			returnModelsToNormal();
		}
		else{
			for (PolygonalModel model : models)
			translucentize(model);
		}
		delay(0);
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void unHighlightActionsOf(PolygonalModel model) {
		delay(0);
		doUnHighlight(new PolygonalModel[] {model});
		delay(0);
	}
	
	//Will try to find the animation associated with both models. 
	//If there is not one with both, it will see if both have separate highlights and will unhighlight each
	//If only one is highlighted, will unhighlight that one
	//Otherwise returns without doing anything
	//In general, assumes both models are in same scene
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void unHighlightActionsOf(PolygonalModel model1, PolygonalModel model2) {
		delay(1);
		doUnHighlight(new PolygonalModel[]{model1,model2});
		delay(1);
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void unHighlightActionsOfAll() {
		delay(0);
		highlightedContainers.removeAll();
		returnModelsToNormal();
		delay(0);
	}
	
	
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getCharacterView(org.alice.apis.moveandturn.Transformable target) {
		getCharacterView(target, 2.0);
	}
	
//	public TwoShotAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.SymmetricPerspectiveCamera subject, org.alice.apis.moveandturn.Transformable asSeenBy, org.alice.apis.moveandturn.Transformable asSeenBy2, edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation spatialRelation, double amount, double duration) {
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void getTwoShotOf(org.alice.apis.moveandturn.Transformable target1, org.alice.apis.moveandturn.Transformable target2, SpatialRelation spatialRelation, double amount, double duration) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.TwoShotAnimation(this, target1, target2, spatialRelation, amount, duration));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getTwoShotOf(org.alice.apis.moveandturn.Transformable target1, org.alice.apis.moveandturn.Transformable target2, SpatialRelation spatialRelation, double amount) {
		getTwoShotOf(target1, target2, spatialRelation, amount, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getTwoShotOf(org.alice.apis.moveandturn.Transformable target1, org.alice.apis.moveandturn.Transformable target2, SpatialRelation spatialRelation) {
		getTwoShotOf(target1, target2, spatialRelation, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getTwoShotOf(org.alice.apis.moveandturn.Transformable target1, org.alice.apis.moveandturn.Transformable target2) {
		getTwoShotOf(target1, target2, SpatialRelation.IN_FRONT_OF);
	}
	
//	public CloseUpAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.SymmetricPerspectiveCamera subject, edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation spatialRelation, double amount, org.alice.apis.moveandturn.Transformable asSeenBy, double duration ) {
//	public CloseUpAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.SymmetricPerspectiveCamera subject, edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation spatialRelation, double amount, org.alice.apis.moveandturn.Transformable asSeenBy, double duration ) {
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public void getCloseUpOf(org.alice.apis.moveandturn.Transformable asSeenBy, SpatialRelation spatialRelation, double amount, double duration, double bubbleSpace) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.CloseUpAnimation(this, spatialRelation, amount, asSeenBy, duration, bubbleSpace));
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void getCloseUpOf(org.alice.apis.moveandturn.Transformable asSeenBy, SpatialRelation spatialRelation, double amount, double duration) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.CloseUpAnimation(this, spatialRelation, amount, asSeenBy, duration));
	}	
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getCloseUpOf(org.alice.apis.moveandturn.Transformable asSeenBy, SpatialRelation spatialRelation, double amount) {
		getCloseUpOf(asSeenBy, spatialRelation, amount, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getCloseUpOf(org.alice.apis.moveandturn.Transformable asSeenBy, SpatialRelation spatialRelation) {
		getCloseUpOf(asSeenBy, spatialRelation, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void getCloseUpOf(org.alice.apis.moveandturn.Transformable asSeenBy) {
		getCloseUpOf(asSeenBy, SpatialRelation.IN_FRONT_OF);
	}	
	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().setAxesOnly( axes, asSeenBy );
//		
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().setAxesOnly( axes, asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void setPositionRightNow(edu.cmu.cs.dennisc.math.Point3 posAbs, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().setTranslationOnly(  new edu.cmu.cs.dennisc.math.Point3(posAbs.x,posAbs.y,posAbs.z), asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void setPositionRightNow(edu.cmu.cs.dennisc.math.Point3 posAbs, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().setTranslationOnly(  new edu.cmu.cs.dennisc.math.Point3(posAbs.x,posAbs.y,posAbs.z), asSeenBy );
//	}
//	
//	//m_subject.pointAtRightNow(m_asSeenBy, new edu.cmu.cs.dennisc.math.VectorD3(0,m_cameraHeight,0), new edu.cmu.cs.dennisc.math.VectorD3(0,1,0), m_asSeenBy, false);
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void pointAtRightNow(org.alice.apis.moveandturn.Transformable subject, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upGuide, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		subject.getSGTransformable().setAxesOnlyToPointAt( asSeenBy.getSGTransformable(), offset, upGuide );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAt(org.alice.apis.moveandturn.Transformable target, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upguide ) {
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 currentOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		this.getSGTransformable().setAxesOnlyToPointAt( target.getSGTransformable(), offset, upguide );
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 pointAtOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		this.getSGTransformable().setAxesOnly( currentOrientation, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		
//		return pointAtOrientation;
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculateTurnToFace(org.alice.apis.moveandturn.Transformable target, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upguide ) {
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 currentOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 mSelf = this.getSGReferenceFrame().getAbsoluteTransformation();
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 mTarget = target.getSGReferenceFrame().getAbsoluteTransformation();
//		if( offset != null ) {
//			mTarget.applyTranslation(offset);
////			edu.cmu.cs.dennisc.math.LinearAlgebra.applyTranslation( mTarget, offset );
//		}
//		
//		edu.cmu.cs.dennisc.math.Vector3 forward = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( mTarget.translation, mSelf.translation );
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m0 = mSelf.orientation;
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m1 = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromForwardAndUpGuide( forward, upguide );
//		
//		double yaw0 = Math.atan2( m0.backward.x, m0.backward.z );
//		double yaw1 = Math.atan2( m1.backward.x, m1.backward.z );
//
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsolute = this.getSGTransformable().getInverseAbsoluteTransformation();
//		edu.cmu.cs.dennisc.math.Vector3 axis = new edu.cmu.cs.dennisc.math.Vector3( 0, 1, 0 );
//		inverseAbsolute.transform( axis );
//
//		double yawDelta = yaw1 - yaw0;
//		if( yaw1 > yaw0 ) {
//			if( yawDelta > Math.PI ) {
//				yawDelta -= 2 * Math.PI;
//			}
//		} else {
//			if( yawDelta < -Math.PI ) {
//				yawDelta += 2 * Math.PI;
//			}
//		}
//		
//		this.getSGTransformable().applyRotationAboutArbitraryAxis( axis, new AngleInRadians(yawDelta) );
//
//
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 pointAtOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		this.getSGTransformable().setAxesOnly( currentOrientation, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		
//		return pointAtOrientation;
//	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculateTurnToFace(org.alice.apis.moveandturn.Transformable target, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upguide ) {
		assert offset == null;
		return calculateTurnToFaceAxes( target );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setPositionRightNow( edu.cmu.cs.dennisc.math.Point3 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.moveTo( standIn, RIGHT_NOW );
		} finally {
			releaseStandIn( standIn );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.orientTo( standIn, RIGHT_NOW );
		} finally {
			releaseStandIn( standIn );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setTransformationRightNow( edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.moveAndOrientTo( standIn, RIGHT_NOW );
		} finally {
			releaseStandIn( standIn );
		}
	}
	
	
	//Adam Method to get corner points of transformable
	public Point[] getCornerPoints(Transformable t){
		AxisAlignedBox bb = t.getAxisAlignedMinimumBoundingBox();
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		offsetAsSeenBySubject.x =  bb.getXMinimum();
		offsetAsSeenBySubject.y = bb.getYMinimum();
		offsetAsSeenBySubject.z = bb.getZMinimum();
		offsetAsSeenBySubject.w = 1.0;
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera = t.getSGTransformable().transformTo_New( offsetAsSeenBySubject, this.getSGCamera() );

		java.awt.Point p1 = this.getSGCamera().transformToAWT_New( offsetAsSeenByCamera, this.getLookingGlass() );

		//Point conversion for top of bb
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject2 = new edu.cmu.cs.dennisc.math.Vector4();
		offsetAsSeenBySubject2.x =  bb.getXMaximum();
		offsetAsSeenBySubject2.y = bb.getYMinimum();
		offsetAsSeenBySubject2.z = bb.getZMinimum();
		offsetAsSeenBySubject2.w = 1.0;
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera2 = t.getSGTransformable().transformTo_New( offsetAsSeenBySubject2, this.getSGCamera() );

		java.awt.Point p2 = this.getSGCamera().transformToAWT_New( offsetAsSeenByCamera2, this.getLookingGlass() );

		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject3 = new edu.cmu.cs.dennisc.math.Vector4();
		offsetAsSeenBySubject3.x =  bb.getXMinimum();
		offsetAsSeenBySubject3.y = bb.getYMaximum();
		offsetAsSeenBySubject3.z = bb.getZMinimum();
		offsetAsSeenBySubject3.w = 1.0;
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera3 = t.getSGTransformable().transformTo_New( offsetAsSeenBySubject3, this.getSGCamera() );

		java.awt.Point p3 = this.getSGCamera().transformToAWT_New( offsetAsSeenByCamera3, this.getLookingGlass() );

		//Point conversion for top of bb
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject4 = new edu.cmu.cs.dennisc.math.Vector4();
		offsetAsSeenBySubject4.x =  bb.getXMaximum();
		offsetAsSeenBySubject4.y = bb.getYMaximum();
		offsetAsSeenBySubject4.z = bb.getZMinimum();
		offsetAsSeenBySubject4.w = 1.0;
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenByCamera4 = t.getSGTransformable().transformTo_New( offsetAsSeenBySubject4, this.getSGCamera() );

		java.awt.Point p4 = this.getSGCamera().transformToAWT_New( offsetAsSeenByCamera4, this.getLookingGlass() );
		Point ps[]= {p1,p2,p3,p4};
		return ps;
		
	}
	
	public enum TextMoveDirection {
		LEFT    ( 1 ),
		RIGHT   ( 2 ),
		UP ( 3 ),
		DOWN( 4 );
		
		private int m_dir;
		
		private TextMoveDirection( int dir ) {
			m_dir = dir;
		}
		/*package protected*/ int getDir() {
			return m_dir;
		}
	}
	
	public enum TextSize {
		TINY (5),
		SMALL    ( 10 ),
		MEDIUM   ( 20 ),
		LARGE ( 40 ),
		XLARGE( 80 );
		
		private int m_size;
		
		private TextSize( int size ) {
			m_size = size;
		}
		/*package protected*/ int getSize() {
			return m_size;
		}
	}
	
	public enum TextLocation {
	
		TopLeft    ( 1 ),
		TopRight   ( 2 ),
		TopCenter ( 3 ),
		MidLeft    ( 4 ),
		MidRight   ( 5 ),
		MidCenter ( 6 ),
		BottomLeft( 7 ),
		BottomRight (8),
		BottomCenter (9);
		
		private int m_loc;
		
		private TextLocation( int loc ) {
			m_loc = loc;
		}
		/*package protected*/ int getLoc() {
			return m_loc;
		}

	}
	
	public enum TextSpin {
		
		LEFT    ( 1 ),
		RIGHT   ( -1 );
		
		private int m_loc;
		
		private TextSpin( int loc ) {
			m_loc = loc;
		}
		/*package protected*/ int getDir() {
			return m_loc;
		}

	}
	



}

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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.alice.apis.moveandturn.SceneOwner;
import org.alice.apis.moveandturn.graphic.animation.OverlayGraphicAnimation;

import edu.cmu.cs.dennisc.animation.AnimationObserver;
import edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.CharacterFrameOverlay;
import edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.CharactersFrameOverlay;
import edu.cmu.cs.dennisc.scenegraph.Graphic;;


class CharacterFrameOverlayDirectAccess {

	private org.alice.apis.moveandturn.Composite composite;
	private Graphic graphic = null;
	private edu.cmu.cs.dennisc.scenegraph.Layer layer;
	
//	not used:
//	private boolean isHighlighted = false;
	
	public CharacterFrameOverlayDirectAccess(org.alice.apis.moveandturn.Composite composite, Graphic graphic ) {
		this.composite = composite;
		this.graphic = graphic;
	}
	public void highlight() {
		org.alice.apis.moveandturn.AbstractCamera camera = this.composite.findFirstCamera();
		this.layer = camera.getPostRenderLayer();
		this.graphic.setParent( this.layer );
	}
	
	public void unHighlight(){
		assert this.layer != null;
		this.graphic.setParent( null );
	}
//	not used:
//	public boolean isActive(){
//		return this.isHighlighted;
//	}
}

public class HighlightedContainer{
	//May remove and add models to this container so need dynamic list
	private LinkedList<PolygonalModel> models = new LinkedList<PolygonalModel>();
	private CharacterFrameOverlayDirectAccess directAccess;
	private org.alice.apis.moveandturn.Composite composite;

	public HighlightedContainer(PolygonalModel[] models, org.alice.apis.moveandturn.Composite composite){
		for (PolygonalModel model : models){
			this.models.add(model);
		}
		this.composite = composite;
		assert models.length > 0;
		resizeContainer();
	}
	
	public boolean contains(PolygonalModel model){
//		System.out.println("Checking if " + model.getName() + " is highlighted and returning " + models.contains(model));
		return models.contains(model);
	}
	
	public boolean containsAll(PolygonalModel[] models){
		int numContained = 0;
		for (PolygonalModel model : models){
			if (this.models.contains(model))
				numContained++;
			if (numContained == models.length) 
				return true;
		}
		return false;
	}
	
	public PolygonalModel[] getModels(){
		return models.toArray(new PolygonalModel[]{}); 
	}
	
	public boolean removeModels(PolygonalModel[] modelsToDelete){
		java.util.Vector<PolygonalModel> modelsToRemove = new java.util.Vector<PolygonalModel>();
		boolean removedSuccessfully = false;
		for (PolygonalModel highlightedModel : models){
			for (PolygonalModel modelToDelete : modelsToDelete){
				if (highlightedModel == modelToDelete){
					modelsToRemove.add(modelToDelete);
				}
			}
		}
		for (PolygonalModel model : modelsToRemove){
			removedSuccessfully = removedSuccessfully ? removedSuccessfully : models.remove(model);
		}
		
		resizeContainer();
		return false;
	}
	public boolean removeModel(PolygonalModel model){
		boolean removedSuccessfully = models.remove(model);
		resizeContainer();
		return removedSuccessfully;
	}
	
	public boolean isEmpty(){
		return models.isEmpty();
	}
	
	public CharacterFrameOverlayDirectAccess getDirectAccess() {
		return this.directAccess;
	}
	/**
	 * 
	 * @return whether the animation was able to resize. True if there are models left, false otherwise.
	 */
	private boolean resizeContainer(){
		if (models.size() >0){
			System.out.println("Animation is getting set");
			if (directAccess != null) directAccess.unHighlight();
			directAccess = new CharacterFrameOverlayDirectAccess( this.composite, new CharactersFrameOverlay(models.toArray(new PolygonalModel[]{})));
			directAccess.highlight();
			return true;
		}
		else{
			System.out.println("Animation is attempting to unhighlight...");
			directAccess.unHighlight();
			return false;
		}

	}
	
	@Override
	public String toString(){
		String returnString = "Highlighted Container includes...\n--Models--\n";
		for (PolygonalModel model : models){
			returnString = returnString + model.getName() + "\n"; 
		}
		returnString = returnString + "--DirectAccess--\n";
		returnString = returnString + directAccess.getClass().getSimpleName() + "\n";
		return returnString;
			
	}
}

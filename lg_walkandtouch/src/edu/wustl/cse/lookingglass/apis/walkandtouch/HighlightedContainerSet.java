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
import java.util.HashSet;

public class HighlightedContainerSet extends HashSet<HighlightedContainer>{
	private HashSet<HighlightedContainer> highlightedContainers = new HashSet<HighlightedContainer>();
	
	public HighlightedContainerSet(){
		
	}
	
	public boolean contains(PolygonalModel model) {
//		System.out.println("Checking if " + model.getName() + " is highlighted, set has  " + highlightedContainers.size() + " highlighted containers");
		for (HighlightedContainer container : highlightedContainers)
			if (container.contains(model))
				return true;
		return false;
	}

	/**
	 * 
	 * @param model to be removed
	 * @return true if something was removed, false otherwise
	 */
	private boolean remove(HighlightedContainer container,PolygonalModel model, java.util.Vector<HighlightedContainer> containersToRemove, boolean somethingWasRemoved) {
		if (container.contains(model)){
			boolean removedModel = container.removeModel(model);
			somethingWasRemoved = somethingWasRemoved ? somethingWasRemoved : removedModel;
			if (container.isEmpty()){
				containersToRemove.add(container);
			}
		}
		return somethingWasRemoved;
	}
	
	/**
	 * 
	 * @param models to be removed
	 * @return true if something was removed, false otherwise
	 */
	public boolean remove(PolygonalModel[] models){
		boolean somethingWasRemoved = false;
		//To prevent a concurrent modification exception, I make a list of containersToRemove instead of removing them in the loop
		java.util.Vector<HighlightedContainer> containersToRemove = new java.util.Vector<HighlightedContainer>();
		for (HighlightedContainer container : highlightedContainers){
			for (PolygonalModel model : models){
				//removes the model and adds the container to containersToRemove if it is empty
				somethingWasRemoved = remove(container,model,containersToRemove, somethingWasRemoved);
			}
		}
		for (HighlightedContainer container : containersToRemove){
			container.getDirectAccess().unHighlight();
			highlightedContainers.remove(container);
		}
		return somethingWasRemoved;
	}
	
	/**
	 * 
	 * @returns whether all containers have been removed 
	 */
	public boolean removeAll(){
		java.util.Vector<HighlightedContainer> containersToRemove = new java.util.Vector<HighlightedContainer>(); 
		for (HighlightedContainer container : highlightedContainers){
			containersToRemove.add(container);
		}
		for (HighlightedContainer container : containersToRemove){
			container.getDirectAccess().unHighlight();
			highlightedContainers.remove(container);
		}
		return highlightedContainers.isEmpty();
	}
	
	/**
	 * 
	 * @param model to find
	 * @return the HighlightedContainer containing that model, if there is one.  Otherwise, null.
	 */
	public HighlightedContainer findContainerWith(PolygonalModel model){
		for (HighlightedContainer container : highlightedContainers){
			if (container.contains(model)){
				return container;
			}
		}
		return null;
	}
	
	public HighlightedContainer findContainerWith(PolygonalModel[] models){
		for (HighlightedContainer container : highlightedContainers){
			if (container.containsAll(models)){
				return container;
			}
		}
		return null;
	}
	
	@Override
	public String toString(){
		String returnString = "HighlightedContainers:\n";
		for (HighlightedContainer container : highlightedContainers){
			returnString = returnString + container.toString();
		}
		return returnString;
	}

	//Redundant methods found in superclass, added so they use proper instance variable...
	
	@Override
	public boolean isEmpty(){
		return highlightedContainers.isEmpty();
	}
	
	@Override
	public int size(){
		return highlightedContainers.size();
	}
	
	@Override
	public boolean add(HighlightedContainer container){
		return highlightedContainers.add(container);
	}
	public boolean addAll(HighlightedContainer[] containers){
		return highlightedContainers.addAll(Arrays.asList(containers));
	}
	
	public boolean removeAll(HighlightedContainer[] containers){
		return highlightedContainers.removeAll(Arrays.asList(containers));
	}
}

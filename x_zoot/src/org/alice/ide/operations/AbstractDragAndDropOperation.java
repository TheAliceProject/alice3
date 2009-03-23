/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.operations;


/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDragAndDropOperation extends zoot.AbstractDragAndDropOperation {
	private zoot.ActionOperation dropOperation = this.createDropOperation();  
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	public java.util.List< ? extends zoot.DropReceptor > createListOfPotentialDropReceptors( zoot.ZDragComponent dragSource ) {
		return getIDE().createListOfPotentialDropReceptors( dragSource );
	}
	public void handleDragStarted( zoot.DragAndDropContext dragAndDropContext ) {
		getIDE().handleDragStarted( dragAndDropContext );
	}
	public void handleDragEnteredDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
		getIDE().handleDragEnteredDropReceptor( dragAndDropContext );
	}
	public void handleDragExitedDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
		getIDE().handleDragExitedDropReceptor( dragAndDropContext );
	}
	public void handleDragStopped( zoot.DragAndDropContext dragAndDropContext ) {
		getIDE().handleDragStopped( dragAndDropContext );
	}
	protected abstract zoot.ActionOperation createDropOperation(); 
	
	public zoot.ActionOperation getDropOperation() {
		return this.dropOperation;
	}
}

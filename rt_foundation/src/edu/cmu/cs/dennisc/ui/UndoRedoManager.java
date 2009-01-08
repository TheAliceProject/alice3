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
package edu.cmu.cs.dennisc.ui;

//todo: move to core?
/**
 * @author Dennis Cosgrove
 */
public abstract class UndoRedoManager {
	private java.util.Stack< edu.cmu.cs.dennisc.pattern.Action > m_undoStack = new java.util.Stack< edu.cmu.cs.dennisc.pattern.Action >();
	private java.util.Stack< edu.cmu.cs.dennisc.pattern.Action > m_redoStack = new java.util.Stack< edu.cmu.cs.dennisc.pattern.Action >();
	
	protected abstract void handleChange();
	public boolean isUndoStackEmpty() {
		return m_undoStack.isEmpty();
	}
	public boolean isRedoStackEmpty() {
		return m_redoStack.isEmpty();
	}
	
	public void runAndPush( edu.cmu.cs.dennisc.pattern.Action action ) {
		action.run();
		pushAlreadyRunActionOntoUndoStack( action );
	}
	
	public void pushAlreadyRunActionOntoUndoStack( edu.cmu.cs.dennisc.pattern.Action action ) {
		m_undoStack.push( action );
		m_redoStack.clear();
		handleChange();
	}
	public void undo() {
		edu.cmu.cs.dennisc.pattern.Action action = m_undoStack.pop();
		m_redoStack.push( action );
		action.undo();
		handleChange();
	}
	public void redo() {
		edu.cmu.cs.dennisc.pattern.Action action = m_redoStack.pop();
		action.redo();
		m_undoStack.push( action );
		handleChange();
	}
}


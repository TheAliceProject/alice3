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
package org.alice.ide.sceneeditor;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.croquet.DragAndDropContext;
import edu.cmu.cs.dennisc.croquet.DragComponent;
import edu.cmu.cs.dennisc.croquet.Operation;
import edu.cmu.cs.dennisc.croquet.ViewController;

/**
 * @author Dennis Cosgrove
 */
public class FauxSceneEditor extends AbstractSceneEditor {
	@Override
	public void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty ) {
		throw new RuntimeException();
	}
//	@Override
//	public void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, boolean isAnimationDesired ) {
//	}
	@Override
	public void enableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
	}
	@Override
	public void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
	}
	@Override
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
	}
	@Override
	public Object getInstanceInJavaForUndo( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		return null;
	}
	@Override
	public void putInstanceForInitializingPendingField(
			FieldDeclaredInAlice field, Object instance) {
		// TODO Auto-generated method stub
		
	}
	public Operation<?, ?> dragDropped(DragAndDropContext dragAndDropContext) {
		return null;
	}
	public void dragEntered(DragAndDropContext dragAndDropContext) {
	}
	public void dragExited(DragAndDropContext dragAndDropContext, boolean isDropRecipient) {
	}
	public void dragStarted(DragAndDropContext dragAndDropContext) {
	}
	public void dragStopped(DragAndDropContext dragAndDropContext) {
	}
	public void dragUpdated(DragAndDropContext dragAndDropContext) {
	}
	public ViewController<?, ?> getViewController() {
		return null;
	}
	public boolean isPotentiallyAcceptingOf(DragComponent source) {
		return false;
	}
	
}

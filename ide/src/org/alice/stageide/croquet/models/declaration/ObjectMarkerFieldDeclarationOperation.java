/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.croquet.models.declaration;

import java.text.MessageFormat;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.croquet.components.PanelWithPreview;
import org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation;
import org.lgna.croquet.history.InputDialogOperationStep;
import org.lgna.project.ast.UserField;
import org.lgna.story.Color;
import org.lgna.story.ImplementationAccessor;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.pattern.event.NameEvent;
import edu.cmu.cs.dennisc.pattern.event.NameListener;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;

/**
 * @author dculyba
 *
 */
public class ObjectMarkerFieldDeclarationOperation extends MarkerFieldDeclarationOperation {

	private static class SingletonHolder {
		private static ObjectMarkerFieldDeclarationOperation instance = new ObjectMarkerFieldDeclarationOperation();
	}
	public static ObjectMarkerFieldDeclarationOperation getInstance() {
		return SingletonHolder.instance;
	}

	public ObjectMarkerFieldDeclarationOperation() {
		super( java.util.UUID.fromString( "830f80f7-da68-43cc-be8a-bbd64da78c24" ),  org.lgna.story.ObjectMarker.class );
	}

	@Override
	protected void localize() {
		super.localize();
		String unformattedName = this.getName();
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(javax.swing.JComponent.getDefaultLocale());
		formatter.applyPattern(unformattedName);
		String defaultName = this.findLocalizedText( "defaultObjectName", ObjectMarkerFieldDeclarationOperation.class );
		String fieldNameParam = this.getSelectedField() == null ? defaultName : this.getSelectedField().getName();
		String formattedName = formatter.format(new Object[]{fieldNameParam});
		this.setName(formattedName);
	}
	
	@Override
	protected org.alice.stageide.croquet.components.declaration.MarkerDeclarationPanel<ObjectMarkerFieldDeclarationOperation> createMainComponent( ) {
		return new org.alice.stageide.croquet.components.declaration.MarkerDeclarationPanel<ObjectMarkerFieldDeclarationOperation>( this );
	}

	@Override
	protected Color getInitialMarkerColor() {
		return org.alice.stageide.StageIDE.getActiveInstance().getMainComponent().getSceneEditor().getColorForNewObjectMarker();
	}

	@Override
	protected String getInitialMarkerName(Color color) {
		return org.alice.stageide.StageIDE.getActiveInstance().getMainComponent().getSceneEditor().getSuggestedNameForNewObjectMarker(color);
	}

	@Override
	protected AffineMatrix4x4 getInitialMarkerTransform() {
		return org.alice.stageide.StageIDE.getActiveInstance().getMainComponent().getSceneEditor().getTransformForNewObjectMarker();
	}


}

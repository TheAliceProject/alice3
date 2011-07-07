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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends org.alice.ide.croquet.PopupButton< org.lgna.croquet.CascadePopupPrepModel< edu.cmu.cs.dennisc.alice.ast.Expression > > implements org.lgna.croquet.DropReceptor {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	public ExpressionPropertyDropDownPane( org.lgna.croquet.CascadePopupPrepModel< edu.cmu.cs.dennisc.alice.ast.Expression > model, org.lgna.croquet.components.Component< ? > prefixPane, org.lgna.croquet.components.Component< ? > component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		super( model, prefixPane, component, null );
		this.expressionProperty = expressionProperty;
	}
	public edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

//	@Override
//	protected int getInsetLeft() {
//		int rv = super.getInsetLeft();
//		if( org.alice.ide.IDE.getSingleton().getExpressionTypeFeedbackDesiredState().getValue() ) {
//			//pass
//		} else {
//			edu.cmu.cs.dennisc.croquet.Component< ? > mainComponent = this.getMainComponent();
//			if( mainComponent instanceof org.alice.ide.common.ExpressionPropertyPane ) {
//				org.alice.ide.common.ExpressionPropertyPane expressionPropertyPane = (org.alice.ide.common.ExpressionPropertyPane)mainComponent;
//				if( expressionPropertyPane.getComponentCount()==1 ) {
//					edu.cmu.cs.dennisc.croquet.Component< ? > component0 = expressionPropertyPane.getComponent( 0 );
//					if( component0 instanceof org.alice.ide.common.InstancePropertyPane ) {
//						//org.alice.ide.common.InstancePropertyPane instancePropertyPane = (org.alice.ide.common.InstancePropertyPane)component0;
//						rv += 2;
//					}
//				}
//			}
//		}
//		return rv;	
//	}

	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return "Drop...";
	}
	
	public org.lgna.croquet.resolvers.CodableResolver< ExpressionPropertyDropDownPane > getCodableResolver() {
		//todo:
		System.err.println( "todo: getCodableResolver" );
		return null;
	}
	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		return this;
	}

	public boolean isPotentiallyAcceptingOf( org.lgna.croquet.components.DragComponent source ) {
		return source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance;
	}
	public void dragStarted( org.lgna.croquet.history.DragStep context ) {
	}
	public void dragEntered( org.lgna.croquet.history.DragStep context ) {
		context.getDragSource().setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), this.getMainComponent(), this.getBounds().height, -1 );
	}
	public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep context ) {
		return null;
	}
	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep context ) {
		org.lgna.croquet.Model rv;
		org.lgna.croquet.components.DragComponent source = context.getDragSource();
		if( source instanceof org.alice.ide.common.ExpressionCreatorPane ) {
			final org.alice.ide.common.ExpressionCreatorPane expressionCreatorPane = (org.alice.ide.common.ExpressionCreatorPane)source;
			rv = expressionCreatorPane.getDropModel( context, this.expressionProperty );
		} else {
			rv = null;
		}
		if( rv != null ) {
			//pass
		} else {
//			source.hideDropProxyIfNecessary();
			context.cancelTransaction();
		}
		return rv;
	}
	public void dragExited( org.lgna.croquet.history.DragStep context, boolean isDropRecipient ) {
//		edu.cmu.cs.dennisc.croquet.DragComponent source = context.getDragSource();
//		source.hideDropProxyIfNecessary();
	}
	public void dragStopped( org.lgna.croquet.history.DragStep context ) {
	}
	
	public org.lgna.croquet.components.ViewController<?,?> getViewController() {
		return this;
	}

	@Override
	protected boolean isInactiveFeedbackDesired() {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = this.expressionProperty.getValue();
		if( expression != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
			if( parent instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression || parent instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplement ) { 
				edu.cmu.cs.dennisc.alice.ast.Node grandparent = parent.getParent();
				return grandparent instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation || grandparent instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression || grandparent instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess; 
			}
		}
		return super.isInactiveFeedbackDesired();
	}
}

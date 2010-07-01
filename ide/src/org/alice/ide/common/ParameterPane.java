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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends TransientPane<edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice> {
	private edu.cmu.cs.dennisc.alice.ast.NodeListProperty< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > parametersProperty;

	public ParameterPane( edu.cmu.cs.dennisc.alice.ast.NodeListProperty< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > parametersProperty, edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( parameter );
		this.parametersProperty = parametersProperty;
		this.addComponent( new org.alice.ide.common.DeclarationNameLabel( parameter ) );
		this.setBackgroundPaint( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.ParameterAccess.class ) );
		final org.alice.ide.operations.ast.RenameParameterOperation renameParameterOperation = new org.alice.ide.operations.ast.RenameParameterOperation( parameter );
		
		if( this.parametersProperty != null ) {
			final org.alice.ide.operations.ast.DeleteParameterOperation deleteParameterOperation = new org.alice.ide.operations.ast.DeleteParameterOperation( this.parametersProperty, parameter );
			final org.alice.ide.operations.ast.ForwardShiftParameterOperation forwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.ForwardShiftParameterOperation( this.parametersProperty, parameter );
			final org.alice.ide.operations.ast.BackwardShiftParameterOperation backwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.BackwardShiftParameterOperation( this.parametersProperty, parameter );
			this.setPopupMenuOperation( new edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation( java.util.UUID.fromString( "5b9b75d7-ce04-4f3d-8915-b825f357cef2" ) ) {
				@Override
				public edu.cmu.cs.dennisc.croquet.Model<?>[] getModels() {
					java.util.List< edu.cmu.cs.dennisc.croquet.Model > operations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
					operations.add( renameParameterOperation );
					if( forwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						operations.add( forwardShiftCodeParameterOperation );
					}
					if( backwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						operations.add( backwardShiftCodeParameterOperation );
					}
					operations.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
					operations.add( deleteParameterOperation );
					return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(operations, edu.cmu.cs.dennisc.croquet.Model.class);
				}
			} );
		} else {
			this.setPopupMenuOperation( new edu.cmu.cs.dennisc.croquet.PopupMenuOperation( java.util.UUID.fromString( "7a9b90a1-a645-4e13-aeef-9ca631baad55" ), renameParameterOperation ) );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getExpressionType() {
		return this.getTransient().getValueType();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( this.getTransient() );
	}
}

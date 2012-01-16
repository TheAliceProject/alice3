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

package org.alice.ide.declarationseditor.code.components;

/**
 * @author Dennis Cosgrove
 */
public class CodeDeclarationView extends org.alice.ide.declarationseditor.components.DeclarationView {
	private final org.alice.ide.codeeditor.CodeEditor codeEditor;
	public CodeDeclarationView( org.alice.ide.declarationseditor.DeclarationComposite composite ) {
		super( composite );
		this.codeEditor = new org.alice.ide.codeeditor.CodeEditor( (org.lgna.project.ast.AbstractCode)composite.getDeclaration() );
		this.setBackgroundColor( this.codeEditor.getBackgroundColor() );
		this.addComponent( this.codeEditor, Constraint.CENTER );
	}
	@Deprecated
	public org.alice.ide.codeeditor.CodeEditor getCodeDropReceptor() {
		return this.codeEditor;
	}
	@Override
	public boolean isPrintSupported() {
		return true;
	}
	public int print( java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex ) throws java.awt.print.PrinterException {
		return this.codeEditor.print( g, pageFormat, pageIndex );
	}
	
	@Override
	public void addPotentialDropReceptors( java.util.List< org.lgna.croquet.DropReceptor > out, org.alice.ide.croquet.models.IdeDragModel dragModel ) {
		if( dragModel instanceof org.alice.ide.ast.draganddrop.CodeDragModel ) {
			org.alice.ide.ast.draganddrop.CodeDragModel codeDragModel = (org.alice.ide.ast.draganddrop.CodeDragModel)dragModel;
			final org.lgna.project.ast.AbstractType< ?,?,? > type = codeDragModel.getType();
			if( type == org.lgna.project.ast.JavaType.VOID_TYPE ) {
				out.add( this.codeEditor );
			} else {
				java.util.List< org.alice.ide.codeeditor.ExpressionPropertyDropDownPane > list = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.codeeditor.ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< org.alice.ide.codeeditor.ExpressionPropertyDropDownPane >() {
					public boolean accept( org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
						org.lgna.project.ast.AbstractType<?,?,?> expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
						assert expressionType != null : expressionPropertyDropDownPane.getExpressionProperty();
						if( expressionType.isAssignableFrom( type ) ) {
							return true;
//						} else {
//							if( type.isArray() ) {
//								if( expressionType.isAssignableFrom( type.getComponentType() ) ) {
//									return true;
//								} else {
//									for( org.lgna.project.ast.JavaType integerType : org.lgna.project.ast.JavaType.INTEGER_TYPES ) {
//										if( expressionType == integerType ) {
//											return true;
//										}
//									}
//								}
//							}
						}
						return false;
					}
				} );
				out.addAll( list );
			}
		}
	}
}

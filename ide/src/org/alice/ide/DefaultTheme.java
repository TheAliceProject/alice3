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
package org.alice.ide;

import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class DefaultTheme implements Theme {
	private static final java.awt.Color DEFAULT_PROCEDURE_COLOR = new java.awt.Color( 0xb2b7d9 );
	private static final java.awt.Color DEFAULT_FUNCTION_COLOR = new java.awt.Color( 0xb0c9a4 );
	private static final java.awt.Color DEFAULT_CONSTRUCTOR_COLOR = new java.awt.Color( 0xadc0ab );
	private static final java.awt.Color DEFAULT_FIELD_COLOR = new java.awt.Color( 230, 230, 210 );
	private static final java.awt.Color DEFAULT_SELECTED_COLOR = new java.awt.Color(255, 255, 179);
	private static final java.awt.Color DEFAULT_UNSELECTED_COLOR = new java.awt.Color(141, 137, 166);
	private static final java.awt.Color DEFAULT_PRIMARY_BACKGROUND_COLOR = new java.awt.Color(173, 167, 208);
	private static final java.awt.Color DEFAULT_SECONDARY_BACKGROUND_COLOR = new java.awt.Color(201, 201, 218);

	public java.awt.Color getProcedureColor() {
		return DEFAULT_PROCEDURE_COLOR;
	}
	public java.awt.Color getFunctionColor() {
		return DEFAULT_FUNCTION_COLOR;
	}
	public java.awt.Color getConstructorColor() {
		return DEFAULT_CONSTRUCTOR_COLOR;
	}
	public java.awt.Color getFieldColor() {
		return DEFAULT_FIELD_COLOR;
	}
	public java.awt.Color getLocalColor() {
		return getFieldColor();
	}
	public java.awt.Color getParameterColor() {
		return getFieldColor();
	}

	public java.awt.Paint getPaintFor( Class< ? extends org.lgna.project.ast.Statement > cls, int x, int y, int width, int height ) {
		java.awt.Color color = this.getColorFor( cls );
		if( org.lgna.project.ast.Comment.class.isAssignableFrom( cls ) ) {
			return color;
		} else {
			if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, org.lgna.project.ast.DoTogether.class, org.lgna.project.ast.EachInArrayTogether.class, org.lgna.project.ast.DoInThread.class ) ) {
				java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 0.85 );
				java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 1.0, 1.15 );
				return new java.awt.GradientPaint( x, y, colorA, x + 200, y, colorB );
			} else {
				return color;
				//return new java.awt.GradientPaint( x, y, colorB, x, y + 64, color );
			}
		}
	}
	public java.awt.Color getColorFor( Class< ? extends org.lgna.project.ast.Node > cls ) {
		if( org.lgna.project.ast.Statement.class.isAssignableFrom( cls ) ) {
			if( org.lgna.project.ast.Comment.class.isAssignableFrom( cls ) ) {
				return edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 245 );
			} else {
//				if( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class.isAssignableFrom( cls ) ) {
//					return new java.awt.Color( 255, 230, 180 );
////				} else if( edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement.class.isAssignableFrom( cls ) ) {
////					return new java.awt.Color( 255, 230, 180 );
//				} else {
					return new java.awt.Color( 0xd3d7f0 );
					//return new java.awt.Color( 255, 255, 210 );
//				}
			}
		} else if( org.lgna.project.ast.Expression.class.isAssignableFrom( cls ) ) {
			if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, org.lgna.project.ast.MethodInvocation.class ) ) {
				return new java.awt.Color( 0xd3e7c7 );
			} else if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, org.lgna.project.ast.InfixExpression.class, org.lgna.project.ast.LogicalComplement.class, org.lgna.project.ast.StringConcatenation.class ) ) {
				return new java.awt.Color( 0xDEEBD3 );
			} else if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, org.lgna.project.ast.InstanceCreation.class, org.lgna.project.ast.ArrayInstanceCreation.class ) ) {
				return new java.awt.Color( 0xbdcfb3 );
			} else if( org.lgna.project.ast.ResourceExpression.class.isAssignableFrom( cls ) ) {
				return new java.awt.Color( 0xffffff );
			} else {
				if( org.lgna.project.ast.NullLiteral.class.isAssignableFrom( cls ) ) {
					return java.awt.Color.RED;
				} else {
					//return new java.awt.Color( 255, 255, 210 );
					return new java.awt.Color( 0xfdf6c0 );
				}
			}
		} else {
			return java.awt.Color.BLUE;
		}
	}
	public java.awt.Color getColorFor( org.lgna.project.ast.Node node ) {
		if( node != null ) {
			Class< ? extends org.lgna.project.ast.Node > cls = node.getClass();
//			if( node instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
//				edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)node;
//				if( fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
//					//pass
//				} else {
//					cls = edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class;
//				}
//			}
			return this.getColorFor( cls );
		} else {
			return java.awt.Color.RED;
		}
	}

	public java.awt.Color getCommentForegroundColor() {
		return new java.awt.Color( 0, 100, 0 );
	}

	public java.awt.Color getCodeDeclaredInAliceColor( org.lgna.project.ast.AbstractCode code ) {
		if( code instanceof org.lgna.project.ast.MethodDeclaredInAlice ) {
			org.lgna.project.ast.MethodDeclaredInAlice methodDeclaredInAlice = (org.lgna.project.ast.MethodDeclaredInAlice)code;
			if( methodDeclaredInAlice.isProcedure() ) {
				return getProcedureColor();
			} else {
				return getFunctionColor();
			}
		} else if( code instanceof org.lgna.project.ast.ConstructorDeclaredInAlice ) {
			return getConstructorColor();
		} else {
			return java.awt.Color.GRAY;
		}
	}
    public Color getSelectedColor()
    {
        return DEFAULT_SELECTED_COLOR;
    }
    public Color getUnselectedColor()
    {
        return DEFAULT_UNSELECTED_COLOR;
    }
    public Color getPrimaryBackgroundColor()
    {
        return DEFAULT_PRIMARY_BACKGROUND_COLOR;
    }
    public Color getSecondaryBackgroundColor()
    {
        // TODO Auto-generated method stub
        return DEFAULT_SECONDARY_BACKGROUND_COLOR;
    }
}

/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class DefaultTheme implements Theme {
	private static final java.awt.Color DEFAULT_NOUN_COLOR = new java.awt.Color( 0xFDF6C0 );
	public static final java.awt.Color DEFAULT_TYPE_COLOR = DEFAULT_NOUN_COLOR;
	public static final java.awt.Color DEFAULT_CONSTRUCTOR_COLOR = new java.awt.Color( 0xE6D4A3 );
	public static final java.awt.Color DEFAULT_FIELD_COLOR = new java.awt.Color( 0xD6AC8B );

	public static final java.awt.Color DEFAULT_PROCEDURE_COLOR = new java.awt.Color( 0xB2B7D9 );
	public static final java.awt.Color DEFAULT_FUNCTION_COLOR = new java.awt.Color( 0xADCF95 );
	//	private static final java.awt.Color DEFAULT_EVENT_COLOR = new Color( 100, 200, 100 );
	//	private static final java.awt.Color DEFAULT_EVENT_BODY_COLOR = DEFAULT_EVENT_COLOR.brighter().brighter(); //new Color( 150, 225, 150 );
	private static final java.awt.Color DEFAULT_EVENT_COLOR = new java.awt.Color( 0xd3d7f0 );
	private static final java.awt.Color DEFAULT_EVENT_BODY_COLOR = DEFAULT_PROCEDURE_COLOR;

	private static final java.awt.Color DEFAULT_SELECTED_COLOR = new java.awt.Color( 255, 255, 179 );
	private static final java.awt.Color DEFAULT_UNSELECTED_COLOR = new java.awt.Color( 141, 137, 166 );
	private static final java.awt.Color DEFAULT_PRIMARY_BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );
	private static final java.awt.Color DEFAULT_SECONDARY_BACKGROUND_COLOR = new java.awt.Color( 201, 201, 218 );

	@Override
	public java.awt.Color getTypeColor() {
		return DEFAULT_TYPE_COLOR;
	}

	@Override
	public java.awt.Color getMutedTypeColor() {
		return edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.getTypeColor(), 1.0, 0.9, 0.9 );
	}

	@Override
	public java.awt.Color getProcedureColor() {
		return DEFAULT_PROCEDURE_COLOR;
	}

	@Override
	public java.awt.Color getFunctionColor() {
		return DEFAULT_FUNCTION_COLOR;
	}

	@Override
	public java.awt.Color getConstructorColor() {
		return DEFAULT_CONSTRUCTOR_COLOR;
	}

	@Override
	public java.awt.Color getFieldColor() {
		return DEFAULT_FIELD_COLOR;
	}

	@Override
	public java.awt.Color getLocalColor() {
		return getFieldColor();
	}

	@Override
	public java.awt.Color getParameterColor() {
		return getFieldColor();
	}

	@Override
	public java.awt.Color getEventColor() {
		return DEFAULT_EVENT_COLOR;
	}

	@Override
	public java.awt.Color getEventBodyColor() {
		return DEFAULT_EVENT_BODY_COLOR;
	}

	@Override
	public java.awt.Paint getPaintFor( Class<? extends org.lgna.project.ast.Statement> cls, int x, int y, int width, int height ) {
		java.awt.Color color = this.getColorFor( cls );
		if( org.lgna.project.ast.Comment.class.isAssignableFrom( cls ) ) {
			return color;
		} else {
			if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( cls, org.lgna.project.ast.DoTogether.class, org.lgna.project.ast.EachInArrayTogether.class ) ) {
				java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 0.85 );
				java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 1.0, 1.15 );
				return new java.awt.GradientPaint( x, y, colorA, x + 200, y, colorB );
			} else {
				return color;
				//return new java.awt.GradientPaint( x, y, colorB, x, y + 64, color );
			}
		}
	}

	@Override
	public java.awt.Color getColorFor( Class<? extends org.lgna.project.ast.Node> cls ) {
		if( org.lgna.project.ast.Statement.class.isAssignableFrom( cls ) ) {
			if( org.lgna.project.ast.Comment.class.isAssignableFrom( cls ) ) {
				return edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 245 );
			} else {
				//				if( org.lgna.project.ast.ExpressionStatement.class.isAssignableFrom( cls ) ) {
				//					return new java.awt.Color( 255, 230, 180 );
				////				} else if( org.lgna.project.ast.LocalDeclarationStatement.class.isAssignableFrom( cls ) ) {
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
				//return new java.awt.Color( 0xbdcfb3 );
				return DEFAULT_CONSTRUCTOR_COLOR;
			} else if( org.lgna.project.ast.ResourceExpression.class.isAssignableFrom( cls ) ) {
				return new java.awt.Color( 0xffffff );
			} else {
				if( org.lgna.project.ast.NullLiteral.class.isAssignableFrom( cls ) ) {
					return java.awt.Color.RED;
				} else {
					return DEFAULT_NOUN_COLOR;
				}
			}
		} else if( org.lgna.project.ast.AbstractField.class.isAssignableFrom( cls ) ) {
			return this.getFieldColor();
		} else if( org.lgna.project.ast.AbstractParameter.class.isAssignableFrom( cls ) ) {
			return this.getParameterColor();
		} else if( org.lgna.project.ast.AbstractType.class.isAssignableFrom( cls ) ) {
			return this.getTypeColor();
		} else if( org.lgna.project.ast.UserLocal.class.isAssignableFrom( cls ) ) {
			return this.getLocalColor();
		} else {
			return java.awt.Color.BLUE;
		}
	}

	@Override
	public java.awt.Color getColorFor( org.lgna.project.ast.Node node ) {
		if( node != null ) {
			if( node instanceof org.lgna.project.ast.AbstractMethod ) {
				org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)node;
				if( method.isProcedure() ) {
					return this.getProcedureColor();
				} else {
					return this.getFunctionColor();
				}
			} else {
				Class<? extends org.lgna.project.ast.Node> cls = node.getClass();
				//				if( node instanceof org.lgna.project.ast.FieldAccess ) {
				//					org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)node;
				//					if( fieldAccess.expression.getValue() instanceof org.lgna.project.ast.TypeExpression ) {
				//						//pass
				//					} else {
				//						cls = org.lgna.project.ast.MethodInvocation.class;
				//					}
				//				}
				return this.getColorFor( cls );
			}
		} else {
			return java.awt.Color.RED;
		}
	}

	@Override
	public java.awt.Color getCommentForegroundColor() {
		return new java.awt.Color( 0, 100, 0 );
	}

	@Override
	public java.awt.Color getCodeColor( org.lgna.project.ast.Code code ) {
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)code;
			if( userMethod.isProcedure() ) {
				return getProcedureColor();
			} else {
				return getFunctionColor();
			}
		} else if( code instanceof org.lgna.project.ast.NamedUserConstructor ) {
			return getConstructorColor();
		} else {
			return java.awt.Color.GRAY;
		}
	}

	@Override
	public java.awt.Color getSelectedColor() {
		return DEFAULT_SELECTED_COLOR;
	}

	@Override
	public java.awt.Color getUnselectedColor() {
		return DEFAULT_UNSELECTED_COLOR;
	}

	@Override
	public java.awt.Color getPrimaryBackgroundColor() {
		return DEFAULT_PRIMARY_BACKGROUND_COLOR;
	}

	@Override
	public java.awt.Color getSecondaryBackgroundColor() {
		return DEFAULT_SECONDARY_BACKGROUND_COLOR;
	}
}

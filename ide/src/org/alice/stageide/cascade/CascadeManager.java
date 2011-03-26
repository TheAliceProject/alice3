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
package org.alice.stageide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class CascadeManager extends org.alice.ide.cascade.CascadeManager {
	public CascadeManager() {
		this.addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( org.alice.apis.moveandturn.Color.class ) );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.KeyFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.AngleFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.PortionFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.AudioSourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.VolumeLevelFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ImageSourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.MouseButtonListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.KeyListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.OutfitFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.HairFillerInner() );
	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> interfaceType ) {
		if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Style.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.TraditionalStyle.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
		} else {
			return super.getEnumTypeForInterfaceType( interfaceType );
		}
	}

	//	@Override
	//	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
	//		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
	//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
	//		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
	//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
	//		} else {
	//			return super.getTypeFor( type );
	//		}
	//	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getActualTypeForDesiredParameterType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		if( type.isAssignableTo( org.alice.apis.moveandturn.Angle.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else if( type.isAssignableTo( org.alice.apis.moveandturn.Portion.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else if( type.isAssignableTo( org.alice.apis.moveandturn.VolumeLevel.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else {
			return super.getActualTypeForDesiredParameterType( type );
		}
	}

	@Override
	protected boolean areEnumConstantsDesired( edu.cmu.cs.dennisc.alice.ast.AbstractType enumType ) {
		if( enumType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Key.class ) ) {
			return false;
		} else {
			return super.areEnumConstantsDesired( enumType );
		}
	}
	@Override
	protected void addCustomFillIns( edu.cmu.cs.dennisc.croquet.CascadeBlank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super.addCustomFillIns( blank, type );
		System.err.println( "TODO: addCustomFillIns handle listeners" );
//		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
//		if( previousExpression != null ) {
//			if( type.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
//				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice enclosingMethod = previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
//				if( enclosingMethod != null ) {
//					for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : enclosingMethod.parameters ) {
//						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeMouseButtonEvent = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.MouseButtonEvent.class );
//						if( parameter.getValueType() == typeMouseButtonEvent ) {
//							String[] methodNames = new String[] { "getModelAtMouseLocation", "getPartAtMouseLocation" };
//							for( String methodName : methodNames ) {
//								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeMouseButtonEvent.getDeclaredMethod( methodName );
//								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
//								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
//								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
//							}
//						}
//					}
//				}
//			} else if( type.isAssignableFrom( Boolean.class ) ) {
//				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice enclosingMethod = previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
//				if( enclosingMethod != null ) {
//					for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : enclosingMethod.parameters ) {
//						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeKeyEvent = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.KeyEvent.class );
//						if( parameter.getValueType() == typeKeyEvent ) {
//							//							String[] methodNames = new String[] { "isKey", "isLetter", "isDigit" };
//							//							Class<?>[][] parameterClses = new Class<?>[][] { new Class<?>[]{ org.alice.apis.moveandturn.Key.class }, new Class<?>[]{}, new Class<?>[]{} };
//							//							for( int i=0; i<methodNames.length; i++ ) {
//							//								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( methodNames[ i ], parameterClses[ i ] );
//							//								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
//							//								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
//							//								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
//							//							}
//
//							{
//								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( "isKey", org.alice.apis.moveandturn.Key.class );
//								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
//								blank.addFillIn( new org.alice.ide.cascade.IncompleteMethodInvocationFillIn( expression, method ) );
//							}
//							String[] methodNames = new String[] { "isLetter", "isDigit" };
//							for( String methodName : methodNames ) {
//								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( methodName );
//								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
//								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
//								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
//							}
//						}
//					}
//				}
//			}
//		}
	}

	@Override
	protected void addFillInAndPossiblyPartFillIns( edu.cmu.cs.dennisc.croquet.CascadeBlank blank, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.AbstractType type2 ) {
		super.addFillInAndPossiblyPartFillIns( blank, expression, type, type2 );
		System.err.println( "TODO: addFillInAndPossiblyPartFills" );
//		if( type.isAssignableTo( org.alice.apis.moveandturn.PolygonalModel.class ) ) {
//			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = null;
//			Class< ? > paramCls = null;
//			if( type2.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
//				typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
//				Class< ? > cls = typeInJava.getClassReflectionProxy().getReification();
//				for( Class innerCls : cls.getDeclaredClasses() ) {
//					if( innerCls.getSimpleName().equals( "Part" ) ) {
//						paramCls = innerCls;
//					}
//				}
//			}
//			if( paramCls != null ) {
//				edu.cmu.cs.dennisc.alice.ast.AbstractMethod getPartMethod = typeInJava.getDeclaredMethod( "getPart", paramCls );
//				if( getPartMethod != null ) {
//					blank.addFillIn( new org.alice.ide.cascade.IncompleteMethodInvocationFillIn( expression, getPartMethod ) );
//				}
//			}
//		}
	}
	
}

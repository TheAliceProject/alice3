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

package org.alice.ide.instancefactory.croquet;

import org.alice.ide.instancefactory.InstanceFactory;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryState extends org.lgna.croquet.CustomItemStateWithInternalBlank< InstanceFactory > {
	private static org.lgna.project.ast.AbstractType< ?,?,? > getDeclaringType( org.lgna.project.ast.AbstractDeclaration declaration ) {
		if( declaration instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)declaration;
			return method.getDeclaringType();
		} else if( declaration instanceof org.lgna.project.ast.AbstractType<?,?,?> ) {
			org.lgna.project.ast.AbstractType<?,?,?> type = (org.lgna.project.ast.AbstractType<?,?,?>)declaration;
			return type;
		} else {
			return null;
		}
	}

	private static class SingletonHolder {
		private static InstanceFactoryState instance = new InstanceFactoryState();
	}
	public static InstanceFactoryState getInstance() {
		return SingletonHolder.instance;
	}
	private final org.alice.ide.MetaDeclarationState.ValueListener declarationListener = new org.alice.ide.MetaDeclarationState.ValueListener() {
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
			InstanceFactoryState.this.handleDeclarationChanged( prevValue, nextValue );
		}
	};
	//todo: map AbstractCode to Stack< InstanceFactory >
	//private java.util.Map< org.lgna.project.ast.AbstractDeclaration, InstanceFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private InstanceFactory value;
	private InstanceFactoryState() {
		super( org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "f4e26c9c-0c3d-4221-95b3-c25df0744a97" ), org.alice.ide.instancefactory.croquet.codecs.InstanceFactoryCodec.SINGLETON );
		org.alice.ide.MetaDeclarationState.getInstance().addValueListener( declarationListener );
	}
	private void fallBackToDefaultFactory() {
		this.setValueTransactionlessly( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() );
	}
	private void handleDeclarationChanged( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
		if( this.ignoreCount == 0 ) {
			InstanceFactory instanceFactory = this.getValue();
			if( instanceFactory != null ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( instanceFactory );
				if( instanceFactory.isValid() ) {
					//pass
				} else {
					this.fallBackToDefaultFactory();
				}
			} else {
				this.fallBackToDefaultFactory();
			}
//			org.lgna.project.ast.AbstractType< ?,?,? > prevType = getDeclaringType( prevValue );
//			org.lgna.project.ast.AbstractType< ?,?,? > nextType = getDeclaringType( nextValue );
//			if( prevType != nextType ) {
//				InstanceFactory prevValue = this.getValue();
//				if( prevType != null ) {
//					if( prevValue != null ) {
//						map.put( prevType, prevValue );
//					} else {
//						map.remove( prevType );
//					}
//				}
//				InstanceFactory nextValue;
//				if( nextType != null ) {
//					nextValue = map.get( nextType );
//					if( nextValue != null ) {
//						//pass
//					} else {
//						nextValue = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
//					}
//				} else {
//					nextValue = null;
//				}
//				this.setValueTransactionlessly( nextValue );
//			}
		}
	}
	
	public void handleAstChangeThatCouldBeOfInterest() {
		InstanceFactory instanceFactory = this.getValue();
		if( instanceFactory != null ) {
			if( instanceFactory.isValid() ) {
				//pass
			} else {
				this.fallBackToDefaultFactory();
			}
		} else {
			this.fallBackToDefaultFactory();
		}
	}

	private org.lgna.croquet.CascadeBlankChild< InstanceFactory > createFillInMenuComboIfNecessary( org.lgna.croquet.CascadeFillIn< InstanceFactory, Void > item, org.lgna.croquet.CascadeMenuModel< InstanceFactory > subMenu ) {
		if( subMenu != null ) {
			return new org.lgna.croquet.CascadeItemMenuCombo< InstanceFactory >( item, subMenu );
		} else {
			return item;
		}
	}
	
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< InstanceFactory > blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
		org.lgna.project.ast.AbstractType< ?,?,? > type = getDeclaringType( org.alice.ide.MetaDeclarationState.getInstance().getValue() );

		rv.add( 
				this.createFillInMenuComboIfNecessary( 
						InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() ), 
						apiConfigurationManager.getInstanceFactorySubMenuForThis( type ) 
				) 
		);
		if( type instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)type;
			for( org.lgna.project.ast.UserField field : namedUserType.getDeclaredFields() ) {
				if( apiConfigurationManager.isInstanceFactoryDesiredForType( field.getValueType() ) ) {
					rv.add( 
							this.createFillInMenuComboIfNecessary( 
									InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) ), 
									apiConfigurationManager.getInstanceFactorySubMenuForThisFieldAccess( field ) 
							) 
					);
				}
			}
			org.lgna.project.ast.AbstractCode code = ide.getFocusedCode();
			if( code instanceof org.lgna.project.ast.UserCode ) {
				org.lgna.project.ast.UserCode userCode = (org.lgna.project.ast.UserCode)code;
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				for( org.lgna.project.ast.UserParameter parameter : userCode.getRequiredParamtersProperty() ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( parameter.getValueType() ) ) {
						rv.add( 
								this.createFillInMenuComboIfNecessary( 
										InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ParameterAccessFactory.getInstance( parameter ) ), 
										apiConfigurationManager.getInstanceFactorySubMenuForParameterAccess( parameter ) 
								) 
						);
					}
				}

				for( org.lgna.project.ast.UserLocal local : org.lgna.project.ProgramTypeUtilities.getLocals( userCode ) ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( local.getValueType() ) ) {
						rv.add( 
								this.createFillInMenuComboIfNecessary( 
										InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.LocalAccessFactory.getInstance( local ) ), 
										apiConfigurationManager.getInstanceFactorySubMenuForLocalAccess( local ) 
								) 
						);
					}
				}
			}
		}
		return rv;
	}
	@Override
	protected org.alice.ide.instancefactory.InstanceFactory getActualValue() {
		return this.value;
	}
	@Override
	protected void updateSwingModel( org.alice.ide.instancefactory.InstanceFactory value ) {
		this.value = value;
	}
	
	
	private int ignoreCount = 0;
	public void pushIgnoreAstChanges() {
		ignoreCount++;
	}
	public void popIgnoreAstChanges() {
		ignoreCount--;
		if( ignoreCount == 0 ) {
			this.handleAstChangeThatCouldBeOfInterest();
		}
	}
}

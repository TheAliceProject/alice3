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
import org.alice.ide.instancefactory.ThisInstanceFactory;
import org.alice.ide.instancefactory.croquet.codecs.InstanceFactoryCodec;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryState extends org.lgna.croquet.CustomItemStateWithInternalBlank< InstanceFactory > {
	private static class SingletonHolder {
		private static InstanceFactoryState instance = new InstanceFactoryState();
	}
	public static InstanceFactoryState getInstance() {
		return SingletonHolder.instance;
	}
	
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
//	private static org.lgna.project.ast.AbstractType< ?,?,? > getDeclaringType( org.alice.ide.croquet.models.typeeditor.DeclarationComposite declarationComposite ) { 
//		if( declarationComposite != null ) {
//			return getDeclaringType( declarationComposite.getDeclaration() );
//		} else {
//			return null;
//		}
//	}
//	private ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > declarationObserver = new ValueObserver< org.alice.ide.croquet.models.typeeditor.DeclarationComposite >() {
//		public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
//		}
//		public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > state, org.alice.ide.croquet.models.typeeditor.DeclarationComposite prevValue, org.alice.ide.croquet.models.typeeditor.DeclarationComposite nextValue, boolean isAdjusting ) {
//			InstanceFactoryState.this.handleDeclaringTypeChange( getDeclaringType( prevValue ), getDeclaringType( nextValue ) );
//		}
//	};

	private final org.alice.ide.MetaDeclarationState.ValueListener declarationListener = new org.alice.ide.MetaDeclarationState.ValueListener() {
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
			InstanceFactoryState.this.handleDeclaringTypeChange( getDeclaringType( prevValue ), getDeclaringType( nextValue ) );
		}
	};
	private java.util.Map< org.lgna.project.ast.AbstractType< ?,?,? >, InstanceFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private InstanceFactory value;
	private InstanceFactoryState() {
		super( org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "f4e26c9c-0c3d-4221-95b3-c25df0744a97" ), InstanceFactoryCodec.SINGLETON );
		//org.alice.ide.croquet.models.typeeditor.DeclarationTabState.getInstance().addValueObserver( declarationObserver );
		org.alice.ide.MetaDeclarationState.getInstance().addValueListener( declarationListener );
	}
	private void handleDeclaringTypeChange( org.lgna.project.ast.AbstractType< ?,?,? > prevType, org.lgna.project.ast.AbstractType< ?,?,? > nextType ) {
		if( prevType != nextType ) {
			InstanceFactory prevValue = this.getValue();
			if( prevType != null ) {
				if( prevValue != null ) {
					map.put( prevType, prevValue );
				} else {
					map.remove( prevType );
				}
			}
			InstanceFactory nextValue;
			if( nextType != null ) {
				nextValue = map.get( nextType );
				if( nextValue != null ) {
					//pass
				} else {
					nextValue = ThisInstanceFactory.SINGLETON;
				}
			} else {
				nextValue = null;
			}
			this.setValueTransactionlessly( nextValue );
		}
	}
	
	private org.lgna.croquet.CascadeBlankChild< InstanceFactory > createFillInMenuComboIfNecessary( org.lgna.croquet.CascadeFillIn< InstanceFactory, Void > item, org.lgna.croquet.CascadeMenuModel< InstanceFactory > subMenu ) {
		if( subMenu != null ) {
			return new org.lgna.croquet.CascadeFillInMenuCombo< InstanceFactory >( item, subMenu );
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
						ThisInstanceFactoryFillIn.getInstance(), 
						apiConfigurationManager.getInstanceFactorySubMenuForThis( type ) 
				) 
		);
		if( type instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)type;
			for( org.lgna.project.ast.UserField field : namedUserType.getDeclaredFields() ) {
				if( apiConfigurationManager.isInstanceFactoryDesiredForType( field.getValueType() ) ) {
					rv.add( 
							this.createFillInMenuComboIfNecessary( 
									ThisFieldAccessFactoryFillIn.getInstance( field ), 
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
										ParameterAccessFactoryFillIn.getInstance( parameter ), 
										apiConfigurationManager.getInstanceFactorySubMenuForParameterAccess( parameter ) 
								) 
						);
					}
				}

				for( org.lgna.project.ast.UserLocal local : org.lgna.project.ProgramTypeUtilities.getLocals( userCode ) ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( local.getValueType() ) ) {
						rv.add( 
								this.createFillInMenuComboIfNecessary( 
										LocalAccessFactoryFillIn.getInstance( local ), 
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
	public org.alice.ide.instancefactory.InstanceFactory getValue() {
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
			this.handleAstChangeTheCouldBeOfInterest();
		}
	}
	public void handleAstChangeTheCouldBeOfInterest() {
		
	}
////	//todo remove
//	public boolean isRespondingToRefreshAccessibles = true;
//	public void refreshAccessibles() {
////		this.typeHierarchyView.refresh();
////		if( isRespondingToRefreshAccessibles ) {
////			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reduce visibility of refreshAccessibles" );
////
////			org.lgna.project.ast.AbstractCode code = this.getFocusedCode();
////			org.lgna.project.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
////
////			java.util.List< org.lgna.project.ast.Accessible > accessibles = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
////			if( this.rootField != null ) {
////				accessibles.add( this.rootField );
////				for( org.lgna.project.ast.AbstractField field : this.getRootTypeDeclaredInAlice().fields ) {
////					if( this.isAccessibleDesired( field ) ) {
////						accessibles.add( field );
////					}
////				}
////			}
////
////			int indexOfLastField = accessibles.size() - 1;
////			if( code instanceof org.lgna.project.ast.CodeDeclaredInAlice ) {
////				org.lgna.project.ast.CodeDeclaredInAlice codeDeclaredInAlice = (org.lgna.project.ast.CodeDeclaredInAlice)code;
////				for( org.lgna.project.ast.ParameterDeclaredInAlice parameter : codeDeclaredInAlice.getParamtersProperty() ) {
////					if( this.isAccessibleDesired( parameter ) ) {
////						accessibles.add( parameter );
////					}
////				}
////				for( org.lgna.project.ast.VariableDeclaredInAlice variable : IDE.getVariables( code ) ) {
////					if( this.isAccessibleDesired( variable ) ) {
////						accessibles.add( variable );
////					}
////				}
////				for( org.lgna.project.ast.ConstantDeclaredInAlice constant : IDE.getConstants( code ) ) {
////					if( this.isAccessibleDesired( constant ) ) {
////						accessibles.add( constant );
////					}
////				}
////			}
////
////			int selectedIndex;
////			if( accessible != null ) {
////				selectedIndex = accessibles.indexOf( accessible );
////			} else {
////				selectedIndex = -1;
////			}
////			if( selectedIndex == -1 ) {
////				if( code != null ) {
////					accessible = this.mapCodeToAccessible.get( code );
////					selectedIndex = accessibles.indexOf( accessible );
////				}
////			}
////			if( selectedIndex == -1 ) {
////				selectedIndex = indexOfLastField;
////			}
////			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setListData( selectedIndex, accessibles );
////		}
//	}
}

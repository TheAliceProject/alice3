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
package org.alice.ide.croquet.models.members;

/**
 * @author Dennis Cosgrove
 */
public class PartSelectionState extends edu.cmu.cs.dennisc.croquet.DefaultListSelectionState< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField > {
	private static class SingletonHolder {
		private static PartSelectionState instance = new PartSelectionState();
	}
	public static PartSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private PartSelectionState() {
		super( edu.cmu.cs.dennisc.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "17b94498-cf54-414c-aa57-be2ef333de57" ),org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField.class ) );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible>() {
			public void changing( edu.cmu.cs.dennisc.croquet.State< edu.cmu.cs.dennisc.alice.ast.Accessible > state, edu.cmu.cs.dennisc.alice.ast.Accessible prevValue, edu.cmu.cs.dennisc.alice.ast.Accessible nextValue, boolean isAdjusting ) {
			}
			public void changed( edu.cmu.cs.dennisc.croquet.State< edu.cmu.cs.dennisc.alice.ast.Accessible > state, edu.cmu.cs.dennisc.alice.ast.Accessible prevValue, edu.cmu.cs.dennisc.alice.ast.Accessible nextValue, boolean isAdjusting ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
				if( nextValue != null ) {
					type = nextValue.getValueType();
				} else {
					type = null;
				}
				setValueType( type );
			}
		} );
	}
	private Class<?> partEnumCls = null;
	private void setPartEnumCls(Class<?> partEnumCls) {
		if( this.partEnumCls != partEnumCls ) {
			this.partEnumCls = partEnumCls;
			java.util.ArrayList< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField > items = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
			if( partEnumCls != null ) {
				java.lang.reflect.Field[] flds = partEnumCls.getFields();
				items.ensureCapacity( flds.length + 1 );
				items.add( null );
				for( java.lang.reflect.Field fld : flds ) {
					if( fld.isEnumConstant() ) {
						items.add( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField.get( fld ) );
					}
				}
			}
			this.setListData( -1, items );
		}
	}
	private void setValueType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		Class<?> partEnumCls = null;
		if( type != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
			Class<?> cls = typeInJava.getClassReflectionProxy().getReification();
			for( Class<?> declaredCls : cls.getDeclaredClasses() ) {
				if( declaredCls.isEnum() && "Part".equals( declaredCls.getSimpleName() ) ) {
					try {
						if( cls.getMethod( "getPart", declaredCls ) != null ) {
							partEnumCls = declaredCls;
						}
					} catch( NoSuchMethodException nsme ) {
						//pass
					}
				}
			}
		}
		this.setPartEnumCls( partEnumCls );
	}
}

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

package org.alice.stageide.operations.ast.oneshot;

class CascadeOperationAdapter extends org.lgna.croquet.CascadeFillIn< Object, Void > {
	private final org.lgna.croquet.Operation< ? > operation;
	public CascadeOperationAdapter( org.lgna.croquet.Operation< ? > operation ) {
		super( java.util.UUID.fromString( "117148c1-8f40-474f-9d6b-eadbccc6adc9" ) );
		this.operation = operation;
	}
	@Override
	protected String getDefaultLocalizedText() {
		return this.operation.getName();
	}
	@Override
	public java.lang.Object createValue( org.lgna.croquet.cascade.ItemNode< ? super java.lang.Object, java.lang.Void > step ) {
		return this.operation;
	}
	@Override
	public java.lang.Object getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super java.lang.Object, java.lang.Void > step ) {
		return this.operation;
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super java.lang.Object, java.lang.Void > step ) {
		return new javax.swing.JLabel( this.getDefaultLocalizedText() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class TopLevelBlank extends org.lgna.croquet.CascadeBlank< org.lgna.croquet.Operation< ? > > {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, TopLevelBlank > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static TopLevelBlank getInstance( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice value ) {
		synchronized( map ) {
			TopLevelBlank rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new TopLevelBlank( value );
				map.put( value, rv );
			}
			return rv;
		}
	}
	private final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
	private TopLevelBlank( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		super( java.util.UUID.fromString( "4d0d6ad3-0b1c-4bf8-a5da-cdbf59bf2cf1" ) );
		this.field = field;
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeItem > updateChildren( java.util.List< org.lgna.croquet.CascadeItem > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.croquet.Operation< ? > > blankNode ) {
		rv.add( ProceduresMenu.getInstance() );
		rv.add( new CascadeOperationAdapter( org.alice.ide.croquet.models.ast.rename.RenameFieldOperation.getInstance( this.field ) ) );
		return rv;
	}
}

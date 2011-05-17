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
package edu.cmu.cs.dennisc.croquet;

import org.lgna.croquet.edits.ListSelectionStateEdit;

/**
 * @author Dennis Cosgrove
 */
public class ListSelectionStatePrepModel<E> extends PrepModel {
	public static class ListSelectionStatePrepModelResolver<E> implements CodableResolver< ListSelectionStatePrepModel< E > > {
		private final ListSelectionStatePrepModel< E > model;
		public ListSelectionStatePrepModelResolver( ListSelectionStatePrepModel< E > model ) {
			this.model = model;
		}
		public ListSelectionStatePrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			CodableResolver<ListSelectionState< E >> resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			ListSelectionState< E > listSelectionState = resolver.getResolved();
			this.model = listSelectionState.getPrepModel();
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			CodableResolver<ListSelectionState< E >> resolver = this.model.listSelectionState.getCodableResolver();
			binaryEncoder.encode( resolver );
		}
		public ListSelectionStatePrepModel< E > getResolved() {
			return this.model;
		}
	}

	private final ListSelectionState< E > listSelectionState;
	/*package-private*/ ListSelectionStatePrepModel( ListSelectionState< E > listSelectionState ) {
		super( java.util.UUID.fromString( "c4b634e1-cd4f-465d-b0af-ab8d76cc7842" ) );
		assert listSelectionState != null;
		this.listSelectionState = listSelectionState;
	}
	@Override
	public Iterable< ? extends Model > getChildren() {
		return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.listSelectionState );
	}
	@Override
	protected void localize() {
	}
	public ListSelectionState< E > getListSelectionState() {
		return this.listSelectionState;
	}
	@Override
	protected ListSelectionStatePrepModelResolver<E> createCodableResolver() {
		return new ListSelectionStatePrepModelResolver<E>( this );
	}
	@Override
	public JComponent< ? > getFirstComponent() {
		return this.listSelectionState.getFirstComponent();
	}
	public ComboBox< E > createComboBox() {
		return new ComboBox< E >( this.getListSelectionState() );
	}
	@Override
	protected java.lang.StringBuilder updateTutorialStepText( java.lang.StringBuilder rv, org.lgna.croquet.steps.Step< ? > step, edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		if( edit != null ) {
			ListSelectionStateEdit< E > listSelectionStateEdit = (ListSelectionStateEdit< E >)edit;
			rv.append( "First press on " );
			rv.append( "<strong>" );
			this.getListSelectionState().getCodec().appendRepresentation( rv, listSelectionStateEdit.getPreviousValue(), java.util.Locale.getDefault() );
			rv.append( "</strong>" );
			rv.append( " in order to change it to " );
			rv.append( "<strong>" );
			this.getListSelectionState().getCodec().appendRepresentation( rv, listSelectionStateEdit.getNextValue(), java.util.Locale.getDefault() );
			rv.append( "</strong>." );
		}
		return rv;
	}
}

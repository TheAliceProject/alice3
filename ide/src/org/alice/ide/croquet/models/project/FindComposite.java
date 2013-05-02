/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.project;

import java.util.List;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.project.TreeNodesAndManagers.FindContentManager;
import org.alice.ide.croquet.models.project.TreeNodesAndManagers.SearchObject;
import org.alice.ide.croquet.models.project.views.FindView;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.data.RefreshableListData;
import org.lgna.project.ast.UserType;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class FindComposite extends FrameComposite<FindView> {

	private FindContentManager manager = new FindContentManager();
	private StringState searchState = createStringState( createKey( "searchState" ) );
	ItemCodec<SearchObject> codec1 = new ItemCodec<SearchObject>() {

		public Class<SearchObject> getValueClass() {
			return SearchObject.class;
		}

		public SearchObject<?> decodeValue( BinaryDecoder binaryDecoder ) {
			return null;
		}

		public void encodeValue( BinaryEncoder binaryEncoder, SearchObject value ) {
		}

		public void appendRepresentation( StringBuilder sb, SearchObject value ) {
			sb.append( value );
		}
	};
	ItemCodec<Object> codec2 = new ItemCodec<Object>() {

		public Class<Object> getValueClass() {
			return Object.class;
		}

		public Object decodeValue( BinaryDecoder binaryDecoder ) {
			return null;
		}

		public void encodeValue( BinaryEncoder binaryEncoder, Object value ) {
		}

		public void appendRepresentation( StringBuilder sb, Object value ) {
			sb.append( value );
		}
	};
	private RefreshableListData<SearchObject> data = new RefreshableListData<SearchObject>( codec1 ) {

		@Override
		protected List createValues() {
			return manager.getResultsForString( searchState.getValue() );
		}
	};
	private RefreshableListData<Object> referencesData = new RefreshableListData<Object>( codec2 ) {

		@Override
		protected List createValues() {
			return searchResults.getValue() != null ? searchResults.getValue().getReferences() : Collections.newArrayList();
		}
	};

	ValueListener<String> searchStateListener = new ValueListener<String>() {

		public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			data.refresh();
			referencesData.refresh();
		}
	};
	private ListSelectionState<SearchObject> searchResults = createListSelectionState( createKey( "searchResultsList" ),
			data, -1 );
	private ListSelectionState<Object> referenceResults = createListSelectionState( createKey( "searchResultsList" ),
			referencesData, -1 );

	public FindComposite() {
		super( java.util.UUID.fromString( "c454dba4-80ac-4873-b899-67ea3cd726e9" ), null );
		searchState.addValueListener( searchStateListener );
		searchResults.addValueListener( new ValueListener<SearchObject>() {

			public void changing( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
			}

			public void changed( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
				referencesData.refresh();
			}
		} );
	}

	@Override
	protected FindView createView() {
		return new FindView( this );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		manager.initialize( (UserType)IDE.getActiveInstance().getProgramType().fields.get( 0 ).getValueType() );
	}

	public StringState getSearchState() {
		return this.searchState;
	}

	public ListSelectionState<SearchObject> getSearchResults() {
		return this.searchResults;
	}

	public ListSelectionState<Object> getReferenceResults() {
		return this.referenceResults;
	}
}

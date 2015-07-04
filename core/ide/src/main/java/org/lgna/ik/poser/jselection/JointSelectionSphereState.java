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
package org.lgna.ik.poser.jselection;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CustomItemStateWithInternalBlank;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.views.ItemDropDown;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

/**
 * @author Matt May
 */
public class JointSelectionSphereState extends CustomItemStateWithInternalBlank<JointSelectionSphere> {

	private JointSelectionSphere value;
	private final List<JointSelectionSphere> possibleStates;

	public JointSelectionSphereState( JointSelectionSphere initialValue, List<JointSelectionSphere> possibleStates ) {
		super( null, java.util.UUID.fromString( "26646dcd-2003-43af-9939-c3eb61fdf560" ), initialValue, new ItemCodec<JointSelectionSphere>() {

			@Override
			public Class<JointSelectionSphere> getValueClass() {
				return JointSelectionSphere.class;
			}

			@Override
			public JointSelectionSphere decodeValue( BinaryDecoder binaryDecoder ) {
				throw new RuntimeException( "todo" );
			}

			@Override
			public void encodeValue( BinaryEncoder binaryEncoder, JointSelectionSphere value ) {
				throw new RuntimeException( "todo" );
			}

			@Override
			public void appendRepresentation( StringBuilder sb, JointSelectionSphere value ) {
				sb.append( value );
			}
		} );
		this.possibleStates = possibleStates;
	}

	@Override
	protected org.lgna.ik.poser.jselection.JointSelectionSphere getSwingValue() {
		return this.value;
	}

	@Override
	protected void setSwingValue( org.lgna.ik.poser.jselection.JointSelectionSphere nextValue ) {
		this.value = nextValue;
	}

	public ItemDropDown<JointSelectionSphere, JointSelectionSphereState> createItemDropDown() {
		return new JointSelectionSphereStateDropDown( this );
	}

	@Override
	protected void updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<JointSelectionSphere> blankNode ) {
		for( JointSelectionSphere sphere : possibleStates ) {
			rv.add( JointSelectionSphereFillIn.getInstance( sphere ) );
		}
	}
}

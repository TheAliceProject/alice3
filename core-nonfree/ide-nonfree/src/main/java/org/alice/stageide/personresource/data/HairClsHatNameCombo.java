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
package org.alice.stageide.personresource.data;

import org.lgna.story.resources.sims2.Hair;

/**
 * @author Dennis Cosgrove
 */
public final class HairClsHatNameCombo {
	private final Class<? extends Hair> hairCls;
	private final String hatName;

	public HairClsHatNameCombo( Class<? extends Hair> hairCls, String hatName ) {
		assert hairCls != null;
		this.hairCls = hairCls;
		this.hatName = hatName;
	}

	public Class<? extends Hair> getHairCls() {
		return this.hairCls;
	}

	public String getHatName() {
		return this.hatName;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj instanceof HairClsHatNameCombo ) {
			HairClsHatNameCombo other = (HairClsHatNameCombo)obj;
			if( this.hairCls.equals( other.hairCls ) ) {
				if( this.hatName != null ) {
					if( other.hatName != null ) {
						return this.hatName.contentEquals( other.hatName );
					} else {
						return false;
					}
				} else {
					if( other.hatName != null ) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int rv = 17;
		rv = ( 37 * rv ) + this.hairCls.hashCode();
		rv = ( 37 * rv ) + ( this.hatName != null ? this.hatName.hashCode() : 0 );
		return rv;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		sb.append( this.hairCls.getName() );
		sb.append( ";" );
		sb.append( this.hatName );
		sb.append( "]" );
		return sb.toString();
	}
}

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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Dennis Cosgrove
 */
public final class HairHatStyle implements Comparable<HairHatStyle> {
	private final HairClsHatNameCombo hairClsHatNameCombo;
	private final List<HairColorNameHairCombo> hairColorNameHairCombos;

	public HairHatStyle( HairClsHatNameCombo hairClsHatNameCombo, List<HairColorNameHairCombo> hairColorNames ) {
		assert hairClsHatNameCombo != null;
		assert hairColorNames != null;
		this.hairClsHatNameCombo = hairClsHatNameCombo;
		this.hairColorNameHairCombos = Collections.unmodifiableList( hairColorNames );
	}

	public Class<? extends Hair> getHairCls() {
		return this.hairClsHatNameCombo.getHairCls();
	}

	public String getHatName() {
		return this.hairClsHatNameCombo.getHatName();
	}

	public List<HairColorNameHairCombo> getHairColorNameHairCombos() {
		return this.hairColorNameHairCombos;
	}

	public Hair getHair( HairColorName hairColorName ) {
		for( HairColorNameHairCombo hairColorNameHairCombo : this.hairColorNameHairCombos ) {
			if( hairColorNameHairCombo.getHairColorName() == hairColorName ) {
				return hairColorNameHairCombo.getHair();
			}
		}
		//		Class<? extends org.lgna.story.resources.sims2.Hair> hairCls = this.getHairCls();
		//		String hatName = this.getHatName();
		//		for( org.lgna.story.resources.sims2.Hair hair : hairCls.getEnumConstants() ) {
		//			String[] hairColorNameAndHatName = HairUtilities.getHairColorNameAndHatName( hair );
		//			if( edu.cmu.cs.dennisc.java.util.Objects.equals( hairColorName.name(), hairColorNameAndHatName[ 0 ] ) ) {
		//				if( edu.cmu.cs.dennisc.java.util.Objects.equals( hatName, hairColorNameAndHatName[ 1 ] ) ) {
		//					return hair;
		//				}
		//			}
		//		}
		//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( hairColorName, this.hairColorNameHairCombos );
		return null;
	}

	private int compareHatName( HairHatStyle other ) {
		String thisHatName = this.getHatName();
		String otherHatName = other.getHatName();
		if( thisHatName != null ) {
			if( otherHatName != null ) {
				return thisHatName.compareTo( otherHatName );
			} else {
				return -1;
			}
		} else {
			if( otherHatName != null ) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private int compareClsName( HairHatStyle other ) {
		Class<? extends Hair> thisHairCls = this.getHairCls();
		Class<? extends Hair> otherHairCls = other.getHairCls();
		String thisSimpleName = thisHairCls.getSimpleName().toLowerCase( Locale.ENGLISH );
		String otherSimpleName = otherHairCls.getSimpleName().toLowerCase( Locale.ENGLISH );
		String[] thisSplit = thisSimpleName.split( "hair", 2 );
		assert thisSplit.length == 2 : thisHairCls;
		String[] otherSplit = otherSimpleName.split( "hair", 2 );
		assert otherSplit.length == 2 : otherHairCls;
		return thisSplit[ 1 ].compareTo( otherSplit[ 1 ] );
	}

	@Override
	public int compareTo( HairHatStyle other ) {
		Class<? extends Hair> thisHairCls = this.getHairCls();
		Class<? extends Hair> otherHairCls = other.getHairCls();
		if( thisHairCls.equals( otherHairCls ) ) {
			return compareHatName( other );
		} else {
			Hair thisHair0 = thisHairCls.getEnumConstants()[ 0 ];
			Hair otherHair0 = otherHairCls.getEnumConstants()[ 0 ];
			if( thisHair0.hasHat() ) {
				if( otherHair0.hasHat() ) {
					return compareClsName( other );
				} else {
					return 1;
				}
			} else {
				if( otherHair0.hasHat() ) {
					return -1;
				} else {
					return compareClsName( other );
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( this.getHairCls().getSimpleName() );
		sb.append( ";" );
		sb.append( this.getHatName() );
		sb.append( ";" );
		sb.append( this.hairColorNameHairCombos );
		sb.append( "]" );
		return sb.toString();
	}
}

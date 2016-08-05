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

/**
 * @author Dennis Cosgrove
 */
public class HairUtilities {
	private static java.util.Set<org.alice.stageide.personresource.data.HairColorName> COMMON_HAIR_COLOR_NAMES = edu.cmu.cs.dennisc.java.util.Sets.newHashSet( HairColorName.BLACK, HairColorName.BROWN, HairColorName.BLOND, HairColorName.RED, HairColorName.GREY );

	public static boolean isCommonHairColorName( org.alice.stageide.personresource.data.HairColorName hairColorName ) {
		return COMMON_HAIR_COLOR_NAMES.contains( hairColorName );
	}

	private static final edu.cmu.cs.dennisc.map.MapToMap<org.lgna.story.resources.sims2.LifeStage, org.lgna.story.resources.sims2.Gender, java.util.List<HairHatStyle>> mapToMap;
	static {
		mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
		for( org.lgna.story.resources.sims2.LifeStage lifeStage : org.lgna.story.resources.sims2.LifeStage.values() ) {
			for( org.lgna.story.resources.sims2.Gender gender : org.lgna.story.resources.sims2.Gender.values() ) {
				mapToMap.put( lifeStage, gender, createHairInfos( lifeStage, gender ) );
			}
		}
	}

	/* package-private */static String[] getHairColorNameAndHatName( org.lgna.story.resources.sims2.Hair enumConstant ) {
		String s = enumConstant.toString();
		if( enumConstant.hasHat() ) {
			if( enumConstant.hasHair() ) {
				return s.split( "_", 2 );
			} else {
				return new String[] { null, s };
			}
		} else {
			if( enumConstant.hasHair() ) {
				return new String[] { s, null };
			} else {
				return new String[] { null, null }; //todo?
			}
		}
	}

	private static java.util.List<HairHatStyle> createHairInfos( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender ) {
		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<HairClsHatNameCombo, HairColorNameHairCombo> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentListHashMap();
		Class<? extends org.lgna.story.resources.sims2.Hair>[] clses = org.lgna.story.resources.sims2.HairManager.getSingleton().getImplementingClasses( lifeStage, gender );
		for( Class<? extends org.lgna.story.resources.sims2.Hair> cls : clses ) {
			if( cls.isEnum() ) {
				for( org.lgna.story.resources.sims2.Hair enumConstant : cls.getEnumConstants() ) {
					String[] hairColorNameAndHatName = getHairColorNameAndHatName( enumConstant );
					assert hairColorNameAndHatName.length == 2 : enumConstant;
					String hairColorNameText = hairColorNameAndHatName[ 0 ];
					String hatName = hairColorNameAndHatName[ 1 ];
					HairClsHatNameCombo hairClsHatNameCombo = new HairClsHatNameCombo( cls, hatName );
					java.util.List<HairColorNameHairCombo> list = map.getInitializingIfAbsentToLinkedList( hairClsHatNameCombo );
					HairColorName hairColorName = hairColorNameText != null ? HairColorName.valueOf( hairColorNameText ) : HairColorName.NULL;
					list.add( new HairColorNameHairCombo( hairColorName, enumConstant ) );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( cls.getName(), "is not enum" );
			}
		}
		java.util.List<HairHatStyle> hairHatStyles = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( HairClsHatNameCombo hairClsHatNameCombo : map.keySet() ) {
			java.util.List<HairColorNameHairCombo> list = map.get( hairClsHatNameCombo );
			java.util.Collections.sort( list );
			hairHatStyles.add( new HairHatStyle( hairClsHatNameCombo, list ) );
		}
		java.util.Collections.sort( hairHatStyles );
		return java.util.Collections.unmodifiableList( hairHatStyles );
	}

	public static java.util.List<HairHatStyle> getHairHatStyles( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender ) {
		if( ( lifeStage != null ) && ( gender != null ) ) {
			return mapToMap.get( lifeStage, gender );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	public static HairHatStyleHairColorName getHairHatStyleColorNameFromHair( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Hair hair ) {
		if( hair != null ) {
			java.util.List<HairHatStyle> hairHatStyles = getHairHatStyles( lifeStage, gender );
			for( HairHatStyle hairHatStyle : hairHatStyles ) {
				if( hairHatStyle.getHairCls().equals( hair.getClass() ) ) {
					String[] hairColorNameAndHatName = getHairColorNameAndHatName( hair );
					assert hairColorNameAndHatName.length == 2 : hair;
					String hatName = hairColorNameAndHatName[ 1 ];
					if( edu.cmu.cs.dennisc.java.util.Objects.equals( hatName, hairHatStyle.getHatName() ) ) {
						String hairColorNameText = hairColorNameAndHatName[ 0 ];
						HairColorName hairColorName = hairColorNameText != null ? HairColorName.valueOf( hairColorNameText ) : HairColorName.NULL;
						return new HairHatStyleHairColorName( hairHatStyle, hairColorName );
					}
				}
			}
		}
		return null;
	}

	public static void main( String[] args ) {
		java.util.Set<HairColorName> hairColorNameSet = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		for( org.lgna.story.resources.sims2.LifeStage lifeStage : org.lgna.story.resources.sims2.LifeStage.values() ) {
			for( org.lgna.story.resources.sims2.Gender gender : org.lgna.story.resources.sims2.Gender.values() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
				for( HairHatStyle hairHatStyle : getHairHatStyles( lifeStage, gender ) ) {
					//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( hairHatStyle );
					for( HairColorNameHairCombo hairColorNameHairCombo : hairHatStyle.getHairColorNameHairCombos() ) {
						hairColorNameSet.add( hairColorNameHairCombo.getHairColorName() );
					}
				}
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
			}
		}
		hairColorNameSet.remove( null );
		java.util.List<HairColorName> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( hairColorNameSet );

		for( HairColorName hairColorName : list ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( hairColorName );
		}
		java.util.Collections.sort( list );
		list.add( 0, null );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( list );
	}
}

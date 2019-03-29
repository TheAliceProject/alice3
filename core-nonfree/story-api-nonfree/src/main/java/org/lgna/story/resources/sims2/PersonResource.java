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

package org.lgna.story.resources.sims2;

import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import javax.swing.JComponent;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Dennis Cosgrove
 */
public abstract class PersonResource implements BipedResource {
	private final Gender gender;
	private final SkinTone skinTone;
	private final Color skinColor;
	private final EyeColor eyeColor;
	private final Hair hair;
	private final double obesityLevel;
	private final Outfit outfit;
	private final Face face;

	private PersonResource( Gender gender, SkinTone skinTone, Color skinColor, EyeColor eyeColor, Hair hair, Number obesityLevel, Outfit outfit, Face face ) {
		this.gender = gender;
		this.skinTone = skinTone;
		this.skinColor = skinColor;
		this.eyeColor = eyeColor;
		this.hair = hair;
		this.obesityLevel = obesityLevel.doubleValue();
		this.outfit = outfit;
		this.face = face;
	}

	private static Color getClosestColor( SkinTone skinTone ) {
		BaseSkinTone baseSkinTone;
		if( skinTone instanceof BaseSkinTone ) {
			baseSkinTone = (BaseSkinTone)skinTone;
		} else {
			baseSkinTone = BaseSkinTone.getRandom();
		}
		return EmployeesOnly.createColor( baseSkinTone.getColor() );
	}

	protected static String getLocalizedDisplayText( String key ) {
		Class cls = PersonResource.class;
		String bundleName = cls.getPackage().getName() + ".PersonStrings";
		try {
			ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle( bundleName, JComponent.getDefaultLocale() );
			String rv = resourceBundle.getString( key );
			return rv;
		} catch( MissingResourceException mre ) {
			Logger.severe( cls, key );
			return key;
		}
	}

	public PersonResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obesityLevel, Outfit outfit, Face face ) {
		this( gender, skinTone, getClosestColor( skinTone ), eyeColor, hair, obesityLevel, outfit, face );
	}

	public PersonResource( Gender gender, Color skinColor, EyeColor eyeColor, Hair hair, Number obesityLevel, Outfit outfit, Face face ) {
		this( gender, BaseSkinTone.getClosestToColor( EmployeesOnly.getAwtColor( skinColor ) ), skinColor, eyeColor, hair, obesityLevel, outfit, face );
	}

	public abstract LifeStage getLifeStage();

	public Gender getGender() {
		return this.gender;
	}

	@Deprecated
	public SkinTone getSkinTone() {
		return this.skinTone;
	}

	public Color getSkinColor() {
		return this.skinColor;
	}

	public EyeColor getEyeColor() {
		return this.eyeColor;
	}

	public Hair getHair() {
		return this.hair;
	}

	public Double getObesityLevel() {
		return this.obesityLevel;
	}

	public Outfit getOutfit() {
		return this.outfit;
	}

	public Face getFace() {
		return this.face;
	}

	public JointId[] getRootJointIds() {
		return BipedResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return JointImplementationAndVisualDataFactory.getInstance( this );
	}

	@Override
	public final BipedImp createImplementation( SBiped abstraction ) {
		return new BipedImp( abstraction, JointImplementationAndVisualDataFactory.getInstance( this ) );
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj instanceof PersonResource ) {
			PersonResource other = (PersonResource)obj;
			if( this.getClass() == other.getClass() ) {
				if( Objects.equals( this.gender, other.gender ) ) {
					if( Objects.equals( this.skinTone, other.skinTone ) ) {
						if( Objects.equals( this.eyeColor, other.eyeColor ) ) {
							if( Objects.equals( this.hair, other.hair ) ) {
								if( Objects.equals( this.face, other.face ) ) {
									if( Objects.equals( this.outfit, other.outfit ) ) {
										return this.obesityLevel == other.obesityLevel;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.gender != null ) {
			rv = ( 37 * rv ) + this.gender.hashCode();
		}
		if( this.skinTone != null ) {
			rv = ( 37 * rv ) + this.skinTone.hashCode();
		}
		if( this.eyeColor != null ) {
			rv = ( 37 * rv ) + this.eyeColor.hashCode();
		}
		if( this.hair != null ) {
			rv = ( 37 * rv ) + this.hair.hashCode();
		}
		if( this.outfit != null ) {
			rv = ( 37 * rv ) + this.outfit.hashCode();
		}
		if( this.face != null ) {
			rv = ( 37 * rv ) + this.face.hashCode();
		}
		long lng = Double.doubleToLongBits( this.obesityLevel );
		rv = ( 37 * rv ) + (int)( lng ^ ( lng >>> 32 ) );
		return rv;
	}
}

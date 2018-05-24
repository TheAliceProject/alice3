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

package org.lgna.story;

import org.lgna.common.LgnaIllegalArgumentException;
import org.lgna.project.annotations.ClassTemplate;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.implementation.EntityImp;

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate( isFollowToSuperClassDesired = false )
public abstract class SThing implements Rider {
	/* package-private */abstract EntityImp getImplementation();

	@GetterTemplate( isPersistent = true )
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public String getName() {
		return this.getImplementation().getName();
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void setName( String name ) {
		this.getImplementation().setName( name );
	}

	@Override
	@GetterTemplate( isPersistent = true )
	@MethodTemplate( )
	public SThing getVehicle() {
		EntityImp vehicleImplementation = this.getImplementation().getVehicle();
		return vehicleImplementation != null ? vehicleImplementation.getAbstraction() : null;
	}

	public VantagePoint getVantagePoint( SThing entity ) {
		LgnaIllegalArgumentException.checkArgumentIsNotNull( entity, 0 );
		return VantagePoint.createInstance( this.getImplementation().getTransformation( entity.getImplementation() ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public void delay( Number duration ) {
		LgnaIllegalArgumentException.checkArgumentIsNumber( duration, 0 );
		this.getImplementation().delay( duration.doubleValue() );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public void playAudio( AudioSource audioSource ) {
		LgnaIllegalArgumentException.checkArgumentIsNotNull( audioSource, 0 );
		this.getImplementation().playAudio( audioSource );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isCollidingWith( SThing other ) {
		LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().isCollidingWith( other );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean getBooleanFromUser( String message ) {
		return this.getImplementation().getBooleanFromUser( message );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public String getStringFromUser( String message ) {
		return this.getImplementation().getStringFromUser( message );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDoubleFromUser( String message ) {
		return this.getImplementation().getDoubleFromUser( message );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Integer getIntegerFromUser( String message ) {
		return this.getImplementation().getIntegerFromUser( message );
	}

	@Override
	public String toString() {
		String name = this.getName();
		if( name != null ) {
			return name;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append( "unnamed " );
			sb.append( this.getClass().getSimpleName() );
			return sb.toString();
		}
	}
}

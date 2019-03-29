/*
 * Alice 3 End User License Agreement
 *
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.lgna.story.resources.marinemammal;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SSwimmer;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.SwimmerImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.MarineMammalResource;

public enum WalrusResource implements MarineMammalResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId BACK_RIGHT_FIN = new JointId( TAIL, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId BACK_RIGHT_FIN_MIDDLE = new JointId( BACK_RIGHT_FIN, WalrusResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId BACK_RIGHT_FIN_TIP = new JointId( BACK_RIGHT_FIN_MIDDLE, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId BACK_LEFT_FIN = new JointId( TAIL, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId BACK_LEFT_FIN_MIDDLE = new JointId( BACK_LEFT_FIN, WalrusResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId BACK_LEFT_FIN_TIP = new JointId( BACK_LEFT_FIN_MIDDLE, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId FRONT_LEFT_FIN_MIDDLE = new JointId( FRONT_LEFT_FIN, WalrusResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId FRONT_LEFT_FIN_TIP = new JointId( FRONT_LEFT_FIN_MIDDLE, WalrusResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId FRONT_RIGHT_FIN_MIDDLE = new JointId( FRONT_RIGHT_FIN, WalrusResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId FRONT_RIGHT_FIN_TIP = new JointId( FRONT_RIGHT_FIN_MIDDLE, WalrusResource.class );

	private final ImplementationAndVisualType resourceType;
	WalrusResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	WalrusResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public SwimmerImp createImplementation( SSwimmer abstraction ) {
		return new SwimmerImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

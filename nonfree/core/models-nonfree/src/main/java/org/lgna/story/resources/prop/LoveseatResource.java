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

package org.lgna.story.resources.prop;

import org.lgna.project.annotations.*;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum LoveseatResource implements org.lgna.story.resources.PropResource {
	ADIRONDACK_CAMOUFLAGE( ImplementationAndVisualType.SIMS2 ),
	ADIRONDACK_GRAY( ImplementationAndVisualType.SIMS2 ),
	ADIRONDACK_GREEN( ImplementationAndVisualType.SIMS2 ),
	ADIRONDACK_RED( ImplementationAndVisualType.SIMS2 ),
	ADIRONDACK_BLUE( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_DARK_WOOD_BROWN_THISTLE( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_DARK_WOOD_GREEN( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_DARK_WOOD_TAN_ROSES( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_OAK_BROWN_THISTLE( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_OAK_GREEN( ImplementationAndVisualType.SIMS2 ),
	ART_NOUVEAU_OAK_TAN_ROSES( ImplementationAndVisualType.SIMS2 ),
	CAMEL_BACK_WHITE_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	CAMEL_BACK_BLACK_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	CAMEL_BACK_BLUE_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	CAMEL_BACK_PINK_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	CAMEL_BACK_RED_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_BLUE_BLUE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_BLUE_GREEN_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_BLUE_ORANGE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_GREEN_BLUE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_GREEN_GREEN_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_GREEN_ORANGE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_ORANGE_BLUE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_ORANGE_GREEN_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MODERN_LOFT_ORANGE_ORANGE_CUSHIONS( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_TAN( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_LIGHT_BLUE( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_GREEN( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_RED( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_WHITE( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_OAK( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_BLUE( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_GREEN( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_RED( ImplementationAndVisualType.SIMS2 ),
	PARK_BENCH_WOOD( ImplementationAndVisualType.SIMS2 ),
	QUAINT_BROWN( ImplementationAndVisualType.SIMS2 ),
	QUAINT_BLUE( ImplementationAndVisualType.SIMS2 ),
	QUAINT_GREEN( ImplementationAndVisualType.SIMS2 ),
	QUAINT_PINK( ImplementationAndVisualType.SIMS2 ),
	QUAINT_WHITE( ImplementationAndVisualType.SIMS2 ),
	VALUE_BLUE_SQUARES( ImplementationAndVisualType.SIMS2 ),
	VALUE_BLUE_STRIPES( ImplementationAndVisualType.SIMS2 ),
	VALUE_WHITE( ImplementationAndVisualType.SIMS2 ),
	VALUE_RED_SQUARES( ImplementationAndVisualType.SIMS2 );

	private final ImplementationAndVisualType resourceType;
	private LoveseatResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private LoveseatResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return new org.lgna.story.resources.JointId[0];
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BasicJointedModelImp createImplementation( org.lgna.story.SJointedModel abstraction ) {
		return new org.lgna.story.implementation.BasicJointedModelImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

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

import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum CoffeeTableResource implements PropResource {
	ART_NOVEAU_LIGHT_WOOD( ImplementationAndVisualType.SIMS2 ),
	ART_NOVEAU_DARK_WOOD( ImplementationAndVisualType.SIMS2 ),
	ART_NOVEAU_WOOD( ImplementationAndVisualType.SIMS2 ),
	CENTRAL_ASIAN_FANCY_BLONDE_BLONDE( ImplementationAndVisualType.SIMS2 ),
	CENTRAL_ASIAN_FANCY_CHERRY_CHERRY( ImplementationAndVisualType.SIMS2 ),
	CENTRAL_ASIAN_FANCY_DARK_WOOD_DARK_WOOD( ImplementationAndVisualType.SIMS2 ),
	CENTRAL_ASIAN_FANCY_REDWOOD_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_WOOD( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_OAK( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_BLUE( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_GREEN( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_WHITE( ImplementationAndVisualType.SIMS2 ),
	SMALL_CLUB_CURLY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_WOOD( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_OAK( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_BLUE( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_CURLY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	LARGE_CLUB_WHITE( ImplementationAndVisualType.SIMS2 ),
	COLONIAL_GOLD_FLORAL( ImplementationAndVisualType.SIMS2 ),
	COLONIAL_WOOD( ImplementationAndVisualType.SIMS2 ),
	COLONIAL_PINK( ImplementationAndVisualType.SIMS2 ),
	COLONIAL_WHITE( ImplementationAndVisualType.SIMS2 ),
	DESIGNER_ASH( ImplementationAndVisualType.SIMS2 ),
	DESIGNER_WALNUT( ImplementationAndVisualType.SIMS2 ),
	DESIGNER_WHITE( ImplementationAndVisualType.SIMS2 ),
	LOFT_CONCRETE( ImplementationAndVisualType.SIMS2 ),
	LOFT_PATINA( ImplementationAndVisualType.SIMS2 ),
	LOFT_RED( ImplementationAndVisualType.SIMS2 ),
	LOFT_METAL( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_YELLOW_INLAY_BLACK( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_YELLOW_INLAY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_YELLOW_INLAY_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_YELLOW_INLAY_YELLOW( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_FANCY_INLAY_BLACK( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_FANCY_INLAY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_FANCY_INLAY_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_FANCY_INLAY_YELLOW( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_STARS_INLAY_BLACK( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_STARS_INLAY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_STARS_INLAY_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_STARS_INLAY_YELLOW( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_TILE_INLAY_BLACK( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_TILE_INLAY_REDWOOD( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_TILE_INLAY_MAHOGANY( ImplementationAndVisualType.SIMS2 ),
	MOROCCAN_TILE_INLAY_YELLOW( ImplementationAndVisualType.SIMS2 ),
	PINE_BIRCH( ImplementationAndVisualType.SIMS2 ),
	PINE_BLONDE( ImplementationAndVisualType.SIMS2 ),
	PINE_CEDAR( ImplementationAndVisualType.SIMS2 ),
	PINE_HONEY( ImplementationAndVisualType.SIMS2 ),
	PINE_WALNUT( ImplementationAndVisualType.SIMS2 ),
	QUAINT_BLUE( ImplementationAndVisualType.SIMS2 ),
	QUAINT_GREEN( ImplementationAndVisualType.SIMS2 ),
	QUAINT_RED( ImplementationAndVisualType.SIMS2 ),
	QUAINT_WHITE( ImplementationAndVisualType.SIMS2 ),
	SPINDLE_RED( ImplementationAndVisualType.SIMS2 ),
	SPINDLE_PAINTED( ImplementationAndVisualType.SIMS2 ),
	SPINDLE_OLD( ImplementationAndVisualType.SIMS2 );

	private final ImplementationAndVisualType resourceType;
	CoffeeTableResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	CoffeeTableResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	@Override
	public JointId[] getRootJointIds(){
		return new JointId[0];
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public BasicJointedModelImp createImplementation( SJointedModel abstraction ) {
		return new BasicJointedModelImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

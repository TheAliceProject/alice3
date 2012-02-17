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
package org.lgna.story.implementation;

import org.lgna.story.Paint;
import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.implementation.sims2.NebulousVisualData;

/**
 * @author alice
 *
 */
public class RoomImp extends SimpleModelImp {
	
	public final PaintProperty wallPaint = new PaintProperty( RoomImp.this ) {
		@Override
		protected void internalSetValue(org.lgna.story.Paint value) {
			if (visualData instanceof NebulousVisualData<?>){
				NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing> nebData = (NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing>)visualData;
				edu.cmu.cs.dennisc.nebulous.Thing nebModel = nebData.getNebModel();
				nebModel.setTexture(value.toString());
			}
		}
	};
	
	public final PaintProperty floorPaint = new PaintProperty( RoomImp.this ) {
		@Override
		protected void internalSetValue(org.lgna.story.Paint value) {
			if (visualData instanceof NebulousVisualData<?>){
				NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing> nebData = (NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing>)visualData;
				edu.cmu.cs.dennisc.nebulous.Thing nebModel = nebData.getNebModel();
				nebModel.setTexture(value.toString());
			}
		}
	};
	
	public final PaintProperty ceilingPaint = new PaintProperty( RoomImp.this ) {
		@Override
		protected void internalSetValue(org.lgna.story.Paint value) {
			if (visualData instanceof NebulousVisualData<?>){
				NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing> nebData = (NebulousVisualData<? extends edu.cmu.cs.dennisc.nebulous.Thing>)visualData;
				edu.cmu.cs.dennisc.nebulous.Thing nebModel = nebData.getNebModel();
				nebModel.setTexture(value.toString());
			}
		}
	};
	
	public static interface VisualDataFactory< R extends org.lgna.story.resources.RoomResource > {
		public R getResource();
		public VisualData createVisualData( org.lgna.story.implementation.RoomImp roomImplementation );
	}
	
	
	private final org.lgna.story.Room abstraction;
	private final VisualDataFactory<org.lgna.story.resources.RoomResource> factory;
	private final VisualData visualData;
	
	public RoomImp( org.lgna.story.Room abstraction, VisualDataFactory< org.lgna.story.resources.RoomResource > factory ) {
		this.abstraction = abstraction;
		this.factory = factory;
		
		this.visualData = this.factory.createVisualData( this );
		this.visualData.setSGParent(this.getSgComposite());
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getSgComposite() );
		}
	}
	
	@Override
	public org.lgna.story.Room getAbstraction() {
		return this.abstraction;
	}

	@Override
	protected double getBoundingSphereRadius() {
		return Double.POSITIVE_INFINITY;
	}
}
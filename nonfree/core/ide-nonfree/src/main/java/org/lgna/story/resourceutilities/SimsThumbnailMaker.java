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
package org.lgna.story.resourceutilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory;
import org.lgna.story.implementation.sims2.NebulousPersonVisualData;
import org.lgna.story.implementation.sims2.NebulousVisualData;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.PersonResource;

/**
 * @author Dave Culyba
 */
public class SimsThumbnailMaker extends AliceThumbnailMaker {
	private static Map<String, SimsThumbnailMaker> instanceMap = new HashMap<String, SimsThumbnailMaker>();

	public static SimsThumbnailMaker getInstance() {
		return getInstance( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT );
	}

	public static SimsThumbnailMaker getInstance( int width, int height ) {
		String key = Integer.toString( width ) + "x" + Integer.toString( height );
		if( instanceMap.containsKey( key ) ) {
			SimsThumbnailMaker instance = instanceMap.get( key );
			instance.clear();
			return instance;
		} else {
			SimsThumbnailMaker instance = new SimsThumbnailMaker( width, height );
			instanceMap.put( key, instance );
			return instance;
		}
	}

	protected SimsThumbnailMaker( int width, int height ) {
		super( width, height );
	}

	protected SimsThumbnailMaker() {
		super();
	}

	private AffineMatrix4x4 getThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) || ( lifeStage == LifeStage.TEEN ) ) {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.4, 0, -.4, .4, 1.6, .5 ) );
		} else {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.2, 0, -.2, .2, 1.1, .2 ) );
		}
	}

	private AffineMatrix4x4 getGalleryThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		Vector3 cameraDir = new Vector3( -1.0, -.5, 3.0 );
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) || ( lifeStage == LifeStage.TEEN ) ) {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.4, 0, -.4, .4, 1.6, .5 ), cameraDir );
		} else {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.2, 0, -.2, .2, 1.6, .2 ), cameraDir );
		}
	}

	private AffineMatrix4x4 getThumbnailCameraOrientationForPerson( PersonResource personResource ) {
		return getThumbnailCameraOrientationForLifeStage( personResource.getLifeStage() );
	}

	private AffineMatrix4x4 getHeadThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.58, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		} else if( lifeStage == LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.48, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		} else if( lifeStage == LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .7, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .6 );
		} else {
			return getThumbnailCameraOrientation( new Point3( 0, 1.1, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		}
	}

	private AffineMatrix4x4 getBodyThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, .72, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 3.3 );
		}
		if( lifeStage == LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, .68, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 3.25 );
		} else if( lifeStage == LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .3, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2 );
		} else {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.3 );
		}
	}

	private AffineMatrix4x4 getTopBodyThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.07, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.8 );
		} else if( lifeStage == LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.02, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.6 );
		} else if( lifeStage == LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		} else {
			return getThumbnailCameraOrientation( new Point3( 0, .75, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.2 );
		}
	}

	private AffineMatrix4x4 getBottomBodyThumbnailCameraOrientationForLifeStage( LifeStage lifeStage ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.4 );
		} else if( lifeStage == LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, .48, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.3 );
		} else {
			return getThumbnailCameraOrientation( new Point3( 0, .33, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.6 );
		}
	}

	private AffineMatrix4x4 getFaceThumbnailCameraOrientationForLifeStageAndGender( LifeStage lifeStage, Gender gender ) {
		if( ( lifeStage == LifeStage.ADULT ) || ( lifeStage == LifeStage.ELDER ) ) {
			if( gender == Gender.FEMALE ) {
				return getThumbnailCameraOrientation( new Point3( 0, 1.58, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .56 );
			} else {
				return getThumbnailCameraOrientation( new Point3( 0, 1.569, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .569 );
			}
		} else if( lifeStage == LifeStage.TEEN ) {
			if( gender == Gender.FEMALE ) {
				return getThumbnailCameraOrientation( new Point3( 0, 1.48, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .56 );
			} else {
				return getThumbnailCameraOrientation( new Point3( 0, 1.469, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .569 );
			}
		} else if( lifeStage == LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .685, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .4 );
		} else {
			return getThumbnailCameraOrientation( new Point3( 0, 1.09, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .48 );
		}
	}

	public BufferedImage createThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createGalleryThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getGalleryThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public BufferedImage createThumbnailFromPersonResource( PersonResource resource ) throws Exception {
		return createThumbnailFromPersonResource( resource, true );
	}

	public BufferedImage createGalleryThumbnailFromPersonResource( PersonResource resource ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createGalleryThumbnailFromPersonVisualData( visualData, false, null );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized BufferedImage createThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public BufferedImage createBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createBodyThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createBodyThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createBodyThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public BufferedImage createTopBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createTopBodyThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createTopBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getTopBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createTopBodyThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createTopBodyThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public BufferedImage createBottomBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createBottomBodyThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createBottomBodyThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getBottomBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createBottomBodyThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createBottomBodyThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public BufferedImage createHeadThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createHeadThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createHeadThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getHeadThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createHeadThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createHeadThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public BufferedImage createFaceThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim ) {
		return createFaceThumbnailFromPersonVisualData( visualData, trim, null );
	}

	public synchronized BufferedImage createFaceThumbnailFromPersonVisualData( NebulousPersonVisualData visualData, boolean trim, Color colorKey ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		BufferedImage returnImage = takePicture( getFaceThumbnailCameraOrientationForLifeStageAndGender( visualData.getLifeStage(), visualData.getGender() ), trim, colorKey );
		visualData.setSGParent( null );
		for( Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized BufferedImage createFaceThumbnailFromPersonResource( PersonResource resource, boolean trim ) throws Exception {
		JointImplementationAndVisualDataFactory factory = JointImplementationAndVisualDataFactory.getInstance( resource );
		NebulousPersonVisualData visualData = (NebulousPersonVisualData)factory.createVisualData();
		BufferedImage thumbnail = createFaceThumbnailFromPersonVisualData( visualData, trim );
		( (NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}
}

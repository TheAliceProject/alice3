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

package org.lgna.story.resources.biped;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum BatResource implements BipedResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, BatResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId LEFT_EAR = new JointId( HEAD, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_EAR_TIP = new JointId( LEFT_EAR, BatResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId RIGHT_EAR = new JointId( HEAD, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_EAR_TIP = new JointId( RIGHT_EAR, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_INDEX_FINGER_TIP = new JointId( LEFT_INDEX_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_THUMB_TIP = new JointId( LEFT_THUMB_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId( LEFT_MIDDLE_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER = new JointId( LEFT_HAND, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_KNUCKLE = new JointId( LEFT_RING_FINGER, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_TIP = new JointId( LEFT_RING_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId( RIGHT_INDEX_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_THUMB_TIP = new JointId( RIGHT_THUMB_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER = new JointId( RIGHT_HAND, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_KNUCKLE = new JointId( RIGHT_RING_FINGER, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_TIP = new JointId( RIGHT_RING_FINGER_KNUCKLE, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, BatResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, BatResource.class );

	public static final JointedModelPose VAMPIRE_POSE = new JointedModelPose(
		new JointIdTransformationPair( NECK, new Orientation(-0.045034516130712916, 0.0, 0.0, 0.9989854315038196), new Position(-0.0, -6.139261321536083E-10, -0.08239995688199997) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(0.15177071095584116, 0.1754681997147109, -0.01613972275722365, 0.9725821669834157), new Position(-0.0, 0.0, -0.386800616979599) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.061347280072770224, -0.11167795460090776, 0.14520817079164294, 0.9811621338082589), new Position(-0.0, 0.0, -0.3228152096271515) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.05712804274830147, 0.15719218089041212, -0.05603068336655007, 0.9843208661407832), new Position(-0.06678619980812073, -0.036235611885786057, -0.19837747514247894) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.09136598090136631, 0.013724635487015184, -0.029946682412973605, 0.9952723688152586), new Position(0.06678619980812073, -0.036235369741916656, -0.19837713241577148) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.1702952047566176, -0.21566118303287682, -0.03938221386170826, 0.960697058703269), new Position(-0.0, 0.0, -0.4689873456954956) ),
		new JointIdTransformationPair( RIGHT_EYELID, new Orientation(0.16591948859004577, -0.06238702846938305, 0.03293508875192279, 0.9836126584757239), new Position(0.07591389864683151, -0.03628937155008316, -0.23341913521289825) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.5208708873783057, -0.5552601743824196, 0.3778752774957076, 0.5268680404846013), new Position(-0.0, 0.0, -0.07769430428743362) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.21800818430316266, -0.17106060021062322, -0.01902671954200266, 0.9606501374464533), new Position(-0.0, 0.0, -0.42901697754859924) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.3259998388075812, 0.2428863412219007, 0.010629384745940702, 0.9135739414657098), new Position(-0.0, 0.0, -0.08567973226308823) ),
		new JointIdTransformationPair( LEFT_EYELID, new Orientation(0.26620215922510093, 0.042982426268116276, -0.3437580436870267, 0.8995106051939956), new Position(-0.0759139209985733, -0.036288902163505554, -0.2334192395210266) )
	);


	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_RING_FINGER, new Orientation(0.0832721588037895, 0.8407141289343514, 0.5064930510913522, 0.17242473771035363), new Position(-0.013824241235852242, -0.049429282546043396, 0.02175980433821678) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(-0.034938145810165154, -0.1741100936583258, 0.0457180712608002, 0.9830436710613771), new Position(-0.0, 0.0, -0.38680312037467957) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.0028564924152008567, 0.0286021788255718, -0.020951224542654804, 0.9993672007864153), new Position(-0.0, 0.0, -0.32281410694122314) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.09735129508969756, 0.1182401547373837, -0.032605497929275425, 0.9876633397351662), new Position(-0.06678619980812073, -0.036235611885786057, -0.19837747514247894) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.31502403864809386, -0.33050926337615494, 0.09360235822569422, 0.8847384248640863), new Position(-0.0, 0.0, -0.07769430428743362) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.09124101207447273, 0.011822489717917535, -0.0010215463335303187, 0.9957581347373388), new Position(-0.0, 0.0, -0.05970576032996178) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.31502560071341706, 0.3305090250153485, -0.09360335531662929, 0.8847378522206583), new Position(-0.0, 0.0, -0.07769458740949631) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.030647625348912563, -0.1715474455001442, 0.013972246400954492, 0.9845999052068606), new Position(-0.0, 0.0, -0.42901697754859924) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.21725642655881067, 0.122085694064187, -0.01046661362781645, 0.9683930908587317), new Position(-0.0, 0.0, -0.08567973226308823) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.14814227007533992, 0.0, 0.0, 0.988966059992417), new Position(-0.0, -6.139261321536083E-10, -0.08239995688199997) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.1702958841625315, 0.21566064609104252, 0.039382216339147326, 0.960697058703269), new Position(-0.0, 0.0, -0.46899113059043884) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(-0.02721770679398282, 0.17548412837029082, 0.01596583031636513, 0.9839764272528612), new Position(-0.0, 0.0, -0.386800616979599) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.0028573491555844784, -0.028602464089848206, 0.020951785117274253, 0.99936717842052), new Position(-0.0, 0.0, -0.3228152096271515) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.09127167722381735, -0.0118220872810959, 0.0010218752042396649, 0.9957553288635399), new Position(-0.0, 0.0, -0.05970684066414833) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.3406840712320836, 0.01948334667125986, -0.005636971305356307, 0.9399590349402056), new Position(5.847714756868072E-9, 0.0, -0.34087714552879333) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.12252232431034987, -0.027082555405620122, -0.010265189490810222, 0.9920430641474234), new Position(0.06678619980812073, -0.036235369741916656, -0.19837713241577148) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.1702952047566176, -0.21566118303287682, -0.03938221386170826, 0.960697058703269), new Position(-0.0, 0.0, -0.4689873456954956) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER, new Orientation(0.09105237905804604, -0.839921940252712, -0.5080439245546581, 0.16772587538328837), new Position(0.013824231922626495, -0.04942234978079796, 0.02176211029291153) ),
		new JointIdTransformationPair( RIGHT_EYELID, new Orientation(0.08303370698584592, -0.03247207451462258, 0.013852417609135477, 0.9959212209845152), new Position(0.07591389864683151, -0.03628937155008316, -0.23341913521289825) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.02471370368477378, 0.17250297606962378, 0.07392575220265646, 0.9819200269159273), new Position(-0.0, 0.0, -0.42901375889778137) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.34068797016769964, -0.019483230065834627, 0.005637000105041317, 0.9399576240230355), new Position(-0.0, 0.0, -0.340877503156662) ),
		new JointIdTransformationPair( LEFT_EYELID, new Orientation(0.12960957811489243, 0.04449026342252673, -0.17167695462244215, 0.975576238421702), new Position(-0.0759139209985733, -0.036288902163505554, -0.2334192395210266) )
	);


	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_RING_FINGER, new Orientation(-0.01545301789540626, 0.5850520957862128, 0.7933695498929247, 0.16745150568681835), new Position(-0.013824241235852242, -0.049429282546043396, 0.02175980433821678) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(0.5438187132210545, -0.11495978193094278, 0.13852331793401826, 0.8196686806738058), new Position(-0.0, 0.0, -0.38680312037467957) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.1375857878871946, -0.03542143618924948, 0.38847166322459065, 0.9104423318921804), new Position(-0.0, 0.0, -0.32281410694122314) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.13722481961513902, 0.050649104581101105, -0.004184168128327685, 0.989235315697837), new Position(-0.06678619980812073, -0.036235611885786057, -0.19837747514247894) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.5556998022883619, 0.04234378229032735, -0.010543034237227196, 0.8302370614873185), new Position(-0.0, 0.0, -0.05970576032996178) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.7797490018894094, -0.22332479382883025, 0.07270827709870466, 0.5803714646285758), new Position(-0.0, 0.0, -0.07769430428743362) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.7375315717067222, 0.3571919554252704, 0.05152935708090923, 0.5707940198303693), new Position(-0.0, 0.0, -0.07769458740949631) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.09481115594966125, 0.0, 0.0, 0.9954952760849691), new Position(-0.0, 0.0, -0.08567973226308823) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.5988949337261201, -0.14752641138758235, -0.08865497199669643, 0.782113234922254), new Position(-0.0, 0.0, -0.42901697754859924) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.2496603207840647, 0.0, 0.0, 0.9683334777988407), new Position(-0.0, -6.139261321536083E-10, -0.08239995688199997) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.5944116651167826, 0.06663590902899975, 0.18777858918453746, 0.7790851233625486), new Position(-0.0, 0.0, -0.46899113059043884) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.10741649275696188, -0.06853573536473712, -0.343476954262615, 0.9304773677807998), new Position(-0.0, 0.0, -0.3228152096271515) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(0.5069979344137183, 0.15645911358100617, -0.0810562789006098, 0.8437437525270663), new Position(-0.0, 0.0, -0.386800616979599) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.5687837994979857, -0.06322337544727079, 0.0310550628139747, 0.8194652996309904), new Position(-0.0, 0.0, -0.05970684066414833) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(-0.06896159619124625, 0.6788978384754577, 0.20838051601384489, 0.7006565376201884), new Position(5.847714756868072E-9, 0.0, -0.34087714552879333) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.1513749021587864, -0.06899776349560266, 0.0068246740389198085, 0.9860417696290991), new Position(0.06678619980812073, -0.036235369741916656, -0.19837713241577148) ),
		new JointIdTransformationPair( RIGHT_EYELID, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(0.07591389864683151, -0.03628937155008316, -0.23341913521289825) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER, new Orientation(-0.017162958810192516, -0.5569284664489471, -0.8127515975384703, 0.17020856853392596), new Position(0.013824231922626495, -0.04942234978079796, 0.02176211029291153) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.6193646854240366, -0.02194272399709898, -0.1654988268136803, 0.7671479920044548), new Position(-0.0, 0.0, -0.4689873456954956) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.6064111851295992, 0.09417581939471827, 0.16233671986173126, 0.7726856922291121), new Position(-0.0, 0.0, -0.42901375889778137) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(-0.23256195614799097, -0.6354438788440724, -0.3263856313848529, 0.6599988128911526), new Position(-0.0, 0.0, -0.340877503156662) ),
		new JointIdTransformationPair( LEFT_EYELID, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0759139209985733, -0.036288902163505554, -0.2334192395210266) )
	);


	private final ImplementationAndVisualType resourceType;
	BatResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	BatResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public BipedImp createImplementation( SBiped abstraction ) {
		return new BipedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

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

import org.lgna.project.annotations.*;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum SkadiResource implements org.lgna.story.resources.BipedResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_0 = new org.lgna.story.resources.JointId( HEAD, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_1 = new org.lgna.story.resources.JointId( HAIR_0, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_2 = new org.lgna.story.resources.JointId( HAIR_1, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_3 = new org.lgna.story.resources.JointId( HAIR_2, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_4 = new org.lgna.story.resources.JointId( HAIR_3, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_5 = new org.lgna.story.resources.JointId( HAIR_4, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_6 = new org.lgna.story.resources.JointId( HAIR_5, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_7 = new org.lgna.story.resources.JointId( HAIR_6, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_8 = new org.lgna.story.resources.JointId( HAIR_7, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId HAIR_9 = new org.lgna.story.resources.JointId( HAIR_8, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_THUMB_TIP = new org.lgna.story.resources.JointId( LEFT_THUMB_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_INDEX_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_INDEX_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_MIDDLE_10 = new org.lgna.story.resources.JointId( LEFT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_MIDDLE_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_MIDDLE_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_RING_10 = new org.lgna.story.resources.JointId( LEFT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_RING_10, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_RING_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_PINKY_10 = new org.lgna.story.resources.JointId( LEFT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_PINKY_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_PINKY_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_THUMB_TIP = new org.lgna.story.resources.JointId( RIGHT_THUMB_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_INDEX_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_INDEX_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_MIDDLE_10 = new org.lgna.story.resources.JointId( RIGHT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_MIDDLE_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_RING_10 = new org.lgna.story.resources.JointId( RIGHT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_RING_10, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_RING_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_PINKY_10 = new org.lgna.story.resources.JointId( RIGHT_HAND, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_PINKY_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_PINKY_FINGER_KNUCKLE, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOES = new org.lgna.story.resources.JointId( LEFT_FOOT, SkadiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOES = new org.lgna.story.resources.JointId( RIGHT_FOOT, SkadiResource.class );

	public static final org.lgna.story.JointedModelPose POWER_STANCE_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_PINKY_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(1.2434497928741312E-16, -1.4210854397564648E-16, -0.02273881435394287) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.09600571854815176, 0.2799007737975888, -0.028144141738189556, 0.9548017417869454), new Position(-0.04338232800364494, 0.142164409160614, -0.073753222823143) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.1610564079627629, 0.0, 0.0, 0.9869452028629209), new Position(3.0589787561811205E-16, 6.394884346555194E-16, -0.27901291847229004) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(0.010463012856545975, -0.20618053138929385, -0.005270067986955884, 0.9784438360076704), new Position(0.024971121922135353, 0.0019849371165037155, -0.02711504139006138) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(7.993605433193992E-17, 5.684341759025859E-16, -0.029321124777197838) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(5.329070399086743E-17, 5.684341759025859E-16, -0.026309026405215263) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6837394695281791, 0.0, 0.0, 0.7297262074294196), new Position(7.041265114772301E-17, 3.552713599391162E-17, -0.10950502753257751) ),
		new JointIdTransformationPair( LEFT_MIDDLE_10, new Orientation(-0.0681124887370651, -0.06832531942267542, 0.04639835963543958, 0.9942532533650433), new Position(0.006031947210431099, 9.704969124868512E-4, -0.03223602846264839) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.1357228562717597, 0.07278455980854152, 0.029491737775374664, 0.9876294606490335), new Position(2.8421708795129297E-16, -2.1316281596346973E-16, -0.3942423164844513) ),
		new JointIdTransformationPair( LEFT_PINKY_10, new Orientation(-0.052867426860928955, 0.23780861857236563, 0.0735638069123435, 0.9670783124558067), new Position(-0.026409318670630455, -0.005099527072161436, -0.025682644918560982) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.04061540783828825, -0.043909162890797146, -0.026710699155043944, 0.9978521496749946), new Position(1.332267550140849E-16, 4.2632563192693945E-16, -0.027933960780501366) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.03915759328922892, -0.038150458056010875, -0.028376486580241646, 0.9981011974981662), new Position(1.4210854397564648E-16, 0.0, -0.3215782940387726) ),
		new JointIdTransformationPair( RIGHT_EYELID, new Orientation(0.009072751051189291, -0.24557597579183618, -0.07544045179691897, 0.9663947762353268), new Position(0.05313999950885773, 0.1433715671300888, -0.08428233861923218) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.33884033668205543, -0.6139094114668487, 0.6683119454441919, 0.24831754735865597), new Position(0.051861099898815155, -0.05273563414812088, -0.0249774232506752) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-5.329070399086743E-17, 1.4210854397564648E-16, -0.027953479439020157) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.1349950511966433, -0.07412635875403528, 0.005581165262100016, 0.988053879950178), new Position(-4.973799171496525E-16, 5.062616928763243E-16, -0.3942428529262543) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(0.019418376296587766, 0.06575560007277799, 0.05324782052081266, 0.9962247724940674), new Position(-1.0214051350095407E-16, -2.8421708795129297E-16, -0.0242975614964962) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.1298404710633364, 0.0, 0.0, 0.9915348970530744), new Position(2.910191132423247E-17, -2.1316281596346973E-16, -0.09441082924604416) ),
		new JointIdTransformationPair( RIGHT_PINKY_10, new Orientation(-0.04380285072079417, -0.26835072876266275, -0.11172219997953775, 0.9558176325392892), new Position(0.02780676633119583, -0.0058725448325276375, -0.023986242711544037) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.09879384188191227, -0.2675465595715316, 0.03134381384784887, 0.9579541641441515), new Position(0.043382298201322556, 0.142161563038826, -0.0737532377243042) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_KNUCKLE, new Orientation(0.00846035225874336, 0.03405264341158399, 0.024864690515514196, 0.9990748656041294), new Position(-1.5987210866387985E-16, 1.4210854397564648E-16, -0.021499481052160263) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.03916664401485651, 0.03815062238716525, 0.02837690291190827, 0.9981008242603617), new Position(-0.0, 1.4210854397564648E-16, -0.3215820789337158) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.042902971611338936, -0.043216188263086396, -0.05561544575131613, 0.9965935070492926), new Position(1.2434497928741312E-16, -2.8421708795129297E-16, -0.02429712936282158) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-1.7319478548877732E-16, 0.0, -0.02273833006620407) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.051380986078707634, 0.20642253573687366, 0.21298148822946983, 0.9536186956436513), new Position(-0.10668836534023285, -0.0034441137686371803, -0.09409034252166748) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.2070621483189654, -0.63443479358621, -0.05080067394445595, 0.7429919588713911), new Position(0.015788406133651733, -5.206303321756423E-4, 0.010130287148058414) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(-0.0020592031458874643, -0.024053653457499356, -0.024718773616787485, 0.9994029035722443), new Position(-7.993605433193992E-17, -4.2632563192693945E-16, -0.02180512621998787) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_10, new Orientation(-0.027848501191620714, 0.026812794074803876, -0.04881502437660751, 0.9980594313217994), new Position(-0.0038367819506675005, 0.0017272414406761527, -0.03253864124417305) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-8.465450761290166E-18, -4.2632563192693945E-16, -0.02932170405983925) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.2595499298654142, 0.34842052733214696, 0.1451174983341616, 0.8889195023834734), new Position(4.44089183380283E-17, 5.684341759025859E-16, -0.09253329038619995) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.2529161785083773, 0.03589960142990384, -0.024188389267627383, 0.9665192947326662), new Position(-6.487857473480574E-10, 2.7213962194849728E-9, -0.396342009305954) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6834076083021215, 0.0, -0.0, 0.7300370133868378), new Position(-3.942378463917557E-33, 6.821210110831031E-15, -5.373144813347608E-5) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(0.0740466568753019, 0.6375587597835362, 0.3381478438327967, 0.6882528286466398), new Position(-0.016432177275419235, 2.5314931917819194E-5, 0.009063763543963432) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.039689612045628445, -0.20898556736288967, -0.15900900131059348, 0.9640881208851638), new Position(0.1066880002617836, -0.00344410864636302, -0.09409049898386002) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(0.021102623267829957, 0.173740198992521, -0.024656662030538862, 0.9842566085949167), new Position(-0.023011881858110428, 0.00350186531431973, -0.028651585802435875) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.9095418453216553, -0.027282966300845146) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.029504386322298303, 0.05201901169585767, 0.016961651802833586, 0.9980660378842926), new Position(-3.0198066587441613E-16, -4.2632563192693945E-16, -0.02793458290398121) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-2.309263806517031E-16, -7.105426934084528E-16, -0.026309233158826828) ),
		new JointIdTransformationPair( LEFT_EYELID, new Orientation(0.02057205893743667, 0.27095300536620304, 0.0728582865594778, 0.9596108218198219), new Position(-0.0531420074403286, 0.14337530732154846, -0.08428242057561874) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.28827389550067345, 0.0, -0.0, 0.9575479941876892), new Position(1.781711677468035E-18, -1.1990407818918743E-16, -0.2113315910100937) ),
		new JointIdTransformationPair( HAIR_7, new Orientation(-0.3060015195858692, 0.0, 0.0, 0.9520310236600165), new Position(-2.7570624648106957E-17, -2.8421708795129297E-16, -0.14747649431228638) ),
		new JointIdTransformationPair( RIGHT_RING_10, new Orientation(-0.09983443875476346, -0.11127859031994256, -0.09295168477658469, 0.9843831289046568), new Position(0.014120998792350292, -0.002198535716161132, -0.03310701251029968) ),
		new JointIdTransformationPair( HAIR_6, new Orientation(0.1916100331960617, 0.0, -0.0, 0.9814711382300573), new Position(5.5220638782684303E-17, 5.684341759025859E-16, -0.09643010795116425) ),
		new JointIdTransformationPair( HAIR_5, new Orientation(-0.21525181251409625, 0.0, 0.0, 0.9765585784833373), new Position(1.1247627256678584E-17, 2.8421708795129297E-16, -0.07034255564212799) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.2542374449179622, -0.024875008505471037, -0.04257984216992663, 0.9658837987015949), new Position(2.8421708795129297E-16, 1.0658140798173486E-16, -0.39634189009666443) ),
		new JointIdTransformationPair( HAIR_0, new Orientation(0.0, -0.9938412798339347, 0.11081295275393971, 6.123233995736766E-17), new Position(-4.071941487335598E-16, 0.13461828231811523, 0.05204447731375694) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_TIP, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-1.2434497928741312E-16, 5.684341759025859E-16, -0.0279537346214056) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.2595853766294894, -0.3484243551572831, -0.14510856864967048, 0.8889091091207253), new Position(-2.742250902587872E-16, 2.8421708795129297E-16, -0.09253305196762085) ),
		new JointIdTransformationPair( HAIR_4, new Orientation(0.30170072243881846, 0.0, -0.0, 0.9534026820184087), new Position(6.701000068459428E-17, -5.684341759025859E-16, -0.1326300948858261) ),
		new JointIdTransformationPair( HAIR_3, new Orientation(-0.26457545551576955, 0.0, 0.0, 0.9643649870970136), new Position(1.97573429340883E-17, -2.8421708795129297E-16, -0.07949487864971161) ),
		new JointIdTransformationPair( HAIR_2, new Orientation(0.16368823276980618, 0.0, -0.0, 0.9865121197697967), new Position(1.1564867565204921E-17, 2.8421708795129297E-16, -0.08341380953788757) ),
		new JointIdTransformationPair( HAIR_1, new Orientation(-0.1213032540635705, 0.0, 0.0, 0.9926154948184059), new Position(-1.510368312409815E-18, 0.0, -0.05599312111735344) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(-0.0020399352552912574, 0.024038691991965996, 0.024702970666512045, 0.9994036938053206), new Position(1.0658140798173486E-16, 2.8421708795129297E-16, -0.021804768592119217) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.3388323846754823, 0.6138873735650515, -0.6683320279459747, 0.24832883057379745), new Position(-0.05186111479997635, -0.05273483693599701, -0.0249731857329607) ),
		new JointIdTransformationPair( LEFT_RING_10, new Orientation(-0.10859870749958948, 0.08172412203782212, 0.06711466913352655, 0.9884447934981817), new Position(-0.012006398290395737, -0.0021863908041268587, -0.03393262252211571) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER_KNUCKLE, new Orientation(0.014996495233482208, -0.03170442785479359, -0.025384078704700525, 0.9990623518747095), new Position(1.6875389762544143E-16, 0.0, -0.021499307826161385) )
	);


	public static final org.lgna.story.JointedModelPose BOW_DRAW_MID_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_PINKY_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.02273881435394287) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(-0.5289194386826935, -0.00739636631782105, -0.03368822807140712, 0.8479708865511161), new Position(-0.0, 0.0, -0.02180512621998787) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_10, new Orientation(-0.550120437951657, 0.04852838721811353, -0.027328159606814124, 0.83322606240707), new Position(-0.0038367819506675005, 0.0017272414406761527, -0.03253864124417305) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.1610564079627629, 0.0, 0.0, 0.9869452028629209), new Position(-0.0, 0.0, -0.27901291847229004) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.2832219980309548, -0.22798798222890151, 0.10089701452088663, 0.9260812989426576), new Position(0.024971121922135353, 0.0019849371165037155, -0.02711504139006138) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02932170405983925) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.029321124777197838) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(0.021760626489376995, 0.5881357291480397, 0.16676398520162847, 0.7910831893511356), new Position(-0.0, 0.0, -0.09253329038619995) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.026309026405215263) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.7164857549725969, -0.07122456425457951, 0.24913174537691182, 0.6476948338635616), new Position(-0.0, 0.0, -0.10950502753257751) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.10830781425014217, 0.8015680201329547, 0.38476653677618866, 0.44464911857525335), new Position(-0.016432177275419235, 2.5314931917819194E-5, 0.009063763543963432) ),
		new JointIdTransformationPair( LEFT_MIDDLE_10, new Orientation(-0.5641124859400349, -0.08242520209612604, 0.005219916595929057, 0.8215570228150315), new Position(0.006031947210431099, 9.704969124868512E-4, -0.03223602846264839) ),
		new JointIdTransformationPair( LEFT_PINKY_10, new Orientation(-0.5371692470103108, 0.1673864307177535, 0.18424538903238144, 0.8059060860287959), new Position(-0.026409318670630455, -0.005099527072161436, -0.025682644918560982) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.5608574434719518, -0.02321418997607412, -0.04585392719167324, 0.8263155855040869), new Position(-0.0, 0.0, -0.027933960780501366) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.5012525419115902, 0.16060948226761554, 0.07069791189044312, 0.847320652815231), new Position(-0.023011881858110428, 0.00350186531431973, -0.028651585802435875) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.4026248798813739, -0.06797437044821494, -0.1461511865936965, 0.9010618856215885), new Position(-0.0, 0.0, -0.3215782940387726) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.6481627306399002, 0.08865835399995356, 0.1426228389550957, 0.742753994727619), new Position(-0.0, 0.0, -0.02793458290398121) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.026309233158826828) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.2834302126311458, -0.6320524557985647, 0.6936288384125903, 0.19762601602336585), new Position(0.051861099898815155, -0.05273563414812088, -0.0249774232506752) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.28827389550067345, 0.0, -0.0, 0.9575479941876892), new Position(-0.0, 0.0, -0.2113315910100937) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.027953479439020157) ),
		new JointIdTransformationPair( RIGHT_RING_10, new Orientation(-0.6040630955523023, -0.04550771036607788, -0.13766623594304436, 0.7836356502675725), new Position(0.014120998792350292, -0.002198535716161132, -0.03310701251029968) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.04621449680831873, -0.15658157567400818, -0.7321343452702931, 0.6613060796029612), new Position(-0.0, 0.0, -0.30867406725883484) ),
		new JointIdTransformationPair( LEFT_THUMB_KNUCKLE, new Orientation(0.16867467040048859, 0.2152465505071538, -0.16548728067410529, 0.9475398345162844), new Position(-0.0, 0.0, -0.024447446689009666) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.0279537346214056) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.0477075442310178, -0.7202318288774009, 0.02116797775585909, 0.6917673160919697), new Position(-0.0, 0.0, -0.09253305196762085) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(0.3296667807849455, -0.03989252088846407, -0.013784879420290877, 0.9431534220493621), new Position(-0.0, 0.0, -0.036505747586488724) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.48973904322183054, 0.029554183205364574, 0.07928231637707231, 0.8677537289514321), new Position(-0.0, 0.0, -0.0242975614964962) ),
		new JointIdTransformationPair( NECK, new Orientation(0.017475056292400974, -0.05529755669954047, 0.3116175266142842, 0.9484362496960111), new Position(-0.0, 0.0, -0.09441082924604416) ),
		new JointIdTransformationPair( RIGHT_THUMB_KNUCKLE, new Orientation(-0.5224097591477126, 0.03404584180469009, 0.11170186989031833, 0.8446606516619828), new Position(-0.0, 0.0, -0.024447547271847725) ),
		new JointIdTransformationPair( RIGHT_PINKY_10, new Orientation(-0.5413927846647322, -0.169049422090012, -0.2364661535402772, 0.7889232559849317), new Position(0.02780676633119583, -0.0058725448325276375, -0.023986242711544037) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.13003999361627067, 0.18787830613919765, 0.5112572005200529, 0.8284970833134807), new Position(-0.0, 0.0, -0.30867230892181396) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(-0.5098336155932401, 0.008142035547694331, 0.03349344916050406, 0.8595822127822313), new Position(-0.0, 0.0, -0.021804768592119217) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_KNUCKLE, new Orientation(-0.5198093741293213, 0.015814136641616683, 0.03908634981475234, 0.8532411059646169), new Position(-0.0, 0.0, -0.021499481052160263) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.5621369207141035, -0.0073786174015645365, -0.07004475444909199, 0.8240396657621971), new Position(-0.0, 0.0, -0.02429712936282158) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.09636066946987315, -0.9033860753913241, 0.11369932574454758, 0.4020953661677962), new Position(-0.0, 0.0, -0.3215820789337158) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.2687393145496148, 0.4675389915143716, -0.6871917994559464, 0.4867791110859482), new Position(-0.05186111479997635, -0.05273483693599701, -0.0249731857329607) ),
		new JointIdTransformationPair( LEFT_RING_10, new Orientation(-0.5960235672256291, 0.036255554403540786, 0.09934155448281709, 0.7959727995600511), new Position(-0.012006398290395737, -0.0021863908041268587, -0.03393262252211571) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02273833006620407) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER_KNUCKLE, new Orientation(-0.49498940972888567, -0.014397002611244249, -0.03797706957194314, 0.8679492800612194), new Position(-0.0, 0.0, -0.021499307826161385) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.500841976780563, -0.42472414943873665, -0.2928330073451874, 0.6949931949215425), new Position(0.015788406133651733, -5.206303321756423E-4, 0.010130287148058414) )
	);


	public static final org.lgna.story.JointedModelPose BOW_DRAW_START_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_PINKY_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.02273881435394287) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(-0.5289194386826935, -0.00739636631782105, -0.03368822807140712, 0.8479708865511161), new Position(-0.0, 0.0, -0.02180512621998787) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_10, new Orientation(-0.550120437951657, 0.04852838721811353, -0.027328159606814124, 0.83322606240707), new Position(-0.0038367819506675005, 0.0017272414406761527, -0.03253864124417305) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.1610564079627629, 0.0, 0.0, 0.9869452028629209), new Position(-0.0, 0.0, -0.27901291847229004) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.2832219980309548, -0.22798798222890151, 0.10089701452088663, 0.9260812989426576), new Position(0.024971121922135353, 0.0019849371165037155, -0.02711504139006138) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02932170405983925) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.029321124777197838) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.04002527193183543, 0.5915808558310175, 0.3382345811310198, 0.7307718089445892), new Position(-0.0, 0.0, -0.09253329038619995) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.026309026405215263) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6202096266211419, -0.30325677075254354, 0.22370886027656708, 0.6879896044803021), new Position(-0.0, 0.0, -0.10950502753257751) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.10830781425014217, 0.8015680201329547, 0.38476653677618866, 0.44464911857525335), new Position(-0.016432177275419235, 2.5314931917819194E-5, 0.009063763543963432) ),
		new JointIdTransformationPair( LEFT_MIDDLE_10, new Orientation(-0.5641124859400349, -0.08242520209612604, 0.005219916595929057, 0.8215570228150315), new Position(0.006031947210431099, 9.704969124868512E-4, -0.03223602846264839) ),
		new JointIdTransformationPair( LEFT_PINKY_10, new Orientation(-0.5371692470103108, 0.1673864307177535, 0.18424538903238144, 0.8059060860287959), new Position(-0.026409318670630455, -0.005099527072161436, -0.025682644918560982) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.5608574434719518, -0.02321418997607412, -0.04585392719167324, 0.8263155855040869), new Position(-0.0, 0.0, -0.027933960780501366) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.5012525419115902, 0.16060948226761554, 0.07069791189044312, 0.847320652815231), new Position(-0.023011881858110428, 0.00350186531431973, -0.028651585802435875) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.6916460112713583, -0.18394801791464174, -0.22487059230769882, 0.6612277508641631), new Position(-0.0, 0.0, -0.3215782940387726) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.6481627306399002, 0.08865835399995356, 0.1426228389550957, 0.742753994727619), new Position(-0.0, 0.0, -0.02793458290398121) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.026309233158826828) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.33762673266491017, -0.6143590886335187, 0.6689258876276367, 0.2472028650015772), new Position(0.051861099898815155, -0.05273563414812088, -0.0249774232506752) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.28827389550067345, 0.0, -0.0, 0.9575479941876892), new Position(-0.0, 0.0, -0.2113315910100937) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.027953479439020157) ),
		new JointIdTransformationPair( RIGHT_RING_10, new Orientation(-0.6040630955523023, -0.04550771036607788, -0.13766623594304436, 0.7836356502675725), new Position(0.014120998792350292, -0.002198535716161132, -0.03310701251029968) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.0378390975448039, -0.23281149585682043, -0.7501875796435052, 0.6177261573242013), new Position(-0.0, 0.0, -0.30867406725883484) ),
		new JointIdTransformationPair( LEFT_THUMB_KNUCKLE, new Orientation(0.16867467040048859, 0.2152465505071538, -0.16548728067410529, 0.9475398345162844), new Position(-0.0, 0.0, -0.024447446689009666) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.0279537346214056) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.07138714144043797, -0.7838837520529139, 0.09221739365772103, 0.6098574354809372), new Position(-0.0, 0.0, -0.09253305196762085) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(0.3296667807849455, -0.03989252088846407, -0.013784879420290877, 0.9431534220493621), new Position(-0.0, 0.0, -0.036505747586488724) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.48973904322183054, 0.029554183205364574, 0.07928231637707231, 0.8677537289514321), new Position(-0.0, 0.0, -0.0242975614964962) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.24317581365371552, -0.09145320175721368, 0.03540688525426896, 0.9650120144427212), new Position(-0.0, 0.0, -0.09441082924604416) ),
		new JointIdTransformationPair( RIGHT_THUMB_KNUCKLE, new Orientation(-0.5224097591477126, 0.03404584180469009, 0.11170186989031833, 0.8446606516619828), new Position(-0.0, 0.0, -0.024447547271847725) ),
		new JointIdTransformationPair( RIGHT_PINKY_10, new Orientation(-0.5413927846647322, -0.169049422090012, -0.2364661535402772, 0.7889232559849317), new Position(0.02780676633119583, -0.0058725448325276375, -0.023986242711544037) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.24854378329197616, 0.12060895350827978, 0.09932637165183726, 0.9559360543545943), new Position(-0.0, 0.0, -0.30867230892181396) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(-0.5098336155932401, 0.008142035547694331, 0.03349344916050406, 0.8595822127822313), new Position(-0.0, 0.0, -0.021804768592119217) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_KNUCKLE, new Orientation(-0.5198093741293213, 0.015814136641616683, 0.03908634981475234, 0.8532411059646169), new Position(-0.0, 0.0, -0.021499481052160263) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.5621369207141035, -0.0073786174015645365, -0.07004475444909199, 0.8240396657621971), new Position(-0.0, 0.0, -0.02429712936282158) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.12073684397945228, -0.7690997688481713, 0.17638119538216418, 0.6023270158137768), new Position(-0.0, 0.0, -0.3215820789337158) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.4089483275116297, 0.25012878227080676, -0.5999223918997418, 0.6405388211517369), new Position(-0.05186111479997635, -0.05273483693599701, -0.0249731857329607) ),
		new JointIdTransformationPair( LEFT_RING_10, new Orientation(-0.5960235672256291, 0.036255554403540786, 0.09934155448281709, 0.7959727995600511), new Position(-0.012006398290395737, -0.0021863908041268587, -0.03393262252211571) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02273833006620407) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER_KNUCKLE, new Orientation(-0.49498940972888567, -0.014397002611244249, -0.03797706957194314, 0.8679492800612194), new Position(-0.0, 0.0, -0.021499307826161385) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.500841976780563, -0.42472414943873665, -0.2928330073451874, 0.6949931949215425), new Position(0.015788406133651733, -5.206303321756423E-4, 0.010130287148058414) )
	);


	public static final org.lgna.story.JointedModelPose BOW_DRAW_END_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_PINKY_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.02273881435394287) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_KNUCKLE, new Orientation(-0.5289194386826935, -0.00739636631782105, -0.03368822807140712, 0.8479708865511161), new Position(-0.0, 0.0, -0.02180512621998787) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_10, new Orientation(-0.550120437951657, 0.04852838721811353, -0.027328159606814124, 0.83322606240707), new Position(-0.0038367819506675005, 0.0017272414406761527, -0.03253864124417305) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.1610564079627629, 0.0, 0.0, 0.9869452028629209), new Position(-0.0, 0.0, -0.27901291847229004) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.2832219980309548, -0.22798798222890151, 0.10089701452088663, 0.9260812989426576), new Position(0.024971121922135353, 0.0019849371165037155, -0.02711504139006138) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02932170405983925) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.029321124777197838) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(0.05947428340586358, 0.540906643918545, 0.027260494022659433, 0.8385342435725425), new Position(-0.0, 0.0, -0.09253329038619995) ),
		new JointIdTransformationPair( RIGHT_RING_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.026309026405215263) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6202096266211419, -0.30325677075254354, 0.22370886027656708, 0.6879896044803021), new Position(-0.0, 0.0, -0.10950502753257751) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.10830781425014217, 0.8015680201329547, 0.38476653677618866, 0.44464911857525335), new Position(-0.016432177275419235, 2.5314931917819194E-5, 0.009063763543963432) ),
		new JointIdTransformationPair( LEFT_MIDDLE_10, new Orientation(-0.5641124859400349, -0.08242520209612604, 0.005219916595929057, 0.8215570228150315), new Position(0.006031947210431099, 9.704969124868512E-4, -0.03223602846264839) ),
		new JointIdTransformationPair( LEFT_PINKY_10, new Orientation(-0.5371692470103108, 0.1673864307177535, 0.18424538903238144, 0.8059060860287959), new Position(-0.026409318670630455, -0.005099527072161436, -0.025682644918560982) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.5608574434719518, -0.02321418997607412, -0.04585392719167324, 0.8263155855040869), new Position(-0.0, 0.0, -0.027933960780501366) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.5012525419115902, 0.16060948226761554, 0.07069791189044312, 0.847320652815231), new Position(-0.023011881858110428, 0.00350186531431973, -0.028651585802435875) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.03915759328922892, -0.038150458056010875, -0.028376486580241646, 0.9981011974981662), new Position(-0.0, 0.0, -0.3215782940387726) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.6481627306399002, 0.08865835399995356, 0.1426228389550957, 0.742753994727619), new Position(-0.0, 0.0, -0.02793458290398121) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.026309233158826828) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.2284145544085296, -0.6455517956358635, 0.7136388950466341, 0.14767937554812482), new Position(0.051861099898815155, -0.05273563414812088, -0.0249774232506752) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.28827389550067345, 0.0, -0.0, 0.9575479941876892), new Position(-0.0, 0.0, -0.2113315910100937) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_TIP, new Orientation(-0.5083801031745258, 0.0, 0.0, 0.8611327834290473), new Position(-0.0, 0.0, -0.027953479439020157) ),
		new JointIdTransformationPair( RIGHT_RING_10, new Orientation(-0.6040630955523023, -0.04550771036607788, -0.13766623594304436, 0.7836356502675725), new Position(0.014120998792350292, -0.002198535716161132, -0.03310701251029968) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.02785027161806204, -0.06668456777796429, -0.7572436535098135, 0.6491221610838562), new Position(-0.0, 0.0, -0.30867406725883484) ),
		new JointIdTransformationPair( LEFT_THUMB_KNUCKLE, new Orientation(0.16867467040048859, 0.2152465505071538, -0.16548728067410529, 0.9475398345162844), new Position(-0.0, 0.0, -0.024447446689009666) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.0279537346214056) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.04440613354539882, -0.6348058683573783, -0.04220350490850484, 0.7702392284063574), new Position(-0.0, 0.0, -0.09253305196762085) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(0.3296667807849455, -0.03989252088846407, -0.013784879420290877, 0.9431534220493621), new Position(-0.0, 0.0, -0.036505747586488724) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.48973904322183054, 0.029554183205364574, 0.07928231637707231, 0.8677537289514321), new Position(-0.0, 0.0, -0.0242975614964962) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.12044245576806882, 0.048499171427184566, 0.3703668912545329, 0.9197661719599641), new Position(-0.0, 0.0, -0.09441082924604416) ),
		new JointIdTransformationPair( RIGHT_THUMB_KNUCKLE, new Orientation(-0.5224097591477126, 0.03404584180469009, 0.11170186989031833, 0.8446606516619828), new Position(-0.0, 0.0, -0.024447547271847725) ),
		new JointIdTransformationPair( RIGHT_PINKY_10, new Orientation(-0.5413927846647322, -0.169049422090012, -0.2364661535402772, 0.7889232559849317), new Position(0.02780676633119583, -0.0058725448325276375, -0.023986242711544037) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.13003999361627067, 0.18787830613919765, 0.5112572005200529, 0.8284970833134807), new Position(-0.0, 0.0, -0.30867230892181396) ),
		new JointIdTransformationPair( LEFT_RING_FINGER_KNUCKLE, new Orientation(-0.5098336155932401, 0.008142035547694331, 0.03349344916050406, 0.8595822127822313), new Position(-0.0, 0.0, -0.021804768592119217) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_KNUCKLE, new Orientation(-0.5198093741293213, 0.015814136641616683, 0.03908634981475234, 0.8532411059646169), new Position(-0.0, 0.0, -0.021499481052160263) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.5621369207141035, -0.0073786174015645365, -0.07004475444909199, 0.8240396657621971), new Position(-0.0, 0.0, -0.02429712936282158) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.022412322820064463, -0.9215537455350172, 0.1641985428743364, 0.3511057111385893), new Position(-0.0, 0.0, -0.3215820789337158) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.07928773401542215, 0.723395461299857, -0.5893985063909252, 0.350744725505051), new Position(-0.05186111479997635, -0.05273483693599701, -0.0249731857329607) ),
		new JointIdTransformationPair( LEFT_RING_10, new Orientation(-0.5960235672256291, 0.036255554403540786, 0.09934155448281709, 0.7959727995600511), new Position(-0.012006398290395737, -0.0021863908041268587, -0.03393262252211571) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_TIP, new Orientation(-0.52748497378837, 0.0, 0.0, 0.849564360379767), new Position(-0.0, 0.0, -0.02273833006620407) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER_KNUCKLE, new Orientation(-0.49498940972888567, -0.014397002611244249, -0.03797706957194314, 0.8679492800612194), new Position(-0.0, 0.0, -0.021499307826161385) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.500841976780563, -0.42472414943873665, -0.2928330073451874, 0.6949931949215425), new Position(0.015788406133651733, -5.206303321756423E-4, 0.010130287148058414) )
	);


	public static final org.lgna.story.resources.JointId[] HAIR_ARRAY = { HAIR_0, HAIR_1, HAIR_2, HAIR_3, HAIR_4, HAIR_5, HAIR_6, HAIR_7, HAIR_8, HAIR_9 };

	private final ImplementationAndVisualType resourceType;
	private SkadiResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private SkadiResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BipedImp createImplementation( org.lgna.story.SBiped abstraction ) {
		return new org.lgna.story.implementation.BipedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

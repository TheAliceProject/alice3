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

package org.lgna.story.resources.quadruped;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.QuadrupedImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.QuadrupedResource;

public enum SquirrelResource implements QuadrupedResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LOWER_LIP = new JointId(MOUTH, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_EAR_0 = new JointId(HEAD, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_EAR_MIDDLE = new JointId(LEFT_EAR, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_EAR_TIP = new JointId(LEFT_EAR_MIDDLE, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_EAR_0 = new JointId(HEAD, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_EAR_MIDDLE = new JointId(RIGHT_EAR, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_EAR_TIP = new JointId(RIGHT_EAR_MIDDLE, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE_0 = new JointId(FRONT_LEFT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE_1 = new JointId(FRONT_LEFT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE_2 = new JointId(FRONT_LEFT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE_3 = new JointId(FRONT_LEFT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE_4 = new JointId(FRONT_LEFT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE_0 = new JointId(FRONT_RIGHT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE_1 = new JointId(FRONT_RIGHT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE_2 = new JointId(FRONT_RIGHT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE_3 = new JointId(FRONT_RIGHT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE_4 = new JointId(FRONT_RIGHT_FOOT, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_4 = new JointId(TAIL_3, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_5 = new JointId(TAIL_4, SquirrelResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_6 = new JointId(TAIL_5, SquirrelResource.class);

  public static final JointedModelPose STANDING_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_EYE, new Orientation(-0.0750122314029591, 0.5122789842817386, -0.11398096587507828, 0.847910223326387), new Position(-0.01959327608346939, 0.024357903748750687, -0.013047268614172935)), new JointIdTransformationPair(FRONT_RIGHT_KNEE, new Orientation(0.30511380499923824, -0.05548389494640932, -0.05167312338404807, 0.9492928903769371), new Position(-1.776356799695581E-17, -3.552713599391162E-17, -0.049763813614845276)), new JointIdTransformationPair(FRONT_LEFT_ANKLE, new Orientation(0.12131786279857901, 0.21458499480220022, 0.7750750994676942, 0.5818022399036195), new Position(-2.6645351995433716E-17, 3.552713599391162E-17, -0.06343860179185867)),
                                                                            new JointIdTransformationPair(LEFT_TOE_3, new Orientation(0.0, 0.2874992474255871, 0.0, 0.9577808636268116), new Position(-0.006314834114164114, -0.0013590127928182483, -0.004921471234411001)), new JointIdTransformationPair(LEFT_TOE_4, new Orientation(-0.0, -0.48711065737288595, -0.0, 0.8733402587043351), new Position(0.007488240487873554, -0.002492679050192237, 0.009667515754699707)), new JointIdTransformationPair(LEFT_TOE_1, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(0.003016643924638629, -6.741586257703602E-4, -0.008445845916867256)),
                                                                            new JointIdTransformationPair(LEFT_TOE_2, new Orientation(0.0, 0.15861279227655012, 0.0, 0.9873408642035616), new Position(-0.002106588566675782, -8.659717277623713E-4, -0.0076146721839904785)), new JointIdTransformationPair(FRONT_LEFT_KNEE, new Orientation(0.3051173260202178, 0.05548474616279486, 0.0516743444075739, 0.9492916424553467), new Position(-8.881783998477905E-18, 1.776356799695581E-17, -0.049764443188905716)), new JointIdTransformationPair(LEFT_TOE_0, new Orientation(-0.0, -0.22590469364881308, -0.0, 0.9741494081440669), new Position(0.00788477249443531, -0.001227390137501061, -0.00721047492697835)),
                                                                            new JointIdTransformationPair(RIGHT_EYE, new Orientation(-0.0, -0.42816948207129063, -0.0, 0.9036984533697082), new Position(0.01959330029785633, 0.024357929825782776, -0.013047267682850361)), new JointIdTransformationPair(RIGHT_TOE_4, new Orientation(0.0, 0.5309810378313244, 0.0, 0.8473837014384745), new Position(-0.006820433307439089, -0.0025551931466907263, 0.010133632458746433)), new JointIdTransformationPair(FRONT_RIGHT_ANKLE, new Orientation(0.12241914759212004, -0.21638660863595874, -0.7644549123510279, 0.5948101166660074), new Position(-2.220445916901415E-17, -6.74889102936227E-11, -0.06343860924243927)),
                                                                            new JointIdTransformationPair(FRONT_RIGHT_SHOULDER, new Orientation(-0.3080521381687533, 0.3616405419834621, 0.30670122838128955, 0.8247753361206767), new Position(-2.6645351995433716E-17, -1.6136424878590572E-12, -0.027711201459169388)), new JointIdTransformationPair(FRONT_LEFT_SHOULDER, new Orientation(-0.3080537837191975, -0.3616402636834627, -0.30670439785090653, 0.8247736649276298), new Position(2.220445916901415E-17, 0.0, -0.027711234986782074)), new JointIdTransformationPair(RIGHT_TOE_2, new Orientation(-0.0, -0.10838386506426273, -0.0, 0.9941091176494318), new Position(0.0016144431428983808, -8.508010068908334E-4, -0.007735535502433777)),
                                                                            new JointIdTransformationPair(RIGHT_TOE_3, new Orientation(-0.0, -0.2688606322815513, -0.0, 0.9631790905170048), new Position(0.005993292201310396, -0.0013044408988207579, -0.00532176997512579)), new JointIdTransformationPair(RIGHT_TOE_0, new Orientation(0.0, 0.30188736094565216, 0.0, 0.9533436008602929), new Position(-0.008325471542775631, -0.001302139600738883, -0.006682592444121838)), new JointIdTransformationPair(RIGHT_TOE_1, new Orientation(-0.01730144292390853, 0.04601545319537822, 0.0394545634196674, 0.998011310339386), new Position(-0.0035528072621673346, -7.055760361254215E-4, -0.008232180960476398)));

  public static final JointedModelPose EATING_POSE = new JointedModelPose(new JointIdTransformationPair(TAIL_2, new Orientation(-0.27258462046971743, 0.0, 0.0, 0.9621318125305806), new Position(4.3043247117170193E-16, 3.88578060273166E-18, -0.04868576303124428)), new JointIdTransformationPair(TAIL_1, new Orientation(0.3665320044382992, 0.0, -0.0, 0.9304054437300131), new Position(-2.5349795581145807E-16, -1.776356799695581E-17, -0.05074014887213707)), new JointIdTransformationPair(TAIL_0, new Orientation(0.8883682570710801, 0.0, -0.0, 0.4591316149302847), new Position(-1.7927340659254572E-19, 0.028838425874710083, -0.051523223519325256)),
                                                                          new JointIdTransformationPair(BACK_RIGHT_ANKLE, new Orientation(0.05463190300357897, -0.9628409881280501, -0.2577112354743394, 0.0594769355725146), new Position(1.776356799695581E-17, -1.776356799695581E-17, -0.03370293602347374)), new JointIdTransformationPair(TAIL_5, new Orientation(0.22248880883264863, 0.0, -0.0, 0.9749352439748135), new Position(1.6448038347248299E-15, 0.0, -0.07331451773643494)), new JointIdTransformationPair(BACK_RIGHT_KNEE, new Orientation(0.7237869445541879, 0.03358302655276227, 0.08565638755371371, 0.6838622832791097), new Position(-4.44089183380283E-17, 4.44089183380283E-17, -0.0716027244925499)),
                                                                          new JointIdTransformationPair(TAIL_4, new Orientation(-0.2555848024282739, 0.0, 0.0, 0.966786640768117), new Position(1.3968397039764617E-15, 7.105427198782324E-17, -0.06855837255716324)), new JointIdTransformationPair(TAIL_3, new Orientation(-0.4700977342017449, 0.0, 0.0, 0.8826143666961158), new Position(1.0612414386260453E-15, -5.329070399086743E-17, -0.06198466569185257)), new JointIdTransformationPair(FRONT_RIGHT_KNEE, new Orientation(0.8954966048473947, -0.03911745232316617, 0.18084401677797118, 0.4047852482812867), new Position(-1.776356799695581E-17, -3.552713599391162E-17, -0.049763813614845276)),
                                                                          new JointIdTransformationPair(SPINE_UPPER, new Orientation(0.14338383271099603, 0.0, -0.0, 0.9896671544095546), new Position(3.786008972377658E-17, -2.6645351995433716E-17, -0.04811042174696922)), new JointIdTransformationPair(FRONT_LEFT_ANKLE, new Orientation(-0.27753719687113093, 0.3137477684880361, 0.7179487037612694, 0.5559542255340865), new Position(-2.6645351995433716E-17, 3.552713599391162E-17, -0.06343860179185867)), new JointIdTransformationPair(HEAD, new Orientation(-0.1160965043533141, 0.0, 0.0, 0.9932379380978864), new Position(4.3665815671657614E-17, -2.220445916901415E-17, -0.019550496712327003)),
                                                                          new JointIdTransformationPair(SPINE_BASE, new Orientation(0.4860517643472203, 0.0, -0.0, 0.8739300214404779), new Position(-0.0, 0.0, -0.0)), new JointIdTransformationPair(BACK_RIGHT_HIP, new Orientation(-0.39873226884741597, 0.07644563430860478, -0.13571467855244326, 0.9037423132731774), new Position(-0.03254690021276474, 0.011659993790090084, 0.02787788212299347)), new JointIdTransformationPair(FRONT_LEFT_KNEE, new Orientation(0.9038670354583347, 0.023931136476736337, -0.1252376071682812, 0.4083714297908908), new Position(-8.881783998477905E-18, 1.776356799695581E-17, -0.049764443188905716)),
                                                                          new JointIdTransformationPair(BACK_LEFT_ANKLE, new Orientation(0.05393738754442586, 0.9597877557897638, 0.26885973060087, 0.06010546832159967), new Position(-2.6645351995433716E-17, -2.220445916901415E-17, -0.033702969551086426)), new JointIdTransformationPair(ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.06866702437400818, 0.024998055770993233)), new JointIdTransformationPair(BACK_LEFT_HIP, new Orientation(-0.41693684515399737, -0.03014065328502759, 0.1001534306063584, 0.9028978339271618), new Position(0.03254692628979683, 0.011660014279186726, 0.02787831984460354)),
                                                                          new JointIdTransformationPair(FRONT_RIGHT_SHOULDER, new Orientation(-0.2585592654154772, 0.2337690284621893, 0.18644280494831691, 0.9185522457011669), new Position(-2.6645351995433716E-17, -1.6136424878590572E-12, -0.027711201459169388)), new JointIdTransformationPair(SPINE_MIDDLE, new Orientation(-0.4523870444126612, 0.0, 0.0, 0.8918217097870946), new Position(-1.0353799057764833E-32, 1.3322675997716858E-17, -0.062499478459358215)), new JointIdTransformationPair(NECK, new Orientation(-0.23728509515957316, 0.0, 0.0, 0.9714400566247576), new Position(3.3058018409053085E-17, 1.4210854397564648E-16, -0.04160454869270325)),
                                                                          new JointIdTransformationPair(FRONT_RIGHT_CLAVICLE, new Orientation(-0.6727211100287531, -0.32602876586486995, 0.10488227000335429, 0.655859178017092), new Position(0.01699949987232685, 0.013967754319310188, -0.03092999756336212)), new JointIdTransformationPair(FRONT_RIGHT_ANKLE, new Orientation(-0.29232202312105676, -0.23788836837945435, -0.7640446231618169, 0.5236341975135799), new Position(-2.220445916901415E-17, -6.74889102936227E-11, -0.06343860924243927)), new JointIdTransformationPair(FRONT_LEFT_CLAVICLE, new Orientation(-0.6794109986228718, 0.26598811384964977, -0.04421831022847516, 0.6824190496179866), new Position(-0.016999511048197746, 0.013967828825116158, -0.03093043901026249)),
                                                                          new JointIdTransformationPair(FRONT_LEFT_SHOULDER, new Orientation(-0.26375680736656054, -0.14057539500911617, -0.2344990054061964, 0.9250303353670812), new Position(2.220445916901415E-17, 0.0, -0.027711234986782074)), new JointIdTransformationPair(BACK_LEFT_KNEE, new Orientation(0.729112027489306, -0.03425117880351002, -0.08538919838827003, 0.6781822711628578), new Position(1.776356799695581E-17, 0.0, -0.07160308957099915)));

  public static final JointedModelPose ALERT_POSE = new JointedModelPose(new JointIdTransformationPair(TAIL_2, new Orientation(0.11424007441104207, 0.0, -0.0, 0.9934531722223043), new Position(4.3043247117170193E-16, 3.88578060273166E-18, -0.04868576303124428)), new JointIdTransformationPair(TAIL_1, new Orientation(0.07592027633689055, 0.0, -0.0, 0.9971138910079079), new Position(-2.5349795581145807E-16, -1.776356799695581E-17, -0.05074014887213707)), new JointIdTransformationPair(TAIL_0, new Orientation(0.23819743699201085, 0.0, -0.0, 0.9712167528468797), new Position(-1.7927340659254572E-19, 0.028838425874710083, -0.051523223519325256)),
                                                                         new JointIdTransformationPair(BACK_RIGHT_ANKLE, new Orientation(-0.0823020355776848, 0.8991281523399306, 0.3626194029117393, 0.2308724956366297), new Position(1.776356799695581E-17, -1.776356799695581E-17, -0.03370293602347374)), new JointIdTransformationPair(TAIL_5, new Orientation(-0.3209838809158258, 0.0, 0.0, 0.9470846573523483), new Position(1.6448038347248299E-15, 0.0, -0.07331451773643494)), new JointIdTransformationPair(BACK_RIGHT_KNEE, new Orientation(0.028353781428618184, -0.03681103388598253, 0.08431954960302344, 0.9953548233759087), new Position(-4.44089183380283E-17, 4.44089183380283E-17, -0.0716027244925499)),
                                                                         new JointIdTransformationPair(TAIL_4, new Orientation(-0.24030073536424199, 0.0, 0.0, 0.970698489018812), new Position(1.3968397039764617E-15, 7.105427198782324E-17, -0.06855837255716324)), new JointIdTransformationPair(FRONT_RIGHT_KNEE, new Orientation(0.1426454013189362, -0.0926403574695996, 0.03145748990223735, 0.9849266368514535), new Position(-1.776356799695581E-17, -3.552713599391162E-17, -0.049763813614845276)), new JointIdTransformationPair(TAIL_3, new Orientation(0.11324441245065447, 0.0, -0.0, 0.9935671608143589), new Position(1.0612414386260453E-15, -5.329070399086743E-17, -0.06198466569185257)),
                                                                         new JointIdTransformationPair(SPINE_UPPER, new Orientation(0.26027709399590043, 0.0, -0.0, 0.9655339633286077), new Position(3.786008972377658E-17, -2.6645351995433716E-17, -0.04811042174696922)), new JointIdTransformationPair(FRONT_LEFT_ANKLE, new Orientation(0.5136190047110643, -0.07637037390983789, 0.1675275230037611, 0.838031988067706), new Position(-2.6645351995433716E-17, 3.552713599391162E-17, -0.06343860179185867)), new JointIdTransformationPair(PELVIS_LOWER_BODY, new Orientation(0.0, 0.590220438441127, 0.8072421161252452, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0)),
                                                                         new JointIdTransformationPair(HEAD, new Orientation(-0.6794482939277444, 0.0, 0.0, 0.7337233919391404), new Position(4.3665815671657614E-17, -2.220445916901415E-17, -0.019550496712327003)), new JointIdTransformationPair(SPINE_BASE, new Orientation(0.6046423656169351, 0.0, -0.0, 0.7964970870638238), new Position(-0.0, 0.0, -0.0)), new JointIdTransformationPair(NECK, new Orientation(0.28666928355737675, 0.0, -0.0, 0.9580296038561128), new Position(3.3058018409053085E-17, 1.4210854397564648E-16, -0.04160454869270325)),
                                                                         new JointIdTransformationPair(LEFT_TOE_4, new Orientation(-0.010176072271624686, -0.3953173708416685, -0.249951617639936, 0.8838239715594575), new Position(0.007488240487873554, -0.002492679050192237, 0.009667515754699707)), new JointIdTransformationPair(FRONT_LEFT_KNEE, new Orientation(0.1814436465837047, 0.09806771614878583, -0.05032052764443915, 0.9772045695049497), new Position(-8.881783998477905E-18, 1.776356799695581E-17, -0.049764443188905716)), new JointIdTransformationPair(BACK_LEFT_ANKLE, new Orientation(-0.11130068127227585, -0.9049400066020236, -0.34056106940596903, 0.2295950800965031), new Position(-2.6645351995433716E-17, -2.220445916901415E-17, -0.033702969551086426)),
                                                                         new JointIdTransformationPair(FRONT_RIGHT_ANKLE, new Orientation(0.5262442652334882, 0.0738985789573053, -0.1399566434593569, 0.8354747819584667), new Position(-2.220445916901415E-17, -6.74889102936227E-11, -0.06343860924243927)), new JointIdTransformationPair(ROOT, new Orientation(-0.6110093257468623, 0.0, 0.0, 0.7916233977405953), new Position(-0.0, 0.08882676064968109, -0.042883217334747314)), new JointIdTransformationPair(FRONT_LEFT_SHOULDER, new Orientation(-0.21743984919648052, -0.39256695324020596, -0.26597456217275645, 0.8531521736959499), new Position(2.220445916901415E-17, 0.0, -0.027711234986782074)),
                                                                         new JointIdTransformationPair(FRONT_RIGHT_SHOULDER, new Orientation(-0.20651848011380763, 0.3959334763909078, 0.26093332828503557, 0.8558624876898082), new Position(-2.6645351995433716E-17, -1.6136424878590572E-12, -0.027711201459169388)), new JointIdTransformationPair(BACK_LEFT_KNEE, new Orientation(0.06233148504091471, 0.03390882022864152, -0.08552568648215231, 0.9938059845035411), new Position(1.776356799695581E-17, 0.0, -0.07160308957099915)), new JointIdTransformationPair(SPINE_MIDDLE, new Orientation(0.085897783291673, 0.0, -0.0, 0.9963039550386101), new Position(-1.0353799057764833E-32, 1.3322675997716858E-17, -0.062499478459358215)));

  public static final JointId[] LEFT_TOE_ARRAY = {LEFT_TOE_0, LEFT_TOE_1, LEFT_TOE_2, LEFT_TOE_3, LEFT_TOE_4};

  public static final JointId[] RIGHT_TOE_ARRAY = {RIGHT_TOE_0, RIGHT_TOE_1, RIGHT_TOE_2, RIGHT_TOE_3, RIGHT_TOE_4};

  public static final JointId[] RIGHT_EAR_ARRAY = {RIGHT_EAR_0};

  public static final JointId[] LEFT_EAR_ARRAY = {LEFT_EAR_0};

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4, TAIL_5, TAIL_6};

  @Override
  public JointId[] getTailArray() {
    return SquirrelResource.TAIL_ARRAY;
  }

  private final ImplementationAndVisualType resourceType;

  SquirrelResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  SquirrelResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  public JointId[] getRootJointIds() {
    return QuadrupedResource.JOINT_ID_ROOTS;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public QuadrupedImp createImplementation(SQuadruped abstraction) {
    return new QuadrupedImp(abstraction, this.resourceType.getFactory(this));
  }
}

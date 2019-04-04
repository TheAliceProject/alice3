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

public enum WolfResource implements QuadrupedResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId JAW_TIP = new JointId(MOUTH, WolfResource.class);

  public static final JointedModelPose WEREWOLF_POSE = new JointedModelPose(new JointIdTransformationPair(TAIL_2, new Orientation(-0.13339085033728823, 0.0, 0.0, 0.9910635100972567), new Position(2.4194579225153686E-15, -7.105427198782324E-17, -0.11782891303300858)), new JointIdTransformationPair(TAIL_1, new Orientation(0.392091971993757, 0.0, -0.0, 0.9199260217528619), new Position(1.290822307441035E-15, 0.0, -0.14247725903987885)), new JointIdTransformationPair(TAIL_0, new Orientation(-0.2556558629550482, 0.0, 0.0, 0.9667678520393144), new Position(-2.0252616002365445E-16, 0.0, -0.23027469217777252)),
                                                                            new JointIdTransformationPair(BACK_RIGHT_ANKLE, new Orientation(0.2504636518182041, 0.0, -0.0, 0.9681260037401586), new Position(1.0125233857526486E-15, -7.105427198782324E-17, -0.1795857548713684)), new JointIdTransformationPair(BACK_RIGHT_KNEE, new Orientation(-0.7759600885554679, 0.0, 0.0, 0.630782007486731), new Position(1.225686214951008E-15, 7.105427198782324E-17, -0.17445999383926392)), new JointIdTransformationPair(FRONT_RIGHT_KNEE, new Orientation(0.7403181170567904, 0.1697723389406113, 0.03153753630063603, 0.6497013331463994), new Position(-5.062616928763243E-16, -7.105427198782324E-17, -0.12609750032424927)),
                                                                            new JointIdTransformationPair(SPINE_UPPER, new Orientation(-0.2430986676506398, 0.0, 0.0, 0.9700015658680576), new Position(1.2878779944465994E-16, -2.1316281596346973E-16, -0.1882767677307129)), new JointIdTransformationPair(FRONT_LEFT_ANKLE, new Orientation(-0.3078003353290578, 0.0, 0.0, 0.9514509727628216), new Position(4.1744385620026768E-16, 1.0658140798173486E-16, -0.2818411886692047)), new JointIdTransformationPair(PELVIS_LOWER_BODY, new Orientation(0.0, 0.9990035695536308, 0.044630348633009846, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0)),
                                                                            new JointIdTransformationPair(HEAD, new Orientation(-0.4454259226057059, 0.0, 0.0, 0.8953187965584414), new Position(1.608119578933075E-16, 1.776356733521132E-16, -0.1040225699543953)), new JointIdTransformationPair(SPINE_BASE, new Orientation(0.4234430699844284, 0.0, -0.0, 0.9059227155128424), new Position(-4.0256073577808124E-32, 7.105427198782324E-17, -0.0)), new JointIdTransformationPair(BACK_RIGHT_HIP, new Orientation(0.0, 0.628113691503349, 0.7781215782549895, 6.123233995736766E-17), new Position(-0.10692200064659119, -0.06929467618465424, -0.16949231922626495)),
                                                                            new JointIdTransformationPair(FRONT_LEFT_KNEE, new Orientation(0.7578632282314943, -0.07145463146300753, -0.11489805300319883, 0.6382287993761312), new Position(-1.1546319032585154E-16, 0.0, -0.1260971575975418)), new JointIdTransformationPair(BACK_LEFT_ANKLE, new Orientation(0.2504608555702036, 0.0, -0.0, 0.9681267271525157), new Position(7.460698492546991E-16, -7.105427198782324E-17, -0.1795855462551117)), new JointIdTransformationPair(FRONT_LEFT_FOOT, new Orientation(-0.31841586863763804, -0.09979989190864469, -0.020354403992288765, 0.9424632695298422), new Position(-1.4743761470560547E-15, 0.0, -0.07129208743572235)),
                                                                            new JointIdTransformationPair(ROOT, new Orientation(0.43601400189489775, 0.0, -0.0, 0.8999398814096395), new Position(-2.788001134423823E-33, 0.6010748147964478, 0.4184955358505249)), new JointIdTransformationPair(BACK_LEFT_HIP, new Orientation(0.0, 0.6281134661284549, 0.7781217601815916, 6.123233995736766E-17), new Position(0.10692249238491058, -0.06929437071084976, -0.16949240863323212)), new JointIdTransformationPair(FRONT_RIGHT_SHOULDER, new Orientation(-0.8636075447427534, -0.06535134972924285, 0.11606758234267699, 0.48625047668982824), new Position(-3.9079850255047273E-16, 0.0, -0.214168518781662)),
                                                                            new JointIdTransformationPair(SPINE_MIDDLE, new Orientation(-0.25643808858768113, 0.0, 0.0, 0.9665606585835659), new Position(8.098971699620997E-17, -7.105427198782324E-17, -0.16711747646331787)), new JointIdTransformationPair(FRONT_RIGHT_FOOT, new Orientation(-0.3184147469489534, -0.09979986339622862, -0.020354520945255656, 0.9424636489905683), new Position(-3.7303492462734957E-16, 3.552713599391162E-17, -0.07129183411598206)), new JointIdTransformationPair(NECK, new Orientation(0.01255011051022937, 0.0, -0.0, 0.9999212442618574), new Position(1.45267447473313E-16, 1.4210854397564648E-16, -0.1298016458749771)),
                                                                            new JointIdTransformationPair(FRONT_RIGHT_CLAVICLE, new Orientation(-0.48766094088350004, -0.05710783625311336, 0.03198947619206204, 0.870575772226671), new Position(0.07562879472970963, 0.06617099046707153, -0.018879499286413193)), new JointIdTransformationPair(BACK_RIGHT_HOCK, new Orientation(0.6611859625634311, 0.0, -0.0, 0.7502220490688534), new Position(2.113864528772015E-15, 0.0, -0.16502009332180023)), new JointIdTransformationPair(FRONT_RIGHT_ANKLE, new Orientation(-0.30780062579958267, 0.0, 0.0, 0.9514508787937427), new Position(-1.1812772304385308E-15, -1.4210854397564648E-16, -0.2818417549133301)),
                                                                            new JointIdTransformationPair(FRONT_LEFT_CLAVICLE, new Orientation(-0.4878039338037217, 0.053115637596909224, -0.029753324864333366, 0.8708276470506695), new Position(-0.0756288394331932, 0.06617041677236557, -0.018879713490605354)), new JointIdTransformationPair(FRONT_LEFT_SHOULDER, new Orientation(-0.8656124985059592, 0.05630855642361474, -0.10000613942902019, 0.4873839564247707), new Position(2.6645351995433716E-17, 7.105427198782324E-17, -0.21416796743869781)), new JointIdTransformationPair(BACK_LEFT_KNEE, new Orientation(-0.7759601989756713, 0.0, 0.0, 0.630781871652663), new Position(8.526512638538789E-16, 7.105427198782324E-17, -0.17446057498455048)),
                                                                            new JointIdTransformationPair(BACK_LEFT_HOCK, new Orientation(0.6611877091801661, 0.0, -0.0, 0.7502205097363601), new Position(8.88178366760566E-17, 7.105427198782324E-17, -0.16502034664154053)));

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2, TAIL_3};

  @Override
  public JointId[] getTailArray() {
    return WolfResource.TAIL_ARRAY;
  }

  private final ImplementationAndVisualType resourceType;

  WolfResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  WolfResource(ImplementationAndVisualType resourceType) {
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

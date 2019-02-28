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

public enum PandaResource implements org.lgna.story.resources.BipedResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, PandaResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_EAR = new org.lgna.story.resources.JointId( HEAD, PandaResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_EAR_TIP = new org.lgna.story.resources.JointId( LEFT_EAR, PandaResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_EAR = new org.lgna.story.resources.JointId( HEAD, PandaResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_EAR_TIP = new org.lgna.story.resources.JointId( RIGHT_EAR, PandaResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, PandaResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, PandaResource.class );

	public static final org.lgna.story.JointedModelPose STANDING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.0865510122859929, -0.18635759833238288, 0.11986472916776969, 0.9712940927018929), new Position(-0.0, 0.0, -0.18063582479953766) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.27047420802523603, 0.0, -0.0, 0.9627272213836697), new Position(-0.0, 0.0, -0.08641287684440613) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.07137178820650658, 0.0, 0.0, 0.9974497821184811), new Position(-0.0, 0.0, -0.09508147835731506) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.16739597813101365, 0.10841374028894267, -0.01852338734607344, 0.9797356437036219), new Position(-0.06755576282739639, -0.014510521665215492, -0.1889897733926773) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.3114579226188188, -0.0035540227798313987, -3.0786850492372337E-4, 0.9502532486537649), new Position(-0.0, 0.0, -0.1624060720205307) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.594695386492334, -0.287224902041369, -0.21368319266572894, 0.719846335063422), new Position(-0.0, 0.0, -0.13394689559936523) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.6079058965114496, 0.11428947831084646, 0.30475876377770417, 0.7242309245225109), new Position(-0.0, 0.0, -0.13394764065742493) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.7269740951297143, 0.6866648855230134, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.07137178820650658, 0.0, 0.0, 0.9974497821184811), new Position(-0.0, 0.0, -0.09508173912763596) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6711798506289727, -0.003096588116597279, 0.00154072790576473, 0.741286479985455), new Position(-0.0, 0.0, -0.11246897280216217) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6430084967118578, 0.0, -0.0, 0.7658590426157784), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(0.24566456111119342, -0.020095009010995736, 9.266085186722037E-4, 0.9691461476080612), new Position(-0.0, 0.0, -0.10906551033258438) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.0, 0.09468140459742419, 0.9955076250955885, 6.123233995736766E-17), new Position(-0.11605700105428696, -0.0014958195388317108, -0.15440964698791504) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.2704737121902555, 0.0, -0.0, 0.962727360686307), new Position(-0.0, -4.811174236785121E-10, -0.08641283959150314) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(-0.16928693876396156, -0.018106876130032454, -0.11759403881065975, 0.9783586844490805), new Position(-0.0, 0.0, -0.18063542246818542) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.0052129759754198065, -0.005388303262787353, 0.029221216424751673, 0.9995448522102877), new Position(-0.0, 0.0, -0.18908032774925232) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.10925970553288299, -0.05292894091265886, 0.005826197325846374, 0.9925859657407273), new Position(0.06755580008029938, -0.014510838314890862, -0.1889895498752594) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.37350696325302124, 0.07471926510334015) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(-0.00854075104948011, -0.03451220075871315, 0.006521287150558117, 0.9993465046640234), new Position(-0.0, 0.0, -0.1890811026096344) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.04418470500416152, 0.8437683787256576, -0.4439188635243732, 0.2983934944223808), new Position(-0.11903329193592072, -0.10827945172786713, -0.05193784087896347) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.0, 0.07328530631805634, 0.9973110166231338, 6.123233995736766E-17), new Position(0.1160569116473198, -0.0014959070831537247, -0.15440990030765533) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(0.02835064703919377, -0.8462609722780645, 0.35598086943053264, 0.39536847144268605), new Position(0.11903299391269684, -0.10827941447496414, -0.05193750932812691) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.26014160252482016, 0.0, -0.0, 0.9655704773012783), new Position(-0.0, 0.0, -0.14726798236370087) )
	);


	public static final org.lgna.story.JointedModelPose SLEEPING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.15962858143801892, -0.01043902550231364, -0.10918143074503309, 0.9810653178637574), new Position(-0.0, 0.0, -0.18063582479953766) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(-0.1777834143423886, 0.0, 0.0, 0.9840696406173511), new Position(-0.0, 0.0, -0.08641287684440613) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.10604842786246452, 0.12434150378286112, -0.023893152957396715, 0.9862667177932503), new Position(-0.06755576282739639, -0.014510521665215492, -0.1889897733926773) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.13456910773680592, 0.0, 0.0, 0.9909042109320759), new Position(-0.0, 0.0, -0.09508147835731506) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.12633823257615853, 0.0, -0.0, 0.9919872231987328), new Position(-0.0, 0.0, -0.1624060720205307) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(0.09896577309964825, -0.5601838584875865, 0.6269666463499187, 0.5322712135833428), new Position(-0.0, 0.0, -0.13394689559936523) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(0.07861374577724627, 0.5867038523298925, -0.5523248818493207, 0.586971629234555), new Position(-0.0, 0.0, -0.13394764065742493) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.6701188367902828, 0.7422538275946028, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(0.12584128990449547, 0.0, -0.0, 0.9920503867017908), new Position(-0.0, 0.0, -0.11246897280216217) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.12500750995964371, 0.0, 0.0, 0.9921557954543679), new Position(-0.0, 0.0, -0.09508173912763596) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.4744959880249238, 0.0, -0.0, 0.8802576653163842), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(0.07667920307272635, 0.0, -0.0, 0.9970558157977575), new Position(-0.0, 0.0, -0.10906551033258438) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(-0.1440781372586601, 0.0, 0.0, 0.9895663142832191), new Position(-0.0, -4.811174236785121E-10, -0.08641283959150314) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.1940125578841315, 0.1625996155308716, -0.9668795004331936, 0.03262704483288801), new Position(-0.11605700105428696, -0.0014958195388317108, -0.15440964698791504) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.0199067995082344, 0.005281986273999725, 0.13956143311219524, 0.9899992052229186), new Position(-0.0, 0.0, -0.18063542246818542) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.12585207092720757, 0.21444931757779626, -0.1375578741676877, 0.9587755616869973), new Position(-0.0, 0.0, -0.18908032774925232) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.1127245889508353, -0.0892810878145417, 0.0010888318341142995, 0.989606421184581), new Position(0.06755580008029938, -0.014510838314890862, -0.1889895498752594) ),
		new JointIdTransformationPair( ROOT, new Orientation(-0.46902861262440365, 0.0, 0.0, 0.8831829711558229), new Position(-0.0, 0.19594046473503113, -0.04771088808774948) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(-0.048616261068569186, -0.19420726855916703, 0.05661068649203457, 0.9781182066465182), new Position(-0.0, 0.0, -0.1890811026096344) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.20651430076344113, 0.6315610914916925, -0.6120474885488549, 0.4288126666215074), new Position(-0.11903329193592072, -0.10827945172786713, -0.05193784087896347) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.22830824250954537, -0.13806914961394215, 0.9631932337615102, 0.032726911909469705), new Position(0.1160569116473198, -0.0014959070831537247, -0.15440990030765533) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.18420956188322307, 0.0, 0.0, 0.9828869911189134), new Position(-0.0, 0.0, -0.14726798236370087) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.20651549073365744, -0.6315590756475224, 0.6120485204418415, 0.42881358966249705), new Position(0.11903299391269684, -0.10827941447496414, -0.05193750932812691) )
	);


	public static final org.lgna.story.JointedModelPose CRAWLING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.4051785303209904, 0.056840255955348536, -0.01568000458993149, 0.9123341938817773), new Position(-0.0, 0.0, -0.18063582479953766) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.5691843239915646, 0.0, -0.0, 0.8222099520939075), new Position(-0.0, 0.0, -0.08641287684440613) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(-0.05191408395892141, 0.16959984015626342, -0.04207997733294665, 0.9832446784058049), new Position(-0.06755576282739639, -0.014510521665215492, -0.1889897733926773) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.6200619770613454, 0.0, 0.0, 0.7845528309825768), new Position(-0.0, 0.0, -0.09508147835731506) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.10752212008513379, 0.0, 0.0, 0.9942026924588356), new Position(-0.0, 0.0, -0.1624060720205307) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.26212520228674546, -0.8872154597161914, 0.154390219266675, 0.3468468921029971), new Position(-0.0, 0.0, -0.13394689559936523) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.2993863191226192, 0.8814614934318958, -0.12716504940515616, 0.3423777412870258), new Position(-0.0, 0.0, -0.13394764065742493) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.3476985948470047, 0.9376063604420666, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(0.0856571827572928, 0.0, -0.0, 0.9963246694938772), new Position(-0.0, 0.0, -0.11246897280216217) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.6215098504696022, 0.0, 0.0, 0.7834063477973948), new Position(-0.0, 0.0, -0.09508173912763596) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.845762722038659, 0.0, -0.0, 0.5335591982243001), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.040630897680755146, 0.0, 0.0, 0.9991742241239292), new Position(-0.0, 0.0, -0.10906551033258438) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.5749710630308871, 0.0, -0.0, 0.8181737447982131), new Position(-0.0, -4.811174236785121E-10, -0.08641283959150314) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.0, 0.5902443214048018, 0.807224653395438, 6.123233995736766E-17), new Position(-0.11605700105428696, -0.0014958195388317108, -0.15440964698791504) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.3943926035711405, -0.05701646355163502, 0.015007598597348999, 0.9170487277766559), new Position(-0.0, 0.0, -0.18063542246818542) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.14948264419898896, 0.18487113151463117, 0.19366874227259698, 0.9518245752670651), new Position(-0.0, 0.0, -0.18908032774925232) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(-0.043322246552376514, -0.136340952326682, 5.029991974342385E-4, 0.989714137851994), new Position(0.06755580008029938, -0.014510838314890862, -0.1889895498752594) ),
		new JointIdTransformationPair( ROOT, new Orientation(-0.7049663382766852, 0.0, 0.0, 0.7092407644070963), new Position(-0.0, 0.3084537982940674, -0.04771088808774948) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.11882941041090883, -0.17117258589024512, -0.13643249602426952, 0.9684862885398172), new Position(-0.0, 0.0, -0.1890811026096344) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.1165356603553463, 0.6865658023529533, -0.6353500938100087, 0.3337320739764193), new Position(-0.11903329193592072, -0.10827945172786713, -0.05193784087896347) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.0, 0.5903599103808017, 0.807140121797431, 6.123233995736766E-17), new Position(0.1160569116473198, -0.0014959070831537247, -0.15440990030765533) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.15800076953483247, 0.0, 0.0, 0.9874389889134421), new Position(-0.0, 0.0, -0.14726798236370087) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.1155682867901352, -0.6870717468350122, 0.6355281358622976, 0.33268660074741685), new Position(0.11903299391269684, -0.10827941447496414, -0.05193750932812691) )
	);


	private final ImplementationAndVisualType resourceType;
	private PandaResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private PandaResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BipedImp createImplementation( org.lgna.story.SBiped abstraction ) {
		return new org.lgna.story.implementation.BipedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}

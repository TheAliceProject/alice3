/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.apis.stage;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public class Adult extends Person {
	//todo: reduce visibility?
	public enum State implements FiniteStateMachine.State {
		PREPARED_TO_EXERCISE,
		PREPARED_TO_PERFORM_SIT_UP,
		PREPARED_TO_PERFORM_SCISSOR_SIT_UP,
		PREPARED_TO_PERFORM_PUSH_UP,
		PREPARED_TO_PERFORM_ONE_HANDED_PUSH_UP,
		PREPARED_TO_PERFORM_LEG_LIFT,
		PREPARED_TO_PERFORM_LEG_SIDE_KICK,
		PREPARED_TO_WORK_OUT,
		PREPARED_TO_PERFORM_SIDE_STRETCH,
		PREPARED_TO_PERFORM_JUMPING_JACK,
		PREPARED_TO_JOG_IN_PLACE,
		PREPARED_TO_PERFORM_KNEE_BEND,
		PREPARED_TO_WORRY,
		PREPARED_TO_CALL_OUT_TO_EVERYONE,
		PREPARED_TO_SWAY_WITH_HANDS_HELD_UP_HIGH,
		PREPARED_TO_SWAY_WITH_HANDS_HELD_DOWN_LOW,
		POSED_A,
		POSED_B,
		PREPARED_TO_CLAP_ENTHUSIASTICALLY,
		PREPARED_TO_CLAP_BECAUSE_YOU_ARE_EXPECTED_TO_CLAP,
		PREPARED_TO_CLAP_QUIETLY,
		PREPARED_TO_COUGH,
		PREPARED_TO_CLEAN_HIGH_CHAIR,
		SLOUCHED_WEIGHT_CENTERED,
		SLOUCHED_WEIGHT_ON_RIGHT_FOOT,
		PREPARED_TO_INTIMIDATE,
		PREPARED_TO_PLAY_HANDS_TALKING,
		PREPARED_TO_MIME,
		LOOKING_AT_FLOOR,
		HANDS_ON_HIPS,
		HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT,
		HANDS_AT_SIDES,
		HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT,
		ARMS_CROSSED,
		ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT,
		DEPRESSED,
		ASLEEP_STANDING_UP,
		ASLEEP_ON_FLOOR,
		PREPARED_TO_GUSSY_UP,
		PREPARED_TO_PREEN,
		PREPARED_TO_PANHANDLE,
		PREPARED_TO_WATCH_CLOUDS,
		PREPARED_TO_WATCH_CLOUDS_LYING_DOWN,
		GOING_INTO_LABOR,
		PREPARED_TO_BOO,
		PREPARED_TO_CHEER,
		PREPARED_TO_CRY,
		PREPARED_TO_EXPRESS_EMBARRASSMENT,
		PREPARED_TO_LAUGH,
		PREPARED_TO_LAUGH_BIG,
		HANDS_COVERING_EARS,
		PREPARED_TO_PINE,
		DEVASTATED,
		NOSE_COVERED,
		PREPARED_TO_HAVE_INTENSE_TANTRUM,
		PREPARED_TO_CRINGE,
		SITTING_ON_FLOOR,
		GREETING_WITH_OPEN_ARMS,
		PREPARED_TO_DANCE_A,
		PREPARED_TO_DANCE_B,
		PREPARED_TO_DANCE_C,
		CROUCHED_IN_FRONT_OF_DISHWASHER,
		PREPARED_TO_GIVE_GREETING,
		PREPARED_TO_RECEIVE_GREETING,
		PREPARED_TO_GIVE_BACK_RUB,
		PREPARED_TO_RECEIVE_BACK_RUB,
		PREPARED_TO_GIVE_HEAVY_PUNCHING_BAG_HIT,
		PREPARED_TO_RECEIVE_HEAVY_PUNCHING_BAG_HIT,
		;
	}
	//todo: reduce visibility?
	public enum StateAB implements FiniteStateMachine.StateAB {
		PREPARED_TO_INTERACT_WITH_COFFEE_MAKER( State.HANDS_AT_SIDES, CoffeeMaker.State.AT_REST ),
		PREPARED_TO_REPAIR_DISHWASHER( State.CROUCHED_IN_FRONT_OF_DISHWASHER, Dishwasher.State.PREPARED_TO_BE_REPAIRED ),
		PREPARED_TO_INTERACT_WITH_DISHWASHER( State.HANDS_AT_SIDES, Dishwasher.State.AT_REST ),
		PREPARED_TO_INTERACT_WITH_ADULT( State.HANDS_AT_SIDES, State.HANDS_AT_SIDES ),
		PREPARED_TO_GREET_ADULT( State.PREPARED_TO_GIVE_GREETING, State.PREPARED_TO_RECEIVE_GREETING ),
		PREPARED_TO_RUB_BACK_OF_ADULT( State.PREPARED_TO_GIVE_BACK_RUB, State.PREPARED_TO_RECEIVE_BACK_RUB ),
		;
		private FiniteStateMachine.State m_a;
		private FiniteStateMachine.State m_b;
		StateAB( FiniteStateMachine.State a, FiniteStateMachine.State b ) {
			m_a = a;
			m_b = b;
		}
		public FiniteStateMachine.State getA() {
			return m_a;
		}
		public FiniteStateMachine.State getB() {
			return m_b;
		}
	}
	//todo: reduce visibility?
	public enum StateABC implements FiniteStateMachine.StateABC {
		PREPARED_TO_INTERACT_WITH_HEAVY_PUNCHING_BAG( State.HANDS_AT_SIDES, HeavyPunchingBag.State.AT_REST, State.HANDS_AT_SIDES ),
		PREPARED_TO_HIT_HEAVY_PUNCHING_BAG( State.PREPARED_TO_GIVE_HEAVY_PUNCHING_BAG_HIT, HeavyPunchingBag.State.PREPARED_TO_BE_HIT, State.PREPARED_TO_RECEIVE_HEAVY_PUNCHING_BAG_HIT ),
		;
		private FiniteStateMachine.State m_a;
		private FiniteStateMachine.State m_b;
		private FiniteStateMachine.State m_c;
		StateABC( FiniteStateMachine.State a, FiniteStateMachine.State b, FiniteStateMachine.State c ) {
			m_a = a;
			m_b = b;
			m_c = c;
		}
		public FiniteStateMachine.State getA() {
			return m_a;
		}
		public FiniteStateMachine.State getB() {
			return m_b;
		}
		public FiniteStateMachine.State getC() {
			return m_c;
		}
	}
	//todo: reduce visibility?
	public enum Cycle implements FiniteStateMachine.Cycle {
		NORMAL_SIT_UP( State.PREPARED_TO_PERFORM_SIT_UP ),
		DIFFICULT_SIT_UP( State.PREPARED_TO_PERFORM_SIT_UP ),
		SCISSOR_SIT_UP( State.PREPARED_TO_PERFORM_SCISSOR_SIT_UP ),
		PUSH_UP_ON_KNEES( State.PREPARED_TO_PERFORM_PUSH_UP ),
		PUSH_UP_ON_TOES( State.PREPARED_TO_PERFORM_PUSH_UP ),
		PUSH_UP_ON_TOES_WITH_CLAP( State.PREPARED_TO_PERFORM_PUSH_UP ),
		ONE_HANDED_PUSH_UP_ON_TOES( State.PREPARED_TO_PERFORM_ONE_HANDED_PUSH_UP ),
		NORMAL_LEG_LIFT( State.PREPARED_TO_PERFORM_LEG_LIFT ),
		DIFFICULT_LEG_LIFT( State.PREPARED_TO_PERFORM_LEG_LIFT ),
		LEG_SIDE_KICK( State.PREPARED_TO_PERFORM_LEG_SIDE_KICK ),
		SIDE_STRETCH( State.PREPARED_TO_PERFORM_SIDE_STRETCH ),
		TOUCH_TOES( State.PREPARED_TO_WORK_OUT ),
		JUMPING_JACK( State.PREPARED_TO_PERFORM_JUMPING_JACK ),
		JOG_IN_PLACE( State.PREPARED_TO_JOG_IN_PLACE ),
		KNEE_BEND( State.PREPARED_TO_PERFORM_KNEE_BEND ),
		WRING_HANDS_WORRIEDLY( State.PREPARED_TO_WORRY ),
		LOOK_AROUND_WORRIEDLY( State.PREPARED_TO_WORRY ),
		SHAKE_HEAD_WORRIEDLY( State.PREPARED_TO_WORRY ),
		CALL_OUT_TO_EVERYONE( State.PREPARED_TO_CALL_OUT_TO_EVERYONE ),
		CALL_EVERYONE_OVER( State.HANDS_AT_SIDES ),
		SWAY_WITH_HANDS_HELD_UP_HIGH( State.PREPARED_TO_SWAY_WITH_HANDS_HELD_UP_HIGH ),
		SWAY_WITH_HANDS_HELD_DOWN_LOW( State.PREPARED_TO_SWAY_WITH_HANDS_HELD_DOWN_LOW ),
		BLOW_HORN( State.HANDS_AT_SIDES ),
		BROWSE( State.HANDS_AT_SIDES ),
		BLOW_WHISTLE( State.HANDS_AT_SIDES ),
		CHECK_CHEST( State.HANDS_AT_SIDES ),
		CHECK_RIGHT_TOE( State.HANDS_AT_SIDES ),
		CHECK_RIGHT_ARM( State.HANDS_AT_SIDES ),
		CHECK_BELLY( State.HANDS_AT_SIDES ),
		CLAP_ENTHUSIASTICALLY( State.PREPARED_TO_CLAP_ENTHUSIASTICALLY ),
		CLAP_BECAUSE_YOU_ARE_EXPECTED_TO_CLAP( State.PREPARED_TO_CLAP_BECAUSE_YOU_ARE_EXPECTED_TO_CLAP ),
		CLAP_QUIETLY( State.PREPARED_TO_CLAP_QUIETLY ),
		SPIN_AROUND( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_SIDE_STEP( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_EXAGERATED_ARM_LIFT( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_BODY_TWIST( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_STEP_BACK_AND_HANDS_ON_HIPS( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_RIGHT_LEG_FORWARD( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_RIGHT_LEG_LIFT( State.HANDS_AT_SIDES ),
		WIGGLE( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_BOTH_LEGS( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_SLIGHT_ARM_LIFT( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_WITH_BEND_FORWARD( State.HANDS_AT_SIDES ),
		CHECK_CLOTHES_RIGHT_ARM( State.HANDS_AT_SIDES ),
		COUGH_SHORT( State.HANDS_AT_SIDES ),
		COUGH_EXTENDED( State.HANDS_AT_SIDES ),
		GET_ATTENTION( State.HANDS_AT_SIDES ),
		GET_ATTENTION_INSISTENTLY( State.HANDS_AT_SIDES ),
		FIDGET_PAT_LEGS( State.HANDS_AT_SIDES ),
		FIDGET_PAT_LEGS_WHILE_LOOKING_AROUND( State.HANDS_AT_SIDES ),
		FIDGET_BOUNDY_LEGS( State.HANDS_AT_SIDES ),
		FIDGET_PUNCH_AND_CLAP_HANDS( State.HANDS_AT_SIDES ),
		TILT_HEAD( State.HANDS_AT_SIDES ),
		TILT_HEAD_DOWN_BACK_AND_FORTH( State.HANDS_AT_SIDES ),
		TILT_HEAD_DOWN_BACK_AND_FORTH_SLOWLY( State.HANDS_AT_SIDES ),
		TILT_HEAD_WITH_A_BIT_OF_SURPRISE( State.HANDS_AT_SIDES ),
		NOD( State.HANDS_AT_SIDES ),
		DOUBLE_TAKE( State.HANDS_AT_SIDES ),
		CLEAN_HIGH_CHAIR_MACRO( State.HANDS_AT_SIDES ),
		CLEAN_HIGH_CHAIR_MICRO( State.HANDS_AT_SIDES ),
		MUTTER( State.HANDS_AT_SIDES ),
		PLOT_SINISTER( State.HANDS_AT_SIDES ),
		RUB_HANDS_SINISTER( State.HANDS_AT_SIDES ),
		PANT_INTENSLY( State.HANDS_AT_SIDES ),
		//todo: paranoically
		LOOK_AROUND_PARANOICLY( State.HANDS_AT_SIDES ),
		RUB_KNEES( State.HANDS_AT_SIDES ),
		SLOUCHED_SIGH( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
		SLOUCHED_SWAY( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
		SLOUCHED_SWAY_WITH_HEAD_BACK( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
		SLOUCHED_BREATHE_WITH_HEAD_BACK( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
		BELCH( State.HANDS_AT_SIDES ),
		FART_AND_WAVE_IN_APPROVAL( State.HANDS_AT_SIDES ),
		//todo: loogie
		HOCK_LOOGEY( State.HANDS_AT_SIDES ),
		//todo: phlegm
		SNIFF_PHELGM( State.HANDS_AT_SIDES ),
		PICK_REAR_END( State.HANDS_AT_SIDES ),
		RING_FINGERS( State.PREPARED_TO_INTIMIDATE ),
		PUNCH_HAND( State.PREPARED_TO_INTIMIDATE ),
		CRACK_KNUCKLES_TO_INTIMIDATE( State.PREPARED_TO_INTIMIDATE ),
		BIDE_TIME( State.PREPARED_TO_INTIMIDATE ),
		BRUSH_SHOULDER( State.HANDS_AT_SIDES ),
		BRUSH_PANTS( State.HANDS_AT_SIDES ),
		SWAY( State.HANDS_AT_SIDES ),
		LITTLE_WAVE( State.HANDS_AT_SIDES ),
		SWOON( State.HANDS_AT_SIDES ),
		HOPEFUL_SIGH( State.HANDS_AT_SIDES ),
		NOD_WITH_MIND_SOMEWHERE_ELSE( State.HANDS_AT_SIDES ),
		OUTGOING_CIRCLE_FISTS( State.HANDS_AT_SIDES ),
		CLAP_HEY_THERE( State.HANDS_AT_SIDES ),
		POINT_BOTH_HANDS( State.HANDS_AT_SIDES ),
		POINT_BOTH_HANDS_WITH_A_FLOURISH( State.HANDS_AT_SIDES ),
		GIVE_THUMBS_UP( State.HANDS_AT_SIDES ),
		FLIRTATIOUS_POSE_AND_WAVE( State.HANDS_AT_SIDES ),
		RIGHT_HAND_TALK_TO_FACE( State.PREPARED_TO_PLAY_HANDS_TALKING ),
		LEFT_HAND_TALK_TO_FACE( State.HANDS_AT_SIDES ),
		RIGHT_HAND_AND_LEFT_HAND_TALK( State.HANDS_AT_SIDES ),
		RIGHT_HAND_ATTACK_LEFT_HAND( State.HANDS_AT_SIDES ),
		MIME_UP_AND_DOWN( State.PREPARED_TO_MIME ),
		MIME_SIDE_TO_SIDE( State.PREPARED_TO_MIME ),
		MIME_SIDE_TO_SIDE_EXAGERATEDLY( State.PREPARED_TO_MIME ),
		ROCK_BACK_AND_FORTH( State.HANDS_AT_SIDES ),
		SPAR_IN_PLACE( State.HANDS_AT_SIDES ),
		SHUFFLE_FEET_HEAL_TO_TOE( State.HANDS_AT_SIDES ),
		BITE_THUMB( State.LOOKING_AT_FLOOR ),
		FIDGET_FINGERS( State.LOOKING_AT_FLOOR ),
		SHIFT_LOOK( State.LOOKING_AT_FLOOR ),
		SHIFT_WEIGHT( State.LOOKING_AT_FLOOR ),
		WRING_HANDS( State.LOOKING_AT_FLOOR ),
		SLOUCHED_STRETCH( State.LOOKING_AT_FLOOR ),
		ACT_COY( State.LOOKING_AT_FLOOR ),
		FLICK_LINT( State.LOOKING_AT_FLOOR ),
		RUN_HAND_THROUGH_HAIR( State.LOOKING_AT_FLOOR ),
		SCRATCH_ARM_PIT( State.HANDS_AT_SIDES ),
		RUB_UNDERNEATH_NOSE( State.HANDS_AT_SIDES ),
		SCRATCH_HEAD( State.HANDS_AT_SIDES ),
		WIPE_MOUTH( State.HANDS_AT_SIDES ),
		SCRATCH_HEAD_FROM_ARMS_ON_HIPS_POSE( State.HANDS_ON_HIPS ),
		RUB_UNDERNEATH_NOSE_FROM_ARMS_ON_HIPS_WEIGHT_SHIFTED_RIGHT_POSE( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		CHECK_NAILS_FROM_ARMS_ON_HIPS_WEIGHT_SHIFTED_RIGHT_POSE( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		SCUFF_LEFT_FOOT( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		BRUSH_THIGHS( State.HANDS_AT_SIDES ),
		SCRATCH_FOREARM( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		LOOSEN_UP_FINGERS( State.HANDS_AT_SIDES ),
		ROCK_BACK_AND_FORTH_WITH_ARMS_CROSSED( State.ARMS_CROSSED ),
		TAP_LIP_FOR_INSPIRATION( State.HANDS_AT_SIDES ),
		STRETCH_SHOULDERS( State.HANDS_AT_SIDES ),
		CRACK_KNUCKLES_TO_LOOSEN_THEM( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		RUB_NECK( State.HANDS_ON_HIPS ),
		BRUSH_HAIR_FROM_FACE( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		TWIST_TORSO( State.ARMS_CROSSED ),
		SHIFT_WEIGHT_BACK_AND_FORTH( State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT ),
		RUB_CHIN( State.HANDS_AT_SIDES ),
		REALIZE_SOMETHING( State.HANDS_AT_SIDES ),
		TAP_TEMPLE( State.HANDS_AT_SIDES ),
		PRIMAL_SCREAM( State.HANDS_AT_SIDES ),
		DEEP_SIGH( State.DEPRESSED ),
		SHAKE_HEAD( State.DEPRESSED ),
		BEMOAN_WHY_ME( State.DEPRESSED ),
		KICK_IN_FRUSTRATION( State.DEPRESSED ),
		CAVORT( State.HANDS_AT_SIDES ),
		PRACTICE_FRIENDLY_HUG( State.HANDS_AT_SIDES ),
		PRACTICE_ROMANTIC_HUG( State.HANDS_AT_SIDES ),
		PRACTICE_FRIENDLY_KISS( State.HANDS_AT_SIDES ),
		PRACTICE_ROMANTIC_KISS( State.HANDS_AT_SIDES ),
		DEMONSTRATE_A_NEED_TO_URINATE( State.HANDS_AT_SIDES ),
		FEEL_BACK( State.HANDS_AT_SIDES ),
		FEEL_NECK( State.HANDS_AT_SIDES ),
		CATCH_ONESELF_ALMOST_FALLING_ASLEEP( State.HANDS_AT_SIDES ),
		DEMONSTRATE_HUNGER( State.HANDS_AT_SIDES ),
		DEMONSTRATE_THIRST( State.HANDS_AT_SIDES ),
		DISCOVER_ARM_PIT_REEKS_SUBTLY( State.HANDS_AT_SIDES ),
		DISCOVER_ARM_PIT_REEKS( State.HANDS_AT_SIDES ),
		DEMOSTRATE_MENTAL_ANGUISH( State.HANDS_AT_SIDES ),
		WET_PANTS( State.HANDS_AT_SIDES ),
		DEMOSTRATE_NAUSEA( State.HANDS_AT_SIDES ),
		EXAMINE_HAIR_IN_MIRROR( State.PREPARED_TO_GUSSY_UP ),
		SMELL_BREATH( State.PREPARED_TO_GUSSY_UP ),
		EXAMINE_FACE_IN_MIRROR( State.PREPARED_TO_GUSSY_UP ),
		BRUSH_SHOULDER_FROM_PREEN( State.PREPARED_TO_PREEN ),
		BRUSH_PANTS_FROM_PREEN( State.PREPARED_TO_PREEN ),
		EXAMINE_SELF( State.PREPARED_TO_PREEN ),
		PICK_AT_LINT_ON_PANTS( State.PREPARED_TO_PREEN ),
		CLAP_AND_POINT_WITH_BOTH_HANDS( State.HANDS_AT_SIDES ),
		DISPLAY_SIGN( State.PREPARED_TO_PANHANDLE ),
		RATTLE_CAN_FOR_PASSERSBY( State.PREPARED_TO_PANHANDLE ),
		RATTLE_CAN( State.PREPARED_TO_PANHANDLE ),
		CHECK_CAN( State.PREPARED_TO_PANHANDLE ),
		WAVE_HANDS_MADLY( State.HANDS_AT_SIDES ),
		LOOK_AROUND_WHILE_SEATED( State.PREPARED_TO_WATCH_CLOUDS ),
		WIGGLE_KNEES( State.PREPARED_TO_WATCH_CLOUDS_LYING_DOWN ),
		LOOK_AROUND_WHILE_LYING_DOWN( State.PREPARED_TO_WATCH_CLOUDS_LYING_DOWN ),
		RUB_PREGNANT_BELLY( State.HANDS_AT_SIDES ),
		HOWL_WITH_LABOR_PAINS( State.GOING_INTO_LABOR ),
		YELL_WITH_LABOR_PAINS( State.GOING_INTO_LABOR ),
		APPLAUD( State.HANDS_AT_SIDES ),
		EXPRESS_NO_WAY_IN_DISGUST( State.HANDS_AT_SIDES ),
		SIGH_IN_DISGUST( State.HANDS_AT_SIDES ),
		GREET_WITH_A_FLOURISH( State.HANDS_AT_SIDES ),
		FAN_SELF( State.HANDS_AT_SIDES ),
		EXAGGERATED_SWOON( State.HANDS_AT_SIDES ),
		CAT_CALL( State.HANDS_AT_SIDES ),
		BOO( State.HANDS_AT_SIDES ),
		BOO_LOUDLY( State.HANDS_AT_SIDES ),
		CLAP_WITH_FIST_PUMP( State.HANDS_AT_SIDES ),
		CHEER( State.PREPARED_TO_CHEER ),
		CLEAR_THROAT( State.HANDS_AT_SIDES ),
		WAIL( State.PREPARED_TO_CRY ),
		SOB( State.PREPARED_TO_CRY ),
		REFUSE_IN_A_KEEP_IT_AWAY_FROM_ME_POSTURE( State.HANDS_AT_SIDES ),
		RETCH( State.HANDS_AT_SIDES ),
		EXPRESS_EMBARRASSMENT( State.PREPARED_TO_EXPRESS_EMBARRASSMENT ),
		EXPRESS_MORTIFIED_EMBARRASSMENT( State.PREPARED_TO_EXPRESS_EMBARRASSMENT ),
		CRAZY_SIGN( State.HANDS_AT_SIDES ),
		GIGGLE( State.PREPARED_TO_LAUGH ),
		CHUCKLE( State.PREPARED_TO_LAUGH ),
		SNICKER( State.PREPARED_TO_LAUGH ),
		LAUGH_WITH_HANDS_ON_BELLY( State.PREPARED_TO_LAUGH_BIG ),
		LAUGH_WITH_HANDS_ON_FACE( State.PREPARED_TO_LAUGH_BIG ),
		LAUGH_WITH_CLAP( State.PREPARED_TO_LAUGH_BIG ),
		COVER_EARS_TO_SHIELD_LOUD_NOISE( State.HANDS_COVERING_EARS ),
		SHRUG_OH_WELL( State.HANDS_AT_SIDES ),
		EXAGGERATED_PINING_SIGH( State.PREPARED_TO_PINE ),
		CHECK_FOR_RAIN( State.HANDS_AT_SIDES ),
		SIGH_DEVASTATEDLY( State.DEVASTATED ),
		SIGH_DEVASTATEDLY_WITH_NOSE_WIPE( State.DEVASTATED ),
		RECOIL_IN_SHOCK( State.HANDS_AT_SIDES ),
		SHOO( State.HANDS_AT_SIDES ),
		EXAGGERATED_SHOO( State.HANDS_AT_SIDES ),
		SHRUG_I_DONT_KNOW( State.HANDS_AT_SIDES ),
		SIGH( State.HANDS_AT_SIDES ),
		WAVE_AWAY_FOUL_SCENT( State.NOSE_COVERED ),
		TEST_SNIFF_OF_FOUL_SCENT( State.NOSE_COVERED ),
		WAFT_AND_SNIFF_AROMA( State.HANDS_AT_SIDES ),
		SNIFF_AROMA_IN_AIR( State.HANDS_AT_SIDES ),
		EXPRESS_STARTLE( State.HANDS_AT_SIDES ),
		JUMP_IN_EXTREME_SURPRISE( State.HANDS_AT_SIDES ),
		STAMP_FOOT( State.PREPARED_TO_HAVE_INTENSE_TANTRUM ),
		EXPRESS_WHY_ME( State.PREPARED_TO_HAVE_INTENSE_TANTRUM ),
		THROW_INTENSE_TANTRUM( State.PREPARED_TO_HAVE_INTENSE_TANTRUM ),
		COVER_MOUTH_IN_SHOCK( State.PREPARED_TO_CRINGE ),
		COVER_EYES_IN_AVERSION( State.PREPARED_TO_CRINGE ),
		CLENCH_HANDS_IN_WORRY( State.PREPARED_TO_CRINGE ),
		WAVE( State.HANDS_AT_SIDES ),
		POINT_SCOLDINGLY( State.HANDS_AT_SIDES ),
		REJECT_FLATLY( State.HANDS_AT_SIDES ),
		REJECT_MENTALLY_SPENT( State.HANDS_AT_SIDES ),
		REJECT_MEEKLY( State.HANDS_AT_SIDES ),
		LISTEN_IN_AGREEMENT( State.HANDS_AT_SIDES ),
		LISTEN_QUESTIONINGLY( State.HANDS_AT_SIDES ),
		LISTEN_WITH_HANDS_TOGETHER( State.HANDS_AT_SIDES ),
		DEBATE_POINT_WITH_FINGER_WAG( State.HANDS_AT_SIDES ),
		DEBATE_POINT_WITH_HAND_CHOPS( State.HANDS_AT_SIDES ),
		DEBATE_POINT_WITH_BOTH_SIDES_OF_THE_ISSUE( State.HANDS_AT_SIDES ),
		CONTEMPLATE_AND_RESPOND( State.HANDS_AT_SIDES ),
		BELCH_THUNDEROUSLY( State.HANDS_AT_SIDES ),
		FART( State.HANDS_AT_SIDES ),
		SMELL_UNDERARMS_APPROVINGLY( State.HANDS_AT_SIDES ),
		SMELL_UNDERARMS_WITHOUT_REACHING_CONCLUSION( State.HANDS_AT_SIDES ),
		SNEEZE( State.HANDS_AT_SIDES ),
		CALL_OUT( State.HANDS_AT_SIDES ),
		INVITE_TO_ACCEPT_HUG( State.GREETING_WITH_OPEN_ARMS ),
		CALL_OVER_WITH_WAVE( State.HANDS_AT_SIDES ),
		CALL_OVER_WITH_WHISTLE( State.HANDS_AT_SIDES ),
		DANCE_A( null ),
		DANCE_B( null ),
		DANCE_C( null ),
		DANCE_D( null ),
		DANCE_E( null ),
		DANCE_F( null ),
		DANCE_G( null ),
		DANCE_H( null ),
		DANCE_I( null ),
		DANCE_J( null ),
		DANCE_K( null ),
		DANCE_L( null ),
		DANCE_M( null ),
		DANCE_N( null ),
		DANCE_O( null ),
		DANCE_P( null ),
		DANCE_Q( null ),
		DANCE_R( null ),
		DANCE_S( null ),
		DANCE_T( null ),
		DANCE_U( null ),
		DANCE_V( null ),
		DANCE_W( null ),
		DANCE_X( null ),
		;
		private State m_state;
		Cycle( State state ) {
			m_state = state;
		}
		public State getState() {
			return m_state;
		}
	}
	public enum CycleAB implements FiniteStateMachine.CycleAB {
		START_COFFEE_MAKER( StateAB.PREPARED_TO_INTERACT_WITH_COFFEE_MAKER ),
		POUR_A_CUP_OF_COFFEE( StateAB.PREPARED_TO_INTERACT_WITH_COFFEE_MAKER ),
		REPAIR_DISHWASHER_TINKER( StateAB.PREPARED_TO_REPAIR_DISHWASHER ),
		REPAIR_DISHWASHER_INSPECT( StateAB.PREPARED_TO_REPAIR_DISHWASHER ),
		SLAP( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		SLAP_SILLY( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		POKE_HARD( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		SHOVE_HARD( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		CARESS_ACCEPTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		CARESS_REJECTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HIGH_FIVE( StateAB.PREPARED_TO_GREET_ADULT ),
		FIST_BUMP( StateAB.PREPARED_TO_GREET_ADULT ),
		OFFER_AND_RETURN_LOW_FIVE( StateAB.PREPARED_TO_GREET_ADULT ),
		OFFER_AND_RETURN_LOW_TEN( StateAB.PREPARED_TO_GREET_ADULT ),
		HIGH_AND_LOW_FIVE( StateAB.PREPARED_TO_GREET_ADULT ),
		FINGER_SHAKE( StateAB.PREPARED_TO_GREET_ADULT ),
		CROOKED_ARM_SHAKE( StateAB.PREPARED_TO_GREET_ADULT ),
		HUG_FRIENDLY_ACCEPTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_FRIENDLY_REJECTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_JUMP_INTO_ARMS_ACCEPTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_JUMP_INTO_ARMS_REJECTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_ROMANTIC_ACCEPTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_ROMANTIC_REJECTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_SQUEEZE_ACCEPTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		HUG_SQUEEZE_REJECTED( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		ANNOY_WITH_WHAT_IS_THAT_ON_YOUR_SHIRT_TRICK( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		TICKLE_ACCEPT( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		TICKLE_REJECT_FLATLY( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		TICKLE_REJECT_NICELY( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		TICKLE_REJECT_SHYLY( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		SHAKE_HANDS( StateAB.PREPARED_TO_INTERACT_WITH_ADULT ),
		GRIND_SHOULDERS( StateAB.PREPARED_TO_RUB_BACK_OF_ADULT ),
		CONCUSS_BACK( StateAB.PREPARED_TO_RUB_BACK_OF_ADULT ),
		SMOOTH_RIGHT_SIDE_OF_BACK( StateAB.PREPARED_TO_RUB_BACK_OF_ADULT ),
		PUT_IN_DISHWASHER( StateAB.PREPARED_TO_INTERACT_WITH_DISHWASHER ),
		;
		private StateAB m_stateAB;
		CycleAB( StateAB stateAB ) {
			m_stateAB = stateAB;
		}
		public StateAB getStateAB() {
			return m_stateAB;
		}
	}
	public enum CycleABC implements FiniteStateMachine.CycleABC {
		JAB( StateABC.PREPARED_TO_HIT_HEAVY_PUNCHING_BAG ),
		ONE_TWO( StateABC.PREPARED_TO_HIT_HEAVY_PUNCHING_BAG ),
		ROUNDHOUSE_KICK( StateABC.PREPARED_TO_HIT_HEAVY_PUNCHING_BAG ),
		;
		private StateABC m_stateABC;
		CycleABC( StateABC stateABC ) {
			m_stateABC = stateABC;
		}
		public StateABC getStateABC() {
			return m_stateABC;
		}
	}

	static {
		//edu.cmu.cs.dennisc.pattern.DefaultNameableAndClassOwnerTrackable.setNamesAndClassOwnersForPublicStaticFinalInstancesOwnedBy( Adult.class );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_EXERCISE );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_EXERCISE, Adult.State.PREPARED_TO_PERFORM_SIT_UP );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_EXERCISE, Adult.State.PREPARED_TO_PERFORM_PUSH_UP );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_EXERCISE, Adult.State.PREPARED_TO_PERFORM_LEG_LIFT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_PERFORM_SIT_UP, Adult.State.PREPARED_TO_PERFORM_SCISSOR_SIT_UP );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_PERFORM_PUSH_UP, Adult.State.PREPARED_TO_PERFORM_ONE_HANDED_PUSH_UP );

		FiniteStateMachine.addTransition( Adult.State.PREPARED_TO_PERFORM_PUSH_UP, Adult.State.PREPARED_TO_PERFORM_LEG_LIFT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_PERFORM_LEG_LIFT, Adult.State.PREPARED_TO_PERFORM_LEG_SIDE_KICK );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_WORK_OUT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_WORK_OUT, Adult.State.PREPARED_TO_PERFORM_SIDE_STRETCH );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_WORK_OUT, Adult.State.PREPARED_TO_PERFORM_JUMPING_JACK );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_WORK_OUT, Adult.State.PREPARED_TO_JOG_IN_PLACE );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_WORK_OUT, Adult.State.PREPARED_TO_PERFORM_KNEE_BEND );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CALL_OUT_TO_EVERYONE );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_WORRY );

		FiniteStateMachine.addTransition( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_SWAY_WITH_HANDS_HELD_UP_HIGH );
		FiniteStateMachine.addTransition( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_SWAY_WITH_HANDS_HELD_DOWN_LOW );

		FiniteStateMachine.addTransition( Adult.State.POSED_A, Adult.State.HANDS_AT_SIDES );
		FiniteStateMachine.addTransition( Adult.State.POSED_B, Adult.State.HANDS_AT_SIDES );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CLAP_ENTHUSIASTICALLY );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CLAP_BECAUSE_YOU_ARE_EXPECTED_TO_CLAP );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CLAP_QUIETLY );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_COUGH );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CLEAN_HIGH_CHAIR );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.SLOUCHED_WEIGHT_CENTERED );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.SLOUCHED_WEIGHT_CENTERED, Adult.State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_INTIMIDATE );

		//todo: generate inverse animation?
		FiniteStateMachine.addTransition( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_PLAY_HANDS_TALKING );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_MIME );

		FiniteStateMachine.addTransition( Adult.State.LOOKING_AT_FLOOR, Adult.State.HANDS_AT_SIDES );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.HANDS_ON_HIPS );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_ON_HIPS, Adult.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );

		//todo?
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.ARMS_CROSSED );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Adult.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Adult.State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.ARMS_CROSSED, Adult.State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.ARMS_CROSSED, Adult.State.HANDS_ON_HIPS );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT, Adult.State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.ARMS_CROSSED, Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.ARMS_CROSSED, Adult.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransition( Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Adult.State.ARMS_CROSSED );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Adult.State.HANDS_ON_HIPS );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_ON_HIPS, Adult.State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransition( Adult.State.HANDS_ON_HIPS, Adult.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT );

		//todo:
		//FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, null, Adult.State.DEPRESSED, null );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.ASLEEP_STANDING_UP );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.ASLEEP_ON_FLOOR );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_GUSSY_UP );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_PREEN );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_PANHANDLE );

		//todo
		//State.addTransitions( SITTING_WITH_LEGS_CROSSED, PREPARED_TO_WATCH_CLOUDS );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_WATCH_CLOUDS, Adult.State.PREPARED_TO_WATCH_CLOUDS_LYING_DOWN );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.GOING_INTO_LABOR );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_BOO );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CHEER );

		//note: stop dries eyes
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CRY );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_EXPRESS_EMBARRASSMENT );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_LAUGH );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_LAUGH_BIG );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.HANDS_COVERING_EARS );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_PINE );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.DEVASTATED );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.NOSE_COVERED );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_HAVE_INTENSE_TANTRUM );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.PREPARED_TO_CRINGE );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.SITTING_ON_FLOOR );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, Adult.State.GREETING_WITH_OPEN_ARMS );
		//todo
//		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, null, Adult.State.PREPARED_TO_DANCE_A, null );
//		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, null, Adult.State.PREPARED_TO_DANCE_B, null );
//		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.HANDS_AT_SIDES, null, Adult.State.PREPARED_TO_DANCE_C, null );

		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_DANCE_A, Adult.State.PREPARED_TO_DANCE_B );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_DANCE_A, Adult.State.PREPARED_TO_DANCE_C );
		FiniteStateMachine.addTransitionsBackAndForth( Adult.State.PREPARED_TO_DANCE_B, Adult.State.PREPARED_TO_DANCE_C );

//		FiniteStateMachine.addTransitionABs( Adult.State.PREPARED_TO_INTERACT_WITH_ADULT, Adult.State.PREPARED_TO_GREET_ADULT );
//		FiniteStateMachine.addTransitionABs( Adult.State.PREPARED_TO_INTERACT_WITH_ADULT, Adult.State.PREPARED_TO_RUB_BACK_OF_ADULT );
//
//		FiniteStateMachine.addTransitionABs( Adult.State.PREPARED_TO_INTERACT_WITH_DISHWASHER, Adult.State.PREPARED_TO_REPAIR_DISHWASHER );
//
//		FiniteStateMachine.addTransitionABCs( Adult.State.PREPARED_TO_INTERACT_WITH_HEAVY_PUNCHING_BAG, Adult.State.PREPARED_TO_HIT_HEAVY_PUNCHING_BAG );
	}

	protected Adult( Gender gender ) { 
		super( LifeStage.ADULT, gender );

//		//todo: remove
//		edu.cmu.cs.dennisc.scenegraph.Transformable sgLightTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
//		sgLightTransformable.applyTranslation( 0.0, 2.0, -1.0 );
//		sgLightTransformable.addComponent( new edu.cmu.cs.dennisc.scenegraph.PointLight() );
//		this.getSGTransformable().addComponent( sgLightTransformable );
	}
	public Adult() { 
		this( null );
	}

	@Override
	protected FiniteStateMachine.State getInitialState() {
		return Adult.State.HANDS_AT_SIDES;
	}
	@Override
	protected FiniteStateMachine.State getNeutralState() {
		return Adult.State.HANDS_AT_SIDES;
	}


	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void standUpStraight() {
		performStateTransition( State.HANDS_AT_SIDES );
	}

	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	@Override
	public Boolean isPregnant() {
		return false;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doSitUp( SitUpStyle style ) {
		perform( style.getSubjectCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void doSitUp() {
		doSitUp( SitUpStyle.NORMAL );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doPushUp( PushUpStyle style ) {
		perform( style.getSubjectCycle() );
	}

	@MethodTemplate( visibility=Visibility.CHAINED )
	public void doPushUp() {
		doPushUp( PushUpStyle.TWO_HANDED_ON_TOES );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doLegLift( LegLiftStyle style ) {
		perform( style.getSubjectCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void doLegLift() {
		doLegLift( LegLiftStyle.SMALL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doSideStretches() {
		perform( Cycle.SIDE_STRETCH );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void touchToes() {
		perform( Cycle.TOUCH_TOES );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doSideLegKick() {
		perform( Cycle.LEG_SIDE_KICK );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doJumpingJack() {
		perform( Cycle.JUMPING_JACK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doKneeBend() {
		perform( Cycle.KNEE_BEND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void jogInPlace() {
		perform( Cycle.JOG_IN_PLACE );
	}
	
	
	
	
	
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void talkWithRightHandToFace() {
		perform( Cycle.RIGHT_HAND_TALK_TO_FACE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void talkWithLeftHandToFace() {
		perform( Cycle.LEFT_HAND_TALK_TO_FACE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void talkBackAndForthBetweenHands() {
		perform( Cycle.RIGHT_HAND_AND_LEFT_HAND_TALK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void attackLeftHandWithRightHand() {
		perform( Cycle.RIGHT_HAND_ATTACK_LEFT_HAND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void mimeUpAndDown() {
		perform( Cycle.MIME_UP_AND_DOWN );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void mimeSideToSide() {
		perform( Cycle.MIME_SIDE_TO_SIDE_EXAGERATEDLY );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void mimeStuckInBox() {
		perform( Cycle.MIME_SIDE_TO_SIDE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void callOut() {
		perform( Cycle.CALL_OUT );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void callOutToEveryone() {
		perform( Cycle.CALL_OUT_TO_EVERYONE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void callEveryoneOver() {
		perform( Cycle.CALL_EVERYONE_OVER );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void cavort() {
		perform( Cycle.CAVORT );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void blowHorn() {
		perform( Cycle.BLOW_HORN );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void blowWhistle() {
		perform( Cycle.BLOW_WHISTLE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void spinAround() {
		perform( Cycle.SPIN_AROUND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void wriggleTheKinksOut() {
		perform( Cycle.WIGGLE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void groove() {
		perform( Cycle.OUTGOING_CIRCLE_FISTS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void pointWithBothHands( PointWithBothHandsStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void pointWithBothHands() {
		pointWithBothHands( PointWithBothHandsStyle.NORMAL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void makeCrazySign() {
		perform( Cycle.CRAZY_SIGN );
	}
	

	
	
	
	
	
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void plotSinisterly() {
		perform( Cycle.PLOT_SINISTER );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void rubHandsSinisterly() {
		perform( Cycle.RUB_HANDS_SINISTER );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void lookAroundParanoically() {
		perform( Cycle.LOOK_AROUND_PARANOICLY );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void throwAFit() {
		perform( Adult.Cycle.GET_ATTENTION_INSISTENTLY );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void attemptToIntimidate( IntimidationStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void attemptToIntimidate() {
		attemptToIntimidate( IntimidationStyle.PUNCH_HAND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void scream() {
		perform( Cycle.PRIMAL_SCREAM );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void bemoanWhyMe() {
		perform( Cycle.BEMOAN_WHY_ME );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void kickInFrustration() {
		perform( Cycle.KICK_IN_FRUSTRATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void stampFoot() {
		perform( Cycle.STAMP_FOOT );
	}
	@Deprecated
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void pointScoldinginly() {
		perform( Cycle.POINT_SCOLDINGLY );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void pointScoldingly() {
		perform( Cycle.POINT_SCOLDINGLY );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void reject( RejectStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void reject() {
		reject( RejectStyle.ASSERTIVELY );
	}



	

	
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void tapTemple() {
		perform( Cycle.TAP_TEMPLE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void realizeSomething() {
		perform( Cycle.REALIZE_SOMETHING );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void demonstrateTheNeedToUrinate() {
		perform( Cycle.DEMONSTRATE_A_NEED_TO_URINATE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void searchForSourceOfOdor( SearchForSourceOfOdorStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void searchForSourceOfOdor() {
		searchForSourceOfOdor( SearchForSourceOfOdorStyle.DISCOVER_UNDER_ARM );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void hockALoogie() {
		perform( Cycle.HOCK_LOOGEY );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void snortPhlegm() {
		perform( Cycle.SNIFF_PHELGM );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void pickRearEnd() {
		perform( Cycle.PICK_REAR_END );
	}
//	DEMOSTRATE_MENTAL_ANGUISH( State.HANDS_AT_SIDES ),
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void wetPants() {
		perform( Cycle.WET_PANTS );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void belch() {
		perform( Cycle.BELCH );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void fart( FartStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void fart() {
		fart( FartStyle.WITH_A_QUICK_SNIFF );
	}
	public void retch() {
		perform( Cycle.RETCH );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void scratchHead() {
		perform( Cycle.SCRATCH_HEAD );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void wipeMouth() {
		perform( Cycle.WIPE_MOUTH );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void scratchArmPit() {
		perform( Cycle.SCRATCH_ARM_PIT );
	}
	
	
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void demonstrateThirst() {
		perform( Cycle.DEMONSTRATE_THIRST );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void demonstrateNausea() {
		perform( Cycle.DEMOSTRATE_NAUSEA );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void pickAtLintOnPants() {
		perform( Cycle.PICK_AT_LINT_ON_PANTS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void nod() {
		perform( Cycle.NOD );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void sighHopefully() {
		perform( Cycle.HOPEFUL_SIGH );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void nodWithMindSomewhereElse() {
		perform( Cycle.NOD_WITH_MIND_SOMEWHERE_ELSE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void brushShoulder() {
		perform( Cycle.BRUSH_SHOULDER );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void brushThigh() {
		perform( Cycle.BRUSH_PANTS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void rockBackAndForth( RockBackAndForthStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void rockBackAndForth() {
		rockBackAndForth( RockBackAndForthStyle.POPPING_FIST_IN_HAND );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void sparInPlace() {
		perform( Cycle.SPAR_IN_PLACE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void shuffleFeet() {
		perform( Cycle.SHUFFLE_FEET_HEAL_TO_TOE );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void demostrateShyness( DemonstrateShynessStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void demostrateShyness() {
		demostrateShyness( DemonstrateShynessStyle.ACT_COY );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void swayBackAndForth( SwayBackAndForthStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void swayBackAndForth() {
		swayBackAndForth( SwayBackAndForthStyle.WITH_HANDS_LOOSE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void tiltHeadToTheSideAndBack( TiltHeadBackAndForthStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void tiltHeadToTheSideAndBack() {
		tiltHeadToTheSideAndBack( TiltHeadBackAndForthStyle.CURIOUS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void scanDownFromSideToSide() {
		perform( Cycle.TILT_HEAD_DOWN_BACK_AND_FORTH );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doDoubleTake() {
		perform( Cycle.DOUBLE_TAKE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void checkNails() {
		perform( Cycle.CHECK_NAILS_FROM_ARMS_ON_HIPS_WEIGHT_SHIFTED_RIGHT_POSE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void scuffFoot() {
		perform( Cycle.SCUFF_LEFT_FOOT );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void brushThighs() {
		perform( Cycle.BRUSH_THIGHS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void scratchForearm() {
		perform( Cycle.SCRATCH_FOREARM );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void loosenUpFingers() {
		perform( Cycle.LOOSEN_UP_FINGERS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void stretchShoulders() {
		perform( Cycle.STRETCH_SHOULDERS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void brushHairFromFace() {
		perform( Cycle.BRUSH_HAIR_FROM_FACE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void sighDeeply() {
		perform( Cycle.DEEP_SIGH );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void shakeHeadDejectedly() {
		perform( Cycle.SHAKE_HEAD );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void sighInDisjust() {
		perform( Cycle.SIGH_IN_DISGUST );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void fanSelf() {
		perform( Cycle.FAN_SELF );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void coverEars() {
		perform( Cycle.COVER_EARS_TO_SHIELD_LOUD_NOISE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void coverMouth() {
		perform( Cycle.COVER_MOUTH_IN_SHOCK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void coverEyes() {
		perform( Cycle.COVER_EYES_IN_AVERSION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void shrug( ShrugStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void shrug() {
		shrug( ShrugStyle.AS_IF_TO_SAY_OH_WELL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void boo( BooStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void boo() {
		boo( BooStyle.NORMAL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void checkForRain() {
		perform( Cycle.CHECK_FOR_RAIN );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void recoilInShock() {
		perform( Cycle.RECOIL_IN_SHOCK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void shoo( ShooStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void shoo() {
		shoo( ShooStyle.NORMAL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void listen( ListenStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void listen() {
		listen( ListenStyle.WITH_HANDS_TOGETHER );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void pointOutBothSidesOfAnIssue() {
		perform( Cycle.DEBATE_POINT_WITH_BOTH_SIDES_OF_THE_ISSUE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void debatePoint() {
		perform( Cycle.DEBATE_POINT_WITH_FINGER_WAG );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void performHandChops() {
		perform( Cycle.DEBATE_POINT_WITH_HAND_CHOPS );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void contemplateAndRespond() {
		perform( Cycle.CONTEMPLATE_AND_RESPOND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void wave( WaveStyle style ) {
		perform( style.getCycle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void wave() {
		wave( WaveStyle.NORMAL );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void rubNeck() {
		perform( Cycle.RUB_NECK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void rubChin() {
		perform( Cycle.RUB_CHIN );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void smellAroma() {
		perform( Cycle.SNIFF_AROMA_IN_AIR );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void waftInAndSmellAroma() {
		perform( Cycle.WAFT_AND_SNIFF_AROMA );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void rubUnderneathNose() {
		perform( Cycle.RUB_UNDERNEATH_NOSE );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void inviteToAcceptHug() {
		perform( Cycle.INVITE_TO_ACCEPT_HUG );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void giveThumbsUp() {
		perform( Cycle.GIVE_THUMBS_UP );
	}


	
	

//	GREET_WITH_A_FLOURISH( State.HANDS_AT_SIDES ),

//	SCRATCH_HEAD_FROM_ARMS_ON_HIPS_POSE( State.HANDS_ON_HIPS ),
//	RUB_UNDERNEATH_NOSE_FROM_ARMS_ON_HIPS_WEIGHT_SHIFTED_RIGHT_POSE( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
//	CHECK_NAILS_FROM_ARMS_ON_HIPS_WEIGHT_SHIFTED_RIGHT_POSE( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),


//	CRACK_KNUCKLES_TO_LOOSEN_THEM( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
	
	
	
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void browse() {
//		perform( Cycle.BROWSE );
//	}

//	CHECK_CHEST( State.HANDS_AT_SIDES ),
//	CHECK_RIGHT_TOE( State.HANDS_AT_SIDES ),
//	CHECK_RIGHT_ARM( State.HANDS_AT_SIDES ),
//	CHECK_BELLY( State.HANDS_AT_SIDES ),

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void clap( ClapStyle clapStyle ) {
//		perform( clapStyle.getSubjectCycle() );
//	}
//	@MethodTemplate( visibility=Visibility.CHAINED )
//	public void clap() {
//		clap( ClapStyle.NORMAL );
//	}
	
//	CHECK_CLOTHES_WITH_SIDE_STEP( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_EXAGERATED_ARM_LIFT( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_BODY_TWIST( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_STEP_BACK_AND_HANDS_ON_HIPS( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_RIGHT_LEG_FORWARD( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_RIGHT_LEG_LIFT( State.HANDS_AT_SIDES ),


//	CHECK_CLOTHES_BOTH_LEGS( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_SLIGHT_ARM_LIFT( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_WITH_BEND_FORWARD( State.HANDS_AT_SIDES ),
//	CHECK_CLOTHES_RIGHT_ARM( State.HANDS_AT_SIDES ),

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void cough( CoughStyle coughStyle ) {
//		perform( coughStyle.getCycle() );
//	}
//	@MethodTemplate( visibility=Visibility.CHAINED )
//	public void cough() {
//		cough( CoughStyle.SHORT );
//	}
// needs naming work
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void fidget( FidgetStyle fidgetStyle ) {
//		perform( fidgetStyle.getCycle() );
//	}
//	@MethodTemplate( visibility=Visibility.CHAINED )
//	public void fidget() {
//		fidget( FidgetStyle.PAT_LEGS );
//	}
	
	
	
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void tiltHeadBackAndForthSlowly() {
//		perform( Cycle.TILT_HEAD_DOWN_BACK_AND_FORTH_SLOWLY );
//	}

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void mutter() {
//		perform( Cycle.MUTTER );
//	}
	
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void rubKnees() {
//		perform( Cycle.RUB_KNEES );
//	}
	
//	SLOUCHED_SIGH( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
//	SLOUCHED_SWAY( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
//	SLOUCHED_SWAY_WITH_HEAD_BACK( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
//	SLOUCHED_BREATHE_WITH_HEAD_BACK( State.SLOUCHED_WEIGHT_ON_RIGHT_FOOT ),
	
	

	
	//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void swoon() {
//		perform( Cycle.SWOON );
//	}
	
//	FLIRTATIOUS_POSE_AND_WAVE( State.HANDS_AT_SIDES ),

//	TWIST_TORSO( State.ARMS_CROSSED ),
//	SHIFT_WEIGHT_BACK_AND_FORTH( State.ARMS_CROSSED_WEIGHT_SHIFTED_RIGHT ),

//	//todo: rename
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void tapLipForInspiration() {
//		perform( Cycle.TAP_LIP_FOR_INSPIRATION );
//	}

////	PRACTICE_ROMANTIC_HUG( State.HANDS_AT_SIDES ),
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void practiceHug() {
//		perform( Cycle.PRACTICE_FRIENDLY_HUG );
//	}
//
////	PRACTICE_ROMANTIC_KISS( State.HANDS_AT_SIDES ),
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void practiceKiss() {
//		perform( Cycle.PRACTICE_FRIENDLY_KISS );
//	}


//	FEEL_BACK( State.HANDS_AT_SIDES ),
//	FEEL_NECK( State.HANDS_AT_SIDES ),
//	CATCH_ONESELF_ALMOST_FALLING_ASLEEP( State.HANDS_AT_SIDES ),
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void catchOneselfAlmostFallingAsleep() {
//		perform( Cycle.CATCH_ONESELF_ALMOST_FALLING_ASLEEP );
//	}

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void demonstrateHunger() {
//		perform( Cycle.DEMONSTRATE_HUNGER );
//	}
	
	//examineHair( ReflectiveSurface ) //mirror, window, stainlessSteelFridge
//	EXAMINE_HAIR_IN_MIRROR( State.PREPARED_TO_GUSSY_UP ),
	
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void smellBreath() {
//		perform( Cycle.SMELL_BREATH );
//	}
	
//	EXAMINE_FACE_IN_MIRROR( State.PREPARED_TO_GUSSY_UP ),
//	BRUSH_SHOULDER_FROM_PREEN( State.PREPARED_TO_PREEN ),
//	BRUSH_PANTS_FROM_PREEN( State.PREPARED_TO_PREEN ),
//	EXAMINE_SELF( State.PREPARED_TO_PREEN ),

//	CLAP_AND_POINT_WITH_BOTH_HANDS( State.HANDS_AT_SIDES ),

//	DISPLAY_SIGN( State.PREPARED_TO_PANHANDLE ),
//	RATTLE_CAN_FOR_PASSERSBY( State.PREPARED_TO_PANHANDLE ),
//	RATTLE_CAN( State.PREPARED_TO_PANHANDLE ),
//	CHECK_CAN( State.PREPARED_TO_PANHANDLE ),
//	WAVE_HANDS_MADLY( State.HANDS_AT_SIDES ),
//	LOOK_AROUND_WHILE_SEATED( State.PREPARED_TO_WATCH_CLOUDS ),
//	WIGGLE_KNEES( State.PREPARED_TO_WATCH_CLOUDS_LYING_DOWN ),
//	LOOK_AROUND_WHILE_LYING_DOWN( State.PREPARED_TO_WATCH_CLOUDS_LYING_DOWN ),
//	RUB_PREGNANT_BELLY( State.HANDS_AT_SIDES ),
//	HOWL_WITH_LABOR_PAINS( State.GOING_INTO_LABOR ),
//	YELL_WITH_LABOR_PAINS( State.GOING_INTO_LABOR ),
//	APPLAUD( State.HANDS_AT_SIDES ),

//	FAN_SELF( State.HANDS_AT_SIDES ),
//	EXAGGERATED_SWOON( State.HANDS_AT_SIDES ),
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void catCall() {
//		perform( Cycle.CAT_CALL );
//	}

//	CLAP_WITH_FIST_PUMP( State.HANDS_AT_SIDES ),
//	CHEER( State.PREPARED_TO_CHEER ),
//	CLEAR_THROAT( State.HANDS_AT_SIDES ),
//	WAIL( State.PREPARED_TO_CRY ),
//	SOB( State.PREPARED_TO_CRY ),
//	REFUSE_IN_A_KEEP_IT_AWAY_FROM_ME_POSTURE( State.HANDS_AT_SIDES ),
//	EXPRESS_EMBARRASSMENT( State.PREPARED_TO_EXPRESS_EMBARRASSMENT ),
//	EXPRESS_MORTIFIED_EMBARRASSMENT( State.PREPARED_TO_EXPRESS_EMBARRASSMENT ),

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void laugh( LaughStyle laughStyle ) {
//		perform( laughStyle.getCycle() );
//	}
//	@MethodTemplate( visibility=Visibility.CHAINED )
//	public void laugh() {
//		laugh( LaughStyle.CHUCKLE );
//	}

//	EXAGGERATED_PINING_SIGH( State.PREPARED_TO_PINE ),
//	SIGH_DEVASTATEDLY( State.DEVASTATED ),
//	SIGH_DEVASTATEDLY_WITH_NOSE_WIPE( State.DEVASTATED ),
//	RECOIL_IN_SHOCK( State.HANDS_AT_SIDES ),
//	SIGH( State.HANDS_AT_SIDES ),
//	WAVE_AWAY_FOUL_SCENT( State.NOSE_COVERED ),
//	TEST_SNIFF_OF_FOUL_SCENT( State.NOSE_COVERED ),
//	EXPRESS_STARTLE( State.HANDS_AT_SIDES ),
//	JUMP_IN_EXTREME_SURPRISE( State.HANDS_AT_SIDES ),
//	EXPRESS_WHY_ME( State.PREPARED_TO_HAVE_INTENSE_TANTRUM ),
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void throwTantrum() {
//		perform( Cycle.THROW_INTENSE_TANTRUM );
//	}
//	CLENCH_HANDS_IN_WORRY( State.PREPARED_TO_CRINGE ),
//	WAVE( State.HANDS_AT_SIDES ),



//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void sneeze() {
//		perform( Cycle.SNEEZE );
//	}

//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void callOver( CallOverStyle callOverStyle ) {
//		perform( callOverStyle.getCycle() );
//	}
//	@MethodTemplate( visibility=Visibility.CHAINED )
//	public void callOver() {
//		callOver( CallOverStyle.WITH_A_WAVE );
//	}
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void greet( Adult who, GreetStyle greetStyle ) {
//		perform( greetStyle.getCycle(), who, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void hug( Adult who, HugStyle hugStyle, Boolean isAccepted ) {
//		CycleAB cycle;
//		if( isAccepted ) {
//			cycle = hugStyle.getAcceptCycle();
//		} else {
//			cycle = hugStyle.getRejectCycle();
//		}
//		perform( cycle, who, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void giveBackRubTo( Adult who, BackRubStyle backRubStyle ) {
//		perform( backRubStyle.getSubjectDirectObjectCycle(), who, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void annoyWithWhatIsThatOnYourShirtTrick( Adult who ) {
//		perform( CycleAB.ANNOY_WITH_WHAT_IS_THAT_ON_YOUR_SHIRT_TRICK, who, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void putIn( org.alice.apis.moveandturn.Transformable what, Dishwasher dishwasher ) {
//		//routeTo( PUT_IN_DISHWASHER.getPathA(), dishwasher );
//
//		//todo: handle what parameter
//
//		performStateTransition( StateAB.PREPARED_TO_INTERACT_WITH_DISHWASHER, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//		perform( CycleAB.PUT_IN_DISHWASHER, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void repair( Dishwasher dishwasher ) {
//		//		routeTo( REPAIR_DISHWASHER_TINKER.getPathA(), dishwasher );
//		performStateTransition( StateAB.PREPARED_TO_REPAIR_DISHWASHER, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//		perform( CycleAB.REPAIR_DISHWASHER_TINKER, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//		perform( CycleAB.REPAIR_DISHWASHER_INSPECT, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//		performStateTransition( StateAB.PREPARED_TO_INTERACT_WITH_DISHWASHER, dishwasher, FiniteStateMachine.BindingAB.BOUND_TOGETHER );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void punchJab( HeavyPunchingBag heavyPunchingBag, Adult trainer ) {
//		perform( CycleABC.JAB, heavyPunchingBag, trainer, FiniteStateMachine.BindingABC.B_IS_BOUND_TO_C_ONLY );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void punchOneTwo( HeavyPunchingBag heavyPunchingBag, Adult trainer ) {
//		perform( CycleABC.ONE_TWO, heavyPunchingBag, trainer, FiniteStateMachine.BindingABC.B_IS_BOUND_TO_C_ONLY );
//	}
//
//	@MethodTemplate( visibility=Visibility.PRIME_TIME )
//	public void kickRoundhouse( HeavyPunchingBag heavyPunchingBag, Adult trainer ) {
//		perform( CycleABC.ROUNDHOUSE_KICK, heavyPunchingBag, trainer, FiniteStateMachine.BindingABC.B_IS_BOUND_TO_C_ONLY );
//	}
	@Override
	protected edu.cmu.cs.dennisc.math.AxisAlignedBox getLocalAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		final double X = 0.208;
		final double Y = 1.7;
		final double Z = 0.131;
		rv.setMinimum( -X, 0.0, -Z );
		rv.setMaximum( +X,   Y, +Z );
		return rv;
	}	
}

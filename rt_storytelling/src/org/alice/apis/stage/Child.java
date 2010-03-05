/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.stage;


import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class Child extends Person {
	public enum State implements FiniteStateMachine.State {
		HANDS_AT_SIDES,
		HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT,
		HANDS_ON_HIPS,
		HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT,
		ARMS_BEHIND_BACK,
		ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT,
		DISTRESSED,
		PINING,
		SAD,
		LAZY_ARM_DROOP,
		LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT,
		WATCH,
		WATCH_ARMS_UP,
		PREPARE_TO_TALK,
		PREPARE_TO_DANCE,
		EMBARASSED,
		REACT_TO_NOISE,
		BE_MEAN,
		PREPARE_TO_LAUGH_LOCOMOTION,
		PREPARE_TO_LAUGH,
		PREPARE_TO_WHINE,
		WATCH_DISTRESSED_LOCOMOTION,
		WATCH_DISTRESSED,
		PREPARE_TO_EXERCISE,
		PREPARE_TO_JOG,
		PREPARE_TO_DO_JUMPING_JACK,
		PREPARE_TO_DO_KNEE_BEND,
		PREPARE_TO_DO_SIDE_STRETCH,
		PREPARE_TO_SING_BDAY,
		PREPARE_TO_BOO,
		PREPARE_TO_CHEER,
		PREPARE_TO_CRY,
		BUILD_STATIC_CHARGE,
		;
	}
	public enum StateAB implements FiniteStateMachine.StateAB {
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
	public enum StateABC implements FiniteStateMachine.StateABC {
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
	public enum Cycle implements FiniteStateMachine.Cycle {
		WORRY( State.DISTRESSED ),
		WORRY_COVER_EYES( State.DISTRESSED ),
		WORRY_CRINGE( State.DISTRESSED ),
		PINING_SIGH( State.PINING ),
		PINING_WHIMPER( State.PINING ),
		SAD_SNUFFLE( State.SAD ),
		SAD_SIGH( State.SAD ),
		ROLL_HEAD_BACK_WEIGHT_SHIFTED_RIGHT( State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT ),
		LAZY_SIGH_WEIGHT_SHIFTED_RIGHT( State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT ),
		LAZY_SWAY_WEIGHT_SHIFTED_RIGHT( State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT ),
		LAZY_HEAD_BACK_SWAY( State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT ),
		RUB_CHIN_WHILE_WATCHING( State.WATCH ),
		TALK_GESTURE_TEENSY( State.PREPARE_TO_TALK ),
		TALK_CLAP( State.PREPARE_TO_TALK ),
		TALK_GESTURE_ARM( State.PREPARE_TO_TALK ),
		DANCE_BOP_HEAD( State.HANDS_AT_SIDES ),
		DANCE_SHAKE_ARMS( State.HANDS_AT_SIDES ),
		DANCE_WIGGLE_HIPS( State.HANDS_AT_SIDES ),
		DANCE_JUMP_AROUND( State.PREPARE_TO_DANCE ),
		DANCE_SHIFT_WEIGHT( State.PREPARE_TO_DANCE ),
		DANCE_WAVE_ARMS( State.PREPARE_TO_DANCE ),
		EMBARASSED_ABASHED( State.EMBARASSED ),
		EMBARASSED_MORTIFIED( State.EMBARASSED ),
		CRINGE_AT_NOISE( State.REACT_TO_NOISE ),
		MEAN_CLENCH_FISTS( State.BE_MEAN ),
		MEAN_SOCK_HAND( State.BE_MEAN ),
		MEAN_CRACK_KNUCKLES( State.BE_MEAN ),
		MEAN_IGNORE( State.BE_MEAN ),
		GIGGLE_LOCOMOTION( State.PREPARE_TO_LAUGH_LOCOMOTION ),
		CHUCKLE_LOCOMOTION( State.PREPARE_TO_LAUGH_LOCOMOTION ),
		SNICKER_LOCOMOTION( State.PREPARE_TO_LAUGH_LOCOMOTION ),
		GUFFAW_AND_POINT( State.PREPARE_TO_LAUGH ),
		HILARITY_AND_POINT( State.PREPARE_TO_LAUGH ),
		GIGGLE( State.PREPARE_TO_LAUGH ),
		CHUCKLE( State.PREPARE_TO_LAUGH ),
		SNICKER( State.PREPARE_TO_LAUGH ),
		LAUGH_CLAP( State.PREPARE_TO_LAUGH ),
		GUFFAW( State.PREPARE_TO_LAUGH ),
		HILARITY( State.PREPARE_TO_LAUGH ),
		WHINE_SHORT_LOCOMOTION( State.PREPARE_TO_WHINE ),
		WHINE_MEDIUM_LOCOMOTION( State.PREPARE_TO_WHINE ),
		WHINE_STRONG_LOCOMOTION( State.PREPARE_TO_WHINE ),
		WATCH_WORRIED_LOCOMOTION( State.WATCH_DISTRESSED_LOCOMOTION ),
		WATCH_CANT_LOOK_LOCOMOTION( State.WATCH_DISTRESSED_LOCOMOTION ),
		WATCH_CRINGE_LOCOMOTION( State.WATCH_DISTRESSED_LOCOMOTION ),
		DISTRESSED_WATCH_WORRIED( State.WATCH_DISTRESSED ),
		DISTRESSED_WATCH_CRINGE( State.WATCH_DISTRESSED ),
		DISTRESSED_WATCH_CANT_LOOK( State.WATCH_DISTRESSED ),
		TOUCH_TOES( State.PREPARE_TO_EXERCISE ),
		JOG_IN_PLACE( State.PREPARE_TO_JOG ),
		DO_JUMPING_JACK( State.PREPARE_TO_DO_JUMPING_JACK ),
		DO_KNEE_BEND( State.PREPARE_TO_DO_KNEE_BEND ),
		DO_SIDE_STRETCH( State.PREPARE_TO_DO_SIDE_STRETCH ),
		BDAY_BOUNCE( State.PREPARE_TO_SING_BDAY ),
		BDAY_HEAD_TILT( State.PREPARE_TO_SING_BDAY ),
		BDAY_SWAY( State.PREPARE_TO_SING_BDAY ),
		BDAY_CALL_OVER( State.HANDS_AT_SIDES ),
		CAT_CALL( State.PREPARE_TO_BOO ),
		RASPBERRY( State.PREPARE_TO_BOO ),
		BOO_THUMBS_DOWN( State.PREPARE_TO_BOO ),
		CALL_AND_CHEER( State.PREPARE_TO_CHEER ),
		CRY( State.PREPARE_TO_CRY ),
		SOB( State.PREPARE_TO_CRY ),
		WAIL( State.PREPARE_TO_CRY ),
		FOOT_SCRATCH( State.HANDS_AT_SIDES ),
		HEAD_SCRATCH( State.HANDS_AT_SIDES ),
		PUT_ARM_OVER_HEAD( State.HANDS_AT_SIDES ),
		ROCK_BACK_AND_FORTH_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		TWIST_KNEE_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		PICK_NOSE_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		SWAY_LEFT_RIGHT_ARMS_BEHIND_BACK( State.ARMS_BEHIND_BACK ),
		BOUNCE_ARMS_BEHIND_BACK( State.ARMS_BEHIND_BACK ),
		TOE_PIVOT_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		TAP_SIDES_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		ROCK_LEFT_AND_RIGHT_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		BACK_STRECH_HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		LEG_SCRATCH_HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		RUB_NOSE_HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		TWIST_ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT( State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT ),
		SHRUG_ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT( State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT ),
		KICK_GROUND_ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT( State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT ),
		HAPPY_SIGH( State.HANDS_AT_SIDES ),
		SLEEPY_NOD( State.HANDS_AT_SIDES ),
		HAPPY_SWAY( State.HANDS_AT_SIDES ),
		SWOON( State.HANDS_AT_SIDES ),
		HAPPY_WAVE( State.HANDS_AT_SIDES ),
		CUTE_WAVE( State.HANDS_AT_SIDES ),
		DO_THE_TWIST( State.HANDS_AT_SIDES ),
		ROCK_BACK_AND_FORTH( State.HANDS_AT_SIDES ),
		PLAYFUL_PUNCH( State.HANDS_AT_SIDES ),
		PLAYFUL_HEEL_TOE_SHUFFLE( State.HANDS_AT_SIDES ),
		RUB_CHIN( State.HANDS_AT_SIDES ),
		TAP_HEAD( State.HANDS_AT_SIDES ),
		AH_HA_GESTURE( State.HANDS_AT_SIDES ),
		IDLE_RUB_CHIN( State.HANDS_AT_SIDES ),
		IDLE_TAP_HEAD( State.HANDS_AT_SIDES ),
		IDLE_AH_HA_GESTURE( State.HANDS_AT_SIDES ),
		SLOPPY_NOSE_RUB( State.HANDS_AT_SIDES ),
		SLOPPY_HEAD_SCRATCH( State.HANDS_AT_SIDES ),
		SLOPPY_MOUTH_WIPE( State.HANDS_AT_SIDES ),
		SLOPPY_SCRATCH_ARMPIT( State.HANDS_AT_SIDES ),
		SHOW_HUNGER( State.HANDS_AT_SIDES ),
		SMELL_SELF( State.HANDS_AT_SIDES ),
		NEED_TO_GO_TO_THE_BATHROOM( State.HANDS_AT_SIDES ),
		CLUTCH_HEAD_IN_FRUSTRATION( State.HANDS_AT_SIDES ),
		WET_PANTS( State.HANDS_AT_SIDES ),
		NAUSEA( State.HANDS_AT_SIDES ),
		CUTE_WAVE_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		SIGH_AND_STARE_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		BIG_CLAP( State.HANDS_AT_SIDES ),
		ANNOYED_FIST_CLENCH( State.HANDS_AT_SIDES ),
		ANNOYED_FIST_CLENCH_LOCOMOTION( State.HANDS_AT_SIDES ),
		ANNOYED_SIGH_LOCOMOTION( State.HANDS_AT_SIDES ),
		ANNOYED_SIGH( State.HANDS_AT_SIDES ),
		MAKE_CRAZY_SIGN( State.HANDS_AT_SIDES ),
		MAKE_CRAZY_SIGN_LOCOMOTION( State.HANDS_AT_SIDES ),
		SHOO_LOCOMOTION( State.HANDS_AT_SIDES ),
		SHOO_SHYLY_LOCOMOTION( State.HANDS_AT_SIDES ),
		SHOO( State.HANDS_AT_SIDES ),
		SHOO_SHYLY( State.HANDS_AT_SIDES ),
		STARTLED( State.HANDS_AT_SIDES ),
		WAVE( State.HANDS_AT_SIDES ),
		WAVE_LOCOMOTION( State.HANDS_AT_SIDES ),
		CHEER( State.HANDS_AT_SIDES ),
		CLEAR_THROAT( State.HANDS_AT_SIDES ),
		NOD_TWICE( State.HANDS_AT_SIDES ),
		SHOCKED_REACTION( State.HANDS_AT_SIDES ),
		SHRUG( State.HANDS_AT_SIDES ),
		YELL_AT( State.HANDS_AT_SIDES ),
		CALL_OVER( State.HANDS_AT_SIDES ),
		EXCITED_JUMP( State.HANDS_AT_SIDES ),
		CRINGE( State.HANDS_AT_SIDES ),
		SLOUCH( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		REJECT_VEHEMENTLY( State.HANDS_AT_SIDES ),
		REJECT_TIREDLY( State.HANDS_AT_SIDES ),
		LISTEN_EAGERLY( State.HANDS_AT_SIDES ),
		WHEW( State.HANDS_AT_SIDES ),
		BIG_WHEW( State.HANDS_AT_SIDES ),
		BECOME_FAT( State.HANDS_AT_SIDES ),
		BECOME_FIT( State.HANDS_AT_SIDES ),
		BECOME_FIT_LAZY( State.HANDS_AT_SIDES ),
		SWING_FISTS_DANCINGLY( State.HANDS_AT_SIDES ),
		GESTURE_LOW_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		GESTURE_MEDIUM_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		GESTURE_HIGH_HANDS_ON_HIPS( State.HANDS_ON_HIPS ),
		UNHAPPY_MUTTER( State.HANDS_AT_SIDES ),
		SINISTER_NOD( State.HANDS_AT_SIDES ),
		RUB_HANDS_EVILY( State.HANDS_AT_SIDES ),
		UNHAPPY_BREATHER( State.HANDS_AT_SIDES ),
		LOOK_AROUND_SUSPICIOUSLY( State.HANDS_AT_SIDES ),
		PAT_LEGS_AND_LOOK_AROUND( State.HANDS_AT_SIDES ),
		PAT_LEGS( State.HANDS_AT_SIDES ),
		BOUNCE_IN_PLACE( State.HANDS_AT_SIDES ),
		CLAP_HANDS_AND_LOOK_AROUND( State.HANDS_AT_SIDES ),
		ROCK_BACK_AND_FORTH_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		TWIST_BODY_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		GESTURE_LOW( State.HANDS_AT_SIDES ),
		GESTURE_MEDIUM( State.HANDS_AT_SIDES ),
		GESTURE_HIGH( State.HANDS_AT_SIDES ),
		BELCH_BIG( State.HANDS_AT_SIDES ),
		BELCH_MEDIUM( State.HANDS_AT_SIDES ),
		STRONG_SNEEZE( State.HANDS_AT_SIDES ),
		IMPLORING_A_HIGHER_POWER_FOR_AN_EXPLANATION_OF_A_FRUSTRATING_SITUATION( State.HANDS_AT_SIDES ),
		SHAKE_HEAD_IN_DISAGREEMENT( State.HANDS_AT_SIDES ),
		REACT_TO_BURGLAR( State.HANDS_AT_SIDES ),
		CLAP_FIST_AND_BOUNCE( State.HANDS_AT_SIDES ),
		CLAP_HANDS( State.HANDS_AT_SIDES ),
		CLAP_FIST( State.HANDS_AT_SIDES ),
		BROWSE( State.HANDS_AT_SIDES ),
		KICK_GROUND_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		SWAY_LEFT_RIGHT_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		YELL_FOR_ATTENTION( State.HANDS_AT_SIDES ),
		BORED_LIP_FLAP( State.HANDS_ON_HIPS ),
		PICK_NOSE( State.HANDS_ON_HIPS ),
		SCRATCH_ARMPIT( State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT ),
		BELCH_LAZY( State.HANDS_AT_SIDES ),
		FART_AND_WAVE( State.HANDS_AT_SIDES ),
		FART( State.HANDS_AT_SIDES ),
		HOCK_LOOGEY( State.HANDS_AT_SIDES ),
		SNORT_PHLEGM( State.HANDS_AT_SIDES ),
		PICK_AT_BUTT_WEIGHT_SHIFTED_RIGHT( State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT ),
		SMELL_FINGER( State.HANDS_AT_SIDES ),
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
		;
		private StateABC m_stateABC;
		CycleABC( StateABC stateABC ) {
			m_stateABC = stateABC;
		}
		public StateABC getStateABC() {
			return m_stateABC;
		}
	}

	static{
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.HANDS_ON_HIPS );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.ARMS_BEHIND_BACK );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.ARMS_BEHIND_BACK, Child.State.HANDS_ON_HIPS );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Child.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT, Child.State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT, Child.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.HANDS_AT_SIDES_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_ON_HIPS, Child.State.HANDS_ON_HIPS_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.ARMS_BEHIND_BACK, Child.State.ARMS_BEHIND_BACK_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.DISTRESSED );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PINING );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.SAD );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.LAZY_ARM_DROOP );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.LAZY_ARM_DROOP, Child.State.LAZY_ARM_DROOP_WEIGHT_SHIFTED_RIGHT );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.WATCH );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.WATCH, Child.State.WATCH_ARMS_UP );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_TALK );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_DANCE );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.EMBARASSED );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.REACT_TO_NOISE );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.BE_MEAN );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_LAUGH_LOCOMOTION );
//		FiniteStateMachine.addTransitionsBackAndForth( Child.State.PREPARE_TO_REACT, Child.State.PREPARE_TO_LAUGH );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_WHINE );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.WATCH_DISTRESSED_LOCOMOTION );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.WATCH_DISTRESSED );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_EXERCISE );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.PREPARE_TO_EXERCISE, Child.State.PREPARE_TO_JOG );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.PREPARE_TO_EXERCISE, Child.State.PREPARE_TO_DO_JUMPING_JACK );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.PREPARE_TO_EXERCISE, Child.State.PREPARE_TO_DO_KNEE_BEND );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.PREPARE_TO_EXERCISE, Child.State.PREPARE_TO_DO_SIDE_STRETCH );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_SING_BDAY );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_BOO );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_CHEER );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.PREPARE_TO_CRY );
		FiniteStateMachine.addTransitionsBackAndForth( Child.State.HANDS_AT_SIDES, Child.State.BUILD_STATIC_CHARGE );
	}

	public Child( Gender gender ) { 
		super( LifeStage.CHILD, gender );
	}
	
	public Child()
	{
		this(null);
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public Boolean isPregnant() {
		return Boolean.FALSE;
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	protected FiniteStateMachine.State getInitialState() {
		return Child.State.HANDS_AT_SIDES;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	protected FiniteStateMachine.State getNeutralState() {
		return Child.State.HANDS_AT_SIDES;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void standUpStraight() {
		performStateTransition( State.HANDS_AT_SIDES );
	}
	
	
	//Exercises
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doSideStretches() {
		perform( Cycle.DO_SIDE_STRETCH  );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void touchToes() {
		perform( Cycle.TOUCH_TOES );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doJumpingJack() {
		perform( Cycle.DO_JUMPING_JACK );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void doKneeBend() {
		perform( Cycle.DO_KNEE_BEND );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void jogInPlace() {
		perform( Cycle.JOG_IN_PLACE );
	}
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void wave() {
		perform( Child.Cycle.WAVE );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.math.AxisAlignedBox getLocalAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		final double X = 0.1375;
		final double Y = 1.20;
		final double Z = 0.131;
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: Child getLocalAxisAlignedMinimumBoundingBox" );
		rv.setMinimum( -X, 0.0, -Z );
		rv.setMaximum( +X,   Y, +Z );
		return rv;
	}	
	
}

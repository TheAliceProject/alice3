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
package org.lgna.story.ast;

import org.lgna.project.ast.JavaMethod;
import org.lgna.story.AddCollisionEndListener;
import org.lgna.story.AddCollisionStartListener;
import org.lgna.story.AddKeyPressListener;
import org.lgna.story.AddMouseClickOnObjectListener;
import org.lgna.story.AddMouseClickOnScreenListener;
import org.lgna.story.AddOcclusionEndListener;
import org.lgna.story.AddOcclusionStartListener;
import org.lgna.story.AddProximityEnterListener;
import org.lgna.story.AddProximityExitListener;
import org.lgna.story.AddTimeListener;
import org.lgna.story.AddViewEnterListener;
import org.lgna.story.AddViewExitListener;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;

/**
 * @author Dennis Cosgrove
 */
public class EventListenerMethodUtilities {
	public static final JavaMethod ADD_KEY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addKeyPressListener",
			KeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod MOVE_WITH_ARROWS = JavaMethod.getInstance(
			SScene.class,
			"addObjectMoverFor",
			SMovableTurnable.class );
	public static final JavaMethod ADD_ARROW_KEY_PRESS_LISTENER = JavaMethod.getInstance(
			SScene.class,
			"addArrowKeyPressListener",
			ArrowKeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod ADD_NUNBER_KEY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addNumberKeyPressListener",
			NumberKeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final java.util.List<JavaMethod> ALL_KEYBOARD_EVENT_METHODS = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( ADD_KEY_LISTENER_METHOD, ADD_ARROW_KEY_PRESS_LISTENER, ADD_NUNBER_KEY_LISTENER_METHOD, MOVE_WITH_ARROWS );

	public static final JavaMethod ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addMouseClickOnScreenListener",
			MouseClickOnScreenListener.class, AddMouseClickOnScreenListener.Detail[].class );
	public static final JavaMethod ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addMouseClickOnObjectListener",
			MouseClickOnObjectListener.class, AddMouseClickOnObjectListener.Detail[].class );
	public static final JavaMethod ADD_DEFAULT_MODEL_MANIPULATOR = JavaMethod.getInstance(
			SScene.class,
			"addDefaultModelManipulation" );
	public static final java.util.List<JavaMethod> ALL_MOUSE_CLICK_EVENT_METHODS = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD, ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD, ADD_DEFAULT_MODEL_MANIPULATOR );

	public static final JavaMethod ADD_SCENE_ACTIVATION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addSceneActivationListener",
			SceneActivationListener.class );
	public static final JavaMethod ADD_TIMER_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addTimeListener",
			TimeListener.class, Number.class, AddTimeListener.Detail[].class );

	public static final JavaMethod ADD_TRANSFORMATION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addPointOfViewChangeListener",
			PointOfViewChangeListener.class, SThing[].class );
	public static final JavaMethod ADD_START_COLLISION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addCollisionStartListener",
			CollisionStartListener.class, SThing[].class, SThing[].class, AddCollisionStartListener.Detail[].class );
	public static final JavaMethod ADD_END_COLLISION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addCollisionEndListener",
			CollisionEndListener.class, SThing[].class, SThing[].class, AddCollisionEndListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addProximityEnterListener",
			ProximityEnterListener.class, SThing[].class, SThing[].class, Number.class, AddProximityEnterListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addProximityExitListener",
			ProximityExitListener.class, SThing[].class, SThing[].class, Number.class, AddProximityExitListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addViewEnterListener",
			ViewEnterListener.class, SModel[].class, AddViewEnterListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addViewExitListener",
			ViewExitListener.class, SModel[].class, AddViewExitListener.Detail[].class );
	public static final JavaMethod ADD_START_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addOcclusionStartListener",
			OcclusionStartListener.class, SModel[].class, SModel[].class, AddOcclusionStartListener.Detail[].class );
	public static final JavaMethod ADD_END_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addOcclusionEndListener",
			OcclusionEndListener.class, SModel[].class, SModel[].class, AddOcclusionEndListener.Detail[].class );

}

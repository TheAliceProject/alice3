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
package org.alice.stageide.personeditor;

import org.alice.apis.stage.BaseEyeColor;
import org.alice.apis.stage.BaseSkinTone;
import org.alice.apis.stage.FullBodyOutfit;
import org.alice.apis.stage.Gender;
import org.alice.apis.stage.Hair;
import org.alice.apis.stage.LifeStage;

/**
 * @author Dennis Cosgrove
 */
class RandomPersonActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public RandomPersonActionOperation() {
		super( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP );
		this.putValue( javax.swing.Action.NAME, "Generate Random Selection" );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> prevState = PersonViewer.getSingleton().getState();
		final edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> nextState = PersonViewer.generateRandomState();
		actionContext.commitAndInvokeRedoIfAppropriate( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
			@Override
			public void doOrRedo() {
				PersonViewer.getSingleton().setState( nextState );
			}
			@Override
			public void undo() {
				PersonViewer.getSingleton().setState( prevState );
			}
		} );
	}
}

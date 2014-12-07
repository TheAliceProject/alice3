package org.alice.netbeans.palette.items;

import javax.swing.text.JTextComponent;
import org.alice.netbeans.palette.items.views.CountLoopCustomizer;

public class CountLoop extends AbstractActiveEditorDrop {

    private String counterVariableName;
	@Override
	protected boolean prologue(JTextComponent targetComponent) {
		CountLoopCustomizer countLoopCustomizer = new CountLoopCustomizer(targetComponent);
		if( countLoopCustomizer.showDialog() ) {
			this.counterVariableName = countLoopCustomizer.getVariableName();
		} else {
			this.counterVariableName = null;
		}
		return this.counterVariableName != null;
	}

	@Override
	protected String epilogue(String source) {
		return source.replaceAll(" i", " " + this.counterVariableName);
	}
}

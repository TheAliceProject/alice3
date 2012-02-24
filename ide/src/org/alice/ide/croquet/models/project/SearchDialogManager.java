package org.alice.ide.croquet.models.project;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Hyperlink;
import org.lgna.croquet.components.Label;

import edu.cmu.cs.dennisc.java.util.Collections;

public class SearchDialogManager implements ValueListener<String> {


	private List<Hyperlink> parentList = Collections.newLinkedList();
	private HashMap<Label, Hyperlink> parentMap = Collections.newHashMap();
	private HashMap<Hyperlink, LinkedList< Label > > childMap = Collections.newHashMap();
	
	public void changing(State<String> state, String prevValue, String nextValue, boolean isAdjusting) {}

	public void changed(State<String> state, String prevValue, String nextValue, boolean isAdjusting) {
		for( Hyperlink parent : parentList) {
			for( Label child : childMap.get( parent ) ) {
				if (!child.getText().toLowerCase().contains(state.getValue().toLowerCase())){
					hide(child);
				} else {
					show(child);
				}
			}
		}
	}

	private void show(Label label) {
		label.setVisible( true );
	}

	private void hide(Label label) {
		label.setVisible( false );
	}

	public void addParentWithChildren(Hyperlink parent, LinkedList<Label> children) {
		parentList.add( parent );
		childMap.put( parent, children );
		for( Label child : children ) {
			parentMap.put( child, parent );
		}
	}
}

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
package org.alice.stageide.sceneeditor.viewmanager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;

/**
 * @author David Culyba
 */
public class CameraViewSelector extends JPanel implements ItemListener, ActionListener{
	
	protected MoveAndTurnSceneEditor sceneEditor;
	protected JComboBox cameraViewComboBox;
	protected List<CameraFieldAndMarker> fieldBasedOptions = new LinkedList<CameraFieldAndMarker>();
	protected List<CameraFieldAndMarker> extraOptions = new LinkedList<CameraFieldAndMarker>();
	protected org.alice.apis.moveandturn.CameraMarker activeMarker;
	
	public CameraViewSelector(MoveAndTurnSceneEditor sceneEditor, List<CameraFieldAndMarker> extraOptions)
	{
		super();
		this.setOpaque(false);
		this.setBorder(null);
		this.sceneEditor = sceneEditor;
		this.cameraViewComboBox = new JComboBox();
		this.add(this.cameraViewComboBox);
		this.startListening();
		if (extraOptions != null)
		{
			this.extraOptions.addAll(extraOptions);
		}
		refreshFields();
	}
	
	public CameraViewSelector(MoveAndTurnSceneEditor sceneEditor)
	{
		this(sceneEditor, null);
	}
	
	public void setExtraOptions(List<CameraFieldAndMarker> options)
	{
		this.extraOptions.clear();
		if (options != null)
		{
			this.extraOptions.addAll(options);
			refreshFields();
		}
	}
	
	public void setExtraMarkerOptions(List<CameraMarker> markers)
	{
		this.extraOptions.clear();
		if (markers != null)
		{
			for (CameraMarker marker : markers)
			{
				this.extraOptions.add(new CameraFieldAndMarker(null, marker));
			}
			refreshFields();
		}
	}
	
	public void refreshFields()
	{
		CameraFieldAndMarker oldSelected = this.getSelectedMarker();
		
		fieldBasedOptions.clear();
		
		int originalSelectedIndex = this.cameraViewComboBox.getSelectedIndex();
		//Stop listening to changed on the combobox while we reorder stuff
		this.stopListening();
		
		this.cameraViewComboBox.removeAllItems();
		List<AbstractField> declaredFields = this.sceneEditor.getDeclaredFields();
		for( AbstractField field : declaredFields) 
		{
			if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class ))
			{
				fieldBasedOptions.add( new CameraFieldAndMarker(field, getMarkerForField(field)) );
			}
		}
		//Populate the combobox with the elements
		for (CameraFieldAndMarker view : this.fieldBasedOptions)
		{
			this.cameraViewComboBox.addItem( view.toString() );
		}
		for (CameraFieldAndMarker extraView : this.extraOptions)
		{
			this.cameraViewComboBox.addItem( extraView.toString() );
		}
		
		int newCount = this.cameraViewComboBox.getItemCount();
		if (originalSelectedIndex == -1 && newCount > 0)
		{
			this.cameraViewComboBox.setSelectedIndex(0);
		}
		else
		{
			this.cameraViewComboBox.setSelectedIndex( originalSelectedIndex );
		}
		
		//Start listening again
		this.startListening();
	}
	

	public CameraFieldAndMarker getSelectedMarker()
	{
		return getCameraAndMarkerForIndex(this.cameraViewComboBox.getSelectedIndex());
	}
	
	public org.alice.apis.moveandturn.CameraMarker getActiveMarker()
	{
		return this.activeMarker;
	}
	
	public int getIndexForField(AbstractField field)
	{
		int index = 0;
		for (CameraFieldAndMarker fieldAndMarker : this.fieldBasedOptions)
		{
			if (fieldAndMarker.field == field)
			{
				return index;
			}
			index++;
		}
		for (CameraFieldAndMarker fieldAndMarker : this.extraOptions)
		{
			if (fieldAndMarker.field == field)
			{
				return index;
			}
			index++;
		}
		return -1;
	}
	
	private CameraMarker getMarkerForField(AbstractField field)
	{
		CameraMarker marker = this.sceneEditor.getInstanceInJavaForField(field, org.alice.apis.moveandturn.CameraMarker.class);
		return marker;
	}
	
	public int getIndexForCameraFieldAndMarker( CameraFieldAndMarker cfAm )
	{
		int index = 0;
		for (CameraFieldAndMarker option : this.fieldBasedOptions)
		{
			if (option == cfAm)
			{
				return index;
			}
			index++;
		}
		for (CameraFieldAndMarker option : this.extraOptions)
		{
			if (option == cfAm)
			{
				return index;
			}
			index++;
		}
		return -1;
	}
	
	private CameraFieldAndMarker getCameraAndMarkerForIndex(int index)
	{
		try
		{
			if (index > -1)
			{
				if (index < this.fieldBasedOptions.size())
				{
					return this.fieldBasedOptions.get(index);
				}
				else
				{
					int orthoIndex = index - this.fieldBasedOptions.size();
					return this.extraOptions.get(orthoIndex);
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private CameraMarker getMarkerForIndex(int index)
	{
		CameraFieldAndMarker cfAm = getCameraAndMarkerForIndex(index);
		if (cfAm != null)
		{
			return cfAm.marker;
		}
		return null;
	}
	
	public void setSelectedView(AbstractField field)
	{
		int index = getIndexForField(field);
		if (index != -1)
		{
			this.cameraViewComboBox.setSelectedIndex(index);
		}
	}
	
	public void setSelectedIndex(int index)
	{
		if (index != -1)
		{
			this.cameraViewComboBox.setSelectedIndex(index);
		}
	}
	
	public int getSelectedIndex()
	{
		return this.cameraViewComboBox.getSelectedIndex();
	}
	
	public boolean isListening()
	{
		for (ItemListener listener : this.cameraViewComboBox.getItemListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		return false;
	}
	
	public void startListening()
	{
		this.cameraViewComboBox.addItemListener(this);
		this.cameraViewComboBox.addActionListener(this);
	}
	
	public void stopListening()
	{
		this.cameraViewComboBox.removeItemListener(this);
		this.cameraViewComboBox.removeActionListener(this);
	}
	
	public void doSetSelectedView(int index)
	{
		this.activeMarker = getMarkerForIndex(index);
//		System.out.println("Set activeMarker to index "+index+", "+this.activeMarker.getName());
//		if (this.activeMarker == null)
//		{
//			this.activeMarker = getMarkerForIndex(index);
//		}
		assert this.activeMarker != null;
	}

	public void itemStateChanged( ItemEvent e ) {
		if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) 
		{
			this.doSetSelectedView( this.cameraViewComboBox.getSelectedIndex() );
		}
	}

	public void actionPerformed(ActionEvent e) {
		this.doSetSelectedView( this.cameraViewComboBox.getSelectedIndex() );
	}


}

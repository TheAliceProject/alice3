import java
import javax
import edu
from edu.cmu.cs.dennisc import lookingglass

import ecc

class AbstractButton( lookingglass.overlay.AbstractButton ):
	def __init__( self ):
		lookingglass.overlay.AbstractButton.__init__( self )
		self.setRenderer( self._getRenderer() )
		self.setHighlighted( False )
	def _getRenderer(self):
		raise "Override"
	def setHighlighted( self, isHighlighted ):
		lookingglass.overlay.AbstractButton.setHighlighted( self, isHighlighted )
		if isHighlighted:
			opacity = 1.0
		else:
			opacity = 0.5
		self.setOpacity( opacity )

class OverlayButton( AbstractButton ):
	def __init__( self, text ):
		self._renderer = lookingglass.overlay.DefaultButtonRenderer()
		self._renderer.setText( text )
		AbstractButton.__init__( self )
	def _getRenderer(self):
		return self._renderer

class OverlayList( lookingglass.overlay.Composite, lookingglass.overlay.Group ):
	def __init__( self ):
		lookingglass.overlay.Composite.__init__( self )
		self._listSelectionListeners = []
		self._popupCallbacks = []
		self._data = []
		self._mapDatumToControl = {}
		self._mapViewToDatum = {}
		self._mapControlToActionListener = {}
		self._mapControlToPopupListener = {}
		self._selectedValue = None

	def getGroupComponents(self):
		return self.getComponents()

	def addListSelectionListener(self, l):
		self._listSelectionListeners.append( l )
	def removeListSelectionListener(self, l):
		self._listSelectionListeners.remove( l )
	def getListSelectionListeners(self):
		return self._listSelectionListeners
	def _fireListSelectionListeners(self, e):
		firstIndex = self._data.index( self._selectedValue )
		lastIndex = firstIndex
		isAdjusting = False
		e = ecc.dennisc.lookingglass.overlay.event.ListSelectionEvent( self, firstIndex, lastIndex, isAdjusting, e )
		for l in self._listSelectionListeners:
			l.valueChanged( e )
	
	#todo
	def addPopupCallback(self, l):
		self._popupCallbacks.append( l )
	def removePopupCallback(self, l):
		self._popupCallbacks.remove( l )
	def getPopupCallbacks(self):
		return self._popupCallbacks
	def _firePopupCallbacks(self, mouseEvent, datum):
		for l in self._popupCallbacks:
			l( mouseEvent, datum )

	def _addDatum( self, datum ):
		control = self._createControl( datum )
		self._mapControlToActionListener[ control ] = ecc.dennisc.swing.event.ActionAdapter( self._handleAction )
		self._mapControlToPopupListener[ control ] = ecc.dennisc.swing.event.ActionAdapter( self._handlePopup, datum )
		control._view.addActionListener( self._mapControlToActionListener[ control ] )
		control.addPopupListener( self._mapControlToPopupListener[ control ] )
		control.setGroup( self )
		self._mapDatumToControl[ datum ] = control
		self._mapViewToDatum[ control._view ] = datum
		self.addComponent( control )
		self._data.append( datum )
		
	def _removeDatum(self, datum ):
		control = self._mapDatumToControl[ datum ]
		control._view.removeActionListener( self._mapControlToActionListener[ control ] )
		control.removePopupListener( self._mapControlToPopupListener[ control ] )
		del self._mapControlToActionListener[ control ]
		del self._mapControlToPopupListener[ control ]
		control.setGroup( None )
		self.removeComponent( control )
		del self._mapDatumToControl[ datum ]
		del self._mapViewToDatum[ control._view ]
		self._data.remove( datum )
		
	def setListData(self, data):
		self.setSelectedValue( None )
		prevData = self._data[:]
		for datum in prevData:
			self._removeDatum(datum)
		for datum in data:
			self._addDatum(datum)

	def getSelectedValue(self):
		return self._selectedValue
	
	def setSelectedValue(self, datum):
		self._selectedValue = datum
		if self._selectedValue:
			control = self._mapDatumToControl[ datum ]
			mouseEvent = None
			for component in self.getGroupComponents():
				component.setSelected( component == control )
			self._fireListSelectionListeners( mouseEvent )

	def _handleAction( self, e ):
		view = e.getSource()
		self._selectedValue = self._mapViewToDatum[ view ]
		self._fireListSelectionListeners( e )
	def _handlePopup( self, e, datum ):
		self._firePopupCallbacks( e.getMouseEvent(), datum )

	def _createControl(self, datum):
		raise "Override"

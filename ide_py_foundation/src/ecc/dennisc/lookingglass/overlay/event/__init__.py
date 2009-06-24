import javax

class ListSelectionEvent( javax.swing.event.ListSelectionEvent ):
	def __init__(self, source, firstIndex, lastIndex, isAdjusting, actionEvent ):
		javax.swing.event.ListSelectionEvent.__init__( self, source, firstIndex, lastIndex, isAdjusting )
		self._actionEvent = actionEvent
	def getActionEvent(self):
		return self._actionEvent
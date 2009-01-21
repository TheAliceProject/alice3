#print "-->", __name__

import java
import javax
import edu
from edu.cmu.cs.dennisc import zoot
from edu.cmu.cs.dennisc import alice

import ecc

class TabCloseOperation( zoot.ActionOperation ):
	def getActionForConfiguringSwingComponents(self):
		return None
	def prepare(self, e, observer):
		self._tabbedPane = e.getSource()
		self._index = e.getIndex()
		return zoot.Operation.PreparationResult.PERFORM
	def perform(self):
		scrollPane = self._tabbedPane.getComponentAt( self._index )
		self._tabbedPane.remove( self._index )
		self._tabbedPane.handleRemove( scrollPane )

class CodeTabbedPane( zoot.ZTabbedPane ):
	def __init__( self ):
		zoot.ZTabbedPane.__init__( self, TabCloseOperation() )
		self._blockStatementViews = None
		self._potentialReceptors = None
		self._mapCodeToScrollPane = {}

	def isCloseButtonDesiredAt(self, index):
		return index > 0

	def addScrollableTab( self, title, pane, isSelectionDesired ):
		color = pane.getBackground()

		scrollPane = javax.swing.JScrollPane( pane )
		scrollPane.setBackground( color )
		scrollPane.setBorder( None )
		
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 )
		#tab = edu.cmu.cs.dennisc.swing.Tab( title, scrollPane )
		
		text = ""
		edu.cmu.cs.dennisc.swing.SwingUtilities.doLayout( title )
		icon = edu.cmu.cs.dennisc.swing.SwingUtilities.createIcon( title, pane )
		self.addTab( text, icon, scrollPane )
		
		if isSelectionDesired:
			self._doNotSetIDE = True
			#self.selectTab( tab )
			index = self.indexOfComponent( scrollPane )
			self.setSelectedIndex( index )
			del self._doNotSetIDE

#		return tab
		return scrollPane
	
	def _getCodeEditorForScrollPane(self, scrollPane):
		if scrollPane:
			return scrollPane.getViewport().getView()
		else:
			return None
	def getSelectedCodeEditor(self):
		scrollPane = self.getSelectedComponent()
		return self._getCodeEditorForScrollPane( scrollPane )

	def _edit( self, code ):
		if self._mapCodeToScrollPane.has_key( code ):
			scrollPane = self._mapCodeToScrollPane[ code ]
			self.setSelectedComponent( scrollPane )
		else:
			from CodePane import CodePane
			codePane = CodePane( code )
			titlePane = alice.ide.editors.code.CodeTitlePane( code )
			scrollPane = self.addScrollableTab( titlePane, codePane, isSelectionDesired=True )
			self._mapCodeToScrollPane[ code ] = scrollPane
		self.revalidate()

	def handleRemove(self, scrollPane):
		codeEditor = self._getCodeEditorForScrollPane( scrollPane )
		code = codeEditor.getCode()
		del self._mapCodeToScrollPane[ code ]
		self.fireStateChanged()

	#todo: investogate if necessary
	def clearTabs( self ):
		self.removeAll()
		self._mapCodeToScrollPane.clear()

#print "<--", __name__

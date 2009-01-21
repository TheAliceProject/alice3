#print "-->", __name__

import java
import javax
import edu
from edu.cmu.cs.dennisc import zoot
from edu.cmu.cs.dennisc import alice

import ecc

class TabCloseOperation( zoot.ActionOperation ):
	def prepare(self, e, observer):
		self._tabbedPane = e.getSource()
		return zoot.Operation.PreparationResult.PERFORM
	def perform(self):
		print "todo: handle close", self._tabbedPane
	def getActionForConfiguringSwingComponents(self):
		return None

class MyTabbedPane( zoot.ZTabbedPane ):
	def __init__( self ):
		tabSelectionOperation = None
		tabCloseOperation = TabCloseOperation()
		zoot.ZTabbedPane.__init__( self, tabSelectionOperation, tabCloseOperation )
		self._blockStatementViews = None
		self._potentialReceptors = None
		self._mapCodeToScrollPane = {}

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
	
	def getPaneForTab( self, tab ):
		return tab.getTabPane().getViewport().getView()

	def getSelectedCodeEditor(self):
		scrollPane = self.getSelectedComponent()
		if scrollPane:
			return scrollPane.getViewport().getView()
		else:
			return None

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

#	def tabSelected( self, e ):
#		print "todo: zoot.ZTabbedPane.tabSelected( self, e )"
#		if self.__dict__.has_key( "_doNotSetIDE" ):
#			pass
#		else:
#			codePane = self.getPaneForTab( e.getSource() )
#			code = codePane.getCode()
#			self._doNotSetIDE = True
#			alice.ide.IDE.getSingleton().setFocusedCode( code )
#			del self._doNotSetIDE
#		
#	def tabClosed( self, e ):
#		print "todo: zoot.ZTabbedPane.tabClosed( self, e )"
#		codePane = self.getPaneForTab( e.getSource() )
#		code = codePane.getCode()
#		del self._mapCodeToScrollPane[ code ]

	#todo: investogate if necessary
	def clearTabs( self ):
		self.removeAll()
		self._mapCodeToScrollPane.clear()


class CodeRootPane( ecc.dennisc.alice.ide.IDEListenerPane ):
	def __init__( self ):
		ecc.dennisc.alice.ide.IDEListenerPane.__init__( self )
		self._codeTabbedPane = self._createCodeTabbedPane()
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._codeTabbedPane, java.awt.BorderLayout.CENTER )
		#self.add( southPane, java.awt.BorderLayout.SOUTH )

	def getCodeEditorInFocus( self ):
		return self._codeTabbedPane.getSelectedCodeEditor()
	
#	def _createCodeTabbedPane( self ):
#		from CodeTabbedPane import CodeTabbedPane
#		return CodeTabbedPane()
	
	def _createCodeTabbedPane( self ):
		return MyTabbedPane()

	def focusedCodeChanged( self, e ):
		code = e.getNextValue()
		#javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable(self._codeTabbedPane._edit, ( code, )) )
		self._codeTabbedPane._edit( code )

	def projectOpening( self, e ):
		self._codeTabbedPane.clearTabs()
		

#print "<--", __name__

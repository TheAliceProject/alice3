#print "-->", __name__

import java
import javax

import ecc

class CodeRootPane( ecc.dennisc.alice.ide.IDEListenerPane ):
	def __init__( self ):
		ecc.dennisc.alice.ide.IDEListenerPane.__init__( self )
		self._codeTabbedPane = self._createCodeTabbedPane()
		#self._ubiquitousTemplatesPane = self._createUbiquitousTemplatesPane()
		#self._languageTogglePane = self._createLanguageTogglePane()
		
		#southPane = javax.swing.JPanel()
		#southPane.setLayout( java.awt.BorderLayout( 16, 0 ) )
		#southPane.add( self._languageTogglePane, java.awt.BorderLayout.WEST )
		#southPane.add( self._ubiquitousTemplatesPane, java.awt.BorderLayout.CENTER )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._codeTabbedPane, java.awt.BorderLayout.CENTER )
		#self.add( southPane, java.awt.BorderLayout.SOUTH )

	def getCodeEditorInFocus( self ):
		tabbedPane = self._codeTabbedPane
		tab = tabbedPane.getSelectedTab()
		return tab.getTabPane().getViewport().getView()
	
	def _createCodeTabbedPane( self ):
		from CodeTabbedPane import CodeTabbedPane
		return CodeTabbedPane()
#	def _createUbiquitousTemplatesPane(self):
#		from UbiquitousTemplatesPane import UbiquitousTemplatesPane
#		return UbiquitousTemplatesPane()
#	def _createLanguageTogglePane(self):
#		from LanguageTogglePane import LanguageTogglePane
#		return LanguageTogglePane()
	
#	def refreshVariableTemplates(self):
#		self._ubiquitousTemplatesPane.refreshVariableTemplates()

	def focusedCodeChanged( self, e ):
		code = e.getNextValue()
		self._codeTabbedPane._edit( code )

	def projectOpening( self, e ):
		self._codeTabbedPane.clearTabs()
		

#print "<--", __name__

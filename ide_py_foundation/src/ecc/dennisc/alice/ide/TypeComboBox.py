#print "-->", __name__

import java
import javax
import edu


from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt

import ecc

_mapTypeToIcon = {}
def _getIconForType( type ):
	if _mapTypeToIcon.has_key( type ):
		rv = _mapTypeToIcon[ type ]
	else:
		component = alice.ide.editors.common.TypePane( type )
		rv = edu.cmu.cs.dennisc.swing.SwingUtilities.createIcon( component, ecc.dennisc.alice.ide.getIDE() )
		_mapTypeToIcon[ type ] = rv
	return rv

class TypeComboBoxModel( javax.swing.DefaultComboBoxModel ):
	def __init__(self):
		javax.swing.DefaultComboBoxModel.__init__( self )
		for type in alice.ide.IDE.getSingleton().getAvailableTypes():
			self.addElement( type )

class TypeListCellRenderer( javax.swing.DefaultListCellRenderer ):
	def __init__( self, inset=2 ):
		javax.swing.DefaultListCellRenderer.__init__( self )
		self._border = javax.swing.border.EmptyBorder( inset, inset, inset, inset )
		
	def getListCellRendererComponent( self, list, value, index, isSelected, isFocus ):
		rv = javax.swing.DefaultListCellRenderer.getListCellRendererComponent( self, list, value, index, isSelected, isFocus )
		rv.setText( alice.ide.IDE.getSingleton().getBonusTextForType( value ) )
		rv.setIcon( _getIconForType( value ) )
		return rv

class TypeComboBox( javax.swing.JComboBox ):
	def __init__(self):
		javax.swing.JComboBox.__init__( self, TypeComboBoxModel() )
		self.setRenderer( TypeListCellRenderer() )

#print "<--", __name__

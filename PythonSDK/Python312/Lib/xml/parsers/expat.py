"""Interface to the Expat non-validating XML parser."""
import sys

from pyexpat import *

# provide pyexpat submodules as xml.parsers.expat submodules
sys.modules['xml.parsers.expat.dao'] = dao
sys.modules['xml.parsers.expat.errors'] = errors

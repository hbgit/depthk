#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# DepthK v1.0
# by Herbert Rocha
#
# e-mail: herberthb12@gmail.com
# -------------------------------------------------

from __future__ import print_function


#Python
import sys
import os
import re

# From PycParser
import pycparser.c_parser
import pycparser.c_ast
from pycparser.c_ast import *
import pycparser.c_generator


#### Gather the absolute path
sys.path.append(os.path.dirname(__file__))


#### Gather the absolute path
sys.path.append(os.path.dirname(__file__))


# -------------------------------------------------
# Global Variables
# Portable cpp path for Windows and Linux/Unix
CPPPATH = 'utils/cpp.exe' if sys.platform == 'win32' else 'cpp'
ABS_PATH = os.path.dirname(__file__)
# -------------------------------------------------


class IdentifyVarDecl(NodeVisitor):

    def __init__(self):
        self.currenttype = ""
        self.datafromdecl = {}

    @staticmethod
    def getnumberofline(nodecoord):
        txt = str(nodecoord)
        matchNumLine = re.search(r'(.[^:]+)$', txt)
        if matchNumLine:
            onlyNumber = matchNumLine.group(1).replace(":","")
            return onlyNumber

    def visit_Decl(self, node):
        if type(node.type) == TypeDecl:
            self.datafromdecl[node.name] = [node.type.type.names,self.getnumberofline(node.coord)]
            #print(node.name,node.type.type.names,self.getnumberofline(node.coord))



class RunAstDecl(object):
    def __init__(self, _cfilepath):
        self.cfilepath = _cfilepath


    def identify_decl(self):

        path_cpp_args = os.path.join(os.path.dirname(__file__), "utils/fake_libc_include")
        ast = pycparser.parse_file(self.cfilepath, use_cpp=True, cpp_path=CPPPATH, cpp_args=r'-I'+path_cpp_args)
        #ast.show()
        #sys.exit()

        vi = IdentifyVarDecl()
        vi.visit(ast)
        #print(vi.datafromdecl)
        return vi.datafromdecl






if __name__ == "__main__":
    if len(sys.argv) > 1:
        filename = sys.argv[1]
        r = RunAstDecl(filename)
        r.identify_decl()



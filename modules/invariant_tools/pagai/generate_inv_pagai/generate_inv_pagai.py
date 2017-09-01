#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# Class to generate PAGAI program invariants
# by Herbert Rocha
#
# e-mail: herberthb12@gmail.com
# -------------------------------------------------

from __future__ import print_function

import re
import os
import sys
import commands

class GeneratePagaiInv(object):
    def __init__(self):
        self.pathprogram = ""
        self.pathprograminv = ""
        #self.compilescript = os.path.dirname(os.path.abspath(__file__)) + "/compile_llvm.sh"
        self.compilecmd = "clang-3.5 -fsanitize=undefined -fsanitize=local-bounds -Wno-return-type " \
                          "-emit-llvm -Wno-implicit-function-declaration -Wno-parentheses-equality -I . -g -c "
        self.opcompscript = " -g -i "
        self.pathpagai = os.path.dirname(os.path.abspath(__file__)) + "/pagai"
        self.currentpath = os.getcwd()


    def generate_inv(self, __pathprogram):
        self.pathprogram = __pathprogram
        #os.system("cat "+ __pathprogram)
        # get path file to compile in the file dir
        dircprogram = os.path.dirname(os.path.abspath(__pathprogram))

        # Compile program to LLVM
        nameprogram = os.path.basename(__pathprogram)
        if nameprogram.endswith(".i"):
            filecompiled = nameprogram.replace(".i",".bc")
            self.pathprograminv = nameprogram.replace(".i","_pagai_inv.i")
            self.pathprograminv = dircprogram + "/" + self.pathprograminv
        else:
            filecompiled = nameprogram.replace(".c",".bc")
            self.pathprograminv = nameprogram.replace(".c","_pagai_inv.c")
            self.pathprograminv = dircprogram + "/" + self.pathprograminv

        # acessing the file dir
        os.chdir(dircprogram)

        # compile to llvm
        #print(self.compilecmd + " " + nameprogram + " -o " + filecompiled)
        #os.system(self.compilecmd + " " + nameprogram
        #                            + " -o " + filecompiled)
        #sys.exit()
        result = commands.getoutput(self.compilecmd + " " + nameprogram
                                    + " -o " + filecompiled)

        # abs path to compiled file
        filecompiled = dircprogram + "/" + filecompiled

        # Going back to current package
        os.chdir(self.currentpath)

        # error:
        matchcompileerror = re.search(r'error:', result)
        if not matchcompileerror:
            os.system("opt-3.5 -mem2reg -inline -lowerswitch -loops  -loop-simplify -loop-rotate -lcssa -loop-unroll -unroll-count=1 " +
                      filecompiled + " -o " +  filecompiled)
            #os.system("opt -mem2reg -lowerswitch " + filecompiled + " -o " +  filecompiled)

            #os.system(self.pathpagai + " -i " + filecompiled)
            #sys.exit()
            #resultrun = commands.getoutput("timeout --signal=9 8m " + self.pathpagai + " -i " + filecompiled
            #                               + " --no-undefined-check --skipnonlinear -s z3 --annotated " + self.pathprograminv)
            resultrun = commands.getoutput("timeout --signal=9 8m " + self.pathpagai + " -i " + filecompiled
                                           + " --annotated " + self.pathprograminv)

            # Validating result
            os.remove(filecompiled)
            matchrunerror = re.search(r'error generated\.', resultrun)
            if matchrunerror:
                print("ERROR. Generating program invariants")
                self.pathprograminv = ""
                return self.pathprograminv
            else:
                #print(resultrun)
                #commands.getoutput("echo " + resultrun + " >> " + self.pathprograminv)
                return self.pathprograminv


#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# DepthK v1.0
# by Herbert Rocha
#
# e-mail: herberthb12@gmail.com
#
#
# The input should be .i already preprocessed by CIL
# -------------------------------------------------

from __future__ import print_function

import argparse
import sys
import os
import commands
import re
from pipes import quote


# From project
from modules.run_ast import ast
from modules.invariant_tools.pips.translate_pips import translate_pips
from modules.bmc_check import esbmccheck




# TODO:
# Precondition to usage:
#   - all variables should be declared in top of the function [todo]
#   - it is necessary to generate the invariante to the code
# (1) [DONE] Identify each variable and it type
# (2) [DONE] Read the code.pips identify #init, save the VAR#init and the function where it was find
# (3) [DONE] write new code where for each function identified in (2) we should add the new vars to VAR#init
# (4) [DONE] Read the code.pips and translate the annotations to __ESBMC. WARNNING: replace VAR#init to VAR_init
# (5) Checking the new code with ESBMC k-induction
# (6) From counterexample added a new ASSUME in the code



class DepthK(object):

    def __init__(self, _cfilepath):
        self.cfilepath = _cfilepath
        self.inputisexti = False
        self.listnumbeginfunc = []
        self.nameoforicprogram = os.path.basename(_cfilepath)
        self.esbmcpath = ''
        self.countMAXtry = 10



    def identify_initpips(self, _cfilepath):
        """
        Read the code.pips identify #init, save the VAR#init and the function
        where it was find
        :param _cfilepath:
        :return:
        """

        # result
        dict_varinitandloc = {}

        # Get the number line where each function begin
        self.listnumbeginfunc = self.getnumbeginfuncts(_cfilepath)
        if not self.listnumbeginfunc:
            print("ERROR. Identifying functions")
            sys.exit()

        filec = open(_cfilepath, "r")
        linescfile = filec.readlines()
        filec.close()

        currentnumfunct = 0
        lastnumfunct = 0
        listsavevar_tmp = []
        flaginit = False
        flagchangefunc = False
        countfunct = 0

        for index, line in enumerate(linescfile):

            # identify the function that has been analyzed
            # How identify that we change of function
            if (index + 1) in self.listnumbeginfunc:
                countfunct += 0

                currentnumfunct = index + 1


                # we create a entry for each function
                # where each line function number receive a list
                # with the variables init identified
                dict_varinitandloc[currentnumfunct] = []


                # if flaginit:
                #     flaginit = False
                #     listsavevar_tmp = list(set(listsavevar_tmp))
                #     print(lastnumfunct, listsavevar_tmp)
                #     listsavevar_tmp = []
                #     #dict_varinitandloc[lastnumfunct] = listsavevar_tmp
                # else:
                #     lastnumfunct = currentnumfunct
                #     listsavevar_tmp = []
                #dict_varinitandloc[lastnumfunct] = []


            #elif countfunct > 0:


            # check if is a comment
            matchpipscommt = re.search(r'^//[ ]+', line)
            if matchpipscommt:

                matchpipsinit = re.findall(r'([a-zA-Z0-9_]+)#init', line)
                if matchpipsinit:
                    flaginit = True
                    #print(matchpipsinit," ---- FUNCT: ", currentnumfunct)
                    # remove variables duplicated
                    listcleanvar = list(set(matchpipsinit))
                    for var in listcleanvar:
                        #listsavevar_tmp.append(var)
                        dict_varinitandloc[currentnumfunct].append(var)

                    dict_varinitandloc[currentnumfunct] = list(set(dict_varinitandloc[currentnumfunct]))


        # removing key without values
        for key, value in dict_varinitandloc.items():
            if not value:
                del dict_varinitandloc[key]

        # for key, value in dict_varinitandloc.items():
        #     print(key, value)
        #     print("")

        #sys.exit()
        return dict_varinitandloc



    def generatecodewithinit(self, _cfilepath, _dicvarinitandloc):
        """
        write new code where for each function identified in (2) we should add
        the new vars to VAR#init

        :param _cfilepath:
        :param _dicvarinitandloc:
        :return: pathcodewithinit
        """

        # generating path to save this new code generated
        pathcodewithinit = "/tmp/" + str(os.path.basename(_cfilepath).replace(".c","_init.c"))
        filewithinit = open(pathcodewithinit, "w")


        flag_initpips = True
        if not _dicvarinitandloc:
            flag_initpips = False


        # Get lines from C file
        filec = open(_cfilepath,"r")
        linescfile = filec.readlines()
        filec.close()

        # Get variable data
        r = ast.RunAstDecl(_cfilepath)
        # output: {'z_val': [['int'], '164']}
        dict_varsdata = r.identify_decl()


        count = 0
        while count < len(linescfile):

            #print(linescfile[count],end="")
            filewithinit.write(linescfile[count])

            nextline = count + 1
            if nextline in self.listnumbeginfunc:
                # print delimiter
                #print(linescfile[nextline],end="")
                filewithinit.write(linescfile[nextline])
                count = nextline

                if _dicvarinitandloc.has_key(nextline):
                    for var in _dicvarinitandloc[nextline]:
                        #print("INIT: ", var)
                        # Creating INIT vars
                        #print("INIT: ", dict_varsdata[var][0], var+"_init")
                        nametype = ' '.join(dict_varsdata[var][0])
                        #print(nametype+str(" " + var + "_init = " + var + ";"))
                        filewithinit.write(nametype+str(" " + var + "_init = " + var + "; \n"))

                    #sys.exit()

            count += 1

        filewithinit.close()

        return pathcodewithinit


    def translatepipsannot(self, _cpathpipscode):

        # Get the number line where each function begin
        listnumbeginfunc = self.getnumbeginfuncts(_cpathpipscode)
        if not self.listnumbeginfunc:
            print("ERROR. Identifying functions")
            sys.exit()

        # Call class to translate PIPS annotation
        runtranslatepips = translate_pips.PipsTranslateAnnot()
        runtranslatepips.nameoforicprogram = self.nameoforicprogram
        runtranslatepips.list_beginnumfuct = listnumbeginfunc

        return runtranslatepips.instprecondinprog(_cpathpipscode)





    @staticmethod
    def getnumbeginfuncts(_cfilepath):

        # result
        listbeginnumfuct = []

        # get data functions using ctags
        textdata = commands.getoutput("ctags -x --c-kinds=f " + _cfilepath)

        # generate a list with the data funct
        listdatafunct = textdata.split("\n")
        for linedfunct in listdatafunct:
            # split by space
            matchlinefuct = re.search(r'function[ ]+([0-9]+)', linedfunct)
            if matchlinefuct:
                # consider the line number of index program in the list
                listbeginnumfuct.append(int(matchlinefuct.group(1).strip()))

        return listbeginnumfuct


    def callesbmccheck(self, _cfilepath):
        runesbmc = esbmccheck.DepthEsbmcCheck()
        runesbmc.esbmcpath = self.esbmcpath
        runesbmc.countMAXtry = self.countMAXtry
        runesbmc.kinductioncheck(_cfilepath)




# -------------------------------------------------
# Main python program
# -------------------------------------------------

if __name__ == "__main__":

    ############# Parse args options
    parser = argparse.ArgumentParser(description='Run DepthK v1.0')
    parser.add_argument('-v','--version', action='version', version="version 1.0")
    parser.add_argument(dest='inputCProgram', metavar='file.c or file.i', type=str,
                        help='the C program file to be analyzed')
    parser.add_argument('-n','--number-try', metavar='nr', type=int, dest='setNumberTry',
                        default=10, help='set the number of times to try the depth check with ESBMC')

    args = parser.parse_args()


    inputCFile=''
    if args.inputCProgram:
        if not os.path.isfile(args.inputCProgram):
            print('Error: unable to open input file (%s)' % quote(args.inputCProgram))
            parser.parse_args(['-h'])
            sys.exit()
        else:
            inputCFile = os.path.abspath(quote(args.inputCProgram))

            rundepthk = DepthK(inputCFile)
            # Define ESBMC path
            rundepthk.esbmcpath = "~/Downloads/BMCs/esbmc/esbmc-v1.24.1/esbmc_vs24"
            if args.setNumberTry:
                rundepthk.countMAXtry = args.setNumberTry

            # Identify the extension of the C program .c or .i (some code is added in the new instance)
            if inputCFile.endswith(".i"):
                rundepthk.inputisexti = True


            # Applying steps of detphk
            dict_init = rundepthk.identify_initpips(inputCFile)
            pathcodeinit = rundepthk.generatecodewithinit(inputCFile,dict_init)
            pathcodepipstranslated = rundepthk.translatepipsannot(pathcodeinit)
            rundepthk.callesbmccheck(pathcodepipstranslated)





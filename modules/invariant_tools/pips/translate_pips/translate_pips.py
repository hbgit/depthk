#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# Class to translate PIPS annotations
# by Herbert Rocha
#
# e-mail: map2check.tool@gmail.com
# -------------------------------------------------

from __future__ import print_function

import re


class PipsTranslateAnnot(object):
    def __init__(self):
        self.list_beginnumfuct = []


    def instprecondinprog(self, _cprogrampath):

        fileprogram = open(_cprogrampath, "r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        i = 0
        while i < len(listfilec):

            matchannot = re.search(r'//[ ]+P', listfilec[i])
            if matchannot:
                # Handling the annotation
                #
                # Insert the precondition
                # Checking if the next line is a function
                k = i
                # jump to  the next vallid line
                # TODO: BUG here we could have multiple lines
                countlineprecond = self.hasprecondmorelines(listfilec, i)
                if countlineprecond == 1:
                    k += 3 # i + 1(actual line) + 1(blank space) + 1(next funct - header funct)
                else:
                    k += countlineprecond + 2 # to reach the function header

                # print(k, self.list_beginnumfuct)

                # functions header
                if k in self.list_beginnumfuct:
                    #print("//P<<<<<<<<<<<<<<<<")

                    # Identify if the precondition has more than one line
                    # print original Precondition
                    # print(listfilec[i].strip())
                    #countlineprecond = self.hasprecondmorelines(listfilec, i)
                    annot2betrans = ""
                    flagmultlines = False
                    if countlineprecond == 1:
                        annot2betrans = listfilec[i]
                    else:
                        cntlines = countlineprecond
                        flagmultlines = True
                        while cntlines > 0:
                            #print(listfilec[i].strip())
                            annot2betrans += listfilec[i]
                            i += 1
                            cntlines -= 1

                    print(annot2betrans)


                    # annotations with multiples lines
                    if flagmultlines:
                        i += 1
                        # print funct header
                        print(listfilec[i].strip())

                        # print the delimiter
                        i += 1
                        print(listfilec[i].strip())

                        # print a blank line - just for pretty code
                        print("")
                    else:
                        # print blank line
                        i += 1
                        print(listfilec[i].strip())
                        # print funct header
                        i += 1
                        print(listfilec[i].strip())
                        # print the delimiter
                        i += 1
                        print(listfilec[i].strip())
                        # print a blank line - just for pretty code
                        print("")



                    # print Precondition translated
                    translateresult = self.translatepreconditionpips(annot2betrans)
                    if translateresult is not None:
                        print(translateresult)

                        #i = k
                else:

                    # Skipping global vars and function declaration
                    # Fisrt we need to know if we are still inside a function

                    # nextidline = i+2
                    # matchskip = re.search(r'\);$', listfilec[nextidline].strip())
                    # if matchskip:
                    # print(listfilec[nextidline].strip(), "<<<<<<<<<<<<<<<<<<")

                    # print original Precondition
                    # Identify if the precondition has more than one line
                    countlineprecond = self.hasprecondmorelines(listfilec, i)
                    annot2betrans = ""
                    if countlineprecond == 1:
                        annot2betrans = listfilec[i]
                    else:
                        #print("::::::::::::::::::")
                        cntlines = countlineprecond
                        while cntlines > 0:
                            #print(listfilec[i].strip())
                            annot2betrans += listfilec[i]
                            i += 1
                            cntlines -= 1

                    # First print the original Precondition and then the translated to ASSUME
                    print(annot2betrans)

                    translateresult = self.translatepreconditionpips(annot2betrans)
                    if translateresult is not None:
                        print(translateresult)

            else:
                print(listfilec[i].strip())
            i += 1


    @staticmethod
    def hasprecondmorelines(_listcfile, _actualindex):
        countlinespre = 1
        _actualindex += 1

        flagcount = True
        while flagcount:
            matchcomment = re.search(r'^//', _listcfile[_actualindex])
            if matchcomment:
                countlinespre += 1
                _actualindex += 1
            else:
                flagcount = False

        return countlinespre



    @staticmethod
    def translatepreconditionpips(_precondtext):

        # Generating a single line from PIPS annotation
        linesannot =  _precondtext.split("//")
        singlelineannot = ''
        for line in linesannot:
            singlelineannot += " " + line.strip()
        #print("************************ ", singlelineannot )

        matchannot = re.search(r'P(.*)[ ]+\{(.*)\}', singlelineannot)

        # Skip this //  P() {0==-1}
        if not matchannot is None:
            if not matchannot.group(2) == "0==-1":

                if matchannot:
                    listprecond = matchannot.group(2).split(",")

                    if len(listprecond) == 0 or listprecond[0] == '':
                        return None
                    else:
                        # Analyzing each predicate in the list of the precondition
                        listnewpreform = []
                        for predicate in listprecond:
                            # splitting the predicates
                            list_leftright = re.split("[=<>!]+", predicate)

                            # wrong form to C program
                            for value in list_leftright:
                                newpreform = ''
                                matchwrongpre = re.search(r'(([0-9]+)([a-zA-Z]+))', predicate.strip())
                                if matchwrongpre:
                                    # invert the strings
                                    newpreform = matchwrongpre.group(3) + "*" + matchwrongpre.group(2)

                                    # print(newpreform)
                                    #print(matchwrongpre.group(1))
                                    predicate = re.sub(matchwrongpre.group(1), newpreform, predicate)

                                # Rename vars in this form comp_m1_st#init
                                matchinit = re.search(r'#init', predicate)
                                if matchinit:
                                    predicate = predicate.replace("#init", "_init")

                            # print(predicate.strip())
                            listnewpreform.append(predicate.strip())

                        # Mount string to precondition
                        middletxt = ' && '.join(listnewpreform)
                        newtext = "__ESBMC_assume( " + middletxt + " );"
                        return newtext
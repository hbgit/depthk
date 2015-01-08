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
                k += 2

                if k in self.list_beginnumfuct:
                    # print("//P<<<<<<<<<<<<<<<<")
                    # print original Precondition
                    print(listfilec[i].strip())
                    i += 1
                    # blank line
                    print(listfilec[i].strip())

                    # print funct header
                    i += 1
                    print(listfilec[i].strip())
                    i += 1
                    # print the delimiter
                    print(listfilec[i].strip())
                    i += 1
                    # blank line
                    print(listfilec[i].strip())

                    # print Precondition translated
                    translateresult = self.translatepreconditionpips(listfilec[i].strip())
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
                    print(listfilec[i].strip())
                    # print Precondition translated
                    translateresult = self.translatepreconditionpips(listfilec[i].strip())
                    if translateresult is not None:
                        print(translateresult)

            else:
                print(listfilec[i].strip())
            i += 1


    @staticmethod
    def translatepreconditionpips(_precondtext):

        matchannot = re.search(r'//[ ]+P(.*)[ ]+\{(.*)\}', _precondtext)

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
                            # print(predicate.strip())
                            listnewpreform.append(predicate.strip())

                        # Mount string to precondition
                        middletxt = ' && '.join(listnewpreform)
                        newtext = "__ESBMC_assume( " + middletxt + " );"
                        return newtext
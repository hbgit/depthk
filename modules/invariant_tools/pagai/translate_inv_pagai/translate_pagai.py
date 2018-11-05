#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# Class to translate PAGAI program invariants
# by Herbert Rocha
#
# e-mail: herberthb12@gmail.com
# -------------------------------------------------

from __future__ import print_function

import re
import os
import sys
import string


class TranslatePagai(object):
    def __init__(self):
        self.pathprogram = ""
        self.invlocation = {} # format data: line number -> [list program invariants]


    def removenotprintable(self, __listprogram):

        i = 0
        while i < len(__listprogram):
            __listprogram[i] = filter(lambda x: x in string.printable, __listprogram[i])
            __listprogram[i] = re.sub('\[0;1;[0-9]+m',"", __listprogram[i])
            __listprogram[i] = re.sub('\[[0-9]+m',"", __listprogram[i])
            #[0;1;35m[0m

            # Removing unecessary annots
            #// safe
            match_safenote = re.search(r'[0-9a-zA-Z_]+(\/\/ safe)', __listprogram[i])
            if match_safenote:
                # broken line
                __listprogram[i] = re.sub('\/\/ safe',"", __listprogram[i])
                __listprogram[i] = __listprogram[i].strip()
                i+=1
                __listprogram[i] = re.sub('^[ ]+',"", __listprogram[i])


            i += 1

        #__listprogram[0] = re.sub('\[0;1;.*0m',"", __listprogram[0])

        #[0;1;31m[0m
        #print(__listprogram)
        return __listprogram


    def identifyInv(self, __pathprogram):
        self.invlocation = {} # format data: line number -> [list program invariants]
        # read file to indetify the program invariants generated by PAGAI tool
        fileprogram = open(__pathprogram, "r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        i = 0
        lineidinv = 0
<<<<<<< HEAD
        flag_labreach = False
=======
        line_start_inv = 0
        line_end_inv = 0
        flag_labreach = False
        match_previnv = False
>>>>>>> Depthk & ESBMC tools updated
        count_labreach = 1
        flag_skip_inv = False
        while i < len(listfilec):

<<<<<<< HEAD
            #label reachable
            match_labrea = re.search(r'/\* reachable', listfilec[i])
            if match_labrea:
                #print(">>>>>>>>>>>>>>>>>>>>>>>>>>>")
                flag_labreach = True

            if flag_labreach:
                count_labreach += 1
                if count_labreach > 3:
                    count_labreach = 0
                    flag_labreach = False
                #else:
                #    print(str(count_labreach)+"======")

            matchinv = re.search(r'/\* invariant:', listfilec[i])
            if matchinv:
                flagreadinv = True

                #Indetifying if the prev line we have other comment inv
                match_previnv = re.search(r'\*/', listfilec[i-1])
                if match_previnv:
                    flag_fline = False
                    tmpcount = i-2
                    while not flag_fline:
                        #print(">>>>>>")
                        matchinvprev = re.search(r'/\* invariant:', listfilec[tmpcount])
                        if matchinvprev:
                            lineidinv = tmpcount -1
                            flag_fline = True
                        else:
                            tmpcount -= 1
                else:
                    lineidinv = i - 1 # to avoid incorrect annotation by PAGAI

                tmplistinv = []
                while flagreadinv:
                    # jump invariant text
                    i += 1
                    matchendcomment = re.search(r'\*/', listfilec[i])
                    if matchendcomment:
                        flagreadinv = False
                        if flag_skip_inv:
                            lineidinv = i
                    else:
                        #print(listfilec[i], end="")
                        # Translate math format -> a = 100 to a == 100
                        list_splitpredicate = re.split("=", listfilec[i].strip())
                        # check if in the predicate we have only "="
=======
            matchinv = re.search(r'/\* invariant:', listfilec[i])
            if matchinv:
                flagreadinv = True
                line_start_inv = i
                
                
                # Invariant translation
                tmplistinv = []
                while flagreadinv and not match_previnv:
                    # jump invariant text
                    i += 1
                    
                    #Indetifying if the prev line we have other 
                    #comment to end inv location
                    match_previnv = re.search(r'\*/', listfilec[i])
                    if match_previnv:
                        flag_fline = False                    
                        line_end_inv = i
                    else:                                        
                        # Translate math format -> a = 100 to a == 100
                        # check if in the predicate we have only "="
                        list_splitpredicate = re.split("=", listfilec[i].strip())
                        #print(">>>>>>"+listfilec[i].strip())
>>>>>>> Depthk & ESBMC tools updated

                        matchleftsidepredicate = re.search(r'[<>!]+', list_splitpredicate[0])
                        matchrightsidepredicate = re.search(r'[<>!]+', list_splitpredicate[1])

                        if not matchleftsidepredicate and not matchrightsidepredicate:
                            listfilec[i] = listfilec[i].replace("=","==")

                        # invariants not translated
                        # --- bitsleft
                        matchbits = re.search(r'bitsleft\.[0-9]*', listfilec[i])
                        if not matchbits:
                            tmplistinv.append(listfilec[i].strip())

                # save program invariants
<<<<<<< HEAD
                if flag_labreach:
                    #print(str(count_labreach)+"<<<<<<<")
                    self.invlocation[lineidinv-count_labreach] = tmplistinv
                    flag_labreach = False
                    count_labreach = 0
                else:
                    self.invlocation[lineidinv] = tmplistinv
=======
                if matchinv:  
                    #print(">>>>>>>"+str(line_end_inv-line_start_inv))                  
                    self.invlocation[line_start_inv-1] = tmplistinv                   
                
>>>>>>> Depthk & ESBMC tools updated
                lineidinv = 0
                tmplistinv = []

            # count while
            i += 1

        # All program invariants identified in the PAGAI output
        #print(self.invlocation)
        #sys.exit()

        if bool(self.invlocation):
            # OKAY program invariants were detected
            return True
            listfilec = []
        else:
            # No program invariants were NOT detected
            return False
            listfilec = []


    def applyesbmcassume(self, __inv):
        __inv = "__ESBMC_assume( " + str(__inv) + " ); \n"
        return __inv




    def writeInvPAGAI(self, __pathprogram, addassume):
        # read file to indetify the program invariants generated by PAGAI tool
        fileprogram = open(__pathprogram, "r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

<<<<<<< HEAD
        listcodewithinv = []
        invbuffer = []
        i = 0
        j = 0
        hasInvariantInLine = False
        while i < len(listfilec):
            listcodewithinv.append(listfilec[i])
            matchinvprev = re.search(r'\*/', listfilec[i])
            if(matchinvprev):
                hasInvariantInLine = False
                for invariant in invbuffer:
                    listcodewithinv.append(invariant)
                invbuffer = []

            if hasInvariantInLine and addassume and j <= len(self.invlocation) - 1:
                #for inv in self.invlocation.get(i):
                #inv = str(self.invlocation.items()[j][1]).replace("[", "").replace("]", "").replace("'", "").replace(",", " && ")
                #invbuffer.append(self.applyesbmcassume(inv))
                lista = self.invlocation.items()[j][1]
                for inv in lista:
                    invbuffer.append(self.applyesbmcassume(inv))
                hasInvariantInLine = False
                j += 1

            matchinvprev = re.search(r'/\* invariant:', listfilec[i])
            if(matchinvprev):
                hasInvariantInLine = True

            # count while
            i += 1

        return listcodewithinv
=======
        listcodewithinv__ = []
        invbuffer = []        
        j = 0
        count_inv = 0
        hasInvariantInLine = False        
        id_list_file = 0
        len_listfilec = len(listfilec)
        #while id_list_file < len_listfilec:
        for line in listfilec:  
            #print(">>>"+str(id_list_file) )                     
            listcodewithinv__.append(line)
            
            if id_list_file in self.invlocation and count_inv <= (len(self.invlocation)-1):                                        
                lista = self.invlocation.items()[count_inv][1]                
                hasInvariantInLine = True                
                for inv in lista:
                    #invbuffer.append(self.applyesbmcassume(inv))                            
                    listcodewithinv__.append(self.applyesbmcassume(inv))
                count_inv = count_inv + 1
                            
            # count while
            id_list_file += 1
        
        return listcodewithinv__
>>>>>>> Depthk & ESBMC tools updated


if __name__ == "__main__":
    if len(sys.argv) > 1:
        filename = sys.argv[1]
        r = TranslatePagai()
        r.pathprogram = filename
        if r.identifyInv(r.pathprogram):
            print("Program invariants were detected")
<<<<<<< HEAD
            newprogram = r.writeInvPAGAI(r.pathprogram)
=======
            newprogram = r.writeInvPAGAI(r.pathprogram,False)
>>>>>>> Depthk & ESBMC tools updated
            for line in newprogram:
                print(line, end="")
        else:
            print("Program invariants were NOT detected")


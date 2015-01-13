#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# Class to support the k-induction verification
# performed by ESBMC
# by Herbert Rocha
#
# e-mail: herberthb12@gmail.com
# -------------------------------------------------

from __future__ import print_function

import commands
import re
import sys
import os
import shutil


class DepthEsbmcCheck(object):

    def __init__(self):
        self.list_beginnumfuct = {}
        self.nameoforicprogram = ""
        self.assumeset = ''
        self.statecurrentfunct = ''
        # depth check options
        self.countMAXtry = 10
        self.esbmcpath = ''
        self.esbmc_arch = "--64"
        self.esbmc_extra_op = "--no-library --memlimit 4g --timeout 15m"
        self.esbmc_solver = "--z3"
        self.esbmc_basecase_op = "--base-case"
        self.esbmc_forwardcond_op = "--forward-condition"
        self.esbmc_inductivestep_op = "--k-induction-parallel  --inductive-step --show-counter-example"


    @staticmethod
    def getlastlinenumfromce(_esbmccepath):
        """
        This method get the last line number pointed in the
        counterexample from bottom to top excluding the line number
        pointed in the property violated.

        :param _esbmccepath:
        :return: The method return the line number where will be write the ESBMC ASSUME
        """

        filece = open(_esbmccepath,"r")
        listfilece = filece.readlines()
        filece.close()

        # reading file from bottom to top
        i = len(listfilece)-1
        flagstartpoint = False

        while i >= 0:

            # identify Violated property point in the CE
            matchvp = re.search(r'Violated property', listfilece[i])
            if matchvp and not flagstartpoint:
                # identifying the first start to jump and then start to search
                # line number by state
                i -= 2 # jump line with ---------
                while not re.search(r'State[ ]+[0-9]+', listfilece[i]):
                    i -= 1

                # how is the first state then we jump it
                i -= 1
                flagstartpoint = True


            matchstate = re.search(r'State[ ]+[0-9]+', listfilece[i])
            if matchstate:

                # identifying if the state has some valid line number
                matchsline = re.search(r'line[ ]+([0-9]+)', listfilece[i])
                if matchsline:
                    #print(matchsline.group(1))
                    #sys.exit()
                    return int(matchsline.group(1)) - 1

            i -= 1


    def getlastdatafromce(self, _esbmccepath):
    
        filece = open(_esbmccepath,"r")
        listfilece = filece.readlines()
        filece.close()
    
        # reading file from bottom to top
        i = len(listfilece)-1
        flagstop = False
        cestatetext = ''
        flaghasstate = False

        while i >= 0 and not flagstop:
            # get the last state
            matchState = re.search(r'cs\$[0-9]+', listfilece[i])
            if matchState:
                flaghasstate = True

                flagkeepgettext = True
                while flagkeepgettext:
    
                    matchblankline = re.search(r'^$', listfilece[i])
                    if matchblankline:
                        flagkeepgettext = False
                    else:
                        #print(listfilece[i],"++++++=")
                        cestatetext += listfilece[i]
    
                    i += 1
    
    
                flagstop = True
            i -= 1

        if not flaghasstate:
            return False

        # identify the function name where is the state identified
        matchfunctname = re.search(r'[.]c[:]+new_[a-zA-Z_0-9]+[:]+([a-zA-Z_0-9]+):.*', cestatetext)
        if matchfunctname:
            self.statecurrentfunct = matchfunctname.group(1)


        # handle text state get from CE
        #.c::new_main::main::1::SIZE=7
        #cestatetext = cestatetext.replace(" ", "")
        #print(cestatetext)
        #sys.exit()
        # preprocessing CE text
        # splitting to remove blank spaces
        listassign = cestatetext.split(".c")

        #removing blank spaces to deal with arrays
        countb = 0
        while countb < len(listassign):
            listassign[countb] = listassign[countb].replace(" ", "")
            countb += 1

        textcejoinspace = ' '.join(listassign)

        #print(textcejoinspace)
        #sys.exit()

        listassign = textcejoinspace.split(" ")
        #listassign = re.split('\.c[:]+.[^]+')

        listnewassign = []
        for item in listassign:
            #
            item = item.strip()
            #print("---------------------",item)
            # get only the assignment in the CE
            skipassign = False
    
            #item = item.replace("}", "")
            #item = item.replace("{", "")
            #item = item.replace(" ", "")
    
            matchassign = re.search(r'(.[^\}\{:]+)$', item)
            if matchassign:
                # clean assignment
                ceassign = matchassign.group(1).strip()
                ceassign = ceassign.replace(":", "")
    
                # ------------- Skipping area
                flag_skip = False
    
                # Skipping tmp$ variables
                matchskip = re.search(r'tmp\$', ceassign)
                if matchskip:
                    flag_skip = True
    
                # Skipping return_value___VERIFIER_nondet_int$2=0
                matchskip = re.search(r'return_value_', ceassign)
                if matchskip:
                    flag_skip = True

                # Skipping arrays <- for while
                # .c::new_main::main::1::v={ 2147483650, 1, 2147483648, 2147483712}
                matcharray = re.search(r'[ =]*\{', ceassign)
                if matcharray:
                    flag_skip = True

                #matchdenddemiliter = re.search(r'[ =]*\{', ceassign)
    
                # ------------- END Skipping area
    
                if not flag_skip:
                    # replace = by !=
                    ceassign = ceassign.replace("=", "!=")
                    ceassign = ceassign.replace(",","")

                    print("\t => " + ceassign)
                    listnewassign.append(ceassign)
    
        #sys.exit()
        # generating __ASSUME
        txtassume = ' && '.join(listnewassign)
        self.assumeset = "__ESBMC_assume( " + txtassume + " );"

        return True



    def addassumeinprogram(self, _cprogrampath, _linenumtosetassume):
        #print(_linenumtosetassume)
        #sys.exit()

        fileprogram = open(_cprogrampath,"r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        # generate data about the functions
        self.list_beginnumfuct = self.getnumbeginfuncts(_cprogrampath)
    
        # TODO: it is best to write this new instance in the original path of the program
        newfile = open("/tmp/new_instance.c", "w")
    
        i = 0
        while i < len(listfilec):
    
            # just write the new code
            #newfile.write(str(i)+"->"+listfilec[i])
            newfile.write(listfilec[i])

            # identify where to write the new assume from CE
            if i == _linenumtosetassume:
                newfile.write(self.assumeset + "\n")



            # identifying the function pointed in the CE state
            #print(self.list_beginnumfuct)

            # if i in self.list_beginnumfuct.keys():
            #     if self.list_beginnumfuct[i].strip() == self.statecurrentfunct.strip():
            #         print(self.statecurrentfunct, "<<<<<<<<,")



            # matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
            # if matchassumeanot:
            #     flagstop = False
            #     while matchassumeanot and not flagstop:
            #         #print(listfilec[i],end="")
            #         newfile.write(listfilec[i])
            #         if i <= len(listfilec):
            #             i += 1
            #             matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
            #         else:
            #             flagstop = True
            #
            #     #print(self.assumeset+"\n")
            #     newfile.write(self.assumeset + "\n \n \n")
            # else:
            #     #print(listfilec[i],end="")
            #     newfile.write(listfilec[i])
            i += 1


        newfile.close()
    
        return "/tmp/new_instance.c"
    
    
    def kinductioncheck(self, _cprogrampath):
    
        flagstopcheck = False
        count = 0

        actual_ce = "/tmp/ce_kinduction.txt"
        last_ce = "/tmp/last_ce_kinduction.txt"

        # Applying k-induction algorithm
        # TODO: How understand the If's in the algorithm
        # (1) Checking base-case
        #result_basecase = commands.getoutput(self.esbmcpath)

    
        while not flagstopcheck:

            if os.path.isfile("/tmp/ce_kinduction.txt"):
                shutil.copyfile("/tmp/ce_kinduction.txt", "/tmp/last_ce_kinduction.txt")

            result = commands.getoutput(self.esbmcpath + " " + self.esbmc_arch + " " +
                                        self.esbmc_extra_op + " " +
                                        self.esbmc_inductivestep_op + " " +
                                        str(_cprogrampath) + " &> /tmp/ce_kinduction.txt")
    
            # checking CE
            statusce = commands.getoutput("cat /tmp/ce_kinduction.txt | grep -c \"VERIFICATION FAILED\" ")
            if int(statusce) > 0:
                print("cat /tmp/ce_kinduction.txt")

    
                if count <= self.countMAXtry:
    
                    print("")
                    print("=======> MAX: "+str(count))
                    print("")
                    print("FAILED inductive step")
                    print("\t - Get data from CE in the last valid state:")

                    # IDENTIFY where (function) the last state is
                    # Possible BUG cuz the PLACE where the assume is added

                    # TODO: BUG with .c::new_main::main::1::v={ 2147483650, 1, 2147483648, 2147483712}


                    if not self.getlastdatafromce("/tmp/ce_kinduction.txt"):
                        print("NO DATA from CE !!!")
                        sys.exit()


                    print("\t - New assume generated: \n" + self.assumeset)
                    print("\t - Instrument program with assume ...")

                    linenumtosetassume = self.getlastlinenumfromce("/tmp/ce_kinduction.txt")

                    _cprogrampath = self.addassumeinprogram(_cprogrampath, linenumtosetassume)

                    #print(_cprogrampath)

                    #sys.exit()
    
                    count += 1
                else:
                    print(">> STOP ============================== MAX == " + str(self.countMAXtry) + " reached !!!")
                    flagstopcheck = True
    
            else:
                # VERIFICATION SUCCESSFUL to inductive step
                #print()
                os.system("cat /tmp/ce_kinduction.txt")
                #flagstopcheck = True
                flagstopcheck = True


    def getnumbeginfuncts(self, _cfilepath):

        # result
        listbeginnumfuct = {}

        # get data functions using ctags
        textdata = commands.getoutput("ctags -x --c-kinds=f " + _cfilepath)

        # generate a list with the data funct
        listdatafunct = textdata.split("\n")
        for linedfunct in listdatafunct:
            # split by space
            matchlinefuct = re.search(r'(.+)[ ]+function[ ]+([0-9]+)', linedfunct)
            if matchlinefuct:
                # a dictionary with the name of the function and the line number where it start
                listbeginnumfuct[int(matchlinefuct.group(2))-1] = matchlinefuct.group(1)

        return listbeginnumfuct

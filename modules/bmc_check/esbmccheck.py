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


class DepthEsbmcCheck(object):

    def __init__(self):
        self.list_beginnumfuct = {}
        self.nameoforicprogram = ""
        self.assumeset = ''
        self.statecurrentfunct = ''
        # depth check options
        self.countMAXtry = 10
        self.esbmcpath = ''
        self.esbmcoptions = "--64 --no-library --k-induction-parallel --memlimit 4g " \
                            "--z3 --inductive-step --show-counter-example "


    def getlastdatafromce(self, _esbmccepath):
    
        filece = open(_esbmccepath,"r")
        listfilece = filece.readlines()
        filece.close()
    
        # reading file from bottom to top
        i = len(listfilece)-1
        flagstop = False
        cestatetext = ''
        while i >= 0 and not flagstop:
            # get the last state
            matchState = re.search(r'cs\$[0-9]+', listfilece[i])
            if matchState:

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
    
        # identify the function name where is the state identified
        matchfunctname = re.search(r'[.]c[:]+new_[a-zA-Z_0-9]+[:]+([a-zA-Z_0-9]+):.*', cestatetext)
        if matchfunctname:
            #print(">>>>>>>>>", matchfunctname.group(1))
            self.statecurrentfunct = matchfunctname.group(1)


        # handle text state get from CE
        listassign = cestatetext.split(",")
        listnewassign = []
        for item in listassign:
            # get only the assignment in the CE
            skipassign = False
    
            item = item.replace("}", "")
            item = item.replace("{", "")
            item = item.replace(" ", "")
    
            matchassign = re.search(r'(.[^\}\{\:]+)$', item)
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
    
                # ------------- END Skipping area
    
                if not flag_skip:
                    # replace = by !=
                    ceassign = ceassign.replace("=", "!=")
                    print("\t => " + ceassign)
                    listnewassign.append(ceassign)
    
        # generating __ASSUME
        txtassume = ' && '.join(listnewassign)
        self.assumeset = "__ESBMC_assume( " + txtassume + " );"



    def addassumeinprogram(self, _cprogrampath):

        fileprogram = open(_cprogrampath,"r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        # generate data about the functions
        self.list_beginnumfuct = self.getnumbeginfuncts(_cprogrampath)
    
        # TODO: it is best to write this new instance in the original path of the program
        newfile = open("/tmp/new_instance.c", "w")
    
        i = 0
        while i < len(listfilec):
    
            # identify where to write the new assume from CE
            # identifying the function pointed in the CE state
            #print(self.list_beginnumfuct)

            if i in self.list_beginnumfuct.keys():
                if self.list_beginnumfuct[i].strip() == self.statecurrentfunct.strip():
                    print(self.statecurrentfunct, "<<<<<<<<,")
                    # TODO: when add the new assume


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

        sys.exit()
    
        newfile.close()
    
        return "/tmp/new_instance.c"
    
    
    def kinductioncheck(self, _cprogrampath):
    
        flagstopcheck = False
        count = 0
    
        while not flagstopcheck:
            result = commands.getoutput(self.esbmcpath + " " + self.esbmcoptions + " " +
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
                    # todo: IDENTIFY where (function) the last state is
                    self.getlastdatafromce("/tmp/ce_kinduction.txt")
                    print("\t - New assume generated: \n" + self.assumeset)
                    print("\t - Instrument program with assume ...")

                    _cprogrampath = self.addassumeinprogram(_cprogrampath)

                    sys.exit()
    
                    count += 1
                else:
                    print(">> STOP ============================== MAX == " + self.countMAXtry + " reached !!!")
                    sys.exit()
    
            else:
                # VERIFICATION SUCCESSFUL to inductive step
                os.system("cat /tmp/ce_kinduction.txt")
                #flagstopcheck = True
                sys.exit()


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

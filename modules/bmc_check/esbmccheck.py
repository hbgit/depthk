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
        self.list_beginnumfuct = []
        self.nameoforicprogram = ""
        self.assumeset = ''
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
    
        # TODO: it is best to write this new instance in the original path of the program
        newfile = open("/tmp/new_instance.c", "w")
    
        i = 0
        while i < len(listfilec):
    
            matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
            if matchassumeanot:
                flagstop = False
                while matchassumeanot and not flagstop:
                    #print(listfilec[i],end="")
                    newfile.write(listfilec[i])
                    if i <= len(listfilec):
                        i += 1
                        matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
                    else:
                        flagstop = True
    
                #print(self.assumeset+"\n")
                newfile.write(self.assumeset + "\n \n \n")
            else:
                #print(listfilec[i],end="")
                newfile.write(listfilec[i])
            i += 1
    
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
    
                if count <= self.countMAXtry:
    
                    print("")
                    print("=======> MAX: "+str(count))
                    print("")
                    print("FAILED inductive step")
                    print("\t - Get data from CE in the last valid state:")
                    self.getlastdatafromce("/tmp/ce_kinduction.txt")
                    print("\t - New assume generated: \n" + self.assumeset)
                    print("\t - Instrument program with assume ...")
    
                    _cprogrampath = self.addassumeinprogram(_cprogrampath)
    
                    count += 1
                else:
                    print(">> STOP ============================== MAX == " + self.countMAXtry + " reached !!!")
                    sys.exit()
    
            else:
                # VERIFICATION SUCCESSFUL to inductive step
                os.system("cat /tmp/ce_kinduction.txt")
                #flagstopcheck = True
                sys.exit()

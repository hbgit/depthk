#!/usr/bin/env python
# -*- coding: latin1 -*-
# -------------------------------------------------
# Map2Check Tool v5
# by Herbert Rocha
#
# e-mail: map2check.tool@gmail.com
# -------------------------------------------------

from __future__ import print_function

import argparse
import sys
import os
import commands
import re
import csv
import shutil
from pipes import quote


# GLOBAL VARS
assumeset = ''


def getlastdatafromce(cepapth_):

    global assumeset

    filece = open(cepapth_,"r")
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
                print("\t => "+ceassign)
                listnewassign.append(ceassign)

    # generating __ASSUME
    txtassume = ' && '.join(listnewassign)
    assumeset = "__ESBMC_assume( " + txtassume + " );"



def addassumeinprogram(cprogrampath_):
    global assumeset


    fileprogram = open(cprogrampath_,"r")
    listfilec = fileprogram.readlines()
    fileprogram.close()

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

            #print(assumeset+"\n")
            newfile.write(assumeset+"\n \n \n")
        else:
            #print(listfilec[i],end="")
            newfile.write(listfilec[i])
        i += 1

    newfile.close()

    return "/tmp/new_instance.c"


def kinductioncheck(cprogrampath_):

    flagstopcheck = False
    countMAXtry = 10
    count = 0

    while not flagstopcheck:
        result = commands.getoutput("esbmc_v1_24 --64 --no-library --k-induction-parallel "
                                    "--memlimit 4g --z3 --inductive-step --show-counter-example " +
                                    str(cprogrampath_) + " &> /tmp/ce_kinduction.txt")

        # checking CE
        statusce = commands.getoutput("cat /tmp/ce_kinduction.txt | grep -c \"VERIFICATION FAILED\" ")
        if int(statusce) > 0:

            if count <= countMAXtry:

                print("")
                print("=======> MAX: "+str(count))
                print("")
                print("FAILED inductive step")
                print("\t - Get data from CE in the last valid state:")
                getlastdatafromce("/tmp/ce_kinduction.txt")
                print("\t - New assume generated: \n" + assumeset)
                print("\t - Instrument program with assume ...")

                cprogrampath_ = addassumeinprogram(cprogrampath_)

                count += 1
            else:
                print("============================== MAX == 10 reached !!!")
                sys.exit()

        else:
            # VERIFICATION SUCCESSFUL to inductive step
            flagstopcheck = True
            sys.exit()

    #VERIFICATION FAILED




# -------------------------------------------------
# Main python program
# -------------------------------------------------

if __name__ == "__main__":
    kinductioncheck(quote(sys.argv[1]))



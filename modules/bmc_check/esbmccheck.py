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
import os
import sys
import shutil


class DepthEsbmcCheck(object):
    def __init__(self):
        self.list_beginnumfuct = {}
        self.nameoforicprogram = ""
        self.assumeset = ''
        self.statecurrentfunct = ''
        # depth check options
        self.debug = False
        self.maxk = 15
        self.maxdepthverification = 25
        self.esbmcpath = ''
        self.esbmc_arch = "--64"
        self.esbmc_bound = 1
        self.esbmc_unwind_op = "--unwind"
        self.esbmc_extra_op = "--no-library --memlimit 4g --timeout 15m"
        self.esbmc_solver_op = "--z3"
        # k-induction options
        self.esbmc_basecase_op = "--base-case"
        self.esbmc_forwardcond_op = "--forward-condition"
        self.esbmc_inductivestep_op = "--inductive-step --no-slice --show-counter-example"


    @staticmethod
    def getlastlinenumfromce(_esbmccepath):
        """
        This method get the last line number pointed in the
        counterexample from bottom to top excluding the line number
        pointed in the property violated.

        :param _esbmccepath:
        :return: The method return the line number where will be write the ESBMC ASSUME
        """

        filece = open(_esbmccepath, "r")
        listfilece = filece.readlines()
        filece.close()

        # reading file from bottom to top
        i = len(listfilece) - 1
        flagstartpoint = False

        while i >= 0:

            # identify Violated property point in the CE
            matchvp = re.search(r'Violated property', listfilece[i])
            if matchvp and not flagstartpoint:
                # identifying the first start to jump and then start to search
                # line number by state
                i -= 2  # jump line with ---------
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
                    # print(matchsline.group(1))
                    # sys.exit()
                    return int(matchsline.group(1)) - 1

            i -= 1


    def getlastdatafromce(self, _esbmccepath):

        filece = open(_esbmccepath, "r")
        listfilece = filece.readlines()
        filece.close()

        # reading file from bottom to top
        i = len(listfilece) - 1
        flagstop = False
        cestatetext = ''
        flaghasstate = False

        while i >= 0 and not flagstop:
            # get the last state
            matchstate = re.search(r'cs\$[0-9]+', listfilece[i])
            if matchstate:
                flaghasstate = True

                flagkeepgettext = True
                while flagkeepgettext:

                    matchblankline = re.search(r'^$', listfilece[i])
                    if matchblankline:
                        flagkeepgettext = False
                    else:
                        # print(listfilece[i],"++++++=")
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
        # .c::new_main::main::1::SIZE=7
        # cestatetext = cestatetext.replace(" ", "")
        # print(cestatetext)
        # sys.exit()
        # preprocessing CE text
        # splitting to remove blank spaces
        listassign = cestatetext.split(".c")

        # removing blank spaces to deal with arrays
        countb = 0
        while countb < len(listassign):
            listassign[countb] = listassign[countb].replace(" ", "")
            countb += 1

        textcejoinspace = ' '.join(listassign)

        # print(textcejoinspace)
        # sys.exit()

        listassign = textcejoinspace.split(" ")
        # listassign = re.split('\.c[:]+.[^]+')

        listnewassign = []
        for item in listassign:
            #
            item = item.strip()
            # print("---------------------",item)
            # get only the assignment in the CE
            # skipassign = False

            # item = item.replace("}", "")
            # item = item.replace("{", "")
            # item = item.replace(" ", "")

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

                # matchdenddemiliter = re.search(r'[ =]*\{', ceassign)

                # ------------- END Skipping area

                if not flag_skip:
                    # replace = by !=
                    ceassign = ceassign.replace("=", "!=")
                    ceassign = ceassign.replace(",", "")

                    # print("\t => " + ceassign)
                    listnewassign.append(ceassign)

        # sys.exit()
        # generating __ASSUME
        txtassume = ' && '.join(listnewassign)
        self.assumeset = "__ESBMC_assume( " + txtassume + " );"

        return True


    def addassumeinprogram(self, _cprogrampath, _linenumtosetassume):
        # print(_linenumtosetassume)
        # sys.exit()

        fileprogram = open(_cprogrampath, "r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        # generate data about the functions
        self.list_beginnumfuct = self.getnumbeginfuncts(_cprogrampath)

        # TODO: it is best to write this new instance in the original path of the program
        newfile = open("/tmp/new_instance.c", "w")

        i = 0
        while i < len(listfilec):

            # identify where to write the new assume from CE
            if i == _linenumtosetassume:
                newfile.write(self.assumeset + "\n")

            # just write the new code
            # newfile.write(str(i)+"->"+listfilec[i])
            newfile.write(listfilec[i])



            # identifying the function pointed in the CE state
            # print(self.list_beginnumfuct)

            # if i in self.list_beginnumfuct.keys():
            # if self.list_beginnumfuct[i].strip() == self.statecurrentfunct.strip():
            # print(self.statecurrentfunct, "<<<<<<<<,")



            # matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
            # if matchassumeanot:
            # flagstop = False
            # while matchassumeanot and not flagstop:
            # #print(listfilec[i],end="")
            # newfile.write(listfilec[i])
            # if i <= len(listfilec):
            # i += 1
            # matchassumeanot = re.search(r'__ESBMC_assume', listfilec[i])
            # else:
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

        """
         Applying k-induction algorithm
         The K (unwind) should be defined.
         >> (1) Checking base-case, i.e., there is a counterexample?
         $ esbmc_v24 --64 --base-case --unwind 1 main.c
         >> Only if there is NOT counterexample (2) increase k = k +1
         $ esbmc_v24 --64 --forward-condition --unwind 2 main.c
         >> Only if in the (2) the result is: "The forward condition is unable to prove the property"
         $ esbmc_v24 --64 --inductive-step --show-counter-example --unwind 2 main.c
         >> If the result was SUCCESUFUL then STOP verification
         >> Else
         >>     IF not k <= maxk and actualdepthverification <= maxdepthverification
         >>        # I.e., the next P' only is generated if the max k was reached
         >>        generate a new program P' with an ESBMC_ASSUME from the counterexample and then go to (1)

        :param _cprogrampath: the program <P'> with: the safety property <Fi> and invariants <I>
        :return:
            - "TRUE"    if there is no path that violates the safety property
            - "FALSE"   if there exists a path that violates the safety property
            - "UNKNOWN" if does not succeed in computing and answer "TRUE" or "FALSE"
        """
        actual_detphver = 1
        actual_ce = "/tmp/ce_kinduction.txt"
        last_ce = "/tmp/last_ce_kinduction.txt"

        # TODO: Create an approach to check only if the k-induction,
        # and if we not find the solution to certain K then try to use the counterexample


        if self.debug:
            print(">> Starting the verification of the P\' program")
        # Checking if we reached the MAX k defined
        while self.esbmc_bound <= self.maxk and actual_detphver <= self.maxdepthverification:

            if self.debug:
                print("\t -> Actual k = " + str(self.esbmc_bound))

            if os.path.isfile(actual_ce):
                shutil.copyfile(actual_ce, last_ce)

            if self.debug:
                print("\t\t Status: checking base case")
            # >> (1) Checking base-case, i.e., there is a counterexample?
            # e.g., $ esbmc_v24 --64 --base-case --unwind 5 main.c
            commands.getoutput(self.esbmcpath + " " + self.esbmc_arch + " " +
                               self.esbmc_solver_op + " " +
                               self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                               self.esbmc_extra_op + " " +
                               self.esbmc_basecase_op + " " +
                               _cprogrampath + " &> " + actual_ce)

            # TODO: handling with error solvers
            # sys.exit()


            # >> (1) Identifying if it was generated a counterexample
            statusce_basecase = int(commands.getoutput("cat " + actual_ce + " | grep -c \"VERIFICATION FAILED\" "))
            if statusce_basecase > 0:
                # show counterexample
                os.system("cat " + actual_ce)
                return "FALSE"

            else:
                # >> (2) Only if there is NOT counterexample, then  increase k = k +1
                # only to check if any crash was generated
                statusce_basecase_nobug = int(commands.getoutput("cat " + actual_ce +
                                                                 " | grep -c " +
                                                                 "\"No bug has been found in the base case\" "))
                if statusce_basecase_nobug > 0:
                    # increase k
                    self.esbmc_bound += 1
                    if self.debug:
                        print("\t\t Status: checking forward condition")
                    # Checking the forward condition
                    # $ esbmc_v24 --64 --forward-condition --unwind 2 main.c
                    commands.getoutput(self.esbmcpath + " " + self.esbmc_arch + " " +
                                       self.esbmc_solver_op + " " +
                                       self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                                       self.esbmc_extra_op + " " +
                                       self.esbmc_forwardcond_op + " " +
                                       _cprogrampath + " &> " + actual_ce)

                    # Checking if it was possible to prove the property
                    #
                    statusce_forwardcond = int(commands.getoutput("cat " + actual_ce +
                                                                  " | grep -c " +
                                                                  "\"The forward condition is unable to prove the property\" "))
                    if statusce_forwardcond == 0:
                        # The property was proved
                        # print("True")
                        return "TRUE"

                    else:
                        # The property was NOT proved
                        if self.debug:
                            print("\t\t Status: checking inductive step")
                        # >> (3) Only if in the (2) the result is: "The forward condition is unable to prove the property"
                        # Checking the inductive step
                        # $ esbmc_v24 --64 --inductive-step --show-counter-example --unwind 2 main.c
                        commands.getoutput(self.esbmcpath + " " + self.esbmc_arch + " " +
                                           self.esbmc_solver_op + " " +
                                           self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                                           self.esbmc_extra_op + " " +
                                           self.esbmc_inductivestep_op + " " +
                                           _cprogrampath + " &> " + actual_ce)

                        # checking CE
                        statusce_inductivestep = int(commands.getoutput("cat " + actual_ce +
                                                                        " | grep -c " +
                                                                        "\"VERIFICATION FAILED\" "))
                        # >> If the result was SUCCESUFUL then STOP verification
                        if statusce_inductivestep == 0:
                            # print("True")
                            return "TRUE"

                        else:
                            if not self.esbmc_bound <= self.maxk and \
                               actual_detphver <= self.maxdepthverification:

                                # reset k, i.e., the bound go back to 1
                                self.esbmc_bound = 1

                                # >> Else generate an ESBMC_ASSUME with the counterexample then go to (1)
                                # generating a new ESBMC assume
                                if self.debug:
                                    print("\t\t -> It was reached the MAX k")

                                    print("\t\t Status: generating a new ESBMC assume")
                                # print("\t - Get data from CE in the last valid state:")
                                # Possible BUG cuz the PLACE where the assume is added
                                if not self.getlastdatafromce(actual_ce):
                                    # >> UNKNOWN
                                    print("ERROR. NO DATA from counterexample! Sorry about that.")
                                    return "UNKNOWN"

                                # print("\t - New assume generated: \n" + self.assumeset)
                                # print("\t - Instrument program with assume ...")
                                # Getting the last valid location in the counterexample to add the assume
                                linenumtosetassume = self.getlastlinenumfromce(actual_ce)
                                # Adding in the new instance of the analyzed program (P') the new assume
                                # generated from the counterexample
                                _cprogrampath = self.addassumeinprogram(_cprogrampath, linenumtosetassume)

        # >> END-WHILE
        # >> UNKNOWN
        print("MAX k (" + str(self.maxk) + ") reached. ")
        return "UNKNOWN"


    @staticmethod
    def getnumbeginfuncts(_cfilepath):

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
                listbeginnumfuct[int(matchlinefuct.group(2)) - 1] = matchlinefuct.group(1)

        return listbeginnumfuct

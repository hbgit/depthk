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
import time


class DepthEsbmcCheck(object):
    def __init__(self):
        self.list_beginnumfuct = {}
        self.nameoforicprogram = ""
        self.assumeset = ''
        self.dict_dataassume = {} # e.g., {'cs':[162,"assume(x>0)"],'s1':[170:"assume(y < 100)"]}
        self.dict_extraassume = {}
        self.statecurrentfunct = ''
        # depth check options
        self.debug = False
        self.forceassume = False
        self.moreonecheckbasecase = False
        #self.disableuse_ce = False
        self.maxk = 15
        self.maxdepthverification = 25
        self.en_kparalell = False
        self.esbmcpath = ''
        self.esbmc_bound = 1
        self.esbmc_unwind_op = "  --state-hashing --unwind"
        self.esbmc_memlimit_op = ""
        self.esbmc_timeout_op = "15m"
        #self.esbmc_nolibrary = "--no-library"
        self.esbmc_nolibrary = ""
        self.esbmc_extra_op = ""
        self.esbmc_solver_op = "--z3"
        self.use_counter_example = False
        # k-induction options
        self.esbmc_basecase_op = "--base-case"
        self.esbmc_forwardcond_op = "--forward-condition"
        self.esbmc_inductivestep_op = "--inductive-step "

        self.dldv_error = " "
        self.dassert = " "
        self.no_bounds_check = ""
        self.no_pointer_check = ""
        self.no_div_by_zero_check = ""
        self.no_assertions = ""
        self.memory_leak_check = ""
        self.no_inductive_step = False
        self.quiet = " "
        self.context_switch = ""
        self.force_malloc = " --force-malloc-success "
        self.is_memory_safety = False
        self.is_termination = False
        self.overflow_check = " "
        
        self.root_path = ""

        self.enable_witnesschecker = True
        self.file2witness = ""
        self.cpachecker_path = commands.getoutput("readlink -f .") +  "/modules/CPAchecker/"
        self.ua_path = commands.getoutput("readlink -f .") +  "/modules/UAutomizer-linux/"
        self.listproperty = ""
        self.original_file = ""
        self.start_time = 0

    @staticmethod
    def getlastlinenumfromce(_esbmccepath, _indexliststartsearch):
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
        #i = len(listfilece) - 1
        #flagstartpoint = False

        i = _indexliststartsearch

        while i < len(listfilece):

            matchstate = re.search(r'State[ ]+[0-9]+', listfilece[i])
            if matchstate:
                # identifying if the state has some valid line number
                matchsline = re.search(r'line[ ]+([0-9]+)', listfilece[i])
                if matchsline:
                    # print(matchsline.group(1))
                    # sys.exit()
                    return int(matchsline.group(1)) - 1

            i += 1


        # while i >= 0:
        #
        #     # identify Violated property point in the CE
        #     matchvp = re.search(r'Violated property', listfilece[i])
        #     if matchvp and not flagstartpoint:
        #         # identifying the first start to jump and then start to search
        #         # line number by state
        #         i -= 2  # jump line with ---------
        #         while not re.search(r'State[ ]+[0-9]+', listfilece[i]):
        #             i -= 1
        #
        #         # how is the first state then we jump it
        #         i -= 1
        #         flagstartpoint = True
        #
        #     matchstate = re.search(r'State[ ]+[0-9]+', listfilece[i])
        #     if matchstate:
        #
        #         # identifying if the state has some valid line number
        #         matchsline = re.search(r'line[ ]+([0-9]+)', listfilece[i])
        #         if matchsline:
        #             # print(matchsline.group(1))
        #             # sys.exit()
        #             return int(matchsline.group(1)) - 1
        #
        #     i -= 1


    def translatecedata(self, _listlinesfromce):

        # removing blank spaces to deal with arrays
        countb = 0
        while countb < len(_listlinesfromce):
            _listlinesfromce[countb] = _listlinesfromce[countb].replace(" ", "")
            countb += 1

        textcejoinspace = ' '.join(_listlinesfromce)

        # print(textcejoinspace)
        # sys.exit()

        listassign = textcejoinspace.split(" ")
        # listassign = re.split('\.c[:]+.[^]+')

        listnewassign = []
        for item in listassign:
            #
            item = item.strip()
            #print("---------------------", item)
            # get only the assignment in the CE
            # skipassign = False

            # item = item.replace("}", "")
            # item = item.replace("{", "")
            # item = item.replace(" ", "")

            #matchassign = re.search(r'(.[^\}\{:]+)$', item)
            #if matchassign:
            # clean assignment
            #ceassign = matchassign.group(1).strip()
            ceassign = item.strip()
            #print(">>>> ", ceassign)
            #
            #ceassign = ceassign.replace(":", "")
            # Getting only the assignment
            matchlastpart = re.search(r"(.[^:]+)$", ceassign)
            if matchlastpart:
                ceassign = matchlastpart.group(1)
                # Cuz the greed .* now remove this ":"
                ceassign = ceassign.replace(":", "")
                #print(ceassign)

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
                    # The last symbol from CS$ state
                    ceassign = ceassign.replace("}", "")

                    # ------- This for ESBMC
                    # WARNNING: we need to improve this because the user can define TRUE and FALSE in the program
                    # Translate result from _Bool nondet_bool();
                    match_bool = re.search(r"(TRUE)|(FALSE)", ceassign)
                    if match_bool:
                        try_bool = ceassign.split("!=")
                        if match_bool.group(1) is not None:
                            try_bool[1] = "1"
                        else:
                            try_bool[1] = "0"
                        ceassign = '!='.join(try_bool)

                    # FOR float 0f
                    match_float = re.search(r"f$", ceassign)
                    if match_float:
                        listfloat = ceassign.split("!=")
                        matchdotfloat = re.search(r"\.", listfloat[1])
                        if not matchdotfloat:
                            listfloat[1] = listfloat[1].replace("f", ".0")
                        else:
                            listfloat[1] = listfloat[1].replace("f", "")

                        ceassign = '!='.join(listfloat)


                    #print("\t => " + ceassign)
                    listnewassign.append(ceassign)

        return listnewassign



    def handletextfrom_ce(self, _stringtxtce, _enablescopecheck):

        # handle text state get from CE
        # .c::new_main::main::1::SIZE=7
        # preprocessing CE text
        # splitting to remove blank spaces
        listassign = _stringtxtce.split(".")

        #print(">>>>", listassign)
        listdelassig = []
        for index, item in enumerate(listassign):
            matchassignvalid = re.search(r"=", item)
            if matchassignvalid:
                listdelassig.append(index)

        tmplist = []
        for i in listdelassig:
            tmplist.append(listassign[i])
        listassign = tmplist
        del tmplist
        #print(">>>>", listassign)

        # TODO: how consider correct refences, but the txt from ce has no line number
        # e.g.,
        # State 30  thread 0
        # <main invocation>
        # ----------------------------------------------------
        # cs$1={ .c::MAX=2,.c::main_new_depthk_09_28_09::main::1::cont=0
        # HIP1: Create a new dict with this data and then check if the function
        #      that is pointed in the txt is in the scope of the line that will
        #      identified in the next steps self.dict_extraassume

        namefuncref_exas = ""
        flag_exas = False
        count_exas = 1

        if _enablescopecheck:
            # only consider :: < 2
            #for i, item in enumerate(listassign):
            i = 0
            listselectassign = []
            while i < len(listassign):
                tmps = listassign[i].strip().split("::")
                # 3 cuz the white space when it is applied the split
                if not len(tmps) > 3:
                    listselectassign.append(listassign[i])
                else:
                    # >> save this data to be possible used as assume
                    # identify function
                    matchfuncref = re.search(r"[a-zA-Z0-9_.-]+::([a-zA-Z0-9_.-]+)::([a-zA-Z0-9_.-]+)::", listassign[i].strip())
                    if matchfuncref:
                        flag_exas = True
                        matchisnum = re.search(r"^[0-9]+$", matchfuncref.group(2))
                        if matchisnum:
                            self.dict_extraassume[count_exas] = [matchfuncref.group(1), listassign[i]]
                        else:
                            self.dict_extraassume[count_exas] = [matchfuncref.group(2), listassign[i]]
                        count_exas += 1


                i += 1

            listassign = listselectassign
            del listselectassign

        # Translate data from counterexample pre-selected
        listnewassign = self.translatecedata(listassign)

        # Translate data from counterexample that have possibility to selected
        if flag_exas:
            #listselecassign_exas = self.translatecedata(listassign)
            list_keys2remove_exas = []
            for key, value in self.dict_extraassume.items():
                resulttrans_exas = self.translatecedata([value[1]])
                #print(self.translatecedata([value[1]]))
                if len(resulttrans_exas) > 0:
                    self.dict_extraassume[key][1] = " ".join(resulttrans_exas)
                else:
                    list_keys2remove_exas.append(key)

            # Cleanning dictionary
            for key in list_keys2remove_exas:
                del self.dict_extraassume[key]

        # print(self.dict_extraassume)
        # sys.exit()
        return listnewassign


    def getfunctnamebylinenum(self, _linenum):

        _linenum += 1 # cuz we consider the ctags result
        keyslist = self.list_beginnumfuct.keys()
        keyslist.sort()
        #print(keyslist, _linenum)
        i = 0

        while i < len(keyslist):
            if i + 1 < len(keyslist):
                if _linenum >= keyslist[i] and _linenum < keyslist[i+1]:
                    return self.list_beginnumfuct[keyslist[i]]
            else:
                # the list has only onde value or is the end of the list
                if _linenum >= keyslist[i]:
                    return self.list_beginnumfuct[keyslist[i]]
            i += 1


    def checkscopeextraassume(self, _linenumber):
        listnewassign = []
        print(self.dict_extraassume)
        functnametoassu = self.getfunctnamebylinenum(_linenumber)
        if functnametoassu is not None:
            for key, value in self.dict_extraassume.items():
               if value[0] == functnametoassu:
                    listnewassign.append(value[1])

        return listnewassign


    @staticmethod
    def lastsearchbylinetoassume(_esbmccepath, _indexliststartsearch):

        filece = open(_esbmccepath, "r")
        listfilece = filece.readlines()
        filece.close()

        i = _indexliststartsearch
        savelastline = 0
        while i < len(listfilece):
            matchpoint = re.search(r'^c::', listfilece[i])
            matchlinepoint = re.search(r'line[ ]+([0-9]+)', listfilece[i])
            if matchpoint and matchlinepoint:
                savelastline = matchlinepoint.group(1)

            matchdotline = re.search(r'^-+', listfilece[i])
            if matchdotline and savelastline > 0:
                return int(savelastline) - 1

            # stop search
            matchstop = re.search(r'Violated property', listfilece[i])
            if matchstop:
                return 0


            i += 1
        return 0



    def getlastdatafromce(self, _esbmccepath):

        filece = open(_esbmccepath, "r")
        listfilece = filece.readlines()
        filece.close()

        # reading file from bottom to top
        i = len(listfilece) - 1
        flagstop = False
        cestatetext = ''
        flaghasstate = False

        # data to assumes
        ce_index2cs = 0
        ce_cs_line = 0
        ce_cs_data = ""

        # reset dictionary with data assumes
        self.dict_dataassume = {}

        # Gathering data from counter-example to CS
        while i >= 0 and not flagstop:
            # get the last state
            matchstate = re.search(r'cs\$[0-9]+', listfilece[i])
            if matchstate:
                ce_index2cs = i
                flaghasstate = True

                # >> Identifying if the State has the line number
                # we consider the following format
                # e.g., State 751  thread 0 line 9
                # If we not found we consider the next state the has that format
                tmpi = i - 2  # jump the actual line and start with -------
                while not re.search(r'^State[ ]*[0-9]*', listfilece[tmpi]):
                    tmpi -= 1

                # print(listfilece[tmpi])
                # tmpi += 1  # jump the line start with State ...
                # matchline_cs = re.search(r'line[ ]*([0-9]*)', listfilece[tmpi])
                # if matchline_cs:
                #     ce_cs_line = int(matchline_cs.group(1)) - 1

                #print(listfilece[tmpi])
                matchline_cs = re.search(r'line[ ]*([0-9]*)', listfilece[tmpi])
                if matchline_cs:
                    ce_cs_line = int(matchline_cs.group(1)) - 1

                flagkeepgettext = True
                while flagkeepgettext:
                    matchblankline = re.search(r'^$', listfilece[i])
                    if matchblankline:
                        flagkeepgettext = False
                    else:
                        cestatetext += listfilece[i]
                    i += 1
                flagstop = True
            i -= 1



        # identify the function name where is the state identified
        # TODO: fix this BUG, incorrect way to get the name of the function
        matchfunctname = re.search(r'[.]c[:]+new_[a-zA-Z_0-9]+[:]+([a-zA-Z_0-9]+):.*', cestatetext)
        if matchfunctname:
            self.statecurrentfunct = matchfunctname.group(1)


        # print("++++++++++++++++++++ ", cestatetext)
        # When we do not have CS$ state we try with other state
        if flaghasstate:
            # >> Handle txt get from CE - CS$

            listnewassign = self.handletextfrom_ce(cestatetext, True)

            # print(listnewassign)
            # print(self.getfunctnamebylinenum(8))
            # print(self.list_beginnumfuct)
            # BUG in extra assumes
            # print(self.dict_extraassume)
            # sys.exit()

            # WARNNING:  some value are not included because
            #            how we can not get the location where to add, then we avoid this value
            if len(listnewassign) > 0 or self.dict_extraassume:
                # >> Generating ASSUME to the last CS$
                # txtassume = ' && '.join(listnewassign)
                # identiying it was found the line number in the CS$
                # print("============================= ", ce_cs_line)
                if ce_cs_line > 0:

                    # print("Has line")
                    validdataexas = self.checkscopeextraassume(ce_cs_line)
                    # Validating the extra assume identified
                    # functnametoassu = self.getfunctnamebylinenum(ce_cs_line)
                    # if functnametoassu is not None:
                    #     for key, value in self.dict_extraassume.items():
                    #        if value[0] == functnametoassu:
                    #             listnewassign.append(value[1])

                    # print(validdataexas)
                    if len(validdataexas) > 0:
                        listnewassign = listnewassign + validdataexas

                    if len(listnewassign) > 0:
                        txtassume = ' && '.join(listnewassign)
                        self.dict_dataassume[ce_cs_line] = "__ESBMC_assume( " + txtassume + " );"

                else:
                    # Identify a possible location to add the ASSUME to CS$
                    ce_cs_line = self.getlastlinenumfromce(_esbmccepath, ce_index2cs)
                    #print("Last line found: " + str(ce_cs_line))
                    validdataexas = self.checkscopeextraassume(ce_cs_line)
                    # functnametoassu = self.getfunctnamebylinenum(ce_cs_line)
                    # if functnametoassu is not None:
                    #     for key, value in self.dict_extraassume.items():
                    #        if value[0] == functnametoassu:
                    #             listnewassign.append(value[1])

                    print(validdataexas)
                    if len(validdataexas) > 0:
                        listnewassign = listnewassign + validdataexas
                    else:
                        #print("Try one more time")
                        # Last try to identify a valid line number in the
                        # counterexample to add the assume
                        rslastline = self.lastsearchbylinetoassume(_esbmccepath, ce_index2cs)
                        #print("======= ", rslastline)
                        if rslastline > 0:
                            ce_cs_line = rslastline
                            validdataexas = self.checkscopeextraassume(ce_cs_line)
                            if len(validdataexas) > 0:
                                listnewassign = listnewassign + validdataexas


                    if len(listnewassign) > 0:
                        txtassume = ' && '.join(listnewassign)
                        self.dict_dataassume[ce_cs_line] = "__ESBMC_assume( " + txtassume + " );"

        #print(self.dict_dataassume)
        #sys.exit()
        #
        # >> Getting other data after the identification of CS$
        #    The State just are valid from here if has a valid line number
        countnxst = ce_index2cs
        nxt_state_line = 0

        while countnxst < len(listfilece):

            actual_funname = ""
            matchstate = re.search(r"^State[ ]*[0-9]+", listfilece[countnxst])
            matchstatebuitin = re.search(r"<built-in>", listfilece[countnxst])
            matchstatefunc = re.search(r"function[ ]+([a-zA-Z0-9_]+)", listfilece[countnxst])

            matchstateline = re.search(r"line[ ]*([0-9]+)", listfilece[countnxst])
            # TODO if in the CE text has reference c:: get the last line to this value
            preciselinenum = 0
            preciselinenum = self.lastsearchbylinetoassume(_esbmccepath, countnxst)
            # print("==========> ", preciselinenum)

            # Ignore state with State 2 file <built-in> line 54 thread 0
            # We match with matchstatefunc, cuz if we do not found is possible that
            # the variable is in a global scope and then we can add an assume
            if matchstate and matchstateline and matchstatefunc and not matchstatebuitin:
                if preciselinenum > 0:
                    nxt_state_line = preciselinenum
                else:
                    nxt_state_line = int(matchstateline.group(1))

                # get name function
                if matchstatefunc:
                    actual_funname = matchstatefunc.group(1).strip()

                while not re.search(r"^-+", listfilece[countnxst]):
                    countnxst += 1
                countnxst += 1  # jump line start with --------------

                # Getting ce txt
                ce_next_state_txt = ""
                while not re.search(r"^$", listfilece[countnxst]):
                    ce_next_state_txt += listfilece[countnxst].strip()
                    # clean ce txt
                    match_detailvalue = re.search(r"[ ]+\(.+\)$", ce_next_state_txt)
                    if match_detailvalue:
                        ce_next_state_txt = re.sub(r"[ ]+\(.+\)$", "", ce_next_state_txt)
                    countnxst += 1

                # Validating counterexample txt
                matchresult_ce = re.search(r"Violated property:", ce_next_state_txt)
                if not matchresult_ce:
                    flag_genassume_nx = True
                    # Checking if has reference scope
                    matchrefscope = re.search(r":", ce_next_state_txt)
                    if matchrefscope:
                        # validating scope
                        #matchisinscope = re.search(r"actual_funname", ce_next_state_txt)
                        if not actual_funname in ce_next_state_txt:
                            flag_genassume_nx = False

                    if flag_genassume_nx:
                        #print(cenxstate_txt)
                        # >> Handle txt get from CE - next states
                        listnewassign = self.handletextfrom_ce(ce_next_state_txt, False)
                        if len(listnewassign) > 0:
                            # >> Generating ASSUME to the next states
                            txtassume = ' && '.join(listnewassign)
                            self.dict_dataassume[nxt_state_line] = "__ESBMC_assume( " + txtassume + " );"

            countnxst += 1

        # print(" ")
        # print(self.dict_dataassume)
        # sys.exit()

        # checking if we have assumes to add in the new instance of the program
        if self.dict_dataassume:
            return True
        else:
            return False


    def addassumeinprogram(self, _cprogrampath):
        # print(_linenumtosetassume)
        # sys.exit()

        fileprogram = open(_cprogrampath, "r")
        listfilec = fileprogram.readlines()
        fileprogram.close()

        # generate data about the functions
        self.list_beginnumfuct = self.getnumbeginfuncts(_cprogrampath)

        #newfile = open("/tmp/new_instance.c", "w")
        newfile = open(_cprogrampath, "w")

        i = 0
        while i < len(listfilec):

            # identify where to write the new assume from CE
            #if i == _linenumtosetassume:
            if self.dict_dataassume.has_key(i):
                #newfile.write(self.assumeset + "\n")
                newfile.write(self.dict_dataassume[i] + "\n")

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
            # flagstop = True
            #
            # #print(self.assumeset+"\n")
            # newfile.write(self.assumeset + "\n \n \n")
            # else:
            #     #print(listfilec[i],end="")
            #     newfile.write(listfilec[i])
            i += 1

        newfile.close()

        return _cprogrampath


    @staticmethod
    def savelist2file(_pathfile, _list2file):
        filewrite = open(_pathfile, "w")
        for item in _list2file:
            filewrite.write(item)


    def isdefiniedmemlimit(self):
        if not self.esbmc_memlimit_op:
            return " "
        else:
            return "--memlimit " + self.esbmc_memlimit_op + " "

    @staticmethod
    def hasincorrectopesbmc(_esbmcoutput):
        statusc = int(commands.getoutput("cat " + _esbmcoutput + " | grep -c \"Unrecognized option\" "))
        if statusc > 0:
            return True
        else:
            return False

    @staticmethod
    def hastimeoutfromesbmc(_esbmcoutput):
        statusc = int(commands.getoutput("cat " + _esbmcoutput + " | grep -c \"Timed out\" "))
        if statusc > 0:
            #os.system("cat " + _esbmcoutput)
            print(" ")
            print("VERIFICATION UNKNOWN - Time Out")
            return "UNKNOWN"

    @staticmethod
    def hassuccessfulfromesbmc(_esbmcoutput):
        statusc = int(commands.getoutput("cat " + _esbmcoutput + " | grep -c \"VERIFICATION SUCCESSFUL\" "))
        if statusc > 0:
            return True
        else:
            return False


    def checkwithkparallel(self, _cprogrampath, _actual_ce, _listtmpfiles):
        result_parallel = commands.getoutput(self.esbmcpath +  " " +
                                             self.esbmc_solver_op + " " +
                                             "--k-induction-parallel" + " " +
                                             self.isdefiniedmemlimit() +
                                             "--timeout " + self.esbmc_timeout_op + " " +
                                             self.esbmc_nolibrary + " " +
                                             self.esbmc_extra_op + " " +
                                             _cprogrampath)

        self.savelist2file(_actual_ce, result_parallel)

        # Checking TRUE
        status_true = int(commands.getoutput("tail -n 3 " + _actual_ce + " | grep -c \"VERIFICATION SUCCESSFUL\" "))
        if status_true > 0:
            #os.system("cat " + _actual_ce)
            print(" ")
            self.cleantmpfiles(_listtmpfiles)
            return "TRUE"

        # Checking FAILED
        status_failed = int(commands.getoutput("tail -n 3 " + _actual_ce + " | grep -c \"VERIFICATION FAILED\" "))
        if status_failed > 0:
            #os.system("cat " + _actual_ce)
            self.cleantmpfiles(_listtmpfiles)
            return "FAILED"

        # Checking TO
        status_to = int(commands.getoutput("tail -n 3 " + _actual_ce + " | grep -c \"Timed out\" "))
        if status_to > 0:
            #os.system("cat " + _actual_ce)
            print(" ")
            self.cleantmpfiles(_listtmpfiles)
            return "Timed Out"
        else:
            #os.system("cat " + _actual_ce)
            print(" ")
            self.cleantmpfiles(_listtmpfiles)
            return "UNKNOWN"




    @staticmethod
    def cleantmpfiles(_listfiles2delete):
        for filepath in _listfiles2delete:
            if os.path.exists(filepath):
                os.remove(filepath)

    def kinductioncheck(self, _cprogrampath):
        #if(self.is_memory_safety or self.is_termination or self.overflow_check != ""):
        #if(self.is_memory_safety or self.is_termination):
        #    return "UNKNOWN"

        listtmpfiles = []
        actual_detphver = 1

        actual_ce  = ""
        last_ce = ""
        if os.path.splitext(_cprogrampath)[1] == '.c':
            actual_ce = _cprogrampath.replace(".c", ".acce")
            last_ce = _cprogrampath.replace(".c", ".ltce")
        else:
            actual_ce = _cprogrampath.replace(".i", ".acce")
            last_ce = _cprogrampath.replace(".i", ".ltce")
        listtmpfiles.append(actual_ce)
        #actual_ce = "/tmp/ce_kinduction.txt"

        listtmpfiles.append(last_ce)

        lastresult = [False,"",""] # flag, result, step

        # generate data about the functions
        self.list_beginnumfuct = self.getnumbeginfuncts(_cprogrampath)

        result = ""#Get the result of verification

        if self.debug:
            print(">> Starting the verification of the P\' program")

        #try:
        #    file = open(_cprogrampath, "r")
        #    lines = file.readlines()
        #    res = [y for y in lines if "safe_write" in y or "fflush_all" in y or "bb_show_usage" in y or "bb_error_msg_and_die" in y]
        #    if(res is not None and len(res) > 0):
        #        print("kinductioncheck: Busybox file.")
        #        return "UNKNOWN"
        #except:
        #    print("kinductioncheck: Fails on open file.")
        #finally:
        #    file.close()

        if self.enable_witnesschecker:
            folderPath =  self.root_path + "/graphml/"
            if not os.path.exists(folderPath):
                os.makedirs(folderPath)

            self.file2witness = folderPath + os.path.basename(_cprogrampath) + ".graphml"
            
            self.esbmc_witness_op = " --witness-output " + str(self.file2witness) + " --witness-programfile " + self.original_file + " "
        else:
            self.esbmc_witness_op = ""

        # Checking if we reached the MAX k defined
        while self.esbmc_bound <= self.maxk and actual_detphver <= self.maxdepthverification:

            if self.en_kparalell:
                if self.debug:
                    print("\t -> Status: checking with k-induction parallel")
                result = self.checkwithkparallel(_cprogrampath, actual_ce, listtmpfiles)
                break

            else:
                # Sequencial
                if self.debug:
                    print("\t -> Actual k = " + str(self.esbmc_bound))

                if os.path.isfile(actual_ce):
                    shutil.copyfile(actual_ce, last_ce)

                if self.debug:
                    print("\t\t Status: checking base case")


                # checking we are in the force last check
                statusce_basecase = self.execBaseCase(self.original_file, actual_ce, lastresult)

                if(statusce_basecase == "UNKNOWN" or statusce_basecase <> "no bug has been found"):
                    result = statusce_basecase
                    break
                else:

                    # checking we are in the force last check
                    # This last force is always to TRUE
                    if lastresult[0]:
                        #os.system("cat " + last_ce)
                        print(" ")
                        z3Error = int(commands.getoutput("cat " + last_ce +
                                                                     " | grep -c " +
                                                                     "\"Z3 error\" "))
                        if(z3Error > 0):
                            result = "UNKNOWN"
                        else:
                            if not self.is_termination:
                                cpachecker_ops = self.configureCPACheckerPath()
                                result = self.execCPAChecker(self.original_file, cpachecker_ops)
                                endresult = self.check_witnessresult(result)
                                if "IS NOT SUPPORTED" in result.upper() or "UNSUPPORTED C FEATURE" in result.upper() \
                                        or "UNSUPPORTED FEATURE" in result.upper() or result == "" \
                                        or "UNRECOGNIZED C CODE" in result:

                                    ua_ops = self.configureUAPath()
                                    result = self.execUA(self.original_file, ua_ops)
                                    endresult = self.check_witnessresultUA(result)

                                    if endresult == "TRUE":
                                        lastresult[1] = "TRUE"
                                    elif endresult == "FALSE":
                                        lastresult[1] =  "FALSE"
                                    else:
                                        lastresult[1] = "UNKNOWN"
                                elif endresult == "TRUE":
                                    lastresult[1] = "TRUE"
                                elif endresult == "FALSE":
                                    lastresult[1] = "FALSE"
                                else:
                                    lastresult[1] = "UNKNOWN"
                            else:
                                 ua_ops = self.configureUAPath()
                                 result = self.execUA(self.original_file, ua_ops)
                                 endresult = self.check_witnessresultUA(result)

                                 if endresult == "TRUE":
                                    lastresult[1] = "TRUE"
                                 elif endresult == "FALSE":
                                    lastresult[1] = "FALSE"
                                 else:
                                    lastresult[1] = "UNKNOWN"
                            result = "\t\t Last adopted - " + lastresult[2] + "\n" + lastresult[1] + "\n"

                        self.cleantmpfiles(listtmpfiles)
                        break

                    # >> (2) Only if there is NOT counterexample, then  increase k = k +1
                    # only to check if any crash was generated
                    statusce_basecase_nobug = int(commands.getoutput("cat " + actual_ce +
                                                                     " | grep -c " +
                                                                     "\"No bug has been found in the base case\" "))
                    if statusce_basecase_nobug > 0:

                        status_forwardcond = self.execForwardCondition(self.original_file, actual_ce, lastresult, listtmpfiles)

                        if(status_forwardcond == "TRUE" or status_forwardcond == "UNKNOWN"):
                            result = status_forwardcond
                            break
                        else:
                            if self.no_inductive_step:
                                continue
                            status_inductiveStep = self.execInductiveStep(_cprogrampath, actual_ce, lastresult, listtmpfiles, actual_detphver)
                            if(status_inductiveStep == ""):
                                continue
                            else:
                                result = status_inductiveStep
                                break
                    else:
                        # Some ERROR was identified in the verification of base-case
                        os.system("cat " + actual_ce)
                        print(" ")
                        self.cleantmpfiles(listtmpfiles)
                        print("ERROR. It was identified an error in the verification of base-case")
                        self.esbmc_bound = self.esbmc_bound + 1

        # >> END-WHILE
        # >> UNKNOWN
        # os.system("cat " + actual_ce)
        self.cleantmpfiles(listtmpfiles)

        return result

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
                listbeginnumfuct[int(matchlinefuct.group(2)) - 1] = matchlinefuct.group(1).strip()

        return listbeginnumfuct

    def configureUAPath(self):
        return "./Ultimate.py " + self.listproperty + " 64bit "

    def configureCPACheckerPath(self):

        if(self.is_memory_safety):
            return "./scripts/cpa.sh -disable-java-assertions -heap 10000m -sv-comp17--memorysafety.properties " \
                    " -spec " + self.listproperty + " "
        if(self.is_termination):
            return "./scripts/cpa.sh -disable-java-assertions -heap 10000m -sv-comp17--termination " \
                    " -spec " + self.listproperty + " "

        if(self.overflow_check):
            return "./scripts/cpa.sh -disable-java-assertions -heap 10000m -sv-comp17--overflow.properties " \
                    " -spec " + self.listproperty + " "

        return  "./scripts/cpa.sh  -timelimit 900 -disable-java-assertions -heap 10000m -sv-comp17 " \
                    " -spec " + self.listproperty + " "

        #return "./scripts/cpa.sh -noout -skipRecursion " \
        #       " -heap 10000M -predicateAnalysis " \
        #       " -setprop cpa.composite.aggregateBasicBlocks=false -setprop cfa.simplifyCfa=false " \
        #       " -setprop cfa.allowBranchSwapping=false -setprop cpa.predicate.ignoreIrrelevantVariables=false " \
        #       " -setprop counterexample.export.assumptions.assumeLinearArithmetics=true " \
        #       " -setprop counterexample.export.assumptions.includeConstantsForPointers=false -setprop counterexample.export.graphml=violation-witness.graphml " \
        #       " -setprop counterexample.export.compressErrorWitness=false "\
        #       " -spec " + self.listproperty + " "

    def execCPAChecker(self, _cprogrampath, cpachecker_ops):
        cwd = os.getcwd()
        os.chdir(self.cpachecker_path)

        elapsed_time = (int) (round(time.time() - self.start_time))
        remaining_time = 895 - elapsed_time
        result_witness = ""
        if (remaining_time > 0):
            cpacommand = "timeout  " + str(remaining_time) + " " + cpachecker_ops  + _cprogrampath
            result_witness =  commands.getoutput(cpacommand)
        if self.debug:
            print(result_witness)
        os.chdir(cwd)

        return result_witness.upper()

    def execUA(self, _cprogrampath, ua_ops):
        cwd = os.getcwd()
        os.chdir(self.ua_path)

        elapsed_time = (int) (round(time.time() - self.start_time))
        remaining_time = 895 - elapsed_time
        result_witness = ""
        if (remaining_time > 0):
            uacommand = "timeout  " + str(remaining_time) + " " + ua_ops + _cprogrampath

	if (self.debug):
            print(uacommand)

            result_witness = commands.getoutput(uacommand)
        if self.debug:
            print(result_witness)
        os.chdir(cwd)

        return result_witness.upper()

    def execBaseCase(self, _cprogrampath, actual_ce, lastResult):
        if(lastResult[0]):
            if (self.esbmc_bound < 25):
                self.esbmc_bound = 25
            elif(self.esbmc_bound < 35):
                self.esbmc_bound = 50
            else:
                return "no bug has been found"

        if self.esbmc_bound <= self.maxk:
            if(self.debug):
                print(self.esbmcpath + " " +
                     self.esbmc_solver_op + " " +
                     self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                     self.isdefiniedmemlimit() +
                     self.esbmc_witness_op +
                     "--timeout " + self.esbmc_timeout_op + " " +
                     self.esbmc_nolibrary + " " +
                     self.esbmc_extra_op + " " +
                     self.overflow_check +
                     self.dldv_error +
                     self.dassert +
                     self.no_bounds_check +
                     self.no_pointer_check +
                     self.no_div_by_zero_check +
                     self.no_assertions +
                     self.quiet +
                     self.context_switch +
                     self.force_malloc +
                     self.memory_leak_check +
                     self.esbmc_basecase_op + " " +
                     _cprogrampath)

            result_basecase = commands.getoutput(self.esbmcpath + " " +
                                                 self.esbmc_solver_op + " " +
                                                 self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                                                 self.isdefiniedmemlimit() +
                                                 self.esbmc_witness_op +
                                                 "--timeout " + self.esbmc_timeout_op + " " +
                                                 self.esbmc_nolibrary + " " +
                                                 self.esbmc_extra_op + " " +
                                                 self.overflow_check +
                                                 self.dldv_error +
                                                 self.dassert +
                                                 self.no_bounds_check +
                                                 self.no_pointer_check +
                                                 self.no_div_by_zero_check +
                                                 self.no_assertions +
                                                 self.quiet +
                                                 self.context_switch +
                                                 self.force_malloc +
                                                 self.memory_leak_check +
                                                 self.esbmc_basecase_op + " " +
                                                 _cprogrampath)

            self.savelist2file(actual_ce, result_basecase)
            # Identify a possible timeout
            if self.hastimeoutfromesbmc(actual_ce) == "UNKNOWN":
                return "UNKNOWN"

            # >> (1) Identifying if it was generated a counterexample
            statusce_basecase = int(commands.getoutput("cat " + actual_ce + " | grep -c \"VERIFICATION FAILED\" "))

            if statusce_basecase > 0:
                if self.is_termination:
                    ua_ops = self.configureUAPath()
                    result = self.execUA(self.original_file, ua_ops)
                    endresult = self.check_witnessresultUA(result)

                    if endresult == "TRUE":
                        return "TRUE"
                    elif endresult == "FALSE":
                        return "FALSE"
                    return "UNKNOWN"
                elif not self.is_memory_safety:
                    return "FALSE"
                else:
                    #PROPERTY_FORGOTTEN_MEMORY_TAG
                    if int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"dereference failure: forgotten memory\" ")) > 0:
                        return "FALSE \n dereference failure: forgotten memory"
                    #PROPERTY_INVALID_POINTER_TAG

                    elif int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"dereference failure: invalid pointer\" ")) > 0:
                        return "FALSE \n dereference failure: invalid pointer"
                    #PROPERTY_ARRAY_BOUND_VIOLATED_TAG

                    elif int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"dereference failure: array bounds violated\" ")) > 0:
                        return "FALSE \n dereference failure: array bounds violated"
                    elif int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"dereference failure: NULL pointer\" ")) > 0:
                        return "FALSE \n dereference failure: NULL pointer"
                    #PROPERTY_UNWIND_ASSERTION_LOOP_TAG
                    elif int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"unwinding assertion loop\" ")) > 0:
                        return "UNKNOWN"
                    return "UNKNOWN"
            return "no bug has been found"

    def execForwardCondition(self, _cprogrampath, actual_ce, lastResult, listtmpfiles):
        # increase k
        self.esbmc_bound += 1

        if self.debug:
            print("\t\t Status: checking forward condition")
        # Checking the forward condition
        # $ esbmc_v24 --64 --forward-condition --unwind 2 main.c
        if(self.debug):
            print(self.esbmcpath  + " " +
                self.esbmc_solver_op + " " +
                self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                self.isdefiniedmemlimit() +
                self.esbmc_witness_op +
                "--timeout " + self.esbmc_timeout_op + " " +
                self.esbmc_nolibrary + " " +
                self.esbmc_extra_op + " " +
                self.overflow_check +
                self.dldv_error +
                self.dassert +
                self.no_bounds_check +
                self.no_pointer_check +
                self.no_div_by_zero_check +
                self.no_assertions +
                self.quiet +
                self.context_switch +
                self.force_malloc +
                self.memory_leak_check +
                self.esbmc_forwardcond_op + " " +
                _cprogrampath)

        result_forwardcond = commands.getoutput(self.esbmcpath + " " +
                                                self.esbmc_solver_op + " " +
                                                self.esbmc_unwind_op + " " + str(self.esbmc_bound) + " " +
                                                self.isdefiniedmemlimit() +
                                                self.esbmc_witness_op +
                                                "--timeout " + self.esbmc_timeout_op + " " +
                                                self.esbmc_nolibrary + " " +
                                                self.esbmc_extra_op + " " +
                                                self.overflow_check +
                                                self.dldv_error +
                                                self.dassert +
                                                self.no_bounds_check +
                                                self.no_pointer_check +
                                                self.no_div_by_zero_check +
                                                self.no_assertions +
                                                self.quiet +
                                                self.context_switch +
                                                self.force_malloc +
                                                self.memory_leak_check +
                                                self.esbmc_forwardcond_op + " " +
                                                _cprogrampath)

        self.savelist2file(actual_ce, result_forwardcond)
        # Identify a possible timeout
        if self.hastimeoutfromesbmc(actual_ce) == "UNKNOWN":
            return "UNKNOWN"

        # Checking if it was possible to prove the property
        #
        statusce_forwardcond = int(commands.getoutput("cat " + actual_ce +
                                                      " | grep -c " +
                                                      "\"The forward condition is unable to prove the property\" "))
        if statusce_forwardcond == 0:
            if self.hassuccessfulfromesbmc(actual_ce):
                # The property was proved
                if self.moreonecheckbasecase:
                    lastResult[0] = True
                    lastResult[1] = "TRUE"
                    lastResult[2] = "Status: checking forward condition"
                    if self.debug:
                        print("\t\t v> Forcing last check in base case")
                    return "CONTINUE"
                else:
                    self.cleantmpfiles(listtmpfiles)
                    if not self.is_termination:

                        cpachecker_ops = self.configureCPACheckerPath()
                        result = self.execCPAChecker(self.original_file, cpachecker_ops)
                        endresult = self.check_witnessresult(result)

                        if "IS NOT SUPPORTED" in result.upper() or "UNSUPPORTED C FEATURE" in result.upper() or "UNSUPPORTED FEATURE" in result.upper() or result == "":
                            ua_ops = self.configureUAPath()
                            result = self.execUA(self.original_file, ua_ops)
                            endresult = self.check_witnessresultUA(result)

                            if endresult == "TRUE":
                                return "TRUE"
                            elif endresult == "FALSE":
                                return "FALSE"
                            else:
                                return "UNKNOWN"
                        if endresult == "TRUE":
                            return "TRUE"
                        elif endresult == "FALSE":
                            return "FALSE"
                    else:
                        ua_ops = self.configureUAPath()
                        result = self.execUA(self.original_file, ua_ops)
                        endresult = self.check_witnessresultUA(result)

                        if endresult == "TRUE":
                            return "TRUE"
                        elif endresult == "FALSE":
                            return "FALSE"

                    return "UNKNOWN"
        return ""

    def execInductiveStep(self, _cprogrampath, actual_ce, lastResult, listtmpfiles, actual_detphver):
        # The property was NOT proved
        if self.debug:
            print("\t\t Status: checking inductive step")
        # >> (3) Only if in the (2) the result is:
        # "The forward condition is unable to prove the property"
        # Checking the inductive step
        # $ esbmc_v24 --64 --inductive-step --show-counter-example --unwind 2 main.c
        if(self.debug):
            print(self.esbmcpath +  " " +
                  self.esbmc_solver_op + " " +
                  self.esbmc_unwind_op + " " +
                  str(self.esbmc_bound) + " " +
                  self.isdefiniedmemlimit() +
                  self.esbmc_witness_op +
                  "--timeout " + self.esbmc_timeout_op + " " +
                  self.esbmc_nolibrary + " " +
                  self.esbmc_extra_op + " " +
                  self.dldv_error +
                  self.dassert +
                  self.no_bounds_check +
                  self.no_pointer_check +
                  self.no_div_by_zero_check +
                  self.no_assertions +
                  self.quiet +
                  self.overflow_check +
                  self.context_switch +
                  self.force_malloc +

                  self.esbmc_inductivestep_op + " " +
                  _cprogrampath)

        result_inductivestep = commands.getoutput(self.esbmcpath + " " +
                                                  self.esbmc_solver_op + " " +
                                                  self.esbmc_unwind_op + " " +
                                                  str(self.esbmc_bound) + " " +
                                                  self.isdefiniedmemlimit() +
                                                  self.esbmc_witness_op +
                                                  "--timeout " + self.esbmc_timeout_op + " " +
                                                  self.esbmc_nolibrary + " " +
                                                  self.esbmc_extra_op + " " +
                                                  self.dldv_error +
                                                  self.dassert +
                                                  self.no_bounds_check +
                                                  self.no_pointer_check +
                                                  self.no_div_by_zero_check +
                                                  self.no_assertions +
                                                  self.quiet +
                                                  self.overflow_check +
                                                  self.context_switch +
                                                  self.force_malloc +
                                                  self.esbmc_inductivestep_op + " " +
                                                  _cprogrampath)

        # print(result_inductivestep)

        self.savelist2file(actual_ce, result_inductivestep)
        # Identify a possible timeout
        if self.hastimeoutfromesbmc(actual_ce) == "UNKNOWN":
            return "UNKNOWN"

        # checking CE
        statusce_inductivestep = int(commands.getoutput("cat " + actual_ce +
                                                        " | grep -c " +
                                                        "\"VERIFICATION FAILED\" "))
        # >> If the result was SUCCESUFUL then STOP verification
        if statusce_inductivestep == 0:
            if self.hassuccessfulfromesbmc(actual_ce):
                # print("True")
                if self.moreonecheckbasecase:
                    #lastresult = "TRUE"
                    lastResult[0] = True
                    lastResult[1] = "TRUE"
                    lastResult[2] = "Status: checking inductive step"
                    if self.debug:
                        print("\t\t > Forcing last check in base case")
                else:
                    self.cleantmpfiles(listtmpfiles)
                    if not self.is_termination:
                        cpachecker_ops = self.configureCPACheckerPath()
                        result = self.execCPAChecker(self.original_file, cpachecker_ops)
                        endresult = self.check_witnessresult(result)

                        if "IS NOT SUPPORTED" in result.upper() or "UNSUPPORTED C FEATURE" in result.upper() or "UNSUPPORTED FEATURE" in result.upper() or result == "":
                            ua_ops = self.configureUAPath()
                            result = self.execUA(self.original_file, ua_ops)
                            endresult = self.check_witnessresultUA(result)
                            if endresult == "TRUE":
                                return "TRUE"
                            elif endresult == "FALSE":
                                return "FALSE"
                            else:
                                return "UNKNOWN"
                        if endresult == "TRUE":
                            return "TRUE"
                        elif endresult == "FALSE":
                            return "FALSE"
                    else:
                        ua_ops = self.configureUAPath()
                        result = self.execUA(self.original_file, ua_ops)
                        endresult = self.check_witnessresultUA(result)

                        if endresult == "TRUE":
                            return "TRUE"
                        elif endresult == "FALSE":
                            return "FALSE"


                    return "UNKNOWN"
            else:
                # Some ERROR was identified in the verification of inductive-step

                status_unknown = int (commands.getoutput("cat " + actual_ce +
                                                        " | grep -c " +
                                                        "\"VERIFICATION UNKNOWN\" "))

                if status_unknown > 0:
                    return "UNKNOWN"

        return ""
    def check_witnessresult(self, _listresultwitness):
        list_recheck = _listresultwitness.split("\n")
        flag_cpa = False
        for line in list_recheck:
            #print(line)
            match_failed_CPA = re.search(r"VERIFICATION RESULT: FALSE", line.upper())
            match_nobug_CPA = re.search(r"VERIFICATION RESULT: TRUE", line.upper())
            if match_failed_CPA:
                return "FALSE"
            elif match_nobug_CPA:
                # "TRUE" not equal to actual
                return "TRUE"

        if not flag_cpa:
            return "UNKNOWN"

    def check_witnessresultUA(self, _listresultwitness):
        if ("RESULT:\nTRUE" in _listresultwitness):
            return "TRUE"
        elif ("RESULT:\nFALSE(TERM)" in _listresultwitness):
            return  "FALSE"
        return "UNKNOWN"

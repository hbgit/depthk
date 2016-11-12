#!/usr/bin/env python
# -*- coding: latin1 -*-

from __future__ import print_function

import sys
import os
import shutil

# -------------------------------------------------
# Main python program
# -------------------------------------------------

if __name__ == "__main__":

    if os.path.exists(sys.argv[1]):

        pathset = os.path.dirname(sys.argv[1])
        nameset = os.path.basename(sys.argv[1])
        if os.path.exists(nameset):
            shutil.rmtree(nameset)
            os.makedirs(nameset)
        else:
            os.makedirs(nameset)

        fileset = open(sys.argv[1], "r")
        linesset = fileset.readlines()
        fileset.close()
        for line in linesset:
            getsplit = line.split("/")
            pathsetdir = os.path.abspath(os.path.join(pathset,getsplit[0]))
            if os.path.exists(pathsetdir):
                print(pathsetdir, nameset + "/")
                os.system("cp -r " + pathsetdir + "/* " + nameset + "/")
                #shutil.copytree(pathsetdir, nameset + "/")


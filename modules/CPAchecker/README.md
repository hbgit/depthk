Getting Started with CPAchecker
===============================

Installation Instructions:  [`INSTALL.md`](INSTALL.md)
Develop and Contribute:     [`doc/Developing.md`](doc/Developing.md)

More documentation can be found in the [`doc`](doc) folder.

Prepare Programs for Verification by CPAchecker
-----------------------------------------------

All programs need to pre-processed with the C pre-processor,
i.e., they may not contain #define and #include directives.
You can enable pre-processing inside CPAchecker
by specifying -preprocess on the command line.
Multiple C files can be given and will be linked together
and verified as a single program (experimental feature).

CPAchecker is able to parse and analyze a large subset of (GNU)C.
If parsing fails for your program, please send a report to
cpachecker-users@googlegroups.com.

Verifying a Program with CPAchecker
-----------------------------------

1. Choose a source code file that you want to be checked.
   If you use your own program, remember to pre-process it as mentioned above.
   Example: doc/examples/example.c
   A good source for more example programs is the benchmark set of the
   [International Competition on Software Verification](http://sv-comp.sosy-lab.org/),
   which can be checked out from https://github.com/sosy-lab/sv-benchmarks.

2. If you want to enable certain analyses like predicate analysis,
   choose a configuration file. This file defines for example which CPAs are used.
   Standard configuration files can be found in the directory config/.
   Example: `config/predicateAnalysis.properties`
   The configuration of CPAchecker is explained in doc/Configuration.txt.

3. Choose a specification file (you may not need this for some CPAs).
   The standard configuration files use `config/specification/default.spc`
   as the default specification. With this one, CPAchecker will look for labels
   named `ERROR` (case insensitive) and assertions in the source code file.
   Other examples for specifications can be found in `config/specification/`

4. Execute `scripts/cpa.sh [ -config <CONFIG_FILE> ] [ -spec <SPEC_FILE> ] <SOURCE_FILE>`
   Either a configuration file or a specification file needs to be given.
   The current directory should be the CPAchecker project directory.
   Additional command line switches are described in doc/Configuration.txt.
   Example: `scripts/cpa.sh -config config/predicateAnalysis.properties doc/examples/example.c`
   This example can also be abbreviated to:
   `scripts/cpa.sh -predicateAnalysis doc/examples/example.c`
   A Java 1.8 compatible JVM is necessary. If it is not in your PATH,
   you need to specify it in the environment variable JAVA.
   Example: `export JAVA=/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java`
   for 64bit OpenJDK 8 on Ubuntu.
   On Windows (witout Cygwin), you need to use `cpa.bat` instead of `cpa.sh` (would work 
   within a Cygwin environment). Please take note that not all analysis 
   configurations are available for Windows; one reason for this is that not 
   all solvers (and other native libraries that are used for specific 
   abstract domains) are available for Windows.

5. Additionally to the console output, there will be several files in the directory output/:

 - `ARG.dot`: Visualization of abstract reachability tree (Graphviz format)
 - `cfa*.dot`: Visualization of control flow automaton (Graphviz format)
 - `reached.dot`: Visualization of control flow automaton with the abstract
    states visualized on top (Graphviz format)
 - `counterexample.msat`: Formula representation of the error path
 - `coverage.info`: Coverage information (similar to those of testing tools) in `Gcov` format
       Use the following command line to generate an HTML report as `output/index.html`:
       `genhtml output/coverage.info --output-directory output --legend`
 - `ErrorPath.*.txt`: A path through the program that leads to an error
 - `ErrorPath.*.assignment.txt`: Assignments for all variables on the error path.
 - `predmap.txt`: Predicates used by predicate analysis to prove program safety
 - `reached.txt`: Dump of all reached abstract states
 - `Statistics.txt`: Time statistics (can also be printed to console with `-stats`)
 
Note that not all of these files will be available for all configurations.
Also some of these files are only produced if an error is found (or vice-versa).
CPAchecker will overwrite files in this directory!
A graphical report which can be viewed in a browser can be generated
from these files by running `scripts/report-generator.py`
(cf. [`doc/BuildReport.md`](doc/BuildReport.md)).

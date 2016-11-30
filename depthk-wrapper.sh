#!/bin/bash

# ------------- DepthK wrapper script to tests
# Usage: ./wrapper_script_tests.sh <[-p|]> <file.c|file.i>

# ESBMC violation properties
VIOLATED_PROPERTY_TAG="Violated property:"
PROPERTY_FORGOTTEN_MEMORY_TAG="dereference failure: forgotten memory"
PROPERTY_INVALID_POINTER_TAG="dereference failure: invalid pointer"
PROPERTY_ARRAY_BOUND_VIOLATED_TAG="dereference failure: array bounds violated"
#PROPERTY_UNWIND_ASSERTION_LOOP_TAG="unwinding assertion loop" #É verificado dentro do DepthK, se for esse tipo, então o resultado é unknown.

# MEMSAFETY properties pattern
BENCHMARK_FALSE_VALID_MEMTRACK=${PROPERTY_FORGOTTEN_MEMORY_TAG}
BENCHMARK_FALSE_VALID_DEREF="(${PROPERTY_INVALID_POINTER_TAG}|${PROPERTY_ARRAY_BOUND_VIOLATED_TAG})"

# Benchmark result controlling flags
IS_MEMSAFETY_BENCHMARK=0
IS_TERMINATION_BENCHMARK=0

# Path to the DepthK tool
path_to_depthk="./depthk.py"


# Memsafety cmdline picked
do_memsafety=0
do_term=0
do_overflow=0
property_list=""

# Use parallel k-induction
parallel=0

passed_property_file=0;

while getopts "c:mhp" arg; do
    case $arg in
        h)
            echo "Usage: $0 [options] path_to_benchmark
Options:
-h             Print this message
-c propfile    Specifythe given property file
-p             Using k-induction with parallel algorithm"
            ;;
        c)
            # Given the lack of variation in the property file... we don't
            # actually interpret it. Instead we have the same options to all
            # tests (except for the memory checking ones), and define all the
            # error labels from other directories to be ERROR.
            #if ! grep -q __VERIFIER_error $OPTARG; then
            property_list=$OPTARG
	    if grep -q -E "LTL[(]G \! overflow" $OPTARG; then
		    do_overflow=1
        fi

	    if grep -q -E "LTL[(]G (valid-free|valid-deref|valid-memtrack)" $OPTARG; then
                do_memsafety=1
                IS_MEMSAFETY_BENCHMARK=1
        fi
	    
	    if grep -q -E "LTL[(]F end" $OPTARG; then
            	do_term=1
                IS_TERMINATION_BENCHMARK=1
        fi
        ;;
	p) parallel=1
        ;;
    esac
done

if [ -z "$property_list" ]; then
   benchmark=${BASH_ARGV[0]}
   directory=$(dirname $benchmark)
   possible_file="$directory/ALL.prp"
   if [ -e $possible_file ]; then
      if grep -q -E "LTL[(]G (valid-free|valid-deref|valid-memtrack)" $possible_file; then
        do_memsafety=1
 	    IS_MEMSAFETY_BENCHMARK=1
      fi
      if grep -q -E "LTL[(]F end" $possible_file; then
          do_term=1
          IS_TERMINATION_BENCHMARK=1
      fi
   fi
fi

# Pick the command line to be using
if test ${do_memsafety} = 0; then
    cmdline=${global_cmd_line}
else
    cmdline=${memory_cmd_line}
fi

# Command line, common to all tests.
depthk_options=""
if test ${parallel} = 1; then
  if test ${do_memsafety} = 0; then
    depthk_options="--debug --force-check-base-case --k-induction-parallel --solver z3 --memlimit 15g --extra-option-esbmc=\"--no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""    
  else
    depthk_options="--debug --force-check-base-case --k-induction-parallel --solver z3 --memlimit 15g --memory-leak-check --extra-option-esbmc=\"--floatbv --error-label ERROR\""    
  fi
else
    if test ${do_term} = 1; then
	    depthk_options="--debug --force-check-base-case --solver z3 --memlimit 15g --termination-category --extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
	elif test ${do_overflow} = 1; then
	    depthk_options="--debug --force-check-base-case --solver z3 --memlimit 15g --overflow-check --extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
    elif test ${do_memsafety} = 0; then
        depthk_options="--debug --force-check-base-case --solver z3 --memlimit 15g --extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
    else
        depthk_options="--debug --force-check-base-case --solver z3 --memlimit 15g --memory-safety-category --extra-option-esbmc=\"--floatbv --memory-leak-check --error-label ERROR\""
    fi
fi

# Store the path to the file we're going to be checking.
benchmark=${BASH_ARGV[0]}
# Store the path to the file to write the witness if the sourcefile
# contains a bug and the tool returns False (Error found)
# witnesspath=$2

if test "${benchmark}" = ""; then
    echo "No benchmark given" >&2
    exit 1
fi


# Creating Dir to save the logs
#LOGS_depthk="LOGS_depthk"
#if [ ! -d "$LOGS_depthk" ]; then
#    mkdir "$LOGS_depthk"
#fi


# The complete command to be executed
run_cmdline="${path_to_depthk} ${depthk_options} \"${benchmark}\";"
#echo "$run_cmdline"
#exit
# Invoke our command, wrapped in a timeout so that we can
# postprocess the results. `timeout` is part of coreutils on debian and fedora.
result_check=`timeout 895 bash -c "$run_cmdline"`

# Saving logs
echo "$result_check" &> "${benchmark}"".log"
# mv "${benchmark}"".log" "$LOGS_depthk"
# Getting K adopted by ESBMC
bound="-"
bond_check1=`tac  "${benchmark}"".log" |grep -o "^\*\*\* K-Induction Loop Iteration.*" | grep -o "[0-9]*[^ ]*" -m 1`
if [ ! -z "$bond_check1" ]; then
   bound=$bond_check1
else 
	bond_check2=`tac  "${benchmark}"".log" | grep -o "^Unwinding loop.*" | grep -o "iteration[ ]*[0-9]*[^ ]*" | grep -o "[0-9]*$" -m 1`
	if [ ! -z "$bond_check2" ]; then
		bound=$bond_check2
	else
		bond_check3=`tac  "${benchmark}"".log" | grep -o "^MAX k (100) reached." -m 1`
		if [ ! -z "$bond_check3" ]; then
			bound="50"
		fi
	fi
fi

# Getting step of the verification
step="-"
step_check=`tac  "${benchmark}"".log" | grep -o "Status: checking.*" -m 1 | sed -e  "s/Status: checking //"`

if [ ! -z "$step_check" ]; then
   step=$step_check
fi

#echo "Solution by:$step"
rm "${benchmark}"".log"


# Identify problems with invariants generation
# Not supported by PIPS
#PIPSerror=`echo ${result_check} | grep -c "A problem was identified in PIPS"`
# When it is not possible generate the invariants
#invTO=`echo ${result_check} | grep -c "TIMEOUT to generate the invariants"`

# Checking approach to force last check with base case
#forcelastcheckbc=`echo ${result_check} | grep -c "> Forcing last check in base case"`


# Postprocessing: first, collect some facts
#echo ${run_cmdline}

VPROP=""

if test ${IS_MEMSAFETY_BENCHMARK} = 1; then
   
   false_valid_mamtrack=`echo ${result_check} | grep -c "${BENCHMARK_FALSE_VALID_MEMTRACK}"`
   false_valid_deref=`echo ${result_check} | grep -c "${BENCHMARK_FALSE_VALID_DEREF}"`

   if [ $false_valid_mamtrack -gt 0 ]; then
      VPROP=$"(valid-memtrack)"
   elif [ $false_valid_deref -gt 0 ]; then
      VPROP=$"(valid-deref)"
   fi
elif test ${do_overflow} = 1; then
    VPROP=$"(no-overflow)"
fi

failed=`echo ${result_check} | grep -c "FALSE"`
success=`echo ${result_check} | grep -c "TRUE"`

# Decide which result we determined here.
if [ $failed -gt 0 ]; then
    # Error path found
    if test ${do_term} = 1; then
       echo "UNKNOWN"
    else
       echo "FALSE${VPROP}"
    fi
elif [ $success -gt 0 ]; then
    echo "TRUE"
else
    echo "UNKNOWN"
fi

#!/bin/bash

DEBUG_SCRIPT=0
if [ $DEBUG_SCRIPT -eq 1 ]; then
  ps axfl
  cat /etc/lsb-release
  uname -a
  pwd
fi


# ------------- DepthK wrapper script to tests
# Usage: ./wrapper_script_tests.sh <[-p|]> <file.c|file.i>

# ESBMC violation properties
PROPERTY_FORGOTTEN_MEMORY_TAG="dereference failure: forgotten memory"
PROPERTY_INVALID_POINTER_TAG="dereference failure: invalid pointer"
PROPERTY_ARRAY_BOUND_VIOLATED_TAG="dereference failure: array bounds violated"
#PROPERTY_UNWIND_ASSERTION_LOOP_TAG="unwinding assertion loop" #É verificado dentro do DepthK, se for esse tipo, então o resultado é unknown.

# MEMSAFETY properties pattern
BENCHMARK_FALSE_VALID_MEMTRACK=${PROPERTY_FORGOTTEN_MEMORY_TAG}
BENCHMARK_FALSE_VALID_DEREF="(${PROPERTY_INVALID_POINTER_TAG}|${PROPERTY_ARRAY_BOUND_VIOLATED_TAG})"

# Benchmark result controlling flags
IS_MEMSAFETY_BENCHMARK=0
# Path to the DepthK tool
path_to_depthk="./depthk.py"

# Memsafety cmdline picked
do_memsafety=0
do_term=0
do_overflow=0
property_list=""

# Use parallel k-induction
parallel=0



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
	    if grep -q -E "LTL[(]G \! overflow" "$OPTARG"; then
		    do_overflow=1
        fi

	    if grep -q -E "LTL[(]G (valid-free|valid-deref|valid-memtrack)" "$OPTARG"; then
                do_memsafety=1
                IS_MEMSAFETY_BENCHMARK=1
        fi
	    
	    if grep -q -E "LTL[(]F end" "$OPTARG"; then
            	do_term=1
        fi
        ;;
	p) parallel=1
        ;;
    esac
done

if [ -z "$property_list" ]; then
   benchmark=${BASH_ARGV[0]}
   directory=$(dirname "$benchmark")
   possible_file="$directory/ALL.prp"
   if [ -e "$possible_file" ]; then
      if grep -q -E "LTL[(]G (valid-free|valid-deref|valid-memtrack)" "$possible_file"; then
        do_memsafety=1
 	    IS_MEMSAFETY_BENCHMARK=1
      fi
      if grep -q -E "LTL[(]F end" "$possible_file"; then
          do_term=1
      fi
   fi
fi

# Command line, common to all tests.
depthk_options=""
if test ${parallel} = 1; then
  if test ${do_memsafety} = 0; then
    depthk_options="--force-check-base-case --k-induction-parallel --solver z3 --memlimit 15g --prp " "$property_list" "--extra-option-esbmc=\"--no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""    
  else
    depthk_options="--force-check-base-case --k-induction-parallel --solver z3 --memlimit 15g --prp " "$property_list" " --memory-leak-check --extra-option-esbmc=\"--floatbv --error-label ERROR\""    
  fi
else
    if test ${do_term} = 1; then
	    depthk_options="--force-check-base-case --solver z3 --memlimit 15g --termination-category --prp " "$property_list" " --extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
	elif test ${do_overflow} = 1; then
	    depthk_options="--force-check-base-case --solver z3 --memlimit 15g --overflow-check --prp " "$property_list"  "--extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
    elif test ${do_memsafety} = 0; then
        depthk_options="--force-check-base-case --solver z3 --memlimit 15g --prp "$property_list"  --extra-option-esbmc=\"--floatbv --no-bounds-check --no-pointer-check --no-div-by-zero-check --error-label ERROR\""
    else
        depthk_options="--force-check-base-case --solver z3 --memlimit 15g --prp " "$property_list" " --memory-safety-category --extra-option-esbmc=\"--floatbv --memory-leak-check --error-label ERROR\""
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

# The complete command to be executed
run_cmdline="${path_to_depthk} ${depthk_options} \"${benchmark}\";"
#echo "$run_cmdline"
#exit
# Invoke our command, wrapped in a timeout so that we can
# postprocess the results. `timeout` is part of coreutils on debian and fedora.
result_check=$(timeout 895 bash -c "$run_cmdline")

VPROP=""

if test ${IS_MEMSAFETY_BENCHMARK} = 1; then
   
   false_valid_mamtrack=$(echo "${result_check}" |grep -c "${BENCHMARK_FALSE_VALID_MEMTRACK}")
   false_valid_deref=$(echo "${result_check}" |grep -c "${BENCHMARK_FALSE_VALID_DEREF}")

   if [ "$false_valid_mamtrack" -gt 0 ]; then
      VPROP=$"(valid-memtrack)"
   elif [ "$false_valid_deref" -gt 0 ]; then
      VPROP=$"(valid-deref)"
   fi
elif test ${do_overflow} = 1; then
    VPROP=$"(no-overflow)"
fi

failed=$(echo "${result_check}" |grep -c "FALSE")
success=$(echo "${result_check}" |grep -c "TRUE")

# Decide which result we determined here.
if [ "$failed" -gt 0 ]; then
    # Error path found
    if test ${do_term} = 1; then
       echo "UNKNOWN"
    else
       echo "FALSE${VPROP}"
    fi
elif [ "$success" -gt 0 ]; then
    echo "TRUE"
else
    echo "UNKNOWN"
fi

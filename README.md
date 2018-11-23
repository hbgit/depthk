<h1>DepthK 3.1</h1>
<h3>K-Induction adopting program invariants</h3>

================ 

          .-.          
          /v\
         // \\    > L I N U X - GPL <
        /(   )\
         ^^-^^

================

- Requirements for using the tool<br>
To use this tool is necessary that the system contains the following software already installed properly:

> - Linux OS
> - Python (v2.7.1 or higher);
> - Uncrustify (v0.60 or higher) - http://uncrustify.sourceforge.net/
> - Pycparser (v2.10) - https://github.com/eliben/pycparser
> - Ctags - http://ctags.sourceforge.net 
> - Clang (v3.5) - http://clang.llvm.org
> - PIPS - http://pips4u.org
> - sed
> - grep
> - timeout
> - GCC compiler
> - Java (v1.7 or higher);

==============

<b>How to install DepthK?</b>

<p align="justify">
First of all, you need to install the required packages:
</p>

- <b>STEP 0:</b>

> - <b>Uncrustify</b>: <br> Ubuntu $ sudo apt-get install uncrustify <br> Fedora $ sudo yum install uncrustify
> - <b>Pycparser</b>: <br> Ubuntu $ sudo apt-get install python-pycparser <br> Fedora $ sudo yum install python-pycparser
> - <b>Ctags</b>: <br> Ubuntu $ sudo apt-get install exuberant-ctags <br> Fedora $ sudo yum install ctags
> - <b>Clang</b>: <br> Ubuntu $ sudo apt-get install clang-3.5 <br> Fedora $ sudo yum install clang-3.5
> - <b>GCC</b>: <br> Ubuntu $ sudo apt-get install gcc <br> Fedora $ sudo yum groupinstall "Development Tools"
> - <b>Java</b>: <br> Ubuntu $ sudo apt-get install openjdk-8-jre <br>
> - <b>PIPS (optional)</b>: <br>Available at http://pips4u.org/copy_of_getting-pips/building-and-installing-pips-from%20svn <br>
You should set the environment variable PATH in your .bashrc file. <br> 
Checkout Step 4: Load the PIPS environment variables from that link<br>


- <b>STEP 1:</b>

<p align="justify">
In order to install DepthK on your PC, you should download and save the depthk.zip file on your disk. 
After that, you should type the following command:
</p>

> $ unzip depthk.zip

or from https://github.com

> $ git clone https://github.com/hbgit/depthk.git

- <b>STEP 2:</b>

<p align="justify">
Testing tool. DepthK can be invoked through a standard command-line interface. DepthK should be called 
in the installation directory. For help and others options: 

> $ ./depthk.py --help

</p>

Use the 'depthk-wrapper.sh' script in the installation directory to verify each single test-case:

> $ ./depthk-wrapper.sh -c  /home/user/depthk/samples/ALL.prp /home/user/depthk/samples/example1_true-unreach-call.c <br>
> TRUE <br>


===========================

<b> Instructions for SV-COMP'19 </b>

Use the 'depthk-wrapper.sh' script in the installation directory to verify each single test-case. 

Usage: 

> $ ./depthk-wrapper.sh -c  full/path/propertyFile.prp full/path/file.i

<p align="justify">
DepthK provides as verification result:
<b>TRUE + Witness</b> The specification is satisfied (i.e., there is no path that violates the specification) and 
a correctness witness is produced; 
<b>FALSE + Witness</b> The specification is violated (i.e., there exists a path that violates the specification) 
and an violation witness is produced; 
or <b>UNKNOWN</b> The tool cannot decide the problem or terminates by a tool crash, time-out, or out-of-memory 
(i.e., the competition candidate does not succeed in computing an answer TRUE or FALSE).
For each witness produced it is genetared a file in DepthK root-path graphml folder; this file has the same name
of the verification task with the extension </b>graphml</b>.
There is timeout of 895 seconds set by this script, using "timeout" tool that is part of coreutils 
on debian and fedora. 
</p>


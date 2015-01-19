<h1>DepthK</h1>
<h3>K-Induction adopting program invariants</h3>

Author: Herbert O. Rocha <br>
E-mail: herberthb12@gmail.com

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
> - sed;
> - grep;
> - GCC compiler; 
> - Pycparser (v2.10) - https://github.com/eliben/pycparser
> - Ctags - http://ctags.sourceforge.net 
> - PIPS - http://pips4u.org/
> - ESBMC (v1.24.1 or higher) - http://esbmc.org/

================

<b>How to install DepthK?</b>

<p align="justify">
First of all, you need to install the required packages:
</p>

- <b>STEP 0:</b>

> - <b>Pycparser</b>: <br> Ubuntu $ sudo apt-get install python-pycparser <br> Fedora $ sudo yum install python-pycparser
> - <b>Ctags</b>: <br> Ubuntu $ sudo apt-get install exuberant-ctags <br>Fedora $ sudo apt-get install ctags
> - <b>PIPS</b>: <br>Available at http://pips4u.org/copy_of_getting-pips/building-and-installing-pips-from%20svn <br>
You should set the environment variable PATH in your .bashrc file. <br> 
Checkout Step 4: Load the PIPS environment variables from that link<br>
> - <b>ESBMC</b>: <br>
In order to install ESBMC on your machine, you should download the archive (e.g., esbmc-v1.24.1.tar.tgz) and <br> 
save it on your disk. After that, you should type the following command: <br>
$ tar xfz esbmc-v1.24.1.tar.tgz <br>
You should set the environment variable PATH in your .bashrc file as follows: <br>
$ export PATH=$PATH:$HOME/esbmc-v1.24.1/ <br>
After that, you can run ESBMC from the command line by calling: <br>
$ esbmc file.c


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
in the installation directory as follows:  
</p>

> $ ./depthk.py test_cases/conceptproof/invgen/confuse/confuse.c -g <br> 
> \>\> Running PIPS to generate the invariants <br>
> \>\> Translating the PIPS annotation with the invariants <br>
> \>\> Starting the verification of the P' program <br>
> 	 \-\> Actual k = 1 <br>
> 		 Status: checking base case <br>
> 		 Status: checking forward condition <br>
> 		 Status: checking inductive step <br>
> TRUE <br>


By default ESBMC is called, as follows: 
> $ esbmc --64 --no-library --z3 --unwind \<nr\> --timeout 15m [--base-case|--forward-condition|--inductive-step] <file.c>

For help and others options: 

> $ ./depthk.py --help

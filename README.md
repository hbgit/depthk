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
> - Ctags - http://ctags.sourceforge.net - sudo apt-get install exuberant-ctags
> - PIPS - http://pips4u.org/

================

<b>How to install DepthK?</b>

<p align="justify">
First of all, you need to install the required packages:
</p>

- <b>STEP 0:</b>

> - Pycparser : Ubuntu $ sudo apt-get install python-pycparser or Fedora $ sudo yum install python-pycparser
> - Ctags: Ubuntu $ sudo apt-get install exuberant-ctags or Fedora $ sudo apt-get install ctags
> - PIPS: Available at http://pips4u.org/copy_of_getting-pips/building-and-installing-pips-from%20svn


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
Testing tool. DepthK can be invoked through a standard command-line interface. Map2Check should be called 
in the installation directory as follows:  
</p>

> $ ./depthk.py test_cases/conceptproof/invgen/confuse/confuse.c -g 

For help and others options: 

> $ ./depthk.py --help

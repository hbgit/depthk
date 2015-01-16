/*
 * file for main.c
 */

//  P() {0==-1}


/* Copyright (C) 1991-2014 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */




/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */

/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses ISO/IEC 10646 (2nd ed., published 2011-03-15) /
   Unicode 6.0.  */


/* We do not support C11 <threads.h>.  */
//#include "../glob.h"


//  P() {0==-1}


typedef int Char;



//  P() {0==-1}


Char *tmp;


//  P() {0==-1}


int glob2(Char *pathbuf, Char *pathlim);


//  P() {0==-1}


int main();

//  P() {}


int glob2(Char *pathbuf, Char *pathlim)
{


//  P() {}


   Char *p;

//  P() {}



   p = pathbuf;

//  P() {}


   while (p<=pathlim) {

//  P() {}


      /* BAD */
      assert(p<=tmp);

//  P() {}


      *p = 1;

//  P() {}


      p++;
   }

//  P() {}



   return 0;
}

//  P() {}


int main()
{


//  P() {}


   Char pathbuf[2];

//  P() {}


   Char *bound = pathbuf+sizeof(pathbuf)-1;

//  P() {}


   tmp = pathbuf+sizeof(pathbuf)/sizeof(*pathbuf)-1;

//  P() {}



   glob2(pathbuf, bound);

//  P() {}



   return 0;
}

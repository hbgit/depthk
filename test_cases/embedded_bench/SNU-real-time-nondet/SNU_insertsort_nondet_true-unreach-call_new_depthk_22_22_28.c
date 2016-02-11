/*
 * file for SNU_insertsort_nondet_true-unreach-call_new_depthk_22_22_28.c
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

//  P() {0==-1}


int nondet_int();


//  P() {0==-1}


int main();

//  P() {}


int main()
{


//  P() {}


   int i, j, temp, a[11];

//  P(i,j,temp) {}


   
   
   
   for(i = 0; i <= 10; i += 1) {

//  P(i,j,temp) {0<=i, i<=10}

__ESBMC_assume( 0<=i && i<=10 );

      a[i] = nondet_int();
   }

//  P(i,j,temp) {i==11}

__ESBMC_assume( i==11 );


   i = 2;

//  P(i,j,temp) {i==2}

__ESBMC_assume( i==2 );

   while (i<=10) {

//  P(i,j,temp) {2<=i, i<=10}

__ESBMC_assume( 2<=i && i<=10 );

      j = i;

//  P(i,j,temp) {i==j, 2<=i, i<=10}

__ESBMC_assume( i==j && 2<=i && i<=10 );

      
      
      
      while (a[j]<a[j-1]) {

//  P(i,j,temp) {2<=i, i<=10, j<=i}

__ESBMC_assume( 2<=i && i<=10 && j<=i );

         temp = a[j];

//  P(i,j,temp) {2<=i, i<=10, j<=i}

__ESBMC_assume( 2<=i && i<=10 && j<=i );

         a[j] = a[j-1];

//  P(i,j,temp) {2<=i, i<=10, j<=i}

__ESBMC_assume( 2<=i && i<=10 && j<=i );

         a[j-1] = temp;

//  P(i,j,temp) {2<=i, i<=10, j<=i}

__ESBMC_assume( 2<=i && i<=10 && j<=i );

         j--;
      }

//  P(i,j,temp) {2<=i, i<=10, j<=i}

__ESBMC_assume( 2<=i && i<=10 && j<=i );

      
      
      
      i++;
   }
}

/*
 * file for main_new_depthk_16_31_26.c
 */

//  P() {0==-1}


/* Copyright (C) 1991-2013 Free Software Foundation, Inc.
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

/* We do support the IEC 559 math functionality, real and complex.  */



/* wchar_t uses ISO/IEC 10646 (2nd ed., published 2011-03-15) /
   Unicode 6.0.  */


/* We do not support C11 <threads.h>.  */

//  P() {0==-1}


int b;

//  P() {0==-1}


_Bool nondet_bool();

//_Bool k=nondet_bool();

//  P() {0==-1}


int main();

//  P() {}


int main()
{


//  P() {}


   _Bool k = nondet_bool();

//  P() {}


   int i, n, j;

//  P(i,j,n) {}


   int a[1025];

//  P(i,j,n) {}



   if (k) {

//  P(i,j,n) {}


      n = 0;
   }
   else {

//  P(i,j,n) {}


      n = 1023;
   }

//  P(i,j,n) {0<=n, n<=1023}

__ESBMC_assume( 0<=n && n<=1023 );


   i = 0;

//  P(i,j,n) {i==0, 0<=n, n<=1023}

__ESBMC_assume( i==0 && 0<=n && n<=1023 );


   while (i<=n) {

//  P(i,j,n) {0<=i, i<=n, n<=1023}

__ESBMC_assume( 0<=i && i<=n && n<=1023 );

      i++;

//  P(i,j,n) {1<=i, i<=n+1, n<=1023}

__ESBMC_assume( 1<=i && i<=n+1 && n<=1023 );

      j = j+2;
   }

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );


   a[i] = 0;

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

   a[j] = 0;

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

   assert(j<1025);

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

   a[b] = 0;

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

   if (b>=0&&b<1023) {

//  P(i,j,n) {i==n+1, 0<=b, b<=1022, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 0<=b && b<=1022 && 1<=i && i<=1024 );

      a[b] = 1;
   }
   else {

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

      a[b%1023] = 1;
   }

//  P(i,j,n) {i==n+1, 1<=i, i<=1024}

__ESBMC_assume( i==n+1 && 1<=i && i<=1024 );

   return 1;
}

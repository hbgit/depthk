# 1 "/home/hrocha/Documents/Projects_DEV/depthk/test_cases/ori_bitcell.c"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
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
# 1 "<command-line>" 2
# 1 "/home/hrocha/Documents/Projects_DEV/depthk/test_cases/ori_bitcell.c"
/*int nondet(void)
{
  int x;
  {
    return (x);
  }
  }*/

extern void error(void);

int b0_val ;
int b0_val_t ;
int b0_ev ;
int b0_req_up ;
int b1_val ;
int b1_val_t ;
int b1_ev ;
int b1_req_up ;
int d0_val ;
int d0_val_t ;
int d0_ev ;
int d0_req_up ;
int d1_val ;
int d1_val_t ;
int d1_ev ;
int d1_req_up ;
int z_val ;
int z_val_t ;
int z_ev ;
int z_req_up ;
int comp_m1_st ;
int comp_m1_i ;
extern void method1(void);
extern int is_method1_triggered(void);
extern void update_b0(void);
extern void update_b1(void);
extern void update_d0(void);
extern void update_d1(void);
extern void update_z(void);
extern void update_channels(void);
extern void init_threads(void);
extern int exists_runnable_thread(void);
extern void eval(void);
extern void fire_delta_events(void);
extern void reset_delta_events(void);
extern void activate_threads(void);
extern int stop_simulation(void);
extern void start_simulation(void);
extern void init_model(void);
extern int main(void);


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
/*int nondet(void)
{
  int x;
  {
    return (x);
  }
  }*/


//  P() {0==-1}

void error(void);


//  P() {0==-1}

int b0_val;

//  P() {0==-1}

int b0_val_t;

//  P() {0==-1}

int b0_ev;

//  P() {0==-1}

int b0_req_up;

//  P() {0==-1}

int b1_val;

//  P() {0==-1}

int b1_val_t;

//  P() {0==-1}

int b1_ev;

//  P() {0==-1}

int b1_req_up;

//  P() {0==-1}

int d0_val;

//  P() {0==-1}

int d0_val_t;

//  P() {0==-1}

int d0_ev;

//  P() {0==-1}

int d0_req_up;

//  P() {0==-1}

int d1_val;

//  P() {0==-1}

int d1_val_t;

//  P() {0==-1}

int d1_ev;

//  P() {0==-1}

int d1_req_up;

//  P() {0==-1}

int z_val;

//  P() {0==-1}

int z_val_t;

//  P() {0==-1}

int z_ev;

//  P() {0==-1}

int z_req_up;

//  P() {0==-1}

int comp_m1_st;

//  P() {0==-1}

int comp_m1_i;

//  P() {0==-1}

void method1(void);

//  P() {0==-1}

int is_method1_triggered(void);

//  P() {0==-1}

void update_b0(void);

//  P() {0==-1}

void update_b1(void);

//  P() {0==-1}

void update_d0(void);

//  P() {0==-1}

void update_d1(void);

//  P() {0==-1}

void update_z(void);

//  P() {0==-1}

void update_channels(void);

//  P() {0==-1}

void init_threads(void);

//  P() {0==-1}

int exists_runnable_thread(void);

//  P() {0==-1}

void eval(void);

//  P() {0==-1}

void fire_delta_events(void);

//  P() {0==-1}

void reset_delta_events(void);

//  P() {0==-1}

void activate_threads(void);

//  P() {0==-1}

int stop_simulation(void);

//  P() {0==-1}

void start_simulation(void);

//  P() {0==-1}

void init_model(void);

//  P() {0==-1}

int main(void);

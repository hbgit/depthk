
//  P() {}

void eval(void)
{

//  P() {}

   int tmp;

//  P(tmp) {}

   int tmp___0;
l99999:   ;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {}

   if (!1) goto while_0_break;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {}

   /* CIL Label */
while_0_continue:   ;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {}

   tmp___0 = exists_runnable_thread();

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {0<=tmp___0,
//    tmp___0<=1}

   if (tmp___0) {
   }
   else {
      goto while_0_break;
   }

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {tmp___0==1}

   if (comp_m1_st==0) {

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {comp_m1_st==0,
//    tmp___0==1}

      tmp = __VERIFIER_nondet_int();

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {comp_m1_st==0,
//    tmp___0==1}

      if (tmp) {

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {comp_m1_st==0,
//    tmp___0==1}

         comp_m1_st = 1;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {comp_m1_st==1,
//    tmp___0==1}

         method1();
      }
   }
   goto l99999;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {tmp___0==0}

   /* CIL Label */
while_0_break:   ;

//  P(comp_m1_st,tmp,tmp___0,z_req_up,z_val_t) {tmp___0==0}


   return;
}

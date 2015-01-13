
//  P() {b0_ev==2, b0_req_up==1, b0_val==0, b1_ev==2, b1_req_up==1,
//    b1_val==0, d0_ev==2, d0_req_up==1, d0_val==0, d1_ev==2,
//    d1_req_up==1, d1_val==0, z_ev==2, z_req_up==0, z_val==0}

void start_simulation(void)
{

//  P() {b0_ev==2, b0_req_up==1, b0_val==0, b1_ev==2, b1_req_up==1,
//    b1_val==0, d0_ev==2, d0_req_up==1, d0_val==0, d1_ev==2,
//    d1_req_up==1, d1_val==0, z_ev==2, z_req_up==0, z_val==0}

   int kernel_st;

//  P(kernel_st) {b0_ev==2, b0_req_up==1, b0_val==0, b1_ev==2,
//    b1_req_up==1, b1_val==0, d0_ev==2, d0_req_up==1, d0_val==0,
//    d1_ev==2, d1_req_up==1, d1_val==0, z_ev==2, z_req_up==0,
//    z_val==0}

   int tmp;

//  P(kernel_st,tmp) {b0_ev==2, b0_req_up==1, b0_val==0, b1_ev==2,
//    b1_req_up==1, b1_val==0, d0_ev==2, d0_req_up==1, d0_val==0,
//    d1_ev==2, d1_req_up==1, d1_val==0, z_ev==2, z_req_up==0,
//    z_val==0}

   kernel_st = 0;

//  P(kernel_st,tmp) {b0_ev==2, b0_req_up==1, b0_val==0, b1_ev==2,
//    b1_req_up==1, b1_val==0, d0_ev==2, d0_req_up==1, d0_val==0,
//    d1_ev==2, d1_req_up==1, d1_val==0, kernel_st==0, z_ev==2,
//    z_req_up==0, z_val==0}

   update_channels();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,z_req_up,z_val)
//    {b0_ev#init==2, b0_req_up#init==1, b0_val#init==0,
//    b1_ev#init==2, b1_req_up#init==1, b1_val#init==0, d0_ev#init==2,
//    d0_req_up#init==1, d0_val#init==0, d1_ev#init==2,
//    d1_req_up#init==1, d1_val#init==0, kernel_st==0, z_ev#init==2,
//    z_req_up#init==0, z_val#init==0, 0<=b0_req_up, b0_req_up<=1,
//    0<=b1_req_up, b1_req_up<=1, 0<=d0_req_up, d0_req_up<=1,
//    0<=d1_req_up, d1_req_up<=1, z_req_up<=0, 0<=z_req_up+1}

   init_threads();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, comp_m1_st==2, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==0, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, 0<=b0_req_up, b0_req_up<=1, 0<=b1_req_up,
//    b1_req_up<=1, 0<=d0_req_up, d0_req_up<=1, 0<=d1_req_up,
//    d1_req_up<=1, z_req_up<=0, 0<=z_req_up+1}

   fire_delta_events();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, comp_m1_st==2, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==0, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, 0<=b0_req_up, b0_req_up<=1, 0<=b1_req_up,
//    b1_req_up<=1, 0<=d0_req_up, d0_req_up<=1, 0<=d1_req_up,
//    d1_req_up<=1, z_req_up<=0, 0<=z_req_up+1}

   activate_threads();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==0, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, 0<=b0_req_up, b0_req_up<=1, 0<=b1_req_up,
//    b1_req_up<=1, 0<=d0_req_up, d0_req_up<=1, 0<=d1_req_up,
//    d1_req_up<=1, z_req_up<=0, 0<=z_req_up+1}

   reset_delta_events();
l99999:   ;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, z_ev#init==2, z_req_up#init==0, z_val#init==0,
//    b0_req_up<=1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1,
//    0<=kernel_st, kernel_st<=3}

   if (!1) goto while_1_break;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, z_ev#init==2, z_req_up#init==0, z_val#init==0,
//    b0_req_up<=1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1,
//    0<=kernel_st, kernel_st<=3}

   /* CIL Label */
while_1_continue:   ;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, z_ev#init==2, z_req_up#init==0, z_val#init==0,
//    b0_req_up<=1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1,
//    0<=kernel_st, kernel_st<=3}

   kernel_st = 1;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==1, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   eval();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==1, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   kernel_st = 2;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==2, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   update_channels();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==2, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   kernel_st = 3;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   fire_delta_events();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   activate_threads();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   reset_delta_events();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   tmp = stop_simulation();

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, z_ev#init==2, z_req_up#init==0,
//    z_val#init==0, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1, 0<=tmp, tmp<=1}

   if (tmp) {
   }
   else {
      goto l99999;
   }

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, tmp==1, z_ev#init==2,
//    z_req_up#init==0, z_val#init==0, b0_req_up<=1, b1_req_up<=1,
//    d0_req_up<=1, d1_req_up<=1}

   /* CIL Label */
while_1_break:   ;

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,comp_m1_st,d0_ev,
//    d0_req_up,d0_val,d1_ev,d1_req_up,d1_val,kernel_st,tmp,z_ev,
//    z_req_up,z_val,z_val_t) {b0_ev#init==2, b0_req_up#init==1,
//    b0_val#init==0, b1_ev#init==2, b1_req_up#init==1,
//    b1_val#init==0, d0_ev#init==2, d0_req_up#init==1,
//    d0_val#init==0, d1_ev#init==2, d1_req_up#init==1,
//    d1_val#init==0, kernel_st==3, tmp==1, z_ev#init==2,
//    z_req_up#init==0, z_val#init==0, b0_req_up<=1, b1_req_up<=1,
//    d0_req_up<=1, d1_req_up<=1}


   return;
}

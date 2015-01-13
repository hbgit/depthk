
//  P() {}

int main(void)
{

//  P() {}

   int __retres1;

//  P(__retres1) {}

   init_model();

//  P(__retres1,b0_ev,b0_req_up,b0_val,b0_val_t,b1_ev,b1_req_up,
//    b1_val,b1_val_t,comp_m1_i,d0_ev,d0_req_up,d0_val,d0_val_t,d1_ev,
//    d1_req_up,d1_val,d1_val_t,z_ev,z_req_up,z_val) {b0_ev==2,
//    b0_req_up==1, b0_val==0, b0_val_t==1, b1_ev==2, b1_req_up==1,
//    b1_val==0, b1_val_t==1, comp_m1_i==0, d0_ev==2, d0_req_up==1,
//    d0_val==0, d0_val_t==1, d1_ev==2, d1_req_up==1, d1_val==0,
//    d1_val_t==1, z_ev==2, z_req_up==0, z_val==0}

   start_simulation();

//  P(__retres1,b0_ev,b0_req_up,b0_val,b0_val_t,b1_ev,b1_req_up,
//    b1_val,b1_val_t,comp_m1_i,comp_m1_st,d0_ev,d0_req_up,d0_val,
//    d0_val_t,d1_ev,d1_req_up,d1_val,d1_val_t,z_ev,z_req_up,z_val,
//    z_val_t) {b0_val_t==1, b1_val_t==1, comp_m1_i==0, d0_val_t==1,
//    d1_val_t==1, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   if (!(z_val==0)) {

//  P(__retres1,b0_ev,b0_req_up,b0_val,b0_val_t,b1_ev,b1_req_up,
//    b1_val,b1_val_t,comp_m1_i,comp_m1_st,d0_ev,d0_req_up,d0_val,
//    d0_val_t,d1_ev,d1_req_up,d1_val,d1_val_t,z_ev,z_req_up,z_val,
//    z_val_t) {b0_val_t==1, b1_val_t==1, comp_m1_i==0, d0_val_t==1,
//    d1_val_t==1, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

      error();
   }

//  P(__retres1,b0_ev,b0_req_up,b0_val,b0_val_t,b1_ev,b1_req_up,
//    b1_val,b1_val_t,comp_m1_i,comp_m1_st,d0_ev,d0_req_up,d0_val,
//    d0_val_t,d1_ev,d1_req_up,d1_val,d1_val_t,z_ev,z_req_up,z_val,
//    z_val_t) {b0_val_t==1, b1_val_t==1, comp_m1_i==0, d0_val_t==1,
//    d1_val_t==1, b0_req_up<=1, b1_req_up<=1, d0_req_up<=1,
//    d1_req_up<=1}

   __retres1 = 0;

//  P(__retres1,b0_ev,b0_req_up,b0_val,b0_val_t,b1_ev,b1_req_up,
//    b1_val,b1_val_t,comp_m1_i,comp_m1_st,d0_ev,d0_req_up,d0_val,
//    d0_val_t,d1_ev,d1_req_up,d1_val,d1_val_t,z_ev,z_req_up,z_val,
//    z_val_t) {__retres1==0, b0_val_t==1, b1_val_t==1, comp_m1_i==0,
//    d0_val_t==1, d1_val_t==1, b0_req_up<=1, b1_req_up<=1,
//    d0_req_up<=1, d1_req_up<=1}

   return 0;
}

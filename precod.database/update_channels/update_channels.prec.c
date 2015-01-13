
//  P() {b0_req_up<=1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1}

void update_channels(void)
{

//  P() {b0_req_up<=1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1}

   if (b0_req_up==1) {

//  P() {b0_req_up==1, b1_req_up<=1, d0_req_up<=1, d1_req_up<=1}

      update_b0();
   }

//  P(b0_ev,b0_req_up,b0_val) {b0_req_up<=0,
//    b0_req_up<=b0_req_up#init, b0_req_up#init<=b0_req_up+1,
//    b1_req_up<=1, d0_req_up<=1, d1_req_up<=1}

   if (b1_req_up==1) {

//  P(b0_ev,b0_req_up,b0_val) {b1_req_up==1, b0_req_up<=0,
//    b0_req_up<=b0_req_up#init, b0_req_up#init<=b0_req_up+1,
//    d0_req_up<=1, d1_req_up<=1}

      update_b1();
   }

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val) {b0_req_up<=0,
//    b0_req_up<=b0_req_up#init, b0_req_up#init<=b0_req_up+1,
//    b1_req_up<=0, b1_req_up<=b1_req_up#init,
//    b1_req_up#init<=b1_req_up+1, d0_req_up<=1, d1_req_up<=1}

   if (d0_req_up==1) {

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val) {d0_req_up==1,
//    b0_req_up<=0, b0_req_up<=b0_req_up#init,
//    b0_req_up#init<=b0_req_up+1, b1_req_up<=0,
//    b1_req_up<=b1_req_up#init, b1_req_up#init<=b1_req_up+1,
//    d1_req_up<=1}

      update_d0();
   }

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val) {b0_req_up<=0, b0_req_up<=b0_req_up#init,
//    b0_req_up#init<=b0_req_up+1, b1_req_up<=0,
//    b1_req_up<=b1_req_up#init, b1_req_up#init<=b1_req_up+1,
//    d0_req_up<=0, d0_req_up<=d0_req_up#init,
//    d0_req_up#init<=d0_req_up+1, d1_req_up<=1}

   if (d1_req_up==1) {

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val) {d1_req_up==1, b0_req_up<=0, b0_req_up<=b0_req_up#init,
//    b0_req_up#init<=b0_req_up+1, b1_req_up<=0,
//    b1_req_up<=b1_req_up#init, b1_req_up#init<=b1_req_up+1,
//    d0_req_up<=0, d0_req_up<=d0_req_up#init,
//    d0_req_up#init<=d0_req_up+1}

      update_d1();
   }

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val,d1_ev,d1_req_up,d1_val) {b0_req_up<=0,
//    b0_req_up<=b0_req_up#init, b0_req_up#init<=b0_req_up+1,
//    b1_req_up<=0, b1_req_up<=b1_req_up#init,
//    b1_req_up#init<=b1_req_up+1, d0_req_up<=0,
//    d0_req_up<=d0_req_up#init, d0_req_up#init<=d0_req_up+1,
//    d1_req_up<=0, d1_req_up<=d1_req_up#init,
//    d1_req_up#init<=d1_req_up+1}

   if (z_req_up==1) {

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val,d1_ev,d1_req_up,d1_val) {z_req_up==1, b0_req_up<=0,
//    b0_req_up<=b0_req_up#init, b0_req_up#init<=b0_req_up+1,
//    b1_req_up<=0, b1_req_up<=b1_req_up#init,
//    b1_req_up#init<=b1_req_up+1, d0_req_up<=0,
//    d0_req_up<=d0_req_up#init, d0_req_up#init<=d0_req_up+1,
//    d1_req_up<=0, d1_req_up<=d1_req_up#init,
//    d1_req_up#init<=d1_req_up+1}

      update_z();
   }

//  P(b0_ev,b0_req_up,b0_val,b1_ev,b1_req_up,b1_val,d0_ev,d0_req_up,
//    d0_val,d1_ev,d1_req_up,d1_val,z_ev,z_req_up,z_val)
//    {b0_req_up<=0, b0_req_up<=b0_req_up#init,
//    b0_req_up#init<=b0_req_up+1, b1_req_up<=0,
//    b1_req_up<=b1_req_up#init, b1_req_up#init<=b1_req_up+1,
//    d0_req_up<=0, d0_req_up<=d0_req_up#init,
//    d0_req_up#init<=d0_req_up+1, d1_req_up<=0,
//    d1_req_up<=d1_req_up#init, d1_req_up#init<=d1_req_up+1,
//    z_req_up<=z_req_up#init, z_req_up#init<=z_req_up+1}


   return;
}

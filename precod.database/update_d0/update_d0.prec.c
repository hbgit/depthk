
//  P() {d0_req_up==1}

void update_d0(void)
{

//  P() {d0_req_up==1}

   if (d0_val!=1) {

//  P() {d0_req_up==1}

      d0_val = 1;

//  P(d0_val) {d0_req_up==1, d0_val==1}

      d0_ev = 0;
   }

//  P(d0_ev,d0_val) {d0_req_up==1, d0_val==1}

   d0_req_up = 0;

//  P(d0_ev,d0_req_up,d0_val) {d0_req_up==0, d0_req_up#init==1,
//    d0_val==1}


   return;
}

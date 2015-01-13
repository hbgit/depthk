
//  P() {b0_req_up==1}

void update_b0(void)
{

//  P() {b0_req_up==1}

   if (b0_val!=1) {

//  P() {b0_req_up==1}

      b0_val = 1;

//  P(b0_val) {b0_req_up==1, b0_val==1}

      b0_ev = 0;
   }

//  P(b0_ev,b0_val) {b0_req_up==1, b0_val==1}

   b0_req_up = 0;

//  P(b0_ev,b0_req_up,b0_val) {b0_req_up==0, b0_req_up#init==1,
//    b0_val==1}


   return;
}

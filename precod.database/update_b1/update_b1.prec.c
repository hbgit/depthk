
//  P() {b1_req_up==1}

void update_b1(void)
{

//  P() {b1_req_up==1}

   if (b1_val!=1) {

//  P() {b1_req_up==1}

      b1_val = 1;

//  P(b1_val) {b1_req_up==1, b1_val==1}

      b1_ev = 0;
   }

//  P(b1_ev,b1_val) {b1_req_up==1, b1_val==1}

   b1_req_up = 0;

//  P(b1_ev,b1_req_up,b1_val) {b1_req_up==0, b1_req_up#init==1,
//    b1_val==1}


   return;
}

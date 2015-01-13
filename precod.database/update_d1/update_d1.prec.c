
//  P() {d1_req_up==1}

void update_d1(void)
{

//  P() {d1_req_up==1}

   if (d1_val!=1) {

//  P() {d1_req_up==1}

      d1_val = 1;

//  P(d1_val) {d1_req_up==1, d1_val==1}

      d1_ev = 0;
   }

//  P(d1_ev,d1_val) {d1_req_up==1, d1_val==1}

   d1_req_up = 0;

//  P(d1_ev,d1_req_up,d1_val) {d1_req_up==0, d1_req_up#init==1,
//    d1_val==1}


   return;
}

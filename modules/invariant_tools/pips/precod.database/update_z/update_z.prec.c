
//  P() {z_req_up==1}

void update_z(void)
{

//  P() {z_req_up==1}

   if (z_val!=z_val_t) {

//  P() {z_req_up==1}

      z_val = z_val_t;

//  P(z_val) {z_req_up==1, z_val==z_val_t}

      z_ev = 0;
   }

//  P(z_ev,z_val) {z_req_up==1, z_val==z_val_t}

   z_req_up = 0;

//  P(z_ev,z_req_up,z_val) {z_req_up==0, z_req_up#init==1,
//    z_val==z_val_t}


   return;
}

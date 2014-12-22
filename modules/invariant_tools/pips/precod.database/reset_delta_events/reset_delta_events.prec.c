
//  P() {}

void reset_delta_events(void)
{

//  P() {}

   if (b0_ev==1)

//  P() {b0_ev==1}

      b0_ev = 2;

//  P(b0_ev) {b0_ev#init<=b0_ev, b0_ev<=b0_ev#init+1}

   if (b1_ev==1)

//  P(b0_ev) {b1_ev==1, b0_ev#init<=b0_ev, b0_ev<=b0_ev#init+1}

      b1_ev = 2;

//  P(b0_ev,b1_ev) {b0_ev#init<=b0_ev, b0_ev<=b0_ev#init+1,
//    b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1}

   if (d0_ev==1)

//  P(b0_ev,b1_ev) {d0_ev==1, b0_ev#init<=b0_ev, b0_ev<=b0_ev#init+1,
//    b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1}

      d0_ev = 2;

//  P(b0_ev,b1_ev,d0_ev) {b0_ev#init<=b0_ev, b0_ev<=b0_ev#init+1,
//    b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1, d0_ev#init<=d0_ev,
//    d0_ev<=d0_ev#init+1}

   if (d1_ev==1)

//  P(b0_ev,b1_ev,d0_ev) {d1_ev==1, b0_ev#init<=b0_ev,
//    b0_ev<=b0_ev#init+1, b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1,
//    d0_ev#init<=d0_ev, d0_ev<=d0_ev#init+1}

      d1_ev = 2;

//  P(b0_ev,b1_ev,d0_ev,d1_ev) {b0_ev#init<=b0_ev,
//    b0_ev<=b0_ev#init+1, b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1,
//    d0_ev#init<=d0_ev, d0_ev<=d0_ev#init+1, d1_ev#init<=d1_ev,
//    d1_ev<=d1_ev#init+1}

   if (z_ev==1)

//  P(b0_ev,b1_ev,d0_ev,d1_ev) {z_ev==1, b0_ev#init<=b0_ev,
//    b0_ev<=b0_ev#init+1, b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1,
//    d0_ev#init<=d0_ev, d0_ev<=d0_ev#init+1, d1_ev#init<=d1_ev,
//    d1_ev<=d1_ev#init+1}

      z_ev = 2;

//  P(b0_ev,b1_ev,d0_ev,d1_ev,z_ev) {b0_ev#init<=b0_ev,
//    b0_ev<=b0_ev#init+1, b1_ev#init<=b1_ev, b1_ev<=b1_ev#init+1,
//    d0_ev#init<=d0_ev, d0_ev<=d0_ev#init+1, d1_ev#init<=d1_ev,
//    d1_ev<=d1_ev#init+1, z_ev#init<=z_ev, z_ev<=z_ev#init+1}


   return;
}

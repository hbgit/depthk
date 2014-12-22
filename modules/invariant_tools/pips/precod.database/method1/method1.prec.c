
//  P() {comp_m1_st==1}

void method1(void)
{

//  P() {comp_m1_st==1}

   int s1;

//  P(s1) {comp_m1_st==1}

   int s2;

//  P(s1,s2) {comp_m1_st==1}

   int s3;

//  P(s1,s2,s3) {comp_m1_st==1}

   if (b0_val)

//  P(s1,s2,s3) {comp_m1_st==1}

      if (d1_val)

//  P(s1,s2,s3) {comp_m1_st==1}

         s1 = 0;
      else

//  P(s1,s2,s3) {comp_m1_st==1, d1_val==0}

         s1 = 1;
   else

//  P(s1,s2,s3) {b0_val==0, comp_m1_st==1}

      s1 = 1;

//  P(s1,s2,s3) {comp_m1_st==1, 0<=s1, s1<=1}

   if (d0_val)

//  P(s1,s2,s3) {comp_m1_st==1, 0<=s1, s1<=1}

      if (b1_val)

//  P(s1,s2,s3) {comp_m1_st==1, 0<=s1, s1<=1}

         s2 = 0;
      else

//  P(s1,s2,s3) {b1_val==0, comp_m1_st==1, 0<=s1, s1<=1}

         s2 = 1;
   else

//  P(s1,s2,s3) {comp_m1_st==1, d0_val==0, 0<=s1, s1<=1}

      s2 = 1;

//  P(s1,s2,s3) {comp_m1_st==1, 0<=s1, s1<=1, 0<=s2, s2<=1}

   if (s2)

//  P(s1,s2,s3) {comp_m1_st==1, s2==1, 0<=s1, s1<=1}

      s3 = 0;
   else if (s1)

//  P(s1,s2,s3) {comp_m1_st==1, s1==1, s2==0}

      s3 = 0;
   else

//  P(s1,s2,s3) {comp_m1_st==1, s1==0, s2==0}

      s3 = 1;

//  P(s1,s2,s3) {comp_m1_st==1, 1<=s1+s2+s3, s1+s3<=1, s2+s3<=1,
//    0<=s3}

   if (s2)

//  P(s1,s2,s3) {comp_m1_st==1, 1<=s1+s2+s3, s1+s3<=1, 1<=s2,
//    s2+s3<=1, 0<=s3}

      if (s1)

//  P(s1,s2,s3) {comp_m1_st==1, 1<=s1, s1+s3<=1, 1<=s2, s2+s3<=1,
//    0<=s3}

         s2 = 1;
      else

//  P(s1,s2,s3) {comp_m1_st==1, s1==0, 1<=s2, s2+s3<=1, 0<=s3, s3<=1}

         s2 = 0;
   else

//  P(s1,s2,s3) {comp_m1_st==1, s1+s3==1, s2==0, 0<=s1, s1<=1}

      s2 = 0;

//  P(s1,s2,s3) {comp_m1_st==1, s2<=s1, s1+s3<=1, 0<=s2, 0<=s3}

   if (s2)

//  P(s1,s2,s3) {comp_m1_st==1, s2<=s1, s1+s3<=1, 1<=s2, 0<=s3}

      z_val_t = 0;
   else if (s3)

//  P(s1,s2,s3) {comp_m1_st==1, s2==0, 0<=s1, s1+s3<=1, 1<=s3}

      z_val_t = 0;
   else

//  P(s1,s2,s3) {comp_m1_st==1, s2==0, s3==0, 0<=s1, s1<=1}

      z_val_t = 1;

//  P(s1,s2,s3,z_val_t) {comp_m1_st==1, s2+s3+z_val_t==1, s2<=s1,
//    s1+s3<=1, 0<=s2, 0<=s3}

   z_req_up = 1;

//  P(s1,s2,s3,z_req_up,z_val_t) {comp_m1_st==1, s2+s3+z_val_t==1,
//    z_req_up==1, s2<=s1, s1+s3<=1, 0<=s2, 0<=s3}

   comp_m1_st = 2;

//  P(comp_m1_st,s1,s2,s3,z_req_up,z_val_t) {comp_m1_st==2,
//    comp_m1_st#init==1, s2+s3+z_val_t==1, z_req_up==1, s2<=s1,
//    s1+s3<=1, 0<=s2, 0<=s3}


   return;
}

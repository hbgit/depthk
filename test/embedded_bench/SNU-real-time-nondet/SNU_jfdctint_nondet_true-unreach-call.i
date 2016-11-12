# 1 "SNU_jfdctint_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_jfdctint_nondet_true-unreach-call.c"
# 196 "SNU_jfdctint_nondet_true-unreach-call.c"
int data[64];





 void
jpeg_fdct_islow ()
{
  int tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7;
  int tmp10, tmp11, tmp12, tmp13;
  int z1, z2, z3, z4, z5;
  int *dataptr;
  int ctr;
 





  dataptr = data;
  for (ctr = 8 -1; ctr >= 0; ctr--) {
    tmp0 = dataptr[0] + dataptr[7];
    tmp7 = dataptr[0] - dataptr[7];
    tmp1 = dataptr[1] + dataptr[6];
    tmp6 = dataptr[1] - dataptr[6];
    tmp2 = dataptr[2] + dataptr[5];
    tmp5 = dataptr[2] - dataptr[5];
    tmp3 = dataptr[3] + dataptr[4];
    tmp4 = dataptr[3] - dataptr[4];





    tmp10 = tmp0 + tmp3;
    tmp13 = tmp0 - tmp3;
    tmp11 = tmp1 + tmp2;
    tmp12 = tmp1 - tmp2;

    dataptr[0] = (int) ((tmp10 + tmp11) << 2);
    dataptr[4] = (int) ((tmp10 - tmp11) << 2);

    z1 = ((tmp12 + tmp13) * (((int) 4433)));
    dataptr[2] = (int) (((z1 + ((tmp13) * (((int) 6270)))) + (((int) 1) << ((13 -2)-1))) >> (13 -2))
                             ;
    dataptr[6] = (int) (((z1 + ((tmp12) * (- ((int) 15137)))) + (((int) 1) << ((13 -2)-1))) >> (13 -2))
                             ;






    z1 = tmp4 + tmp7;
    z2 = tmp5 + tmp6;
    z3 = tmp4 + tmp6;
    z4 = tmp5 + tmp7;
    z5 = ((z3 + z4) * (((int) 9633)));

    tmp4 = ((tmp4) * (((int) 2446)));
    tmp5 = ((tmp5) * (((int) 16819)));
    tmp6 = ((tmp6) * (((int) 25172)));
    tmp7 = ((tmp7) * (((int) 12299)));
    z1 = ((z1) * (- ((int) 7373)));
    z2 = ((z2) * (- ((int) 20995)));
    z3 = ((z3) * (- ((int) 16069)));
    z4 = ((z4) * (- ((int) 3196)));

    z3 += z5;
    z4 += z5;

    dataptr[7] = (int) (((tmp4 + z1 + z3) + (((int) 1) << ((13 -2)-1))) >> (13 -2));
    dataptr[5] = (int) (((tmp5 + z2 + z4) + (((int) 1) << ((13 -2)-1))) >> (13 -2));
    dataptr[3] = (int) (((tmp6 + z2 + z3) + (((int) 1) << ((13 -2)-1))) >> (13 -2));
    dataptr[1] = (int) (((tmp7 + z1 + z4) + (((int) 1) << ((13 -2)-1))) >> (13 -2));

    dataptr += 8;
  }






  dataptr = data;
  for (ctr = 8 -1; ctr >= 0; ctr--) {
    tmp0 = dataptr[8*0] + dataptr[8*7];
    tmp7 = dataptr[8*0] - dataptr[8*7];
    tmp1 = dataptr[8*1] + dataptr[8*6];
    tmp6 = dataptr[8*1] - dataptr[8*6];
    tmp2 = dataptr[8*2] + dataptr[8*5];
    tmp5 = dataptr[8*2] - dataptr[8*5];
    tmp3 = dataptr[8*3] + dataptr[8*4];
    tmp4 = dataptr[8*3] - dataptr[8*4];





    tmp10 = tmp0 + tmp3;
    tmp13 = tmp0 - tmp3;
    tmp11 = tmp1 + tmp2;
    tmp12 = tmp1 - tmp2;

    dataptr[8*0] = (int) (((tmp10 + tmp11) + (((int) 1) << ((2)-1))) >> (2));
    dataptr[8*4] = (int) (((tmp10 - tmp11) + (((int) 1) << ((2)-1))) >> (2));

    z1 = ((tmp12 + tmp13) * (((int) 4433)));
    dataptr[8*2] = (int) (((z1 + ((tmp13) * (((int) 6270)))) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;
    dataptr[8*6] = (int) (((z1 + ((tmp12) * (- ((int) 15137)))) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;






    z1 = tmp4 + tmp7;
    z2 = tmp5 + tmp6;
    z3 = tmp4 + tmp6;
    z4 = tmp5 + tmp7;
    z5 = ((z3 + z4) * (((int) 9633)));

    tmp4 = ((tmp4) * (((int) 2446)));
    tmp5 = ((tmp5) * (((int) 16819)));
    tmp6 = ((tmp6) * (((int) 25172)));
    tmp7 = ((tmp7) * (((int) 12299)));
    z1 = ((z1) * (- ((int) 7373)));
    z2 = ((z2) * (- ((int) 20995)));
    z3 = ((z3) * (- ((int) 16069)));
    z4 = ((z4) * (- ((int) 3196)));

    z3 += z5;
    z4 += z5;

    dataptr[8*7] = (int) (((tmp4 + z1 + z3) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;
    dataptr[8*5] = (int) (((tmp5 + z2 + z4) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;
    dataptr[8*3] = (int) (((tmp6 + z2 + z3) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;
    dataptr[8*1] = (int) (((tmp7 + z1 + z4) + (((int) 1) << ((13 +2)-1))) >> (13 +2))
                              ;

    dataptr++;
  }
}
# 360 "SNU_jfdctint_nondet_true-unreach-call.c"
int nondet_int();

int main(int argc, char *argv[])
{
  int i, seed;



  for (i = 0; i < 64; i++) {
    data[i] = nondet_int();
  }

  jpeg_fdct_islow();

  return 0;
}

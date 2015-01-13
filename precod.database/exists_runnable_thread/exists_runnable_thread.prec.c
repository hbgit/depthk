
//  P() {}

int exists_runnable_thread(void)
{

//  P() {}

   int __retres1;

//  P(__retres1) {}

   if (comp_m1_st==0) goto l99999;

//  P(__retres1) {}

   __retres1 = 0;
   goto return_label;
l99999:   ;

//  P(__retres1) {comp_m1_st==0}

   __retres1 = 1;

//  P(__retres1) {0<=__retres1, __retres1<=1}

   /* CIL Label */
return_label:   return __retres1;
}

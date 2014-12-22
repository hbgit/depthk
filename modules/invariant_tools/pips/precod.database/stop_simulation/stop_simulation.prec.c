
//  P() {}

int stop_simulation(void)
{

//  P() {}

   int tmp;

//  P(tmp) {}

   int __retres2;

//  P(__retres2,tmp) {}

   tmp = exists_runnable_thread();

//  P(__retres2,tmp) {0<=tmp, tmp<=1}

   if (tmp) goto l99999;

//  P(__retres2,tmp) {tmp==0}

   __retres2 = 1;
   goto return_label;
l99999:   ;

//  P(__retres2,tmp) {tmp==1}

   __retres2 = 0;

//  P(__retres2,tmp) {__retres2+tmp==1, 0<=__retres2, __retres2<=1}

   /* CIL Label */
return_label:   return __retres2;
}

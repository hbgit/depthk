
//  P() {}

int is_method1_triggered(void)
{

//  P() {}

   int __retres1;

//  P(__retres1) {}

   if (b0_ev==1) goto l99999;

//  P(__retres1) {}

   if (b1_ev==1) goto l99998;

//  P(__retres1) {}

   if (d0_ev==1) goto l99997;

//  P(__retres1) {}

   if (d1_ev==1) goto l99996;

//  P(__retres1) {}

   __retres1 = 0;
   goto return_label;
l99996:   ;

//  P(__retres1) {d1_ev==1}

   __retres1 = 1;
   goto return_label;
l99997:   ;

//  P(__retres1) {d0_ev==1}

   __retres1 = 1;
   goto return_label;
l99998:   ;

//  P(__retres1) {b1_ev==1}

   __retres1 = 1;
   goto return_label;
l99999:   ;

//  P(__retres1) {b0_ev==1}

   __retres1 = 1;

//  P(__retres1) {0<=__retres1, __retres1<=1}

   /* CIL Label */
return_label:   return __retres1;
}

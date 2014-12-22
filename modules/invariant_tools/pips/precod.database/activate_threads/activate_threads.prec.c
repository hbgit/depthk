
//  P() {}

void activate_threads(void)
{

//  P() {}

   int tmp;

//  P(tmp) {}

   tmp = is_method1_triggered();

//  P(tmp) {0<=tmp, tmp<=1}

   if (tmp)

//  P(tmp) {tmp==1}

      comp_m1_st = 0;

//  P(comp_m1_st,tmp) {0<=tmp, tmp<=1}


   return;
}

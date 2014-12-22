# 274
void eval(void)
{ int tmp ;
  int tmp___0 ;
 // int __VERIFIER_nondet_int(); 

  {
  {
  while (1) {
    while_0_continue: /* CIL Label */ ;
    {
    tmp___0 = exists_runnable_thread();
    }
    if (tmp___0) {

    } else {
      goto while_0_break;
    }
    if ((int )comp_m1_st == 0) {
      {
 tmp = __VERIFIER_nondet_int();
      }
      if (tmp) {
        {
        comp_m1_st = 1;
        method1();
        }
      } else {

      }
    } else {

    }
  }
  while_0_break: /* CIL Label */ ;
  }

  return;
}
}

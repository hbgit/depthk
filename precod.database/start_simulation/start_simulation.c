# 414
void start_simulation(void)
{ int kernel_st ;
  int tmp ;

  {
  {
  kernel_st = 0;
  update_channels();
  init_threads();
  fire_delta_events();
  activate_threads();
  reset_delta_events();
  }
  {
  while (1) {
    while_1_continue: /* CIL Label */ ;
    {
    kernel_st = 1;
    eval();
    }
    {
    kernel_st = 2;
    update_channels();
    }
    {
    kernel_st = 3;
    fire_delta_events();
    activate_threads();
    reset_delta_events();
    tmp = stop_simulation();
    }
    if (tmp) {
      goto while_1_break;
    } else {

    }
  }
  while_1_break: /* CIL Label */ ;
  }

  return;
}
}

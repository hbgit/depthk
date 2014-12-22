# 98
int is_method1_triggered(void)
{ int __retres1 ;

  {
  if ((int )b0_ev == 1) {
    __retres1 = 1;
    goto return_label;
  } else {
    if ((int )b1_ev == 1) {
      __retres1 = 1;
      goto return_label;
    } else {
      if ((int )d0_ev == 1) {
        __retres1 = 1;
        goto return_label;
      } else {
        if ((int )d1_ev == 1) {
          __retres1 = 1;
          goto return_label;
        } else {

        }
      }
    }
  }
  __retres1 = 0;
  return_label: /* CIL Label */
  return (__retres1);
}
}

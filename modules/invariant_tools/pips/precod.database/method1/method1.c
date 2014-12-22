# 41
void method1(void)
{ int s1 ;
  int s2 ;
  int s3 ;

  {
  if (b0_val) {
    if (d1_val) {
      s1 = 0;
    } else {
      s1 = 1;
    }
  } else {
    s1 = 1;
  }
  if (d0_val) {
    if (b1_val) {
      s2 = 0;
    } else {
      s2 = 1;
    }
  } else {
    s2 = 1;
  }
  if (s2) {
    s3 = 0;
  } else {
    if (s1) {
      s3 = 0;
    } else {
      s3 = 1;
    }
  }
  if (s2) {
    if (s1) {
      s2 = 1;
    } else {
      s2 = 0;
    }
  } else {
    s2 = 0;
  }
  if (s2) {
    z_val_t = 0;
  } else {
    if (s3) {
      z_val_t = 0;
    } else {
      z_val_t = 1;
    }
  }
  z_req_up = 1;
  comp_m1_st = 2;

  return;
}
}

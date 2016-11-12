# 1 "SNU_fft1_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_fft1_nondet_true-unreach-call.c"
# 52 "SNU_fft1_nondet_true-unreach-call.c"
double ar[8];
double ai[8] = {0., };

int fft1(int n, int flag);


static double fabs(double n)
{
  double f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}

static double log(double n)
{
  return(4.5);
}


static double sin(double rad)

{
  double app;

  double diff;
  int inc = 1;

  while (rad > 2*3.14159)
 rad -= 2*3.14159;
  while (rad < -2*3.14159)
    rad += 2*3.14159;
  app = diff = rad;
   diff = (diff * (-(rad*rad))) /
      ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  while(fabs(diff) >= 0.00001) {
    diff = (diff * (-(rad*rad))) /
      ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  }

  return(app);
}


static double cos(double rad)
{


  return (sin (3.14159 / 2.0 - rad));
}

double nondet_double();

void main()
{

 int i, n = 8, flag, chkerr;



 for(i = 0; i < n; i++)
 {
   ar[i] = nondet_double();
   ai[i] = nondet_double();
 }


 flag = 0;
 chkerr = fft1(n, flag);


 flag = 1;
 chkerr = fft1(n, flag);

}



int fft1(int n, int flag)
{

  int i, j, k, it, xp, xp2, j1, j2, iter;
  double sign, w, wr, wi, dr1, dr2, di1, di2, tr, ti, arg;

  if(n < 2) return(999);
  iter = log((double)n)/log(2.0);
  j = 1;



  for(i = 0; i < iter; i++)
    j *= 2;




  sign = ((flag == 1) ? 1.0 : -1.0);
  xp2 = n;
  for(it = 0; it < iter; it++)
  {
    xp = xp2;
    xp2 /= 2;
    w = 3.14159 / xp2;



    for(k = 0; k < xp2; k++)
    {
      arg = k * w;
      wr = cos(arg);
      wi = sign * sin(arg);
      i = k - xp;
      for(j = xp; j <= n; j += xp)
      {
        j1 = j + i;
        j2 = j1 + xp2;
        dr1 = ar[j1];
        dr2 = ar[j2];
        di1 = ai[j1];
        di2 = ai[j2];
        tr = dr1 - dr2;
        ti = di1 - di2;
        ar[j1] = dr1 + dr2;
        ai[j1] = di1 + di2;
        ar[j2] = tr * wr - ti * wi;
        ai[j2] = ti * wr + tr * wi;
      }
    }
  }



  j1 = n / 2;
  j2 = n - 1;
  j = 1;



  for(i = 1; i <= j2; i++)
  {
    if(i < j)
    {
     tr = ar[j-1];
     ti = ai[j-1];
     ar[j-1] = ar[i-1];
     ai[j-1] = ai[i-1];
     ar[i-1] = tr;
     ai[i-1] = ti;
    }
    k = j1;
    while(k < j)
    {
     j -= k;
     k /= 2;
    }
    j += k;
  }
  if(flag == 0) return(0);
  w = n;
  for(i = 0; i < n; i++)
  {
    ar[i] /= w;
    ai[i] /= w;
  }
  return(0);
}

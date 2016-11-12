# 1 "SNU_qurt_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_qurt_nondet_true-unreach-call.c"
# 55 "SNU_qurt_nondet_true-unreach-call.c"
double a[3], x1[2], x2[2];
int flag;

int qurt();


double fabs(double n)
{
  double f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}

double sqrt(double val)

{
  double x = val/10;

  double dx;

  double diff;
  double min_tol = 0.00001;

  int i, flag;

  flag = 0;
  if (val == 0 ) x = 0;
  else {
    for (i=1;i<20;i++)
      {
 if (!flag) {
   dx = (val - (x*x)) / (2.0 * x);
   x = x + dx;
   diff = val - (x*x);
   if (fabs(diff) <= min_tol) flag = 1;
 }
 else
   x =x;
      }
  }
  return (x);
}

double nondet_double();

void main()
{
  a[0] = nondet_double();
  a[1] = nondet_double();
  a[2] = nondet_double();

  qurt();

  a[0] = nondet_double();
  a[1] = nondet_double();
  a[2] = nondet_double();

  qurt();

  a[0] = nondet_double();
  a[1] = nondet_double();
  a[2] = nondet_double();

  qurt();
}

int qurt()
{
 double d, w1, w2;

 if(a[0] == 0.0) return(999);
 d = a[1]*a[1] - 4 * a[0] * a[2];
 w1 = 2.0 * a[0];
 w2 = sqrt(fabs(d));
 if(d > 0.0)
 {
   flag = 1;
   x1[0] = (-a[1] + w2) / w1;
   x1[1] = 0.0;
   x2[0] = (-a[1] - w2) / w1;
   x2[1] = 0.0;
   return(0);
 }
 else if(d == 0.0)
 {
   flag = 0;
   x1[0] = -a[1] / w1;
   x1[1] = 0.0;
   x2[0] = x1[0];
   x2[1] = 0.0;
   return(0);
 }
 else
 {
   flag = -1;
   w2 /= w1;
   x1[0] = -a[1] / w1;
   x1[1] = w2;
   x2[0] = x1[0];
   x2[1] = -w2;
   return(0);
 }
}

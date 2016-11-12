# 1 "SNU_ludcmp_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_ludcmp_nondet_true-unreach-call.c"
# 61 "SNU_ludcmp_nondet_true-unreach-call.c"
double a[50][50], b[50], x[50];

int ludcmp(int nmax, int n, double eps);


static double fabs(double n)
{
  double f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}

double nondet_double();

void main()
{

  int i, j, nmax = 50, n = 5, chkerr;
  double eps=nondet_double();

  for(i = 0; i <= n; i++)
    {
      for(j = 0; j <= n; j++)
 {
   a[i][j] = nondet_double();
 }
      b[i] = nondet_double();
      x[i] = nondet_double();
    }

  chkerr = ludcmp(nmax, n, eps);

}

int ludcmp(int nmax, int n, double eps)
{

 int i, j, k;
 double w, y[100];

 if(n > 99 || eps <= 0.0) return(999);
 for(i = 0; i < n; i++)
 {
   if(fabs(a[i][i]) <= eps) return(1);
   for(j = i+1; j <= n; j++)
   {
     w = a[j][i];
     if(i != 0)
       for(k = 0; k < i; k++)
         w -= a[j][k] * a[k][i];
     a[j][i] = w / a[i][i];
   }
   for(j = i+1; j <= n; j++)
     {
       w = a[i+1][j];
       for(k = 0; k <= i; k++)
         w -= a[i+1][k] * a[k][j];
       a[i+1][j] = w;
     }
 }
 y[0] = b[0];
 for(i = 1; i <= n; i++)
   {
     w = b[i];
     for(j = 0; j < i; j++)
       w -= a[i][j] * y[j];
     y[i] = w;
   }
 x[n] = y[n] / a[n][n];
 for(i = n-1; i >= 0; i--)
   {
     w = y[i];
     for(j = i+1; j <= n; j++)
       w -= a[i][j] * x[j];
     x[i] = w / a[i][i] ;
   }
 return(0);

}

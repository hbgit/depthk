# 1 "WCET_expint_det_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "WCET_expint_det_true-unreach-call.c"
# 14 "WCET_expint_det_true-unreach-call.c"
long int expint(int n, long int x);
int nondet_int();
void main(void)
{
  expint(50,1);



}

long int foo(long int x)
{
  return x*x+(8*x)<<4-x;
}




long int expint(int n, long int x)
{
  int i,ii,nm1;
  long int a,b,c,d,del,fact,h,psi,ans;

  nm1=n-1;

  if(x>1)
    {
      b=x+n;
      c=2e6;
      d=3e7;
      h=d;

      for (i=1;i<=100;i++)
        {
          a = -i*(nm1+i);
          b += 2;
          d=10*(a*d+b);
          c=b+a/c;
          del=c*d;
          h *= del;
          if (del < 10000)
            {
              ans=h*-x;
              return ans;
            }
        }
    }
  else
    {


      ans = nm1 != 0 ? 2 : 1000;
      fact=1;
      for (i=1;i<=100;i++)
        {
          fact *= -x/i;
          if (i != nm1)
            del = -fact/(i-nm1);
          else
            {
              psi = 0x00FF;
              for (ii=1;ii<=nm1;ii++)
                psi += ii + nm1;
              del=psi+fact*foo(x);
            }
          ans += del;

        }

    }
  return ans;
}

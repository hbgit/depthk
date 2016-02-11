# 1 "SNU_fibcall_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_fibcall_nondet_true-unreach-call.c"
# 51 "SNU_fibcall_nondet_true-unreach-call.c"
fib(int n)

{
  int i, Fnew, Fold, temp,ans;

    Fnew = 1; Fold = 0;
    i = 2;
    while( i <= n ) {
      temp = Fnew;
      Fnew = Fnew + Fold;
      Fold = temp;
      i++;



    }
    ans = Fnew;



  return ans;
}

main()
{
  int a;



  fib(a);
}

# 1 "SNU_insertsort_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_insertsort_nondet_true-unreach-call.c"
# 50 "SNU_insertsort_nondet_true-unreach-call.c"
int nondet_int();

main()
{
  int i,j, temp, a[11];







  for(i=0; i<11; i++)
    a[i]=nondet_int();

  i = 2;
  while(i <= 10){



      j = i;



      while (a[j] < a[j-1])
      {



 temp = a[j];
 a[j] = a[j-1];
 a[j-1] = temp;
 j--;
      }



      i++;
    }



}

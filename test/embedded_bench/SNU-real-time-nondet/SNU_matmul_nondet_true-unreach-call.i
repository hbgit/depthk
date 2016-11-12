# 1 "SNU_matmul_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_matmul_nondet_true-unreach-call.c"
# 46 "SNU_matmul_nondet_true-unreach-call.c"
int a[5 +1][5 +1] = { {0,0,0,0,0,0},
       {0,0,9,4,7,9},
       {0,12,14,15,16,11},
       {0,2,3,4,5,6},
       {0,4,3,2,1,2},
       {0,2,7,6,4,9} };
int b[5 +1][5 +1] = { {0,0,0,0,0,0},
       {0,0,9,4,7,9},
       {0,12,14,15,16,11},
       {0,2,3,4,5,6},
       {0,4,3,2,1,2},
       {0,2,7,6,4,9} };
int c[5 +1][5 +1];

matmul(int a[5 +1][5 +1],int b[5 +1][5 +1],int c[5 +1][5 +1])

{
  int i,j,k;

IL0: for(i=1;i<=5;i++)
  IL1: for(j=1;j<=5;j++)
      c[i][j] = 0;

CL0: for(i=1;i<=5;i++)
 CL1: for(j=1;j<=5;j++)
  CL2: for(k=1;k<=5;k++)
 c[i][j] += a[i][k] * b[k][j];

}

int nondet_int();

main()
{
  int i,j;
  for(i=1; i<=5; i++)
    for(j=1; j<=5; j++)
    {
      a[i][j]=nondet_int();
      b[i][j]=nondet_int();
    }

    matmul(a,b,c);
}

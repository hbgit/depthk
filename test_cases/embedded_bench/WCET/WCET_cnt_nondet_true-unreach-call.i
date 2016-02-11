# 1 "WCET_cnt_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "WCET_cnt_nondet_true-unreach-call.c"
# 13 "WCET_cnt_nondet_true-unreach-call.c"
typedef int matrix [10][10];


int main(void);
int Test(matrix);
int Initialize(matrix);
int InitSeed(void);
void Sum(matrix);
int RandomInteger(void);


int Seed;
matrix Array;
int Postotal, Negtotal, Poscnt, Negcnt;


int main (void)
{
   InitSeed();


   Test(Array);
   return 1;
}


int Test(matrix Array)
{
   long StartTime, StopTime;
   float TotalTime;

   Initialize(Array);
   StartTime = 1000.0;
   Sum(Array);
   StopTime = 1500.0;

   TotalTime = (StopTime - StartTime) / 1000.0;






   return 0;
}

int nondet_int();

int Initialize(matrix Array)
{
   register int OuterIndex, InnerIndex;

   for (OuterIndex = 0; OuterIndex < 10; OuterIndex++)
      for (InnerIndex = 0; InnerIndex < 10; InnerIndex++)
         Array[OuterIndex][InnerIndex] = nondet_int();

   return 0;
}



int InitSeed (void)
{
   Seed = 0;
   return 0;
}

void Sum(matrix Array)
{
  register int Outer, Inner;

  int Ptotal = 0;
  int Ntotal = 0;
  int Pcnt = 0;
  int Ncnt = 0;

  for (Outer = 0; Outer < 10; Outer++)
    for (Inner = 0; Inner < 10; Inner++)



 if (Array[Outer][Inner] < 0) {

   Ptotal += Array[Outer][Inner];
   Pcnt++;
 }
 else {
   Ntotal += Array[Outer][Inner];
   Ncnt++;
 }

  Postotal = Ptotal;
  Poscnt = Pcnt;
  Negtotal = Ntotal;
  Negcnt = Ncnt;
}
# 124 "WCET_cnt_nondet_true-unreach-call.c"
int RandomInteger(void)
{
   Seed = ((Seed * 133) + 81) % 8095;
   return Seed;
}

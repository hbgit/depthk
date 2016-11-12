# 1 "SNU_bs_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_bs_nondet_true-unreach-call.c"
# 46 "SNU_bs_nondet_true-unreach-call.c"
struct DATA {
  int key;
  int value;
} ;





struct DATA data[15] = { {1, 100},
      {5, 200},
      {6, 300},
      {7, 700},
      {8, 900},
      {9, 250},
      {10, 400},
      {11, 600},
      {12, 800},
      {13, 1500},
      {14, 1200},
      {15, 110},
      {16, 140},
      {17, 133},
      {18, 10} };

binary_search(int x)
{
  int fvalue, mid, up, low ;

  low = 0;
  up = 14;
  fvalue = -1 ;
  while (low <= up) {
    mid = (low + up) >> 1;
    if ( data[mid].key == x ) {
      up = low - 1;
      fvalue = data[mid].value;



    }
    else
      if ( data[mid].key > x ) {
 up = mid - 1;



      }
      else {
              low = mid + 1;



      }



  }



  return fvalue;
}

int nondet_int();

main()
{
  unsigned i;
  for(i=0; i<15; i++)
     data[i].value=nondet_int();

 binary_search(8);
}

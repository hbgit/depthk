# 1 "WCET_duff_det_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "WCET_duff_det_true-unreach-call.c"
# 23 "WCET_duff_det_true-unreach-call.c"
void duffcopy( char *to, char *from, int count)
{
  int n=(count+7)/8;
  switch(count%8){
  case 0: do{ *to++ = *from++;
  case 7: *to++ = *from++;
  case 6: *to++ = *from++;
  case 5: *to++ = *from++;
  case 4: *to++ = *from++;
  case 3: *to++ = *from++;
  case 2: *to++ = *from++;
  case 1: *to++ = *from++;
  } while(--n>0);
  }
}
void initialize( char *arr, int length)
{
  int i;
  for(i=0;i<length;i++)
    {
      arr[i] = length-i;
    }
}


char source[100];
char target[100];

void main(void)
{
  initialize( source, 100 );
  duffcopy( source, target, 43 );
}

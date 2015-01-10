#include <assert.h>
int __BLAST_NONDET;

int __VERIFIER_nondet_int();

int main(){
  int x=1030792151;
  int y=-2147483645;
  int z=10;
  int w=1;

  
  // Like CEGAR
  /**
   * NO BUGs - deadline: 12/12
   * (1) From test cases - Inv
   * (2) esbmc_v1_24 --64 --no-library --k-induction-parallel --memlimit 4g --z3 --inductive-step --show-counter-example confuse_cl2.c
   * (3) From (2) generate the invariants from each inductive-step FAILED
   * (4) STOP: SUCCESSFUL the inductive-step
   * 
   * */

  __ESBMC_assume( x > 0);
  
  while ( __VERIFIER_nondet_int() ){
    if ( __VERIFIER_nondet_int() ) {
      x++; y = y+100;
    } else if  ( __VERIFIER_nondet_int() ) {
      if( x >= 4)
	{ x=x+1; y=y+1;}
    } else if  ( y >10*w)
      if (z>=100*x )
      y = -y;
    w=w+1; 
    z=z+10;
  }
  if ( x >=4 )
    assert(y>2);

}

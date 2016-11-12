#include <stdio.h>
#include <assert.h>

extern int __VERIFIER_nondet_int(void);

void main(){
    int x = 0 ;
    int t = 0 ;
    int n = /* reachable */
            __VERIFIER_nondet_int();
    int phase = 0 ;

    while ( t < n) {
__ESBMC_assume( -2*x+t+3*phase == 0 ); 
__ESBMC_assume( 3-2*x+t >= 0 ); 
__ESBMC_assume( -x+2*t >= 0 ); 
__ESBMC_assume( 3221225469+x-2*t >= 0 ); 
__ESBMC_assume( 2*x-t >= 0 ); 
__ESBMC_assume( -1+n-t >= 0 ); 
        if( /* invariant:
            -2*x+t+3*phase = 0
            3-2*x+t >= 0
            -x+2*t >= 0
            3221225469+x-2*t >= 0
            2*x-t >= 0
            -1+n-t >= 0
            */
            phase == 0 ){
            // safe
            x = x + 2;
	}
        if( phase == 1 ){
            // safe
            x = x - 1;
	}
        // safe
        phase = 1 - phase;
        // unsafe: possible undefined behavior
        t++;
    }
    /* assert not proved */
    assert( x <= 100 ); 
/* reachable */
}

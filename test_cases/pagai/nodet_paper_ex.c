#include <stdio.h>
#include <assert.h>

extern int __VERIFIER_nondet_int(void);

void main(){
    int x = 0 ;
    int t = 0 ;
    int n = __VERIFIER_nondet_int();
    int phase = 0 ;

    while ( t < n) {
        if( phase == 0 ){
            x = x + 2;
	}
        if( phase == 1 ){
            x = x - 1;
	}
        phase = 1 - phase;
        t++;
    }
    assert( x <= 100 ); 
}

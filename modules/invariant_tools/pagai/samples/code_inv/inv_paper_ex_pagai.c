// analysis: AIopt
/* processing Function main */

void main(){
	int x = 0 ;
	int t = 0 ;
	int phase = 0 ;

/* reachable */
/* invariant:
	-3*phase-t+2*x = 0
	100-phase-t >= 0
	1-phase >= 0
	-phase+t >= 0
	phase >= 0
	*/	
	while ( t < 100) {
		if( phase == 0 )
			// safe
			x = x + 2;
		if( phase == 1 )
			// safe
			x = x - 1;
			// safe
			phase = 1 - phase;
			// safe
			t++;
	}

	/* invariant:
	-50+x = 0
	*/
	__ESBMC_assume( -50+x == 0 );	
	assert( x <= 100 );	
}

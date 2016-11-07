
//  P() {0==-1}


void main();

//  P() {}


void main()
{


//  P() {}


   int x = 0;

//  P(x) {x==0}

__ESBMC_assume( x==0 );

   int t = 0;

//  P(t,x) {t==0, x==0}

__ESBMC_assume( t==0 && x==0 );

   int phase = 0;

//  P(phase,t,x) {phase==0, t==0, x==0}

__ESBMC_assume( phase==0 && t==0 && x==0 );


   while (t<100) {

//  P(phase,t,x) {t<=99, x<=2t, 0<=t+x}

__ESBMC_assume( t<=99 && x<=2*t && 0<=t+x );

      if (phase==0) {

//  P(phase,t,x) {phase==0, t<=99, x<=2t, 0<=t+x}

__ESBMC_assume( phase==0 && t<=99 && x<=2*t && 0<=t+x );

         x = x+2;
      }

//  P(phase,t,x) {0<=t, t<=99, 0<=t+x, x<=2t+2}

__ESBMC_assume( 0<=t && t<=99 && 0<=t+x && x<=2*t+2 );

      if (phase==1) {

//  P(phase,t,x) {phase==1, 0<=t, t<=99, 0<=t+x, x<=2t+2}

__ESBMC_assume( phase==1 && 0<=t && t<=99 && 0<=t+x && x<=2*t+2 );

         x = x-1;
      }

//  P(phase,t,x) {0<=t, t<=99, x<=2t+2, 0<=t+x+1}

__ESBMC_assume( 0<=t && t<=99 && x<=2*t+2 && 0<=t+x+1 );

      phase = -phase+1;

//  P(phase,t,x) {0<=t, t<=99, x<=2t+2, 0<=t+x+1}

__ESBMC_assume( 0<=t && t<=99 && x<=2*t+2 && 0<=t+x+1 );

      t++;
   }

//  P(phase,t,x) {t==100, 0<=x+100, x<=200}

__ESBMC_assume( t==100 && 0<=x+100 && x<=200 );


   assert(x<=100);
}

extern void __VERIFIER_error();

void __VERIFIER_assert(int cond) {
  if (!(cond)) {
    ERROR: __VERIFIER_error();
  }
  return;
}

int main(void) {
  unsigned int x = 1; 
  __ESBMC_assume( x!=0 ); // FROM counterexample
  
//  P(x) {x==1}

//__ESBMC_assume( x==1 );
  
  unsigned int y = 0; 
  __ESBMC_assume( y!=100 ); // FROM counterexample
  
//  P(x,y) {x==1, y==0}

//__ESBMC_assume( x==1 && y==0 );  
  

  while (y < 1024) {
//  P(x,y) {0<=x, 1<=x+y, 1023x+y<=1023}

//__ESBMC_assume( 0<=x && 1<=x+y && 1023*x+y<=1023 );

    x = 0;  
//  P(x,y) {x==0, 0<=y, y<=1023}

//__ESBMC_assume( x==0 && 0<=y && y<=1023 );
   
    y++;     
  
  }
  
//  P(x,y) {x==0, y==1024}

__ESBMC_assume( x==0 && y==1024 );

  __VERIFIER_assert(x == 1);
}



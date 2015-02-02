extern void __VERIFIER_error() __attribute__ ((__noreturn__));

void __VERIFIER_assert(int cond) {
  if (!(cond)) {
    ERROR: __VERIFIER_error();
  }
  return;
}

int main(void) {
  unsigned int x = 1; 
  //__ESBMC_assume( x!=1 ); // FROM counterexample   
  unsigned int y = 0; //LINE  20 ???
  __ESBMC_assume( y!=1 ); // FROM counterexample
  //__ESBMC_assume( y!=0 ); // FROM counterexample

  while (y < 1024) {
    x = 0; 
    //__ESBMC_assume( x!=0 ); // FROM counterexample
    y++; 
    //__ESBMC_assume( y!=1 ); // FROM counterexample
  }

  __VERIFIER_assert(x == 1);
}



void __VERIFIER_assert(int cond) {
  if (!(cond)) {
    ERROR: goto ERROR;
  }
  return;
}

extern unsigned int __VERIFIER_nondet_uint();

int main(void) {
  unsigned int a = __VERIFIER_nondet_uint(); //interval from 0 to infinity

  __VERIFIER_assert(a == 1); 
  return a;
}


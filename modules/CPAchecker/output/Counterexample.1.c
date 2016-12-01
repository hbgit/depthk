void __VERIFIER_error();
void __VERIFIER_assert(int cond);
int main();
extern void __VERIFIER_error(void);
int main_0();
void __VERIFIER_assert_1(int cond);
int main_0() {
  unsigned int x;
  __CPROVER_assume(!(x < 268435455));
  __VERIFIER_assert_1(x > 268435455);
}

void __VERIFIER_assert_1(int cond) {
  __CPROVER_assume(cond == 0);
  __VERIFIER_error(); // target state
  __VERIFIER_error();
}

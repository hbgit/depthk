// analysis: AIopt
[0;1;34m/* processing Function main */
[0m/*
 * Run:
 * $ ./compile_llvm.sh -g -i ex1.c -o ex1.bc
 * $ ./pagai -i ex1.bc
 * */

#include "pagai_assert.h"

int main() {
	int x = 0;
	[0;1;35m/* reachable */
[0m	int y = 0;
	while[0;1;35m/* invariant:
	     102-x-y >= 0
	     y >= 0
	     x-y >= 0
	     */
[0m	      (1) {
		i[0;1;32m// safe
[0m		 f (x <= 50)  {
			y++;
		} else y--;
		if (y < 0) break;
		x++;
	}
	assert(x+y<=101);
	assert(x <= 102);
}

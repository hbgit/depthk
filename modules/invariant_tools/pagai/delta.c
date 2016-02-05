[0;1;35m[0m[0;1;35m[0m[0;1;32m[0m[0;1;32m[0m[0;1;32m[0m[0;1;32m[0m[0;1;35m[0m/*
 * Run:
 * $ ./compile_llvm.sh -g -i ex1.c -o ex1.bc
 * $ ./pagai -i ex1.bc
 * */

//#include "pagai_assert.h"

int main() {
	int x = 0;
	int y = 0;
	/* reachable */
	while (1) {
		if (/* invariant:
		    102-x-y >= 0
		    y >= 0
		    x-y >= 0
		    */
		    x <= 50)  {
			// safe
			y++;
		} else // safe
		       y--;
		if (y < 0) break;
		// safe
		x++;
	}
	// safe
	/* invariant:
	1+y = 0
	102-x >= 0
	-51+x >= 0
	*/
	assert(x+y<=101);
	assert(x <= 102);
}

// analysis: AIopt
[0;1;34m/* processing Function main */
[0m/*
 * Run:
 * $ ./compile_llvm.sh -g -i ex1.c -o ex1.bc
 * $ ./pagai -i ex1.bc
 * */

//#include "pagai_assert.h"

int main() {
	int x = 0;
	int y = 0;
	[0;1;35m/* reachable */
[0m	while (1) {
		if ([0;1;35m/* invariant:
		    102-x-y >= 0
		    y >= 0
		    x-y >= 0
		    */
[0m		    x <= 50)  {
			[0;1;32m// safe
[0m			y++;
		} else [0;1;32m// safe
[0m		       y--;
		if (y < 0) break;
		[0;1;32m// safe
[0m		x++;
	}
	[0;1;32m// safe
[0m	[0;1;35m/* invariant:
	1+y = 0
	102-x >= 0
	-51+x >= 0
	*/
[0m	assert(x+y<=101);
	assert(x <= 102);
}

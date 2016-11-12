#include <assert.h>

void main() {
  int x=0;
  int n;
 
  while( x < n ){
    //templ("(1;1)");
    x++;
  }
  if(n > 0)
    assert( x<=n);
}

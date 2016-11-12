#include <assert.h>

//int __BLAST_NONDET;
int __VERIFIER_nondet_int();


// This is an iterative version of merge sort.
// It merges pairs of two consecutive lists one after another.
// After scanning the whole array to do the above job,
// it goes to the next stage. Variable k controls the size
// of lists to be merged. k doubles each time the main loop
// is executed.

//#include <stdio.h>
//#include <math.h>

int i,n,t,k;
int l,r,u,j;
int x,y,z;
//int a[100000],b[100000];

main()
{ 
  x=1;
  while (x<n) {
    z=1;
    while (z+x<=n) {
      y=z+x*2;
      if (y>n) y=n+1;
      //      merge(z,z+x,y);
      l = z; r = z+x; u = y;
      i=l; j=r; k=l;
      while (i<r && j<u) { 
	//	assert(0<=i);assert(i<=n);
	//assert(0<=j);assert(j<=n);
	if(__VERIFIER_nondet_int()) {
	//if (a[i]<=a[j]) {
	  //assert(0<=i);assert(i<=n);
	  //assert(0<=k);assert(k<=n);
	  //b[k]=a[i]; 
	  i++;
	} 
	else {
	  //assert(0<=j);assert(j<=n);
	  //assert(0<=k);assert(k<=n);	  
	  //b[k]=a[j]; 
	  j++;
	}
	k++;
      }
      //assert(0<=r);assert(r<=n);
      
      assert(k<=n);
      
      while (i<r) {
	//assert(0<=i);assert(i<=n);
	//assert(0<=k);assert(k<=n);
	//b[k]=a[i]; 
	i++; 
	k++;
      }
      while (j<u) { 
	//assert(0<=j);assert(j<=n);
	//assert(0<=k);assert(k<=n);
	//b[k]=a[j]; 
	j++; k++;
      }
      for (k=l; k<u; k++) { 
	//assert(0<=k);assert(k<=n);
	//a[k]=b[k]; 
      }
      
      z=z+x*2;
    }
    x=x*2;
  }
}

/*
main()
{ printf("input size \n");
  scanf("%d",&n); 
  for (i=1;i<=n;i++) a[i]=random()%1000;
  t=clock();
  sort1();
  for (i=1;i<=10;i++) printf("%d ",a[i]);
  printf("\n");
  printf("time= %d millisec\n",(clock()-t)/1000);
}
*/

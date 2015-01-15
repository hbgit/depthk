#include "assert.h"

//int NONDET;
int __VERIFIER_nondet_int();

//invgen_template(pc(svdcmp-glb-0), (le(n,m,l,i,j,k),le(n,m,l,i,j,k))).


//void svdcmp(float **a, int m, int n, float w[], float **v)
void main()
     /*
       Given a matrix a[1..m][1..n], this routine computes its
       singular value decomposition, A = U·W·V
       T. Thematrix U replaces a on output. The diagonal matrix of
       singular values W is output as a vector w[1..n]. The matrix V
       (not the transpose V T ) is output as v[1..n][1..n].
     */

     /* Dimensions:

	a[1..m][1..n]

	w[1..n]

	v[1..n][1..n]

	rv1=vector(1,n)

     */
{
  //
  float **a;
  int m;
  int n; 
  //float w[];
  float **v;
  //  float pythag(float a, float b);
  int flag,i,its,j,jj,k,l,nm;
  // float anorm,c,f,g,h,s,scale,x,y,z,*rv1;

  //tmpl("(le(n,m,l,i,j,k),le(n,m,l,i,j,k))");

  //  rv1=vector(1,n);
  //  g=scale=anorm=0.0; // Householder reduction to bidiagonal form. 

  for (i=1;i<=n;i++) {
    l=i+1;
    
    assert(1<=i);assert(i<=n);
    //rv1[i]=scale*g;


    //g=s=scale=0.0;
    
    if (i <= m) {

      for (k=i;k<=m;k++) { 
	assert(1<=k);assert(k<=m);
	assert(1<=i);assert(i<=n);
	// scale += fabs(a[k][i]); 
      }

      if ( __VERIFIER_nondet_int() ) {

	for (k=i;k<=m;k++) {
	  
	  assert(1<=k);assert(k<=m);
	  assert(1<=i);assert(i<=n);
	  // a[k][i] = scale;

	  // s += a[k][i]*a[k][i];
	}

	assert(1<=i);assert(i<=m);
	assert(1<=i);assert(i<=n);
	// f=a[i][i];

	//	g = -SIGN(sqrt(s),f);
	//	h=f*g-s;


	assert(1<=i);assert(i<=m);
	assert(1<=i);assert(i<=n);
	//	a[i][i]=f-g;

	for (j=l;j<=n;j++) {
	  // s=0.0;
	  for (k=i;k<=m;k++) {

	    assert(1<=k);assert(k<=m);
	    assert(1<=i);assert(i<=n);
	    assert(1<=j);assert(j<=n);
	    //	    s += a[k][i]*a[k][j];
	  }
	  //	  f=s/h;
	  for (k=i;k<=m;k++) {

	    assert(1<=k);assert(k<=m);
	    assert(1<=i);assert(i<=n);
	    assert(1<=j);assert(j<=n);
	    //	    a[k][j] += f*a[k][i];
	  }
	}
	for (k=i;k<=m;k++) { 

	  assert(1<=k);assert(k<=m);
	  assert(1<=i);assert(i<=n);
	  //a[k][i] *= scale;
	}

      }

      }


    //    w[i]=scale * g;
    //    g=s=scale=0.0;
    if (i <= m && i != n) {
      for (k=l;k<=n;k++) {
	assert(1<=i);assert(i<=m);
	assert(1<=k);assert(k<=n);
	//	scale += fabs(a[i][k]);
      }
      if ( __VERIFIER_nondet_int() ) {
	for (k=l;k<=n;k++) {

	 assert(1<=i);assert(i<=m);
	 assert(1<=k);assert(k<=n);
	  //	  a[i][k] /= scale;


	 assert(1<=i);assert(i<=m);
	 assert(1<=k);assert(k<=n);
	  //	  s += a[i][k]*a[i][k];
	}
	
	assert(1<=i);assert(i<=m);
	assert(1<=l);assert(l<=n);
	//	f=a[i][l];

	//	g = -SIGN(sqrt(s),f);
	//h=f*g-s;

	assert(1<=i);assert(i<=m);
	assert(1<=l);assert(l<=n);
	// a[i][l]=f-g;
	for (k=l;k<=n;k++) { 

	 assert(1<=i);assert(i<=m);
	 assert(1<=k);assert(k<=n);
	  //  rv1[k]=a[i][k]/h;
	}
	for (j=l;j<=m;j++) {
	  // s=0.0;
	  for (k=l;k<=n;k++) { 

	    assert(1<=j);assert(j<=m);
	    assert(1<=i);assert(i<=m);
	    assert(1<=k);assert(k<=n);
	    //    s += a[j][k]*a[i][k];
	  }
	  for (k=l;k<=n;k++) { 

	    assert(1<=j);assert(j<=m);
	    assert(1<=k);assert(k<=n);
	    //  a[j][k] += s*rv1[k];
	  }
	}
	for (k=l;k<=n;k++) { 

	  assert(1<=i);assert(i<=m);
	  assert(1<=k);assert(k<=n);
	  // a[i][k] *= scale;
	}
      }
    }
    // Why there is no assert here?
    //    anorm=FMAX(anorm,(fabs(w[i])+fabs(rv1[i])));
  }


  for (i=n;i>=1;i--) { // Accumulation of right-hand transformations. 
    l = i+1;
    if (i < n) {
      if ( __VERIFIER_nondet_int() ) {
	for (j=l;j<=n;j++) { // Double division to avoid possible underflow. 

	  assert(1<=j);assert(j<=n);
	  assert(1<=i);assert(i<=n);
	  //assert(1<=i);assert(i<=m); // TODO feasible counterexample found, hm
	  assert(1<=l);assert(l<=n);
	  //  v[j][i]=(a[i][j]/a[i][l])/g;
	}
	for (j=l;j<=n;j++) {
	  // s = 0.0;
	  for (k=l;k<=n;k++) { 

	    //assert(1<=i);assert(i<=m); // TODO feasible counterexample found, hm
	    assert(1<=k);assert(k<=n);
	    assert(1<=j);assert(j<=n);
	    //  s += a[i][k]*v[k][j];
	  }
	  for (k=l;k<=n;k++) { 
	    assert(1<=k);assert(k<=n);
	    assert(1<=j);assert(j<=n);
	    assert(1<=i);assert(i<=n);
	    // v[k][j] += s*v[k][i];
	  }
	}
      }
      for (j=l;j<=n;j++) { 

        assert(1<=j);assert(j<=n);
	assert(1<=i);assert(i<=n);
	//v[i][j]=v[j][i]=0.0;
      }
    }

    assert(1<=i);assert(i<=n);
    //    v[i][i]=1.0;
    assert(1<=i);assert(i<=n);
    //   g=rv1[i];
    // l=i;
  }
  
  /*
  if (m<=n) { i = m; } else { i = n; } // TODO model min precisely!!!
  //  for (i=IMIN(m,n);i>=1;i--) { // Accumulation of left-hand transformations. 
  for ( ;i>=1;i--) { // Accumulation of left-hand transformations. 
    l=i+1;

    assert(1<=i);assert(i<=n); 
    //g=w[i];
    for (j=l;j<=n;j++) {

      assert(1<=i);assert(i<=m);
      assert(1<=j);assert(j<=n);
      //a[i][j]=0.0;
    }

    if ( NONDET ) {
      //g=1.0/g;
      for (j=l;j<=n;j++) {
	//s=0.0;
	for (k=l;k<=m;k++) {

	  assert(1<=k);assert(k<=m);
	  assert(1<=i);assert(i<=n);
	  assert(1<=j);assert(j<=n);
	  //  s += a[k][i]*a[k][j];
	}

	assert(1<=i);assert(i<=m);
	assert(1<=i);assert(i<=n);
	//f=(s/a[i][i])*g;
	for (k=i;k<=m;k++) {

	  assert(1<=k);assert(k<=m);
	  assert(1<=j);assert(j<=n);
	  assert(1<=i);assert(i<=n);
	  //a[k][j] += f*a[k][i];
	}
      }
      for (j=i;j<=m;j++) { 

	assert(1<=j);assert(j<=m); 
	assert(1<=i);assert(i<=n);
	//a[j][i] *= g;
      }
    } else for (j=i;j<=m;j++) { 
      assert(1<=j);assert(j<=m); 
      assert(1<=i);assert(i<=n);

      // a[j][i]=0.0;
    }
    
    assert(1<=i);assert(i<=m); 
    assert(1<=i);assert(i<=n);
    // ++a[i][i];
    }*/

}

/*
void svdcmp1(float **a, int m, int n, float w[], float **v)
{

  for (k=n;k>=1;k--) { 
    //Diagonalization of the bidiagonal form: Loop
    //over singular values, and over allowed
    //iterations. 
    for (its=1;its<=30;its++) { 
      flag=1;
      for (l=k;l>=1;l--) { // Test for splitting. 
	nm=l-1; // Note that rv1[1] is always zero.

	assert(1<=l);assert(l<=n); 
    	if ((float)(fabs(rv1[l])+anorm) == anorm) {
	  flag=0;
	  break;
	}
	//assert(1<=nm);assert(nm<=n); // TODO feasible counterexample found
	if ((float)(fabs(w[nm])+anorm) == anorm) break;
      }
      if ( NONDET ) {
	c=0.0; // Cancellation of rv1[l], if l > 1. 
	s=1.0;
	for (i=l;i<=k;i++) {

	  // assert(1<=i);assert(i<=n); // TODO feasible counterexample found
	  f=s*rv1[i];
	  rv1[i]=c*rv1[i];
	  if ((float)(fabs(f)+anorm) == anorm) break;


	  // assert(1<=i);assert(i<=n); // TODO feasible counterexample found
	  g=w[i];
	  h=pythag(f,g);

	  // assert(1<=i);assert(i<=n); // TODO feasible counterexample found
	  w[i]=h;
	  h=1.0/h;
	  c=g*h;
	  s = -f*h;
	  for (j=1;j<=m;j++) {

	    assert(1<=j);assert(j<=m);
	    // assert(1<=nm);assert(nm<=n); // TODO feasible counterexample found
	    // assert(1<=i);assert(i<=n); // TODO feasible counterexample found
	    y=a[j][nm];
	    z=a[j][i];
	    a[j][nm]=y*c+z*s;
	    a[j][i]=z*c-y*s;
	  }
	}
      }
      
      assert(1<=k);assert(k<=n);
      z=w[k];
      if (l == k) { // Convergence. 
	if (z < 0.0) { // Singular value is made nonnegative. 

	  assert(1<=k);assert(k<=n);
	  w[k] = -z;
	  for (j=1;j<=n;j++) { 

	    assert(1<=j);assert(j<=n);
	    assert(1<=k);assert(k<=n);
	    v[j][k] = -v[j][k];
	  }
	}
	break;
      }
      if (its == 30) nrerror("no convergence in 30 svdcmp iterations");

      //      assert(1<=l);assert(l<=n); // TODO feasible counterexample
      x=w[l]; // Shift from bottom 2-by-2 minor. 
      nm=k-1;

      //      assert(1<=nm);assert(nm<=n); // TODO feasible counterexample
      y=w[nm];
      g=rv1[nm];

      assert(1<=k);assert(k<=n);
      h=rv1[k];
      f=((y-z)*(y+z)+(g-h)*(g+h))/(2.0*h*y);
      g=pythag(f,1.0);
      f=((x-z)*(x+z)+h*((y/(f+SIGN(g,f)))-h))/x;
      c=s=1.0; // Next QR transformation: 
      for (j=l;j<=nm;j++) {
	i=j+1;

	assert(1<=i);assert(i<=n); 
	g=rv1[i];
	y=w[i];
	h=s*g;
	g=c*g;
	z=pythag(f,h);
	rv1[j]=z;
	c=f/z;
	s=h/z;
	f=x*c+g*s;
	g = g*c-x*s;
	h=y*s;
	y *= c;
	for (jj=1;jj<=n;jj++) {

	  assert(1<=jj);assert(jj<=n);
	  assert(1<=i);assert(i<=n); 
	  x=v[jj][j];
	  z=v[jj][i];

	  // assert(1<=j);assert(j<=n); // TODO feasible counterexample

	  v[jj][j]=x*c+z*s;
	  v[jj][i]=z*c-x*s;
	}
	z=pythag(f,h);

	// assert(1<=j);assert(j<=n); // TODO feasible counterexample 
	w[j]=z; // Rotation can be arbitrary if z = 0. 
	if ( NONDET ) {

	  if ( z == 0 ) { ERROR:; } 
	  z=1.0/z;
	  c=f*z;
	  s=h*z;
	}
	f=c*g+s*y;
	x=c*y-s*g;
	for (jj=1;jj<=m;jj++) {
	  assert(1<=jj);assert(jj<=m);
	  // assert(1<=j);assert(j<=n); // TODO feasible counterexample
	  assert(1<=i);assert(i<=n); 
	  y=a[jj][j];
	  z=a[jj][i];
	  a[jj][j]=y*c+z*s;
	  a[jj][i]=z*c-y*s;
	}
      }

      // assert(1<=l);assert(l<=n); // TODO feasible counterexample
      rv1[l]=0.0;

      assert(1<=k);assert(k<=n); 
      rv1[k]=f;
      w[k]=x;
    }
  }

  free_vector(rv1,1,n);

  }*/

# 1 "SNU_fft1k_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_fft1k_nondet_true-unreach-call.c"
# 48 "SNU_fft1k_nondet_true-unreach-call.c"
typedef struct {
    float real, imag;
} COMPLEX;

void fft_c(int n);
void init_w(int n);


int n = 1024;
COMPLEX x[1024],w[1024];


float fabs(float n)
{
  float f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}


float sin(float rad)

{
  float app;

  float diff;
  int inc = 1;

  while (rad > 2*3.14159265358979323846)
 rad -= 2*3.14159265358979323846;
  while (rad < -2*3.14159265358979323846)
    rad += 2*3.14159265358979323846;
  app = diff = rad;
   diff = (diff * (-(rad*rad))) /
      ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  while(fabs(diff) >= 0.00001) {
    diff = (diff * (-(rad*rad))) /
      ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  }

  return(app);
}

float cos(float rad)

{


  return (sin (3.14159265358979323846 / 2.0 - rad));
}



void main()
{
    int i;

    init_w(n);

    x[0].real = 1.0;
    fft_c(n);
}

void fft_c(int n)
{
    COMPLEX u,temp,tm;
    COMPLEX *xi,*xip,*wptr;

    int i,j,le,windex;



    windex = 1;
    for(le=n/2 ; le > 0 ; le/=2) {
        wptr = w;
        for (j = 0 ; j < le ; j++) {
            u = *wptr;
            for (i = j ; i < n ; i = i + 2*le) {
                xi = x + i;
  xip = xi + le;
                temp.real = xi->real + xip->real;
                temp.imag = xi->imag + xip->imag;
                tm.real = xi->real - xip->real;
                tm.imag = xi->imag - xip->imag;
                xip->real = tm.real*u.real - tm.imag*u.imag;
                xip->imag = tm.real*u.imag + tm.imag*u.real;
                *xi = temp;
            }
            wptr = wptr + windex;
        }
        windex = 2*windex;
    }
}

float nondet_float();

void init_w(int n)
{
    int i;
    float a = 2.0*3.14159265358979323846/n;
    for(i = 0 ; i < n ; i++) {
        w[i].real = nondet_float();
        w[i].imag = nondet_float();
    }
}

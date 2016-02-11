# 1 "POWERSTONE_fir_det_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "POWERSTONE_fir_det_true-unreach-call.c"
# 1 "common.h" 1





int abs(int x) {
  return x < 0 ? -x : x;
}

double fabs(double x) {
  return x < 0 ? -x : x;
}

void *memcpy(void *d, const void *s, unsigned long t) {
  while( t-- ) {
    *((char*)d) = *((char*)s);
  }
  return d;
}

int strncmp(const char *s1, const char *s2, unsigned long n) {
  unsigned long i;
  for(i=0; i<n; i++) {
    if ( s1[i] < s2[i] ) return -1;
    else if( s1[i] > s2[i] ) return +1;
  }
  return 0;
}

int rand(void) {
  static unsigned long next = 1;
  next = next * 1103515245 + 12345;
  return (unsigned int)(next/65536) % 32768;
}

double sin(double rad) {
  double app;
  double diff;
  int inc = 1;
  while( rad > 2*3.14159265358979323846 ) rad -= 2*3.14159265358979323846;
  while( rad < -2*3.14159265358979323846 ) rad += 2*3.14159265358979323846;
  app = diff = rad;
  diff = (diff * (-(rad*rad))) / ((2.0 * inc) * (2.0 * inc + 1.0));
  app = app + diff;
  inc++;
  while( fabs(diff) >= 0.00001 ) {
    diff = (diff * (-(rad*rad))) / ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  }
  return app;
}

double cos(double rad) {
  double app;
  double diff;
  int inc = 1;
  rad += 3.14159265358979323846/2.0;
  while( rad > 2*3.14159265358979323846 ) rad -= 2*3.14159265358979323846;
  while( rad < -2*3.14159265358979323846 ) rad += 2*3.14159265358979323846;
  app = diff = rad;
  diff = (diff * (-(rad*rad))) / ((2.0 * inc) * (2.0 * inc + 1.0));
  app = app + diff;
  inc++;
  while( fabs(diff) >= 0.00001 ) {
    diff = (diff * (-(rad*rad))) / ((2.0 * inc) * (2.0 * inc + 1.0));
    app = app + diff;
    inc++;
  }
  return app;
}

double tan(double rad) {
  return sin(rad) / cos(rad);
}

double sqrt(double val) {
  double x = val/10;
  double dx;
  double diff;
  double min_tol = 0.00001;
  int i, flag;
  flag = 0;
  if( val == 0 ) {
    x = 0;
  }
  else {
    for(i=1; i<20; i++) {
      if( !flag ) {
 dx = (val - (x*x)) / (2.0 * x);
 x = x + dx;
 diff = val - (x*x);
 if( fabs(diff) <= min_tol ) flag = 1;
      }
      else
 x =x;
    }
  }
  return x;
}

void putc(char c) {

}

void puts(const char *s) {
  while( *s ) {
    putc(*s++);
  }
}

void puti(int x) {
  int i = 0, j = 0;
  char buf[12];
  if( x < 0 ) {
    x = -x;
    buf[i++] = '-';
  }
  buf[i] = '0' + ((x / 1000000000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 100000000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 10000000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 1000000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 100000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 10000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 1000) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 100) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x / 10) % 10); if( j || buf[i] != '0' ) j=i+=1;
  buf[i] = '0' + ((x ) % 10); j= i + 1;
  buf[j] = 0;
  puts(buf);
}

void putf(double x) {
  int dec = (int)x;
  int fra = abs((int)((x - dec) * 1000000));
  puti(dec);
  putc('.');
  puti(fra);
}
# 2 "POWERSTONE_fir_det_true-unreach-call.c" 2

float fir_filter(float input, float *coef, int n, float *history);
static float gaussian(void);

float fir_lpf35[35] = {
  -6.3600959e-03, -7.6626200e-05, 7.6912856e-03, 5.0564148e-03, -8.3598122e-03,
  -1.0400905e-02, 8.6960020e-03, 2.0170502e-02, -2.7560785e-03, -3.0034777e-02,
  -8.9075034e-03, 4.1715767e-02, 3.4108155e-02, -5.0732918e-02, -8.6097546e-02,
  5.7914939e-02, 3.1170085e-01, 4.4029310e-01, 3.1170085e-01, 5.7914939e-02,
  -8.6097546e-02, -5.0732918e-02, 3.4108155e-02, 4.1715767e-02, -8.9075034e-03,
  -3.0034777e-02, -2.7560785e-03, 2.0170502e-02, 8.6960020e-03, -1.0400905e-02,
  -8.3598122e-03, 5.0564148e-03, 7.6912856e-03, -7.6626200e-05, -6.3600959e-03
};
float fir_lpf37[37] = {
  -6.51000e-04, -3.69500e-03, -6.28000e-04, 6.25500e-03, 4.06300e-03,
  -8.18900e-03, -1.01860e-02, 7.84700e-03, 1.89680e-02, -3.05100e-03,
  -2.96620e-02, -9.06500e-03, 4.08590e-02, 3.34840e-02, -5.07550e-02,
  -8.61070e-02, 5.75690e-02, 3.11305e-01, 4.40000e-01, 3.11305e-01,
  5.75690e-02, -8.61070e-02, -5.07550e-02, 3.34840e-02, 4.08590e-02,
  -9.06500e-03, -2.96620e-02, -3.05100e-03, 1.89680e-02, 7.84700e-03,
  -1.01860e-02, -8.18900e-03, 4.06300e-03, 6.25500e-03, -6.28000e-04,
  -3.69500e-03, -6.51000e-04
};

int Cnt1, Cnt2, Cnt3, Cnt4;

static float log(r)
     float r;
{
  return 4.5;
}

float fir_filter(float input, float *coef, int n, float *history)
{
  int i;
  float *hist_ptr, *hist1_ptr, *coef_ptr;
  float output;
  hist_ptr = history;
  hist1_ptr = hist_ptr;
  coef_ptr = coef + n - 1;
  output = *hist_ptr++ * (*coef_ptr--);
  for (i = 2; i < n; i++)
    {
      *hist1_ptr++ = *hist_ptr;
      output += (*hist_ptr++) * (*coef_ptr--);
    }
  output += input * (*coef_ptr);
  *hist1_ptr = input;
  return (output);
}
static float gaussian()
{
  static int ready = 0;
  static float gstore;
  static float rconst1 = (float) (2.0 / 32768);
  static float rconst2 = (float) (32768 / 2.0);
  float v1, v2, r, fac;
  float gaus;
  if (ready == 0)
    {
      v1 = (float) rand() - rconst2;
      v2 = (float) rand() - rconst2;
      v1 *= rconst1;
      v2 *= rconst1;
      r = v1 * v1 + v2 * v2;
      while (r > 1.0f)
 {
   v1 = (float) rand() - rconst2;
   v2 = (float) rand() - rconst2;
   v1 *= rconst1;
   v2 *= rconst1;
   r = v1 * v1 + v2 * v2;
 }
      fac = sqrt(-2.0f * 0.1);
      gstore = v1 * fac;
      gaus = v2 * fac;
      ready = 1;
    }
  else
    {
      ready = 0;
      gaus = gstore;
    }
  return (gaus);
}

float sigma = 0.2;

void main()
{
  int i, j;
  float x;
  static float hist[34];

  for (i = 0; i < 10; i++)
    {
      x = sin(0.05 * 2 * 3.14159265358979323846 * i) + sigma * gaussian();
      x *= 25000.0;
      fir_filter(x, fir_lpf35, 35, hist);
    }
  for (i = 0; i < 10; i++)
    {
      x = sin(0.05 * 2 * 3.14159265358979323846 * i) + sigma * gaussian();
      x *= 25000.0;
    }

 if (x < 8180 || x > 8196)
    {
      puts("fir: failed\n");
    }
  else
    {
      puts("fir: success\n");
    }
}

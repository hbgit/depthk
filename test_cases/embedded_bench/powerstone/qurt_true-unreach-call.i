# 1 "qurt_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "qurt_true-unreach-call.c"
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
# 2 "qurt_true-unreach-call.c" 2
int qurt(double a[], double x1[], double x2[])
{
  double d, w1, w2;
  if(a[0] == 0.0)
    return (999);
  d = a[1] * a[1] - 4 * a[0] * a[2];
  w1 = 2.0 * a[0];
  w2 = sqrt(fabs(d));
  if(d > 0.0)
    {
      x1[0] = (-a[1] + w2) / w1;
      x1[1] = 0.0;
      x2[0] = (-a[1] - w2) / w1;
      x2[1] = 0.0;
      return (0);
    }
  else if(d == 0.0)
    {
      x1[0] = -a[1] / w1;
      x1[1] = 0.0;
      x2[0] = x1[0];
      x2[1] = 0.0;
      return (0);
    }
  else
    {
      w2 /= w1;
      x1[0] = -a[1] / w1;
      x1[1] = w2;
      x2[0] = x1[0];
      x2[1] = -w2;
      return (0);
    }
}
int main()
{
  double a[3], x1[2], x2[2];
  int result;
  a[0] = 1.75;
  a[1] = -3.2;
  a[2] = 2.45;
  qurt(a, x1, x2);
  result = *(int *) &x1[0];
  a[0] = 1.5;
  a[1] = -2.5;
  a[2] = 1.5;
  qurt(a, x1, x2);
  result += *(int *) &x1[1];
  a[0] = 1.8;
  a[1] = -4.275;
  a[2] = 8.31;
  qurt(a, x1, x2);
  result -= *(int *) &x1[1];
  if(result != -1776094907)
    {
      puts("qurt: fail\n");
    }
  else
    {
      puts("qurt: success\n");
    }
  return 0;
}

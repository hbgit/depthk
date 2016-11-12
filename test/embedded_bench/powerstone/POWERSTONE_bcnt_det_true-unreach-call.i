# 1 "POWERSTONE_bcnt_det_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "POWERSTONE_bcnt_det_true-unreach-call.c"
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
# 2 "POWERSTONE_bcnt_det_true-unreach-call.c" 2
unsigned char poptab[256] =
{
  0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
  1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
  1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
  1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
  2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
  3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
  3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
  4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8,
};
unsigned long src[1024] =
{
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x12345678, 0x12345678, 0x12345678, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x12345678, 0x12345678, 0x12345678, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x12345678, 0x12345678, 0x12345678, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x12345678, 0x12345678, 0x12345678, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x12345678, 0x12345678, 0x12345678, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x10101010, 0x12345678, 0x10101010, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x10101010, 0x12345678, 0x10101010, 0x12345678,
  0x00005678, 0x12340000, 0x02040608, 0x00000001,
  0x10101010, 0x12345678, 0x10101010, 0x12345678,
  0
};
unsigned long dst[1024];
main()
{
  unsigned long *s, *d;
  unsigned long x;
  int k, t = 0;
  for (s = src, d = dst, t = 0; *s; s += 4, d += 4)
    {
      x = s[0] ^ d[0];
      k = poptab[x & 0xff];
      k += poptab[(x >> 8) & 0xff];
      k += poptab[(x >> 16) & 0xff];
      k += poptab[x >> 24];
      t += k;
      x = s[1] ^ d[1];
      k = poptab[x & 0xff];
      k += poptab[(x >> 8) & 0xff];
      k += poptab[(x >> 16) & 0xff];
      k += poptab[x >> 24];
      t += k;
      x = s[2] ^ d[2];
      k = poptab[x & 0xff];
      k += poptab[(x >> 8) & 0xff];
      k += poptab[(x >> 16) & 0xff];
      k += poptab[x >> 24];
      t += k;
      x = s[3] ^ d[3];
      k = poptab[x & 0xff];
      k += poptab[(x >> 8) & 0xff];
      k += poptab[(x >> 16) & 0xff];
      k += poptab[x >> 24];
      t += k;
    }
  if (t != 0x202)
    {
      puts("bcnt: failed\n");
    }
  else
    {
      puts("bcnt: success\n");
    }
  return 0;
}

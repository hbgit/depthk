# 1 "SNU_lms_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_lms_nondet_true-unreach-call.c"
# 53 "SNU_lms_nondet_true-unreach-call.c"
static float gaussian(void);





float mu = 0.01;


int rand()
{
  static unsigned long next = 1;

  next = next * 1103515245 + 12345;
  return (unsigned int)(next/65536) % 32768;
}

static float log(float r)

{
  return 4.5;
}

static float fabs(float n)
{
  float f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}

static float sqrt(float val)

{
  float x = val/10;

  float dx;

  double diff;
  double min_tol = 0.00001;

  int i, flag;

  flag = 0;
  if (val == 0 ) x = 0;
  else {
    for (i=1;i<20;i++)
      {
 if (!flag) {
   dx = (val - (x*x)) / (2.0 * x);
   x = x + dx;
   diff = val - (x*x);
   if (fabs(diff) <= min_tol) flag = 1;
 }
 else
   x =x;
      }
  }
  return (x);
}


static float sin(float rad)

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

static float gaussian()
{
    static int ready = 0;
    static float gstore;
    static float rconst1 = (float)(2.0/32768);
    static float rconst2 = (float)(32768/2.0);
    float v1,v2,r,fac;
    float gaus;


    if(ready == 0) {
            v1 = (float)rand() - rconst2;
            v2 = (float)rand() - rconst2;
            v1 *= rconst1;
            v2 *= rconst1;
            r = v1*v1 + v2*v2;
        while (r > 1.0f) {
            v1 = (float)rand() - rconst2;
            v2 = (float)rand() - rconst2;
            v1 *= rconst1;
            v2 *= rconst1;
            r = v1*v1 + v2*v2;
        }


        fac = sqrt(-2.0f*log(r)/r);
        gstore = v1*fac;
        gaus = v2*fac;
        ready = 1;
    }

    else {
        ready = 0;
        gaus = gstore;
    }

    return(gaus);
}

float nondet_float();

void main()
{
    float lms(float,float,float *,int,float,float);
    static float d[201],b[21];
    float signal_amp,noise_amp,arg,x,y;
    int k;


    for(k = 0 ; k < 201 ; k++) {
        d[k] = nondet_float();
    }


    mu = 2.0*mu/(20 +1);

    x = 0.0;
    for(k = 0 ; k < 201 ; k++) {
        lms(x,d[k],b,20,mu,0.01);

        x = d[k];
    }
}
# 223 "SNU_lms_nondet_true-unreach-call.c"
float lms(float x,float d,float *b,int l,
                  float mu,float alpha)
{
    int ll;
    float e,mu_e,lms_const,y;
    static float px[51];
    static float sigma = 2.0;

    px[0]=x;


    y=b[0]*px[0];



    for(ll = 1 ; ll <= l ; ll++)
        y=y+b[ll]*px[ll];


    e=d-y;


    sigma=alpha*(px[0]*px[0])+(1-alpha)*sigma;
    mu_e=mu*e/sigma;


    for(ll = 0 ; ll <= l ; ll++)
        b[ll]=b[ll]+mu_e*px[ll];

    for(ll = l ; ll >= 1 ; ll--)
        px[ll]=px[ll-1];

    return(y);
}

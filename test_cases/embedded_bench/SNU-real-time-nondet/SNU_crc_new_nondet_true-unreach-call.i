# 1 "SNU_crc_new_nondet_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "SNU_crc_new_nondet_true-unreach-call.c"
# 49 "SNU_crc_new_nondet_true-unreach-call.c"
typedef unsigned char uchar;





unsigned short icrc1(unsigned short crc, unsigned char onech)
{
 int i;
 unsigned short ans=(crc^onech << 8);

 for (i=0;i<8;i++) {
  if (ans & 0x8000)
   ans = (ans <<= 1) ^ 4129;
  else
   ans <<= 1;
 }
 return ans;
}

unsigned short icrc(unsigned short crc, unsigned long len,
      short jinit, int jrev)
{
 unsigned short icrc1(unsigned short crc, unsigned char onech);
 static unsigned short icrctb[256],init=0;
 static uchar rchr[256];
 unsigned short tmp1, tmp2, j,cword=crc;
 static uchar it[16]={0,8,4,12,2,10,6,14,1,9,5,13,3,11,7,15};

 if (!init) {
  init=1;
  for (j=0;j<=255;j++) {
   icrctb[j]=icrc1(j << 8,(uchar)0);
   rchr[j]=(uchar)(it[j & 0xF] << 4 | it[j >> 4]);
  }
 }
 if (jinit >= 0) cword=((uchar) jinit) | (((uchar) jinit) << 8);
 else if (jrev < 0)
   cword=rchr[((uchar)((cword) >> 8))] | rchr[((uchar)((cword) & 0xFF))] << 8;



 for (j=1;j<=len;j++) {
   char data;
   if (jrev < 0) {
     tmp1 = rchr[data]^ ((uchar)((cword) >> 8));
   }
   else {
     tmp1 = data^ ((uchar)((cword) >> 8));
   }
   cword = icrctb[tmp1] ^ ((uchar)((cword) & 0xFF)) << 8;
 }
 if (jrev >= 0) {
   tmp2 = cword;
 }
 else {
   tmp2 = rchr[((uchar)((cword) >> 8))] | rchr[((uchar)((cword) & 0xFF))] << 8;
 }
 return (tmp2 );
}


int main(void)
{

 unsigned short i1,i2;
 unsigned long n;

 n=40;

  i1=icrc(0,n,(short)0,1);



 return 0;
}

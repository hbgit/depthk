int nondet_int();//__NONDET__();

  char x[100], y[100];
  int i,j,k;

void main() {  
  k = nondet_int(); //__NONDET__();
  
  i = 0;
  while(x[i] != 0){
    y[i] = x[i];
    i++;
  }
  y[i] = 0;
  
  if(k >= 0 && k < i)
    if(y[k] == 0)
      {ERROR: goto ERROR;}
}

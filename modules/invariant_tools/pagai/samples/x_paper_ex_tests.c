
void main(){
    //teste
    int x = 0 ;
    int t = 0 ;
    /**
    * Test comment
    */
    int phase = 0 ;

    while ( t < 100) {
        if( phase == 0 )
            x = x + 2;
        if( phase == 1 )
            x = x - 1;
        phase = 1 - phase;
        t++;
    }

    assert( x <= 100 ); 
}

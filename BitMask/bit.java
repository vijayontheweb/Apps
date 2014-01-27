

class Bit {
    public static void p( int n) {
	int j= 0x80000000;
	for(int i=0; i<32; i++){
	    if((n & j) == j){
		System.out.print("1");
	    }else{
		System.out.print("0");
	    }
	    n<<=1;
	}
	System.out.println("");
    }

    public static int toggle(int n, int s, int e){
	int mask1=0x80000000;
	int mask2=0xffffffff;
        mask1 >>= (32 - e);
        mask2 >>>= (32 - s);
	return (n | (mask1 & mask2));
    }
	
	
    
    public static void main(String args[]){
	int i = toggle(1,15,10);
	p(i);
	System.out.println("" + sizeof(int));

    }
}
	

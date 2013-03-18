
public class SalesTaxBehavior implements TaxBehavior{
	Product product;
	private static final double BASIC_SALES_TAX_PCNT = 10;
	private static final double IMPORT_DUTY_PCNT = 5;
	

	//TODO - To Use Decorator Pattern to handle this code	
	public void computeTax(Product product){
		this.product = product;		
		if(!product.isTaxExempt()){
			product.tax = product.tax + product.cost*BASIC_SALES_TAX_PCNT/100;			
		}
		if(product.isImported()){
			product.tax = product.tax + product.cost*IMPORT_DUTY_PCNT/100;			
		}
		product.tax = Round(product.tax,2);
	}
	
	private double Round(double Rval, int Rpl) {
		double p = (double)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  double tmp = Math.round(Rval);
		  return (double)tmp/p;
    }
	
	public void printReceipt(){
		if(product.isImported())System.out.print("[IMPORTED]");
		System.out.println("PRODUCT NAME:"+product.getProductName());		
		System.out.println("PRODUCT CATEGORY:"+ExemptBehavior.getDescription(product));
		System.out.println("PRODUCT COST:"+product.getCost());
		System.out.println("PRODUCT TAX:"+product.getTax());
		System.out.println("PRODUCT COST AFTER TAX:"+(product.getCost()+product.getTax()));
		System.out.println("---------------------------------------------------");
	}
	
}

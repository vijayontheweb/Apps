import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingCart{
	public List cart = new ArrayList();
	private TaxBehavior taxBehavior;
	private double totalTax = 0;
	private double total = 0;
	
	public void addProduct(Product product){
		cart.add(product);
	}
	
	public void processCart(){
		Iterator iterator = cart.iterator();
		while(iterator.hasNext()){
			Product product = (Product)iterator.next();
			product.setTaxBehavior(taxBehavior);
			product.processTax();			
			totalTax = totalTax + product.getTax();
			total = total + product.getCost() + product.getTax();
		}
		System.out.println("---------------------------------------------------");
		System.out.println("SALES TAX = "+Round(totalTax,2));
		System.out.println("TOTAL = "+Round(total,2));
	}
	
	private double Round(double Rval, int Rpl) {
		double p = (double)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  double tmp = Math.round(Rval);
		  return (double)tmp/p;
    }

	protected TaxBehavior getTaxBehavior() {
		return taxBehavior;
	}

	protected void setTaxBehavior(TaxBehavior taxBehavior) {
		this.taxBehavior = taxBehavior;
	}
	
}
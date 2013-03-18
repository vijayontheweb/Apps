public class Product{
	private ExemptBehavior exemptBehavior;
	private TaxBehavior taxBehavior;
	private String productName;
	private String productCategory;
	protected double cost;
	protected double tax;	
	private boolean taxExempt = false;
	private boolean imported = false;	
	
	public Product(String name, String category, double cost, boolean imported){
		setProductName(name);
		setProductCategory(category);
		setCost(cost);
		setImported(imported);		
		setTaxExempt(ExemptBehavior.find(category));
	}
	
	public void processTax(){
		taxBehavior.computeTax(this);
		taxBehavior.printReceipt();
	}
	
	protected void setImported(boolean imported) {
		this.imported = imported;
	}
	protected String getProductName() {
		return productName;
	}
	protected boolean isImported() {
		return imported;
	}
	protected boolean isTaxExempt() {
		return taxExempt;
	}
	protected void setProductName(String productName) {
		this.productName = productName;
	}
		
	protected void setTaxExempt(boolean taxExempt) {
		this.taxExempt = taxExempt;
	}	
	
	protected double getCost() {
		return cost;
	}
	
	protected void setCost(double cost) {
		this.cost = cost;
	}
	protected String getProductCategory() {
		return productCategory;
	}
	protected void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	

	protected ExemptBehavior getExemptBehavior() {
		return exemptBehavior;
	}

	protected void setExemptBehavior(ExemptBehavior exemptBehavior) {
		this.exemptBehavior = exemptBehavior;
	}

	protected double getTax() {
		return tax;
	}

	protected void setTax(double tax) {
		this.tax = tax;
	}

	protected TaxBehavior getTaxBehavior() {
		return taxBehavior;
	}

	protected void setTaxBehavior(TaxBehavior taxBehavior) {
		this.taxBehavior = taxBehavior;
	}	
}

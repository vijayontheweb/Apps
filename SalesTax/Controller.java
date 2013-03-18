public class Controller{
	public static void main(String args[]){
		ShoppingCart shoppingCart = new ShoppingCart();
		/* INPUT 1
		Product book = new Product("harryPorter","BK", 12.49, false);
		Product musicCD = new Product("bryan","OT", 14.99, false);
		Product chocolate = new Product("carburys","FD", 0.85, false);		
		shoppingCart.addProduct(book);
		shoppingCart.addProduct(musicCD);		
		shoppingCart.addProduct(chocolate);
		End of INPUT 1	*/
		
		/* INPUT 2
		Product import_chocolate = new Product("kitkat","FD", 10, true);
		Product import_perfume = new Product("axe","OT", 47.5, true);
		shoppingCart.addProduct(import_chocolate);
		shoppingCart.addProduct(import_perfume);		
		End of INPUT 2	*/
				
		/* INPUT 3
		Product import_perfume_1 = new Product("nivea","OT", 27.99, true);
		Product import_perfume_2 = new Product("rexona","OT", 18.99, false);
		Product pills = new Product("saridon","MD", 9.75, false);
		Product import_chocolate_1 = new Product("dairymilk","FD", 11.25, true);
		shoppingCart.addProduct(import_perfume_1);
		shoppingCart.addProduct(import_perfume_2);
		shoppingCart.addProduct(pills);
		shoppingCart.addProduct(import_chocolate_1);		
		End of INPUT 3	*/
		
		shoppingCart.setTaxBehavior(new SalesTaxBehavior());
		shoppingCart.processCart();
	}
}

/*NOTE: Setting Product Level Tax Behavior is also possible
 	harryPorter.setTaxBehavior(new SalesTaxBehavior());	
 	harryPorter.processTax();
*/

public class ExemptBehavior {
	
	static final String exemptCategories[] = {"BK","FD","MD"};
	static final String description[] = {"BOOK","FOOD","MEDICINE"};
	
	
	public static boolean find(String productCategory){
		for(int i=0; i < exemptCategories.length ; i++){
			if(exemptCategories[i].equals(productCategory))
				return true;
		}
		return false;
	}
	
	public static String getDescription(Product product){
		for(int i=0; i < exemptCategories.length ; i++){
			if(exemptCategories[i].equals(product.getProductCategory()))
				return description[i];
		}
		return "OTHER";
	}
	
}

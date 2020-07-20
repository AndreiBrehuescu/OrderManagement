package validators;

import model.Product;

public class ProductNameValidator implements Validator<Product>{

	@Override
	public void validate(Product t) {
		if( !t.getProductName().matches("^[a-zA-Z ]+$") ) {
			throw new IllegalArgumentException("The produc name is not a valid name!");
		}
		
	}
	
	

}

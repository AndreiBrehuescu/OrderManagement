package validators;

import model.Customer;

public class ClientNameValidator implements Validator<Customer>{

	
	public void validate(Customer t) {
		if( !t.getCustomerName().matches("^[a-zA-Z ]+$") ) {
			throw new IllegalArgumentException("The name is not a valid name");
		}
	}
	

}

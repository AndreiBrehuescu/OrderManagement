package validators;

import model.OrderProducts;

public class QuantityOrderValidator implements Validator<OrderProducts>{

	@Override
	public void validate(OrderProducts t) {
		if( t.getQuantity() < 0 ) {
			throw new IllegalArgumentException("Quantity can't be a negative number");
		}
		
	}

}

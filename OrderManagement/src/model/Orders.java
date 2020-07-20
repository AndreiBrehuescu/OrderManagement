package model;

public class Orders {
	private int idOrder;
	private int idCustomer;
	private double total;
	
	public Orders(int idOrder, int idCustomer, double total) {
		super();
		this.idOrder = idOrder;
		this.idCustomer = idCustomer;
		this.total = total;
	}

	public Orders() {
		super();
	}

	/**
	 * Se obtine suma totala a comenzii
	 * @return total - totalul de plata al comenzii
	 */
	public double getTotal() {
		return total;
	}
	
	/**
	 * Se seteaza suma totala a comenzii
	 * @param total - totalul ce se va seta comenzii
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	
	/**
	 * Se obtine idul comenzii
	 * @return id - idul comenzii
	 */
	public int getIdOrder() {
		return idOrder;
	}
	
	/**
	 * Se seteaza idul comenzii
	 * @param idOrder - idul ce va fi setat comenzii
	 */
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	/**
	 * Se obtine idul clientului
	 * @return id - idul clientului 
	 */
	public int getIdCustomer() {
		return idCustomer;
	}

	/**
	 * Se seteaza idul clientului
	 * @param idCustomer - idul ce va fi setat clientului
	 */
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

}

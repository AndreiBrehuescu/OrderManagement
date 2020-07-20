package model;

public class OrderProducts {
	
	private int idOrderProducts;
	private int idProduct;
	private int idOrder;
	private int quantity;
	private double total;
	
	public OrderProducts(int idOrderProducts, int idProduct, int idOrder, int quantity, double total) {
		super();
		this.idOrderProducts = idOrderProducts;
		this.idProduct = idProduct;
		this.idOrder = idOrder;
		this.quantity = quantity;
		this.total = total;
	}

	public OrderProducts() {
		super();
	}

	/**
	 * Se obtine cantitatea disponibila a produsului
	 * @return quantity - cantitatea disponibila
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Se seteaza cantitatea produsului
	 * @param quantity - cantitatea ce va fi setata produsului
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Se obtine costul total al produsului ( cantitate*pret)
	 * @return total - costul total pentru toate produsele de acelasi tip
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * Se seteaza costul total al produsului
	 * @param total - totalul ce este setat pentru un anumit produs
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * Se obtine idul produsului
	 * @return id - idul produsului
	 */
	public int getIdOrderProducts() {
		return idOrderProducts;
	}

	/**
	 * Se seteaza idul produsului
	 * @param idOrderProducts - idul ce va fi setat produsului
	 */
	public void setIdOrderProducts(int idOrderProducts) {
		this.idOrderProducts = idOrderProducts;
	}

	/**
	 * Se obtine idul produslui
	 * @return id - idul produsului
	 */
	public int getIdProduct() {
		return idProduct;
	}

	/**
	 * Se seteaza idul produsului
	 * @param idProduct - idul ce va fi setat produsului
	 */
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
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
	
}

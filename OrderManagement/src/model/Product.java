package model;

public class Product {
	private int idProduct;
	private String productName;
	private int quantity;
	private double price;

	public Product(int idProduct, String name, int quantity, double price) {
		super();
		this.idProduct = idProduct;
		this.productName = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public Product() {
		
	}

	/**
	 * Se obtine numele produsului
	 * @return productName - numele produsului
	 */
	public String getProductName() {
		return productName;
	}
	
	/**
	 * Se seteaza numele produsului
	 * @param productName - numele ce va fi setat produsului
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Se obtine idul produsului
	 * @return idProduct - idul produsului
	 */
	public int getIdProduct() {
		return idProduct;
	}

	/**
	 * Se seteaza idul produsului
	 * @param idProduct - id-ul ce va fi setat produsului
	 */
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	/**
	 * Se obtine pretul pe unitate al produsului
	 * @return price - pretului produsului pe unitate
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Se seteaza pretul pe unitate al produsului
	 * @param price - pretul ce va fi setat produsului
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Se obtine cantitatea disponibila pentru un produs
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

}

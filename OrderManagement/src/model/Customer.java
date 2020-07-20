package model;

public class Customer {
	private int idCustomer;
	private String customerName;
	private String address;

	public Customer(int idClient, String name, String address) {
		super();
		this.idCustomer = idClient;
		this.customerName = name;
		this.address = address;
	}
	
	public Customer() {
	
	}
	
	/**
	 * Se obtine numele clientului
	 * @return customerName - numele clientului
	 */
	public String getCustomerName() {
		return customerName;
	}
	
	/**
	 * Se seteaza numele clientului
	 * @param customerName - numele 
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * Se obtine idul clientului
	 * @return idClient - idul clientului
	 */
	public int getIdCustomer() {
		return idCustomer;
	}

	/**
	 * Se seteaza idul clientului
	 * @param idClient - idul ce va fi setat 
	 */
	public void setIdCustomer(int idClient) {
		this.idCustomer = idClient;
	}
	
	/**
	 * Se obtine adresa cumparatorului
	 * @return address -
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Se seteaza adresa clientului
	 * @param address - adresa ce va fi setata clientului
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}

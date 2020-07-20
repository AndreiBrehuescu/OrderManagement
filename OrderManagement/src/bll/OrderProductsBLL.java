package bll;

import java.util.*;

import dao.OrderProductsDAO;
import validators.Validator;
import model.OrderProducts;
import validators.QuantityOrderValidator;

public class OrderProductsBLL {
	
	private List<Validator<OrderProducts>> validators;
	private OrderProductsDAO orderProductsDAO = new OrderProductsDAO();
	
	public OrderProductsBLL() {
		this.validators = new ArrayList<Validator<OrderProducts>>();
		this.validators.add(new QuantityOrderValidator());
	}
	
	/**
	 * Functia executa cautarea in baza de date pe baza unui id 
	 * @param id - id corespunzator campului ce trebuie cautat
	 * @return T - o instanta de tipul OrderProduct in caz de succes  sau se arunca o exceptie in caz ca nu s-au gasit date
	 */
	public OrderProducts findById(int id) throws NoSuchElementException{
		OrderProducts st = this.orderProductsDAO.findByID(id);
		if( st == null ) {
			throw new NoSuchElementException("The OrderProducts with id = " + id +" was not found!");
		}
		
		return st;
	}
	
	/**
	 * Se creaza o lista cu clientii existenti in baza de date
	 * Metoda poate arunca o exceptie in cazul in care nu exista produse comandate 
	 * @return o lista ce contine produsele comandate, se arunca o exceptie in caz ca nu exista inregistrari
	 */
	public ArrayList<OrderProducts> findAll() throws NoSuchElementException{
		ArrayList<OrderProducts> orders = this.orderProductsDAO.findAll();
		
		if( orders == null ) {
			throw new NoSuchElementException("There are no orderProducst! Empty list!");
		}
		
		return orders;
	}
	
	/**
	 * Functia executa inserarea in baza de date 
	 * @param orderProduct - obiectul ce trebuie inserat in baza de date
	 * @return id - id-ul la care a fost inserat in caz de succes, -1 in caz de erroare
	 */
	public int insert(OrderProducts orderProduct) {
		return this.orderProductsDAO.insert(orderProduct);
	}
	
	/**
	 * Functia executa update-ul in baza de date pe baza unui id si orderProducts cu noile date
	 * @param id - id-ul corespunzator campului ce trebuie actualizat
	 * @param orderProducts obiectul ce contine date actualizate
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean update(OrderProducts orderProducts, int id) {
		return this.orderProductsDAO.update(orderProducts, id);
	}
	
	/**
	 * Functia executa stergerea din baza de date pe baza unui id
	 * @param id - id-ul corespunzator campului ce trebuie sters
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean delete(int id) {
		return this.orderProductsDAO.delete(id);
	}
	
	/**
	 * Verifica daca clientul respecta conditiile impuse de validatori
	 * @param orderProducts o instanta a comenzii de produs ce trebuie validat
	 * @throws Exception Se arunca o exceptie in cazul in care datele nu sunt valide
	 */
	public void validate(OrderProducts orderProducts) throws Exception {
		for (Validator<OrderProducts> validator : this.validators) {
			validator.validate(orderProducts);
		}
	}

}

package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import dao.OrdersDAO;
import model.Orders;
import validators.Validator;

public class OrdersBLL {
	
	private OrdersDAO orderDAO = new OrdersDAO();
	
	public OrdersBLL() {

	}
	
	/**
	 * Functia executa cautarea comenzii in baza de date pe baza unui id 
	 * @param id - id-ul corespunzator campului ce trebuie cautat
	 * @return T - o instanta de tipul Orders in caz de succes sau se arunca o exceptie in caz ca nu s-au gasit date
	 */
	public Orders findOrderById(int id) throws NoSuchElementException{
		Orders st = this.orderDAO.findByID(id);
		
		if( st == null ) {
			throw new NoSuchElementException("The Order with id = " + id +" was not found!");
		}
		
		return st;
	}
	
	/**
	 * Se creaza o lista cu comenzile existente in baza de date
	 * Metoda poate arunca o exceptie in cazul in care nu exista produse comandate 
	 * @return o lista ce contine comenzile create sau se arunca o exceptie in caz ca nu exista inregistrari
	 */
	public ArrayList<Orders> findAll() throws NoSuchElementException{
		ArrayList<Orders> orders = this.orderDAO.findAll();
		
		if( orders == null ) {
			throw new NoSuchElementException("There are no order! Empty list!");
		}
		
		return orders;
	}
	
	/**
	 * Functia executa inserarea in baza de date 
	 * @param order - obiectul ce trebuie inserat in baza de date
	 * @return id - id la care a fost inserat in caz de succes, -1 in caz de erroare
	 */
	public int insert(Orders order) {
		return this.orderDAO.insert(order);
	}
	
	/**
	 * Functia executa update-ul in baza de date pe baza unui id si comanda cu noile date
	 * @param id - id-ul corespunzator campului ce trebuie actualizat
	 * @param order - obiectul ce contine datele actualizate
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean update(Orders order, int id) {
		return this.orderDAO.update(order, id);
	}
	
	/**
	 * Functia executa stergerea din baza de date pe baza unui id
	 * @param id - id-ul corespunzator campului ce trebuie sters
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean delete(int id) {
		return this.orderDAO.delete(id);
	}

}

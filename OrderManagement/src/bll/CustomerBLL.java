package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import dao.CustomerDAO;
import model.Customer;
import validators.ClientNameValidator;
import validators.Validator;

public class CustomerBLL {
	
	private List<Validator<Customer>> validators;
	private CustomerDAO CustomerDAO = new CustomerDAO();
	
	public CustomerBLL() {
		this.validators = new ArrayList<Validator<Customer>>();
		this.validators.add(new ClientNameValidator());

	}
	
	/**
	 * Se cauta un client in baza de date dupa un id specificat
	 * In interiorul metodei se apeleaza metoda de cautare din DAO
	 * @param id id-ul ce trebuie cauta
	 * @return instanta a clasei Class Customer sau null daca nu a fost gasit clientul
	 */
	public Customer findClientById(int id) throws NoSuchElementException{
		Customer st = this.CustomerDAO.findByID(id);
		
		if( st == null ) {
			throw new NoSuchElementException("The customer with id = " + id +" was not found!");
		}
		
		return st;
	}
	
	/**
	 * Se cauta un client in baza de date dupa un nume specificat
	 * In interiorul metodei se apeleaza metoda de cautare din DAO
	 * @param name numele ce trebuie cauta
	 * @return instanta a clasei Class Customer sau null daca nu a fost gasit clientul
	 */
	
	/**
	 * Se cauta un client in baza de date dupa un nume specificat
	 * In interiorul metodei se apeleaza metoda de cautare din DAO 
	 * @param name numele ce trebuie cauta
	 * @return instanta a clasei Class Customer
	 * @throws NoSuchElementException se arunca o exceptie in caz ca nu exista clientul
	 */
	public Customer findByName(String name) throws NoSuchElementException{
		Customer st = this.CustomerDAO.findByName(name);
		if( st == null ) {
			throw new NoSuchElementException("The customer with name = " + name +" was not found!");
		}
		
		return st;
	}
	
	/**
	 * Se creaza o lista cu clientii existenti in baza de date
	 * Metoda poate arunca o exceptie in cazul in care nu exista clienti 
	 * @return o lista ce contine clientii existenti 
	 */
	public ArrayList<Customer> findAll() throws NoSuchElementException{
		ArrayList<Customer> clients = this.CustomerDAO.findAll();
		
		if( clients == null ) {
			throw new NoSuchElementException("There are no customers! Empty list!");
		}
		
		return clients;
	}
	
	/**
	 * Functia executa inserarea in baza de date a unui client
	 * @param client Clientru cu datele acestuia ce trebuie inserate in baza de date
	 * @return int reprezentand id-ul clientului in caz de succes, -1 in caz de eroare
	 */
	public int insert(Customer client) {
		return this.CustomerDAO.insert(client);
	}
	
	/**
	 * Functia executa update-ul in baza de date pe baza unui id si clientul cu noile date
	 * @param client Clientul cu noile date ce trebuie actualizate in baza de date
	 * @param id id-ul corespunzator campului ce trebuie actualizat
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean update(Customer client,int id) {
		return this.CustomerDAO.update(client, id);
	}
	
	/**
	 * Functia executa stergerea din baza de date pe baza unui id
	 * @param id id-ul corespunzator campului ce trebuie sters
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean delete(int id) {
		return this.CustomerDAO.delete(id);
	}
	
	/**
	 * Verifica daca clientul respecta conditiile impuse de validatori
	 * @param client o instanta a clientului ce trebuie validat
	 * @throws Exception arunca o exceptie in caz ca clientul nu are date valide
	 */
	public void validate(Customer client) throws Exception{
		for (Validator<Customer> validator : this.validators) {
			validator.validate(client);
		}
		return ;
	}
	
	
}

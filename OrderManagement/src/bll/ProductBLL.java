package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import dao.ProductDAO;
import model.Product;
import validators.ProductNameValidator;
import validators.QuantityValidator;
import validators.Validator;

public class ProductBLL {
	
	private List<Validator<Product>> validators;
	private ProductDAO productDAO = new ProductDAO();
	
	public ProductBLL() {
		this.validators = new ArrayList<Validator<Product>>();
		this.validators.add(new QuantityValidator());
		this.validators.add(new ProductNameValidator());
	}
	
	/**
	 * Functia executa cautarea produsului in baza de date pe baza unui id 
	 * @param id - id-ul corespunzator campului ce trebuie cautat
	 * @return T - o instanta de tipul Product in caz de succes sau se arunca o exceptie in caz ca nu s-au gasit date
	 */
	public Product findById(int id) throws NoSuchElementException{
		Product st = this.productDAO.findByID(id);
		if( st == null ) {
			throw new NoSuchElementException("The products with id = " + id +" was not found!");
		}
		
		return st;
	}
	
	/**
	 * Functia executa cautarea produsului in baza de date pe baza unui nume 
	 * @param name - nume corespunzator campului ce trebuie cautat
	 * @return T - o instanta de tipul Product in caz de succes sau null in caz ca nu au fost gasite date
	 */
	public Product findByName(String name) {
		Product st = this.productDAO.findByName(name);
		
		return st; //returns null if can't find the specified product
	}
	
	/**
	 * Se creaza o lista cu produsele existente in baza de date
	 * Metoda poate arunca o exceptie in cazul in care nu exista produse comandate 
	 * @return o lista ce contine produsele existene sau se arunca o exceptie in caz ca nu exista inregistrari
	 */
	public ArrayList<Product> findAll() throws NoSuchElementException{
		ArrayList<Product> products = this.productDAO.findAll();
		
		if( products == null ) {
			throw new NoSuchElementException("There are no products! Empty list!");
		}
		
		return products;
	}
	
	/**
	 * Functia executa inserarea in baza de date 
	 * @param product - obiectul ce trebuie inserat in baza de date
	 * @return id - id-ul la care a fost inserat in caz de succes, -1 in caz de erroare
	 */
	public int insert(Product product) {
		return this.productDAO.insert(product);
	}
	
	/**
	 * Functia executa stergerea din baza de date pe baza unui id
	 * @param id id-ul corespunzator campului ce trebuie sters
	 * @return in caz de succes true, false in caz contrar
	 */
	public boolean delete(int id) {
		return this.productDAO.delete(id);
	}
	
	/**
	 * Functia executa update-ul in baza de date pe baza unui id si produsul cu noile date
	 * @param product - obiectul ce contine datele actualizate
	 * @param id - id-ul corespunzator campului ce trebuie actualizat
	 * @return boolean in caz de succes true, false in caz contrar
	 */
	public boolean update(Product product, int id ) {
		return this.productDAO.update(product, id);
	}
	
	/**
	 * Se executa validarea produsului pe baza listei de validatori
	 * @param product produsul ce trebuie validat
	 * @throws Exception Se arunca o exceptie in cazul in care datele nu sunt valide
	 */
	public void validate(Product product) throws Exception{
		for (Validator<Product> validator : this.validators) {
			validator.validate(product);
		}
	}
	

}

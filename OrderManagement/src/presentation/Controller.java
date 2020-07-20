package presentation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import bll.*;
import model.*;

public class Controller {
	
	private CustomerBLL customer ;
	private ProductBLL product;
	private OrdersBLL order;
	private OrderProductsBLL orderProduct;
	private int[] id = {1,1,1,1,1,1,1};
	
	public Controller() {
		super();
		this.customer = new CustomerBLL();
		this.product = new ProductBLL();
		this.order = new OrdersBLL();
		this.orderProduct = new OrderProductsBLL();
	}

	/**
	 * Functie primeste o comanda si in functie de comanda se vor executa diferite operatii
	 * Pentru fiecare comanda se apeleaza o metoda corespunzatoare
	 * @param command - comanda ce trebuie executata
	 */
	public void sqlCommand(String[] command) {
		if( command[0].equalsIgnoreCase("Insert client") ) {
			insertCustomer(command);
		}else if ( command[0].equalsIgnoreCase("Insert product") ){
			insertProduct(command);
		}else if( command[0].equalsIgnoreCase("Delete client") ) {
			deleteCustomer(command);
		}else if( command[0].equalsIgnoreCase("Delete product") ) {
			deleteProduct(command);
		}else if( command[0].equalsIgnoreCase("Order") ) {
			orderProduct(command);
		}else if( command[0].contains("Report") ) {
			reportGenerate(command);
		}
	}
	
	/**
	 * Se realizeaza inserea clientului in baza de date
	 * @param customerInfo - comanda de inserare care contine numele si adresa clientului
	 */
	public void insertCustomer(String[] customerInfo) {
		Customer c = new Customer( 0, customerInfo[1], customerInfo[2]);
		try {
			this.customer.validate(c);
		} catch (Exception e) {
			System.out.println("Date invalide pentru clientul : " + customerInfo[1]);
		}
		this.customer.insert(c);
	}
	
	/**
	 * Se realizeaza inserarea produsului in baza de date
	 * @param productInfo - comanda de inserare ce contine si informatii despre numele, cantitatea si pretul produsului
	 */
	public void insertProduct(String[] productInfo) {
		Product existing = this.product.findByName(productInfo[1]);
		
		if( existing == null ) {
			Product p = new Product( 0, productInfo[1], Integer.parseInt(productInfo[2]),Double.parseDouble(productInfo[3]));
			try {
				this.product.validate(p);
			} catch (Exception e) {
				System.out.println("Date invalide pentru produsul + " + productInfo[1]);
			}
			this.product.insert(p);
		}else {
			existing.setQuantity(existing.getQuantity() + Integer.parseInt(productInfo[2]));
			this.product.update(existing, existing.getIdProduct());
		}
	}
	
	/**
	 * Se realizeaza stergerea clientului
	 * @param customerInfo - comanda de stergere ce contine numele si adresa clientului ce se doreste a fi sters
	 */
	public void deleteCustomer(String[] customerInfo) {
		Customer existingCustomer = this.customer.findByName(customerInfo[1]);
		if( existingCustomer == null ) {
			System.out.println("Nu poate fi sters un client neexistent!");
		}else {
			this.customer.delete(existingCustomer.getIdCustomer());
		}
	}
	
	/**
	 * Se realizeaza stergerea produsului
	 * @param productInfo - comanda de stergere ce contine numele produsului ce se doreste a fi sters
	 */
	public void deleteProduct(String[] productInfo) {
		Product existingProduct = this.product.findByName(productInfo[1]);
		if( existingProduct == null ) {
			System.out.println("Nu se poate sterge un produs neexistent!");
		}else {
			this.product.delete(existingProduct.getIdProduct());
		}
	}
	
	/**
	 * Se realizeaza crearea unei comenzi
	 * Se creaza atat in tabele de comenzi cat si in tabela de comandaProduse o inregistrare
	 * Fiecare comanda contine un client, un produs si o cantitate comandata
	 * @param orderInfo - comanda de creare a comenzii, contine numele clientului, al produsului si cantitatea comandata
	 */
	public void orderProduct(String[] orderInfo) {
		Customer existingCustomer = this.customer.findByName(orderInfo[1]);
		Product existingProduct = this.product.findByName(orderInfo[2]);
		
		if( existingCustomer == null || existingProduct == null ) {
			System.out.println("Error! Nu se poate realiza comanda, produs/client neexistent!");
			return;
		}
		int orderedQuantity = Integer.parseInt(orderInfo[3]);
		
		if( existingProduct.getQuantity() - orderedQuantity < 0 ) {
			generateReportFailed(existingCustomer, existingProduct, orderedQuantity);
			return ;
		}else {
			generateBillOrder(existingCustomer, existingProduct, orderedQuantity);
			existingProduct.setQuantity( existingProduct.getQuantity() - orderedQuantity);
			this.product.update(existingProduct, existingProduct.getIdProduct());
			this.order.insert(new Orders( 0, existingCustomer.getIdCustomer(), orderedQuantity * existingProduct.getPrice()));
			OrderProducts p = new OrderProducts( 0, existingProduct.getIdProduct(), id[0]++, orderedQuantity, orderedQuantity * existingProduct.getPrice());
			try {
				this.orderProduct.validate(p);
			} catch (Exception e) {
				System.out.println("Date invalide pentru comanda!");
			}
			this.orderProduct.insert(p);
		}
	}
	
	/**
	 * Se genereaza bonul pentru o comanda
	 * Se creaza un fisier PDF in directorul aplicatiei
	 * @param customer - clientul care a realizat comanda
	 * @param product - produsul comandat 
	 * @param orderedQuantity - cantitatea comandata
	 */
	public void generateBillOrder(Customer customer, Product product,int orderedQuantity) {
		String pathPDF = "Bill_Order_" + id[3] +".pdf";
		id[3]++;
		
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(pathPDF));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.open();
		try {
			doc.add(new Paragraph("Ordered: " + orderedQuantity + " x " + product.getProductName()));
			doc.add(new Paragraph("Price per unit: " + product.getPrice()));
			doc.add(new Paragraph("Total : " + product.getPrice() * orderedQuantity));
			doc.add(new Paragraph("Customer: " + customer.getCustomerName()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		doc.close();
	}
	
	/**
	 * In cazul in care comanda nu poate fi realizata se genereaza un PDF cu detalii
	 * Fisierul PDF este creat in directorul aplicatiei
	 * @param customer - clientul care realizeaza comanda
	 * @param product - produsul comandat
	 * @param orderedQuantity - cantitatea comandata
	 */
	public void generateReportFailed(Customer customer, Product product, int orderedQuantity) {
		String pathPDF = "UnderStock_" + id[4] +".pdf";
		id[4]++;
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(pathPDF));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found!");
			e.printStackTrace();
		}
		
		doc.open();
		try {
			doc.add( new Paragraph("Under-stock! The ordered quantity exceed the werehouse stock!") );
			
			doc.add( new Paragraph("Client " + customer.getCustomerName() + " ordered " + orderedQuantity + " " +product.getProductName() ));
		
			doc.add( new Paragraph("The avaible stock is " + product.getQuantity()));
		
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		doc.close();
	}
	
	public void reportGenerate(String[] command) {
		if( command[0].contains("client") || command[0].contains("Client") ) {
			generateReportCustomer();
		}else if (  command[0].contains("Product") || command[0].contains("product") ) {
			generateReportProducts();
		}else if ( command[0].contains("order") || command[0].contains("Order") ) {
			generateReportOrder();
		}
		
		return ;
	}
	
	/**
	 * Se realizeaza generarea unui fisier PDF ce contine toti clientii si datele despre acestia structurate in table
	 * Fisierul PDF este generat in directorul aplicatiei
	 */
	public void generateReportCustomer() {
		String pathPDF = "Report_Customer_" + id[5] +".pdf";
		id[5]++;
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(pathPDF));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.open();
		
		PdfPTable table = new PdfPTable(3);
		addTableHeader(table, 0);
		ArrayList<Customer> customerList = this.customer.findAll();
		for (Customer customer : customerList) {
			table.addCell( Integer.toString(customer.getIdCustomer()) );
			table.addCell( customer.getCustomerName() );
			table.addCell( customer.getAddress() );
		}
		
		try {
			doc.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.close();
	}
	
	/**
	 * Se genereaza un fisier PDF ce contine detalii despre produsele existente in baza de date (nume, cantitate, pret, etc.)
	 * Fisierul PDF este generat in directorul aplicatiei
	 */
	private void generateReportProducts() {
		String pathPDF = "Report_Product_" + id[1] +".pdf";
		id[1]++;
		
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(pathPDF));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.open();
		
		PdfPTable table = new PdfPTable(4);
		addTableHeader(table, 2);
		ArrayList<Product> productList = this.product.findAll();
		for (Product product : productList) {
			table.addCell( Integer.toString( product.getIdProduct()) );
			table.addCell( product.getProductName() );
			table.addCell( Integer.toString( product.getQuantity() ));
			table.addCell( Double.toString( product.getPrice() ));
		}
		
		try {
			doc.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.close();
		
	}
	
	/**
	 * Se genereaza un fisier PDF ce contine detalii despre comenzile existente in baza de date 
	 * O comanda consta din detalii despre client, cantitatea comandata, detalii despre produsul comandat si suma totala
	 * Fisierul PDF este generat in directorul aplicatiei
	 */
	private void generateReportOrder() {
		String pathPDF = "Report_Order_" + id[2] +".pdf";
		id[2]++;
		
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(pathPDF));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.open();
		
		PdfPTable table = new PdfPTable(5);
		addTableHeader(table, 1);
		
		ArrayList<Orders> ordersList = this.order.findAll();
		ArrayList<OrderProducts> orderProductsList = this.orderProduct.findAll();
		
		for ( int i = 0; i < ordersList.size(); i++) {
			table.addCell( Integer.toString( ordersList.get(i).getIdOrder() ));
			
			Customer customerFromOrder = this.customer.findClientById( ordersList.get(i).getIdCustomer());
			table.addCell( customerFromOrder.getCustomerName() );
			
			Product productFromOrder = this.product.findById( orderProductsList.get(i).getIdProduct());
			table.addCell( productFromOrder.getProductName() );
			
			table.addCell( Integer.toString(orderProductsList.get(i).getQuantity()) );
			table.addCell( Double.toString(orderProductsList.get(i).getTotal()) );
		}
		
		try {
			doc.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		doc.close();
	}
	
	/**
	 * Se realizeaza generarea de header pentru tabelele de raport
	 * @param table - tabelul la care se va adauga headerul
	 * @param type - un atribut folosit pe post de selectie
	 *  0 - client
	 *  1 - comenzi
	 *  2 - produs
	 */
	private void addTableHeader(PdfPTable table, int type) {
		if( type == 0 ) { //customers
			Stream.of("ID","Name","Address").forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.WHITE);
				header.setBorderWidth(3);
				header.setPhrase(new Phrase(columnTitle));
				table.addCell(header);
			});
		}else if( type == 1 ) { // orders
			Stream.of("ID", "Customer Name", "Product Name", "Quantity", "Total price").forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.WHITE);
				header.setBorderWidth(3);
				header.setPhrase(new Phrase(columnTitle));
				table.addCell(header);
			});
		}else if( type == 2 ) { // product
			Stream.of("ID","Product Name","Quantity","Price per unit").forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.WHITE);
				header.setBorderWidth(3);
				header.setPhrase(new Phrase(columnTitle));
				table.addCell(header);
			});
		}
	}

}

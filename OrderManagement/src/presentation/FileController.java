package presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileController {
	
	private File input;
	private Controller controller = new Controller();
	
	public FileController(String file) {
		input = new File(file);
		readFromFile(input);
	}
	
	/**
	 * Se realizeaza citirea linie cu line din fisier
	 * Fiecare linie va fi procesata de un controller
	 * @param input - fisierul din care se va face citirea
	 */
	private void readFromFile(File input) {
		Scanner scan = null;
		String line;
		try {
			scan = new Scanner(this.input);
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		}
		
		while( scan.hasNext() ){
			line = scan.nextLine();
			String[] all = line.split("[,:]");
			
			for( int i = 0; i < all.length; i++) {
				if( all[i].charAt(0) == ' ' ) {
					all[i] = all[i].substring(1);
				}
			}

			//System.out.println(line);
			this.controller.sqlCommand(all);
			
		}
	}
}

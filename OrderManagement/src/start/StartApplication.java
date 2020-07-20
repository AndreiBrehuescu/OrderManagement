package start;

import presentation.FileController;

public class StartApplication {

	public static void main(String[] args) {
		FileController file = new FileController(args[0]);
	}
}

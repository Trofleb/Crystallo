import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* Charge Flip - Test.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 18 janv. 07
 * 
 * nicolas.schoeni@epfl.ch
 */

public class Test {

	public static void main(String[] args) throws IOException {
		
		InputStream in = new FileInputStream("C:/Documents and Settings/nschoeni/Bureau/Image13.raw");
		while (true) {
			int c = in.read();
			if (c==-1) break;
			System.out.println(c);
		}
		
		
	}

}

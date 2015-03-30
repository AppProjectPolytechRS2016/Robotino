import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class NetworkFlow
{
	/**Methode permettant de lire le flux reseau d'entree et de convertir ce qui a ete lue en String*/
	public static String readMessage(DataInputStream in) throws IOException,EOFException
	{
		int taille = in.available();		
		byte message[]=new byte[taille];
		in.readFully(message);
		return new String(message);
	}
	
	/**Methode permettant d'ecrire un message sur le flux reseau de sortie */
	public static void writeMessage(DataOutputStream out, String s) throws IOException
	{
		byte message[] = s.getBytes();		
		//out.writeInt(message.length);
		out.write(message);
	}
	
	public static String readMessageBis(BufferedReader in) throws IOException,EOFException
	{
		//String toto;
		System.out.println("2");
		return in.readLine();
		//return in.read(toto);
	}
}

package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, List<String>> stockage = new HashMap<>();
		recursiveSearch(new File("C:\\Users\\alger\\eclipse-workspace\\entrainement\\bin"),".",stockage);
		
		for (String key : stockage.keySet()) {
			System.out.println("extension : "+key);
		}
	}
	
	public static void recursiveSearch(File curentDir,String path,Map<String, List<String>> stockage) {
		for(File file : curentDir.listFiles()) {
			String childPath=path+"/"+file.getName();
			if(file.isDirectory())
				recursiveSearch(file, childPath, stockage);
			else if(file.isFile() && file.canRead()) {
				try {
				String sha1=toSha(file);
				List<String> liste = stockage.get(sha1);
				if(liste == null) {
					liste = new ArrayList<String>(1);
					stockage.put(sha1,liste);	
				}
				liste.add(childPath);	
				}
				catch(Exception exception) {
					System.err.println("echec de la lecture du fichier "+file+" fichier ignoré");
				}
			}
			else {
				System.err.println("impossible de lire le fichier"+file);
			}
		}
	}
	
	public static String toSha(File file) throws Exception {
		MessageDigest mDigest= MessageDigest.getInstance("SHA-1");
		try(InputStream input= new FileInputStream(file)) {
			byte[] buffer =new byte[4096];
			int longueur;
			while((longueur = input.read(buffer))!=-1 ) {
				mDigest.update(buffer,0,longueur);
			}
			byte[] total= mDigest.digest();
			return DatatypeConverter.printHexBinary(total).toLowerCase();
		}
	}
	
}

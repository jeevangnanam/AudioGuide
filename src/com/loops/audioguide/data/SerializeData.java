package com.loops.audioguide.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeData {
	
	

	public static void writeObs(String filename ,Object obs) throws IOException{
		
		String file = filename;

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(obs);
			out.close();
		
	}
	
	public static Object getObjs(String file) throws IOException, ClassNotFoundException{
		Object obs = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
	
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			obs = (Object) in.readObject();
			in.close();
	
		return obs;
	}
	
}

package com.sms;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {
	private static final String FILE_NAME = "students.dot";
	
	public static void saveToFile(ArrayList<Student> students) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			oos.writeObject(students);
			System.out.println("💾 Students saved to file.");
		} catch (IOException e) {
			System.out.println("❌ Error saving to file: " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchacked")
	public static ArrayList<Student> loadFromFile() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			return (ArrayList<Student>) ois.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("⚠️ No saved file found. Starting fresh!");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("❌ Error loading from file: " + e.getMessage());
		}
		return new ArrayList<>();
	}
	
}

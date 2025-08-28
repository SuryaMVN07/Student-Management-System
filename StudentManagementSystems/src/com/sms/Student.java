package com.sms;

import java.io.Serializable;

public class Student implements Serializable {
	private int id;
	private String name;
	private int age;
	private String course;
	
	public Student(int id, String name, int age, String course) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.course = course;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void displayStudent() {
		System.out.println("ID : %d | Name : %s | Age : %d | Course : %s\n".formatted(id, name, age, course));
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
	

}

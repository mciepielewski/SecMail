package com.example.sec.sec;

public class Contact implements Comparable<Contact> {
	
	// Variables
	private int _id;
	private String _name;
	private String _lastname;
	private String _email;
	private String _public_key;
	
	// Constructor
	public Contact(){
		
	}
	
	// Constructor
	public Contact(String name, String lastname, String email, String public_key){
		this._name = name;
		this._lastname = lastname;
		this._email = email;
		this._public_key = public_key;
	}
	
	// ID getter and setter functions
	public int getID(){
		return _id;
	}
		
	public void setID(int id){
		this._id = id;
	}
	
	// Name getter and setter functions
	public String getName(){
		return this._name;
	}
		
	public void setName(String name){
		this._name = name;
	}
	
	// Phone Number getter and setter functions
	public String getLastname(){
		return this._lastname;
	}
		
	public void setLastname(String lastname){
		this._lastname = lastname;
	}
	
	// Email getter and setter functions
	public String getEmail(){
		return this._email;
	}
		
	public void setEmail(String email){
		this._email = email;
	}
	
	// Postal Address getter and setter functions
	public String getPublicKey(){
		return this._public_key;
	}	
	
	public void setPublicKey(String public_key){
		this._public_key = public_key;
	}

	public int compareTo(Contact o) {
		int porownaneNazwiska = _lastname.compareTo(o._lastname);

		if(porownaneNazwiska == 0) {
			return _name.compareTo(o._name);
		}
		else {
			return porownaneNazwiska;
		}
	}
}
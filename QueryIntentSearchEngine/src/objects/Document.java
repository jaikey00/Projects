package objects;
/********************************
Name: Jordan Aikey
Problem Set: PS3
Class: CS-4373 Information Retr.
Due Date: 7/16/2021
********************************/
public class Document {
	public String name;
	public int doc_id;
	public float score;
	
	public Document() {
		
	}
	
	public Document(String name, int doc_id, float score) {
		this.name = name;
		this.doc_id = doc_id;
		this.score = score;
	}
}

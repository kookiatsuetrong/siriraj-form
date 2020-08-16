package web;
import javax.persistence.*;

@Entity @Table(name="forms")
public class Form {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	public String title;
	
	public String getTitle() { return title; }
}

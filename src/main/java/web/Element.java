package web;
import javax.persistence.*;

@Entity @Table(name="elements")
public class Element {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	
	@ManyToOne
	public Form form;
	public String title;
	public String type;
	
	public int getId()       { return id;    }
	public String getTitle() { return title; }
	public String getType()  { return type;  }
}

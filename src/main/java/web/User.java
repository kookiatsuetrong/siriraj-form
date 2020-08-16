package web;
import javax.persistence.*;

@Entity @Table(name="users")
public class User {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	
	@Column(unique=true, nullable=false)
	public String email;
	
	@Column(nullable=false)
	public String password;
	
	@Column(name="full_name", nullable=false)
	public String name;
}

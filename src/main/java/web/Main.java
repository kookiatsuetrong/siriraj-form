package web;
import java.util.*;
import javax.persistence.*;
import org.springframework.ui.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class Main {
	public static void main(String[] z) {
		SpringApplication.run(Main.class, z);
	}
}
@Controller
class Web {
	@GetMapping("/")
	String showHome() {
		return "home";
	}
	
	@GetMapping("/test")
	String saveProduct(Model model) {
		int fid = 1;
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
		EntityManager manager = factory.createEntityManager();
		ArrayList<Element> result = null;
		Form form = null;
		try {
			Query query = manager.createQuery("select e from Element e " +
											  "left join e.form f      " +
											  "where f.id = :fid       ");
			query.setParameter("fid", fid);
			result = (ArrayList<Element>)query.getResultList();
			form = manager.find(Form.class, fid);
		} catch (Exception e) {
			result = new ArrayList<Element>();
		}
		model.addAttribute("form", form);
		model.addAttribute("elements", result);
		manager.close();
		return "display";
	}
}

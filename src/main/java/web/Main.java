package web;
import java.util.*;
import java.math.*;
import java.security.*;
import javax.persistence.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class Main
{
    public static void main(String[] z)
    {
        SpringApplication.run(Main.class, z);
    }
}

@Controller
class Web
{
    @GetMapping("/")
    String showHome()
    {
        return "home";
    }

    @GetMapping("/error")
    String showError()
    {
        return "error";
    }

    @GetMapping("/form/{id}")
    String showForm(Model model, @PathVariable String id)
    {
        int fid = -1;
        try {
            fid = Integer.valueOf(id);
        } catch (Exception e) { }

        ArrayList<Element> result = null;
        Form form = null;
        if (fid != -1) {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            try {
                Query query = manager.createQuery(
                                        "select e from Element e " +
                                        "left join e.form f      " +
                                        "where f.id = :fid       ");
                query.setParameter("fid", fid);
                result = (ArrayList<Element>)query.getResultList();
                form = manager.find(Form.class, fid);
            } catch (Exception e) {

            }
            manager.close();
        }

        if (form == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("form", form);
            model.addAttribute("elements", result);
            return "display";
        }
    }

    @GetMapping("/edit/{id}")
    String editForm(HttpSession session, Model model, @PathVariable String id)
    {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            int fid = -1;
            try {
                fid = Integer.valueOf(id);
            } catch (Exception e) { }

            ArrayList<Element> result = null;
            Form form = null;
            if (fid != -1) {
                EntityManagerFactory factory = Persistence
                                            .createEntityManagerFactory("main");
                EntityManager manager = factory.createEntityManager();
                try {
                    Query query = manager.createQuery(
                                "select e from Element e " +
                                "left join e.form f      " +
                                "left join f.user u      " +
                                "where f.id = :fid       " +
                                "and   u.id = :uid       ");
                    query.setParameter("fid", fid);
                    query.setParameter("uid", user.id);
                    result = (ArrayList<Element>)query.getResultList();
                    form = manager.find(Form.class, fid);
                } catch (Exception e) {

                }
                manager.close();
            }

            if (form == null) {
                return "redirect:/error";
            } else {
                model.addAttribute("form", form);
                model.addAttribute("elements", result);
                return "editor";
            }
        }
    }

    @GetMapping("/login")
    String showLogInPage()
    {
        return "login";
    }

    @PostMapping("/login")
    String checkPassword(HttpSession session, String email, String password)
    {
        EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        boolean passed = false;
        try {
            Query query = manager.createQuery("select u from User u " +
                                              "where u.email = :e   " +
                                              "and u.password= :p   ");
            query.setParameter("e", email);
            query.setParameter("p", encrypt(password));
            User u = (User)query.getSingleResult();
            if (u != null) {
                passed = true;
                session.setAttribute("user", u);
            }
        } catch (Exception e) { }
        manager.close();

        if (passed) {
            return "redirect:/profile";
        } else {
            return "redirect:/login?fail-to-login";
        }
    }

    @GetMapping("/logout")
    String showLogOut(HttpSession session)
    {
        session.removeAttribute("user");
        return "logout";
    }

    @GetMapping("/profile")
    String showProfile(HttpSession session, Model model)
    {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        ArrayList<Form> forms;
        EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        try {
            Query query = manager.createQuery("select f from Form f " +
                                              "left join f.user u   " +
                                              "where u.id = :uid    ");
            query.setParameter("uid", user.id);
            forms = (ArrayList<Form>)query.getResultList();
        } catch (Exception e) {
            forms = new ArrayList<Form>();
        }

        model.addAttribute("forms", forms);
        manager.close();
        return "profile";
    }

    @GetMapping("/add-form")
    String showAddForm(HttpSession session)
    {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            Form form = new Form();
            form.title = "New Form";
            form.user = user;
            manager.getTransaction().begin();
            manager.persist(form);
            manager.getTransaction().commit();
            return "redirect:/edit/" + form.id;
        }
    }

    @PutMapping("/save-form") @ResponseBody
    String saveForm(HttpSession session, Integer form, String title)
    {
        User user = (User)session.getAttribute("user");
        if (user == null || form == null) {
            return "Invalid";
        } else {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            try {
                Form f = manager.find(Form.class, form);
                if (f.user.id == user.id) {
                    f.title = title;
                    manager.getTransaction().begin();
                    manager.persist(f);
                    manager.getTransaction().commit();
                }
            } catch (Exception x) { }
            manager.close();
            return "OK";
        }
    }

    @PostMapping("/add-element") @ResponseBody
    Element addElement(HttpSession session,
                    String name,
                    String type,
                    Integer form,
                    String placeholder,
                    Integer min,
                    Integer max)
    {
        Element e = new Element();
        User user = (User)session.getAttribute("user");
        if (user == null || form == null) {
            return e;
        } else {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            try {
                Form f = manager.find(Form.class, form);
                if (f.user.id == user.id) {
                    e.type  = type;
                    e.title = name;
                    e.form  = f;
                    e.placeholder = placeholder;
                    e.min   = 0;
                    e.max   = 10;
                    if (min != null) { e.min = min; }
                    if (max != null) { e.max = max; }
                    manager.getTransaction().begin();
                    manager.persist(e);
                    manager.getTransaction().commit();
                }
            } catch (Exception x) { }
            manager.close();
            return e;
        }
    }

    @DeleteMapping("/remove-element/{eid}") @ResponseBody
    Element removeElement(HttpSession session, @PathVariable Integer eid)
    {
        Element e = new Element();
        User user = (User)session.getAttribute("user");
        if (user == null || eid == null) {
            return e;
        } else {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            try {
                Element t = manager.find(Element.class, eid);
                if (t.form.user.id == user.id) {
                    e = t;
                    manager.getTransaction().begin();
                    manager.remove(t);
                    manager.getTransaction().commit();
                }
            } catch (Exception x) { }
            manager.close();
            return e;
        }
    }

    @PutMapping("/save-element/{eid}") @ResponseBody
    Element saveElement(HttpSession session,
                        @PathVariable Integer eid,
                        String title,
                        String placeholder,
                        Integer min,
                        Integer max)
    {
        Element e = new Element();
        User user = (User)session.getAttribute("user");
        if (user == null || eid == null) {
            return e;
        } else {
            EntityManagerFactory factory = Persistence
                                        .createEntityManagerFactory("main");
            EntityManager manager = factory.createEntityManager();
            try {
                Element t = manager.find(Element.class, eid);
                if (t.form.user.id == user.id) {
                    t.title = title;
                    t.placeholder = placeholder;
                    if (min != null) { t.min = min; }
                    if (min != null) { t.max = max; }
                    manager.getTransaction().begin();
                    manager.persist(t);
                    manager.getTransaction().commit();
                    e = t;
                }
            } catch (Exception x) { }
            manager.close();
            return e;
        }
    }

    /*
    @PostMapping(value="/save", consumes="application/json") @ResponseBody
    List saveForm(HttpSession session,
                        @RequestBody Map<String, List<Element>> data) {
        List<Element> all = data.get("data");

        // for each element, change or insert a new one

        return all;
    }
    */

    String encrypt(String data)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] b = md.digest(data.getBytes());
            BigInteger number = new BigInteger(1, b);
            String hash = number.toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;
        } catch (Exception e) {
            return e.toString();
        }
    }
}

package web;
import java.time.*;
import javax.persistence.*;

@Entity @Table(name="form_values")
public class Value {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;

    @OneToOne
    public Element element;
    public String value;

    public Instant time;

    public Instant getTime()    { return time;    }
    public String  getValue()   { return value;   }
    public Element getElement() { return element; }
}

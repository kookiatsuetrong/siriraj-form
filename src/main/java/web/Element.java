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
    public String placeholder;
    public int min;
    public int max;

    public int getId()       { return id;    }
    public String getTitle() { return title; }
    public String getType()  { return type;  }
    public String getPlaceholder() { return placeholder; }
    public int getMin()      { return min; }
    public int getMax()      { return max; }
}

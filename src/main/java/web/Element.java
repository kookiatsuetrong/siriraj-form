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
    public String status;
    public char required;
    public char custom;
    public String value;
    public String detail;

    public int    getId()          { return id;    }
    public String getTitle()       { return title; }
    public String getType()        { return type;  }
    public String getPlaceholder() { return placeholder; }
    public int    getMin()         { return min;      }
    public int    getMax()         { return max;      }
    public char   getRequired()    { return required; }
    public char   getCustom()      { return custom;   }
    public String getValue()       { return value;    }
    public String getDetail()      { return detail;   }
}

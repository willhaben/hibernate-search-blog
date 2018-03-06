package at.willhaben.test.search.models;
import org.hibernate.search.annotations.*;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "adData")
@Indexed
public class AdData {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private Date created;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @Field(analyze = Analyze.NO)
    @Facet(name = "category")
    @NotNull
    private String category;

    public AdData() {
    }

    public AdData(Date created, String title, String text, String category) {
        this.created = created;
        this.title = title;
        this.text = text;
        this.category = category;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Transient
    @Field(name = "text")
    public String getSearchText(){
        return title + " "+text;
    }

    @Transient
    @Field(name = "createdYear", analyze = Analyze.NO)
    @Facet(forField = "createdYear", encoding = FacetEncodingType.STRING)
    public String getCreated() {
        return created.toLocalDate().format(DateTimeFormatter.ofPattern("YYYY"));
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

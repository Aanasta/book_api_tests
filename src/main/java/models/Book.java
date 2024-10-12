package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    private int id;
    private String name;
    private String author;
    private String publication;
    private String category;
    private int pages;
    private double price;

    public String compare(Book other) {
        if (other == null) {
            return "The other book is null.";
        }
        StringBuilder differences = new StringBuilder();
        if (!Objects.equals(this.name, other.name)) {
            differences.append(String.format("Name does not match: expected '%s' but found '%s'. ",
                    this.name, other.name));
        }
        if (!Objects.equals(this.author, other.author)) {
            differences.append(String.format("Author does not match: expected '%s' but found '%s'. ",
                    this.author, other.author));
        }
        if (!Objects.equals(this.publication, other.publication)) {
            differences.append(String.format("Publication does not match: expected '%s' but found '%s'. ",
                    this.publication, other.publication));
        }
        if (!Objects.equals(this.category, other.category)) {
            differences.append(String.format("Category does not match: expected '%s' but found '%s'. ",
                    this.category, other.category));
        }
        if (this.pages != other.pages) {
            differences.append(String.format("Pages do not match: expected '%d' but found '%d'. ",
                    this.pages, other.pages));
        }
        if (Double.compare(this.price, other.price) != 0) {
            differences.append(String.format("Price does not match: expected '%f' but found '%f'. ",
                    this.price, other.price));
        }
        return differences.toString();
    }
}

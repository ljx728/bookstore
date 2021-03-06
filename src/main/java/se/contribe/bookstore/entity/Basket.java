package se.contribe.bookstore.entity;

import javax.persistence.*;

/**
 * An entity class to map the relationship between User Basket and DB persistence.
 */
@Entity
@Table(name = "basket", uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id"})})
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Book book;
    private Integer quantity;

    public Basket() { }

    public Basket(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

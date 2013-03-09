/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;

/**
 *
 * @author berik
 */
public class Book {

    public enum BookType {

        CUENTO, NARRATIVA, CONOCIMIENTOS, MISTERIO, NOVELA, HISTORIA, NOTICIAS
    };
    private String ISBN, title, editorial;
    private BookType type;
    private int edition, year;
    private ArrayList<Author> author = new ArrayList();

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Author> getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author.add(author);
    }

    @Override
    public String toString() //{return (this.title+" (de "+this.author+")");}
    {
        String authors = "";
        if (this.author.size() != 1) {
            for (int i = 0; i < this.author.size() - 1; i++) {
                authors = authors + this.author.get(i);
                if (i < this.author.size() - 2) {
                    authors = authors + ", ";
                }
            }
            authors = authors + " y " + this.author.get(this.author.size() - 1);

        } else {
            authors = authors + this.author.get(this.author.size() - 1);
        }
        return (this.title + " (de " + authors + ")");
    }

    @Override
    public boolean equals(Object obj) {
        Book a = (Book) obj;
        return ISBN.equals(a.getISBN());
    }
}

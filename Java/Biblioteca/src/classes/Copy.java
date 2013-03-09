/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author berik
 */
public class Copy {
    public enum CopyState{BORROWED,STORED};
    private int bookCode;
    private CopyState state;
    private Book book;

    public int getBookCode() {
        return bookCode;
    }

    public void setBookCode(int bookCode) {
        this.bookCode = bookCode;
    }

    public CopyState getState() {
        return state;
    }

    public void setState(CopyState state) {
        this.state = state;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
        public String toString() //{return (this.title+" (de "+this.author+")");}
    {
       return(this.book.toString());
    }
}


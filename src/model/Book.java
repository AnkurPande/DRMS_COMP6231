/*
 * 
 * @author Syed Sumair Zafar
 * 
 */

package model;

public class Book {

	// Book Structure
	private String bookName = null;
	private String bookAuthor = null;
	private int numberCopies = 0;
	private int numberOfCopiesIsHolding = 0;
	/*
	 * Default Book constructor
	 */
	public Book()
	{
		
	}
	
	
	/*
	 * Book constructor overloading
	 */
	public Book(String bookName, String BookAuthor, int numberCopies)
	{		
		this.bookName = bookName;
		this.bookAuthor = BookAuthor;
		this.numberCopies = numberCopies;
	}
	
	
	// ------------------------ GETTERS Functions START --------------------------------------
	
	/*
	 * This function will return the book name
	 */
	public String getBookName()
	{
		return this.bookName;
	}
	
	
	/*
	 * This function will return book author name
	 */
	public String getBookAuthor()
	{
		return this.bookAuthor;
	}
	
	
	/*
	 * This function will return number of copies left
	 */
	public int getNumberCopies()
	{
		return this.numberCopies;
	}
	
	// ------------------------ GETTERS Functions END --------------------------------------
	
	
	// ------------------------ SETTERS Functions Start --------------------------------------
	/*
	 * This function will set Book Name
	 */
	public void setBookName(String bookName)
	{
		this.bookName = bookName;
	}
	
	/*
	 * This function will set Book Author Name
	 */
	public void setBookAuthor(String bookAuthor)
	{
		this.bookAuthor = bookAuthor;
	}
	
	/*
	 * This function will set number of copies available
	 */
	public void setNumberCopies(int numberCopies)
	{
		this.numberCopies = numberCopies;
	}
	// ------------------------ SETTERS Functions END --------------------------------------
	
	
	// ------------------------ Misc Functions --------------------------------------------
	/*
	 * This will subtract the copies when a user reserve a book
	 */
	public void reserveBook()
	{
		this.numberCopies--;
	}
	
	/*
	 * This will add number of copies when a use return back a book
	 */
	public void returnBackBook()
	{
		this.numberCopies++;
	}


	public int getNumberOfCopiesIsHolding() {
		return numberOfCopiesIsHolding;
	}


	public void setNumberOfCopiesIsHolding(int numberOfCopiesIsHolding) {
		this.numberOfCopiesIsHolding = numberOfCopiesIsHolding;
	}

}

package telran.library.dto;
import java.time.LocalDate;
import java.util.*;

import lombok.Setter;
@Setter

public class Book {
	long isbn;
	int publishingYear;
	String publisherName;
	Set<String> authors;
	String title;
	int amountInLibrary;
	int amountInUse;
	SubjectBook subject;
	String language;
	int maxDaysInUse;
	LocalDate archivingDate;
	public Book(long isbn, int publishingYear, String publisherName, Set<String> authors, String title,
			int amountInLibrary, SubjectBook subject, String language, int maxDaysInUse) {
		super();
		this.isbn = isbn;
		this.publishingYear = publishingYear;
		this.publisherName = publisherName;
		this.authors = authors;
		this.title = title;
		this.amountInLibrary = amountInLibrary;
		this.subject = subject;
		this.language = language;
		this.maxDaysInUse = maxDaysInUse;
	}
	public Book() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (isbn ^ (isbn >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (isbn != other.isbn)
			return false;
		return true;
	}
	public int getAmountInLibrary() {
		return amountInLibrary;
	}
	public void setAmountInLibrary(int amountInLibrary) {
		this.amountInLibrary = amountInLibrary;
	}
	public int getAmountInUse() {
		return amountInUse;
	}
	public void setAmountInUse(int amountInUse) {
		this.amountInUse = amountInUse;
	}
	public int getMaxDaysInUse() {
		return maxDaysInUse;
	}
	public void setMaxDaysInUse(int maxDaysInUse) {
		this.maxDaysInUse = maxDaysInUse;
	}
	public LocalDate getArchivingDate() {
		return archivingDate;
	}
	public void setArchivingDate(LocalDate archivingDate) {
		this.archivingDate = archivingDate;
	}
	public long getIsbn() {
		return isbn;
	}
	public int getPublishingYear() {
		return publishingYear;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public Set<String> getAuthors() {
		return authors;
	}
	public String getTitle() {
		return title;
	}
	public SubjectBook getSubject() {
		return subject;
	}
	public String getLanguage() {
		return language;
	}
}

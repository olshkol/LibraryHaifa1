package telran.library.dto;

import java.time.LocalDate;

public class Record {
	 long  isbn;
	 long  readerId;
	  LocalDate datePickingUp;
	LocalDate  dateOfReturning; 
	 int  daysDelayed;
	 public Record() {
	}
	 public Record(long isbn, long readerId, LocalDate datePickingUp) {
		super();
		this.isbn = isbn;
		this.readerId = readerId;
		this.datePickingUp = datePickingUp;
	}
	public LocalDate getDateOfReturning() {
		return dateOfReturning;
	}
	public void setDateOfReturning(LocalDate dateOfReturning) {
		this.dateOfReturning = dateOfReturning;
	}
	public int getDaysDelayed() {
		return daysDelayed;
	}
	public void setDaysDelayed(int daysDelayed) {
		this.daysDelayed = daysDelayed;
	}
	public boolean isBookIsLost() {
		return bookIsLost;
	}
	public void setBookIsLost(boolean bookIsLost) {
		this.bookIsLost = bookIsLost;
	}
	public long getIsbn() {
		return isbn;
	}
	public long getReaderId() {
		return readerId;
	}
	public LocalDate getDatePickingUp() {
		return datePickingUp;
	}
	boolean  bookIsLost;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bookIsLost ? 1231 : 1237);
		result = prime * result + ((dateOfReturning == null) ? 0 : dateOfReturning.hashCode());
		result = prime * result + ((datePickingUp == null) ? 0 : datePickingUp.hashCode());
		result = prime * result + daysDelayed;
		result = prime * result + (int) (isbn ^ (isbn >>> 32));
		result = prime * result + (int) (readerId ^ (readerId >>> 32));
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
		Record other = (Record) obj;
		if (bookIsLost != other.bookIsLost)
			return false;
		if (dateOfReturning == null) {
			if (other.dateOfReturning != null)
				return false;
		} else if (!dateOfReturning.equals(other.dateOfReturning))
			return false;
		if (datePickingUp == null) {
			if (other.datePickingUp != null)
				return false;
		} else if (!datePickingUp.equals(other.datePickingUp))
			return false;
		if (daysDelayed != other.daysDelayed)
			return false;
		if (isbn != other.isbn)
			return false;
		if (readerId != other.readerId)
			return false;
		return true;
	}
}

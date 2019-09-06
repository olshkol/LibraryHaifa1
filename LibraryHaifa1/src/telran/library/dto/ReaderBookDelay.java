package telran.library.dto;

public class ReaderBookDelay {
long isbn;
long readerId;
int delayDays;
public long getIsbn() {
	return isbn;
}
public long getReaderId() {
	return readerId;
}
public int getDelayDays() {
	return delayDays;
}
public ReaderBookDelay() {
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + delayDays;
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
	ReaderBookDelay other = (ReaderBookDelay) obj;
	if (delayDays != other.delayDays)
		return false;
	if (isbn != other.isbn)
		return false;
	if (readerId != other.readerId)
		return false;
	return true;
}
public ReaderBookDelay(long isbn, long readerId, int delayDays) {
	super();
	this.isbn = isbn;
	this.readerId = readerId;
	this.delayDays = delayDays;
}

}

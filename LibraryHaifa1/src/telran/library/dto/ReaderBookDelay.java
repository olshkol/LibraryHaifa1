package telran.library.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"delayDays", "isbn", "readerId"})
@ToString
public class ReaderBookDelay {
    long isbn;
    long readerId;
    long delayDays;
}

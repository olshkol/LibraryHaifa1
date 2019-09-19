package telran.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.library.dto.Book;
import telran.library.dto.LibReturnCode;
import telran.library.dto.PublisherAuthor;
import telran.library.service.interfaces.ILibrary;

import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryController {
    private final ILibrary libraryService;

    @PostMapping(LibraryApiConstants.ADD_AUTHOR)
    public LibReturnCode addAuthor(@RequestBody PublisherAuthor author){
        return libraryService.addAuthor(author);
    }

    @PostMapping(LibraryApiConstants.ADD_PUBLISHER)
    public LibReturnCode addPublisher(@RequestBody PublisherAuthor publisher){
        return libraryService.addPublisher(publisher);
    }

    @PostMapping(LibraryApiConstants.ADD_BOOK)
    public LibReturnCode addBook(@RequestBody Book book){
        return libraryService.addBookItem(book);
    }

    @GetMapping(LibraryApiConstants.GET_AUTHOR)
    public List<PublisherAuthor> getAuthor(@RequestParam String name){
        return libraryService.getAuthorsByName(name);
    }

    @GetMapping(LibraryApiConstants.GET_PUBLISHER)
    public PublisherAuthor getPublisher(@RequestParam String name){
        return libraryService.getPublisherByName(name);
    }

    @GetMapping(LibraryApiConstants.GET_BOOK)
    public Book getBook(@PathVariable("isbn") long isbn){
        return libraryService.getBookItem(isbn);
    }
}

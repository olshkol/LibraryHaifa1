package telran.library.controller;

public interface LibraryApiConstants {
    String ADD_AUTHOR = "/authors";
    String GET_AUTHOR = "/authors";
    String ADD_BOOK = "/books";
    String GET_BOOK = "/books/{isbn}";
    String ADD_PUBLISHER = "/publisher";
    String GET_PUBLISHER = "/publisher";
    String ADD_READER = "/reader";
    String GET_READER = "/reader";
    String PICK_BOOK = "/books/pick";
    String RETURN_BOOK = "/books/return";
    String ACTIVE_READER = "readers/active";
}

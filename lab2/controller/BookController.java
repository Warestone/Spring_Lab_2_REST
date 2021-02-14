package lab2.controller;

import lab2.exception.NotFoundException;
import lab2.getter.GetItem;
import lab2.model.Book;
import lab2.repository.BookRepository;
import lab2.response.Response;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    private final BookRepository bookRepository;
    private final Response response = new Response();
    private final GetItem getItem = new GetItem();

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository; }

    @GetMapping(value = "/sql/1")
    @Query(value="SELECT bookId,hrs,ot FROM book",nativeQuery=true)
    private List<Book> getValues(){
        return null;
    }

    @GetMapping(value = "/")
    public List<Book>getAllBooks(){ return bookRepository.findAll(); }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) throws NotFoundException {
        return ResponseEntity.ok().body(
                getItem.getBook(id, bookRepository));
    }

    @PostMapping(value = "/")
    public Map<String, String> addBook(@Valid @RequestBody Book book){
        bookRepository.save(book);
        return response.getResponse("POST","The book with name '"+book.getName()+"' has been added.");
    }

    @DeleteMapping(value = "/{id}")
    public Map<String, String> deleteBook(@PathVariable int id) throws NotFoundException {
        bookRepository.delete(getItem.getBook(id,bookRepository));
        return response.getResponse("DELETE","The book with id '"+id+"' has been deleted.");
    }

    @PatchMapping(value = "/{id}")
    public Map<String, String> patchBook(@RequestBody Book bookUpdate, @PathVariable int id) throws NotFoundException {
        Book currentBook = getItem.getBook(id,bookRepository);
        if (bookUpdate.getName()!=null) currentBook.setName(bookUpdate.getName());
        if (bookUpdate.getCost()>0.0) currentBook.setCost(bookUpdate.getCost());
        if (bookUpdate.getStorage() !=null) currentBook.setStorage(bookUpdate.getStorage());
        if (bookUpdate.getQuantity()>0) currentBook.setQuantity(bookUpdate.getQuantity());
        bookRepository.save(currentBook);
        return response.getResponse("PATCH","The book with name '"+currentBook.getName()+"' has been patched.");
    }

    @PutMapping(value = "/{id}")
    public Map<String, String> updateBook(@RequestBody @Valid Book book, @PathVariable int id) throws NotFoundException {
        Book bookOld = getItem.getBook(id,bookRepository);
        book.setId(id);
        book.setPurchases(bookOld.getPurchases());
        bookRepository.save(book);
        return response.getResponse("PUT","The book with name '"+book.getName()+"' has been updated.");
    }
}

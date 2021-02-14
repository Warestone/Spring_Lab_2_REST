package lab2.controller;

import lab2.exception.NotFoundException;
import lab2.getter.GetItem;
import lab2.model.*;
import lab2.repository.*;
import lab2.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/purchase")
public class PurchaseController {

    private final PurchaseRestRepository purchaseRestRepository;
    private final PurchaseDbRepository purchaseDbRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final Response response = new Response();
    private final GetItem getItem = new GetItem();

    public PurchaseController(PurchaseRestRepository purchaseRestRepository,
                              PurchaseDbRepository purchaseDbRepository, BookRepository bookRepository,
                              CustomerRepository customerRepository,
                              ShopRepository shopRepository) {
        this.bookRepository = bookRepository;
        this.purchaseRestRepository = purchaseRestRepository;
        this.purchaseDbRepository = purchaseDbRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
    }

    @GetMapping(value = "/")
    public List<PurchaseRest> getAllPurchasers(){ return purchaseRestRepository.findAll(); }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PurchaseRest> getPurchaseById(@PathVariable int id) throws NotFoundException {
        return ResponseEntity.ok().body(
                getItem.getPurchase(id, purchaseRestRepository));
    }

    @PostMapping(value = "/")
    public Map<String, String> addPurchase(@RequestBody @Valid PurchaseDb purchase) throws NotFoundException {
        Book book = getItem.getBook(purchase.getBookId(), bookRepository);
        if (!checkBookQuantity(book,purchase.getQuantity()))
            return response.getResponse("ERROR","Not enough books for purchase!");
        getItem.getShop(purchase.getShopId(), shopRepository);
        getItem.getCustomer(purchase.getCustomerId(), customerRepository);
        purchaseDbRepository.save(purchase);
        book.setQuantity(book.getQuantity()-purchase.getQuantity());
        new BookController(bookRepository).updateBook(book,book.getId());
        return response.getResponse("POST","The purchase with book '"+book.getName()+"' has been added.");
    }

    @DeleteMapping(value = "/{id}")
    public Map<String, String> deletePurchase(@PathVariable int id) throws NotFoundException {
        purchaseRestRepository.delete(getItem.getPurchase(id, purchaseRestRepository));
        return response.getResponse("DELETE","The purchase with id '"+id+"' has been deleted.");
    }

    @PatchMapping(value = "/{id}")
    public Map<String, String> patchPurchase(@RequestBody PurchaseDb purchaseDb, @PathVariable int id) throws NotFoundException {
        putOrPatch(purchaseDb, id);
        return response.getResponse("PATCH","The purchase with id '"+id+"' has been patched.");
    }

    @PutMapping(value = "/{id}")
    public Map<String, String> updatePurchase(@RequestBody @Valid PurchaseDb purchaseDb, @PathVariable int id) throws NotFoundException {
        putOrPatch(purchaseDb, id);
        return response.getResponse("PUT","The purchase with id '"+id+"' has been updated.");
    }

    private Boolean checkBookQuantity(Book book, int quantity){
        return book.getQuantity() >= quantity; }

    private void putOrPatch(PurchaseDb purchaseUpdate, int id) throws NotFoundException {
        PurchaseDb purchaseCurrent = getItem.getPurchase(id, purchaseDbRepository);
        int quantityBooksUpdate = purchaseUpdate.getQuantity();

        if (purchaseUpdate.getDate()!=null) purchaseCurrent.setDate(purchaseUpdate.getDate());
        if (purchaseUpdate.getShopId()>0){
            getItem.getShop(purchaseUpdate.getShopId(), shopRepository);
            purchaseCurrent.setShopId(purchaseUpdate.getShopId());
        }
        if (purchaseUpdate.getCustomerId()>0){
            getItem.getCustomer(purchaseUpdate.getCustomerId(), customerRepository);
            purchaseCurrent.setCustomerId(purchaseUpdate.getCustomerId());
        }
        if (purchaseUpdate.getBookId()>0)
        {
            Book book = getItem.getBook(purchaseUpdate.getBookId(), bookRepository);
            purchaseCurrent.setBookId(book.getId());
        }

        if (quantityBooksUpdate>0){
            Book book = getItem.getBook(purchaseCurrent.getBookId(), bookRepository);
            int oldQuantity = book.getQuantity()+purchaseCurrent.getQuantity();
            if (quantityBooksUpdate<=oldQuantity){
                book.setQuantity(oldQuantity-quantityBooksUpdate);
                new BookController(bookRepository).updateBook(book,book.getId());
                purchaseCurrent.setQuantity(quantityBooksUpdate);
            }
        }
        if (purchaseUpdate.getPrice()>0)
            purchaseCurrent.setPrice(purchaseUpdate.getPrice());

        purchaseDbRepository.save(purchaseCurrent);
    }
}

package lab2.getter;

import lab2.exception.NotFoundException;
import lab2.model.*;
import lab2.repository.*;

public class GetItem {
    public Book getBook(int id, BookRepository bookRepository) throws NotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Book with id "+id+" not found!"));
    }

    public Customer getCustomer(int id, CustomerRepository customerRepository) throws NotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Customer with id "+id+" not found!"));
    }

    public Shop getShop(int id, ShopRepository shopRepository) throws NotFoundException {
        return shopRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Shop with id "+id+" not found!"));
    }

    public PurchaseRest getPurchase(int id, PurchaseRestRepository purchaseRepository) throws NotFoundException {
        return purchaseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Purchase with id "+id+" not found!"));
    }

    public PurchaseDb getPurchase(int id, PurchaseDbRepository purchaseDbRepository) throws NotFoundException {
        return purchaseDbRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Purchase with id "+id+" not found!"));
    }
}

package lab2.controller;

import lab2.exception.NotFoundException;
import lab2.getter.GetItem;
import lab2.model.Customer;
import lab2.repository.CustomerRepository;
import lab2.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Response response = new Response();
    private final GetItem getItem = new GetItem();

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository; }

    @GetMapping(value = "/")
    public List<Customer> getAllCustomers(){ return customerRepository.findAll(); }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) throws NotFoundException {
        return ResponseEntity.ok().body(
                getItem.getCustomer(id,customerRepository));
    }

    @PostMapping(value = "/")
    public Map<String, String> addCustomer(@Valid @RequestBody Customer customer){
        customerRepository.save(customer);
        return response.getResponse("POST","The customer with last name '"+customer.getLastName()+"' has been added.");
    }

    @DeleteMapping(value = "/{id}")
    public Map<String, String> deleteCustomer(@PathVariable int id) throws NotFoundException {
        customerRepository.delete(getItem.getCustomer(id,customerRepository));
        return response.getResponse("DELETE","The customer with id '"+id+"' has been deleted.");
    }

    @PatchMapping(value = "/{id}") //bad
    public Map<String, String> patchCustomer(@RequestBody Customer customerUpdate, @PathVariable int id) throws NotFoundException {
        Customer currentCustomer = getItem.getCustomer(id,customerRepository);
        if (customerUpdate.getLastName()!=null) currentCustomer.setLastName(customerUpdate.getLastName());
        if (customerUpdate.getRegion()!=null) currentCustomer.setRegion((customerUpdate.getRegion()));
        if (customerUpdate.getDiscount()>0.0) currentCustomer.setDiscount(customerUpdate.getDiscount());
        customerRepository.save(currentCustomer);
        return response.getResponse("PATCH","The customer with last name '"+currentCustomer.getLastName()+"' has been patched.");
    }

    @PutMapping(value = "/{id}")
    public Map<String, String> updateCustomer(@RequestBody @Valid Customer customer, @PathVariable int id) throws NotFoundException {
        Customer customerOld = getItem.getCustomer(id,customerRepository);
        customer.setId(id);
        customer.setPurchases(customerOld.getPurchases());
        customerRepository.save(customer);
        return response.getResponse("PUT","The customer with last name '"+customer.getLastName()+"' has been updated.");
    }
}

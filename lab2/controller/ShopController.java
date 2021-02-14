package lab2.controller;

import lab2.exception.NotFoundException;
import lab2.getter.GetItem;
import lab2.model.Shop;
import lab2.repository.ShopRepository;
import lab2.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    private final ShopRepository shopRepository;
    private final Response response = new Response();
    private final GetItem getItem = new GetItem();

    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository; }

    @GetMapping(value = "/")
    public List<Shop> getAllShops(){ return shopRepository.findAll(); }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable int id) throws NotFoundException {
        return ResponseEntity.ok().body(
                getItem.getShop(id, shopRepository));
    }

    @PostMapping(value = "/")
    public Map<String, String> addShop(@Valid @RequestBody Shop shop){
        shopRepository.save(shop);
        return response.getResponse("POST","The shop with name '"+shop.getName()+"' has been added.");
    }

    @DeleteMapping(value = "/{id}")
    public Map<String, String> deleteShop(@PathVariable int id) throws NotFoundException {
        shopRepository.delete(getItem.getShop(id,shopRepository));
        return response.getResponse("DELETE","The shop with id '"+id+"' has been deleted.");
    }

    @PatchMapping(value = "/{id}") //bad
    public Map<String, String> patchShop(@RequestBody Shop shopUpdate, @PathVariable int id) throws NotFoundException {
        Shop shopCurrent = getItem.getShop(id, shopRepository);
        if (shopUpdate.getName()!=null) shopCurrent.setName(shopUpdate.getName());
        if (shopUpdate.getRegion()!=null) shopCurrent.setRegion(shopUpdate.getRegion());
        if (shopUpdate.getCommission_pct()>0) shopCurrent.setCommission_pct(shopUpdate.getCommission_pct());
        shopRepository.save(shopCurrent);
        return response.getResponse("PATCH","The shop with name '"+shopCurrent.getName()+"' has been patched.");
    }

    @PutMapping(value = "/{id}")
    public Map<String, String> updateShop(@RequestBody @Valid Shop shop, @PathVariable int id) throws NotFoundException {
        Shop shopOld = getItem.getShop(id, shopRepository);
        shop.setId(id);
        shop.setPurchases(shopOld.getPurchases());
        shopRepository.save(shop);
        return response.getResponse("PUT","The shop with name '"+shop.getName()+"' has been updated.");
    }
}

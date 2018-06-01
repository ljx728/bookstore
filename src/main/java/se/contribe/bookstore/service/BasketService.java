package se.contribe.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.contribe.bookstore.dao.BasketDAO;
import se.contribe.bookstore.entity.Basket;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BasketService {

    @Autowired
    private BasketDAO basketDAO;

    public Basket[] findAllBasketBooks() {
        return basketDAO.findAll().toArray(new Basket[0]);
    }

    public BigDecimal totalBasketPrice() {
        List<Basket> basketBookList = basketDAO.findAll();
        double totalPrice = basketBookList.stream()
                .map(basket -> basket.getBook().getPrice().multiply(new BigDecimal(basket.getQuantity())))
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        return new BigDecimal(totalPrice);
    }
}

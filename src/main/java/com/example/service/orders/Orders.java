package com.example.service.orders;

import com.example.service.inventory.InventoryUpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

class Orders {
}

@Controller
@ResponseBody
@RequestMapping("/orders")
@Transactional
class OrdersController {
    private final OrderRepository repository;
    private final LineItemRepository lineItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    OrdersController(OrderRepository repository, LineItemRepository lineItemRepository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.lineItemRepository = lineItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    void create(@RequestBody Order order) {
        System.out.println("To save [" + order + "]");
        var saved = this.repository.save(order);
        System.out.println("saved [" + saved + "]");
        saved
                .lineItems()
                .forEach( lineItem -> {
                    eventPublisher.publishEvent(
                            new InventoryUpdateEvent(lineItem.product(), lineItem.quantity())
                    );
                });
    }

    @GetMapping
    Collection<Order> getAllOrders() {
        return this.repository.findAll();
    }

//    @GetMapping
//    List<LineItem> getAllLineItemsByOrder(String orderId) {
//        return this.lineItemRepository.findBy;
//    }

}

@Table("orders_line_items")
record LineItem (@Id Integer id, int product, int quantity) {}

@Table("orders")
record Order (@Id Integer id, Set<LineItem> lineItems) {}

interface OrderRepository extends ListCrudRepository<Order, Integer> {
}

interface LineItemRepository extends ListCrudRepository<LineItem, Integer> {
}

class Loans {
    String displayMessageFor (Loan loan) {
        var message = switch (loan ) {
            case UnsecuredLoan (var interest) ->
                    "Ouch! That " + interest + "% is going to hurt!";
            case SecuredLoan sl -> "Hey. Good job. You got a loan. Well done.";
        };
        if (loan instanceof SecuredLoan sl) {
            message = "Hey. Good job. You got a loan. Well done.";
        }
//        if (loan instanceof UnsecuredLoan (var interest)) {
//            message = "Ouch! That " + interest + "% is going to hurt!";
//        }
        return message;
    }
}
sealed interface Loan permits SecuredLoan, UnsecuredLoan {}

final class SecuredLoan implements Loan {}
record UnsecuredLoan(float interest) implements Loan {}
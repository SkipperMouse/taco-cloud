package sia.tacocloud.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import sia.tacocloud.model.TacoOrder;
import sia.tacocloud.repository.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order,
                               Errors errors,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) return "orderForm";
        log.info("Order submitted {}", order);
        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @PostMapping("/clear")
    //method won't be invoked if user doesn't have role ADMIN throws AccessDeniedException
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAllOrders() {
        orderRepo.deleteAll();
        return "redirect:/";
    }
    @PostAuthorize("hasRole('ADMIN') || " +
            "returnObject.user.username == authentication.name")
    public TacoOrder getOrder(long id) {
        return orderRepo.findById(id).orElse(new TacoOrder());
    }

}


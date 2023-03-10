package sia.tacocloud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Taco_Order")
@Getter
@Setter
public class TacoOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "delivery_name")
    @NotBlank(message = "Delivery name is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryName;

    @Column(name = "delivery_street")
    @NotBlank(message = "Street is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryStreet;

    @Column(name = "delivery_city")
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryCity;

    @Column(name = "delivery_state")
    @NotBlank(message = "State is required")
    @Size(max = 2, message = "Maximum size of the field is 2 characters")
    private String deliveryState;

    @Column(name = "delivery_zip")
    @NotBlank(message = "Zip code is required")
    @Size(max = 10, message = "Maximum size of the field is 10 characters")
    private String deliveryZip;

    @Column(name = "cc_number")
    @CreditCardNumber(message = "Not a valid card number")
    private String ccNumber;

    @Column(name = "cc_expiration")
    @Pattern(regexp = "(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
            message = "Must be format MM/YY")
    private String ccExpiration;

    @Column(name = "cc_cvv")
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Taco> tacos;

    @Column(name = "placed_at")
    private Date placedAt;

    public void addTaco(Taco taco) {
        if (tacos == null) tacos = new ArrayList<>();
        tacos.add(taco);
    }
}

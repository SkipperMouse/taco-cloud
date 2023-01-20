package sia.tacocloud.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TacoOrder implements Serializable {
    private static final long serialVersionUID  = 1L;
    private Long id;

    @NotBlank(message = "Delivery name is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "Maximum size of the field is 50 characters")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    @Size(max = 2, message = "Maximum size of the field is 2 characters")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    @Size(max = 10, message = "Maximum size of the field is 10 characters")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid card number")
    private String ccNumber;

    @Pattern(regexp = "(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
            message = "Must be format MM/YY")
    private String ccExpiration;

    @Digits(integer =3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;
    private List<Taco> tacos;

    private Date createdAt;

    public void addTaco(Taco taco) {
        if (tacos == null) tacos = new ArrayList<>();
        tacos.add(taco);
    }
}

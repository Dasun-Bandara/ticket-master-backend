package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeTO implements Serializable {
    @Pattern(regexp = "^(MOTOR_BIKE|THREE_WHEEL|CAR|BUS|OTHER)$",message = "Invalid vehicle category")
    private String category;
    @PositiveOrZero
    private BigDecimal chargePerHour;
}

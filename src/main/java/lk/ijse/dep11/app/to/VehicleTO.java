package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTO implements Serializable {
    @Null(message = "Parking Number should be empty")
    private Integer parkingNumber;
    @NotBlank(message = "Registration Number can't be empty")
    @Pattern(regexp = "^[A-Za-z0-9-]{1,10}$",
            message = "Invalid registration number")
    private String registrationNumber;
    @NotBlank(message = "Contact can't be empty")
    @Pattern(regexp = "^0[0-9]{2}-[0-9]{7}$", message = "Invalid contact number")
    private String contact;
    @NotBlank(message = "Category can't be empty")
    @Pattern(regexp = "^(BIKE|THREE_WHEEL|CAR|BUS|OTHER)$",message = "Invalid category")
    private String category;
    @Null(message = "Status should be empty")
    private String status;
    @Null(message = "Charge per hour should be empty")
    private BigDecimal chargePerHour;
    @Null(message = "In time should be empty")
    private String inTime;
    @Null(message = "Out time should be empty")
    private String outTime;
}

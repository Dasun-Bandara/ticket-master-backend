package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTO implements Serializable {
    private Integer parkingNumber;
    private String registrationNumber;
    private String contact;
    private String category;
    private String status;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
}

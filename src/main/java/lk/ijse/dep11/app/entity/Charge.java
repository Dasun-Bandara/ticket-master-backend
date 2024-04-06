package lk.ijse.dep11.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "charge")
public class Charge implements Serializable {
    @Id
    @Column(name = "vehicle_category",length = 15)
    private String vehicleCategory;
    @Column(name = "charge_per_hour",precision = 8, scale = 2, nullable = false)
    private BigDecimal chargePerHour;
}

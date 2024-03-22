package lk.ijse.dep11.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "check_out")
public class CheckOut implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "parking_no", referencedColumnName = "parking_no")
    private Vehicle vehicle;
    @Column(name = "out_time",nullable = false)
    private Timestamp outTime;
    @Column(name = "amount_per_hour", precision = 8, scale = 2, nullable = false)
    private BigDecimal amountPerHour;
}

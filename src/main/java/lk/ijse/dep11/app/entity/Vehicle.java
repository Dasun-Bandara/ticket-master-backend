package lk.ijse.dep11.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_no")
    private int parkingNumber;
    @Column(name = "reg_no",length = 15,nullable = false)
    private String registrationNumber;
    @Column(length = 15, nullable = false)
    private String contact;
    @Column(length = 10, nullable = false)
    private String category;
    @Column(length = 10, nullable = false)
    private String status;
    @Column(name = "in_time", nullable = false)
    private Timestamp inTime;

    public Vehicle(String registrationNumber, String contact, String category, String status, Timestamp inTime) {
        this.registrationNumber = registrationNumber;
        this.contact = contact;
        this.category = category;
        this.status = status;
        this.inTime = inTime;
    }
}

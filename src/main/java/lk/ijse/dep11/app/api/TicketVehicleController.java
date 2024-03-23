package lk.ijse.dep11.app.api;


import lk.ijse.dep11.app.to.VehicleTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin
@Validated
public class TicketVehicleController {

    @Autowired
    private EntityManager em;
    @ExceptionHandler(ConstraintViolationException.class)
    public String exceptionHandler(ConstraintViolationException exp) {
        ResponseStatusException resExp = new ResponseStatusException(HttpStatus.BAD_REQUEST, exp.getMessage());
        exp.initCause(resExp);
        throw resExp;
    }

    @GetMapping(params = "status",produces = "application/json")
    public List<VehicleTO> getAllVehiclesByStatus(@Pattern(regexp = "^[A-Z0-9-]{1,10}$", message = "q only can contains capital letters,numbers and hyphen") String q, @Pattern(regexp = "^(in|out)$", message = "Status should be in or out") String status){
        if(q == null) q = "";
        q = "%" + q + "%";
        em.getTransaction().begin();
        List<VehicleTO> vehicleList = new ArrayList<>();
        try{
            Query query = em.createNativeQuery("SELECT parking_no,reg_no,contact,category,status,charge.charge_per_hour charge_per_hour,in_time,out_time FROM " +
                    "(SELECT vehicle.parking_no parking_no, vehicle.reg_no reg_no, vehicle.contact contact, vehicle.category category,\n" +
                    "       vehicle.status status,vehicle.in_time in_time,check_out.out_time out_time FROM vehicle LEFT OUTER JOIN check_out ON vehicle.parking_no = check_out.parking_no) v\n" +
                    "    INNER JOIN charge ON v.category = charge.vehicle_category WHERE status = '" + status + "' AND reg_no LIKE '" + q + "'", Tuple.class);

            List<Tuple> resultList = query.getResultList();

            for (Tuple tuple : resultList) {
                Integer parkingNo = (Integer) tuple.get("parking_no");
                String regNo = (String) tuple.get("reg_no");
                String contact = (String) tuple.get("contact");
                String category = (String) tuple.get("category");
                String st = (String) tuple.get("status");
                BigDecimal chargePerHour = (BigDecimal) tuple.get("charge_per_hour");
                Timestamp inTime = (Timestamp) tuple.get("in_time");
                Timestamp outTime = null;
                if (status.equalsIgnoreCase("out")) outTime = (Timestamp) tuple.get("out_time");
                vehicleList.add(new VehicleTO(parkingNo,regNo,contact,category,st,chargePerHour,inTime,outTime));
            }
            em.getTransaction().commit();
            return vehicleList;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    @GetMapping(path = "/{vehicle_no}",params = "status")
    public VehicleTO getVehicleByStatus(@PathVariable("vehicle_no") String vehicleNo, @Pattern(regexp = "^(in|out)$") String status){
        System.out.println("getVehicleByStatus()");
        return null;
    }

    @PostMapping("/in")
    public void addVehicleToPark(){
        System.out.println("addVehicle()");
    }

    @PostMapping("/out")
    public void removeVehicleFromPark(){
        System.out.println("removeVehicleFromPark()");
    }
}

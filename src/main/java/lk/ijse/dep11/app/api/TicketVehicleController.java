package lk.ijse.dep11.app.api;


import lk.ijse.dep11.app.entity.Charge;
import lk.ijse.dep11.app.entity.CheckOut;
import lk.ijse.dep11.app.entity.Vehicle;
import lk.ijse.dep11.app.to.VehicleTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin
@Validated
public class TicketVehicleController {

    @Autowired
    private EntityManager em;

    @Autowired
    private ModelMapper mapper;

    @ExceptionHandler(ConstraintViolationException.class)
    public String exceptionHandlerForConstraintViolationException(ConstraintViolationException exp) {
        ResponseStatusException resExp = new ResponseStatusException(HttpStatus.BAD_REQUEST, exp.getMessage());
        exp.initCause(resExp);
        throw resExp;
    }
    @ExceptionHandler(NoResultException.class)
    public String exceptionHandlerForNoResultException(NoResultException exp) {
        ResponseStatusException resExp = new ResponseStatusException(HttpStatus.BAD_REQUEST, exp.getMessage());
        exp.initCause(resExp);
        throw resExp;
    }

    @GetMapping(params = "status",produces = "application/json")
    public List<VehicleTO> getAllVehiclesByStatus(@Pattern(regexp = "^[A-Za-z0-9-]{1,10}$", message = "Invalid vehicle number") String q,
                                                  @Pattern(regexp = "^(in|out)$", message = "Invalid status") String status){
        if(q == null) q = "";
        q = "%" + q.toUpperCase() + "%";
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
                String outTime = null;
                if (status.equalsIgnoreCase("out")){
                    outTime = convertTimeToString((Timestamp) tuple.get("out_time"));
                }
                vehicleList.add(new VehicleTO(parkingNo,regNo,contact,category,st,chargePerHour,convertTimeToString(inTime),outTime));
            }
            em.getTransaction().commit();
            return vehicleList;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    @GetMapping(path = "/{vehicle_no}",params = "status",produces = "application/json")
    public VehicleTO getVehicleByStatus(@PathVariable("vehicle_no")
                                            @Pattern(regexp = "^[A-Za-z0-9-]{1,10}$", message = "Invalid vehicle number") String vehicleNo,
                                        @Pattern(regexp = "^(in|out)$",message = "Invalid status") String status){

        em.getTransaction().begin();
        try{
            Query query = em.createNativeQuery("SELECT parking_no,reg_no,contact,category,status,charge.charge_per_hour charge_per_hour,in_time,out_time FROM " +
                    "(SELECT vehicle.parking_no parking_no, vehicle.reg_no reg_no, vehicle.contact contact, vehicle.category category,\n" +
                    "       vehicle.status status,vehicle.in_time in_time,check_out.out_time out_time FROM vehicle LEFT OUTER JOIN check_out ON vehicle.parking_no = check_out.parking_no) v\n" +
                    "    INNER JOIN charge ON v.category = charge.vehicle_category WHERE status = '" + status + "' AND reg_no LIKE '" + vehicleNo.toUpperCase() + "'" +
                    " ORDER BY parking_no DESC LIMIT 1", Tuple.class);

            Tuple result = (Tuple) query.getSingleResult();
            Integer parkingNo = (Integer) result.get("parking_no");
            String regNo = (String) result.get("reg_no");
            String contact = (String) result.get("contact");
            String category = (String) result.get("category");
            String st = (String) result.get("status");
            BigDecimal chargePerHour = (BigDecimal) result.get("charge_per_hour");
            Timestamp inTime = (Timestamp) result.get("in_time");
            String outTime = null;
            if (status.equalsIgnoreCase("out")) {
                outTime = convertTimeToString((Timestamp) result.get("out_time"));
            }
            VehicleTO vehicleObj = new VehicleTO(parkingNo, regNo, contact, category, st, chargePerHour, convertTimeToString(inTime), outTime);
            em.getTransaction().commit();
            return vehicleObj;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    @PostMapping(produces = "application/json",consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleTO addVehicleToPark(@RequestBody VehicleTO vehicleTO){
        em.getTransaction().begin();
        try {
            if (getParkingNumber(vehicleTO.getRegistrationNumber()) != -1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The vehicle already in the parking");
            Charge chargeObj = em.find(Charge.class, vehicleTO.getCategory());
            Vehicle vehicle = new Vehicle(vehicleTO.getRegistrationNumber().toUpperCase(), vehicleTO.getContact(), vehicleTO.getCategory(),
                    "in", Timestamp.valueOf(LocalDateTime.now()));
            em.persist(vehicle);
            vehicleTO = mapper.map(vehicle, VehicleTO.class);
            vehicleTO.setChargePerHour(chargeObj.getChargePerHour());
            em.getTransaction().commit();
            return vehicleTO;

        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    @GetMapping(path = "/out/{vehicle_no}",produces = "application/json")
    public VehicleTO removeVehicleFromPark(@PathVariable("vehicle_no") @Pattern(regexp = "^[A-Za-z0-9-]{1,10}$",
            message = "Invalid vehicle number") String vehicleNo){
        em.getTransaction().begin();
        try {
            int parkingNumber = getParkingNumber(vehicleNo);
            if (parkingNumber == -1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no vehicle in the parking for relevant registration number");
            Vehicle vehicle = em.find(Vehicle.class, parkingNumber);
            vehicle.setStatus("out");
            Charge charge = em.find(Charge.class, vehicle.getCategory());
            CheckOut checkOut = new CheckOut(vehicle, Timestamp.valueOf(LocalDateTime.now()), charge.getChargePerHour());
            em.persist(checkOut);
            VehicleTO vehicleTO = mapper.map(vehicle, VehicleTO.class);  //Todo - type miss-match can happen
            vehicleTO.setChargePerHour(charge.getChargePerHour());
            vehicleTO.setOutTime(convertTimeToString(checkOut.getOutTime()));
            em.getTransaction().commit();
            System.out.println(vehicleTO);
            return vehicleTO;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    private int getParkingNumber(String vehicleNo){
        try {
            Query query = em.createNativeQuery("SELECT parking_no FROM vehicle WHERE status = 'in' AND reg_no = '" + vehicleNo + "'",Tuple.class);
            Tuple result = (Tuple) query.getSingleResult();
            return  (int) result.get("parking_no");
        }catch (Throwable t){
            return -1;
        }
    }

    private String convertTimeToString(Timestamp time){
        LocalDateTime localTime = time.toLocalDateTime();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        return localTime.format(pattern);
    }
}

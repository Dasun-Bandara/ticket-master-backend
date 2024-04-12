package lk.ijse.dep11.app.api;

import lk.ijse.dep11.app.entity.Charge;
import lk.ijse.dep11.app.to.ChargeTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/chargers")
@CrossOrigin
@Validated
public class TicketChargeController {

    @Autowired
    private EntityManager em;

    @ExceptionHandler(ConstraintViolationException.class)
    public String exceptionHandlerForConstraintViolationException(ConstraintViolationException exp) {
        ResponseStatusException resExp = new ResponseStatusException(HttpStatus.BAD_REQUEST, exp.getMessage());
        exp.initCause(resExp);
        throw resExp;
    }

    @GetMapping
    public List<ChargeTO> getAllChargers(){
        em.getTransaction().begin();
        List<ChargeTO> chargersList = new ArrayList<>();
        try {
            Query query = em.createNativeQuery("SELECT vehicle_category, charge_per_hour FROM charge ORDER BY charge_per_hour", Tuple.class);
            List<Tuple> resultList = query.getResultList();
            for (Tuple tuple : resultList) {
                chargersList.add(new ChargeTO((String) tuple.get("vehicle_category"),(BigDecimal) tuple.get("charge_per_hour")));
            }
            em.getTransaction().commit();
            return chargersList;
        }catch (Throwable t){
            em.getTransaction().rollback();
            return null;
        }
    }
    @PatchMapping(path = "/{vehicle_category}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAllChargers(@PathVariable("vehicle_category")
                                      @Pattern(regexp = "^(MOTOR_BIKE|THREE_WHEEL|CAR|BUS|OTHER)$",message = "Invalid vehicle category") String vehicleCategory,
                                  @RequestBody ChargeTO charge){
        em.getTransaction().begin();
        try{
            Charge chargeObj = em.find(Charge.class, vehicleCategory);
            chargeObj.setChargePerHour(charge.getChargePerHour());
            em.getTransaction().commit();
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw t;
        }
    }

    @GetMapping(path = "/{vehicle_category}")
    public ChargeTO updateAllChargers(@PathVariable("vehicle_category") @Pattern(regexp = "^(MOTOR_BIKE|THREE_WHEEL|CAR|BUS|OTHER)$",message = "Invalid vehicle category") String vehicleCategory){
        em.getTransaction().begin();
        try {
            Query query = em.createNativeQuery("SELECT vehicle_category, charge_per_hour FROM charge WHERE vehicle_category = '" + vehicleCategory + "'", Tuple.class);
            Tuple result = (Tuple) query.getSingleResult();
            ChargeTO chargeObj = new ChargeTO((String) result.get("vehicle_category"), (BigDecimal) result.get("charge_per_hour"));
            em.getTransaction().commit();
            return chargeObj;
        }catch (Throwable t){
            em.getTransaction().rollback();
            return null;
        }
    }
}

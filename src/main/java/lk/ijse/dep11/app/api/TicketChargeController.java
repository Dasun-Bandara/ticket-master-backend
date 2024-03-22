package lk.ijse.dep11.app.api;

import lk.ijse.dep11.app.to.ChargeTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/chargers")
public class TicketChargeController {

    @GetMapping
    public List<ChargeTO> getAllChargers(){
        System.out.println("getAllChargers()");
        return null;
    }

    @PutMapping
    public void updateAllChargers(){
        System.out.println("updateAllChargers()");
    }
}

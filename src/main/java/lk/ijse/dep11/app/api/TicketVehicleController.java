package lk.ijse.dep11.app.api;


import lk.ijse.dep11.app.to.VehicleTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class TicketVehicleController {

    @GetMapping(params = "status")
    public List<VehicleTO> getAllVehiclesByStatus(String q, @Pattern(regexp = "^(in|out)$") String status){
        System.out.println("getAllVehiclesByStatus()");
        return null;
    }

    @GetMapping(path = "/{vehicle_no}",params = "status")
    public VehicleTO getVehicleByStatus(@PathVariable("vehicle_no") String vehicleNo, @Pattern(regexp = "^(in|out)$") String status){
        System.out.println("getVehicleByStatus()");
        return null;
    }

    @PostMapping
    public void addVehicle(){
        System.out.println("addVehicle()");
    }
}

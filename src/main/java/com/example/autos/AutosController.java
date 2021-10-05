package com.example.autos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/autos")
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @PostMapping
    public Automobile addAutomobile(@RequestBody Automobile automobile){
        return autosService.addAutomobile(automobile);
    }

    @GetMapping
    public AutosList searchAutos(@RequestParam(defaultValue = "") String color){
        return autosService.searchAutos(color);
    }

    @GetMapping("/{vin}")
    public Automobile getAutomobile(@PathVariable String vin){
        return autosService.getAutomobile(vin);
    }

    @PatchMapping("/{vin}")
    public Automobile patchAutomobile(@PathVariable String vin, @RequestBody Map<String, String> customer){
        return autosService.patch(vin, customer.get("name"), customer.get("phone"));
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<String> deleteAuto(@PathVariable String vin){
        if (autosService.deleteAuto(vin)){
            return ResponseEntity.ok("Deleted "+vin);
        }else{
            return ResponseEntity.noContent().build();
        }
    }
}

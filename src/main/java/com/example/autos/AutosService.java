package com.example.autos;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutosService {

    private final AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public Automobile addAutomobile(Automobile automobile) {
        return autosRepository.save(automobile);
    }

    public AutosList searchAutos(String color) {
        List<Automobile> autos = autosRepository.findAutomobilesByColorContains(color);
        return new AutosList(autos);
    }

    public Automobile getAutomobile(String vin) {
        return autosRepository.findAutomobileByVin(vin);
    }

    public Automobile patch(String vin, String owner, String phone) {
        Automobile automobile = autosRepository.findAutomobileByVin(vin);
        if (automobile != null){
            automobile.setOwnerName(owner);
            automobile.setOwnerPhone(phone);
            automobile.setStatus("sold");
            return autosRepository.save(automobile);
        }else{
            return null;
        }
    }

    public boolean deleteAuto(String vin) {
        Automobile automobile = autosRepository.findAutomobileByVin(vin);
        if (automobile!=null) {
            autosRepository.delete(automobile);
            return true;
        }else{
            return false;
        }
    }
}

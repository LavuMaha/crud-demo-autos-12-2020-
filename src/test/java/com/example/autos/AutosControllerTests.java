package com.example.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void postNewAuto_returnsDetails() throws Exception {
        //Receive the request
        //Call service layer to add the record to the db
        //Return the record to user
        // Arrange
        Automobile automobile = new Automobile();
        automobile.setVin("ABC123XX");
        automobile.setYear("1995");
        automobile.setMake("Ford");
        automobile.setModel("Windstar");
        automobile.setColor("Blue");

        when(autosService.addAutomobile(any(Automobile.class)))
                .thenReturn(automobile);

        mockMvc.perform(post("/autos").contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(automobile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value("ABC123XX"));

    }

    @Test
    void searchAutos_all_returnsList() throws Exception {
        when(autosService.searchAutos(anyString())).thenReturn(new AutosList(sampleAutos(5)));

        mockMvc.perform(get("/autos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    @Test
    void getAuto_returnsDetails() throws Exception {
        when(autosService.getAutomobile(anyString())).thenReturn(sampleAutos(1).get(0));

        mockMvc.perform(get("/autos/ANYVIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value("ABC123XX0"));
    }

    @Test
    void patchAuto_setsStatuToSold() throws Exception {
        Automobile automobile = sampleAutos(1).get(0);
        automobile.setStatus("sold");
        automobile.setOwnerName("owner");
        automobile.setOwnerPhone("phone");

        when(autosService.patch(anyString(), anyString(), anyString()))
                .thenReturn(automobile);

        mockMvc.perform(patch("/autos/"+automobile.getVin()).contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"owner\",\"phone\":\"phone\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("sold"));
    }

    @Test
    void delteAuto_valid() throws Exception {
        when(autosService.deleteAuto(anyString())).thenReturn(true);

        mockMvc.perform(delete("/autos/ANYVIN"))
                .andExpect(status().isOk());
    }

    @Test
    void delteAuto_notValid() throws Exception {
        when(autosService.deleteAuto(anyString())).thenReturn(false);

        mockMvc.perform(delete("/autos/ANYVIN"))
                .andExpect(status().isNoContent());
    }
    private List<Automobile> sampleAutos(int qty){
        List<Automobile> automobiles = new ArrayList<>();
        Automobile auto;
        for (int i = 0; i < qty; i++) {
            auto = new Automobile();
            auto.setVin("ABC123XX"+i);
            auto.setYear("190"+i);
            auto.setMake("Make-"+i);
            auto.setModel("Model-"+i);
            auto.setColor(i%2==0 ? "Red" : "Green");
            automobiles.add(auto);
        }
        return automobiles;
    }
}

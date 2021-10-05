package com.example.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {

    @Mock
    private AutosRepository autosRepository;

    private AutosService autosService;

    @BeforeEach
    void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void addAuto_returnsDetails() {
        Automobile expected = new Automobile();
        expected.setVin("ABC123XX");
        when(autosRepository.save(any(Automobile.class))).thenReturn(expected);

        Automobile actual = autosService.addAutomobile(expected);

        assertThat(actual).isNotNull();
        assertThat(actual.getVin()).isEqualTo(expected.getVin());

    }

    @Test
    void searchAutos_all_returnsList() {
        when(autosRepository.findAutomobilesByColorContains(anyString())).thenReturn(sampleAutos(5));

        AutosList actual = autosService.searchAutos("");

        assertThat(actual).isNotNull();
        assertThat(actual.getAutomobiles().size()).isGreaterThan(0);
    }

    @Test
    void getAutoByVin_returnsDetails() {
        when(autosRepository.findAutomobileByVin(anyString())).thenReturn(sampleAutos(1).get(0));

        Automobile actual = autosService.getAutomobile("AAA");

        assertThat(actual).isNotNull();
    }

    @Test
    void patchAuto_returnsDetails() {
        Automobile original = sampleAutos(1).get(0);
        original.setOwnerPhone("123");
        original.setOwnerName("name");
        original.setStatus("Sold");
        when(autosRepository.findAutomobileByVin(anyString())).thenReturn(original);
        when(autosRepository.save(any(Automobile.class))).thenReturn(original);

        Automobile actual = autosService.patch(original.getVin(), original.getOwnerName(), original.getOwnerPhone());

        assertThat(actual).isNotNull();
    }

    @Test
    void deleteAuto_returnsTrue() {
        Automobile expected = sampleAutos(1).get(0);
        when(autosRepository.findAutomobileByVin(anyString())).thenReturn(expected);

        autosService.deleteAuto(expected.getVin());

        verify(autosRepository, atLeastOnce()).delete(any(Automobile.class));
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
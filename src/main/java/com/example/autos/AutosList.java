package com.example.autos;

import java.util.List;

public class AutosList {
    List<Automobile> automobiles;

    public AutosList() {
    }

    public AutosList(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }

    public List<Automobile> getAutomobiles() {
        return automobiles;
    }

    public void setAutomobiles(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }
}

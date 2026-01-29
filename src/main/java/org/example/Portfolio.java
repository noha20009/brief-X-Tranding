package org.example;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private List<Asset<?>> listAsset = new ArrayList<>();

    public Portfolio(List<Asset<?>> listAsset, int qantité) {
        this.listAsset = listAsset;

    }

    public void addAsset(Asset<?> asset) {
        if (asset == null) {
            throw new IllegalArgumentException("l'asset ne peut pas étre null");
            listAsset.add(asset);
        }
        public double getTotalValue(){
            double total = 0;
            for (Asset<?> asset : listAsset) {
                total += asset.getValue();
            }
            return total;
        }
    }
}



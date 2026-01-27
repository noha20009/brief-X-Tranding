package org.example;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
   private List<Asset> listAsset;
   private int qantité;

    public Portfolio(List<Asset> listAsset, int qantité) {
        this.listAsset = new ArrayList<>();
        this.qantité = qantité;
    }

    public List<Asset> getListAsset() {
        return listAsset;
    }

    public int getQantité() {
        return qantité;
    }

    public void setListAsset(List<Asset> listAsset) {
        this.listAsset = listAsset;
    }

    public void setQantité(int qantité) {
        this.qantité = qantité;
    }


}

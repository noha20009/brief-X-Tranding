package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio<T extends Asset> {
    private Map<T,Integer> assets;

    public Map<T, Integer> getAssets() {
        return assets;
    }

    public Portfolio() {
        this.assets = new HashMap<>();
    }

    public void ajouterAsset(T asset,int quantite){
        if (quantite<=0) throw new IllegalArgumentException("la quantite doit etre >0");
        assets.put(asset,assets.getOrDefault(asset,0)+ quantite);
    }
    public boolean retirerAsset(T asset,int quantite) {
        if (!assets.containsKey(asset) || assets.get(asset) < quantite) return false;
        int newQuantity = assets.get(asset) - quantite;
        if (newQuantity == 0) assets.remove(asset);
        else assets.put(asset, newQuantity);
        return true;
    }
    // Vérifier si l'actif est détenu avec quantité suffisante
    public boolean hasAsset(T asset, int quantite){
        return assets.containsKey(asset) && assets.get(asset) >=quantite;
    }
        public double  getTotalvalue(){
           return assets.entrySet().stream().mapToDouble(entry->entry.getKey().getPrixUnitaire()* entry.getValue()).sum();
        }
}






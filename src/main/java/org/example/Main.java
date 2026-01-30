package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Market market = Market.getInstance();
        boolean  choix= true;
        while (choix){
            System.out.println("\n======Menu Principal");
            System.out.println("1.Mode Admin");
            System.out.println("2.Mode Trader");
            System.out.println("0.Quitter");
            System.out.println("choix : ");
            int choice = scan.nextInt();
            switch (choice){
                case 1:
                    AdmniMenu(scan,market);
                    break;
                case 2:
                    TraderMenu(scan,market);
                    break;
                case 0:
                    choix=false;
                    System.out.println("merci");
                    break;
                default:
                    System.out.println("choix invalide");

            }
        }
        scan.close();
    }
    public static void AdmniMenu(Scanner scan,Market market){
        boolean admin =true;
        while (admin){
            System.out.println("\n =====Mode Admin====");
            System.out.println("1.ajouter trader");
            System.out.println("2.ajouter stock");
            System.out.println("3.ajouter Crypto");
            System.out.println("4.afficher Assets");
            System.out.println("5.afficher traders");
            System.out.println("0.retour");
            System.out.println("choix");
            int choice = scan.nextInt();
            switch (choice){
                case 1:
                    System.out.println("id trader");
                    int id = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Nom");
                    String nom = scan.nextLine();
                    System.out.println("solde initiale");
                    double solde = scan.nextDouble();
                    Trader trader=new Trader(id,nom,solde,new Portfolio<>());
                    market.ajouterTrader(trader);
                    System.out.println("trader ajouté ");
                    break;
                case 2:
                    System.out.println("id stock");
                    int idStock = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Nom");
                    String nomStock = scan.nextLine();
                    System.out.println("prix");
                    double prixStock = scan.nextDouble();
                    market.ajouterAsset(new Stock(idStock,nomStock,prixStock));
                    System.out.println("stock ajouter");
                    break;
                case 3:
                    System.out.println("id crypto");
                    int idCrypto = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Nom");
                    String nomCrypto = scan.nextLine();
                    System.out.println("prix");
                    double prixCrypto =scan.nextDouble();
                    market.ajouterAsset(new CryptoCurrency(idCrypto,nomCrypto,prixCrypto));
                    System.out.println("crypto ajouté");
                    break;
                case 4:
                    market.afficherActifs();
                    break;
                case 5:
                    for (Trader t: market.getTraders()){
                        System.out.println(t);
                    }
                    break;
                case 0:
                    admin=false;
                    break;
                default:
                    System.out.println("choix ivalide");

            }
        }
    }
    public static void TraderMenu(Scanner scan,Market market){
        System.out.println("entrer votre id traser");
        int traderId = scan.nextInt();
        Trader trader = market.trouverTrader(traderId);
        if (trader==null){
            System.out.println("trader introuvable");
            return;
        }
        boolean traderMode =true;
        while (traderMode){
            System.out.println("\n ====Mode Trader===");
            System.out.println("1. acheter asset");
            System.out.println("2. vendre asset");
            System.out.println("3. voir portfolio");
            System.out.println("4. voir transactions");
            System.out.println("5. voir solde");
            System.out.println("0. sortie");
            int choice =scan.nextInt();
            switch (choice){
                case 1:
                    System.out.println("id asset : ");
                    int idAssetachet = scan.nextInt();
                    System.out.println("quantité : ");
                    int qAchet = scan.nextInt();
                    market.acheterAsset(traderId ,idAssetachet,qAchet);
                    System.out.println("achet effectué");
                    break;
                case 2:
                    System.out.println("id asset : ");
                    int idVendre = scan.nextInt();
                    System.out.println("quantité :");
                    int qVendre = scan.nextInt();
                    market.vendreAsset(traderId,idVendre,qVendre);
                    break;
                case 3:
                    market.displayTransactions();
                    break;
                case 4:
                    System.out.println("solde actuel" +trader.getBalance());
                    break;
                case 0:
                    traderMode=false;
                    break;
                default:
                    System.out.println("choix invalide");

            }
        }
    }
}

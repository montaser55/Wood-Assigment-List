/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author User
 */
public class Hw3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        LinkedList<oak> OakList=new LinkedList<>();
        LinkedList<maple> MapleList= new LinkedList<>();
        
        String inputLine;
        String[] inputValues;
        int promotionApplied=0;
        String inputpath="src\\hw3\\input.txt";
        BufferedReader reader = new BufferedReader(new FileReader(inputpath));
        
        inputLine=reader.readLine();
        while((inputLine=reader.readLine())!=null){
            inputValues=inputLine.split("\\s+");
            
            switch (inputValues[0]) {
                case "R":
                    ReceiveWood(inputValues,OakList,MapleList);
                    break;
                case "S":
                    SaleWood(inputValues,OakList,MapleList,promotionApplied);
                    promotionApplied=0;
                    break;
                case "P":
                    promotionApplied = PromotionApply(inputValues);
                    break;
                default:
                    break;
            }
        }
        
        printRemainingOak(OakList);
        printRemainingMaple(MapleList);
    }

    private static void ReceiveWood(String[] inputValues, LinkedList<oak> OakList, LinkedList<maple> MapleList) {
        if(inputValues[1].equals("O")){
            ReceiveOakWood(inputValues,OakList);
        }
        else if(inputValues[1].equals("C")){
            ReceiveMapleWood(inputValues,MapleList);
        }
    }
    
    private static void ReceiveOakWood(String[] inputValues, LinkedList<oak> OakList) {
        float[] amountPriceArray=GetAmountAndPrice(inputValues);
        int amount=(int)amountPriceArray[0];
        float price=amountPriceArray[1];
                
        oak OakNew=new oak(amount,price);
        OakList.add(OakNew);
        
        printReceipt("Oak",amount,price);
    }

    private static void ReceiveMapleWood(String[] inputValues, LinkedList<maple> MapleList) {
        float[] amountPriceArray=GetAmountAndPrice(inputValues);
        int amount=(int)amountPriceArray[0];
        float price=amountPriceArray[1];
                
        maple MapleNew=new maple(amount,price);
        MapleList.add(MapleNew);
        
        printReceipt("Maple",amount,price);
    }
    
    private static float[] GetAmountAndPrice(String[] inputValues) {
        String amountString=inputValues[2];
        String priceString=inputValues[3];
        priceString=priceString.substring(1,priceString.length()-1);
        
        float amount=Float.parseFloat(amountString);
        float price=Float.parseFloat(priceString);
        
        return new float[]{amount,price};
    }
    
    private static void printReceipt(String wood, int amount, float price) {
        System.out.println("Received "+wood+" wood, amount= "+amount+", unit price= $"+price+".");
        System.out.println("");
    }

    private static void SaleWood(String[] inputValues, LinkedList<oak> OakList, LinkedList<maple> MapleList, int promotionApplied) {
        if(inputValues[1].equals("O")){
            SaleOakWood(inputValues,OakList,promotionApplied);
        }
        else if(inputValues[1].equals("C")){
            SaleMapleWood(inputValues,MapleList,promotionApplied);
        }
    }
    
    private static void SaleOakWood(String[] inputValues, LinkedList<oak> OakList, int promotionApplied) {
        int saleAmount=findSaleAmount(inputValues);
        List<wood> OakPrintList=new ArrayList<>();
        oak oakSale;
        float markupTotal=(float) 1.4;
        
        while(saleAmount>0 && OakList.size()>0){
            int amountSold=0;
            oakSale=OakList.getFirst();
            if(oakSale.amount<=saleAmount){
                OakList.removeFirst();
                amountSold=oakSale.amount;
            }
            else if(oakSale.amount>saleAmount){
                int remaining=oakSale.amount-saleAmount;
                amountSold=saleAmount;
                float price=oakSale.price;
                oak OakUpdated=new oak(remaining,price);
                OakList.set(0,OakUpdated);
            }
            saleAmount=saleAmount-amountSold;
            
            float priceSold=oakSale.price*markupTotal;
            oak OakNew=new oak(amountSold,priceSold); 
            OakPrintList.add(OakNew);
        }
        
        printSale(OakPrintList,saleAmount,promotionApplied,"O");
        
    }

    private static void SaleMapleWood(String[] inputValues, LinkedList<maple> MapleList, int promotionApplied) {
        int saleAmount=findSaleAmount(inputValues);
        List<wood> MaplePrintList=new ArrayList<>();
        maple mapleSale;
        float markupTotal=(float) 1.4;
        
        while(saleAmount>0 && MapleList.size()>0){
            int amountSold=0;
            mapleSale=MapleList.getFirst();
            if(mapleSale.amount<=saleAmount){
                MapleList.removeFirst();
                amountSold=mapleSale.amount;
            }
            else if(mapleSale.amount>saleAmount){
                int remaining=mapleSale.amount-saleAmount;
                amountSold=saleAmount;
                float price=mapleSale.price;
                maple MapleUpdated=new maple(remaining,price);
                MapleList.set(0,MapleUpdated);
            }
            saleAmount=saleAmount-amountSold;
            
            float priceSold=mapleSale.price*markupTotal;
            maple MapleNew=new maple(amountSold,priceSold); 
            MaplePrintList.add(MapleNew);
        }
        
        printSale(MaplePrintList,saleAmount,promotionApplied,"C");
        
    }
    
    private static void printSale(List<wood> PrintList, int saleRemaining, int promotionApplied,String woodType) {
        int totalSold=0;
        float totalPrice=0;
        for(int i=0;i<PrintList.size();i++){
            totalSold+=PrintList.get(i).amount;
        }
        System.out.println(totalSold+" Widgets sold");
        for(int i=0;i<PrintList.size();i++){
            wood Wood=PrintList.get(i);
            float price=Wood.amount*Wood.price;
            totalPrice+=price;
            System.out.printf("\t"+Wood.amount+" at "+Wood.price+" each\t Sales: $%.2f\n",price);
        }
        System.out.printf("\t\t\tTotal Sale:  $%.2f\n",totalPrice);
        if(promotionApplied>0){
            System.out.println("\t\t\tDiscount:  "+promotionApplied+"%");
            float promotionPercent=(float)(promotionApplied/100.0);
            float discountPrice=totalPrice*promotionPercent;
            totalPrice-=discountPrice;
            System.out.printf("\t\t\t\t: -%.2f\n",discountPrice);
            System.out.printf("\t\t\tTotal Sale:  $%.2f\n",totalPrice);
            
        }
        if(saleRemaining>0){
            if (woodType=="O"){
            System.out.println("Remainder of "+saleRemaining+" pieces of Oak wood not available.");
            }
            else if(woodType=="C"){
            System.out.println("Remainder of "+saleRemaining+" pieces of Cherry Maple wood not available.");
            }
        }
        System.out.println("");
    }
    
    private static int findSaleAmount(String[] inputValues) {
        String saleAmountString=inputValues[2];
        int saleAmount=Integer.parseInt(saleAmountString);
        return saleAmount;
    }

    private static int PromotionApply(String[] inputValues) {
        String promotionString=inputValues[1];
        promotionString=promotionString.substring(0, promotionString.length()-1);
        int promotionValue=Integer.parseInt(promotionString);
        printPromotion(promotionValue);
        return promotionValue;
    }

    private static void printPromotion(int promotionValue) {
        System.out.println("Promotion "+promotionValue+"% applied for next sale.");
        System.out.println("");
    }

    private static void printRemainingOak(LinkedList<oak> OakList) {
        if(OakList!=null){
            System.out.println("Remaining Oak Wood:");
            oak OakNew;
            for(int i=0;i<OakList.size();i++){
                OakNew=OakList.get(i);
                System.out.println("\t "+OakNew.amount+" woods of price $"+OakNew.price);
            }
            System.out.println("");
        }
    }

    private static void printRemainingMaple(LinkedList<maple> MapleList) {
        if(MapleList!=null){
            System.out.println("Remaining Cherry Maple Wood:");
            maple MapleNew;
            for(int i=0;i<MapleList.size();i++){
                MapleNew=MapleList.get(i);
                System.out.println("\t "+MapleNew.amount+" woods of price $"+MapleNew.price);
            }
            System.out.println("");
        }
    }

    

    
    
}

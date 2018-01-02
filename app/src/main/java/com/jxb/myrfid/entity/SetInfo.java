package com.jxb.myrfid.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxb on 2017-11-20.
 */

public class SetInfo {

    private String test;
    private List<RFIDInfoEx> Instruments = new ArrayList<>();

    public List<RFIDInfoEx> getInstruments() {
        return Instruments;
    }
    public void setInstruments(List<RFIDInfoEx> instruments) {
        Instruments = instruments;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "SetInfo{" +
                "test='" + test + '\'' +
                //", Instruments=" + Instruments +
                '}';
    }

    public static class RFIDInfoEx {

        private String id;
        private String RFID;
        private String Code;
        private String Sort;
        private String Code_P;
        private String Img;
        private String Count;
        private String CheckIn;
        private String OutStock;
        private String Stock;
        private String CheckOut;
        private String Sterilization;
        private String Package;
        private String Wash;
        private String Factory;
        private String ValidTime;
        private String ProductionDate;
        private String PruchaseDate;
        private String Material;
        private String Origin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRFID() {
            return RFID;
        }

        public void setRFID(String RFID) {
            this.RFID = RFID;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public String getSort() {
            return Sort;
        }

        public void setSort(String sort) {
            Sort = sort;
        }

        public String getCode_P() {
            return Code_P;
        }

        public void setCode_P(String code_P) {
            Code_P = code_P;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String img) {
            Img = img;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }

        public String getCheckIn() {
            return CheckIn;
        }

        public void setCheckIn(String checkIn) {
            CheckIn = checkIn;
        }

        public String getOutStock() {
            return OutStock;
        }

        public void setOutStock(String outStock) {
            OutStock = outStock;
        }

        public String getStock() {
            return Stock;
        }

        public void setStock(String stock) {
            Stock = stock;
        }

        public String getCheckOut() {
            return CheckOut;
        }

        public void setCheckOut(String checkOut) {
            CheckOut = checkOut;
        }

        public String getSterilization() {
            return Sterilization;
        }

        public void setSterilization(String sterilization) {
            Sterilization = sterilization;
        }

        public String getPackage() {
            return Package;
        }

        public void setPackage(String aPackage) {
            Package = aPackage;
        }

        public String getWash() {
            return Wash;
        }

        public void setWash(String wash) {
            Wash = wash;
        }

        public String getFactory() {
            return Factory;
        }

        public void setFactory(String factory) {
            Factory = factory;
        }

        public String getValidTime() {
            return ValidTime;
        }

        public void setValidTime(String validTime) {
            ValidTime = validTime;
        }

        public String getProductionDate() {
            return ProductionDate;
        }

        public void setProductionDate(String productionDate) {
            ProductionDate = productionDate;
        }

        public String getPruchaseDate() {
            return PruchaseDate;
        }

        public void setPruchaseDate(String pruchaseDate) {
            PruchaseDate = pruchaseDate;
        }

        public String getMaterial() {
            return Material;
        }

        public void setMaterial(String material) {
            Material = material;
        }

        public String getOrigin() {
            return Origin;
        }

        public void setOrigin(String origin) {
            Origin = origin;
        }

        @Override
        public String toString() {
            return "RFIDInfoEx{" +
                    "id='" + id + '\'' +
                    ", RFID='" + RFID + '\'' +
                    ", Code='" + Code + '\'' +
                    ", Sort='" + Sort + '\'' +
                    ", Code_P='" + Code_P + '\'' +
                    ", Img='" + Img + '\'' +
                    ", Count='" + Count + '\'' +
                    ", CheckIn='" + CheckIn + '\'' +
                    ", OutStock='" + OutStock + '\'' +
                    ", Stock='" + Stock + '\'' +
                    ", CheckOut='" + CheckOut + '\'' +
                    ", Sterilization='" + Sterilization + '\'' +
                    ", Package='" + Package + '\'' +
                    ", Wash='" + Wash + '\'' +
                    ", Factory='" + Factory + '\'' +
                    ", ValidTime='" + ValidTime + '\'' +
                    ", ProductionDate='" + ProductionDate + '\'' +
                    ", PruchaseDate='" + PruchaseDate + '\'' +
                    ", Material='" + Material + '\'' +
                    ", Origin='" + Origin + '\'' +
                    '}';
        }
    }
}

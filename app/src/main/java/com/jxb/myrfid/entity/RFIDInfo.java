package com.jxb.myrfid.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jxb on 2017-11-20.
 */
@Entity(indexes = {
        @Index(value = "RFID, sort DESC", unique = true)
})

public class RFIDInfo {
    @Id
    private Long id;

    @NotNull
    private String RFID;

    private String code;
    private String sort;
    private String code_p;
    private String img;
    private String count;
    private Date checkin;
    private Date outstock;
    private Date stock;
    private Date checkout;
    private Date Sterilization;
    private Date Package;
    private Date Wash;
    private String Factory;
    private Date ValidTime;
    private Date ProductionDate;
    private Date PruchaseDate;
    private String Material;
    private String Origin;
    private String Size;
    private String UserCode;
@Generated(hash = 271553436)
public RFIDInfo(Long id, @NotNull String RFID, String code, String sort,
        String code_p, String img, String count, Date checkin, Date outstock,
        Date stock, Date checkout, Date Sterilization, Date Package, Date Wash,
        String Factory, Date ValidTime, Date ProductionDate, Date PruchaseDate,
        String Material, String Origin, String Size, String UserCode) {
    this.id = id;
    this.RFID = RFID;
    this.code = code;
    this.sort = sort;
    this.code_p = code_p;
    this.img = img;
    this.count = count;
    this.checkin = checkin;
    this.outstock = outstock;
    this.stock = stock;
    this.checkout = checkout;
    this.Sterilization = Sterilization;
    this.Package = Package;
    this.Wash = Wash;
    this.Factory = Factory;
    this.ValidTime = ValidTime;
    this.ProductionDate = ProductionDate;
    this.PruchaseDate = PruchaseDate;
    this.Material = Material;
    this.Origin = Origin;
    this.Size = Size;
    this.UserCode = UserCode;
}
@Generated(hash = 846936959)
public RFIDInfo() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getRFID() {
    return this.RFID;
}
public void setRFID(String RFID) {
    this.RFID = RFID;
}
public String getCode() {
    return this.code;
}
public void setCode(String code) {
    this.code = code;
}
public String getSort() {
    return this.sort;
}
public void setSort(String sort) {
    this.sort = sort;
}
public String getCode_p() {
    return this.code_p;
}
public void setCode_p(String code_p) {
    this.code_p = code_p;
}
public String getImg() {
    return this.img;
}
public void setImg(String img) {
    this.img = img;
}
public String getCount() {
    return this.count;
}
public void setCount(String count) {
    this.count = count;
}
public Date getCheckin() {
    return this.checkin;
}
public void setCheckin(Date checkin) {
    this.checkin = checkin;
}
public Date getOutstock() {
    return this.outstock;
}
public void setOutstock(Date outstock) {
    this.outstock = outstock;
}
public Date getStock() {
    return this.stock;
}
public void setStock(Date stock) {
    this.stock = stock;
}
public Date getCheckout() {
    return this.checkout;
}
public void setCheckout(Date checkout) {
    this.checkout = checkout;
}
public Date getSterilization() {
    return this.Sterilization;
}
public void setSterilization(Date Sterilization) {
    this.Sterilization = Sterilization;
}
public Date getPackage() {
    return this.Package;
}
public void setPackage(Date Package) {
    this.Package = Package;
}
public Date getWash() {
    return this.Wash;
}
public void setWash(Date Wash) {
    this.Wash = Wash;
}
public String getFactory() {
    return this.Factory;
}
public void setFactory(String Factory) {
    this.Factory = Factory;
}
public Date getValidTime() {
    return this.ValidTime;
}
public void setValidTime(Date ValidTime) {
    this.ValidTime = ValidTime;
}
public Date getProductionDate() {
    return this.ProductionDate;
}
public void setProductionDate(Date ProductionDate) {
    this.ProductionDate = ProductionDate;
}
public Date getPruchaseDate() {
    return this.PruchaseDate;
}
public void setPruchaseDate(Date PruchaseDate) {
    this.PruchaseDate = PruchaseDate;
}
public String getMaterial() {
    return this.Material;
}
public void setMaterial(String Material) {
    this.Material = Material;
}
public String getOrigin() {
    return this.Origin;
}
public void setOrigin(String Origin) {
    this.Origin = Origin;
}
public String getSize() {
    return this.Size;
}
public void setSize(String Size) {
    this.Size = Size;
}
public String getUserCode() {
    return this.UserCode;
}
public void setUserCode(String UserCode) {
    this.UserCode = UserCode;
}
}

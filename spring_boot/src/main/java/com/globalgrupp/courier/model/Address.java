package com.globalgrupp.courier.model;

import javax.persistence.*;

/**
 * Created by Lenovo on 27.01.2016.
 */
@Entity
@Table(name="addresses")
public class Address {
    @Id
    @Column(name = "addresses_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="kv")
    private String kv;

    @Column(name = "street")
    private String street;

    @Column(name="house_number")
    private String houseNumber;

    @Column(name = "apartment_count")
    private Long apartmentCount;

    @Column(name = "postbox_type")
    private String postboxType;

    @Column(name = "postbox_quality")
    private String postboxQuality;

    @Column(name="house_quality")
    private String houseQuality;

    @Column(name="level_count")
    private String levelCount;

    @Column(name="porch_count")
    private Long porchCount;

    @Column(name = "city_rayon")
    private String cityRayon;

    @Column(name="rayon")
    private String rayon;

    @Column(name="house_year")
    private Long houseYear;

    @Column(name="key")
    private String key;

    @Column(name="comment")
    private String comment;

    @Column(name="last_update")
    private String lastUpdate;

    @Column(name="novostroyka")
    private String Novostroyka;

    public Address() {
    }

    public Long getApartmentCount() {
        return apartmentCount;
    }

    public void setApartmentCount(Long apartmentCount) {
        this.apartmentCount = apartmentCount;
    }

    public String getCityRayon() {
        return cityRayon;
    }

    public void setCityRayon(String cityRayon) {
        this.cityRayon = cityRayon;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseQuality() {
        return houseQuality;
    }

    public void setHouseQuality(String houseQuality) {
        this.houseQuality = houseQuality;
    }

    public Long getHouseYear() {
        return houseYear;
    }

    public void setHouseYear(Long houseYear) {
        this.houseYear = houseYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKv() {
        return kv;
    }

    public void setKv(String kv) {
        this.kv = kv;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(String levelCount) {
        this.levelCount = levelCount;
    }

    public String getNovostroyka() {
        return Novostroyka;
    }

    public void setNovostroyka(String novostroyka) {
        Novostroyka = novostroyka;
    }

    public Long getPorchCount() {
        return porchCount;
    }

    public void setPorchCount(Long porchCount) {
        this.porchCount = porchCount;
    }

    public String getPostboxQuality() {
        return postboxQuality;
    }

    public void setPostboxQuality(String postboxQuality) {
        this.postboxQuality = postboxQuality;
    }

    public String getPostboxType() {
        return postboxType;
    }

    public void setPostboxType(String postboxType) {
        this.postboxType = postboxType;
    }

    public String getRayon() {
        return rayon;
    }

    public void setRayon(String rayon) {
        this.rayon = rayon;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

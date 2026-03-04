package com.oceanview.model;

import java.math.BigDecimal;

/**
 * RoomType Model - Represents a type of hotel room with pricing.
 */
public class RoomType {
    private int id;
    private String typeName;
    private BigDecimal ratePerNight;
    private String description;

    public RoomType() {}

    public RoomType(int id, String typeName, BigDecimal ratePerNight, String description) {
        this.id = id;
        this.typeName = typeName;
        this.ratePerNight = ratePerNight;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public BigDecimal getRatePerNight() { return ratePerNight; }
    public void setRatePerNight(BigDecimal ratePerNight) { this.ratePerNight = ratePerNight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

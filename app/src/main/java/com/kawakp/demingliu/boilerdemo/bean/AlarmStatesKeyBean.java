package com.kawakp.demingliu.boilerdemo.bean;

/**
 * Created by deming.liu on 2017/1/16.
 */

public class AlarmStatesKeyBean {

    /**
     * id : krs_element_1
     * tableId : ceb6ab16297943af8af1921bf1155ace
     * number : 1
     * name : 内腔温度传感器断路
     * fieldName : L_T_TS_OC
     * conversionFactor : 0
     * valueType : BOOL
     * operateType : ALARM
     * unit :
     * level : 1
     * processMode :
     * defaultAddress : M1000
     */

    private String id;
    private String tableId;
    private int number;
    private String name;
    private String fieldName;
    private int conversionFactor;
    private String valueType;
    private String operateType;
    private String unit;
    private String level;
    private String processMode;
    private String defaultAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(int conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}

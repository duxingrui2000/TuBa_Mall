package com.tuba.mall.common;

/**
 * 分类级别
 */
public enum TuBaMallCategoryLevelEnum {

    //eg：家电数码手机属于一级，单独出家电或数码或手机属于耳机，每个属性下的各种对应商品属于三级
    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    TuBaMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static TuBaMallCategoryLevelEnum getTubaMallOrderStatusEnumByLevel(int level) {
        for (TuBaMallCategoryLevelEnum tuBaMallCategoryLevelEnum : TuBaMallCategoryLevelEnum.values()) {
            if (tuBaMallCategoryLevelEnum.getLevel() == level) {
                return tuBaMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

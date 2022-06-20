package com.example.tally.utils;

import java.math.BigDecimal;

public class FloatUtils {
    //进行除法运算，保留4位小数
    public static float div(float v1,float v2){
        float v3=v1/v2;
        BigDecimal bigDecimal = new BigDecimal(v3);
        float val = bigDecimal.setScale(4, 4).floatValue();
        return val;
    }

    //将浮点数类型转换成百分比显示形式
    public static String ratioToPercent(float val) {
        float v = val * 100;
        BigDecimal b1 = new BigDecimal(v);
        float v1 = b1.setScale(2, 4).floatValue();
        String per = v1 + "%";
        return per;
    }
}

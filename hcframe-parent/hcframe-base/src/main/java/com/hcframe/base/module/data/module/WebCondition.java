package com.hcframe.base.module.data.module;

import com.hcframe.base.module.data.constants.QueryConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author lhc
 * @date 2020-12-23
 * @decription 前端Condition
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class WebCondition {
    // key
    private String key;
    // value
    private Object value;
    // 符号
    private int sign;
    // 第二个value，只有在sign为between的时候生效
    private Object sValue;
    // 逗号隔开的value
    private String values;

    private Integer logic;

    private Integer curves;

    private List<String> valueList;

    public List<String> getValueList() {
        if (valueList == null) {
            String[] arr = this.values.split(",");
            this.valueList = Arrays.asList(arr);
        }
        return valueList;
    }

    public static Condition.ConditionBuilder setSign(WebCondition webCondition,Condition.ConditionBuilder builder) {
        switch (webCondition.getSign()) {
            case QueryConstants.LIKE:{
                return builder.like(webCondition.key, webCondition.value);
            }
            case QueryConstants.EQUAL:{
                return builder.equal(webCondition.key,webCondition.value);
            }
            case QueryConstants.BETWEEN:{
                return builder.between(webCondition.key, webCondition.value,webCondition.sValue);
            }
            case QueryConstants.GT:{
                return builder.gt(webCondition.key, webCondition.value);
            }
            case QueryConstants.GTE:{
                return builder.gte(webCondition.key, webCondition.value);
            }
            case QueryConstants.LT:{
                return builder.lt(webCondition.key, webCondition.value);
            }
            case QueryConstants.LTE:{
                return builder.lte(webCondition.key, webCondition.value);
            }
            case QueryConstants.IN:{
                String[] strings = webCondition.value.toString().split(",");
                return builder.in(webCondition.key, Arrays.asList(strings));
            }
            default:
                throw new IllegalStateException("Unexpected sign value: " + webCondition.getLogic());
        }
    }

    public static Condition.ConditionBuilder setLogic(WebCondition webCondition,Condition.ConditionBuilder builder) {
        switch (webCondition.getLogic()) {
            case QueryConstants.AND:{
                return builder.and();
            }
            case QueryConstants.OR:{
                return builder.or();
            }
            default:
                throw new IllegalStateException("Unexpected Logic value: " + webCondition.getLogic());
        }
    }

    public static Condition.ConditionBuilder setCurves(WebCondition webCondition,Condition.ConditionBuilder builder) {
        switch (webCondition.getCurves()) {
            case QueryConstants.L_CURVES:{
                return builder.leftCurves();
            }
            case QueryConstants.R_CURVES:{
                return builder.rightCurves();
            }
            default:
                throw new IllegalStateException("Unexpected Curves value: " + webCondition.getLogic());
        }
    }
}

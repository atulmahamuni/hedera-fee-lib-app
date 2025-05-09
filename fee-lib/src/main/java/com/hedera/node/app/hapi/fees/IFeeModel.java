package com.hedera.node.app.hapi.fees;

import java.util.List;
import java.util.Map;

public interface IFeeModel {
    String getDescription();
    List<ParameterDefinition> getParameters();

    default FeeCheckResult checkParameters(Map<String, Object> values) {
        for (ParameterDefinition p : getParameters()) {
            if (p.type.compareTo("number") == 0) {
                int val;
                try {
                    val = (int) values.get(p.name);
                } catch (Exception ex) {
                    return FeeCheckResult.failure("Parameter " + p.name + " must be an integer");
                }
                if (val < p.min || val > p.max) {
                    return FeeCheckResult.failure("Parameter " + p.name + " must be in range [" + p.min + ", " + p.max + "]");
                }
            }
        }
        return FeeCheckResult.success();
    }

    FeeResult computeFee(Map<String, Object> values);
}

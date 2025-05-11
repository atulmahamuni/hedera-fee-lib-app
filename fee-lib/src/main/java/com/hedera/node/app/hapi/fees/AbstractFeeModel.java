package com.hedera.node.app.hapi.fees;

import java.util.*;

public abstract class AbstractFeeModel {
    protected static final List<ParameterDefinition> COMMON_PARAMS = List.of(
            new ParameterDefinition("numSignatures", "number", 1, 1, Integer.MAX_VALUE, "Number of signatures")
    );

    // Returns the description of the API
    public abstract String getService();

    // Returns the description of the API
    public abstract String getDescription();

    protected abstract List<ParameterDefinition> apiSpecificParams();

    // Get the list of parameters that are relevant for the specified API
    public List<ParameterDefinition> getParameters() {
        List<ParameterDefinition> merged = new ArrayList<>(COMMON_PARAMS);
        merged.addAll(apiSpecificParams());
        return merged;
    }

    // Check the values of parameters based on the specified API
    public FeeCheckResult checkParameters(Map<String, Object> values) {
        for (ParameterDefinition p : getParameters()) {
            if ("number".equals(p.type)) {
                try {
                    Object value = values.get(p.name);
                    if (value == null) {
                        continue;
                    }
                    int val = (int) value;
                    if (val < p.min || val > p.max) {
                        return FeeCheckResult.failure("Parameter " + p.name + " must be in range [" + p.min + ", " + p.max + "]");
                    }
                } catch (ClassCastException ex) {
                    return FeeCheckResult.failure("Parameter " + p.name + " must be a number");
                }
            }
        }
        return FeeCheckResult.success();
    }

    // Compute the fee. There are 2 parts to the fee. There's the API specific fee (e.g. cryptoCreate price is based on the number of keys), and there's fee for parameters that are common across all APIs (e.g. number of signatures)
    public FeeResult computeFee(Map<String, Object> values) {
        FeeResult result = computeApiSpecificFee(values);

        // Compute the fee for parameters that are common across all APIs
        if (values.containsKey("numSignatures")) {
            int numSignatures = (int) values.get("numSignatures");
            double fee = (numSignatures - 1) * BaseFeeRegistry.getBaseFee("PerSignature");
            result.addDetail("Signature verification fee", numSignatures, fee);
        }
        return result;
    }

    // Compute API specific fee (e.g. cryptoCreate price is based on the number of keys)
    protected abstract FeeResult computeApiSpecificFee(Map<String, Object> values);
}

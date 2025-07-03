package com.hedera.node.app.hapi.fees;

import com.hedera.node.app.hapi.fees.apis.common.FeeApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class FeeController {

    @GetMapping("/transactions")
    public Map<String, List<String>> getTransactionTypes() {
        Map<String, List<String>> result = new LinkedHashMap<>();

        for (var entry : FeeModelRegistry.registry.entrySet()) {
            var tx = entry.getValue();
            String service = tx.getService();
            String name = String.valueOf(entry.getKey());

            result.computeIfAbsent(service, k -> new ArrayList<>()).add(name);
        }

        return result;
    }

    @GetMapping("/transactions/{type}/description")
    public ResponseEntity<String> getDescription(@PathVariable String type) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(FeeApi.fromString(type));
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.getDescription());
    }

    @GetMapping("/transactions/{type}/parameters")
    public ResponseEntity<List<ParameterDefinition>> getParameters(@PathVariable String type) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(FeeApi.fromString(type));
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.getParameters());
    }

    @PostMapping("/transactions/{type}/check")
    public ResponseEntity<FeeCheckResult> checkParameters(@PathVariable String type, @RequestBody Map<String, Object> values) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(FeeApi.fromString(type));
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.checkParameters(values));
    }

    @PostMapping("/transactions/{type}/fee")
    public ResponseEntity<FeeResult> computeFee(@PathVariable String type, @RequestBody Map<String, Object> values) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(FeeApi.fromString(type));
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.computeFee(values));
    }
}

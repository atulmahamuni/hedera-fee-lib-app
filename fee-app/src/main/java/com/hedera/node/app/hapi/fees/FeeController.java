package com.hedera.node.app.hapi.fees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class FeeController {

    @GetMapping("/transactions")
    public Set<String> getTransactionTypes() {
        return FeeModelRegistry.registry.keySet();
    }

    @GetMapping("/transactions/{type}/description")
    public ResponseEntity<String> getDescription(@PathVariable String type) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(type);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.getDescription());
    }

    @GetMapping("/transactions/{type}/parameters")
    public ResponseEntity<List<ParameterDefinition>> getParameters(@PathVariable String type) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(type);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.getParameters());
    }

    @PostMapping("/transactions/{type}/check")
    public ResponseEntity<FeeCheckResult> checkParameters(@PathVariable String type, @RequestBody Map<String, Object> values) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(type);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.checkParameters(values));
    }

    @PostMapping("/transactions/{type}/fee")
    public ResponseEntity<FeeResult> computeFee(@PathVariable String type, @RequestBody Map<String, Object> values) {
        AbstractFeeModel model = FeeModelRegistry.registry.get(type);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model.computeFee(values));
    }
}

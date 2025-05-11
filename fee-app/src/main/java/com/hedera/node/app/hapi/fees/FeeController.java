package com.hedera.node.app.hapi.fees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class FeeController {

//    @GetMapping("/transactions")
//    public Set<String> getTransactionTypes() {
//        LinkedHashMap<String, String[]> map = new LinkedHashMap();
//        for (var key: FeeModelRegistry.registry.keySet()) {
//            var exists = map.get(key);
//            if (exists == null) {
//                map.put(key, []);
//            }
//            map.get(key).append(FeeModelRegistry.registry.get(key).getService());
//        }
//
////        return FeeModelRegistry.registry.keySet();
//        return map;
//    }

    @GetMapping("/transactions")
    public Map<String, List<String>> getTransactionTypes() {
        Map<String, List<String>> result = new LinkedHashMap<>();

        for (var entry : FeeModelRegistry.registry.entrySet()) {
            var tx = entry.getValue();
            String service = tx.getService();
            String name = entry.getKey();

            result.computeIfAbsent(service, k -> new ArrayList<>()).add(name);
        }

        return result;
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

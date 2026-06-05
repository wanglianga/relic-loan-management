package com.relicloan.controller;

import com.relicloan.dto.ChainResponse;
import com.relicloan.service.ChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chain")
@CrossOrigin
@RequiredArgsConstructor
public class ChainController {
    private final ChainService chainService;

    @GetMapping("/{loanId}")
    public ChainResponse getChain(@PathVariable Long loanId) {
        return chainService.getChain(loanId);
    }
}

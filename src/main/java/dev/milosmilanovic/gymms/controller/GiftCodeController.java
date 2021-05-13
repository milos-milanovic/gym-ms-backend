package dev.milosmilanovic.gymms.controller;

import dev.milosmilanovic.gymms.data.service.GiftCodeService;
import dev.milosmilanovic.gymms.model.request.GiftCodeRedeemRequest;
import dev.milosmilanovic.gymms.model.response.CheckGiftCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GiftCodeController {

    @Autowired
    private GiftCodeService giftCodeService;

    @PostMapping("/api/gift-code/generate")
    public Map<String,String> generateGiftCode(@RequestBody(required = false) String email) {
        return giftCodeService.generateGiftCode(email);
    }

    @GetMapping("/api/gift-code/check")
    public CheckGiftCodeResponse checkGiftCode(@RequestParam long code) {
        return giftCodeService.getGiftCode(code);
    }

    @PutMapping("/api/gift-code/redeem")
    public void redeemGiftCode(@RequestBody GiftCodeRedeemRequest request) {
        giftCodeService.redeemGiftCode(request.getCode(), request.getMemberId());
    }

}

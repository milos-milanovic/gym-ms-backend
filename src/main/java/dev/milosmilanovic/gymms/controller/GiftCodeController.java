package dev.milosmilanovic.gymms.controller;

import dev.milosmilanovic.gymms.data.service.GiftCodeService;
import dev.milosmilanovic.gymms.model.request.EmailRequest;
import dev.milosmilanovic.gymms.model.request.GiftCodeRedeemRequest;
import dev.milosmilanovic.gymms.model.response.CheckGiftCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("https://gymms.milosmilanovic.dev")
@RequestMapping("gift-code")
public class GiftCodeController {

    @Autowired
    private GiftCodeService giftCodeService;

    @PostMapping("generate")
    public Map<String, String> generateGiftCode(@RequestBody(required = false) EmailRequest emailRequest) {
        return giftCodeService.generateGiftCode(emailRequest);
    }

    @GetMapping("check")
    public CheckGiftCodeResponse checkGiftCode(@RequestParam long code) {
        return giftCodeService.getGiftCode(code);
    }

    @PutMapping("redeem")
    public void redeemGiftCode(@RequestBody GiftCodeRedeemRequest request) {
        giftCodeService.redeemGiftCode(request.getCode(), request.getMemberId());
    }

}
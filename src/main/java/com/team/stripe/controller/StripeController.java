package com.team.stripe.controller;

import com.team.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripe")
public class StripeController {
    @Autowired
    private StripeService stripeService;

    @PostMapping("/do")
    public ResponseEntity doSomething() {
        try {
            String intedId = stripeService.createPaymentIntent();
            stripeService.confirmPaymentIntent(intedId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (StripeException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/done/{intentId}")
    public ResponseEntity doneWithSomething(@PathVariable String intentId) {
        try {
            stripeService.capturePaymentIntent(intentId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (StripeException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}

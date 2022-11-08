package com.ecommerceapp.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class PurchaseResponse {

    @NotNull
    private String orderTrackingNumber;

}

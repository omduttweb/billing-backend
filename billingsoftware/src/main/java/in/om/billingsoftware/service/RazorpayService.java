package in.om.billingsoftware.service;

import com.razorpay.RazorpayException;
import in.om.billingsoftware.io.RazorpayOrderResponse;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}

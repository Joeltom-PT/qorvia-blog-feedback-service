package com.qorvia.blogfeedbackservice.messaging;//package com.qorvia.accountservice.messaging;
//
//import com.qorvia.accountservice.messaging.wrapper.RabbitMQMessageWrapper;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitMQReceiver {
//
//    @RabbitListener(queues = "myQueue")
//    public void receiveMessage(byte[] bytes) {
//        // Convert bytes to wrapper object
//        RabbitMQMessageWrapper wrapper = (RabbitMQMessageWrapper) convertBytesToObject(bytes);
//
//        // Handle the message based on its type
//        switch (wrapper.getMessageType()) {
////            case "verify-otp":
////                VerifyOtpRequest verifyOtpRequest = (VerifyOtpRequest) wrapper.getPayload();
////                handleVerifyOtp(verifyOtpRequest); // Call the dedicated method
////                break;
////            case "send-otp":
////                SendOtpRequest sendOtpRequest = (SendOtpRequest) wrapper.getPayload();
////                handleSendOtp(sendOtpRequest);
////                break;
//            default:
//                throw new IllegalArgumentException("Unknown message type: " + wrapper.getMessageType());
//        }
//    }
//
////    // Dedicated method to handle verify-otp requests
////    private void handleVerifyOtp(VerifyOtpRequest verifyOtpRequest) {
////        // Add your verify-otp logic here
////        boolean isOtpValid = verifyOtp(verifyOtpRequest.getOtp(), verifyOtpRequest.getUserId());
////
////        if (isOtpValid) {
////            System.out.println("OTP verification successful for user: " + verifyOtpRequest.getUserId());
////        } else {
////            System.out.println("OTP verification failed for user: " + verifyOtpRequest.getUserId());
////        }
////    }
////
////    // Example OTP verification logic
////    private boolean verifyOtp(String otp, String userId) {
////        // Replace this with your actual OTP verification logic
////        return "123456".equals(otp); // Example: Hardcoded OTP for testing
////    }
////
////    private void handleSendOtp(SendOtpRequest sendOtpRequest) {
////        // Handle send-otp logic
////        System.out.println("Received send-otp request: " + sendOtpRequest);
////    }
//
//    // Convert byte array to object
//    private Object convertBytesToObject(byte[] bytes) {
//        return new RabbitTemplate().getMessageConverter().fromMessage(new Message(bytes, new MessageProperties()));
//    }
//}
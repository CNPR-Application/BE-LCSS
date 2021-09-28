package cnpr.lcss.service;

import cnpr.lcss.model.NotificationRequestDto;
import cnpr.lcss.model.SubscriptionRequestDto;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    private FirebaseApp firebaseApp;

    public String sendPnsToDevice(NotificationRequestDto notificationRequestDto) {
        Message message = com.google.firebase.messaging.Message.builder()
                .setToken(notificationRequestDto.getTarget())
                .setNotification(new com.google.firebase.messaging.Notification(notificationRequestDto.getTitle(), notificationRequestDto.getBody()))
                .putData("content", notificationRequestDto.getTitle())
                .putData("body", notificationRequestDto.getBody())
                .build();

        String response = "send Pns to Device";
        try {
            response = FirebaseMessaging.getInstance().send(message);
            } catch (Exception e) {
                response="fail";
        }

        return response;
    }

    public void subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {

        }
    }

    public void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {

        }
    }

    public String sendPnsToTopic(NotificationRequestDto notificationRequestDto) {
        Message message = com.google.firebase.messaging.Message.builder()
                .setTopic(notificationRequestDto.getTarget())
                .setNotification(new Notification(notificationRequestDto.getTitle(), notificationRequestDto.getBody()))
                .putData("content", notificationRequestDto.getTitle())
                .putData("body", notificationRequestDto.getBody())
                .build();

        String response = "send Pns to Topic";
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
        response="fail";
        }

        return response;
    }
}

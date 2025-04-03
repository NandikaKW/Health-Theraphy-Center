package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailController {
    private String customerEmail;

    @FXML
    private TextArea txtBody;

    @FXML
    private TextField txtSubject;
    public void setCustomerEmail(String email) {
        this.customerEmail = email;
    }

    @FXML
    void sendUsingGmailOnAction(ActionEvent event) {
        if (customerEmail == null) {
            new Alert(Alert.AlertType.WARNING, "No customer email provided!").show();
            return;
        }

        final String FROM = "nandikakaweesha4005@gmail.com";
        String subject = txtSubject.getText();
        String body = txtBody.getText();

        if (subject.isEmpty() || body.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Subject and body must not be empty!").show();
            return;
        }


        new Thread(() -> {
            try {
                sendEmailWithGmail(FROM, customerEmail, subject, body);


                javafx.application.Platform.runLater(() -> {
                    new Alert(Alert.AlertType.INFORMATION, "Email sent successfully!").show();
                    txtBody.clear();
                    txtSubject.clear();
                });

            } catch (AuthenticationFailedException e) {
                javafx.application.Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "Authentication failed. Please check your email credentials.").show()
                );
                e.printStackTrace();
            } catch (MessagingException e) {
                javafx.application.Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "Failed to send email. Please check your internet connection or try again later.").show()
                );
                e.printStackTrace();
            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show()
                );
                e.printStackTrace();
            }
        }).start();

    }
    private void sendEmailWithGmail(String from, String to, String subject, String messageBody) throws MessagingException {
        final String PASSWORD = "argw plqc ujsx uvrw";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(messageBody);


        Transport.send(message);
    }

}

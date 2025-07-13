package Utility;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class OtpReader {

    private static OtpReader OtpReaderInstance = null;

    public OtpReader() {

    }

    public static OtpReader getInstance() {
        if (OtpReaderInstance == null) {
            OtpReaderInstance = new OtpReader();
        }
        return OtpReaderInstance;
    }

    private final static String host = "imap.gmail.com";
    private final static String mailStoreType = "imaps";
    private final static String port = "993";
    // email is your email address where email with OTP is sent
    private final static String email = "lgstester50@gmail.com";
    // password is app password created for the email address above
    private final static String password = "yypbzstepwjjcrku";

    public static String getOtp() {
        try {
            String otp = "";

            // delay to let backend sends email with OTP
            Thread.sleep(5000);

            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", port);
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore(mailStoreType);
            store.connect(host, email, password);

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            // all unread emails from Inbox folder is to be in the messages array
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            // go through all the messages
            for (Message singleMessage : messages) {
                // find a message with the same Subject as emails with OTP usually have
                if (singleMessage.getSubject().equals("Hello")) {
                    singleMessage.setFlag(Flags.Flag.SEEN, true);
                    String messageBody = getMessageBody(singleMessage);
                    String otpPhrase = " Your one time password is ";

                    //  System.out.println(messageBody);
                    // find index inside the message body where OTP is written
                    int indexOfOtpStart = messageBody.indexOf(otpPhrase) + otpPhrase.length();
                    // get 6-digit OTP
                    otp = otp + messageBody.substring(indexOfOtpStart, indexOfOtpStart + 4);
                }
            }
            inbox.close(false);
            store.close();
            System.out.println("The OTP Verification is " + otp);
            return otp;
        } catch (Exception e) {
            throw new RuntimeException("There are problems with reading emails.");

        }
    }

    private static String getMessageBody(Message message) throws MessagingException, IOException {
        String messageBody = "";
        if (message.isMimeType("text/plain")) {
            messageBody = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            messageBody = getTextFromMimeMultipart(mimeMultipart);
        }
        return messageBody;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private static String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }
}

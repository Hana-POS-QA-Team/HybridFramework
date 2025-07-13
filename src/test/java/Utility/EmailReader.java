package Utility;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class EmailReader {
    private final static Properties properties = new Properties();
    private final static String host = "imap.gmail.com";
    private final static String username = "hanaposqateam@gmail.com"; // password is app password created
    private final static String password = "bcfburrmktksjckr";
    private static EmailReader EmailReaderInstance = null;

    public EmailReader() {

    }

    public static EmailReader getInstance() {
        if (EmailReaderInstance == null) {
            EmailReaderInstance = new EmailReader();
        }
        return EmailReaderInstance;
    }


    // Set up email server properties


    public static String getInvoiceNumber() {

        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.starttls.enable", "true");

        try {
            Thread.sleep(5000);
            // Connect to the email server
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);

            // Open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            String invoiceNumber = null;
            String invoicePhrase = "Order Confirmation For ";

            System.out.println("Number of emails present is : " + messages.length);

            String expectedMessage = "THIS IS A COPY OF THE INVOICE FOR YOUR RECENT ORDER";

            boolean isEmailValid = false;
            for (Message singleMessage : messages) {
                String subject = singleMessage.getSubject();
                // Find a message with the subject that starts with the invoice Phrase 
                if (subject != null && subject.startsWith(invoicePhrase)) {
                    singleMessage.setFlag(Flags.Flag.SEEN, true);
                    int startIndex = invoicePhrase.length();
                    int endIndex = startIndex + 7; // Assuming the invoice number is 7 digits
                    invoiceNumber = subject.substring(startIndex, endIndex);
                    System.out.println("Invoice number at email subject : " + invoiceNumber);
                    break;
                }

                String messageBody = getMessageBody(singleMessage);

                String invoicePrefix = "INVOICE NO - ";

                // Check if the email body contains the invoice prefix
                int invoiceIndex = messageBody.indexOf(invoicePrefix);
                if (invoiceIndex != -1) {
                    // Extract the 7-digit invoice number
                    int startIndex = invoiceIndex + invoicePrefix.length();
                    int endIndex = startIndex + 7; // Assuming the invoice number is 7 digits
                    invoiceNumber = messageBody.substring(startIndex, endIndex);
                    System.out.println("Invoice number at email body : " + invoiceNumber);
                    break;
                }

            }

            // Close the folder and store
            inbox.close(false);
            store.close();

            if (invoiceNumber != null) {
                System.out.println("The Invoice Number is " + invoiceNumber);
            } else {
                System.out.println("Invoice number not found.");
            }
            return invoiceNumber;
        } catch (Exception e) {
            throw new RuntimeException("There are problems with reading emails.", e);
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




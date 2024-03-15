package qrlabel.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to execute
 * ./gradlew clean build && ./gradlew run2
 */


public class QRCodeNameGeneratorMain {

    public static void main(String[] args) {

        String prefix = "B-SW-";

        List<String> qrCodeNames = new ArrayList<>();

        for (int i=1; i<100; i++) {
            qrCodeNames.add(prefix + String.format("%04d", i));
        }

        qrCodeNames.stream().forEach(currentQrCodeName -> {
            String[] words = {"\"", currentQrCodeName, "\"", ","};
            System.out.println(String.join("", words));
        });

    }

}

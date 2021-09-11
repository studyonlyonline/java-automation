package automation;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


/**
 * Libraries required - selenium-java-3.141.59/client-combined-3.141.59 jar and chromedriver for selenium compatible with your chrome version
 * <p>
 * To run this whatsapp sender  -
 * For mac run this command in terminal , this opens chrome in debig mode at port 9222
 * /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --remote-debugging-port=9222 --no-first-run --no-default-browser-check --user-data-dir=$(mktemp -d -t 'chrome-remote_data_dir')
 * <p>
 * For other operating system check command online and run chrome in debug mode at port 9222
 * <p>
 * Running above command will open new Chrome window
 * Open web.whatsapp in it and login using QR code (required only once)
 * Now run the script anytime you want to send the whatsapp message
 * <p>
 * Whatsapp API to be used - "https://web.whatsapp.com/send?phone="+ PhoneNo +"&text=" + text +"&source&data&app_absent";
 * Replace phone no with mobile no and text with the message you want to send
 */

public class whatsappSingleMsgSender {

    public static void main(String[] args) throws InterruptedException {

        //set the path in your computer
        System.setProperty("webdriver.chrome.driver", "/Users/gsartha/learning/chromedriverfile/chromedriver_89");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        WebDriver driver = new ChromeDriver(options);


        String no = "917355043253";
//        String data[] = {
//                "9839337577",
//                "9839779957",
//                "9839818111",
//                "9935181223",
//                "9935270303",
//                "9935533808",
//                "7388969594",
//                "7408894116",
//                "8081310064",
//                "8687575586",
//                "8756130130",
//                "8787252689",
//                "8853695063",
//                "8858109709",
//                "9140651258",
//                "9335758332",
//                "9935533808",
//                "9935533865",
//                "9935533866",
//                "9936299357",
//                "9936567589",
//                "9956324573",
//                "9984646109",
//                "9415044502",
//                "9415429719",
//                "9450131670",
//                "9450154725",
//                "9452249255",
//                "9506743930",
//                "9621128191",
//                "9621131339",
//                "9721135913",
//                "9794986384",
//                "7668996709",
//                "9236582821",
//                "9305710740",
//                "8960866031",
//                "9792112113",
//                "8858731409",
//                "6306085216",
//                "9839572666",
//                "8546041113",
//                "9935017291",
//                "9415092521",
//                "9918685458",
//                "9889103179",
//                "8858921905",
//                "8707818400",
//                "9598964186",
//                "8009448045",
//                "9140859644",
//                "9935445807",
//                "8299110358",
//                "8840892784",
//                "9795498294",
//                "8299217100",
//                "9455514264",
//                "8004111117",
//                "8429003613",
//                "9140759562",
//                "8960699301",
//                "9511006834",
//                "9415143133",
//                "8795744007",
//                "9889048552",
//                "7007554129",
//                "9336814647",
//                "9598170908",
//                "9935054502",
//                "9794986384",
//                "9935181223",
//                "9369027548",
//                "6393752742",
//                "7252749821",
//                "9795440791",
//                "9956175761",
//                "9839058807",
//                "9554507936",
//                "9794993663",
//                "9889053255",
//                "9415475559",
//                "9889668063",
//                "9956223840",
//                "9919747434",
//                "8765673040",
//                "9936299357",
//                "9452249255",
//                "9648269394",
//                "9935533870",
//                "7275370959",
//                "7388969594",
//                "7007417050",
//                "9936575848",
//                "9807055166",
//                "9760007366",
//                "9759682098",
//                "9456843454",
//                "9838698759",
//                "9839818111",
//                "9839119736",
//                "9696480072",
//                "9935012306",
//                "9935533866",
//                "8171555701",
//                "9219555850",
//                "8533006600",
//                "9897289082",
//                "9839127452",
//                "9839960491",
//                "9839220410",
//                "9935079333",
//                "9839116189",
//                "9935533713",
//                "9839099581",
//                "9956001028",
//                "9792888980",
//                "9935533448",
//                "9721800016",
//                "9918770689",
//                "9076744332",
//                "9956349600",
//                "9936766638",
//                "8726020414",
//                "9415320756",
//                "8574843396",
//                "8707250941",
//                "7007909978",
//                "8687575586",
//                "7355283767",
//                "8218270312",
//                "9569727372"
//        };


        String data[] = {
                "7355043253",
                "9453028193",
                "9415302372"
        };

//        String text = "This message is selenium autogenerated for testing - test3";
//        String text = String.format("Hello %s Ji, \n" +
//                "\n" +
//                "Thanks you for visiting Benling Harbacore Electric vehicle showroom at Bhannana Purwa Near Sangeet Takiz.\n" +
//                "\n" +
//                "\n" +
//                "This is your Last chance to book Benling electric scooter \uD83D\uDEF5 \uD83D\uDEF5 at Heavy discount and get a \uD83D\uDCA5Free Assured Gift \uD83D\uDCA5with the purchase. \n" +
//                "\n" +
//                "Book the vehicles below \uD83D\uDEF5\uD83D\uDEF5\n" +
//                "Benling Falcon E-Scooter bit.ly/3fM7YYh\n" +
//                "Benling Icon E-Scooter bit.ly/2PJp0vz\n" +
//                "Benling Kriti E-Scooter bit.ly/3agWgE3\n" +
//                "\n" +
//                "Chalo is baar manaye pollution free Diwali. \n" +
//                "#KhushiyonValiDiwali\n" +
//                "\n" +
//                "From :\n" +
//                "Harbacore Family\n" +
//                "We wish you a very Happy Dhanteras and Diwali \uD83D\uDCA5\uD83D\uDCA5.",temp);

//        String spamtext = text;
        System.out.println("here");

        for (int i = 0; i < data.length; i++) {

            try {

                System.out.println(" counter = " + i + " at no " + data[i]) ;
//                String text = "Hello " + name[0].trim() + " Ji, %0A" +
//                        "%0A" +
//                        "Only Last 2 days remaining to book Benling Electric Scooter before price hike in New Year 2021. %0A %0A" +
//                        "Join uur Noble Cause to Make India A Cleaner and A Greener India %0A" +
//                        "#Swachh Bharat Mission %0A %0A" +
//                        "View and Book Benling Electric Scooters at www.harbacore.com " +
//                        "or at https://wa.me/c/917355043253 %0A %0A" +
//                        "#Swachh Bharat Mission %0A" +
//                        "%0A" +
//                        "From : %0A" +
//                        "Harbacore Family %0A" +
//                        "Showroom Location: Bhannana Purwa, Sangeet %0A" +
//                        "Find us on Google: https://g.page/benling-electric-scooter-kanpur?gm %0A" ;


                String text = "प्रिय दोस्त,\n %0A" +
                        "अब खरीदें ब्रांडेड इलेक्ट्रिकल ⚡ प्रोडक्ट्स जैसे wire, fan ,led lights सीधे डिस्ट्रीब्यूटर/डायरेक्ट डीलर से अधिकतम डिस्काउंट मे, Harbacore से मात्र एक क्लिक पे \uD83D\uDC4D\uD83D\uDC4D %0A" +
                        "\n" +
                        "विजिट करे %0A " +
                        "http://harbacore.com/products/electrical-product-marketplace/kei-wires";

//                String text="Hi %0A this";
                String path = "https://web.whatsapp.com/send?phone=91" + data[i] + "&text=" + text;
                driver.get(path);
                System.out.println("Final URL " + path);
//                Thread.sleep(10000);
                if (i > 0) {
                    try {
                        Alert alert = driver.switchTo().alert();
                        System.out.println("Alert text  " + alert.getText());
                        alert.accept();
                    } catch (Exception ex) {
                        System.out.println("no such alert");
                    }
                }

                //wait for web whatsapp page to load
                Thread.sleep(15000);

                //select the current active element and press enter key
                System.out.println("Executing press key");
                WebElement messageElement = driver.findElements(By.cssSelector("footer .selectable-text")).get(0);
//                WebElement element = driver.switchTo().activeElement();
//                driver.switchTo().activeElement().sendKeys("The Title");
                messageElement.sendKeys(" ");
                System.out.println("tag name" + messageElement.getTagName());
                Thread.sleep(5000);
                System.out.println("after wait");
//                element.click();
                messageElement.sendKeys(Keys.RETURN);

            } catch (Exception ex) {
                System.out.println("Exception in sending whatsapp message " + ex);
                System.out.println("Retrying to send whatsapp message");

                //stop the main thread for 1 minute, and then proceed
                Thread.sleep(60000);

            }
        }
    }
}

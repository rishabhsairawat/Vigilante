package vigilante;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import static vigilante.BrowserLayout.AddBookmark;
import static vigilante.BrowserLayout.Refresh;
import static vigilante.BrowserLayout.ResponseForGo;
import static vigilante.BrowserLayout.ResponseForURL;
import static vigilante.BrowserLayout.Show;

public class TranscriberDemo {   
    static int x=1;
             static LiveSpeechRecognizer recognizer;                          
    public static void transcribe() throws Exception {
                                     
        Configuration configuration = new Configuration();

        configuration
                .setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration
                .setDictionaryPath("3747.dict");
        configuration
                .setLanguageModelPath("3747.lm");
//resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin
        
         recognizer = new LiveSpeechRecognizer(configuration);

        recognizer.startRecognition(true);
       
        while(x==1){
            System.out.println("speak : ");
        SpeechResult result = recognizer.getResult();
       processResult(result.getHypothesis());
        
        }


    }
    public static void processResult(String command){
        System.out.println("you said : "+command);
       if(command.contains("OPEN")){
           ResponseForURL(HomeEngine.browser, HomeEngine.webEngine,command);
       }
       else if(command.contains("GO")){
           ResponseForGo(HomeEngine.browser, HomeEngine.webEngine,command);
       }
       else if(command.contains("SHOW")){
           Show(HomeEngine.browser, HomeEngine.webEngine,command);
       }
       //||command.contains("MIC")
       else if (command.contains("STOP")){
           x=0;
           recognizer.stopRecognition();
            Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                    BrowserLayout.mic.setGraphic(new ImageView("file:icons/micOff.png"));
                 }
             });
       }
       else if(command.contains("REFRESH")){
       Refresh(HomeEngine.browser,HomeEngine.webEngine,command);
       }
       else if(command.contains("ADD")){
       AddBookmark(HomeEngine.browser,HomeEngine.webEngine,command);
       }
       else
           System.out.println("INVALID INPUT");
    }
}

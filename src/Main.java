import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import com.google.inject.Injector;
import controllers.GameCtrl;
import controllers.MainCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var game = FXML.load(GameCtrl.class, "controllers/Game.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, game);
    }
}
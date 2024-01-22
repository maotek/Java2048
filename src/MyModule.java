import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import controllers.GameCtrl;
import controllers.MainCtrl;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(GameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
    }
}
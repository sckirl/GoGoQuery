package Manager;

import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ManagerHome extends ManagerNavBar implements Builder<Region>{
	BorderPane layout = new BorderPane();
	
	public ManagerHome(Runnable addSwapper, 
			Runnable manageSwapper, 
			Runnable loginSwapper) {
		super(addSwapper, manageSwapper, loginSwapper);

	}

	@Override
	public Region build() {
		
		layout.setTop(createNavBar());
		layout.setCenter(welcomeLabel);
		
		return layout;
	}

	@Override
	protected Region createContent() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

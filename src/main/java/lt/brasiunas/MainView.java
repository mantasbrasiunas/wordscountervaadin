package lt.brasiunas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        Upload upload = new Upload();
        Button btnBegin = new Button("Begin counting");
        btnBegin.setEnabled(false);
        
        Button btnDownloadAll = new Button("Download all");
        btnDownloadAll.setEnabled(false);
        
        add(upload, btnBegin, btnDownloadAll);
        
        ArrayList<TextArea> txasWords = new ArrayList<>();
        ArrayList<Button> btnsDownload = new ArrayList<>();
        String[] firstLetters = { "A-G", "H-N", "O-U", "V-Z" };
        for (int i = 0; i < 4; i++) {
			TextArea txaWords = new TextArea("Words frequency " + 
				firstLetters[i]);
			txaWords.setWidth("100%");
			txaWords.setHeight("250px");
			txasWords.add(txaWords);
			
			Button btnDownload = new Button("Download file");
			btnDownload.setEnabled(false);
			btnsDownload.add(btnDownload);
			
			add(txaWords, btnDownload);
		}
    }
}

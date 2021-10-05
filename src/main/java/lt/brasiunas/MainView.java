package lt.brasiunas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Mantas Brasiunas, mantas@brasiunas.lt
 * @version 2021-10-05
 */
@Route
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = -224778650233417406L;
	
	private final int NOTIFICATION_SHOW_MS = 10000;
	
	private ArrayList<TextArea> txasWords;
	private char[][] firstLetters = { { 'a', 'g'}, { 'h', 'n'}, { 'o', 'u'}, 
		{ 'v', 'z'} };
	
	private void createResultElements() {
		txasWords = new ArrayList<>();
        for (int i = 0; i < firstLetters.length; i++) {
			TextArea txaWords = new TextArea(String.format(
				"Words frequency [%c-%c]", 
				firstLetters[i][0], firstLetters[i][1]));
			txaWords.setWidth("100%");
			txaWords.setHeight("250px");
			txasWords.add(txaWords);
		}
	}
	
	private void addBtnBeginClickListener(MultiFileMemoryBuffer mfmb) {
		Set<String> fileNamesSet = mfmb.getFiles();
		int i = 0;
		int n = fileNamesSet.size();
		InputStream[] inputStreams = new InputStream[n];
		for (String fileName : fileNamesSet) {
			inputStreams[i++] = mfmb.getInputStream(fileName);
		}
		WordsCountersManager wcm = new WordsCountersManager();
		WordsStatistics ws = wcm.countWords(inputStreams);
		
		i = 0;
		for (TextArea txa : txasWords) {
			txa.setValue(ws.getStatistics(
				firstLetters[i][0], firstLetters[i][1]));
			i++;
		}
	}
	private Button createBtnBegin(MultiFileMemoryBuffer mfmb) {
		Button btnBegin = new Button("Begin counting");
		if (mfmb != null) {
			btnBegin.addClickListener(event -> addBtnBeginClickListener(mfmb));
		} else {
			Notification.show("""
				MultiFileMemoryBuffer is not created. 
				Won't be able to upload files.
				Please, correct error in source code.""", 
				NOTIFICATION_SHOW_MS,
				Notification.Position.MIDDLE);
		}
		
        btnBegin.setEnabled(false);
        return btnBegin;
	}
	
	private Upload createUploadElement(MultiFileMemoryBuffer mfmb,
			Button btnBegin) {
		Upload upload = new Upload(mfmb);
        upload.setAcceptedFileTypes("text/plain");
        upload.setMaxFileSize(1 * 1024 * 1024); // 1 MB.
        
        upload.addFileRejectedListener(event -> {
			Notification.show("""
				File was not upload because of file restrictions.""", 
				NOTIFICATION_SHOW_MS,
				Notification.Position.TOP_CENTER);
		});
        upload.addAllFinishedListener(event -> {
			btnBegin.setEnabled(true);
		});
		
		upload.getElement().addEventListener("file-remove", event -> {
			Notification.show("""
				Removing file here won\'t prevent file from counting.""",
				NOTIFICATION_SHOW_MS, Notification.Position.TOP_CENTER);
			
		});
		/*
		upload.getElement().addEventListener("file-remove", event -> {
			System.out.println("Norima pašalinti bylą: " + upload.getElement());
			System.out.println("Pašalinta byla. Liko: " + 
				mfmb.getFiles().size());
		});
		*/
		
		return upload;
	}
	
    public MainView() {
		createResultElements();

		MultiFileMemoryBuffer mfmb = new MultiFileMemoryBuffer();
        Button btnBegin = createBtnBegin(mfmb);
        Upload upload = createUploadElement(mfmb, btnBegin);

        add(upload, btnBegin);
        
        for (int i = 0; i < firstLetters.length; i++) {
			add(txasWords.get(i));
		}
    }
}

package lt.brasiunas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Set;
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
*/
@Route
public class MainView extends VerticalLayout {
	private ArrayList<TextArea> txasWords;
	private ArrayList<Button> btnsDownload;
	private String[] firstLetters = { "a-g", "h-n", "o-u", "v-z" };
	
	private void createResultElements() {
		txasWords = new ArrayList<>();
        btnsDownload = new ArrayList<>();
        for (int i = 0; i < firstLetters.length; i++) {
			TextArea txaWords = new TextArea("Words frequency " + 
				firstLetters[i]);
			txaWords.setWidth("100%");
			txaWords.setHeight("250px");
			txasWords.add(txaWords);
			
			Button btnDownload = new Button("Download file");
			btnDownload.setEnabled(false);
			btnsDownload.add(btnDownload);
			
			//add(txaWords, btnDownload);
		}
	}
	
	private Button createBtnBegin(MultiFileMemoryBuffer mfmb) {
		Button btnBegin = new Button("Begin counting");
		if (mfmb != null) {
			btnBegin.addClickListener(event -> {
				Set<String> fileNamesSet = mfmb.getFiles();
				int i = 0;
				int n = fileNamesSet.size();
				InputStream[] inputStreams = new InputStream[n];
				for (String fileName : fileNamesSet) {
					inputStreams[i++] = mfmb.getInputStream(fileName);
				}
				WordsCountersManager wcm = new WordsCountersManager();
				WordsStatistics ws = wcm.countWords(inputStreams);
				System.out.println(ws);
				/*
				String[] fileNamesArray = new String[n];
				for (String fileName : fileNamesSet) {
					fileNamesArray[i++] = fileName;
					InputStream is = mfmb.getInputStream(fileName);
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						String line = null;
						while ((line = br.readLine()) != null) {
							System.out.println(line);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				WordsCountersManager wcm = new WordsCountersManager();
				WordsStatistics ws = wcm.countWords(fileNamesArray);
				*/
				
			});
		} else {
			Notification.show("""
				MultiFileMemoryBuffer is not created. 
				Won't be able to upload files.
				Please, correct error in source code.""", 10000,
				Notification.Position.MIDDLE);
		}
		
        btnBegin.setEnabled(false);
        return btnBegin;
	}
	
    public MainView() {
		MultiFileMemoryBuffer mfmb = new MultiFileMemoryBuffer();
        Button btnBegin = createBtnBegin(mfmb);
        
        Upload upload = new Upload(mfmb);
        
        upload.addFinishedListener(e -> {
			/*
			for (String fileName : fileNames) {
				InputStream inputStream = mfmb.getInputStream(fileName);
				System.out.println("---->" + fileName);
			}
			*/
			btnBegin.setEnabled(true);
		});
        
        Button btnDownloadAll = new Button("Download all");
        btnDownloadAll.setEnabled(false);
        
        add(upload, btnBegin, btnDownloadAll);
        
        createResultElements();
        
        for (int i = 0; i < firstLetters.length; i++) {
			add(txasWords.get(i), btnsDownload.get(i));
		}
        
        
    }
}

package lt.brasiunas;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class FileNames {
	@Getter @Setter private Set<String> fileNames;
	
	public FileNames() {
		fileNames = null;
	}
}

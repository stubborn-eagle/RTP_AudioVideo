import java.io.File;
public class RinominaFile {

private static final String SERIE = "Serie ";
private static int filename = 1;
/**

* Metodo richiamato dall'applicazione
* 
* @param args
* @throws Exception
*/
public static void main(String args[]) throws Exception {
  File path = new File("C:\\Users\\stubborn-eagle\\git\\git\\RTP_AudioVideo\\test_vari\\Giovanni");
  ciclaFilesOrDirectories(path);
}

/**
* Cicla sui file o sulle directory del path ricevuto e rinomina tutti i
* file che trova
* 
* @param nomeTelefilm
* @param numeroSerie
* @param daEliminare
* @param path
* @throws Exception
*/
private static void ciclaFilesOrDirectories(File path) throws Exception {
  for (File file : path.listFiles()) {
    if (file.isFile()) {
      renameFile(file);
    } else if (file.isDirectory()) {
      ciclaFilesOrDirectories(new File(file.getAbsolutePath()));
    }
  }
}

/**
* Rinomina i file della cartella
* 
* @param nomeTelefilm
* @param numeroSerie
* @param daEliminare
* @throws Exception
*/
private static void renameFile(File file) throws Exception {		
	String newFileName = String.valueOf(filename++) + ".jpg";
	File newFile = new File(file.getParent() + File.separatorChar + newFileName);
	try {
		boolean rename = file.renameTo(newFile);
		if (!rename) {
			System.err.println("Errore nel rinominare il file");
		} else {
			System.out.println("File rinominato con successo: " + newFileName);
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}
}
}

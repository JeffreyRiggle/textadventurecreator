package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.io.FileOutputStream;

public abstract class BaseProjectBuilder implements IProjectBuilder {
	
	protected int writeFileContent(File file, byte[] content) {
		FileOutputStream out = null;
		int retVal = 0;
		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
			retVal = -1;
		} finally {
			if (out == null) {
				return -1;
			}
			
			try {
				out.close();
			} catch (Exception e) { }
		}
		
		return retVal;
	}
	
}

//작성자 : 윤정도

package util;

import java.io.File;


// JSTL에서 함수를 실행하기 위해
public class FileValidator {
	public boolean isExist(String directory, String fileName) {
		String filePath1 = directory +  '\\' +  fileName;
		String filePath2 = directory +  fileName;
		
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		
		if (file1.exists() || file2.exists()) {
			return true;
		}
		
		return false;
	}
}

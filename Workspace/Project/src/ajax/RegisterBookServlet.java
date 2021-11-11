//작성자 : 윤정도



/*
	POST 요청 : 업로드 진행 
	GET  요청 : 업로드 진행상황 확인
	 
      현재 이 구현방식은 문제가 있다. 
	바로 POST 요청보다 먼저 GET 요청이 도착하게 되면 클라이언트측에서 바로 완료되었다고 뜬다는 점이다.
			→ setInterval로 POST 요청이후 살짝 기다렸다가 GET 요청을 하는 식으로 구현하였다.
	
	따라서 무조건 POST 요청이후에 GET 요청이 도착하도록 로직 수정이 필요하다.
	해결방안으로는 ajax 방식으로 multipart/form-data를 post 전송하여 이를 클라이언트가 수신한 이후에
	클라이언트 측으로 UPLOAD_PENDING 신호를 전송한다.
	이후에 클라이언트가 setInterval + ajax 방식으로 GET 요청을 주기적으로 보내어 진행상황을 확인하면 된다.
*/
 

package ajax;

import java.io.File;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import ajax.response.AjaxUploadResponse;
import bean.BookBean;
import database.manager.BookILDBManager;
import database.result.DBResult;
import util.HanConv;





@WebServlet(
	loadOnStartup = 1,	// 서버 시작하자마자 이미지, 파일 초기 저장경로 세팅을 위해
	urlPatterns = {"/RegisterBookServlet"}
)
public class RegisterBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String imageUploadFullPath;
	private static String fileUploadFullPath;
	
	private static String imageUploadRelativePath;
	private static String fileUploadRelativePath;

	@Override
	public void init() throws ServletException {
		imageUploadRelativePath = "uploadedFiles" + File.separator + "images";
		imageUploadFullPath = getServletContext().getRealPath("/") + imageUploadRelativePath;
		
		fileUploadRelativePath = "uploadedFiles" + File.separator + "files";
		fileUploadFullPath = getServletContext().getRealPath("/") + fileUploadRelativePath;
		
		File imageDirectory = new File(imageUploadFullPath);
		File fileDirectory = new File(fileUploadFullPath);
		
		if (!imageDirectory.exists()) {
			imageDirectory.mkdir();
		}
		
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
	}

	// 업로드 요청처리
	protected void doPost(HttpServletRequest req, HttpServletResponse rsesp) throws ServletException, IOException {
		
		// 업로드 ID용이다.
		String id = req.getParameter("startTime");
		String progreeAttrId = id + 1;
		String formAttrId = id + 2;
		
		// TEMP파일 저장경로를 가져오기 위해 컨텍스트를 가져옴
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

		// 임시 저장공간을 할당해준다.
		DiskFileItemFactory factory = createDiskFileItemFactory(servletContext, repository);
		factory.setRepository(repository);
		
		ProgressAttribute progressAttribute = new ProgressAttribute(); 
		progressAttribute.status = ProgressAttribute.WAIT_PENDING; 
		req.getSession().setAttribute(progreeAttrId, progressAttribute);
		req.getSession().setAttribute(formAttrId, new FormAttribute());
		

		// 업로드 핸들러 생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		// 업로드 진행상황 확인을 위한 리스너 등록
		upload.setProgressListener(getProgressListener(progreeAttrId, req.getSession()));

		// 요청을 분석하고 업로딩을 진행한다.
		try {
			
			Thread.sleep(2000); // 팬딩 동작확인을 위해 시간을 줌
			List<FileItem> items = upload.parseRequest(req);
			progressAttribute.progress = 100.0; 
			progressAttribute.status = ProgressAttribute.FILE_WRITE_PROCCESSING; 
			req.getSession().setAttribute(progreeAttrId, progressAttribute);
			Map<String, String> formFields = new HashMap<String, String>();
			
			Map<String, FileItem> fileMap = new HashMap<String, FileItem>();
			
			for (FileItem item : items) {
				
				if (item.getString().isEmpty()) {
					continue;
				}
				
				if (item.getFieldName().isEmpty()) {
					continue;
				}
				
				String fieldName = item.getFieldName();
				if (item.isFormField()) {
					String fieldValue = HanConv.toKor(item.getString(), "iso-8859-1", "euc_kr");
					
					if (fieldName.equals("imageFileName") || fieldName.equals("dataFileName")) {
						File f = new File(fieldValue);
						formFields.put(fieldName,  f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1));
					} else {
						formFields.put(fieldName, fieldValue);
					}
					
				} else {
					
					// item.getName 인코딩이 뭐지..? 한글로 변환이 안된다.
					// 꼼수로 다른필드로 전송해서 파일명 가져옴
					
					if (item.getFieldName().equals("data_filepath")) {
						fileMap.put("dataFileName", item);
					} else if (item.getFieldName().equals("image_filepath")) {
						fileMap.put("imageFileName", item);
					}
				}
			}
			
			if (fileMap.containsKey("dataFileName")) {
				File f = new File(fileUploadFullPath + File.separator + formFields.getOrDefault("dataFileName", "not_found"));
				if (f.exists()) {
					f.delete();
				}
				fileMap.get("dataFileName").write(f);
			}
			
			if (fileMap.containsKey("imageFileName")) {
				File f = new File(imageUploadFullPath + File.separator + formFields.getOrDefault("imageFileName", "not_found"));
				if (f.exists()) {
					f.delete();
				}
				fileMap.get("imageFileName").write(f);
			}
				
			
			
			Thread.sleep(2000); // 파일쓰기 동작 확인을 위해 시간을 줌
			FormAttribute formAttribute = new FormAttribute();
			formAttribute.applyBookBean(formFields);
			req.getSession().setAttribute(formAttrId, formAttribute);
			
			progressAttribute.status = ProgressAttribute.FINISHED;
			req.getSession().setAttribute(progreeAttrId, progressAttribute);
			
		} catch (FileUploadException e) {
			// 오류가 발생한경우 업로드 정보를 제거해준다.
			req.getSession().removeAttribute(progreeAttrId);
			req.getSession().removeAttribute(formAttrId);
		}
		catch (Exception e) {
			req.getSession().removeAttribute(progreeAttrId);
			req.getSession().removeAttribute(formAttrId);
			e.printStackTrace();
		}
	}

	// 업로드 진행상황 요청처리
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("startTime");
		String progreeAttrId = id + 1;
		String formAttrId = id + 2;
		String progress = "";
		
		AjaxUploadResponse uploadResponse = new AjaxUploadResponse();
		// null: 이미 완료된 경우
		ProgressAttribute progressAttribute = (ProgressAttribute)req.getSession().getAttribute(progreeAttrId);
		
		// 세션이 해당 ID에 해당하는 어트리뷰트를 들고있는 경우 
		if (progressAttribute != null) {
			progress = progressAttribute.progress + "";
			uploadResponse.setStatus(progressAttribute.status);
			
			// 업로드가 완료된 경우에 도서 정보를 DB에 등록해주자.
			if (progressAttribute.status == ProgressAttribute.FINISHED) { 
				FormAttribute formAttribute = (FormAttribute)req.getSession().getAttribute(formAttrId);
				
				DBResult result = BookILDBManager.getInstance().register(formAttribute.bookBean);
				uploadResponse.setMessage(result.getMsg());
				
				// DB등록에 실패 또는 에러가 발생한 경우 업로드된 파일을 제거해준다.
				if (result.getStatus() == DBResult.FAIL ||
					result.getStatus() == DBResult.ERROR) {
					
					File dataFile = new File(fileUploadFullPath +  formAttribute.bookBean.getDataFileName());
					File imageFile = new File(imageUploadFullPath +  formAttribute.bookBean.getImageFileName());
					
					if (dataFile.exists()) {
						System.out.print(dataFile.getAbsolutePath());
						System.out.print("이미 데이터파일이 존재하여 삭제하였습니다.");
						dataFile.delete();
					}
					if (imageFile.exists()) {
						System.out.print(dataFile.getAbsolutePath());
						System.out.print("이미 파일이 존재하여 삭제하였습니다.");
						imageFile.delete();
					}
				}
				
				req.getSession().removeAttribute(progreeAttrId);
				req.getSession().removeAttribute(formAttrId);
			} 
			
		} else {
			// 업로드 완료된 이후 요청이 들어오는 경우가 존재할 수 있으므로...
			progress = "100.0";
			uploadResponse.setStatus(ProgressAttribute.FINISHED);
		}
		
		System.out.println("GET 송신 : " + progress + " / status " + uploadResponse.getStatus());
		
		uploadResponse.setProgress(Double.parseDouble(progress));
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		out.println(uploadResponse.toJsonString());
		out.close();
	}

	// 가비지 컬렉터가 File을 제거한 경우 트랙커가 해당 파일을 자동으로 삭제해준다.
	// 이경우 임시 저장파일에 트랙커를 부착한다.
	private static DiskFileItemFactory createDiskFileItemFactory(ServletContext context, File repository) {
		FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		factory.setFileCleaningTracker(fileCleaningTracker);
		return factory;
	}

	// 프로그레스 리스너 반환
	private static ProgressListener getProgressListener(final String id, final HttpSession session) {
		return new ProgressListener() {
			public void update(long pBytesRead, long pContentLength, int pItems) {
				// put progress into session
				ProgressAttribute progressAttribute = new ProgressAttribute();
				double progress = ((double) pBytesRead / (double) pContentLength) * 100;
				progressAttribute.progress = progress;
				progressAttribute.status = ProgressAttribute.UPLOAD_PROCCESSING; 
				session.setAttribute(id, progressAttribute);
			}
		};
	}
	
	public static String getImageUploadFullPath() {
		return imageUploadFullPath;
	}

	public static String getFileUploadFullPath() {
		return fileUploadFullPath;
	}

	public static String getImageUploadRelativePath() {
		return imageUploadRelativePath;
	}

	public static String getFileUploadRelativePath() {
		return fileUploadRelativePath;
	}
}

class FormAttribute {
	BookBean bookBean = new BookBean();
	
	public void applyBookBean(Map<String, String> formFields) {
		bookBean.setName(formFields.getOrDefault("name", ""));
		bookBean.setWriterName(formFields.getOrDefault("writer_name", ""));
		bookBean.setPublisherName(formFields.getOrDefault("publisher_name", ""));
		bookBean.setCode(formFields.getOrDefault("b_code", ""));
		bookBean.setAgeLimit(Integer.parseInt(formFields.getOrDefault("age_limit", "1")));
		bookBean.setBorrowPoint(Integer.parseInt(formFields.getOrDefault("borrow_point", "-1")));
		bookBean.setBuyPoint(Integer.parseInt(formFields.getOrDefault("buy_point", "-1")));
		bookBean.setDataFileName(formFields.getOrDefault("dataFileName", "empty.data"));
		bookBean.setImageFileName(formFields.getOrDefault("imageFileName", "empty.image"));
	}
}

class ProgressAttribute {
	public static final int WAIT_PENDING = 0;
	public static final int UPLOAD_PROCCESSING = 1;
	public static final int FILE_WRITE_PROCCESSING = 2;
	public static final int FINISHED = 3;
	public static final int FAIL = 4;
	
	public int status = WAIT_PENDING;
	public double progress = 0.0;
	
	ProgressAttribute() {}
	ProgressAttribute(int progress) {
		this.progress = progress;
	}
}
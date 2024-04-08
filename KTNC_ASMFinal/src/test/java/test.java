import java.io.IOException;

import constant.LoginResult;
import utils.ReadExcelUtil;
import utils.WriteExcelUtil;

public class test {

	public static void main(String[] args) {
		
		try {
			WriteExcelUtil.writeTestDefect("id", "testLogin failed at case: 8 expected [true] but found [false]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Object[][] xxx = ReadExcelUtil.getTestData("username", "password");
//
//        for (Object[] row : xxx) {
//            for (Object item : row) {
//            	System.out.println(LoginResult.SUCCEED.getValue() + "||" + LoginResult.FAILED.getValue());
//                System.out.print(item + ">>");
//            }
//            System.out.println();
//        }
		
//		String test = "Case 1:\r\n"
//				+ "username=\r\n"
//				+ "pass=\r\n"
//				+ "Case 2:\r\n"
//				+ "username=anhkhoa\r\n"
//				+ "pass=anhkhoa123\r\n"
//				+ "Case 3:\r\n"
//				+ "username=notexisteduser\r\n"
//				+ "pass=correctpasswordCase ";
//		String[] arr = test.split("Case ");
//		
//		int i = 0;
//		for (String s : arr) {
//			i++;
//			System.out.println(i + ">>>" + s);
//			System.out.println(">>>>>" + arr.length);
//		}
	}
}

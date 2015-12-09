import java.io.UnsupportedEncodingException;

import secken.entry.response.SeckenEvent;
import secken.entry.response.SeckenId;
import secken.entry.response.SeckenQr;
import secken.exceptions.SeckenParamException;
import secken.exceptions.SeckenSignatureVerifyException;
import secken.SeckenApi;
import secken.entry.request.SeckenReqEvent;
import secken.entry.request.SeckenReqId;
import secken.enums.ActionType;
import secken.enums.AuthType;

public class SeckenSDKTest {

	public static SeckenApi api = null;

	public static final String APP_ID = "";
	public static final String APP_KEY = "";

	public static SeckenId waitResult(SeckenApi api, SeckenReqEvent event)
			throws InterruptedException, SeckenSignatureVerifyException {
		while (true) {
			SeckenId resp = (SeckenId) api.getResult(event);
			System.out.println(resp.toString());
			Thread.sleep(5000);
			if (resp.getStatus() == 200) {
				return resp;
			}
		}
	}

	public static void main(String[] args) throws SeckenParamException,
			SeckenSignatureVerifyException, InterruptedException,
			UnsupportedEncodingException {
		api = new SeckenApi(APP_ID, APP_KEY);

		SeckenQr qr = null;
		SeckenId id = null;
		SeckenEvent event = null;

		// 获取绑定二维码地址及数据
		qr = api.getBinding();
		System.out.println(qr.toString());
		// 等待成功返回结果
		id = waitResult(api, qr.getEvent());

		// 获得验证二维码地址及数据
		qr = api.getAuth();
		System.out.println(qr.toString());
		// 等待成功返回结果
		id = waitResult(api, qr.getEvent());

		// 一键推送验证
		// SeckenReqId 可以从
		// 		id.getUid() 获取 或者
		//      new SeckenReqId("uid")
		event = api.realtimeAuth(
				ActionType.LOGIN, 
				AuthType.FACE,
				id.getUid());
		
		System.out.println(event.toString());
		id = waitResult(api, event.getEvent());

		api.offlineAuth(new SeckenReqId(""), 0);
	}
}


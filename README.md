# Java Sdk for secken public cloud 
## Demo :
```java
    // 声明SeckenApi SDK
    // 详情参见https://www.yangcong.com/api
    public static SeckenApi api = null;

    // 填写从https://www.yangcong.com 申请的公有云app_id 和 app_key
    public static final String APP_ID = "";
    public static final String APP_KEY = "";

    // 轮询等待事件结果
    // SeckenReqEvent:
    //     可从继承自SeckenEvent的类中获取
    //     也可new SeckenReqEvent(string event_id)
    public static SeckenId waitResult(SeckenReqEvent event)
            throws InterruptedException, SeckenSignatureVerifyException {
        while (true) {
            // 从SDK中获取结果
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
        id = waitResult(qr.getEvent());

        // 获得验证二维码地址及数据
        qr = api.getAuth();
        System.out.println(qr.toString());
        // 等待成功返回结果
        id = waitResult(qr.getEvent());

        // 一键推送验证
        // SeckenReqId 可以从
        //      id.getUid() 获取 或者
        //      new SeckenReqId("uid")
        event = api.realtimeAuth(
                ActionType.LOGIN, 
                AuthType.FACE,
                id.getUid());
        
        System.out.println(event.toString());
        id = waitResult(event.getEvent());

        // 洋葱离线认证接口
        api.offlineAuth(new SeckenReqId(""), 0);
    }
```

# Secken Public Cloud Server SDK For Java

## 简介（Description）
Secken.Public.ServerSdk是Secken官方提供了一套用于和洋葱验证服务交互的SDK组件，通过使用它，您可以简化集成Secken服务的流程并降低开发成本。

密码就要大声说出来，开启无密时代，让密码下岗

洋葱是一个基于云和用户生物特征的身份验证服务。网站通过集成洋葱，可以快速实现二维码登录，或在支付、授权等关键业务环节使用指纹、声纹或人脸识别功能，从而彻底抛弃传统的账号密码体系。对个人用户而言，访问集成洋葱服务的网站将无需注册和记住账号密码，直接使用生物特征验证提高了交易安全性，无需担心账号被盗。洋葱还兼容Google验证体系，支持国内外多家网站的登录令牌统一管理。

【联系我们】

官网：https://www.yangcong.com

微信：yangcongAPP

微信群：http://t.cn/RLGDwMJ

QQ群：475510094

微博：http://weibo.com/secken

帮助：https://www.yangcong.com/help

合作：010-64772882 / market@secken.com

支持：support@secken.com

帮助文档：https://www.yangcong.com/help

项目地址：https://github.com/secken/secken-java

洋葱公有云服务端SDK主要包含四个方法：
* 获取二维码的方法（GetYangAuthQrCode），用于获取二维码内容和实现绑定。
* 请求推送验证的方法（AskYangAuthPush），用于发起对用户的推送验证操作。
* 查询事件结果的方法（CheckYangAuthResult），用于查询二维码登录或者推送验证的结果。
* 处理离线验证的方法（HandleOfflineAuthorization），用于处理离线验证的结果。

## 安装使用（Install & Get Started）

To install Secken.Public.ServerSdk, Import these packages

```
import java.io.UnsupportedEncodingException;

import secken.SeckenApi;
import secken.entry.request.SeckenReqEvent;
import secken.entry.request.SeckenReqId;
import secken.entry.response.SeckenEvent;
import secken.entry.response.SeckenId;
import secken.entry.response.SeckenQr;
import secken.entry.response.SeckenSignature;
import secken.enums.ActionType;
import secken.enums.AuthType;
import secken.exceptions.SeckenParamException;
import secken.exceptions.SeckenSignatureVerifyException;
```
## 更新发布（Update & Release Notes）

```
【1.0.0】更新内容：
1、完成了接口封装。
```

## 要求和配置（Require & Config）
```
// 需要去洋葱开发者中心新建一个类型为公有云的应用，创建完成之后，将对应的AppId+AppKey填过来
public static final String APP_ID = "";
public static final String APP_KEY = "";
// 初始化调用实例
public static SeckenApi api = new SeckenApi(APP_ID, APP_KEY);
```

## 获取二维码内容并发起验证事件（Get YangAuth QrCode）
```
// 获得验证二维码地址及数据
SeckenQr thisSeckenQrResult = api.getAuth();
// 打印输出
System.out.println(thisSeckenQrResult.toString());
```

GetYangAuthQrCode接口包含一个必传参数，AuthType。

|    状态码   | 		状态详情 		  |
|:----------:|:-----------------:|
|  200       |       成功         |
|  400       |       上传参数错误  |
|  403       |       签名错误                |
|  404       |       应用不存在                |
|  407       |       请求超时                |
|  500       |       系统错误                |
|  609       |       ip地址被禁                |

## 查询验证事件的结果（Check YangAuth Result）
```
// 事件ID
String requestEventId = "";
// 事件请求类
SeckenReqEvent thisSeckenReqEvent = new SeckenReqEvent(requestEventId);
// 等待成功返回结果
SeckenId thisCheckYcAuthResult = waitResult(thisSeckenReqEvent);
// 打印输出
System.out.println(thisCheckYcAuthResult.toString());

public static SeckenId waitResult(SeckenReqEvent event)
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
```
CheckYangAuthResult接口包含一个必传参数，RequestEventId。

|    状态码   | 		状态详情 		  |
|:----------:|:-----------------:|
|  200       |       成功         |
|  201       |       事件已被处理                |
|  400       |       上传参数错误  |
|  403       |       签名错误                |
|  404       |       应用不存在                |
|  407       |       请求超时                |
|  500       |       系统错误                |
|  601       |       用户拒绝                |
|  602       |       用户还未操作                |
|  604       |       事件不存在                |
|  606       |       callback已被设置                |
|  609       |       ip地址被禁                |

## 发起推送验证事件（Ask YangAuth Push）
```
// 用户ID
String thisUid = "";
// 请求类
SeckenReqEvent thisSeckenReqEvent = new SeckenReqEvent(thisUid);
// 一键推送验证
SeckenEvent thisAskYcAuthPushResult = api.realtimeAuth(ActionType.OTHER,AuthType.CLICK,thisSeckenReqEvent);
// 打印输出
System.out.println(thisAskYcAuthPushResult.toString());
```
AskYangAuthPush接口包含三个必传参数：AuthType、UserId、ActionType；两个可选参数：UserName、UserIpAddress。  

|    状态码   | 		状态详情 		  |
|:----------:|:-----------------:|
|  200       |       成功         |
|  400       |       上传参数错误  |
|  403       |       签名错误                |
|  404       |       应用不存在                |
|  407       |       请求超时                |
|  500       |       系统错误                |
|  608       |       验证token不存在           |
|  609       |       ip地址被禁                |

## 处理离线验证请求（Handle Offline Authorization）
```
// 动态验证码
String thisAuthCode = "";
// 用户ID
String thisUid = "";
// 请求类
SeckenReqEvent thisSeckenReqEvent = new SeckenReqEvent(thisUid);
// 离线验证
var offlineResult = api.offlineAuth(thisSeckenReqEvent, thisAuthCode);
// 打印输出
System.out.println(offlineResult.toString());
```
HandleOfflineAuthorization接口包含两个必传参数：UserId、AuthCode；

|    状态码   | 		状态详情 		  |
|:----------:|:-----------------:|
|  200       |       成功         |
|  400       |       上传参数错误  |
|  403       |       签名错误                |
|  404       |       应用不存在                |
|  407       |       请求超时                |
|  500       |       系统错误                |
|  600       |       动态码验证错误           |
|  604       |       event_id 事件不存在      |

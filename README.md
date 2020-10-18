# PAC URL Service
 
A simple android http server based on [nanohttpd](http://nanohttpd.org), which serve only the PAC URL to send direct traffic.

This is for avoiding a Samsung issue which will query and connect to Mainland websites such as qq.com, baidu.com, taobao.com etc for connectivity test, when the device is connected to WiFi.

Simply run the App, turn on the service, and copy the URL to the PAC URL setting of the WiFi connection (under Advanced settings)

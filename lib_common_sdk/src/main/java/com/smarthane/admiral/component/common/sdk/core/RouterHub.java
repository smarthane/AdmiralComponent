package com.smarthane.admiral.component.common.sdk.core;

/**
 * @author smarthane
 * @time 2019/11/10 14:19
 * @describe RouterHub ARouter中所有的路由必须定义在此 按分组层级进行管理
 */
public interface RouterHub {

    /*******************************************
     * 组名
     *****************************************/

    /**
     * 宿主 App 组件
     */
    String APP = "/app";

    /**
     * Common组件
     */
    String COMMON = "/common";

    /**
     * 知乎组件
     */
    String ZHIHU = "/zhihu";

    /**
     * 干货集中营组件
     */
    String GANK = "/gank";

    /**
     * 稀土掘金组件
     */
    String GOLD = "/gold";

    /**
     * 网易新闻
     */
    String NETEASE = "/netease";

    /****************************************
     * 服务组件, 用于给每个组件暴露特有的服务
     ***************************************/
    String SERVICE = "/service";

    /****************************************
     * ACTIVITY 组件
     ***************************************/
    String ACTIVITY = "/activity";

    /****************************************
     * FRAGMENT 组件
     ***************************************/
    String FRAGMENT = "/fragment";


    /****************************************
     * 宿主 App 分组
     ***************************************/
    String APP_SPLASHACTIVITY = APP + ACTIVITY + "/SplashActivity";

    String APP_PRETREATMENTSERVICE = APP + SERVICE + "/PretreatmentService ";
    String APP_MAINACTIVITY = APP + ACTIVITY + "/MainActivity";


    /****************************************
     * Common 分组
     ***************************************/
    String COMMON_SERVICE_COMMONINFOSERVICE = COMMON + SERVICE + "/CommonInfoService";

    String COMMON_HOMEACTIVITY = COMMON + ACTIVITY + "/HomeActivity";
    String COMMON_LOGINACTIVITY = COMMON + ACTIVITY + "/LoginActivity";
    String COMMON_SPLASHACTIVITY = COMMON + ACTIVITY + "/SplashActivity";

    /****************************************
     * 知乎分组
     ***************************************/
    String ZHIHU_SERVICE_ZHIHUINFOSERVICE = ZHIHU + SERVICE + "/ZhihuInfoService";

    String ZHIHU_HOMEACTIVITY = ZHIHU + ACTIVITY + "/HomeActivity";
    String ZHIHU_DETAILACTIVITY = ZHIHU + ACTIVITY + "/DetailActivity";

    /****************************************
     * 干货集中营分组
     ***************************************/
    String GANK_SERVICE_GANKINFOSERVICE = GANK + SERVICE + "/GankInfoService";

    String GANK_HOMEACTIVITY = GANK + ACTIVITY + "/HomeActivity";

    /****************************************
     * 稀土掘金分组
     ***************************************/
    String GOLD_SERVICE_GOLDINFOSERVICE = GOLD + SERVICE + "/GoldInfoService";

    String GOLD_HOMEACTIVITY = GOLD + ACTIVITY + "/HomeActivity";
    String GOLD_DETAILACTIVITY = GOLD + ACTIVITY + "/DetailActivity";


    /****************************************
     * 网易新闻分组
     ***************************************/
    String NETEASE_SERVICE_NETEASEINFOSERVICE = NETEASE + SERVICE + "/NeteaseInfoService";

    String NETEASE_HOMEACTIVITY = NETEASE + ACTIVITY + "/HomeActivity";
    String NETEASE_NEWSDETAILACTIVITY = NETEASE + ACTIVITY + "/NewsDetailActivity";
    String NETEASE_NEWSCHANNELACTIVITY = NETEASE + ACTIVITY + "/NewsChannelActivity";
}

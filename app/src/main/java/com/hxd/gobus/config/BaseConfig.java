package com.hxd.gobus.config;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: wangqingbin
 * @date: 2019/7/17 20:13
 */

public class BaseConfig {

    /****** 会话超时错误码 ********/
    public static final String CONVERSATION_TIMEOUT = "102000";

    /** 通讯录更新时间间隔 4个小时 */
    public static final long CONTACTS_UPDATE_TIME = 1 * 60 * 60 * 1000;

    public static final String DATABASE_NAME = "gobus.db";

    public static final String CONV_TITLE = "conv_title";
    public static final int IMAGE_MESSAGE = 1;
    public static final int TAKE_PHOTO_MESSAGE = 2;
    public static final int TAKE_LOCATION = 3;
    public static final int FILE_MESSAGE = 4;
    public static final int TACK_VIDEO = 5;
    public static final int TACK_VOICE = 6;
    public static final int BUSINESS_CARD = 7;
    public static final int REQUEST_CODE_SEND_FILE = 26;


    public static final int RESULT_CODE_ALL_MEMBER = 22;
    public static Map<Long, Boolean> isAtMe = new HashMap<>();
    public static Map<Long, Boolean> isAtall = new HashMap<>();
    public static List<Message> forwardMsg = new ArrayList<>();

    public static long registerOrLogin = 1;
    public static final int REQUEST_CODE_TAKE_PHOTO = 4;
    public static final int REQUEST_CODE_SELECT_PICTURE = 6;
    public static final int REQUEST_CODE_CROP_PICTURE = 18;
    public static final int REQUEST_CODE_CHAT_DETAIL = 14;
    public static final int RESULT_CODE_FRIEND_INFO = 17;
    public static final int REQUEST_CODE_ALL_MEMBER = 21;
    public static final int RESULT_CODE_EDIT_NOTENAME = 29;
    public static final String NOTENAME = "notename";
    public static final int REQUEST_CODE_AT_MEMBER = 30;
    public static final int RESULT_CODE_AT_MEMBER = 31;
    public static final int RESULT_CODE_AT_ALL = 32;
    public static final int SEARCH_AT_MEMBER_CODE = 33;

    public static final int RESULT_BUTTON = 2;
    public static final int START_YEAR = 1900;
    public static final int END_YEAR = 2050;
    public static final int RESULT_CODE_SELECT_FRIEND = 23;

    public static final int REQUEST_CODE_SELECT_ALBUM = 10;
    public static final int RESULT_CODE_SELECT_ALBUM = 11;
    public static final int RESULT_CODE_SELECT_PICTURE = 8;
    public static final int REQUEST_CODE_BROWSER_PICTURE = 12;
    public static final int RESULT_CODE_BROWSER_PICTURE = 13;
    public static final int RESULT_CODE_SEND_LOCATION = 25;
    public static final int RESULT_CODE_SEND_FILE = 27;
    public static final int REQUEST_CODE_SEND_LOCATION = 24;
    public static final int REQUEST_CODE_FRIEND_INFO = 16;
    public static final int RESULT_CODE_CHAT_DETAIL = 15;
    public static final int ON_GROUP_EVENT = 3004;
    public static final String DELETE_MODE = "deleteMode";
    public static final int RESULT_CODE_ME_INFO = 20;

    public static final String DRAFT = "draft";
    public static final String GROUP_ID = "groupId";
    public static final String POSITION = "position";
    public static final String MsgIDs = "msgIDs";
    public static final String NAME = "name";
    public static final String ATALL = "atall";
    public static final String SEARCH_AT_MEMBER_NAME = "search_at_member_name";
    public static final String SEARCH_AT_MEMBER_USERNAME = "search_at_member_username";
    public static final String SEARCH_AT_APPKEY = "search_at_appkey";

    public static final String MEMBERS_COUNT = "membersCount";

    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";
    public static final String JCHAT_CONFIGS = "JChat_configs";
    public static String FILE_DIR = "sdcard/JChatDemo/recvFiles/";
    public static String VIDEO_DIR = "sdcarVIDEOd/JChatDemo/sendFiles/";
    public static final String TARGET_ID = "targetId";
    public static final String ATUSER = "atuser";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static int maxImgCount;               //允许选择图片最大数
    public static final String GROUP_NAME = "groupName";

    public static List<GroupInfo> mGroupInfoList = new ArrayList<>();
    public static List<UserInfo> mFriendInfoList = new ArrayList<>();
    public static List<UserInfo> mSearchGroup = new ArrayList<>();
    public static List<UserInfo> mSearchAtMember = new ArrayList<>();
    public static List<Message> ids = new ArrayList<>();
    public static List<UserInfo> alreadyRead = new ArrayList<>();
    public static List<UserInfo> unRead = new ArrayList<>();
    public static List<String> forAddFriend = new ArrayList<>();


    /**
     * 待办事项来源
     */
    public static final int SOURCE_TODO = 1;
    /**
     * 查看审核详情来源
     */
    public static final int SOURCE_LOOK_DETAIL = 2;
    /**
     * 移动审批来源
     */
    public static final int SOURCE_MOBILE_APPROVAL = 3;

    /**
     * 代办事项标志
     */
    public static final String SIGN_TO_DO = "0";

    /**
     * 已办事项标志
     */
    public static final String SIGN_ALREADY_DO = "1";

    /**
     * 未提交
     */
    public static final String APPROVAL_STATUS_NOT_COMMIT = "0";

    /**
     * 审核中
     */
    public static final String APPROVAL_STATUS_IN_PROGRESS = "1";

    /**
     * 审核完成
     */
    public static final String APPROVAL_STATUS_COMPLETE = "2";


    /**
     * 用车审核流程类型
     */
    public static final String DATUMTYPE_VEHICLE = "120";
    /**
     * 维修审核流程类型
     */
    public static final String DATUMTYPE_REPAIR = "121";
    /**
     * 保养审核流程类型
     */
    public static final String DATUMTYPE_MAINTAIN = "122";

}

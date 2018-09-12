package com.mutoumulao.expo.redwood.util;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.mutoumulao.expo.redwood.base.BaseApp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static byte[] calculateRFC2104HMAC(String data, String key) throws SignatureException {
        byte[] rawHmac;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            rawHmac = mac.doFinal(data.getBytes());

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return rawHmac;
    }

    /**
     * 获取字符串的MD5值 StringToMD5
     *
     * @param str
     * @return
     * @since 1.0
     */
    public static String stringToMD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toLowerCase(Locale.getDefault());

    }


    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        //通过keySet获取所有的key集合
        Set<String> keySet = data.keySet();
        //将set转化为数组keyArray
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //数组升序排序
        Arrays.sort(keyArray);
        //构建StringBuilder字符串变量
        StringBuilder sb = new StringBuilder();
        //for循环key数组
        for (String k : keyArray) {
            //（重点1）如果数组中包含sign，则继续，不做字符串拼接操作
            /*if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }*/
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0)
                //字符串拼接形式：key1=value1&key2=value2
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        //（重点2）拼接密钥，参数上传的密钥
        sb.append("key=").append(key);
        //如果签名加密方式为MD5，则将字符串所有的英文字符转换为大写字母，再做MD5编码，返回md5加密结果
       /* if (SignType.MD5.equals(signType)) {
            //（重点3）返回加密结果字符串
            return MD5(sb.toString()).toUpperCase();
        }
        //如果签名加密方式为HMACSHA256，则直接将字符串和key密钥直接生成 HMACSHA256
        else if (SignType.HMACSHA256.equals(signType)) {
            //（重点3）返回加密结果字符串
            return HMACSHA256(sb.toString(), key);
        }
        //如果是其他加密方式，则报异常
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }*/
        return MD5Encode(sb.toString().toUpperCase(),"utf-8").toUpperCase();
    }

    /**
     * 判断是否为手机号码
     * isMobileNumber
     *
     * @param mobile
     * @return
     * @since 1.0
     */
    public static boolean isMobileNumber(String mobile) {
//        Pattern pattern = Pattern.compile("\\d{11}$");
        Pattern pattern = Pattern.compile("^[1][0-9]{10}$");
        return pattern.matcher(mobile).find();
    }

    public static boolean isPasswordValidate(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.length() >= 6 && mobile.length() <= 18;
    }

    public static boolean isVerifyCodeValidate(String code) {
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        return pattern.matcher(code).find();
    }

    /**
     * 判断密码是否过于简单
     *
     * @param pwd
     * @return
     */
    public static boolean isPasswordLeak(String pwd) {
        return TextUtils.isEmpty(pwd) || isNumeric(pwd);
    }

    /**
     * 给手机号局部隐藏
     * decodeMobile
     *
     * @param mobile
     * @return
     * @since 1.0
     */
    public static String decodeMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return mobile;
        }
        if (mobile.length() > 8) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mobile.length(); i++) {
                if (i >= mobile.length() - 8 && i < mobile.length() - 4) {
                    sb.append("*");
                } else {
                    sb.append(mobile.charAt(i));
                }
            }
            return sb.toString();
        }
        return mobile;
    }

    /**
     * 保留2位小数
     *
     * @param f
     * @return
     */
    public static String doubleKeepTwo(double f) {
        return String.format("%.2f", f);
    }

    /**
     * 取字符串的前toCount个字符
     *
     * @param str       被处理字符串
     * @param byteCount 截取长度
     * @return
     * @since 3.6
     */
    public static String subStringByCharCount(String str, int byteCount) {
        int reInt = 0;
        String reStr = "";
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        for (int i = 0; (i < tempChar.length && byteCount > reInt); i++) {
            String s1 = String.valueOf(tempChar[i]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            if (reInt > byteCount) {
                break;
            } else {
                reStr += tempChar[i];
            }
        }
        return reStr;
    }

    /**
     * 返回字符串，参数为null时，返回空字符串
     *
     * @param str
     * @return
     */
    public static String toStringEx(Object str) {
        if (str == null) {
            return "";
        }

        return str.toString();
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsEx(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 != null) {
            return str1.equals(str2);
        }
        return false;
    }

    /**
     * 判断是否位数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * com.wondersgroup.healthcloud://patient/member_group_mood?uid=8a81c0194e2ed130014e58a165b90011&message=哎哟，%s看上去心情不错哦！&mood=0
     * 获取URL中parameter指定的指
     *
     * @param url       URL地址
     * @param parameter 参数
     * @return
     */
    public static String getUrlParameter(String url, String parameter) {
        Pattern p = Pattern.compile(parameter + "=([^&]*)(&|$)");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static String convertAppointmentCount(int appointmentCount, int offset) {
        if (appointmentCount < offset) {
            return String.valueOf(appointmentCount);
        } else {
            double computeValue = (appointmentCount / 10000) + (appointmentCount % 10000 / 10000.00);
            BigDecimal b = new BigDecimal(computeValue);
            double finalValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return finalValue + "万";
        }
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 加过*手机号
     *
     * @param mobile
     * @return
     */
    public static String getCheckedMobile(String mobile) {
        try {
            return mobile.substring(0, mobile.length() - (mobile.substring(3)).length()) + "****" + mobile.substring(7);
        } catch (Exception e) {
            return "";
        }
    }

    // 判断某个字符串是否是float字符串，若是返回0，若不是则返回-1
    public static boolean isFloathString(String testString) {
        if (!testString.contains(".")) {
            return isNumeric(testString);
        } else {
            String[] floatStringPartArray = testString.split("\\.");
            if (floatStringPartArray.length == 2) {
                if (isNumeric(floatStringPartArray[0]) && isNumeric(floatStringPartArray[1]))
                    return true;
                else
                    return false;
            } else
                return false;

        }

    }


    /**
     * 根据身份编号获取年龄
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        String ymd = idCard.substring(6, 14);
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");

        try {
            Date mydate = myFormatter.parse(ymd);
            return getAgeByBirthday(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 给身份证号局部隐藏
     * decodeMobile
     *
     * @param idCard 身份证号
     * @return
     * @since 1.0
     */
    public static String decodeIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        if (idCard.length() > 3) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < idCard.length(); i++) {
                if (i >= 4 && i < idCard.length() - 4) {
                    sb.append("*");
                } else {
                    sb.append(idCard.charAt(i));
                }
            }
            return sb.toString();
        }
        return idCard;
    }

    /**
     * 根据当前时间计算血糖所在周期
     *
     * @return
     */
    public static Integer getCurrPeriod() {
        try {
            Map<Integer, List<Date>> TimeMap = initTime();
            Date now = new Date();
            Iterator<Integer> iter = TimeMap.keySet().iterator();
            while (iter.hasNext()) {
                int key = iter.next();
                List<Date> lst = TimeMap.get(key);
                Date before = lst.get(0);
                Date after = lst.get(1);
                if (now.after(before) && now.before(after)) {
                    return key;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 初始化个时间段范围
     */
    public static Map<Integer, List<Date>> initTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<Integer, List<Date>> map = new HashMap();
        GregorianCalendar ca = new GregorianCalendar();
        int y = ca.get(Calendar.YEAR);
        int m = ca.get(Calendar.MONTH) + 1;
        int d = ca.get(Calendar.DAY_OF_MONTH);
        try {
            Date d0 = sf.parse(y + "-" + m + "-" + d + " " + "00:00:00");
            Date d5 = sf.parse(y + "-" + m + "-" + d + " " + "05:00:00");//3.0版本后来去掉了凌晨
            Date d8 = sf.parse(y + "-" + m + "-" + d + " " + "08:00:00");
            Date d10 = sf.parse(y + "-" + m + "-" + d + " " + "10:00:00");
            Date d12 = sf.parse(y + "-" + m + "-" + d + " " + "12:00:00");
            Date d14 = sf.parse(y + "-" + m + "-" + d + " " + "15:00:00");
            Date d16 = sf.parse(y + "-" + m + "-" + d + " " + "18:00:00");
            Date d18 = sf.parse(y + "-" + m + "-" + d + " " + "20:00:00");
            Date d19 = sf.parse(y + "-" + m + "-" + d + " " + "24:00:00");
            List<Date> a0 = new ArrayList<Date>();
            a0.add(d0);
            a0.add(d5);
            map.put(7, a0);

            List<Date> a1 = new ArrayList<Date>();
            a1.add(d0);
            a1.add(d8);
            map.put(0, a1);

            List<Date> a2 = new ArrayList<Date>();
            a2.add(d8);
            a2.add(d10);
            map.put(1, a2);

            List<Date> a3 = new ArrayList<Date>();
            a3.add(d10);
            a3.add(d12);
            map.put(2, a3);

            List<Date> p1 = new ArrayList<Date>();
            p1.add(d12);
            p1.add(d14);
            map.put(3, p1);

            List<Date> p2 = new ArrayList<Date>();
            p2.add(d14);
            p2.add(d16);
            map.put(4, p2);

            List<Date> p3 = new ArrayList<Date>();
            p3.add(d16);
            p3.add(d18);
            map.put(5, p3);

            List<Date> p4 = new ArrayList<Date>();
            p4.add(d18);
            p4.add(d19);
            map.put(6, p4);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 给身份证号局部隐藏前四 后四
     * decodeMobile
     *
     * @param idCard 身份证号
     * @return
     * @since 1.0
     */
    public static String decode4IdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        return decodeString(idCard, 4, 4);
    }

    /**
     * 给姓名隐藏
     *
     * @param name 姓名
     */
    public static String decodeName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() == 2) {
            return decodeString(name, 1, 0);
        }
        if (name.length() > 2) {
            return decodeString(name, 1, 1);
        }
        return name;

    }

    public static String decodeString(String str, int frontLen, int endLen) {
        int len = str.length() - frontLen - endLen;
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, frontLen));
        for (int i = 0; i < len; i++) {
            sb.append("*");
        }
        sb.append(str.substring(str.length() - endLen));
        return sb.toString();
    }


    public static String getTime() {
        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        int second = time.get(Calendar.SECOND);
        return hour + ":" + minute + ":" + second;
    }

    /**
     * 根据位数隐藏数据
     *
     * @param idCard
     * @param start
     * @param end
     * @return
     */
    public static String decodeIdCardByNum(String idCard, int start, int end) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        if (idCard.length() > start) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < idCard.length(); i++) {
                if (i >= start && i < idCard.length() - end) {
                    sb.append("*");
                } else {
                    sb.append(idCard.charAt(i));
                }
            }
            return sb.toString();
        }
        return idCard;
    }

    /**
     * 根据身份证号判断性别
     *
     * @param idCard
     * @return
     */
    public static String getGenderByIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        String gender = idCard.substring(16, 17);
        if (Integer.parseInt(gender) % 2 == 0) {
            gender = "女";
        } else {
            gender = "男";

        }
        return gender;
    }

    /**
     * 格式化json
     *
     * @param jsonStr 字符串json
     * @return json 格式等字符串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append("\n");
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append("\n");
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append("\n");
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }

        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("\t");
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   //uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


    /**
     * 获取字符串数组资源
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取Resource对象
     */
    public static Resources getResources() {
        return BaseApp.getApp().getResources();
    }


    // 给edittext控件控件设置光标在文字的后边
    public static void setSelectionEnd(EditText editText) {
        Editable b = editText.getText();
        editText.setSelection(b.length());
    }

}

package com.mutoumulao.expo.redwood.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2015/7/6.
 */
public class BaseListResponse<T> {
//    public boolean more;
//    public HashMap<String, String> more_params;
//    public JSONArray content;
//    private List<T> data;
    public JSONArray data;

    protected Class<T> mType;
    public JSONObject extras;
    private List<T> array;

    public BaseListResponse() {
        Type genType = getClass().getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            mType = ((Class<T>) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
    }

    public List<T> getList() {
        if (array == null && data == null) {
            array = new ArrayList<>();
            return array;
        }
        if (data == null) {
            array = JSON.parseArray(data.toString(), mType);
        }
        if (data == null) {
            array = new ArrayList<>();
        }
        return array;
    }

    public boolean isListEmpty() {
        return getList() == null || getList().isEmpty();
    }

    public <T> T getExtraData(Class<T> clazz) {
        if (extras == null) {
            return null;
        }
        return JSON.parseObject(extras.toString(), clazz);
    }

/*    public int refresh(int type, BaseListResponse<T> t) {
        if (t == null) {
            if (type != Constant.TYPE_NEXT) {
                more = false;
                more_params = null;
                content = null;
                data = null;
                extras = null;
            }
            return 0;
        }
        more = t.more;
        more_params = t.more_params;
        extras = t.extras;
        if (type == Constant.TYPE_NEXT) {
            getList().addAll(t.getList());
            return t.getList().size();
        } else {
            data = t.getList();
            return 0;
        }
    }*/


   /* @Override
    public String toString() {
        return "BaseListResponse{" +
                "content=" + content +
                ", more_params=" + more_params +
                ", more=" + more +
                '}';
    }*/
}

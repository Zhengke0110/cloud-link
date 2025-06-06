package fun.timu.cloud.net.app.func;

import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class VistorMapFunction extends RichMapFunction<JSONObject, String> {

    //记录用户的udid访问
    private ValueState<String> newDayVisitorState;

    @Override
    public void open(Configuration parameters) throws Exception {
        //对状态做初始化
        newDayVisitorState = getRuntimeContext().getState(new ValueStateDescriptor<String>("newDayVisitorState", String.class));
    }

    /**
     * 重写map方法处理访客数据
     * 该方法主要用于识别新老访客，并在处理后将结果转换为JSON字符串返回
     *
     * @param value 输入的访客数据，包含访问时间戳等信息
     * @return 处理后的访客数据JSON字符串，包括新老访客标识
     * @throws Exception 如果处理过程中发生错误，抛出异常
     */
    @Override
    public String map(JSONObject value) throws Exception {
        //获取之前是否有访问日期
        String beforeDateState = newDayVisitorState.value();

        //获取当前访问时间戳
        Long ts = value.getLong("ts");

        //将时间戳格式化为日期字符串
        String currentDateStr = TimeUtil.format(ts);

        //判断日期是否为空进行新老访客识别
        if (StringUtils.isNotBlank(beforeDateState)) {
            //如果之前有访问日期，比较上次访问日期和当前访问日期
            if (beforeDateState.equalsIgnoreCase(currentDateStr)) {
                //一样则是老访客
                value.put("is_new", 0);
                log.info("老访客:{}", currentDateStr);
            } else {
                //时间不一样，则是新用户,标记1，老访客标记0
                value.put("is_new", 1);
                //更新最新访问日期为当前日期
                newDayVisitorState.update(currentDateStr);
                log.info("新访客:{}", currentDateStr);
            }
        } else {
            //如果状态为空，则是新用户,标记1，老访客标记0
            value.put("is_new", 1);
            //更新最新访问日期为当前日期
            newDayVisitorState.update(currentDateStr);
            log.info("新访客:{}", currentDateStr);
        }

        //将处理后的访客数据转换为JSON字符串返回
        return value.toJSONString();
    }
}


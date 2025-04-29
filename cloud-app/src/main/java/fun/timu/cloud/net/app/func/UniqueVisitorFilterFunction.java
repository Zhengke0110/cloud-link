package fun.timu.cloud.net.app.func;

import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

/**
 * UniqueVisitorFilterFunction是一个用于过滤唯一访问者的类
 * 它通过比较用户每次访问的时间来判断用户是否为重复访问者
 * 主要通过Flink的状态编程接口来保存用户最后一次访问的时间，并在每次访问时进行比较
 */
public class UniqueVisitorFilterFunction extends RichFilterFunction<JSONObject> {

    // 定义一个状态变量来保存用户最后一次访问的时间
    private ValueState<String> lastVisitDateState = null;

    /**
     * open方法是初始化方法，用于在执行filter方法之前初始化状态变量
     *
     * @param parameters 配置参数，通常未使用
     * @throws Exception 如果初始化过程中出现错误，抛出异常
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        // 创建一个描述符来定义状态的类型和名称
        ValueStateDescriptor<String> visitDateStateDes = new ValueStateDescriptor<>("visitDateState", String.class);

        // 配置状态的存活时间，这里设置为1天
        // 这意味着如果一条数据在一天内没有被访问，那么它将被自动清除
        StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.days(1)).build();
        //StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.seconds(15)).build();

        // 为状态描述符启用存活时间配置
        visitDateStateDes.enableTimeToLive(stateTtlConfig);

        // 获取运行时上下文中的状态实例
        this.lastVisitDateState = getRuntimeContext().getState(visitDateStateDes);
    }

    /**
     * close方法用于释放资源，通常未使用
     *
     * @throws Exception 如果关闭过程中出现错误，抛出异常
     */
    @Override
    public void close() throws Exception {
        super.close();
    }

    /**
     * filter方法用于判断一个访问者是否为唯一访问者
     *
     * @param jsonObj 包含访问信息的JSON对象，包括访问时间和设备ID
     * @return 如果访问者为唯一访问者，返回true；否则返回false
     * @throws Exception 如果处理过程中出现错误，抛出异常
     */
    @Override
    public boolean filter(JSONObject jsonObj) throws Exception {
        // 获取当前访问时间
        Long visitTime = jsonObj.getLong("visitTime");
        String udid = jsonObj.getString("udid");

        // 当前访问时间
        String currentVisitDate = TimeUtil.format(visitTime);

        // 获取上次的状态访问时间
        String lastVisitDate = lastVisitDateState.value();

        // 用当前页面的访问时间和状态时间进行对比
        if (StringUtils.isNotBlank(lastVisitDate) && currentVisitDate.equalsIgnoreCase(lastVisitDate)) {
            // 如果访问者在同一天已经访问过，则不认为是唯一访问者
            System.out.println(udid + " 已经在 " + currentVisitDate + "时间访问过");
            return false;
        } else {
            // 如果是新的访问或者访问时间不在同一天，则更新状态并认为是唯一访问者
            System.out.println(udid + " 在 " + currentVisitDate + "时间初次访问");
            return true;
        }
    }
}

package fun.timu.cloud.net.app.func;

import fun.timu.cloud.net.app.model.DO.DeviceInfo;
import fun.timu.cloud.net.app.model.DO.ShortLinkWide;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.util.DeviceUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class DeviceMapFunction implements MapFunction<String, ShortLinkWide> {
    /**
     * 重写map方法以处理输入字符串并生成ShortLinkWide对象
     * 该方法主要负责解析输入的JSON字符串，提取相关信息，并根据用户代理信息推断设备信息，
     * 最后将所有这些信息封装到一个ShortLinkWide对象中，以便于短链的基本信息和设备信息的宽表配置
     *
     * @param value 包含短链访问信息的JSON字符串
     * @return ShortLinkWide对象，其中包含了从输入字符串中提取和推断的所有相关信息
     * @throws Exception 如果解析JSON或处理设备信息时发生错误
     */
    @Override
    public ShortLinkWide map(String value) throws Exception {

        //还原json
        JSONObject jsonObject = JSON.parseObject(value);

        //提取用户代理信息
        String userAgent = jsonObject.getJSONObject("data").getString("user-agent");

        //根据用户代理信息获取设备信息
        DeviceInfo deviceInfoDO = DeviceUtil.getDeviceInfo(userAgent);

        //提取udid并设置到设备信息对象中
        String udid = jsonObject.getString("udid");
        deviceInfoDO.setUdid(udid);

        //配置短链基本信息宽表
        ShortLinkWide shortLinkWideDO = ShortLinkWide.builder()
                //短链访问基本信息
                .visitTime(jsonObject.getLong("ts"))
                .accountNo(jsonObject.getJSONObject("data").getLong("accountNo"))
                .code(jsonObject.getString("bizId"))
                .referer(jsonObject.getString("referer"))
                .isNew(jsonObject.getInteger("is_new"))
                .ip(jsonObject.getString("ip"))

                //设备信息补齐
                .browserName(deviceInfoDO.getBrowserName())
                .os(deviceInfoDO.getOs())
                .osVersion(deviceInfoDO.getOsVersion())
                .deviceType(deviceInfoDO.getDeviceType())
                .deviceManufacturer(deviceInfoDO.getDeviceManufacturer())
                .udid(deviceInfoDO.getUdid())

                .build();

        //返回配置好的ShortLinkWide对象
        return shortLinkWideDO;
    }
}

export const Data = {
    "accountNo": -1,
    "headImg": "https://loremflickr.com/400/400?lock=8354802214694823",
    "phone": "",
    "mail": "",
    "username": "未登录",
    "auth": "",
    "create_time": ""
}

// 账户统计数据
export const AccountStats = [
    {
        id: "created_links",
        title: "创建短链",
        value: 0,
        icon: "link",
        color: "blue"
    },
    {
        id: "link_clicks",
        title: "短链点击",
        value: 0,
        icon: "click",
        color: "cyan"
    },
    {
        id: "created_groups",
        title: "创建群组",
        value: 0,
        icon: "group",
        color: "teal"
    },
    {
        id: "membership_days",
        title: "会员剩余",
        value: 0,
        unit: "天",
        icon: "time",
        color: "indigo"
    }
]



// 定义数据接口
export interface UserData {
    username: string;
    headImg: string;
    auth: string;
    create_time: string;
    accountNo: string | number;
    mail: string;
    phone: string;
    [key: string]: any; // 允许其他可能的字段
}

export interface AccountStat {
    id: number | string;
    title: string;
    value: number | string;
    color: string;
    icon: string;
    unit?: string;
}
export interface Traffic {
    id: string | number;
    level: string;
    productId: string | number;
    pluginType: string;
    dayUsed: number;
    dayLimit: number;
    expiredDate: string;
    outTradeNo: string;
}

export interface TrafficDataType {
    total_record: number;
    current_data: Traffic[];
}
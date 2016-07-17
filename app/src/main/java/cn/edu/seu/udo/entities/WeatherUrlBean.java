package cn.edu.seu.udo.entities;

import java.util.List;

/*
 *desc 
 *author rhg
 *time 2016/6/6 11:08
 *email 1013773046@qq.com
 */
public class WeatherUrlBean {

    /**
     * desc : OK
     * status : 1000
     * data : {"wendu":"26","ganmao":"鍚勯」姘旇薄鏉\u2032欢閫傚疁锛屽彂鐢熸劅鍐掓満鐜囪緝浣庛\u20ac備絾璇烽伩鍏嶉暱鏈熷浜庣┖璋冩埧闂翠腑锛屼互闃叉劅鍐掋\u20ac�","forecast":[{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 31鈩�","type":"澶氫簯","low":"浣庢俯 20鈩�","date":"6鏃ユ槦鏈熶竴"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 24鈩�","type":"闆烽樀闆�","low":"浣庢俯 16鈩�","date":"7鏃ユ槦鏈熶簩"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 32鈩�","type":"鏅�","low":"浣庢俯 21鈩�","date":"8鏃ユ槦鏈熶笁"},{"fengxiang":"鍗楅","fengli":"3-4绾�","high":"楂樻俯 33鈩�","type":"澶氫簯","low":"浣庢俯 22鈩�","date":"9鏃ユ槦鏈熷洓"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 30鈩�","type":"闆烽樀闆�","low":"浣庢俯 20鈩�","date":"10鏃ユ槦鏈熶簲"}],"yesterday":{"fl":"寰","fx":"鏃犳寔缁鍚�","high":"楂樻俯 30鈩�","type":"澶氫簯","low":"浣庢俯 18鈩�","date":"5鏃ユ槦鏈熸棩"},"aqi":"122","city":"鍖椾含"}
     */

    private String desc;
    private int status;
    /**
     * wendu : 26
     * ganmao : 鍚勯」姘旇薄鏉′欢閫傚疁锛屽彂鐢熸劅鍐掓満鐜囪緝浣庛€備絾璇烽伩鍏嶉暱鏈熷浜庣┖璋冩埧闂翠腑锛屼互闃叉劅鍐掋€�
     * forecast : [{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 31鈩�","type":"澶氫簯","low":"浣庢俯 20鈩�","date":"6鏃ユ槦鏈熶竴"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 24鈩�","type":"闆烽樀闆�","low":"浣庢俯 16鈩�","date":"7鏃ユ槦鏈熶簩"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 32鈩�","type":"鏅�","low":"浣庢俯 21鈩�","date":"8鏃ユ槦鏈熶笁"},{"fengxiang":"鍗楅","fengli":"3-4绾�","high":"楂樻俯 33鈩�","type":"澶氫簯","low":"浣庢俯 22鈩�","date":"9鏃ユ槦鏈熷洓"},{"fengxiang":"鏃犳寔缁鍚�","fengli":"寰绾�","high":"楂樻俯 30鈩�","type":"闆烽樀闆�","low":"浣庢俯 20鈩�","date":"10鏃ユ槦鏈熶簲"}]
     * yesterday : {"fl":"寰","fx":"鏃犳寔缁鍚�","high":"楂樻俯 30鈩�","type":"澶氫簯","low":"浣庢俯 18鈩�","date":"5鏃ユ槦鏈熸棩"}
     * aqi : 122
     * city : 鍖椾含
     */

    private DataBean data;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String wendu;
        private String ganmao;
        /**
         * fl : 寰
         * fx : 鏃犳寔缁鍚�
         * high : 楂樻俯 30鈩�
         * type : 澶氫簯
         * low : 浣庢俯 18鈩�
         * date : 5鏃ユ槦鏈熸棩
         */

        private YesterdayBean yesterday;
        private String aqi;
        private String city;
        /**
         * fengxiang : 鏃犳寔缁鍚�
         * fengli : 寰绾�
         * high : 楂樻俯 31鈩�
         * type : 澶氫簯
         * low : 浣庢俯 20鈩�
         * date : 6鏃ユ槦鏈熶竴
         */

        private List<ForecastBean> forecast;

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            private String fl;
            private String fx;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
            /*private String fl;
            private String fx;
            private String high;
            private String type;
            private String low;
            private String date;*/

            @Override
            public String toString() {
                return "fl: " + fl + " fx: " + fx + " high: " + high + " type: " + type + " low: " + low + " date: " + date;
            }
        }

        public static class ForecastBean {
            private String fengxiang;
            private String fengli;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            /*private String fengxiang;
            private String fengli;
            private String high;
            private String type;
            private String low;
            private String date;*/
            @Override
            public String toString() {
                return "fengxiang: " + fengxiang + " fengli: " + fengli + " high: " + high
                        + " type: " + type + " low: " + low + " date: " + date;
            }
        }
       /* private String wendu;
        private String ganmao;
        private YesterdayBean yesterday;
        private String aqi;
        private String city;
        private List<ForecastBean> forecast;*/

        @Override
        public String toString() {
            return "wendu: " + wendu + "\n ganmao: " + ganmao + "\n yesterday: " + yesterday
                    + "\n aqi: " + aqi + "\n city: " + city
                    + "\n forecast: " + forecast;
        }
    }
}

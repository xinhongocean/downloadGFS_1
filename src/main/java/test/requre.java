//package test;
//
//
//
///**
// * Created by Administrator on 2018/5/3.
// */
//public class requre {
//    public requre() {
//    }
//    private String getWeatherFromXinhong(DateTime dateTime, float lat, float lon) {
//        String url = "http://weather.xinhong.net/stationdata_cityfc/datafromlatlng?lat="
//                + lat + "&lng=" + lon + "&elem=WW";
//        try {
//            URL localURL = new URL(url);
//            URLConnection connection = localURL.openConnection();
//            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
//            httpURLConnection.setRequestProperty("Accept", "application/json");
//            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
//            connection.connect();
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String s = in.readLine();
//            JSONObject jsonObject = JSON.parseObject(s);
//            in.close();
//            httpURLConnection.disconnect();
//            Object obj = jsonObject.get("data");
//            Object tobj=jsonObject.get("time");
//            if(tobj==null)return null;
//            String rtime = (String) jsonObject.get("time");
//            DateTimeFormatter dateformat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
//            DateTime runtime = dateformat.parseDateTime(rtime);
//            int difhour = Math.round((dateTime.getMillis() -
//                    runtime.getMillis()) / 3600000.f);
//            String res = null;
//            if (obj != null) {
//                if (obj instanceof JSONObject) {
//                    if (difhour <= 48)
//                        res = (String) ((JSONObject) obj).get(
//                                String.valueOf((int) Math.ceil(difhour / 3.) * 3));
//                    else if (difhour <= 96)
//                        res = (String) ((JSONObject) obj).get(String.valueOf((int) Math.ceil(difhour / 6.) * 6));
//                    else if (difhour <= 168)
//                        res = (String) ((JSONObject) obj).get(String.valueOf((int) Math.ceil(difhour / 12.) * 12));
//                }
//            }

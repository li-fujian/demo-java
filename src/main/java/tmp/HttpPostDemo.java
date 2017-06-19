package tmp;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpPostDemo {
    
    public static final Gson gson = new GsonBuilder().create();
    
    public static boolean sendToInboxByBatch(Object message) {
        
        StringBuilder url = new StringBuilder();

        url.append("http://es.wilddog.cn/stat-20161227/conn/_search");

        String json = gson.toJson(message);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(url.toString());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(3000).build();
        httpPatch.setConfig(requestConfig);

        try {
            StringEntity entity = new StringEntity(json, ContentType.create("text/plain", "UTF-8"));
            httpPatch.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPatch);

            if (response.getStatusLine().getStatusCode() == 200) {

                System.out.println("sendToInboxByBatch success. ");

                String result = EntityUtils.toString(response.getEntity());
                System.out.println("EntityUtils is : "+ result);
                JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
                JsonArray languages = returnData.getAsJsonObject("hits").getAsJsonArray("hits");
                int count = 0;
                int totCount = 0;
                
                Map<String, Integer> map = new HashMap<String, Integer>(); 
                for (JsonElement jsonElement : languages) {
                    JsonObject language = jsonElement.getAsJsonObject().getAsJsonObject("_source");
                    String appId = language.get("appid").getAsString();
                    int coun = language.get("val").getAsInt();
//                    System.out.println(language.get("appid").getAsString()+"--:--"+language.get("val").getAsInt());
                    if (map.get(appId) != null) {
                        int counMap = map.get(appId);
                        coun = counMap > coun ? counMap : coun;
                    }
                    map.put(appId, coun);
                    
                    totCount+=coun;
//                    System.out.println(language.get("val").getAsInt());
                }
                
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    System.out.println(MessageFormat.format("{0}={1}",entry.getKey(),entry.getValue()));
                    count++;
                }
                
                System.out.println(count);
                System.out.println("--totCount--"+totCount);

                return true;
            } else {
                System.out.println("Error sendToInboxByBatch,  [StatusCode :{}] " + response.getStatusLine().getStatusCode());

                return false;
            }
        } catch (Exception e) {
            System.out.println("1111 sendToInboxByBatch response faild.");
            System.out.println(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                
                System.out.println("sendToInboxByBatch response faild. appId : {}");
                System.out.println(e);
                
            }
        }

        return false;
    }

    public static void main(String[] args) {
        String json = "{\"query\":{\"bool\":{\"filter\":[{\"range\":{\"ts\":{\"gt\":1482826690000,\"lt\":1482826750000}}},{\"range\":{\"val\":{\"gt\":\"0\"}}}]}},\"size\":1000,\"aggs\":{\"pids\":{\"terms\":{\"field\":\"appid\"},\"aggs\":{\"pid_avg\":{\"avg\":{\"field\":\"val\"}}}},\"sum_pid_conn\":{\"sum_bucket\":{\"buckets_path\":\"pids>pid_avg\"}}}}";
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(json);
        boolean result = HttpPostDemo.sendToInboxByBatch(object);
        System.out.println(result);
    }
}
